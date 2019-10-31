import java.util.ArrayList;
import java.util.List;

public class AlphaBetaPruning {
	private int move;
	private double value;
	private int numVisited = 0;
	private int numEvaluated = 0;
	private int minD;
	private int maxDepth;
	private int initialDepth;
	private double branching = 0.0;
	private double numBranched = 0.0;
	

    public AlphaBetaPruning() {
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        System.out.println("Move: " + move);
    	System.out.println("Value: " + value);
    	System.out.println("Number of Nodes Visited: " + numVisited);
    	System.out.println("Number of Nodes Evaluated: " + numEvaluated);
    	System.out.println("Max Depth Reached: " + maxDepth);
    	double total = branching/numBranched;
    	total = Math.round(total*10.0)/10.0;
    	System.out.println("Avg Effective Branching Factor: " + total);
    	// 
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
    	initialDepth = depth;
    	minD = depth;
		  int taken = 0;
		  for(int i = 1; i <= state.getSize(); i++) {
			  if(!state.getStone(i)) {
				  taken += 1;
			  }
		  }
    	if(taken%2 == 0) {
    		this.value = alphabeta(state, depth, Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY, true);
    	} else {
            this.value = -alphabeta(state, depth, Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY, true);
    	}
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
    	return maxValue(state, depth, Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY, true);
    }
    
    private double maxValue(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
    	numVisited += 1;
    	if(depth < minD)
    		minD = depth;
		double currBest = Double.NEGATIVE_INFINITY;
		if(state.getMoves().size() == 0 || depth == 0) {
			if(depth == 0)
				maxDepth = initialDepth;
			else
				maxDepth = initialDepth - minD;
			numEvaluated += 1;
			if(state.getMoves().isEmpty())
				return -1.0;
			return state.evaluate();
		}
		double v = Double.NEGATIVE_INFINITY;
		List<GameState> actions = state.getSuccessors();
		numBranched += 1;
		for(GameState i: actions) {
			branching+=1;
	    	double check = minValue(i, depth - 1, alpha, beta, false);
			v = Math.max(v, check);
			if(check > currBest) {
				currBest = check;
				this.move = i.getLastMove();
			}
			if(v >= beta) {
				return v;
			}
			alpha = Math.max(alpha, v);
		}
    	return v;
    }
    
    private double minValue(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
    	numVisited +=1;
    	if(depth > maxDepth)
    		maxDepth = depth;
		if(state.getMoves().size() == 0 || depth == 0) {
			if(depth == 0)
				maxDepth = initialDepth;
			else
				maxDepth = initialDepth - minD;
			numEvaluated += 1;
			if(state.getMoves().isEmpty())
				return 1.0;
			return -state.evaluate();
		}
		double v = Double.POSITIVE_INFINITY;
		List<GameState> actions = state.getSuccessors();
		numBranched += 1;
		for(GameState i: actions) {
			branching += 1;
	    	double check = maxValue(i, depth -1, alpha, beta, true);
			v = Math.min(v, check);
			if(v <= alpha) {
				return v;
			}
			beta = Math.min(beta,v);
		}
    	return v;
    }
}
