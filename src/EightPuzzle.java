 /**
 * @author ucoleda
 * CS 664 Artificial Intelligence Final Exam
 * Option 2: 8 Puzzle Game Solver Using A*
 */
public class EightPuzzle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// goal board
		// idea is to get
		// 1 | 2 | 3
		// 4 | 5 | 6
		// 7 | 8 | --
		// in this program, 0 will represent the empty tile
		Board goalBoard = Board.goalBoard();
		// generate a random board
		Board randomBoard = Board.randomBoard();
		boolean solvable = randomBoard.isSolvable();
		// make sure that the puzzle is solvable
		// keep generating random puzzles until we get a solvable one
		while(!solvable){
			randomBoard = Board.randomBoard();
			solvable = randomBoard.isSolvable();
		}
		randomBoard.setGoalBoard(goalBoard);
		
		// display goal board
		System.out.println("GOAL BOARD:");
		goalBoard.printBoard();
		// display random starting state
		System.out.println("RANDOM START BOARD:");
		randomBoard.printBoard();
		// solve the puzzle!
		AStar aStar = new AStar(randomBoard);
		aStar.solve();
	}
}
