import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;


public class Delfinen extends Application {
	
	//Ændret 12-05-2015 af Frederik
	private static SerializableList serList = new SerializableList();
	private static FileHandler fileHandler = new FileHandler("Data", serList);
	private static ObservableList<Member> obsMemList = FXCollections.observableArrayList();
	
	//Ændret 13-05-2015 af Frederik, ting til creation af member / user
	private RadioButton rbMem1, rbMem2, rbMem3;
	private final ToggleGroup tglGroup = new ToggleGroup();
	
	//Til create member
	private Label lblFirstName, lblLastName, lblAge, lblName;
	private TextField tfFirstName, tfLastName, tfAge, tfUsername;
	private PasswordField tfPassword;
	
	//Matthias. Til resultsViews
	private ObservableList<SwimResult> trainResults = FXCollections.observableArrayList();
	private TableView<SwimResult> trResultTable = new TableView<SwimResult>();
	private TableColumn timeCol = new TableColumn("Time");
	private TableColumn dateCol = new TableColumn("Date");
	private TableColumn disCol = new TableColumn("Disciplin");
	private TableColumn lenCol = new TableColumn("Length");
	private TableColumn placeCol = new TableColumn("Place");
	private TableColumn eventCol = new TableColumn("Event");
	
	//Matthias Til top5
	private ObservableList<CompMember> top5List = FXCollections.observableArrayList();
	int crawlFlag = 0;
	int freestyleFlag = 0;
	int backFlag = 0;
	int breastFlag = 0;
	int butterFlag = 0;
	int medFlag = 0;
	int hundFlag = 0;
	int hunFifFlag = 0;
	int twoHundFlag = 0;
	private TableView<CompMember> top5Table = new TableView<CompMember>();
	
	//Matthias Til addResult
	private TextField tfTime;
	private TextField tfPlace;
	private TextField tfEvent;
	private ComboBox<Disciplin> disBox; 
	private ComboBox<SwimLength> lenBox;
	
	//Tabel 
	private TableView<Member> mainTable = new TableView<Member>();
	private TableColumn firstNameCol = new TableColumn("First Name");
	private TableColumn lastNameCol = new TableColumn("Last Name");
	private TableColumn restanceCol = new TableColumn("Restance");
	
	private static Stage window, crtMemDialog, newUserWindow;
	private static Scene mainScene, topFiveScene;
	
	private Member memPlaceHolder; 

	private User loggedUser = new User("admin", "admin", UserType.ADMIN);
	
	public static void main (String[] args) {
		
		instantiatorList();
		launch(args);
		
	}
	
	public static void instantiatorList(){
		/*Metoden bruges til at sikre sig at den ikke instantierer serMemList hvis der allerede er en fil.
		 * Derved instantierer den kun serMemList hvis der ikke er en fil
		 */
		
		//Førstgangs opsætning.
		if(fileHandler.read().getUserList().isEmpty()){
			serList.addUserToList(new User("admin", "admin", UserType.ADMIN));
			fileHandler.save(serList);
		}
		
		serList = fileHandler.read();
	}
	
	@Override
	public void start(Stage Stage) throws Exception {	
		window = Stage;
		login();		
	}
	
	public void mainView(){
		
		updateTable(fileHandler.read());
		
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
		btnOpret.setOnAction(e -> createMemberView());
				
		Button btnEnroll = new Button("Indmeld");
		btnEnroll.setPrefSize(140, 200);
		btnEnroll.setOnAction(e -> createMemberView());
	
		Button btnRemove = new Button("Slet medlem");
		btnRemove.setPrefSize(140, 200);
		btnRemove.setOnAction(e -> {
			if(mainTable.getSelectionModel().getSelectedItem() != null){
				removeMemberAction();
			}
		});
				
		Button btnPay = new Button("Kontigent betaling");
		btnPay.setPrefSize(140, 200);
		btnPay.setOnAction(e -> {
			if(mainTable.getSelectionModel().getSelectedItem() != null){
				kontigentBetalingAction();
			}
		});
		
		Button btnRestance = new Button("Restance");
		btnRestance.setPrefSize(140, 200);
		btnRestance.setOnAction(e -> showRestanceMembers());
				
		Button btnSwimRes = new Button("Svømmeresultat");
		btnSwimRes.setPrefSize(140, 200);
		btnSwimRes.setOnAction(e -> {
			if(mainTable.getSelectionModel().getSelectedItem() instanceof CompMember){
				resultsView();
			}	
		});
				
		Button btnTop = new Button("Top 5");
		btnTop.setPrefSize(140, 200);
		btnTop.setOnAction(e -> showTopFiveMainView());
				
		Button btnRegUser = new Button("Ny Bruger");
		btnRegUser.setPrefSize(140, 20);
		btnRegUser.setOnAction(e -> newUserView());
		
		Button btnLogout = new Button("Log ud");
		btnLogout.setPrefSize(140, 20);
		btnLogout.setOnAction(e -> login());
		
		//Labels
		Label lblWelcome = new Label("Delfinen                                                                                                                       ");
		lblWelcome.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		Label lblTableTotal = new Label(" Total: "+obsMemList.size());
		
		/*
		 * Forskellige brugere har forskellig access til programmet funktioner.
		 * Denne if/else sørger for, at knapperne kun er tilgængelige for dem
		 * der har access.
		 */
		if(loggedUser.getAccess().equals(UserType.ADMIN)){
			btnRegUser.setVisible(true);
		}else if(loggedUser.getAccess().equals(UserType.TREASURER)){
			btnRegUser.setVisible(false);
			btnEnroll.setDisable(true);
			btnRemove.setDisable(true);
			btnSwimRes.setDisable(true);
			btnTop.setDisable(true);
		}else if(loggedUser.getAccess().equals(UserType.COACH)){
			btnRegUser.setVisible(false);
			btnEnroll.setDisable(true);
			btnRemove.setDisable(true);
			btnPay.setDisable(true);
			btnRestance.setDisable(true);
		}
		
		//layout
		BorderPane borderPane = new BorderPane();
		
		HBox hboxTop = new HBox();
		hboxTop.setAlignment(Pos.BASELINE_LEFT);
		hboxTop.setPadding(new Insets(10, 10, 10, 10));
		hboxTop.setSpacing(10);
	    hboxTop.setStyle("-fx-background-color: #99CCFF;");
		hboxTop.getChildren().addAll(lblWelcome, btnRegUser,btnLogout);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(btnEnroll, btnRemove, btnPay, btnRestance, btnSwimRes, btnTop);
		
		VBox vboxCenter = new VBox();
		vboxCenter.getChildren().addAll(mainTable, lblTableTotal);
		
		borderPane.setTop(hboxTop);
		borderPane.setCenter(vboxCenter);
		borderPane.setRight(vbox);
		
		//scene
		mainScene = new Scene(borderPane, 1000, 600);
		
		//stage
		window.setResizable(true);
		window.setScene(mainScene);
		window.setTitle("Logged in as: "+loggedUser.getUsername()+", Access Type: "+loggedUser.getAccess());
		window.setResizable(false);
		window.show();
	}
	
