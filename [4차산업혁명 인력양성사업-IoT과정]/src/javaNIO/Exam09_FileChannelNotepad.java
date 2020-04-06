package javaNIO;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Exam09_FileChannelNotepad extends Application {

	TextArea textarea;
	Button openBtn, saveBtn, directCopyBtn, nonDirectCopyBtn;
	File file;
	Path path;
	ByteBuffer buffer;
	Charset charset = Charset.forName("UTF-8");
	FileChannel fileChannel;
	final int bufferSize = 5*1024*1024; 
	
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
		
		openBtn = new Button("파일 열기");
		openBtn.setPrefSize(150, 40);
		openBtn.setOnAction(e->{
			textarea.clear();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Text File");
			file = fileChooser.showOpenDialog(primaryStage);
			path = file.toPath();			
			try {
				fileChannel = FileChannel.open(path, StandardOpenOption.READ);
				buffer = ByteBuffer.allocate((int)fileChannel.size());
				fileChannel.read(buffer);
				buffer.flip();
				String msg = charset.decode(buffer).toString();
				printMsg(msg);
				fileChannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		saveBtn = new Button("파일 저장");
		saveBtn.setPrefSize(150, 40);
		saveBtn.setOnAction(e->{
			String text = textarea.getText();
			try {
				fileChannel = FileChannel.open(path, StandardOpenOption.WRITE);
				buffer = charset.encode(text);
				fileChannel.write(buffer);
				fileChannel.close();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("파일저장");
				alert.setHeaderText("File SAVE");
				alert.setContentText("파일저장 성공!!");
				alert.showAndWait();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		directCopyBtn = new Button("파일 복사(direct)");
		directCopyBtn.setPrefSize(150, 40);
		directCopyBtn.setOnAction(e->{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Original File");
			file = fileChooser.showOpenDialog(primaryStage);
			path = file.toPath();		
			fileChooser.setTitle("Copy File");
			File copyFile = fileChooser.showSaveDialog(primaryStage);
			Path copyPath = copyFile.toPath();
			
			try {
				fileChannel = FileChannel.open(path, StandardOpenOption.READ);
				FileChannel copyFileChannel = 
						FileChannel.open(copyPath, StandardOpenOption.CREATE,
								StandardOpenOption.WRITE);
				int bytes = 0;
				buffer = ByteBuffer.allocateDirect(bufferSize);
				long start=0, end=0;
				start = System.nanoTime();
				while(true) {					
					bytes = fileChannel.read(buffer);
					if( bytes == -1 ) {
						break;
					}
					buffer.flip();
					copyFileChannel.write(buffer);
					buffer.clear();							
				}
				end = System.nanoTime();
				fileChannel.close();
				copyFileChannel.close();
				NumberFormat nFormat = NumberFormat.getIntegerInstance();
				printMsg("Direct Copy에 걸린시간 : " + nFormat.format(end-start) + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		nonDirectCopyBtn = new Button("파일 복사(non-direct)");
		nonDirectCopyBtn.setPrefSize(150, 40);
		nonDirectCopyBtn.setOnAction(e->{

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Original File");
			file = fileChooser.showOpenDialog(primaryStage);
			path = file.toPath();		
			fileChooser.setTitle("Copy File");
			File copyFile = fileChooser.showSaveDialog(primaryStage);
			Path copyPath = copyFile.toPath();
			
			try {
				fileChannel = FileChannel.open(path, StandardOpenOption.READ);
				FileChannel copyFileChannel = 
						FileChannel.open(copyPath, StandardOpenOption.CREATE,
								StandardOpenOption.WRITE);
				int bytes = 0;
				buffer = ByteBuffer.allocate(bufferSize);
				long start=0, end=0;
				start = System.nanoTime();
				while(true) {					
					bytes = fileChannel.read(buffer);
					if( bytes == -1 ) {
						break;
					}
					buffer.flip();
					copyFileChannel.write(buffer);
					buffer.clear();							
				}
				end = System.nanoTime();
				fileChannel.close();
				copyFileChannel.close();
				NumberFormat nFormat = NumberFormat.getIntegerInstance();
				printMsg("Non-Direct Copy에 걸린시간 : " + nFormat.format(end-start) + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		FlowPane flowPane = new FlowPane();
     	flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(openBtn);
		flowPane.getChildren().add(saveBtn);
		flowPane.getChildren().add(directCopyBtn);
		flowPane.getChildren().add(nonDirectCopyBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("메모장");
		primaryStage.setOnCloseRequest(e->{
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	
}