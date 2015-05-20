
public class Contingent {
	
	/*
	 * Lavet af: Jens Jakob Sveding
	 */
	
	public static int kontigent;
	
	public static int whichKontigent(Member member){
		if(member instanceof PasMember){
			kontigent = 500;
		}else{
			if(member.getAge() < 18){
				kontigent = 1000;
			}else if(member.getAge() < 60){
				kontigent = 1600;
			}else if(member.getAge() > 60){
				kontigent = 1200;
			}
		}
	return kontigent;
	}
}
