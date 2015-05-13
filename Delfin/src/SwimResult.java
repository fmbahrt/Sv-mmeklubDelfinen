import java.time.LocalDate;


public class SwimResult {
	private double time;  // In seconds
	private LocalDate date; // Format: 
	private int place;  //What place they got in the vent
	private Disciplin disciplin; 
	private SwimLength length;
	private String swimEvent; //Event name
	
	public SwimResult(double time, LocalDate date, Disciplin disciplin, SwimLength length){
		this.time = time;
		this.date = date;
		this.disciplin = disciplin;
		this.length = length;
	}
	
	public SwimResult(double time, LocalDate date, int place, Disciplin disciplin, SwimLength length, String swimEvent){
		this.time = time;
		this.date = date;
		this.place = place;
		this.disciplin = disciplin;
		this.length = length;
		this.swimEvent = swimEvent;
	}
	
	public double getTime(){
		return time;
		
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	public int getPlace(){
		return place;
	}
	
	public Disciplin getDisciplin(){
		return disciplin;
	}
	
	public SwimLength getSwimLength(){
		return length;
	}
	
	public String getSwimEvent(){
		return swimEvent;
	}
	
	
}
