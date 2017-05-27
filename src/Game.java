import java.util.ArrayList;

public class Game {
	
	public static void main(String[] args) {
		
		GameState currentState = new GameState();
		Interface gui = new Interface(currentState);
                gui.welcomeScreen();
	}
	
	public static int minimax(GameState state, boolean max, int depth){
		int initialScore = state.getScore();
		if(depth == 0){
			//at target depth
			return state.getScore();
		}
		if (initialScore >= 5000){
			return 10000;
		}
		if(initialScore <= -5000){
			return -10000;
		}
		
		//make list of possible moves
		ArrayList<GameMove> moves = new ArrayList<GameMove>();
		if(!getMoves(moves,state,max)){
			//board is full
			System.out.println("Error: Board is full.");
			return state.getScore();
		}
		
		//get minimax of children
		//first instance (also used to initialize best score)
		state.updatePosition(moves.get(0));
		int bestScore = minimax(state,!max,depth-1);
		int compareScore;
		state.retractMove(moves.get(0));
		
		//continue getting minimax of children
		//while keeping track of best score
		for(int i = 1; i < moves.size(); i++){
			//make possible move
			state.updatePosition(moves.get(i));
			
			//get score & compare to previous best score
			compareScore = minimax(state,!max,depth-1);
			if(max && bestScore < compareScore){
				//if max turn --> want bestScore to be highest
				bestScore = compareScore;
			}
			if(!max && bestScore > compareScore){
				//if min turn --> want bestScore to be lowest
				bestScore = compareScore;
			}
			
			//retract move
			state.retractMove(moves.get(i));
		}
		return bestScore;
	}
	
	//updates ArrayList of Moves with available moves (leftmost and rightmost squares)
	public static boolean getMoves(ArrayList<GameMove> list, GameState state, boolean MAX){
		PositionState p;
		boolean returnThis = false;
		//Choose player (max/plyr1 or min/plyr2)
		if(MAX){
			p = PositionState.PLYR1;
		}
		else {
			p = PositionState.PLYR2;
		}
		
		//iterate through rows of game-state to find leftmost and rightmost squares
		//inserts them into array list
		for(int row = 0; row < 8; row++){
			int columnLeft = -1;
			for(int col = 0; col < 8; col++){
				if(state.currentState[row][col] == PositionState.EMPTY){
					list.add(new GameMove(row,col,p));
					columnLeft = col;
					returnThis = true;
					break;
				}
			}
			for(int col = 7; col >= 0; col--){
				if(state.currentState[row][col] == PositionState.EMPTY && col != columnLeft){
					list.add(new GameMove(row,col,p));
					returnThis = true;
					break;
				}
			}
		}
		return returnThis;
	}
	
}
