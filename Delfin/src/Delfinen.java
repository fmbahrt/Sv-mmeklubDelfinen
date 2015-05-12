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
		
		//button
		Button button = new Button("*");
		button.setOnAction(e -> {
			
			
			try{
				//Placeholder variable til selected member i vores table
				memPlaceHolder = mainTable.getSelectionModel().getSelectedItem();
				System.out.println(memPlaceHolder.getFirstName());
			}catch(Exception j){
				System.out.println(j.getMessage());
			}
			

		});
		
		//layout
		HBox hbox = new HBox();
		hbox.getChildren().addAll(mainTable, button);
		
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
		window.show();
	}
	
}