	public void login(){
		
		/* 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metoden laver GUI til login.
		 */
		
		//layout 
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		//Textfields
		tfUsername = new TextField();
		tfUsername.setMaxWidth(250);
		tfUsername.setAlignment(Pos.BASELINE_CENTER);
		
		tfPassword = new PasswordField();
		tfPassword.setMaxWidth(250);
		tfPassword.setAlignment(Pos.BASELINE_CENTER);
		
		//Labels
		Label lblUsername = new Label("Username");
		Label lblPassword = new Label("Password");
		
		//Buttons
		Button btnLogin = new Button("Login");
		btnLogin.setPrefWidth(150);
		btnLogin.setOnAction(e -> loginAction());
		
		//Add to layout
		vbox.getChildren().addAll(lblUsername, tfUsername, lblPassword, tfPassword, btnLogin);
		
		//Scene
		Scene scene = new Scene(vbox, 300, 180);
		
		//Window
		window.setScene(scene);
		window.setTitle("Login");
		window.setResizable(false);
		window.show();
	}
	
	public void loginAction(){
		
		/* 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metoden bruger loginValidation og tager indtastet data.
		 * Hvis exception kastes, bliver brugeren opmærksom på fejl.
		 */
		
		try{
			loginValidation(tfUsername.getText(), tfPassword.getText());
			mainView();
		}catch(InvalidLoginException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("InvalidLoginException");
			alert.setHeaderText("Invalid Login Information");
			alert.setContentText("Please enter valid login information!");
			alert.showAndWait();
		}
		
	}
	
	public void loginValidation(String username, String password) throws InvalidLoginException{
		
		/* 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metoden validerer, at indtastede data stemmer overens med allerede eksisterende bruger,
		 * hvis ikke kastes en exception.
		 */
		
		boolean grantAccess = false;
		for(int i = 0; i < serList.getUserList().size(); i++){
			if(serList.getUserList().get(i).getUsername().equals(username) && serList.getUserList().get(i).getPassword().equals(password)){
				grantAccess = true;	
				loggedUser = serList.getUserList().get(i);
			}
		}
		
		if(grantAccess){
			//
		}else{
			throw new InvalidLoginException();
		}
	}
	
	public void newUserView(){
		
		/* 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metoden laver GUI til oprettelse af ny bruger
		 */
		
		newUserWindow = new Stage();
		
		//layout
		VBox vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		HBox hbox = new HBox(5);
		hbox.setAlignment(Pos.BASELINE_CENTER);
		
		//Toggle
		ToggleGroup tglGrp = new ToggleGroup();
		RadioButton rbAdmin = new RadioButton("Admin");
		rbAdmin.setToggleGroup(tglGrp);
		rbAdmin.setSelected(true);
		RadioButton rbTreasurer = new RadioButton("Kasserer");
		rbTreasurer.setToggleGroup(tglGrp);
		RadioButton rbCoach = new RadioButton("Træner");
		rbCoach.setToggleGroup(tglGrp);
		
		//TextField
		TextField tfDesUsername = new TextField();
		tfDesUsername.setMaxWidth(250);
		TextField tfDesPassword = new TextField();
		tfDesPassword.setMaxWidth(250);
		
		//Labels
		Label lblUsername = new Label("Username");
		Label lblPassword = new Label("Password");
		
		//Button
		Button btnCreate = new Button("Opret");
		btnCreate.setPrefWidth(150);
		btnCreate.setOnAction(e -> {
			try{
				
				if(rbAdmin.isSelected()){
					serList.addUserToList(new User(tfDesUsername.getText(), tfDesPassword.getText(), UserType.ADMIN));
				}else if(rbTreasurer.isSelected()){
					serList.addUserToList(new User(tfDesUsername.getText(), tfDesPassword.getText(), UserType.TREASURER));
				}else if(rbCoach.isSelected()){
					serList.addUserToList(new User(tfDesUsername.getText(), tfDesPassword.getText(), UserType.COACH));
				}
				fileHandler.save(serList);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Succes!");
				alert.setHeaderText("User Created!");
				alert.setContentText("User succesfully created!");
				alert.showAndWait();
				
				newUserWindow.close();
			}catch(Exception j){
				System.out.println(j.getMessage());
			}
		});
		Button btnCancel = new Button("Cancel");
		btnCancel.setPrefWidth(150);
		btnCancel.setOnAction(e -> newUserWindow.close());
		
		hbox.getChildren().addAll(rbAdmin, rbTreasurer, rbCoach);
		vbox.getChildren().addAll(lblUsername, tfDesUsername, lblPassword, tfDesPassword, hbox, btnCreate, btnCancel);
		//Scene
		Scene scene = new Scene(vbox, 300, 240);
		
		//Window
		newUserWindow.setScene(scene);
		newUserWindow.setTitle("Ny Bruger");
		newUserWindow.setResizable(false);
		newUserWindow.show();
	}
	
