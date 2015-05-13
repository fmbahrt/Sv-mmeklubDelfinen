import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Delfinen extends Application {
	
	//Ã†ndret 12-05-2015 af Frederik
	private static SerializableList serMemList = new SerializableList();
	private static FileHandler fileMemHandler = new FileHandler("Members", serMemList);
	private static ObservableList<Member> obsMemList = FXCollections.observableArrayList();
	
	//Til create member
	private TextField tfFirstName, tfLastName;
	
	//Tabel 
	private TableView<Member> mainTable = new TableView<Member>();
	private TableColumn firstNameCol = new TableColumn("First Name");
	private TableColumn lastNameCol = new TableColumn("Last Name");;
	
	private static Stage window;
	
	private Member memPlaceHolder; 
	

	
	public static void main (String[] args) {
		
		instantiatorMemList();
		launch(args);
		
	}
	/*Metoden bruges til at sikre sig at den ikke instantierer serMemList hvis der allerede er en fil.
	 * Derved instantierer den kun serMemList hvis der ikke er en fil
	 */
	public static void instantiatorMemList(){
		if (fileMemHandler.getFile().exists()){
			serMemList = fileMemHandler.read();
		}
		else{
			serMemList = new SerializableList();
		}
	}
	
	
	
	
	@Override
	public void start(Stage Stage) throws Exception {	
		window = Stage;
		mainView();		
	}
	
	public void mainView(){
		
		updateTable(fileMemHandler.read());
		
		firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Member, String>("firstName")
                );
		firstNameCol.setPrefWidth(100);
	
		lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Member, String>("lastName")
                );
		lastNameCol.setPrefWidth(100);
		
		//Har sat .clear() ind sÃ¥ der ikke kommmer duplicates.
		mainTable.getColumns().clear();
		mainTable.getColumns().addAll(firstNameCol, lastNameCol);
		
		mainTable.setItems(obsMemList);
		mainTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		
		//buttons
				Button button1 = new Button("Opret bruger");
				button1.setPrefSize(61, 30);
				
				Button button2 = new Button("Indmeldelse");
				button2.setPrefSize(61, 30);
				
				Button button3 = new Button("Ændring af medlemstatus");
				button3.setPrefSize(61, 30);
				
				Button button4 = new Button("Slet medlem");
				button4.setPrefSize(61, 30);
				
				Button button5 = new Button("Kontigent betaling");
				button5.setPrefSize(61, 30);
				
				Button button6 = new Button("Restance-medlemmer");
				button6.setPrefSize(61, 30);
				
				Button button7 = new Button("Registrer svømmeresultat");
				button7.setPrefSize(61, 30);
				
				Button button8 = new Button("Top 5");
				button8.setPrefSize(61, 30);
				
		
		//button Action
		
		button1.setOnAction(e -> {
			
			/*
			try{
				//Placeholder variable til selected member i vores table
				memPlaceHolder = mainTable.getSelectionModel().getSelectedItem();
				System.out.println(memPlaceHolder.getFirstName());
			}catch(Exception j){
				System.out.println(j.getMessage());
			}*/
			
			createMemberView();

		});
		
		//layout
		VBox btnVBox = new VBox();
		btnVBox.setAlignment(Pos.TOP_CENTER);
		btnVBox.getChildren().addAll(button1, button2, button3, button4, button5, button6, button7, button8);
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(mainTable, btnVBox);
		
		
		
		
		//scene
		Scene scene = new Scene(hbox, 300, 300);
		
		//stage
		window.setResizable(true);
		window.setScene(scene);
		window.show();
	}
	
	public void login(){
		//layout 
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		//Textfields
		TextField tfUsername = new TextField("Username...");
		tfUsername.setMaxWidth(250);
		
		TextField tfPassword = new TextField("Password...");
		tfPassword.setMaxWidth(250);
		
		//Buttons
		Button btnLogin = new Button("Login");
		
		//Add to layout
		vbox.getChildren().addAll(tfUsername, tfPassword, btnLogin);
		
		//Scene
		Scene scene = new Scene(vbox, 300, 150);
		
		//Window
		window.setScene(scene);
		window.setTitle("Login");
		window.setResizable(false);
		window.show();
	}
	
	//lavet d. 12-05-2015, Frederik
	public void createMemberView(){
		//layout
		VBox vbox = new VBox(10);
		
		//textfield
		tfFirstName = new TextField("First Name...");
		tfLastName = new TextField("Last Name...");
		
		//buttons
		Button btnCreate = new Button("Create");
		btnCreate.setOnAction(e -> createMemberAction());
		
		vbox.getChildren().addAll(tfFirstName, tfLastName, btnCreate);
		//scene
		Scene scene = new Scene(vbox, 300, 300);
		
		//window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("Create Member");
	}
	
	public void createMemberAction(){
		 serMemList.addMemberToList(new Member("Karl", "Jensen", 56, true, LocalDate.now()));
		//serMemList.addMemberToList(new Member(tfFirstName.getText(), tfLastName.getText()));
		fileMemHandler.save(serMemList);
		mainView();
		
	}
	
	public static void updateTable(SerializableList serList){
		ObservableList<Member> tempMemList = FXCollections.observableArrayList();
		for (int i = 0; i < serList.getArrayList().size(); i++){
			tempMemList.add(serList.getArrayList().get(i));
		}
		obsMemList = tempMemList;
		
	}
	
}
