import java.util.ArrayList;
import java.util.Random;

/**
 * @author ucoleda
 * This class represents a board state, including tile place, 
 * estimated cost of path,
 * and reference to parent node
 * Also contains static methods to generate a random board and the goal board
 */
public class Board {
	private int[][] currentBoard;
	private Board goalBoard;

	int fOfn = 0; // cost estimate from start to goal f(n) = g(n) + h(n)
	int gOfn = 0; // length of current path (distance from start)
	int hOfn = 0; // heuristic value
	Board parent = null; // for reconstructing winning path

	/*
	 * constructor
	 * @param currentBoard 3 x 3 array representing board state
	 * @param depth distance of node from start
	 * @param applyHeuristic boolean used for generating goal board since we don't need heuristic for that
	 */
	public Board(int[][] currentBoard, int depth, boolean applyHeuristic ){
		this.currentBoard = currentBoard;
		if(applyHeuristic){
			hOfn = heuristicEvaluation();
			gOfn = depth;
			fOfn = gOfn + hOfn;
		}
	}

	/*
	 * static method to generate the goal board
	 */
	public static Board goalBoard(){
		int[][] goal = new int[3][3]; // initialize the goal start
		int count = 1;
		for(int i = 0; i < goal.length; i++){
			for(int j = 0; j < goal[0].length; j++){
				goal[i][j] = count % 9;
				count++;
			}
		}
		return new Board(goal, 0, false);
	}

	/*
	 * static method to generate a random start board
	 */
	public static Board randomBoard(){
		int[][] random = new int[3][3]; // initialize 3 x 3 board
		int[] used = new int[9]; // keep track of used values
		for(int i = 0; i < random.length; i++){
			for(int j = 0; j < random[0].length; j++){
				boolean foundAvailable = false;
				while(!foundAvailable){
					// generate a random number 1 through 9
					int randomInt = Board.randInt(1, 9);
					// if it's already been used, loop again
					if(used[randomInt - 1] == randomInt){
						continue;
					// if the number is not yet placed, place it at this position (i, j)
					} else {
						used[randomInt - 1] = randomInt;
						random[i][j] = randomInt % 9;
						foundAvailable = true;
					}					
				}
			}
		}
		return new Board(random, 0, true);
	}

	/*
	 * estimate score for current state
	 * @return int score
	 */
	private int heuristicEvaluation(){
		int curValue;
		int value = 0;
		for(int i = 0; i < currentBoard.length; i++){
			for(int j = 0; j < currentBoard[0].length; j++){
				curValue = currentBoard[i][j];
				// add up all of the individual cell evaluations
				value += distanceFromGoal(curValue, i, j);
			}
		}
		return value;
	}

	/*
	 * generates manhattan distance by comparing current placement with goal placement
	 */
	private int distanceFromGoal(int curValue, int i, int j){
		int x_offset = 0;
		int y_offset = 0;
		Board goalBoard = Board.goalBoard();
		// loop through goal board to find the value being assessed
		for(int a = 0; a < goalBoard.getCurrentBoard().length; a++){
			for(int b = 0; b < goalBoard.getCurrentBoard()[0].length; b++){
				if(curValue == goalBoard.getCurrentBoard()[a][b]){
					// get the vertical distance
					if(a >= i){
						x_offset = a - i;
					} else {
						x_offset = i - a;
					}
					// get the horizontal distance
					if(b >= j){
						y_offset = b - j;
					} else {
						y_offset = j - b;
					}
				}
			}
		}
		// return total distance current placement versus goal placement
		return (x_offset + y_offset);
	}
	
