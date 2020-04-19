package javaNetwork;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Exam05_MultiRoomChatClient extends Application {

	String userId;
	TextArea textarea;
	Button connBtn, disconnBtn;
	Button createRoomBtn;
	Button connRoomBtn;
	FlowPane MenuflowPane;
	ListView<String> roomlistview;
	ListView<String> participantslistview;
	
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
		textarea.setEditable(false);
		root.setCenter(textarea);
		
		roomlistview = new ListView<String>();
		participantslistview = new ListView<String>();
		
		GridPane gridPane = new GridPane();    	      
	    //Setting size for the pane  
	    //gridPane.setMinSize(400, 200); 
	       
	    //Setting the padding  
	    gridPane.setPadding(new Insets(10, 10, 10, 10)); 
	      
        //Setting the vertical and horizontal gaps between the columns 
	    gridPane.setVgap(10); 
	    //gridPane.setHgap(5);       
	      
	    //Setting the Grid alignment 
	    //gridPane.setAlignment(Pos.CENTER); 
	       
	      //Arranging all the nodes in the grid 
	    gridPane.add(roomlistview, 0, 0); 
	    gridPane.add(participantslistview, 0, 1); 
	      
		root.setRight(gridPane);
		
		roomlistview.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	        	//connRoomBtn.setDisable(false);
	        	//printMsg(listview.getSelectionModel().getSelectedItem());
	        }
	    });
		
		connBtn = new Button("Chat 서버 접속");
		connBtn.setPrefSize(150, 40);
		connBtn.setOnAction(e->{
			
			Dialog<String> dialog = new TextInputDialog("자신의 NickName을 입력하세요");
			dialog.setTitle("닉네임설정");
			dialog.setHeaderText("닉네임 설정입니다. 적절한 이름을 입력하세요.");
			 
			Optional<String> result = dialog.showAndWait();
			String entered = "";
			 
			if (result.isPresent()) {			 
			    entered = result.get();
			}
			 
			if( !entered.equals("") ) {
				textarea.clear();
				connBtn.setDisable(true);
				createRoomBtn.setDisable(false);
				connRoomBtn.setDisable(false);
				disconnBtn.setDisable(false);
				roomlistview.getItems().add("서울,경기지역모임");
				roomlistview.getItems().add("등산하실분!!");
				roomlistview.getItems().add("기사시험 공부하실분");
				printMsg("채팅서버에 접속했습니다.");
				printMsg(entered + "님 환영합니다.");
				userId = entered;
			} else {
				printMsg("닉네임을 설정해 주세요!!");
			}

		});

		createRoomBtn = new Button("채팅방 생성");
		createRoomBtn.setPrefSize(150, 40);
		createRoomBtn.setDisable(true);
		createRoomBtn.setOnAction(e->{
			Dialog<String> dialog = new TextInputDialog("생성할 방이름을 입력하세요");
			dialog.setTitle("채팅방 생성");
			dialog.setHeaderText("채팅방 생성입니다. 적절한 방 이름을 입력하세요.");
			 
			Optional<String> result = dialog.showAndWait();
			String entered = "";
			 
			if (result.isPresent()) {			 
			    entered = result.get();
			}
			 
			roomlistview.getItems().add(entered);
			printMsg("채팅방 : " + entered + " 이(가) 생성되었습니다.");			
		});

		connRoomBtn = new Button("채팅방 접속");
		connRoomBtn.setPrefSize(150, 40);
		connRoomBtn.setDisable(true);
		connRoomBtn.setOnAction(e->{
            String selectedItem = roomlistview.getSelectionModel().getSelectedItem();

            if(selectedItem == null) {
            	printMsg("입장하실 방을 선택해 주세요!");
            } else {
            	printMsg(selectedItem + "방에 입장합니다.");	
            	printMsg("==========================");
            	participantslistview.getItems().add("홍길동");
            	participantslistview.getItems().add("신사임당");
            	participantslistview.getItems().add("유관순");
            	participantslistview.getItems().add("강감찬");
            	
        		FlowPane chatPane = new FlowPane();
        		chatPane.setPadding(new Insets(10, 10, 10, 10));
        		chatPane.setColumnHalignment(HPos.CENTER);
        		chatPane.setPrefSize(700, 40);
        		chatPane.setHgap(10);
        		
        		TextField idfield = new TextField();
        		idfield.setPrefSize(100, 40);
        		idfield.setDisable(true);
        		idfield.setText(userId);
        		
        		TextField msgfield = new TextField();
        		msgfield.setPrefSize(400, 40);
        		msgfield.setDisable(false);
        		msgfield.setOnAction(e1->{
        			
        			String msg = "[" + idfield.getText() + "] : " + msgfield.getText();
        			printMsg(msg);
        			msgfield.clear();
        			
        		});
        		
        		Button chatCloseBtn = new Button("방 나가기");
        		chatCloseBtn.setPrefSize(100, 40);
        		chatCloseBtn.setOnAction(e2->{
        			root.setBottom(MenuflowPane);       			
        		});
        		
        		
        		chatPane.getChildren().add(idfield);
        		chatPane.getChildren().add(msgfield);
        		chatPane.getChildren().add(chatCloseBtn);
            	
        		root.setBottom(chatPane);
            }            
		});
		
						
				
	
		disconnBtn = new Button("Chat 서버 접속 종료");
		disconnBtn.setPrefSize(150, 40);
		disconnBtn.setDisable(true);
		
		disconnBtn.setOnAction(e->{
			connBtn.setDisable(false);
			createRoomBtn.setDisable(true);
			disconnBtn.setDisable(true);
			textarea.clear();
			printMsg("채팅서버에 접속을 종료했습니다.");
			roomlistview.getItems().clear();
		});
		
		MenuflowPane = new FlowPane();
		MenuflowPane.setPadding(new Insets(10, 10, 10, 10));
		MenuflowPane.setColumnHalignment(HPos.CENTER);
		MenuflowPane.setPrefSize(700, 40);
		MenuflowPane.setHgap(10);
		MenuflowPane.getChildren().add(connBtn);
		MenuflowPane.getChildren().add(createRoomBtn);
		MenuflowPane.getChildren().add(connRoomBtn);
		MenuflowPane.getChildren().add(disconnBtn);
		
		
		root.setBottom(MenuflowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Chatting Server에 접속");
		primaryStage.setOnCloseRequest(e->{
		});
		primaryStage.show();		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	
}