	public void createMemberView(){
		
		/* 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metoden laver GUI til oprettelse af nyt medlem.
		 */
		
		crtMemDialog = new Stage();
		
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
		btnCreate.setOnAction(e -> {
			createMemberAction();
		});
		Button btnCancel = new Button("Cancel");
		btnCancel.setPrefWidth(165);
		btnCancel.setOnAction(e -> crtMemDialog.close());
				
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
		crtMemDialog.setScene(scene);
		crtMemDialog.setResizable(false);
		crtMemDialog.setTitle("Indmeldelse af medlem");
		crtMemDialog.show();
		
		
	}
	
	public void createMemberAction(){
		
		/* 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metoden skal tilføje og gemme et nyt medlem til listen med data fra RadioButtons og
		 * TextFields.
		 * 
		 * Der try/catches, hvis invalide data fra brugeren indtastes.
		 */
		
		try{
			
			if(!checkTextField(tfFirstName.getText()) || !checkTextField(tfLastName.getText())){
				throw new InvalidMemberDataException();
			}
			
			if(rbMem1.isSelected()){
				serList.addMemberToList(new CompMember(tfFirstName.getText(), tfLastName.getText() ,Integer.parseInt(tfAge.getText()), true, LocalDate.now()));
			}else if(rbMem2.isSelected()){
				serList.addMemberToList(new ExcMember(tfFirstName.getText(), tfLastName.getText() ,Integer.parseInt(tfAge.getText()), true, LocalDate.now()));
			}else if(rbMem3.isSelected()){
				serList.addMemberToList(new PasMember(tfFirstName.getText(), tfLastName.getText() ,Integer.parseInt(tfAge.getText()), true, LocalDate.now()));				}
			
			fileHandler.save(serList);
			mainView();
				
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Member Creation");
			alert.setHeaderText("Member created!");
			alert.setContentText("Member has been succesfully created!");
			alert.showAndWait();
			
			crtMemDialog.close();
			
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
	
		/* 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metoden fjerner den markerede bruger fra listen.
		 */
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Dialog");
		alert.setHeaderText("Du er ved, at slette et medlem!");
		alert.setContentText("Er du sikker på, at du vil slette et medlem?");

		ButtonType btnTypeYes = new ButtonType("Ja");
		ButtonType btnTypeNo = new ButtonType("Nej");
		ButtonType btnTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(btnTypeYes, btnTypeNo, btnTypeCancel);

		//Her ser systemet hvilken knap man trykker, og handler ud fra det.
		Optional<ButtonType> result = alert.showAndWait();
		/*
		 * Hvis 'ja', finder loopet matchende medlem og sletter denne fra listen.
		 */
		if (result.get() == btnTypeYes){
			for(int i = 0; i < obsMemList.size(); i++){
				if(obsMemList.get(i) == mainTable.getSelectionModel().getSelectedItem()){
					obsMemList.remove(i);
				}
			}
			serList.setList(obsMemList);
			fileHandler.save(serList);
		} else if (result.get() == btnTypeNo) {
		    //
		} else {
		    //
		}
		
	}

	public boolean checkTextField(String textField){
		/*
		 * 
		 * Metode skrevet af: Frederik Bahrt
		 * 
		 * Metodens funktion er, at tjekke om det der bliver skrevet i et tekstfield er et gyldigt
		 * navn.
		 * 
		 * Bruger regex(regular expression) til at finde ud af om navnet indeholder invalide chars
		 * Hvis textField indeholder invalide navnetegn returner den false.
		 */
		
		Pattern pRegex = Pattern.compile("[^A-Z^a-z-^Æ^æ^Ø^ø^Å^å]"); //Tilføjede selv 'æøå', da den kun kender a-z!
		Matcher mRegex = pRegex.matcher(textField);
		
		if(mRegex.find()){
			return false;
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
	
	public void kontigentBetalingAction(){
		
		/* 
		 * Metode skrevet af: Jens Jakob Sveding
		 * 
		 * Metoden skal bruges til at bekr�ft kontigent betaling, eller opkr�ve kontigenter for et eller alle medlemmer.
		 */

		//Placeholder variable til selected member i vores table
		memPlaceHolder = mainTable.getSelectionModel().getSelectedItem();
		
		//createKontigentBetaling();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kontigent betaling");
		// "Firstname skal betale: 500/1000/1600; i kontigent"
		alert.setHeaderText(""+memPlaceHolder.getFirstName()+" skal betale: "+Contingent.whichKontigent(memPlaceHolder)+",- i kontigent");
		// "Bekr�ft at Firstname Lastname har betalt dette belQb"
		alert.setContentText("Bekraeft at "+ memPlaceHolder.getFirstName()+ " " +memPlaceHolder.getLastName()+" har betalt dette belQb");
	
		ButtonType btnTypeYes = new ButtonType("Confirm");
		ButtonType btnTypeNo = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		ButtonType btnTypeKontigent = new ButtonType("Opkraev kontigent");
		
	
		alert.getButtonTypes().setAll(btnTypeYes, btnTypeNo,btnTypeKontigent);
	
		Optional<ButtonType> result = alert.showAndWait();
		
		
		if (result.get() == btnTypeYes){
			memPlaceHolder.setRestance(Contingent.whichKontigent(memPlaceHolder));
			memPlaceHolder.setPayingMember(true);
			serList.setList(obsMemList);
			fileHandler.save(serList);
		}else if (result.get() == btnTypeNo) {
		    //
		} else if(result.get() == btnTypeKontigent){
			//ShowOpkraevKontigent er en ny alert, hvor der kan opkr�ves kontigent, metoden ses nederst
			ShowOpkraevKontigent();
		}
	
}

	public void showRestanceMembers(){
		/* 
		 * Metode skrevet af: Jens Jakob Sveding
		 * 
		 * Metoden skal bruges vise alle medlemmer i restance
		 */
		
		ObservableList<Member> restanceList = FXCollections.observableArrayList();
		
		// Looper igennem listen med medlemmer og finder de medlemmer der er i restance
		// og putter dem i restanceList
		for(int i = 0; i < obsMemList.size(); i++){	
			
			if(obsMemList.get(i).getRestance() != 0){
				restanceList.add(obsMemList.get(i));
			}
		}
		//stage
		Stage restanceStage = new Stage();
		
		//gridpane
		GridPane grid = new GridPane();
		
		//TableView med columns |Firstname|Lastname|restance|
		TableView<Member> tv = new TableView<Member>();
		TableColumn firstNameCol = new TableColumn("First Name");
		TableColumn lastNameCol = new TableColumn("Last Name");;
		grid.getChildren().add(tv);
		
		firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Member, String>("firstName")
                );
		firstNameCol.setPrefWidth(100);
	
		lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Member, String>("lastName")
                );
		lastNameCol.setPrefWidth(100);
		
		restanceCol.setCellValueFactory(
                new PropertyValueFactory<Member, String>("restance")
                );
		restanceCol.setPrefWidth(100);
		
		//Har sat .clear() ind saa der ikke kommmer duplicates.
		tv.getColumns().clear();
		tv.getColumns().addAll(firstNameCol, lastNameCol, restanceCol);
		tv.setItems(restanceList);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.setPrefHeight(600);
		//Scene
		Scene scene = new Scene(grid, 300, 300);
		restanceStage.setScene(scene);
		restanceStage.setResizable(false);
		restanceStage.show();
				
	}

	public void resultsView(){
		//Labels
		Label resultsBorder = new Label("Lav og se resultater                                                                                                                         ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

				
		//Layout
		BorderPane bPane1 = new BorderPane();
		VBox vbox1 = new VBox();
		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		HBox hbox3 = new HBox();
		
		//Buttons
		Button btnShowTrResults = new Button("Show training results");
		btnShowTrResults.setPrefSize(250, 140);
		btnShowTrResults.setOnAction(e -> {
			showTrResultsView();
			
		});
		
		Button btnShowCompResults = new Button("Show competetive results");
		btnShowCompResults.setPrefSize(250, 140);
		btnShowCompResults.setOnAction(e -> {
			showCompResultsView();
			
		});
		
		Button btnAddTrResults = new Button("Add training result");
		btnAddTrResults.setPrefSize(250, 140);
		btnAddTrResults.setOnAction(e -> {
			addTrResultsView();
			
		});
		
		Button btnAddCompResults = new Button("Add competetive result");
		btnAddCompResults.setPrefSize(250, 140);
		btnAddCompResults.setOnAction(e -> {
			addCompResultsView();
			
		});
		
		Button btnBack = new Button("Back");
		btnBack.setPrefSize(1000, 50);
		btnBack.setOnAction(e ->{
			mainView();
		});
		
		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
		hbox2.getChildren().addAll(btnAddTrResults, btnShowTrResults, btnAddCompResults, btnShowCompResults);
		vbox1.getChildren().addAll(hbox2);
				
				
		bPane1.setTop(hbox1);
		bPane1.setCenter(vbox1);
		bPane1.setBottom(btnBack);
				
				
				
		//scene
		Scene scene = new Scene(bPane1, 1000, 230);
			
		
		//Window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("View/Add Results");

	}

	public void showTrResultsView(){
		
		//Labels
		Label resultsBorder = new Label("Se Training Results                                                                                                                        ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
				
		//Layout
		BorderPane bPane1 = new BorderPane();
				
		HBox hbox1 = new HBox();
		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
				
				
		VBox vbox1 = new VBox();
		
		timeCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, Double>("time")
                );
		timeCol.setPrefWidth(100);
	
		dateCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, LocalDate>("date")
                );
		dateCol.setPrefWidth(100);
		
		disCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, Disciplin>("disciplin")
                );
		disCol.setPrefWidth(100);
		
		lenCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, SwimLength>("length")
                );
		lenCol.setPrefWidth(100);
		
