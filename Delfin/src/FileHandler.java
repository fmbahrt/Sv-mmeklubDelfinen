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
 */ 
public class FileHandler {
	
	private String fileName;
	private File file; 
	
	public FileHandler(String fileName, SerializableList serializableList){
		
		this.fileName = fileName+".bin";
		
		this.file = new File(this.fileName);
		
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
			System.out.println("Vi skriver her");
			FileOutputStream fous = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fous);
		    oos.writeObject(serializableList);
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
			System.out.println("Vi starter her");
			FileInputStream streamIn = new FileInputStream(file);
		    ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);    
		    serList = (SerializableList) objectinputstream.readObject();
		    streamIn.close();
		    objectinputstream.close();

		} catch (Exception e) {
			System.out.println("Fejler ved read");
		}
		
		return serList;

	}

}
