package javaNIO;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam04_WatchService extends Application {

	TextArea textarea;
	Button startBtn, stopBtn;
	WatchService watchService;
	ExecutorService executorService = Executors.newFixedThreadPool(2);
	
	
	private void startWatchService() throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		Path path = Paths.get("asset");
		path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);

		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {

				try {
					while(true) {
						WatchKey key = watchService.take();
						List<WatchEvent<?>> list = key.pollEvents();
						
						for(WatchEvent<?> event : list) {
							Path path = (Path)event.context();
							Kind<?> kind = event.kind();
							
							if( kind == StandardWatchEventKinds.ENTRY_CREATE ) {
								printMsg("파일 생성 - 파일명 : " + path.getFileName());
							} else if ( kind == StandardWatchEventKinds.ENTRY_DELETE ) {
								printMsg("파일 삭제 - 파일명 : " + path.getFileName());
							} else if ( kind == StandardWatchEventKinds.ENTRY_MODIFY ) {
								printMsg("파일 수정 - 파일명 : " + path.getFileName());
							}							
						}
						boolean valid = key.reset();
						if(!valid) {
							break;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		executorService.execute(runnable);
		executorService.shutdown();
	}
	
	private void stopWatchService() {
		executorService.shutdownNow();
	}
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
		
		startBtn = new Button("WatchService 기동");
		startBtn.setPrefSize(150, 40);
		startBtn.setOnAction(e->{
			try {
				startWatchService();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		stopBtn = new Button("WatchService 중지");
		stopBtn.setPrefSize(150, 40);
		stopBtn.setOnAction(e->{
			stopWatchService();
		});
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(startBtn);
		flowPane.getChildren().add(stopBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("WatchService");
		primaryStage.setOnCloseRequest(e->{
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	

	
}