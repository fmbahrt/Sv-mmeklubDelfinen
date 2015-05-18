import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * Lavet af: Frederik Bahrt
 */
public class SerializableList implements Serializable {
	//hvilket UID objected bliver skrevet med, når det bliver serialized.
	private static final long serialVersionUID = -7432520665595171207L;
	//Begge ArrayLists til Members og Users
	private ArrayList<Member> serMemberList = new ArrayList<Member>();
	private ArrayList<User> serUserList = new ArrayList<User>();
	
	public void addMemberToList(Member member){
		serMemberList.add(member);
	}
	
	public void addUserToList(User user){
		serUserList.add(user);
	}
	
	public ArrayList<User> getUserList(){
		return this.serUserList;
	}
	
	public void setList(ObservableList<Member> memberList){
		ArrayList<Member> serListTemp = new ArrayList<Member>();
		for(int i = 0; i < memberList.size(); i++){
			serListTemp.add(memberList.get(i));
		}
		this.serMemberList = serListTemp;
	}
	
	public ObservableList<Member> getList(){
		ObservableList<Member> serListTemp = FXCollections.observableArrayList();
		for(int i = 0; i < serListTemp.size(); i++){
			serListTemp.add(this.serMemberList.get(i));
		}
		return serListTemp;
	}
	
	public ArrayList<Member> getArrayList(){
		return this.serMemberList;
	}
	
}
