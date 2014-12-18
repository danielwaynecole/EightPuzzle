import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * 
 */

/**
 * @author ucoleda
 * This class contains implementation of A* algorithm and supporting properties and methods
 */
public class AStar {
	Board startBoard;
	Board goalBoard;
	BoardComparator comparator = new BoardComparator(); // used to order priority queue
	PriorityQueue<Board> openSet = new PriorityQueue<Board>(10, comparator); // holds non-visited states
	LinkedList<Board> closedSet = new LinkedList<Board>(); // holds visited states

	public AStar(Board startBoard) {
		super();
		this.startBoard = startBoard;
		this.goalBoard = Board.goalBoard();
	}

	/*
	 * method to search for path to goal using a* 
	 */
	public void solve(){
		// add the starting node to the open set
		openSet.add(this.startBoard);
		// since we are doing a*
		// we go through all possible solutions rather than stopping at the first one
		while(openSet.size() > 0){
			// get the head of queue (shortest estimate path) and remove from open set
			Board x = openSet.poll();
			// add to closed set
			closedSet.add(x);
			// if we have reached the goal state
			if(x.equals(goalBoard)){
				printSolution(x);
				return;
			} else {
				// get possible moves
				ArrayList<Board> children = x.spawnChildren();
				for (Board b : children){
					// if we generate an already visited node, skip
					if(inClosedSet(b)){
						continue;
					} else {
					// otherwise add to open set
					// the open set will be ordered based on its f(n) value
					// using PriorityQueue with BoardComparator
					// the shortest estimated path will be head of queue
						openSet.add(b);
					}
				}
			}
		}
		System.out.println("Did not find solution");
		return;
	}

	/*
	 * determine with Board is in the closed set
	 */
	private boolean inClosedSet(Board b){
		Board curBoard;
		for (int i = 0; i < closedSet.size(); i++) {
			curBoard = closedSet.get(i);
			if(curBoard.equals(b)){
				return true;
			}
		}
		return false;
	} 

	/*
	 * display path from start to solution
	 * @param Board last move (matches goal state)
	 */
	private void printSolution(Board x){
		// stack to hold moves
		Stack<Board> path = new Stack<Board>();
		// move up the tree and add each node to stack
		while(x.getParent() != null){
			path.push(x);
			x = x.getParent();
		}
		path.push(x);
		int counter = 0;
		// pop boards off stack and print each of them from start to goal
		while(!path.empty()){
			Board b = path.pop();
			System.out.printf("Turn %d\n", counter);
			b.printBoard();
			counter++;
		}
	}

	// Getters and Setters
	
	public Board getStartBoard() {
		return startBoard;
	}

	public void setStartBoard(Board startBoard) {
		this.startBoard = startBoard;
	}


}
