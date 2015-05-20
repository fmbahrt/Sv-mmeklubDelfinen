import java.io.Serializable;
import java.time.LocalDate;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Member implements Serializable{
	//private SimpleStringProperty firstName;
	//private SimpleStringProperty lastName;
	//private SimpleIntegerProperty age;
	//private SimpleBooleanProperty payingMember;
	//private SimpleObjectProperty<LocalDate> creationDate;
	//private static int memberCount;
	
	private String firstName;
	private String lastName;
	private int age;
	private boolean payingMember;
	private LocalDate creationDate;
	public static int memberCount;
	private int restance = 0;
	private boolean pasMember = false;


	
	/*public Member(String fName, String lName, int age, boolean pMember, LocalDate cDate){
		this.firstName = new SimpleStringProperty(fName);
		this.lastName = new SimpleStringProperty(lName);
		this.age = new SimpleIntegerProperty(age);
		this.payingMember = new SimpleBooleanProperty(pMember);
		this.creationDate = new SimpleObjectProperty<LocalDate>(cDate);
		memberCount++;
	}
	*/
	public Member(String fName, String lName, int age, boolean pMember, LocalDate cDate){
		this.firstName = fName;
		this.lastName = lName;
		this.age = age;
		this.payingMember = pMember;
		this.creationDate = cDate;
		
	}
	public SimpleStringProperty toSimpleStringProp(String namString){
		SimpleStringProperty placeHolder = new SimpleStringProperty(namString);
		return placeHolder;
	}
	
	public SimpleIntegerProperty toSimpleIntProp(int age){
		SimpleIntegerProperty placeHolder = new SimpleIntegerProperty(age);
		return placeHolder;
	}
	
	public SimpleBooleanProperty toSimpleBoolProp(boolean pMember){
		SimpleBooleanProperty placeHolder = new SimpleBooleanProperty(pMember);
		return placeHolder;
	}
	public SimpleObjectProperty toSimpleBoolLDate(LocalDate cDate){
		SimpleObjectProperty<LocalDate> placeHolder = new SimpleObjectProperty<LocalDate>(cDate);
		return placeHolder;
	}
	
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setFirstName(String fName){
		this.firstName = fName;
		
	}
	
	public String getLastName(){
		return this.lastName;
		
	}
	
	public void setLastName(String lName){
		this.lastName = lName;
		
	}
	
	public int getAge(){
		return this.age;
		
	}
	
	public void setAge(int newAge){
		this.age = newAge;
		
	}
	
	public boolean getPayingMember(){
		return this.payingMember;
		
	}
	
	public void setPayingMember(boolean pMember){
		this.payingMember = pMember;
		
	}
	
	public LocalDate getCreationDate(){
		return this.creationDate;
		
	}
	
	/*
	 * Jens Jakob Sveding
	 */
	public int getRestance(){
		return this.restance;
		
	}
	
	public void setRestance(int newRestance){
		this.restance += newRestance;
	}
	
	public boolean getPasMember(){
		return this.pasMember;
		
	}
	
	public void setPasMember(boolean paMember){
		this.pasMember = paMember;
	}

}
