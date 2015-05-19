import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * Skrevet af: Frederik Bahrt
 * 
 * Denne klasse håndterer vores SerializableList, således, at den kan gemmes
 * og læses i/fra fil.
 */ 
public class FileHandler {
	
	private String fileName;
	private File file; 
	
	public FileHandler(String fileName, SerializableList serializableList){
		
		this.fileName = fileName+".delfin";
		
		this.file = new File(this.fileName);
		
		/*
		 * Da det er et krav at filen eksister i vores system, bliver dette
		 * klaret i vores constructer til klassen.
		 */
		if(file.exists()){
			//do nothing
		}else{
			try 
			{
				FileOutputStream fous = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fous);
			    oos.writeObject(serializableList);
			    fous.close();
			    oos.close();
			    System.out.println(this.fileName+" has been succesfullly created!");
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		
	}
	
	public void save(SerializableList serializableList){
				
		try 
		{
			FileOutputStream fous = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fous);
		    oos.writeObject(serializableList);
		    System.out.println(this.fileName+" has been succesfully saved!");
		    fous.close();
		    oos.close();
		}
		catch (Exception e)
		{
			System.out.println("Fejler ved save");
		}
	}
	
	public File getFile(){
		return this.file;
	}
	
	public SerializableList read(){
		
		SerializableList serList = null;
		
		try 
		{
			FileInputStream streamIn = new FileInputStream(file);
		    ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);    
		    serList = (SerializableList) objectinputstream.readObject();
		    System.out.println(this.fileName+" has been succesfully loaded!");
		    streamIn.close();
		    objectinputstream.close();

		} catch (Exception e) {
			System.out.println("Fejler ved read");
		}
		
		return serList;

	}

}
