package javaNIO;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

class Attachment {
	
	private AsynchronousFileChannel asyncFileChannel;
	private long iteration;
	
	public Attachment(AsynchronousFileChannel asyncFileChannel) {
		this.asyncFileChannel = asyncFileChannel;
	}

	public long getIteration() {
		return iteration;
	}


	public void setIteration(long iteration) {
		this.iteration = iteration;
	}


	public AsynchronousFileChannel getAsyncFileChannel() {
		return asyncFileChannel;
	}

	public void setAsyncFileChannel(AsynchronousFileChannel asyncFileChannel) {
		this.asyncFileChannel = asyncFileChannel;
	}
	
}

public class Exam10_AsynchronousFileChannel extends Application {

	TextArea textarea;
	Button simpleRead, readBtn, writeBtn;
	File file;
	Path path;
	Charset charset = Charset.forName("UTF-8");
	final int bufferSize = 100;
	//final int bufferSize = 1024*1024*5;
	
	ExecutorService executorService = 
			Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	

	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name);
		});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);

		// 저용량 Read ( buffer의 크기를 파일크기와 동일하게 처리 )
		simpleRead = new Button("비동기 File Read(Simple)");
		simpleRead.setPrefSize(200, 40);
		simpleRead.setOnAction(e->{
			textarea.clear();
			path = Paths.get("asset/readme.txt");
			
			try {
				AsynchronousFileChannel asyncFileChannel = AsynchronousFileChannel.open(path, 
						EnumSet.of(StandardOpenOption.READ),
						executorService);
				
				ByteBuffer buffer = ByteBuffer.allocate((int)asyncFileChannel.size());
				Attachment attach = new Attachment(asyncFileChannel);
				
				asyncFileChannel.read(buffer, 0, attach, new CompletionHandler<Integer, Attachment>() {

					@Override
					public void completed(Integer result, Attachment attachment) {
						buffer.flip();
						String msg = charset.decode(buffer).toString();
						printMsg(msg + "\n");
						try {
							attachment.getAsyncFileChannel().close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						printMsg("Simpe File Read 종료!! \n");
					}

					@Override
					public void failed(Throwable exc, Attachment attachment) {
						
					}					
				});
				printMsg("Simpe File Read 실행.(비동기) \n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		// 고용량 Read ( buffer의 크기가 파일크기보다 작은경우 ) 
		readBtn = new Button("비동기 File Read");
		readBtn.setPrefSize(150, 40);
		readBtn.setOnAction(e->{
			textarea.clear();
			path = Paths.get("asset/readme.txt");
			
			try {
				AsynchronousFileChannel asyncFileChannel = AsynchronousFileChannel.open(path, 
						EnumSet.of(StandardOpenOption.READ),
						executorService);
				
				ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
				Attachment attach = new Attachment(asyncFileChannel);
				
				long fileSize = asyncFileChannel.size();
				
				asyncFileChannel.read(buffer, 0, attach, new CompletionHandler<Integer, Attachment>() {

					@Override
					public void completed(Integer result, Attachment attachment) {
						long iterator = attachment.getIteration();
						printMsg("현재 Thread : " + Thread.currentThread().getName() + ", " + iterator + "회차 반복 \n");
						
						buffer.flip();
						//printMsg(charset.decode(buffer).toString());
						buffer.clear();
						
						if(result == fileSize || result < buffer.capacity()) {
							printMsg("파일 읽기 완료! \n");
							return;
						}
						
						attachment.setIteration(attachment.getIteration() + 1);
						asyncFileChannel.read(buffer, result*attachment.getIteration(), attachment, this);
					}

					@Override
					public void failed(Throwable exc, Attachment attachment) {
						
					}					
				});

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		writeBtn = new Button("비동기 File Write");
		writeBtn.setPrefSize(150, 40);
		writeBtn.setOnAction(e->{
			textarea.clear();
			path = Paths.get("asset/readmeBack.txt");
			
			try {
				AsynchronousFileChannel asyncFileChannel = AsynchronousFileChannel.open(path, 
						EnumSet.of(StandardOpenOption.CREATE,StandardOpenOption.WRITE),
						executorService);
				
				Attachment attach = new Attachment(asyncFileChannel);
							
				String msg = "이것은 소리없는 아우성!!";
				ByteBuffer buffer = charset.encode(msg);
				
				asyncFileChannel.write(buffer, 0, attach, new CompletionHandler<Integer, Attachment>() {

					@Override
					public void completed(Integer result, Attachment attachment) {
						try {
							printMsg("파일 Write Complete!! \n");
							attachment.getAsyncFileChannel().close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void failed(Throwable exc, Attachment attachment) {
						
					}					
				});
				printMsg("File Write 실행.(비동기) \n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		FlowPane flowPane = new FlowPane();
     	flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(simpleRead);
		flowPane.getChildren().add(readBtn);
		flowPane.getChildren().add(writeBtn);
				
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("비동기 파일 Read & Write");
		primaryStage.setOnCloseRequest(e->{
			executorService.shutdown();
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	
}