		trResultTable.getColumns().clear();
		trainResults.clear();
		trResultTable.getColumns().addAll(timeCol, dateCol, disCol, lenCol);
		trResultTable.setItems(trainResults);
		trResultTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		//Goes through each result in the array of the selected member and adds those results to an arraylist
		if(mainTable.getSelectionModel().getSelectedItem() instanceof CompMember){
			SwimResult[] placeHolder = ((CompMember) mainTable.getSelectionModel().getSelectedItem()).getTrainResults();
			for (int i = 0; i < ((CompMember) mainTable.getSelectionModel().getSelectedItem()).getTrainResults().length; i++){	
				if (((CompMember) mainTable.getSelectionModel().getSelectedItem()).getTrainResults()[i] != null){
					
					SwimResult listResult = new SwimResult();
					listResult.setTime(placeHolder[i].getTime());
					listResult.setDate(placeHolder[i].getDate());
					listResult.setDis(placeHolder[i].getDisciplin());
					listResult.setLen(placeHolder[i].getLength());
					trainResults.add(placeHolder[i]);
					}
				}
		}
		
		//Button
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e ->{
			resultsView();
		});
		btnBack.setPrefHeight(600);
			
		bPane1.setTop(hbox1);
		bPane1.setCenter(trResultTable);
		bPane1.setRight(btnBack);

		//Scene
		Scene scene = new Scene(bPane1, 1000, 600);
		
		//Window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("Training Results");
	}
	
	//Matthias 15-05-2015
	public void showCompResultsView(){
		
		//Labels
		Label resultsBorder = new Label("Se Competetive Results                                                                                                                        ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
				
		//Layout
		BorderPane bPane1 = new BorderPane();
				
		HBox hbox1 = new HBox();
		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
				
				
		VBox vbox1 = new VBox();
		

		timeCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, Double>("time")
                );
		timeCol.setPrefWidth(100);
	
		dateCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, LocalDate>("date")
                );
		dateCol.setPrefWidth(100);
		
		placeCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, LocalDate>("place")
                );
		placeCol.setPrefWidth(100);
		
		disCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, Disciplin>("disciplin")
                );
		disCol.setPrefWidth(100);
		
		lenCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, SwimLength>("length")
                );
		lenCol.setPrefWidth(100);
		
		eventCol.setCellValueFactory(
                new PropertyValueFactory<SwimResult, LocalDate>("swimEvent")
                );
		eventCol.setPrefWidth(100);
		
		
		
		trResultTable.getColumns().clear();
		trResultTable.getColumns().addAll(timeCol, dateCol, placeCol, disCol, lenCol, eventCol);
		trResultTable.setItems(trainResults);
		trResultTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		//If the chosen member is a CompMember it adds it results to an arraylist
		if(mainTable.getSelectionModel().getSelectedItem() instanceof CompMember){
			trainResults.setAll(((CompMember) mainTable.getSelectionModel().getSelectedItem()).getCompResults());
		}
		
		//Button
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e ->{
			resultsView();
		});
		btnBack.setPrefHeight(600);
	
		vbox1.getChildren().addAll(trResultTable);
		
		bPane1.setTop(hbox1);
		bPane1.setCenter(trResultTable
				);
		bPane1.setRight(btnBack);

		//Scene
		Scene scene = new Scene(bPane1, 1000, 600);
		
		//Window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("Competetive Results");

	}
	
	//Matthas 14-05-2015
	public void addTrResultsView(){
		//Labels
		Label resultsBorder = new Label("Lav training resultater                                                                                                                         ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

						
		//Layout
		BorderPane bPane1 = new BorderPane();
		VBox vbox1 = new VBox();
		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		HBox hbox3 = new HBox();
		
		//Textfields
		tfTime = new TextField("Time");
		tfTime.setPrefWidth(200);
		
		//ComboBox
		ComboBox<Disciplin> disBox = new ComboBox<>();
		disBox.getItems().setAll(Disciplin.values());
		disBox.setPrefWidth(200);
		disBox.setValue(Disciplin.FREESTYLE);

		
		ComboBox<SwimLength> lenBox = new ComboBox<>();
		lenBox.getItems().setAll(SwimLength.values());
		lenBox.setPrefWidth(200);
		lenBox.setValue(SwimLength.HUNDRED);
		
		//Buttons
		//Adds uses the addTrainResult method to the selected member
		Button btnAddResult = new Button("Add Result");
		btnAddResult.setOnAction(e ->{  
			try{
				if(mainTable.getSelectionModel().getSelectedItem() instanceof CompMember){
					((CompMember)mainTable.getSelectionModel().getSelectedItem()).addTrainResult(Double.parseDouble(tfTime.getText()), disBox.getValue(), lenBox.getValue());
					serList.setList(obsMemList);
					fileHandler.save(serList);
				}	
			}
			
			catch(NumberFormatException x){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("NumberFormatException");
				alert.setHeaderText("Invalid Double");
				alert.setContentText("Please enter valid result time!");
				alert.showAndWait();
			}
			
		});
		
		btnAddResult.setPrefSize(600, 50);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e ->{			
			resultsView();
		});
		btnBack.setPrefSize(600, 40);

		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
		
		hbox2.getChildren().addAll(tfTime, disBox, lenBox);
		vbox1.getChildren().addAll(hbox2, btnAddResult);		
				
		bPane1.setTop(hbox1);
		bPane1.setCenter(vbox1);
		bPane1.setBottom(btnBack);
				
				
				
		//scene
		Scene scene = new Scene(bPane1, 600, 150);
		
		
		
		//Window
		window.setScene(scene);
		window.setResizable(false);
		window.setTitle("Add Training Result");
	}

	public void addCompResultsView(){
		//Labels
		Label resultsBorder = new Label("Lav competetive resultater                                                                                                                         ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

								
		//Layout
		BorderPane bPane1 = new BorderPane();
		VBox vbox1 = new VBox();
		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		HBox hbox3 = new HBox();
		
		//Textfields
		tfTime = new TextField("Time");
		tfTime.setPrefWidth(200);
		tfPlace = new TextField("Place");
		tfPlace.setPrefWidth(200);
		tfEvent = new TextField("Event");
		tfEvent.setPrefWidth(200);
		
		//ComboBox
		ComboBox<Disciplin> disBox = new ComboBox<>();
		disBox.getItems().setAll(Disciplin.values());
		disBox.setPrefWidth(200);
		disBox.setValue(Disciplin.FREESTYLE);

				
		ComboBox<SwimLength> lenBox = new ComboBox<>();
		lenBox.getItems().setAll(SwimLength.values());
		lenBox.setPrefWidth(200);
		lenBox.setValue(SwimLength.HUNDRED);
		
		//Buttons
		//Adds a competitive result to the selected member

		Button btnAddResult = new Button("Add Result");
		btnAddResult.setOnAction(e ->{  
			
			try{
				if(!checkTextField(tfEvent.getText())){
					throw new InvalidResultDataException();
				}
				
				if(mainTable.getSelectionModel().getSelectedItem() instanceof CompMember){
					((CompMember)mainTable.getSelectionModel().getSelectedItem()).addCompResult(Double.parseDouble(tfTime.getText()), Integer.parseInt(tfPlace.getText()) , disBox.getValue(), lenBox.getValue(), tfEvent.getText());
					serList.setList(obsMemList);
					fileHandler.save(serList);
				}
			}
			
			catch(NumberFormatException x){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("NumberFormatException");
				alert.setHeaderText("Invalid number");
				alert.setContentText("Please enter valid numbers!");
				alert.showAndWait();
			}
			catch(InvalidResultDataException x){
				System.out.println(x.getMessage());
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("InvalidResultDataException");
				alert.setHeaderText("Empty Strings");
				alert.setContentText("Please enter valid event!");
				alert.showAndWait();
			}
			
			
			
		});
		btnAddResult.setPrefSize(1000, 50);
			
				
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e ->{			
			resultsView();
		});
		btnBack.setPrefSize(1000, 40);
				
		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
		
		hbox2.getChildren().addAll(tfTime, tfPlace, disBox, lenBox, tfEvent);
		vbox1.getChildren().addAll(hbox2, btnAddResult);		
				
		bPane1.setTop(hbox1);
		bPane1.setCenter(vbox1);
		bPane1.setBottom(btnBack);
				
				
				
		//scene
		Scene scene = new Scene(bPane1, 1000, 150);
		
				
		//Window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("Add Competetive Result");
	}

	public void showTopFiveMainView(){
		//Labels
		Label resultsBorder = new Label("Top 5 discipliner                                                                                                                         ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

				
		//Layout
		BorderPane bPane1 = new BorderPane();
		VBox vbox1 = new VBox();
		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		HBox hbox3 = new HBox();
		
		//Buttons
		Button btnFreestyle = new Button("Freestyle");
		btnFreestyle.setPrefSize(250, 140);
		btnFreestyle.setOnAction(e -> {
			freestyleFlag++;
			swimLengthView();
			
		});
		
		Button btnCrawl = new Button("Crawl");
		btnCrawl.setPrefSize(250, 140);
		btnCrawl.setOnAction(e -> {
			crawlFlag++;
			swimLengthView();
			
		});
		
		Button btnBackstroke = new Button("Backstroke");
		btnBackstroke.setPrefSize(250, 140);
		btnBackstroke.setOnAction(e -> {
			backFlag++;
			swimLengthView();
			
		});
		
		Button btnBreaststroke= new Button("Breaststroke");
		btnBreaststroke.setPrefSize(250, 140);
		btnBreaststroke.setOnAction(e -> {
			breastFlag++;
			swimLengthView();
			
		});
		
		Button btnButterfly = new Button("Butterfly");
		btnButterfly.setPrefSize(250, 140);
		btnButterfly.setOnAction(e -> {
			butterFlag++;
			swimLengthView();
			
		});
		
		Button btnMedley = new Button("Medley");
		btnMedley.setPrefSize(250, 140);
		btnMedley.setOnAction(e -> {
			medFlag++;
			swimLengthView();
			
		});
		
		Button btnBack = new Button("Back");
		btnBack.setPrefSize(1000, 50);
		btnBack.setOnAction(e ->{
			crawlFlag = 0;
			freestyleFlag = 0;
			backFlag = 0;
			breastFlag = 0;
			butterFlag = 0;
			medFlag = 0;
			hundFlag = 0;
			hunFifFlag = 0;
			twoHundFlag = 0;
			mainView();
		});
		
		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
		hbox2.getChildren().addAll(btnFreestyle, btnCrawl, btnBackstroke, btnBreaststroke, btnButterfly, btnMedley);
		vbox1.getChildren().addAll(hbox2);
				
				
		bPane1.setTop(hbox1);
		bPane1.setCenter(vbox1);
		bPane1.setBottom(btnBack);
				
				
				
		//scene
		Scene scene = new Scene(bPane1, 1000, 600);
			
		
		//Window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("View/Add Results");
	}
	
	public void swimLengthView(){
		//Labels
		Label resultsBorder = new Label("Top 5 lengths                                                                                                                         ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

						
		//Layout
		BorderPane bPane1 = new BorderPane();
		VBox vbox1 = new VBox();
		HBox hbox1 = new HBox();
		HBox hbox2 = new HBox();
		HBox hbox3 = new HBox();
				
		//Buttons
		Button btnHundred= new Button("Hundred");
		btnHundred.setPrefSize(250, 140);
		btnHundred.setOnAction(e -> {
			hundFlag++;
			showTopFiveView();
					
		});
				
		Button btnTwoHundred = new Button("Twohundred");
		btnTwoHundred.setPrefSize(250, 140);
		btnTwoHundred.setOnAction(e -> {
			twoHundFlag++;
			showTopFiveView();
					
		});
				
		Button btnHundredFifty = new Button("Hundredfifty");
		btnHundredFifty.setPrefSize(250, 140);
		btnHundredFifty.setOnAction(e -> {
			hunFifFlag++;
			showTopFiveView();
					
		});
				
		Button btnBack = new Button("Back");
		btnBack.setPrefSize(1000, 50);
		btnBack.setOnAction(e ->{
			crawlFlag = 0;
			freestyleFlag = 0;
			backFlag = 0;
			breastFlag = 0;
			butterFlag = 0;
			medFlag = 0;
			hundFlag = 0;
			hunFifFlag = 0;
			twoHundFlag = 0;
			mainView();
		});
				
		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
		hbox2.getChildren().addAll(btnHundred, btnHundredFifty, btnTwoHundred);
		vbox1.getChildren().addAll(hbox2);
						
						
		bPane1.setTop(hbox1);
		bPane1.setCenter(vbox1);
		bPane1.setBottom(btnBack);
						
						
						
		//scene	
		Scene scene = new Scene(bPane1, 1000, 600);
					
				
		//Window
		window.setScene(scene);
		window.setResizable(true);
		window.setTitle("View/Add Results");		
	}
	
	public void showTopFiveView(){
		//showTopFive(Disciplin.FREESTYLE, SwimLength.HUNDRED);
		
		if(crawlFlag > 0 && hundFlag > 0){
			showTopFive(Disciplin.CRAWL, SwimLength.HUNDRED);
		}
		else if(crawlFlag > 0 && hunFifFlag > 0){
			showTopFive(Disciplin.CRAWL, SwimLength.HUNDREDFIFTY);
		}
		else if(crawlFlag > 0 && twoHundFlag > 0){
			showTopFive(Disciplin.CRAWL, SwimLength.TWOHUNDRED);
		}
		else if(freestyleFlag > 0 && hundFlag > 0){
			showTopFive(Disciplin.FREESTYLE, SwimLength.HUNDRED);
		}
		else if(freestyleFlag > 0 && hunFifFlag > 0){
			showTopFive(Disciplin.FREESTYLE, SwimLength.HUNDREDFIFTY);
		}
		else if(freestyleFlag > 0 && twoHundFlag > 0){
			showTopFive(Disciplin.FREESTYLE, SwimLength.TWOHUNDRED);
		}
		else if(backFlag > 0 && hundFlag > 0){
			showTopFive(Disciplin.BACKSTROKE, SwimLength.HUNDRED);
		}
		else if(backFlag > 0 && hunFifFlag > 0){
			showTopFive(Disciplin.BACKSTROKE, SwimLength.HUNDREDFIFTY);
		}
		else if(backFlag > 0 && twoHundFlag > 0){
			showTopFive(Disciplin.BACKSTROKE, SwimLength.TWOHUNDRED);
		}
		else if(breastFlag > 0 && hundFlag > 0){
			showTopFive(Disciplin.BREASTSTROKE, SwimLength.HUNDRED);
		}
		else if(breastFlag > 0 && hunFifFlag > 0){
			showTopFive(Disciplin.BREASTSTROKE, SwimLength.HUNDREDFIFTY);
		}
		else if(breastFlag > 0 && twoHundFlag > 0){
			showTopFive(Disciplin.BREASTSTROKE, SwimLength.TWOHUNDRED);
		}
		else if(butterFlag > 0 && hundFlag > 0){
			showTopFive(Disciplin.BUTTERFLY, SwimLength.HUNDRED);
		}
		else if(butterFlag > 0 && hunFifFlag > 0){
			showTopFive(Disciplin.BUTTERFLY, SwimLength.HUNDREDFIFTY);
		}
		else if(butterFlag > 0 && twoHundFlag > 0){
			showTopFive(Disciplin.BUTTERFLY, SwimLength.TWOHUNDRED);
		}
		else if(medFlag > 0 && hundFlag > 0){
			showTopFive(Disciplin.MEDLEY, SwimLength.HUNDRED);
		}
		else if(medFlag > 0 && hunFifFlag > 0){
			showTopFive(Disciplin.MEDLEY, SwimLength.HUNDREDFIFTY);
		}
		else if(medFlag > 0 && twoHundFlag > 0){
			showTopFive(Disciplin.MEDLEY, SwimLength.TWOHUNDRED);
		}
		
		//Labels
		Label resultsBorder = new Label("Se Competetive Results                                                                                                                        ");
		resultsBorder.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
						
		//Layout
		BorderPane bPane1 = new BorderPane();
						
		HBox hbox1 = new HBox();
		hbox1.setAlignment(Pos.BASELINE_LEFT);
		hbox1.setPadding(new Insets(10, 10, 10, 10));
		hbox1.setSpacing(10);
		hbox1.setStyle("-fx-background-color: #99CCFF;");
		hbox1.getChildren().addAll(resultsBorder);
						
						
		VBox vbox1 = new VBox();
			

		firstNameCol.setCellValueFactory(
		        new PropertyValueFactory<CompMember, String>("firstName")
		 );
		firstNameCol.setPrefWidth(100);
			
		lastNameCol.setCellValueFactory(
		        new PropertyValueFactory<CompMember, String>("lastName")
		);
		lastNameCol.setPrefWidth(100);	
				
		top5Table.getColumns().clear();
		top5Table.getColumns().addAll(firstNameCol, lastNameCol);
		top5Table.setItems(top5List);
		top5Table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				
		//Button
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e ->{
			hundFlag = 0;
			hunFifFlag = 0;
			twoHundFlag = 0;
			swimLengthView();
		});
		btnBack.setPrefHeight(600);
			
		vbox1.getChildren().addAll(top5Table);
				
		bPane1.setTop(hbox1);
		bPane1.setCenter(top5Table);
		bPane1.setRight(btnBack);

		//Scene
		topFiveScene = new Scene(bPane1, 1000, 600);
				
		//Window
		window.setScene(topFiveScene);
		window.setResizable(true);
		window.setTitle("Top 5");

		
		
	}
	
	public void showTopFive(Disciplin disciplin, SwimLength swimLength){
		top5List.clear();
		
		ArrayList<CompMember> tempCompMemList = new ArrayList<CompMember>();
		CompMember comMem1 = null;
		CompMember comMem2 = null;
		CompMember comMem3 = null;
		CompMember comMem4 = null;
		CompMember comMem5 = null;
		
	try{
		for (int i = 0; i < obsMemList.size(); i++){
			if (obsMemList.get(i) instanceof CompMember){
				for (int j = 0; j < ((CompMember) obsMemList.get(i)).getTrainResults().length; j++){
					if(((CompMember) obsMemList.get(i)).getTrainResults()[j] != null){
					if (((CompMember) obsMemList.get(i)).getTrainResults()[j].getDisciplin().equals(disciplin) && ((CompMember) obsMemList.get(i)).getTrainResults()[j].getLength().equals(swimLength)){
						System.out.println("Vi er inde");
						if (comMem1 == null){
							comMem1 = ((CompMember) obsMemList.get(i));
						}
						else if (comMem2 == null){
							comMem2 = ((CompMember) obsMemList.get(i));
						}
						else if (comMem3== null){
							comMem3 = ((CompMember) obsMemList.get(i));
						}
						else if (comMem4 == null){
							comMem4 = ((CompMember) obsMemList.get(i));
						}
						else if (comMem5 == null){
							comMem5 = ((CompMember) obsMemList.get(i));
						}
						
						else if (((CompMember) obsMemList.get(i)).getTrainResults()[j].getTime() < comMem1.getTrainResults()[j].getTime()){
							comMem5 = comMem4;
							comMem4 = comMem3;
							comMem3 = comMem2;
							comMem2 = comMem1;
							comMem1 = ((CompMember) obsMemList.get(i));
						}
						else if (((CompMember) obsMemList.get(i)).getTrainResults()[j].getTime() < comMem2.getTrainResults()[j].getTime()){
							comMem5 = comMem4;
							comMem4 = comMem3;
							comMem3 = comMem2;
							comMem2 = ((CompMember) obsMemList.get(i));
						}
						else if (((CompMember) obsMemList.get(i)).getTrainResults()[j].getTime() < comMem3.getTrainResults()[j].getTime()){
							comMem5 = comMem4;
							comMem4 = comMem3;
							comMem3 = ((CompMember) obsMemList.get(i));
						}
						else if (((CompMember) obsMemList.get(i)).getTrainResults()[j].getTime() < comMem4.getTrainResults()[j].getTime()){
							comMem5 = comMem4;
							comMem4 = ((CompMember) obsMemList.get(i));
						}
						else if (((CompMember) obsMemList.get(i)).getTrainResults()[j].getTime() < comMem5.getTrainResults()[j].getTime()){
							comMem5 = ((CompMember) obsMemList.get(i));
						}
					} 
				}}
			}
		}
		if(comMem1 != null){
			tempCompMemList.add(comMem1);
		}
		if(comMem2 != null){
			tempCompMemList.add(comMem2);
		}
		if(comMem3 != null){
				tempCompMemList.add(comMem3);
		}
		if(comMem4 != null){
				tempCompMemList.add(comMem4);
		}
		if(comMem5 != null){
			tempCompMemList.add(comMem5);
		}
		
		top5List.setAll(tempCompMemList);
	}

	catch(NullPointerException e){
		System.out.print(e.getMessage());
	}
	
	}

	public void ShowOpkraevKontigent(){
		/* 
		 * Metode skrevet af: Jens Jakob Sveding
		 * 
		 * Metoden bruges i Kontigent betaling, og bruges til at opkr�ve kontigenter.
		 */
		
		Alert kAlert = new Alert(AlertType.CONFIRMATION);
		kAlert.setTitle("Opkraev kontigent:");
		kAlert.setHeaderText("Tryk: Dette Medlem, for at opkr�ve kontigent for dette medlem \nTryk: Alle Medlemmer, for at opkraeve kontigent for alle medlemmer i klubben");
		kAlert.setContentText("Medlemmerne bliver sat i %restance ved kontigent opkraevning");
		
		ButtonType btnTypeDM = new ButtonType("Dette medlem");
		ButtonType btnTypeAM = new ButtonType("Alle Medlemmer");
		ButtonType btnTypeA = new ButtonType("Annuller", ButtonData.CANCEL_CLOSE);
		
		kAlert.getButtonTypes().setAll(btnTypeDM, btnTypeAM, btnTypeA);
		Optional<ButtonType> resultKontigent = kAlert.showAndWait();
		
		if(resultKontigent.get() == btnTypeDM){
			//s�tter dette medlems restance til %kontigent og saver
			memPlaceHolder.setRestance(Contingent.whichKontigent(memPlaceHolder)*-1);
			serList.setList(obsMemList);
			fileHandler.save(serList);
		}else if(resultKontigent.get() == btnTypeAM){
			//s�tter alle medlemmers restance til %kontigent og saver
			for(int i = 0; i < obsMemList.size(); i++){
				obsMemList.get(i).setRestance(Contingent.whichKontigent(obsMemList.get(i))*-1);
				serList.setList(obsMemList);
				fileHandler.save(serList);
			}
			
		}else if(resultKontigent.get() == btnTypeA){
			//
		}
	}
}
