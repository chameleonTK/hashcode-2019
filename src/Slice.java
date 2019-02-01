
public class Slice {

	public int fromX, toX, fromY, toY, score, dx, dy;

	public Slice(int x1, int y1, int dx, int dy) {
		fromX = x1;
		fromY = y1;

		toX = x1 + (dx) - 1;
		toY = y1 + (dy) - 1;

		score = dx * dy;
	}
	

}
