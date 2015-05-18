
public class InvalidLoginException extends Exception{

	@Override
	public String getMessage(){
		return "Invalid Login, details doesn't match!";
	}
}
