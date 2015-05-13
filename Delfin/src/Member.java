import java.io.Serializable;
import java.time.LocalDate;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Member implements Serializable{
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleIntegerProperty age;
	private SimpleBooleanProperty payingMember;
	private SimpleObjectProperty<LocalDate> creationDate;
	private static int memberCount;
	
	public Member(String fName, String lName, int age, boolean pMember, LocalDate cDate){
		this.firstName = new SimpleStringProperty(fName);
		this.lastName = new SimpleStringProperty(lName);
		this.age = new SimpleIntegerProperty(age);
		this.payingMember = new SimpleBooleanProperty(pMember);
		this.creationDate = new SimpleObjectProperty<LocalDate>(cDate);
		memberCount++;
	}
	
	public String getFirstName(){
		return firstName.get();
	}
	
	public void setFirstName(String fName){
		firstName.set(fName);
		
	}
	
	public String getLastName(){
		return lastName.get();
		
	}
	
	public void setLastName(String lName){
		lastName.set(lName);
		
	}
	
	public int getAge(){
		return age.get();
		
	}
	
	public void setAge(int newAge){
		age.set(newAge);
		
	}
	
	public boolean getPayingMember(){
		return payingMember.get();
		
	}
	
	public void setPayingMember(boolean pMember){
		payingMember.set(pMember);
		
	}
	
	public LocalDate getCreationDate(){
		return creationDate.get();
		
	}
}