	/*
	 * method to determine if some other board state is equal to this one
	 */
	public boolean equals(Board otherBoard){
		for(int i = 0; i < currentBoard.length; i++){
			for(int j = 0; j < currentBoard[0].length; j++){
				if(!(otherBoard.getCurrentBoard()[i][j] == currentBoard[i][j])){
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * method to create possible moves given current board state
	 * used to populate our tree
	 * @return ArrayList<Board> returns possible moves
	 */
	public ArrayList<Board> spawnChildren(){
		ArrayList<Board> children = new ArrayList<Board>();
		// find where the empty tile is
		int empty_x, empty_y;
		int[] coordinates = findEmptySquare();
		empty_x = coordinates[0];
		empty_y = coordinates[1];
		Board goal = Board.goalBoard();
		// check up
		if(!((empty_x - 1) < 0)){
			int switcharoo = currentBoard[(empty_x - 1)][empty_y]; // value of tile to move
			int newBoardArray[][] = copyBoard(); // create new 3 x 3 board
			newBoardArray[empty_x][empty_y] = switcharoo; // set empty to tile value
			newBoardArray[(empty_x - 1)][empty_y] = 0; // set former tile position to empty
			Board newBoard = new Board(newBoardArray, gOfn++, true); // create Board object
			newBoard.setParent(this); // set the parent
			newBoard.setGoalBoard(goal); // let it know what the goal state is
			children.add(newBoard); // add to arraylist to return to caller
		}
		// check down
		if(!((empty_x + 1) >= currentBoard.length)){
			int switcharoo = currentBoard[(empty_x + 1)][empty_y]; // value of tile to move
			int newBoardArray[][] = copyBoard(); // create new 3 x 3 board
			newBoardArray[empty_x][empty_y] = switcharoo; // set empty to tile value
			newBoardArray[(empty_x + 1)][empty_y] = 0; // set former tile position to empty
			Board newBoard = new Board(newBoardArray, gOfn++, true); // create Board object
			newBoard.setParent(this); // set the parent
			newBoard.setGoalBoard(goal); // let it know what the goal state is
			children.add(newBoard); // add to arraylist to return to caller
		}
		// check left
		if(!((empty_y - 1) < 0)){
			int switcharoo = currentBoard[(empty_x)][empty_y - 1]; // value of tile to move
			int newBoardArray[][] = copyBoard(); // create new 3 x 3 board
			newBoardArray[empty_x][empty_y] = switcharoo; // set empty to tile value
			newBoardArray[empty_x][empty_y - 1] = 0; // set former tile position to empty
			Board newBoard = new Board(newBoardArray, gOfn++, true); // create Board object
			newBoard.setParent(this); // set the parent
			newBoard.setGoalBoard(goal); // let it know what the goal state is
			children.add(newBoard); // add to arraylist to return to caller
		}
		// check right
		if(!((empty_y + 1) >= currentBoard[0].length)){
			int switcharoo = currentBoard[empty_x][empty_y + 1]; // value of tile to move
			int newBoardArray[][] = copyBoard(); // create new 3 x 3 board
			newBoardArray[empty_x][empty_y] = switcharoo; // set empty to tile value
			newBoardArray[empty_x][empty_y + 1] = 0; // set former tile position to empty
			Board newBoard = new Board(newBoardArray, gOfn++, true); // create Board object
			newBoard.setParent(this); // set the parent
			newBoard.setGoalBoard(goal); // let it know what the goal state is
			children.add(newBoard); // add to arraylist to return to caller
		}
		return children;
	}

	/*
	 * method to copy 3 x 3 board
	 * @return int[][] returns 3 x 3 array
	 */
	private int[][] copyBoard(){
		int[][] newBoard = new int[3][3];
		for(int i = 0; i < currentBoard.length; i++){
			for(int j = 0; j < currentBoard[0].length; j++){
				newBoard[i][j] = currentBoard[i][j];
			}
		}
		return newBoard;
	}

	/*
	 * method to locate the empty tile
	 * @return int[] index 0 is x coordinate and index 1 is y coordinate
	 */
	private int[] findEmptySquare(){
		int[] coordinates = new int[2];
		for(int i = 0; i < currentBoard.length; i++){
			for(int j = 0; j < currentBoard[0].length; j++){
				if(currentBoard[i][j] == 0){
					coordinates[0] = i;
					coordinates[1] = j;
				}
			}
		}
		return coordinates;
	}
	
	/*
	 * Not all random puzzle configurations are solvable
	 * inversions (cases where tile value is greater than tile + n value where n is 1 to puzzle length)
	 * also factor in whether distance of blank space from bottom right is even or odd
	 * if odd, add inversion
	 * if total number of inversions is odd, puzzle is not solvable
	 * @return boolean whether it's solvable or not
	 */
	public boolean isSolvable(){
		int[][] board = this.getCurrentBoard();
		int[] flat = new int[9];
		int inversions = 0;
		int counter = 0;
		// flatten array for analysis
		for(int i = 0; i < board.length; i++ ){
			for(int j = 0; j < board[0].length; j++){
				flat[counter] = board[i][j];
				counter++;
			}
		}
		for(int a = 0; a < flat.length - 1; a++) {
			// Check if a tile precedes smaller-valued tiles
			// if so increment inversions.
			for(int b = a + 1; b < flat.length; b++){
				if(flat[a] > flat[b]) inversions++;
			}
			// Determine if the distance of the blank space from the bottom 
			// right is even or odd, and increment inversions if it is odd.
			if(flat[a] == 0 && a % 2 == 1) inversions++;
		}

		// If inversions is even, the puzzle is solvable.
		return (inversions % 2 == 0);
	}

	/*
	 * displays board, very fancy
	 */
	public void printBoard(){
		System.out.println("_____________");
		String one, two, three;
		for(int i = 0; i < currentBoard.length; i++){
			one = (!(currentBoard[i][0] == 0)) ? Integer.toString(currentBoard[i][0]) : " ";
			two = (!(currentBoard[i][1] == 0)) ? Integer.toString(currentBoard[i][1]) : " ";
			three = (!(currentBoard[i][2] == 0)) ? Integer.toString(currentBoard[i][2]) : " ";

			System.out.printf("| %s | %s | %s |\n-------------\n", one, two, three);
		}
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return int randome integer
	 */
	public static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	// Getters and Setters
	
	public int[][] getCurrentBoard() {
		return currentBoard;
	}

	public void setCurrentBoard(int[][] currentBoard) {
		this.currentBoard = currentBoard;
	}
	public int getfOfn() {
		return fOfn;
	}

	public void setfOfn(int fOfn) {
		this.fOfn = fOfn;
	}

	public int getgOfn() {
		return gOfn;
	}

	public void setgOfn(int gOfn) {
		this.gOfn = gOfn;
	}

	public int gethOfn() {
		return hOfn;
	}

	public void sethOfn(int hOfn) {
		this.hOfn = hOfn;
	}

	public Board getParent() {
		return parent;
	}

	public void setParent(Board parent) {
		this.parent = parent;
	}

	public Board getGoalBoard() {
		return goalBoard;
	}

	public void setGoalBoard(Board goalBoard) {
		this.goalBoard = goalBoard;
	}
}
