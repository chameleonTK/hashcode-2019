package hashcode2018.practice;
public class Pair implements Comparable {
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
	
	@Override
    public int compareTo(Object cmp) {
        if (this.x == ((Pair)cmp).x) {
        	return this.y-((Pair)cmp).y;
        }
        return this.x-((Pair)cmp).x;
    }
}