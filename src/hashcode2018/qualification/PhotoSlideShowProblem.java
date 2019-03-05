package hashcode2018.qualification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import hashcode2018.practice.PizzaProblem;
import hashcode2018.practice.Slice;

public class PhotoSlideShowProblem {

	int N;
	List<Photo> photos;
	private Solver solver;
	public static void main(String[] args) {
		//a_example
		args = new String[] {"a_example", "b_lovely_landscapes", "c_memorable_moments", "d_pet_pictures", "e_shiny_selfies"};

		for (String s: args) {
			PhotoSlideShowProblem pb = PhotoSlideShowProblem.load(""+s + ".txt");
			PhotoSlideShowProblem.save(s + ".out", pb.solve());
		}
		
	}
	
	public PhotoSlideShowProblem (int n, List<Photo> photos) {
		this.N = n;
		this.photos = photos;
		System.out.println("N="+n);
		this.solver = new Solver();
	}
	
	public List<Slide> solve() {
		return solver.solve(this);
	}
	
	protected static void save(String filename, List<Slide> slides) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));
			bw.write(slides.size()+"\n");
			for(Slide s: slides) {
				if (s.b == null) {
					bw.write(String.format("%d\n", s.a.id));
				} else {
					bw.write(String.format("%d %d\n", s.a.id, s.b.id));
				}
			}
			bw.flush();
		} catch (Exception ex) {
			System.err.println("Err"+ ex.getMessage());
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

	protected static PhotoSlideShowProblem load(String filename) {
		BufferedReader br = null;

		int n = 0;
		List<Photo> photos = new ArrayList<Photo>();
		try {
			br = new BufferedReader(new FileReader(filename));
			int line_no = 0;

			while (br.ready()) {
				// Get details
				if (line_no == 0) {
					String line = br.readLine();
					n = Integer.parseInt(line);
				} else {
					for (int j = 0; j < n; j++) {
						String line = br.readLine();
						String[] sp = line.split(" ");
						List<String> tags = new ArrayList<String>();
						for(int p=2; p<sp.length; p++) {
							tags.add(sp[p]);
						}
						
						Photo ph = new Photo(j, sp[0], Integer.parseInt(sp[1]), tags);
						photos.add(ph);
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

		return (new PhotoSlideShowProblem(n, photos));
	}
}
