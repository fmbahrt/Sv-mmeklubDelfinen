import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * Lavet af: Frederik Bahrt
 */
public class SerializableList implements Serializable {
	
	//hvilket UID objected bliver skrevet med.
	private static final long serialVersionUID = -7432520665595171207L;
	//Fejlen er her!
	private ArrayList<Member> serList = new ArrayList<Member>();
	
	public void addMemberToList(Member member){
		serList.add(member);
	}
	
	public void setList(ObservableList<Member> memberList){
		ArrayList<Member> serListTemp = new ArrayList<Member>();
		for(int i = 0; i < memberList.size(); i++){
			serListTemp.add(memberList.get(i));
		}
		this.serList = serListTemp;
	}
	
	public ObservableList<Member> getList(){
		ObservableList<Member> serListTemp = FXCollections.observableArrayList();
		for(int i = 0; i < serListTemp.size(); i++){
			serListTemp.add(this.serList.get(i));
		}
		return serListTemp;
	}
	
	
	
}
