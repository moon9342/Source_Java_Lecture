package javaIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

public class Exam02_Notepad extends Application {

	TextArea textarea;
	Button openBtn, saveBtn;
	File file;
	
	
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
		
		openBtn = new Button("파일 열기");
		openBtn.setPrefSize(150, 40);
		openBtn.setOnAction(e->{
			textarea.clear();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Text File");
			file = fileChooser.showOpenDialog(primaryStage);
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = "";
				while((line = br.readLine()) != null) {
					printMsg(line);
				}
				br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		saveBtn = new Button("파일 저장");
		saveBtn.setPrefSize(150, 40);
		saveBtn.setOnAction(e->{
			String text = textarea.getText();
			try {
				FileWriter fw = new FileWriter(file);
				fw.write(text);
				fw.close();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("파일저장");
				alert.setHeaderText("File 저장");
				alert.setContentText("파일저장 성공!!");
				alert.showAndWait();
				
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