import java.text.BreakIterator;

public class AlphaBeta implements Player
{

	int id;
	int opponent_id;
	
	
    @Override
    public String name() {
        return "Alpha";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id; // your player id us id, opponenet's is (3-id)
    	
    	opponent_id = 3-id;
    }

    @Override
    public void calcMove(Connect4Board board, int oppMoveCol, Arbitrator arb) 
        throws TimeUpException {
        // Make sure there is room to make a move.
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        	
        
        int cutOffDepth = 1;
        
        int moveto = 0;
        int a = Integer.MIN_VALUE;
        int b = Integer.MAX_VALUE;
        
        
        while (!arb.isTimeUp() && cutOffDepth <= board.numEmptyCells()) {
        	// do minimax search
        	// start the first level of minimax, set move as you're finding bestScore
    		int bestScore = -1000;
    		// for each possible next move
    		for (int cols = 0; cols < 7; cols++) {
    			if (board.isValidMove(cols)) {
    				board.move(cols, id);
    				int score = alphabeta(board, cutOffDepth - 1, a, b, false, arb);
    				if (score> bestScore) {
    					bestScore = score;
    					moveto = cols;
    					a = Math.max(a, bestScore);
        				if (a > b) {
        					break;
    				}
    				board.unmove(cols, id);
    			}
			} 

        	arb.setMove(moveto);
        	cutOffDepth++;
    		}
        }
        
        
    }
    
    public int alphabeta(Connect4Board board, int depth, int a, int b, boolean isMaximizing, Arbitrator arb) {
    	// terminal node
    	if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
    		return score(board);
    	}
    	
    	int bestScore;
    	
    	// if maximizing player
    	if (isMaximizing == true) {
    		bestScore = -1000;
    		// for each possible next move
    		for (int cols = 0; cols < 7; cols++) {
    			if (board.isValidMove(cols)) {
    				board.move(cols, id);
    				bestScore = Math.max(bestScore,alphabeta(board, depth - 1, a, b, false, arb));
    				board.unmove(cols, id);
    				a = Math.max(a, bestScore);
    				if (a > b) {
    					break;
    				}
    			}
			} 
    		return bestScore;
    	}
    	else {
    		bestScore = 1000;
    		for (int cols = 0; cols < 7; cols++) {
    			if (board.isValidMove(cols)) {
    				board.move(cols, id);
    				bestScore = Math.min(bestScore,alphabeta(board, depth - 1, a, b, true, arb));
    				board.unmove(cols, id);
    				b = Math.min(b, bestScore);
    				if (a > b) {
    					break;
    				}
    			}
	        	
    		}
    		return bestScore;
    	}
    	
    	

    }
    
    public int score(Connect4Board board) {
    	return calcScore(board, id) - calcScore(board, opponent_id);
    }
    
 // Return the number of connect-4s that player #id has.
 	public int calcScore(Connect4Board board, int id)
 	{
 		final int rows = board.numRows();
 		final int cols = board.numCols();
 		int score = 0;
 		// Look for horizontal connect-4s.
 		for (int r = 0; r < rows; r++) {
 			for (int c = 0; c <= cols - 4; c++) {
 				if (board.get(r, c + 0) != id) continue;
 				if (board.get(r, c + 1) != id) continue;
 				if (board.get(r, c + 2) != id) continue;
 				if (board.get(r, c + 3) != id) continue;
 				score++;
 			}
 		}
 		// Look for vertical connect-4s.
 		for (int c = 0; c < cols; c++) {
 			for (int r = 0; r <= rows - 4; r++) {
 				if (board.get(r + 0, c) != id) continue;
 				if (board.get(r + 1, c) != id) continue;
 				if (board.get(r + 2, c) != id) continue;
 				if (board.get(r + 3, c) != id) continue;
 				score++;
 			}
 		}
 		// Look for diagonal connect-4s.
 		for (int c = 0; c <= cols - 4; c++) {
 			for (int r = 0; r <= rows - 4; r++) {
 				if (board.get(r + 0, c + 0) != id) continue;
 				if (board.get(r + 1, c + 1) != id) continue;
 				if (board.get(r + 2, c + 2) != id) continue;
 				if (board.get(r + 3, c + 3) != id) continue;
 				score++;
 			}
 		}
 		for (int c = 0; c <= cols - 4; c++) {
 			for (int r = rows - 1; r >= 4 - 1; r--) {
 				if (board.get(r - 0, c + 0) != id) continue;
 				if (board.get(r - 1, c + 1) != id) continue;
 				if (board.get(r - 2, c + 2) != id) continue;
 				if (board.get(r - 3, c + 3) != id) continue;
 				score++;
 			}
 		}
 		return score;
 	}

}

