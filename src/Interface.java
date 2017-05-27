import java.util.Scanner;
import java.util.ArrayList;
//import java.util.regex.*;
import java.util.Calendar;

//NOTE: If the user enters an invalid move in game, they automatically lose the game
public class Interface {
    
    public enum gamePlayType {PLYR_FIRST, AI_FIRST , MULTIPLAYER}
    public enum playerColor {WHITE, BLACK,}
    
    Scanner kb = new Scanner(System.in);
    long timer;
    int input;
    String move;
    playerColor color;
    gamePlayType playType;
    GameState currentState;
    boolean showHeurAfterTurn;
   
    
    public Interface(GameState currentState) {
        this.currentState = currentState;
        showHeurAfterTurn = true;
    }
    
    //Use this method whenever validating menu input. Range refers to the highest available option.
    public int validateInput(int input, int range) {
        while (input < 1 || input > range) {
            System.out.println("Invalid input. please select a valid option.");
            System.out.print("Input: ");
            input = kb.nextInt();
        }
        return input;
    } 
    
    public int getCol(String move) {
        char letter;
        letter = move.charAt(0);
        switch (letter) {
        case 'a': case'A': 
            return 0;
        case 'b': case 'B':
            return 1;
        case 'c': case'C':
            return 2;
        case 'd': case'D':
            return 3;
        case 'e': case'E':
            return 4;
        case 'f': case'F':
            return 5;
        case 'g': case 'G':
            return 6;
        case 'h': case 'H':
            return 7;
        default:
            return 0;
        }
    }
    
    public int getRow(String mov) {
        //Takes in the ASCII value of the character, then identifies it the actual placement in the array
        int num = move.charAt((1))-49;
        return num;
    }
    
    public String validatePlayerMove(String input) {
        //1. Check that the lenght is okay, and that it is a letter between a-h followed by a character 1-8
        
        //Need to check the position of the left (if its not on a wall), if thats not empty place there. If it is, need to check position on the right
        //If thats not empty(or on a wall), the position is invalid. 
        boolean validated = false;
        while (!validated) {
            if (move.length() != 2 || !move.matches("[a-hA-H{1}][1-8{1}]")) {
                System.out.println("Invalid move. Try Again");
                move = kb.next();
            } else if (currentState.currentState[getRow(move)][getCol(move)]!= PositionState.EMPTY) {
                System.out.println("Invalid move. Try Again");
                move = kb.next();
            } else if (getCol(move) != 0 && currentState.currentState[getRow(move)][getCol(move)-1] == PositionState.EMPTY) {
                //Here I need to check that if the right one is empty, if it is; break this. We good
                if (getCol(move) == 7 || currentState.currentState[getRow(move)][getCol(move)+1] != PositionState.EMPTY) {
                    break;
                }
                System.out.println("Invalid move. Try Again");
                move = kb.next();
            } else {
                validated = true;
            }
        }
        
        return move;
    }
    
    public void printPlayerTurn(PositionState player) {
        if (player == PositionState.PLYR1) {
            System.out.println("----------------------------------------");
            System.out.println("Player 1's turn");
            System.out.println("----------------------------------------");
        } else {
            System.out.println("----------------------------------------");
            System.out.println("Player 2's turn");
            System.out.println("----------------------------------------");
        }
    }
    

    //Prints out the main menu to the console
    public void welcomeScreen()
    {
        System.out.println("----------------------------------------");
        System.out.println("Welcome to Magnetic Cave!");
        System.out.println("----------------------------------------");
        System.out.println("Please select an option:");
        System.out.println("    1. New Singleplayer Game");
        System.out.println("    2. New Multiplayer Game");
        System.out.print("Input: ");
        input = kb.nextInt();
        input = validateInput(input, 3);
        switch (input) {
            case 1:
                System.out.println("----------------------------------------");
                System.out.println("Singleplayer");
                System.out.println("----------------------------------------");
                System.out.println("Please select your color:");
                System.out.println("    1. Black");
                System.out.println("    2. White");
                System.out.print("Input: ");
                input = kb.nextInt();
                input = validateInput(input, 2);
                if(input == 1){
                	playType = gamePlayType.PLYR_FIRST;
                }
                else{
                	playType = gamePlayType.AI_FIRST;
                }
                playGame();
            break;
            case 2: 
                playType = gamePlayType.MULTIPLAYER;
                System.out.println("----------------------------------------");
                System.out.println("Multiplayer");
                System.out.println("----------------------------------------");
                playGame();
            break;
        }
    }
    
    public void playerTurn(){
    	System.out.println("Please enter a position where you would like to move.");
        move = kb.next();
        move = validatePlayerMove(move);
    }
    
    public void aiTurn(){
    	//determine if max or min
    	boolean isPlyr1;
    	if(playType == gamePlayType.AI_FIRST){
    		isPlyr1 = true;
    	}
    	else{
    		isPlyr1 = false;
    	}
    	
    	//get list of moves
        long start = System.currentTimeMillis();
    	ArrayList<GameMove> potentialMoves = new ArrayList<GameMove>();
    	Game.getMoves(potentialMoves, currentState, isPlyr1);
    	GameMove bestMove = null;
    	GameMove testMove = null;
    	int bestScore = 0;
    	int testScore = 0;
    	for(int i = 0; i < potentialMoves.size(); i++){
    		testMove = potentialMoves.get(i);
    		currentState.updatePosition(testMove);
    		testScore = Game.minimax(currentState, !isPlyr1, 3);
    		if(isPlyr1){
    			if(testScore > bestScore || bestMove == null){
    				bestScore = testScore;
    				bestMove = testMove;
    			}
    		}
    		else{
    			if(testScore < bestScore || bestMove == null){
    				bestScore = testScore;
    				bestMove = testMove;
    			}
    		}
    		currentState.retractMove(testMove);
    	}
    	move = bestMove.toString();
        long end = System.currentTimeMillis();
        
        float elapsedTime = (end-start)/1000f;
        System.out.println("Took " + elapsedTime + " seconds to calculate move");
    	if(isPlyr1){
    		System.out.println("Player 1 chose move: " + move);
    	}
    	else{
    		System.out.println("Player 2 chose move: " + move);
    	}
    }
    
    public void playGame() {
        boolean playing = true;
        int winner = -1;
        PositionState currentPlayer = PositionState.PLYR1;
        while (playing) {
            printPlayerTurn(currentPlayer);
            System.out.println(currentState);
            if(currentPlayer == PositionState.PLYR1){
	            if(playType != gamePlayType.AI_FIRST){
	            	playerTurn();
	            }
	            else{
	            	aiTurn();
	            }
            }
            else{
            	if(playType == gamePlayType.PLYR_FIRST){
            		aiTurn();
	            }
	            else{
	            	playerTurn();
	            }
            }
            currentState.updatePosition(currentPlayer, getRow(move), getCol(move));
            if (currentPlayer == PositionState.PLYR1) {
                currentPlayer = PositionState.PLYR2;
                if(currentState.getScore() >= 5000){
                	//player1 wins
                	winner = 1;
                	playing = false;
                }
            } else {
                currentPlayer = PositionState.PLYR1;
                if(currentState.getScore() <= -5000){
                	//player2 wins
                	winner = 2;
                	playing = false;
                }
            } 
            
        }  
        System.out.println(currentState);
        System.out.println("-------------------------------------------------");
        System.out.println("Player " + winner + " won! Thank you for playing!");
        System.out.println("-------------------------------------------------");
    }
}
