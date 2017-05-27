
//a position can be vacant or occupied by either player
enum PositionState {EMPTY, PLYR1, PLYR2};
//in an 8-square line, there are 4 combos of 5-square lines --> we call them groups
enum SubGroup {A, B, C, D}; 

public class GameState {
	
	public PositionState[][] currentState = new PositionState[8][8];
	
	//--------------------------------------------------------------
	//						CONSTRUCTORS
	//--------------------------------------------------------------
	public GameState(){
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				currentState[row][col] = PositionState.EMPTY;
			}
		}
	}
	
	public GameState(GameState copyState){
		for(int row = 0; row < 8; row++){
			for(int col = 0; col < 8; col++){
				currentState[row][col] = copyState.currentState[row][col];
			}
		}
	}
	
	//--------------------------------------------------------------
	//						MUTATOR
	//--------------------------------------------------------------
	//Change state of a position
	public void updatePosition(PositionState newState, int row, int col){
		currentState[row][col] = newState;
	}
	
	public void updatePosition(GameMove g){
		currentState[g.row][g.column] = g.state;
	}
	
	public void retractMove(GameMove g){
		currentState[g.row][g.column] = PositionState.EMPTY;
	}
	
	//--------------------------------------------------------------
	//							DRAW
	//--------------------------------------------------------------
	//draws board state
	public String toString(){
		System.out.println("  A  B  C  D  E  F  G  H");
                String returnThis = "";
		for(int row = 0; row < 8; row++){
                    returnThis += (row+1)+" ";
			for(int col = 0; col < 8; col++){
				switch(currentState[row][col]){
				case EMPTY:
					returnThis += "-";
					break;
				case PLYR1:
					returnThis += "X";
					break;
				case PLYR2:
					returnThis += "O";
					break;
				}
				if(col == 7){
					returnThis += "\n";
				}
				else{
					returnThis += "  ";
				}
			}
		}
		return returnThis;
	}
	
	//--------------------------------------------------------------
	//						HEURISTIC
	//--------------------------------------------------------------
	public int getScore(){
		int score = horScore() + vertScore() + diagScore();
		if(score > 10000){
			return 10000;
		}
		if(score < -10000){
			return -10000;
		}
		return score;
		//return 1;
	}
	
	//calculate horizonal score
	public int horScore(){
		int score = 0;
		for(int row = 0; row < 8; row++){
			score += rowScore(row);
		}
		return score;
	}
	
	//score of individual row
	public int rowScore(int row){
		//each line of 8 squares can create 4 different groups of 5 squares
		return horGrpScore(row,SubGroup.A) + horGrpScore(row,SubGroup.B) +
			   horGrpScore(row,SubGroup.C) + horGrpScore(row,SubGroup.D);
	}
	
	//score of individual 5-square group (horizontal)
	private int horGrpScore(int row, SubGroup subGrp){
		int max = 0; //Player1 score
		int min = 0; //Player2 score
		int lowBound = 0;
		int highBound = 0;
		
		//each group has a different range of indices in the row
		//A: 0-5; B: 1-6; C: 2-7; D: 3-8
		switch(subGrp){
		case A:
			lowBound = 0;
			highBound = 5;
			break;
		case B:
			lowBound = 1;
			highBound = 6;
			break;
		case C:
			lowBound = 2;
			highBound = 7;
			break;
		case D:
			lowBound = 3;
			highBound = 8;
			break;
		default:
			break;
		}
		
		//count # of PLYR1 and PLYR2 tokens in group
		for (int i = lowBound; i < highBound; i++){
			switch(currentState[row][i]){
			case PLYR1:
				max++;
				break;
			case PLYR2:
				min--;
				break;
			default:
				break;
			}
		}
		
		if((max == 0 && min == 0) || (max != 0 && min != 0)){
			//no tokens OR both PLYR1 and PLYR2 tokens
			return 0;
		}
		else if (max != 0){
			//only PLYR1 tokens
			if(max == 1){
				return max;
			}
			if(max == 5){
				return 10000;
			}
			return max + 1;
		}
		else{
			//only PLYR2 tokens
			if(min == -1){
				return min;
			}
			if(min == -5){
				return -10000;
			}
			return min - 1;
		}
	}
	
	//calculate vertical score
	public int vertScore(){
		int score = 0;
		for(int col = 0; col < 8; col++){
			score += colScore(col);
		}
		return score;
	}
	
	//score of individual column
	public int colScore(int col){
		//each line of 8 squares can create 4 different groups of 5 squares
		return vertGrpScore(col,SubGroup.A) + vertGrpScore(col,SubGroup.B) +
			   vertGrpScore(col,SubGroup.C) + vertGrpScore(col,SubGroup.D);
	}
	
	//score of individual 5-square group (vertical)
	private int vertGrpScore(int col, SubGroup subGrp){
		int max = 0; //Player1 score
		int min = 0; //Player2 score
		int lowBound = 0;
		int highBound = 0;
		
		//each group has a different range of indices in the row
		//A: 0-5; B: 1-6; C: 2-7; D: 3-8
		switch(subGrp){
		case A:
			lowBound = 0;
			highBound = 5;
			break;
		case B:
			lowBound = 1;
			highBound = 6;
			break;
		case C:
			lowBound = 2;
			highBound = 7;
			break;
		case D:
			lowBound = 3;
			highBound = 8;
			break;
		default:
			break;
		}
		
		//count # of PLYR1 and PLYR2 tokens in group
		for (int i = lowBound; i < highBound; i++){
			switch(currentState[i][col]){
			case PLYR1:
				max++;
				break;
			case PLYR2:
				min--;
				break;
			default:
				break;
			}
		}
		
		if((max == 0 && min == 0) || (max != 0 && min != 0)){
			//no tokens OR both PLYR1 and PLYR2 tokens
			return 0;
		}
		else if (max != 0){
			//only PLYR1 tokens
			if(max == 1){
				return max;
			}
			if(max == 5){
				return 10000;
			}
			return max + 1;
		}
		else{
			//only PLYR2 tokens
			if(min == -1){
				return min;
			}
			if(min == -5){
				return -10000;
			}
			return min - 1;
		}
	}
	
	//calculate diagonal score
	public int diagScore(){
		int diagScore = 0;
		//counts score of all diagonal groups directly and adds
		for(int row = 0; row < 4; row++){
			for(int col = 0; col < 4; col++){
				diagScore += SEGrpScore(row,col);
			}
			for(int col = 4; col < 8; col++){
				diagScore += SWGrpScore(row,col);
			}
		}
		return diagScore;
	}
	
	//score of individual 5-square group (southeast)
	public int SEGrpScore(int stRow, int stCol){
		int max = 0; //Player1 score
		int min = 0; //Player2 score
		
		//count # of PLYR1 and PLYR2 tokens in group
		for (int i = 0; i < 5; i++){
			switch(currentState[stRow+i][stCol+i]){
			case PLYR1:
				max++;
				break;
			case PLYR2:
				min--;
				break;
			default:
				break;
			}
		}
		
		if((max == 0 && min == 0) || (max != 0 && min != 0)){
			//no tokens OR both PLYR1 and PLYR2 tokens
			return 0;
		}
		else if (max != 0){
			//only PLYR1 tokens
			if(max == 1){
				return max;
			}
			if(max == 5){
				return 10000;
			}
			return max + 1;
		}
		else{
			//only PLYR2 tokens
			if(min == -1){
				return min;
			}
			if(min == -5){
				return -10000;
			}
			return min - 1;
		}
	}
	
	//score of individual 5-square group (southwest)
	public int SWGrpScore(int stRow, int stCol){
		int max = 0; //Player1 score
		int min = 0; //Player2 score
		
		//count # of PLYR1 and PLYR2 tokens in group
		for (int i = 0; i < 5; i++){
			switch(currentState[stRow+i][stCol-i]){
			case PLYR1:
				max++;
				break;
			case PLYR2:
				min--;
				break;
			default:
				break;
			}
		}
		
		if((max == 0 && min == 0) || (max != 0 && min != 0)){
			//no tokens OR both PLYR1 and PLYR2 tokens
			return 0;
		}
		else if (max != 0){
			//only PLYR1 tokens
			if(max == 1){
				return max;
			}
			if(max == 5){
				return 10000;
			}
			return max + 1;
		}
		else{
			//only PLYR1 tokens
			if(min == -1){
				return min;
			}
			if(min == -5){
				return -10000;
			}
			return min - 1;
		}
	}
}
