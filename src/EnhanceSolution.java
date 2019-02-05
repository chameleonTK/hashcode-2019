import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnhanceSolution extends GreedySolver {

	List<Slice> slices;
	private int highestScore = 0, score = 0;
	private List<Slice> hightestSlices;
	private List<Slice> removingSlices;
	
	public static void main(String[] args) {
		args = new String[] { "res/d_big", "res/d_big" };
		if (args.length <= 0) {
			System.out.println("Wrong arguments");
			return;
		}

		PizzaProblem pb = PizzaProblem.load(args[0] + ".in");
		EnhanceSolution solution = EnhanceSolution.load(args[1] + ".out", pb);
		PizzaProblem.save(args[0] + "_enhance.out", solution.cilmbing(pb));
	}
	


	public EnhanceSolution(PizzaProblem pb, List<Slice> slices) {
		this.slices = slices;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Slice> cilmbing(PizzaProblem pb) {
		List<Shape> possibleShapes = generateAllShapes(pb.min_topping, pb.max_size);

		int[][] sliceMask = new int[pb.rows][pb.columns];
		List<Pair> frontier = new ArrayList<Pair>();
	    
	    this.score = 0;
	    int max_score = pb.columns * pb.rows;
	    
	    int index = 0;
	    for(Slice s: this.slices) {
	    	markSlice(new Pair(s.fromX, s.fromY), new Shape(s.dy, s.dx), sliceMask, index+1);
	    	this.score += s.dx * s.dy;
	    	index++;
	    }
	
	    for(int i=0; i < pb.rows; i++) {
	    	for(int j=0; j< pb.columns; j++) {
	    		if(sliceMask[i][j] == 0) {
	    			frontier.add(new Pair(i, j));
	    		}
	    	}
	    }
	    
	    System.out.println("READY with "+this.score);
	    
	    int iters = frontier.size(), r = 0;
	    for(r = 0; r < iters; r++) {
	    	if (this.score >= max_score) {
	    		break;
	    	}
	    	
	    	Pair location = getAvailableLocation(frontier, sliceMask);
	    	if (location == null) {
	    		break;
	    	}
	    	
	    	int sc = score;
	    	removingSlices = new ArrayList<Slice>();
	    	sliceMask = removeNeigbours(location, sliceMask);
	    	List<Pair> newPatch = findPatch(location.x, location.y, sliceMask, new ArrayList<Pair>());
	    	clearUp(sliceMask, newPatch, 0);
 
	    	Collections.sort(newPatch);

	    	if (true) {
	    		highestScore = 0;
		    	hightestSlices = new ArrayList<Slice>();
	    		System.out.println("patch size "+newPatch.size());
	    		exhaustiveSearch(sliceMask, newPatch, 0, possibleShapes, pb, new ArrayList<Slice>(), 0);
	    		
	    		for(Slice s: hightestSlices) {
		    		markSlice(new Pair(s.fromX, s.fromY), new Shape(s.dy, s.dx), sliceMask, this.slices.size()+1);
		    		this.score += s.dy * s.dx;
					this.slices.add(s);
					
		    	}
	    	} else {
	    		sc = score;
	    		for(Slice s: removingSlices) {
		    		markSlice(new Pair(s.fromX, s.fromY), new Shape(s.dy, s.dx), sliceMask, this.slices.size()+1);
		    		this.score += s.dy * s.dx;
					this.slices.add(s);
					
		    	}
	    	}
	    	
	    	
//	    	
	    	if (r%1000 ==0) {
	    		System.out.println("progress: "+r);
	    	}
	    }
	    
	    
	    System.out.print(String.format("Iter: %d/%d\nScore: %d/%d\n", r,iters, this.score, max_score));
	    List<Slice> newSlices = new ArrayList<Slice>();
	    for(Slice s: this.slices) {
	    	if (s != null) {
	    		newSlices.add(s);
	    	}
	    }
		return newSlices;
	}

	
	private List<Slice> exhaustiveSearch(int[][] sliceMask, List<Pair> frontier, int frontierIndex, List<Shape> possibleShapes, PizzaProblem pb, List<Slice> newSlices, int score) {
		int iters = frontier.size(), r = 0;
		
//		System.out.println("Start" + highestScore);
//    	print(sliceMask);
    	
    	if (score > highestScore) {
    		highestScore = score;
    		hightestSlices = (List<Slice>) ((ArrayList) newSlices).clone();;
    	}
    	
	    for(r = 0; r < iters; r++) {
	    	Pair location = null;
	    	
	    	while (frontierIndex < frontier.size()) {
				Pair candidate = frontier.get(frontierIndex);
				frontierIndex++;
				if (sliceMask[candidate.x][candidate.y] == 0) {
					location = candidate;	
					break;
				}
			}
	    	
	    	if (location == null) {
	    		break;
	    	}
			
//	    	System.out.println("cardicate location "+location);
	    	for(Shape candidate: possibleShapes) {
				if (satisfyConstraints(location, candidate, sliceMask, pb)) {
//					System.out.println("Shape "+candidate+" Location "+location);
					markSlice(location, candidate, sliceMask, this.slices.size() + newSlices.size()+1);
					score += candidate.height * candidate.width;
					newSlices.add(new Slice(location.x, location.y, candidate.height, candidate.width));
//					print(sliceMask);
//					System.out.println("Recur to");
					exhaustiveSearch(sliceMask, frontier, 0, possibleShapes, pb, newSlices, score);
//					System.out.println("Come back "+candidate+" "+location);
			
					markSlice(location, candidate, sliceMask, 0);
					score -= candidate.height * candidate.width;
					newSlices.remove(newSlices.size()-1);
				}
			}
	    	
//	    	System.out.println("Done loop");
	    }
	    
	    return newSlices;
	}



	private boolean outOfBorder(int x, int y, int[][] sliceMask) {
		if (x >= sliceMask.length || x < 0) {
			return true;
		}
		
		
		if (y >= sliceMask[x].length || y < 0) {
			return true;
		}
		
		
		return false;
	}

	private int[][] removeNeigbours(Pair location, int[][] sliceMask) {
		
		int index = sliceMask[location.x][location.y];
		List<Pair> patch = findPatch(location.x, location.y, sliceMask, new ArrayList<Pair>());
		clearUp(sliceMask, patch, index);
	
		for(Pair p: patch) {
			int x = location.x, y = location.y;
			
			if (!outOfBorder(x+1, y-1, sliceMask) && sliceMask[x+1][y-1] != 0) {
			    recurRemove(x+1, y-1, sliceMask);
			}

			if (!outOfBorder(x+1, y, sliceMask) && sliceMask[x+1][y] != 0) {
			    recurRemove(x+1, y, sliceMask);
			}

			if (!outOfBorder(x+1, y+1, sliceMask) && sliceMask[x+1][y+1] != 0) {
			    recurRemove(x+1, y+1, sliceMask);
			}

			if (!outOfBorder(x, y+1, sliceMask) && sliceMask[x][y+1] != 0) {
			    recurRemove(x, y+1, sliceMask);
			}

			if (!outOfBorder(x, y-1, sliceMask) && sliceMask[x][y-1] != 0) {
			    recurRemove(x, y-1, sliceMask);
			}

			if (!outOfBorder(x-1, y+1, sliceMask) && sliceMask[x-1][y+1] != 0) {
			    recurRemove(x-1, y+1, sliceMask);
			}

			if (!outOfBorder(x-1, y, sliceMask) && sliceMask[x-1][y] != 0) {
			    recurRemove(x-1, y, sliceMask);
			}

			if (!outOfBorder(x-1, y-1, sliceMask) && sliceMask[x-1][y-1] != 0) {
			    recurRemove(x-1, y-1, sliceMask);
			}
		}
		
		return sliceMask;
	}



	private List<Pair> findPatch(int x, int y, int[][] sliceMask, List<Pair> lst) {
		lst.add(new Pair(x, y));
		
		int index = sliceMask[x][y];
		sliceMask[x][y] = -1;
		
		
		if (!outOfBorder(x+1, y, sliceMask) && sliceMask[x+1][y] == index) {
			findPatch(x+1, y, sliceMask, lst);
		}

		if (!outOfBorder(x, y+1, sliceMask) && sliceMask[x][y+1] == index) {
			findPatch(x, y+1, sliceMask, lst);
		}

		if (!outOfBorder(x, y-1, sliceMask) && sliceMask[x][y-1] == index) {
			findPatch(x, y-1, sliceMask, lst);
		}

		if (!outOfBorder(x-1, y, sliceMask) && sliceMask[x-1][y] == index) {
			findPatch(x-1, y, sliceMask, lst);
		}

		return lst;
	}

	private void clearUp(int[][] sliceMask, List<Pair> lst, int index) {
		for(Pair p: lst) {
			sliceMask[p.x][p.y] = index;
		}
	}


	private void recurRemove(int x, int y, int[][] sliceMask) {
		int index = sliceMask[x][y];
		if (index >= 1) {
			Slice s = slices.get(index-1);
			if (s!=null) {
				removingSlices.add(s);
			}
			slices.set(index - 1, null);
		}
		List<Pair> patch = findPatch(x, y, sliceMask, new ArrayList<Pair>());
		
		this.score -= patch.size();
		clearUp(sliceMask, patch, 0);
	}



	protected static EnhanceSolution load(String filename, PizzaProblem pb) {
		BufferedReader br = null;
		List<Slice> slices = new ArrayList<Slice>();
		
		try {
			br = new BufferedReader(new FileReader(filename));
			int line_no = 0;
			int n = 0;
			while (br.ready()) {
				String line = br.readLine();

				// Get details
				if (line_no == 0) {
					n = Integer.parseInt(line);
				} else {
					String[] info = line.split(" ");

					int fromX = Integer.parseInt(info[0]);
					int fromY  = Integer.parseInt(info[1]);
					int toX = Integer.parseInt(info[2]);
					int toY = Integer.parseInt(info[3]);
					
					int dx = toX - fromX +1;
					int dy = toY - fromY +1;
					slices.add(new Slice(fromX, fromY, dx, dy));
				}
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

		return (new EnhanceSolution(pb, slices));
	}
}
