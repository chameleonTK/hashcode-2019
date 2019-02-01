
public class Pair {
	int width, height;
	int x, y;

	public Pair(int w, int h) {
		this.width = w;
		this.height = h;
		
		this.x = w;
		this.y = h;
	}

	@Override
	public String toString() {
		return String.format("%d %d", width, height);
	}
}