import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class PizzaProblem {
	public int rows, columns, min_topping, max_size;
	public int[][] pizza;
	private Solver solver;

	public PizzaProblem(int rows, int columns, int min_topping, int max_size, int[][] pizza) {
		this.rows = rows;
		this.columns = columns;
		this.min_topping = min_topping;
		this.max_size = max_size;
		this.pizza = pizza;

		this.solver = new GreedySolver();
	}

	public List<Slice> solve() {
		return solver.solve(this);
	}

	public static void main(String[] args) {
		args = new String[] { "res/b_small" };
		if (args.length <= 0) {
			System.out.println("Wrong arguments");
			return;
		}

		PizzaProblem pb = PizzaProblem.load(args[0] + ".in");
		PizzaProblem.save(args[0] + ".out", pb.solve());
	}

	protected static void save(String filename, List<Slice> slices) {
		BufferedWriter bw = null;
		int score = 0;
		try {
			bw = new BufferedWriter(new FileWriter(filename));
			bw.write(slices.size()+"\n");
			for(Slice s: slices) {
				score += s.score;
				bw.write(String.format("%d %d %d %d\n", s.fromX, s.fromY, s.toX, s.toY));
			}
			bw.flush();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			}
		}
	}

	protected static PizzaProblem load(String filename) {
		BufferedReader br = null;

		int rows = 0, columns = 0, min_topping = 0, max_size = 0;
		int[][] pizza = null;

		try {
			br = new BufferedReader(new FileReader(filename));
			int line_no = 0;

			while (br.ready()) {
				String line = br.readLine();

				// Get details
				if (line_no == 0) {
					String[] info = line.split(" ");

					rows = Integer.parseInt(info[0]);
					columns = Integer.parseInt(info[1]);
					min_topping = Integer.parseInt(info[2]);
					max_size = Integer.parseInt(info[3]);

					pizza = new int[rows][columns];
				} else {
					for (int j = 0; j < columns; j++) {
						pizza[line_no - 1][j] = (line.charAt(j)=='M'?1:0);
					}
				}
//
				line_no++;
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			}
		}

		return (new PizzaProblem(rows, columns, min_topping, max_size, pizza));
	}
}
