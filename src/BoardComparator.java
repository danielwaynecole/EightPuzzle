import java.util.Comparator;

/**
 * @author ucoleda
 * this class is used to sort the priority queue based on estimated cost
 */
public class BoardComparator implements Comparator<Board> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Board b1, Board b2) {
		// use f(n) cost estimate value to compare
		return Integer.compare(b1.getfOfn(), b2.getfOfn());
	}

}
