package javaThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

class UserPanel extends FlowPane {
	TextField nameField = new TextField();;
	ProgressBar progressBar = new ProgressBar(0.0);
	ProgressIndicator indicator = new ProgressIndicator(0.0);
	
	public UserPanel() {
	}
	
	UserPanel(String name) {
		
		setPrefSize(750, 50);
		
		this.nameField.setText(name);
		nameField.setPrefSize(100, 50);
		progressBar.setPrefSize(600, 50);
		indicator.setPrefSize(50, 50);
		getChildren().add(nameField);
		getChildren().add(progressBar);
		getChildren().add(indicator);
	}

	public TextField getNameField() {
		return nameField;
	}

	public void setNameField(TextField nameField) {
		this.nameField = nameField;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public ProgressIndicator getIndicator() {
		return indicator;
	}

	public void setIndicator(ProgressIndicator indicator) {
		this.indicator = indicator;
	}
	
}	

public class Exam02_ThreadRace extends Application {

	private List<String> userNames = Arrays.asList("홍길동","최길동","박길동");
	private List<ProgressRunnable> uRunnable = new ArrayList<ProgressRunnable>();
	
	private Button startBtn;
	private TextArea textarea;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// 전체 window 크기 세팅
		BorderPane root = new BorderPane();
		root.setPrefSize(900, 600);
		
		// 사용자 Panel이 들어갈 GridPane 생성
		TilePane center = new TilePane();
		center.setPrefColumns(1);
		center.setPrefRows(userNames.size());

		// 최종 순위가 출력될 TextArea 생성
		textarea = new TextArea();
		textarea.setPrefSize(500, 70);
		
		// 사용자 Panel 생성 및 Runnable객체 생성
		for(String name : userNames) {
			UserPanel uPanel = new UserPanel(name);
			center.getChildren().add(uPanel);
			uRunnable.add(new ProgressRunnable(uPanel.getProgressBar(),
					uPanel.getIndicator(),
					uPanel.getNameField().getText(),textarea));
		}		
				
		center.getChildren().add(textarea);
		
		// Window enter에 TilePane을 부착
		root.setCenter(center);
		
		// 버튼 클릭시 Thread 생성 및 시작
		startBtn = new Button("시작");
		startBtn.setPrefSize(700, 50);
		startBtn.setOnAction(e->{
			for(ProgressRunnable runnable : uRunnable) {
				Thread thread = new Thread(runnable);
				thread.start();
			}
		});
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(startBtn);		
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Race");
		primaryStage.setOnCloseRequest(e->{
			
		});
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch();
	}
}

class ProgressRunnable implements Runnable {

	private ProgressBar progressbar;
	private ProgressIndicator progressIndicator;
	private String msg;
	private TextArea textarea;
	
	
	public ProgressRunnable(ProgressBar progressbar, ProgressIndicator progressIndicator, String msg, TextArea textarea) {
		super();
		this.progressbar = progressbar;
		this.progressIndicator = progressIndicator;
		this.msg = msg;
		this.textarea = textarea;
	}


	@Override
	public void run() {
		double k = 0.0;
		Random random = new Random();
		while( progressbar.getProgress() < 1.0 ) {
			
			try {
				Thread.sleep(100);
				k += random.nextDouble() * 0.1;
				final double tmp = k;
				Platform.runLater(()->{
					progressbar.setProgress(tmp);
					progressIndicator.setProgress(tmp);	
					if( tmp >= 1.0 ) {
						textarea.appendText(msg + "\n"); 
					}
				});
				if( k >= 1.0 ) break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
}