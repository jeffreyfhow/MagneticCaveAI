
public class GameMove {
	
	public int column;
	public int row;
	public PositionState state;
	
	public GameMove(int r, int c, PositionState s){
		column = c;
		row = r;
		state = s;
	}
	public String toString(){
		String returnThis = "";
		switch(column){
			case 0:
				returnThis += 'A';
				break;
			case 1:
				returnThis += 'B';
				break;
			case 2:
				returnThis += 'C';
				break;
			case 3:
				returnThis += 'D';
				break;
			case 4:
				returnThis += 'E';
				break;
			case 5:
				returnThis += 'F';
				break;
			case 6:
				returnThis += 'G';
				break;
			case 7:
				returnThis += 'H';
				break;
			default:
				returnThis += "???";
				break;
		}
		returnThis += (row+1);
		return returnThis;
	}

}
