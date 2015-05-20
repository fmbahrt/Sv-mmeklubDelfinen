import java.time.LocalDate;

public class ExcMember extends Member{
	/*
	 * 
	 * Class made by: Daniel-Matthias Holtti
	 * 
	 * Class is a subclass of member. 
	 * It is used for all exercise members
	 * 
	 */
	
	public ExcMember(String fName, String lName, int age, boolean pMember, LocalDate cDate){
		super(fName, lName, age, pMember, cDate);
	}
}
