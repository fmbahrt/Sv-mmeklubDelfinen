import java.time.LocalDate;
import java.util.ArrayList;


public class CompMember extends Member {
	
	private ArrayList<SwimResult> swimResults;
	private SwimResult[] bestTrainResult = new SwimResult[18];
	private int noOfTrainResults = 0;

	public CompMember(String fName, String lName, int age, boolean pMember, LocalDate cDate){
		super(fName, lName, age, pMember, cDate);
	}
	
	public void addCompResult(double time, LocalDate date, int place, Disciplin disciplin, SwimLength length, String swimEvent){
		SwimResult swimResult = new SwimResult(time, date, place, disciplin, length, swimEvent);
		swimResults.add(swimResult);
		
		
	}
	
	public void addTrainResult(double time, LocalDate date, Disciplin disciplin, SwimLength length){
		SwimResult placeHolder = new SwimResult(time, date, disciplin, length);
		for (int i = 0; i < bestTrainResult.length; i++){
			if (bestTrainResult[i] == null){
				bestTrainResult[i] = placeHolder;
				noOfTrainResults++;
				break;
			}
			else if (placeHolder.getDisciplin().equals(bestTrainResult[i].getDisciplin()) && placeHolder.getSwimLength().equals(bestTrainResult[i].getSwimLength())){
				if (placeHolder.getTime() < bestTrainResult[i].getTime()){
					bestTrainResult[i] = placeHolder;
					break;
				}
				else{
					System.out.println("Too slow buddy!");
				}
			}
		}
	}
		
	
	public void showResults(){
		System.out.println("Best training results");
		if (noOfTrainResults >=0 && noOfTrainResults <= bestTrainResult.length){
			for (int i = 0; i < noOfTrainResults; i++){
				System.out.println(bestTrainResult[i].getTime() + " seconds");
			}
		}
		else{
			System.out.println("No results");
		}
		
		System.out.println("Comptetive results");
		
		for (int i= 0; i < swimResults.size(); i++){
			System.out.println("Event: " + swimResults.get(i).getSwimEvent());
			System.out.println("Date: " + swimResults.get(i).getDate());
			System.out.println("Disciplin: " + swimResults.get(i).getDisciplin());
			System.out.println("Length: " + swimResults.get(i).getSwimLength());
			System.out.println("Place: " + swimResults.get(i).getPlace());
			System.out.println("Time: " + swimResults.get(i).getTime());
	
		}
	}
	
	
}
