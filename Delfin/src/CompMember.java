import java.time.LocalDate;
import java.util.ArrayList;


public class CompMember extends Member {
	
	private ArrayList<SwimResult> swimResults = new ArrayList<SwimResult>();
	private SwimResult[] bestTrainResult = new SwimResult[18];
	private int noOfTrainResults = 0;


	public CompMember(String fName, String lName, int age, boolean pMember, LocalDate cDate){
		super(fName, lName, age, pMember, cDate);
	}
	
	public void addCompResult(double time, int place, Disciplin disciplin, SwimLength length, String swimEvent){
		SwimResult swimResult = new SwimResult(time, LocalDate.now(), place, disciplin, length, swimEvent);
		swimResults.add(swimResult);
	}
	//Adds Training result
		//Loops through array and only adds if the result is faster or if there is no result
	public void addTrainResult(double time, Disciplin disciplin, SwimLength length){
			SwimResult placeHolder = new SwimResult(time, LocalDate.now(), disciplin, length);
			for (int i = 0; i < bestTrainResult.length; i++){
				if (bestTrainResult[i] == null){
					switch(disciplin){
						case FREESTYLE: switch(length){
							case HUNDRED: bestTrainResult[0] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[1] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[2] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case CRAWL: switch(length){
							case HUNDRED: bestTrainResult[3] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[4] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[5] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case BACKSTROKE: switch(length){
							case HUNDRED: bestTrainResult[6] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[7] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[8] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case BREASTSTROKE: switch(length){
							case HUNDRED: bestTrainResult[9] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[10] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[11] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case BUTTERFLY: switch(length){
							case HUNDRED: bestTrainResult[12] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[13] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[14] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case MEDLEY: switch(length){
							case HUNDRED: bestTrainResult[15] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[16] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[17] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
					}

				}
				else if (placeHolder.getDisciplin().equals(bestTrainResult[i].getDisciplin()) && placeHolder.getLength().equals(bestTrainResult[i].getLength())){
					if (placeHolder.getTime() < bestTrainResult[i].getTime()){
						switch(disciplin){
						case FREESTYLE: switch(length){
							case HUNDRED: bestTrainResult[0] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[1] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[2] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case CRAWL: switch(length){
							case HUNDRED: bestTrainResult[3] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[4] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[5] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case BACKSTROKE: switch(length){
							case HUNDRED: bestTrainResult[6] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[7] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[8] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case BREASTSTROKE: switch(length){
							case HUNDRED: bestTrainResult[9] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[10] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[11] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case BUTTERFLY: switch(length){
							case HUNDRED: bestTrainResult[12] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[13] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[14] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
						
						case MEDLEY: switch(length){
							case HUNDRED: bestTrainResult[15] = placeHolder;
							noOfTrainResults++;
							break;
							
							case HUNDREDFIFTY: bestTrainResult[16] = placeHolder;
							noOfTrainResults++;
							break;
							
							case TWOHUNDRED: bestTrainResult[17] = placeHolder;
							noOfTrainResults++;
							break;
							
						}
						break;
					
						}
						
					}
					
					else{
						System.out.println("Too slow buddy!");
						break;
					}
				}
			}
		}
	
	public SwimResult[] getTrainResults(){
		return this.bestTrainResult;
	}
	
	public ArrayList<SwimResult> getCompResults(){
		return this.swimResults;
	}
}
