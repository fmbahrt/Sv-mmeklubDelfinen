import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Delfinen extends Application {
	
	private User admin = new User("admin", "admin", UserType.ADMIN); //For test only
	
	//Ændret 12-05-2015 af Frederik
	private static SerializableList serMemList = new SerializableList();
	private static FileHandler fileMemHandler = new FileHandler("Members", serMemList);
	private static ObservableList<Member> obsMemList = FXCollections.observableArrayList();
	
	//Ændret 13-05-2015 af Frederik, ting til creation af member / user
	private RadioButton rbMem1, rbMem2, rbMem3;
	private final ToggleGroup tglGroup = new ToggleGroup();
	
	//Til create member
	private Label lblFirstName, lblLastName, lblAge, lblName;
	private TextField tfFirstName, tfLastName, tfAge;
	
	//Til addResult
	private TextField tfTime;
	private ComboBox<Disciplin> disBox; 
	private ComboBox<SwimLength> lenBox;
	
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
		login();		
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
		
		//Har sat .clear() ind så der ikke kommmer duplicates.
		mainTable.getColumns().clear();
		mainTable.getColumns().addAll(firstNameCol, lastNameCol);
		mainTable.setItems(obsMemList);
		mainTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		mainTable.setPrefHeight(600);
		
		
		//buttons
		Button btnOpret = new Button("Opret bruger");
		btnOpret.setPrefSize(61, 30);
		btnOpret.setOnAction(e -> {
					
			createMemberView();

		});
				
		Button btnEnroll = new Button("Indmeldelse");
		btnEnroll.setPrefSize(140, 200);
		btnEnroll.setOnAction(e -> createMemberView());
				
		Button btnMemStatus = new Button("Changing af medlemstatus");
		btnMemStatus.setPrefSize(140, 200);
				
		Button btnRemove = new Button("Slet medlem");
		btnRemove.setPrefSize(140, 200);
		btnRemove.setOnAction(e -> removeMemberAction());
				
		Button btnPay = new Button("Kontigent betaling");
		btnPay.setPrefSize(140, 200);
				
		Button btnRestance = new Button("Restance-medlemmer");
		btnRestance.setPrefSize(140, 200);
				
		Button btnSwimRes = new Button("Registrer swimmingresultat");
		btnSwimRes.setPrefSize(140, 200);
		btnSwimRes.serOnAction(e -> createAddResultView());
				
		Button btnTop = new Button("Top 5");
		btnTop.setPrefSize(140, 200);
				
		Button btnLogout = new Button("Logout");
		btnLogout.setPrefSize(140, 20);
		btnLogout.setOnAction(e -> login());
		
		//Labels
		Label lblWelcome = new Label("Velkommen, -username-                                                                                                                         ");
		lblWelcome.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		Label lblTableTotal = new Label(" Total: "+obsMemList.size());
		
		//layout
		BorderPane borderPane = new BorderPane();
		
		HBox hboxTop = new HBox();
		hboxTop.setAlignment(Pos.BASELINE_LEFT);
		hboxTop.setPadding(new Insets(10, 10, 10, 10));
		hboxTop.setSpacing(10);
	    hboxTop.setStyle("-fx-background-color: #99CCFF;");
		hboxTop.getChildren().addAll(lblWelcome, btnLogout);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(btnEnroll, btnMemStatus, btnRemove, btnPay, btnRestance, btnSwimRes, btnTop);
		
		VBox vboxCenter = new VBox();
		vboxCenter.getChildren().addAll(mainTable, lblTableTotal);
		
		borderPane.setTop(hboxTop);
		borderPane.setCenter(vboxCenter);
		borderPane.setRight(vbox);
		
		
		
		//scene
		Scene scene = new Scene(borderPane, 1000, 600);
		
		//stage
		window.setResizable(true);
		window.setScene(scene);
		window.setTitle("Delfinen, ALPHA v0.1.3");
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
		btnLogin.setOnAction(e -> loginAction());
		
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
	
	public void loginAction(){
		
		mainView();
		
	}
	
	//lavet d. 12-05-2015, Frederik
	public void createMemberView(){
		//layout
		GridPane grid = new GridPane();
		grid.setHgap(4); 
		grid.setVgap(4);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		//textfield and labels
		lblName = new Label("Name: ");
		lblFirstName = new Label("First Name");
		lblLastName = new Label("Last Name");
		lblAge = new Label("Age: ");
		
		tfFirstName = new TextField();
		tfLastName = new TextField();
		tfAge = new TextField();
		
		//buttons
		Button btnCreate = new Button("Create");
		btnCreate.setPrefWidth(165);
		btnCreate.setOnAction(e -> createMemberAction());
		Button btnCancel = new Button("Cancel");
		btnCancel.setPrefWidth(165);
		btnCancel.setOnAction(e -> mainView());
		
		//Radio Button
		rbMem1 = new RadioButton("Konkurrence");
		rbMem1.setToggleGroup(tglGroup);
		rbMem2 = new RadioButton("Motionist");
		rbMem2.setToggleGroup(tglGroup);
		rbMem3 = new RadioButton("Passiv");
		rbMem3.setToggleGroup(tglGroup);
		rbMem3.setSelected(true);
		
		grid.add(lblName, 0, 0);
		grid.add(tfFirstName, 1, 0);
		grid.add(tfLastName, 2, 0);
		grid.add(lblFirstName, 1, 1);
		grid.add(lblLastName, 2, 1);
		grid.add(lblAge, 0, 2);
		grid.add(tfAge, 1, 2);
		grid.add(rbMem1, 2, 2);
		grid.add(rbMem2, 2, 3);
		grid.add(rbMem3, 2, 4);
		grid.add(btnCreate, 1, 3);
		grid.add(btnCancel, 1, 4);
		//scene
		Scene scene = new Scene(grid, 400, 160);
		
		//window
		window.setScene(scene);
		window.setResizable(false);
		window.setTitle("Indmeldelse af medlem");
	}
	
	//Ændret 13-05-2015, af Frederik
	public void createMemberAction(){
		
		try{
			
			if(!checkTextField(tfFirstName.getText()) || !checkTextField(tfLastName.getText())){
				throw new InvalidMemberDataException();
			}
			
			if(rbMem1.isSelected()){
				serMemList.addMemberToList(new CompMember(tfFirstName.getText(), tfLastName.getText() ,Integer.parseInt(tfAge.getText()), true, LocalDate.now()));
			}else if(rbMem2.isSelected()){
				serMemList.addMemberToList(new ExcMember(tfFirstName.getText(), tfLastName.getText() ,Integer.parseInt(tfAge.getText()), true, LocalDate.now()));
			}else if(rbMem3.isSelected()){
				serMemList.addMemberToList(new PasMember(tfFirstName.getText(), tfLastName.getText() ,Integer.parseInt(tfAge.getText()), true, LocalDate.now()));				}
				
			fileMemHandler.save(serMemList);
			mainView();
				
		}catch(NumberFormatException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("NumberFormatException");
			alert.setHeaderText("Invalid Interger");
			alert.setContentText("Please enter valid age!");
			alert.showAndWait();
		}catch(InvalidMemberDataException e){
			System.out.println(e.getMessage());
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("InvalidMemberDataException");
			alert.setHeaderText("Empty Strings");
			alert.setContentText("Please enter valid First name and Last name!");
			alert.showAndWait();
		}

		
	}
	
	public void removeMemberAction(){
	
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Dialog");
		alert.setHeaderText("Du er ved, at slette et medlem!");
		alert.setContentText("Er du sikker på, at du vil slette et medlem?");

		ButtonType btnTypeYes = new ButtonType("Ja");
		ButtonType btnTypeNo = new ButtonType("Nej");
		ButtonType btnTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(btnTypeYes, btnTypeNo, btnTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == btnTypeYes){
			for(int i = 0; i < obsMemList.size(); i++){
				if(obsMemList.get(i) == mainTable.getSelectionModel().getSelectedItem()){
					obsMemList.remove(i);
				}
			}
			serMemList.setList(obsMemList);
			fileMemHandler.save(serMemList);
		} else if (result.get() == btnTypeNo) {
		    //
		} else {
		    //
		}
		
	}

	//Matthas 14-05-2015
	public void createAddResultView(){
		//Layout
		VBox vbox1 = new VBox();
		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		
		//Textfields
		tfTime = new TextField("Time");
		
		//ComboBox
		ComboBox<Disciplin> disBox = new ComboBox<>();
		disBox.getItems().setAll(Disciplin.values());

		
		ComboBox<SwimLength> lenBox = new ComboBox<>();
		lenBox.getItems().setAll(SwimLength.values());
		
		//Buttons
		Button btnAddResult = new Button("Add Result");
		btnAddResult.setOnAction(e ->{  
			if(mainTable.getSelectionModel().getSelectedItem() instanceof CompMember){
				((CompMember)mainTable.getSelectionModel().getSelectedItem()).addTrainResult(Double.parseDouble(tfTime.getText()), disBox.getValue(), lenBox.getValue());
				serMemList.setList(obsMemList);
				fileMemHandler.save(serMemList);

			}
			else{
				System.out.println("You do not need to add results scrub!");
			}
			});
		
		Button btnTest = new Button("Show shit");
		btnTest.setOnAction(e ->{
			if(mainTable.getSelectionModel().getSelectedItem() instanceof CompMember){
				((CompMember) mainTable.getSelectionModel().getSelectedItem()).showResults();
				
			}
			else{
				System.out.println("You do not need to see results scrub!");
			}
			
			
		});
		
		Button saveShit = new Button("Save");
		saveShit.setOnAction(e ->{
			
			System.out.println("Saved");
			
			mainView();
		});
		
		hbox1.getChildren().addAll(tfTime, disBox, lenBox);
		vbox1.getChildren().addAll(hbox1, btnAddResult, btnTest, saveShit);
		//Scene
		Scene scene = new Scene(vbox1, 300, 300);
		
		//Window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("Add Training Result");
	}

	
	public boolean checkTextField(String textField){
		
		//Tester for om stringen indeholder blank tegn eller tal
		char a = ' ';
		for(int i = 0; i < textField.length(); i++){
			if(textField.charAt(i) == a || Character.isDigit(textField.charAt(i))){
				return false;
			}
		}
		
		return true;
	}
	
	public static void updateTable(SerializableList serList){
		ObservableList<Member> tempMemList = FXCollections.observableArrayList();
		for (int i = 0; i < serList.getArrayList().size(); i++){
			tempMemList.add(serList.getArrayList().get(i));
		}
		obsMemList = tempMemList;
		
	}
	
}
