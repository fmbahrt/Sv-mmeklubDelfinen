import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Delfinen extends Application {
	
	private TableView mainTable = new TableView();
	
	public static void main (String[] args) {
		
		launch(args);
		
	}

	@Override
	public void start(Stage Stage) throws Exception {
		
		/*
		 * Alt det her skal lave meget bedre,pls.
		 * Det er mega grimt
		 * pls
		*/
		
		TableColumn firstNameCol = new TableColumn("First Name");
		firstNameCol.setPrefWidth(100);
		TableColumn lastNameCol = new TableColumn("Last Name");
		lastNameCol.setPrefWidth(100);
		
		mainTable.getColumns().addAll(firstNameCol, lastNameCol);
		
		HBox hbox = new HBox();
		hbox.getChildren().addAll(mainTable);
		
		Scene scene = new Scene(hbox, 300, 300);
		
		Stage.setResizable(false);
		Stage.setScene(scene);
		Stage.show();
		
	}
}
