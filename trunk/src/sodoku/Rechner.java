package sodoku;

public class Rechner {

	public static void main(String[] args) {
		
		for( int x = 0; x < 9; x++){
			for( int y = 0; y < 9; y++){
				berechne(x, y);
			}
		}
	}

	private static void berechne(int x, int y) {
		System.out.print("Quadrat: " + x + "," + y + " : ");
		int xd = (x / 3) * 3;
		int yd = y / 3;
		System.out.print("Quadrat Step: " + xd + "," + yd + " : ");
		int xy = (xd + yd);
		System.out.println("Quadrat Quadrant: " + xy );
		
	}
	
}
