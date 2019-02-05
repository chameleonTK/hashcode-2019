
public class Slice {

	public int fromX, toX, fromY, toY, score, dx, dy;

	public Slice(int x1, int y1, int dx, int dy) {
		this.fromX = x1;
		this.fromY = y1;

		this.toX = x1 + (dx) - 1;
		this.toY = y1 + (dy) - 1;

		this.dx = dx;
		this.dy = dy;
		this.score = dx * dy;
	}
	

	@Override
	public String toString() {
		return String.format("%d %d %d %d", fromX, fromY, toX, toY);
	}
}
