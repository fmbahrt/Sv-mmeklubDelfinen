import java.io.Serializable;

public class User implements Serializable{
	private String username;
	private String password;
	private UserType access;
	
	public User(String username, String password, UserType access){
		
		this.username = username;
		this.password = password;
		this.access = access;
		
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public UserType getAccess(){
		return this.access;
	}
}
