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
	
	private static Stage window;
	
	private static Member mem1 = new Member("Jens", "Sveding");
	private static Member mem2 = new Member("Daniel-Matthias", "Holtti");
	private static Member mem3 = new Member("Frederik", "Bahrt");
	
	private Member memPlaceHolder; 
	
	private TableView<Member> mainTable = new TableView<Member>();
	
	//Det giver god mening at have Obeservablelist i main, da vi kun bruger den til "view".
	private static ObservableList<Member> memberList = FXCollections.observableArrayList();
	
	public static void main (String[] args) {
		
		launch(args);
		
	}

	@Override
	public void start(Stage Stage) throws Exception {
		
		window = Stage;
		
		/*
		 * Alt det her skal lave meget bedre,pls.
		 * Det er mega grimt
		 * pls
		*/
		
		memberList.addAll(mem1, mem2, mem3);
		
		TableColumn firstNameCol = new TableColumn("First Name");
		firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Member, String>("firstName")
                );
		
		firstNameCol.setPrefWidth(100);
		
		TableColumn lastNameCol = new TableColumn("Last Name");
		lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Member, String>("lastName")
                );
		
		lastNameCol.setPrefWidth(100);
		
		mainTable.getColumns().addAll(firstNameCol, lastNameCol);
		mainTable.setItems(memberList);
		mainTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		
		//buttons
				Button button1 = new Button("Opret bruger");
				button1.setPrefSize(61, 30);
				
				Button button2 = new Button("Indmeldelse");
				button2.setPrefSize(61, 30);
				
				Button button3 = new Button("∆ndring af medlemstatus");
				button3.setPrefSize(61, 30);
				
				Button button4 = new Button("Slet medlem");
				button4.setPrefSize(61, 30);
				
				Button button5 = new Button("Kontigent betaling");
				button5.setPrefSize(61, 30);
				
				Button button6 = new Button("Restance-medlemmer");
				button6.setPrefSize(61, 30);
				
				Button button7 = new Button("Registrer svÝmmeresultat");
				button7.setPrefSize(61, 30);
				
				Button button8 = new Button("Top 5");
				button8.setPrefSize(61, 30);
				
		
		//button Action
		
		button1.setOnAction(e -> {
			
			
			try{
				//Placeholder variable til selected member i vores table
				memPlaceHolder = mainTable.getSelectionModel().getSelectedItem();
				System.out.println(memPlaceHolder.getFirstName());
			}catch(Exception j){
				System.out.println(j.getMessage());
			}
			

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
		window.setResizable(false);
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
	
}
