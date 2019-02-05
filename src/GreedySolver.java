import java.util.ArrayList;
import java.util.List;

public class GreedySolver extends Solver {
	protected List<Slice> solve(PizzaProblem pb) {

	    System.out.println("Min Topping: "+ pb.min_topping+"\nMax Size: "+pb.max_size);
	    
	    List<Shape> possibleShapes = generateAllShapes(pb.min_topping, pb.max_size);
	    
//	    for(int i=0; i < pb.rows; i++) {
//	    	for(int j=0; j< pb.columns; j++) {
//	    		System.out.print(pb.pizza[i][j]);
//	    	}
//	    	System.out.println();
//	    }
	    
//	    for(Shape s: possibleShapes) {
//	    	System.out.println(s);
//	    }
	    
	    int[][] sliceMask = new int[pb.rows][pb.columns];
	    List<Slice> slices = new ArrayList<Slice>();
	    
	    List<Pair> locationFrontier = new ArrayList<Pair>();
	    for(int j=0; j<pb.columns; j++) {
	    	for(int i=0; i<pb.rows; i++) {
	    		locationFrontier.add(new Pair(i, j));
	    	}
	    }

	    int score = 0;
	    int iters = pb.columns*pb.rows, r = 0;
	    
	    for(r = 0; r < iters; r++) {
	    	Pair location = getAvailableLocation(locationFrontier, sliceMask);
	    	if (location == null) {
	    		break;
	    	}
	    	
	    	Shape s = findSatisfyShape(location, possibleShapes, sliceMask, pb);
	    	
	    	if (s == null) {
	    		continue;
	    	}
	    	
	    	score += s.height*s.width;
	    	sliceMask = markSlice(location, s, sliceMask, r+1);
	    	slices.add(new Slice(location.x, location.y, s.height, s.width));
	    	
	    	if (r%1000 ==0) {
	    		System.out.println("progress: "+r);
	    	}
	    	
//	    	System.out.println("Shape "+s+" Location "+location);
//	    	System.out.println("Step "+r);
//	    	for(int i=0; i < pb.rows; i++) {
//		    	for(int j=0; j< pb.columns; j++) {
//		    		System.out.print(sliceMask[i][j]);
//		    	}
//		    	System.out.println();
//		    }
//	    	System.out.println();
	    }
	    
	    print(sliceMask);
	    System.out.print(String.format("Iter: %d/%d\nScore: %d/%d\n", r,iters, score, pb.columns*pb.rows));
		return slices;
	}
	
	protected int[][] markSlice(Pair location, Shape shape, int[][] sliceMask, int index) {
		
		if (index == 0) {
			for(int i = location.x; i < location.x + shape.height; i++) {
				for(int j = location.y; j < location.y + shape.width; j++) {
					if (sliceMask[i][j] != 0) {
						return sliceMask;
					}
				}
			}
		}
		
		for(int i = location.x; i < location.x + shape.height; i++) {
			for(int j = location.y; j < location.y + shape.width; j++) {
				sliceMask[i][j] = index;
			}
		}
		return sliceMask;
	}

	private Shape findSatisfyShape(Pair location, List<Shape> possibleShapes, int[][] sliceMask, PizzaProblem pb) {
		for(Shape candidate: possibleShapes) {
			if (satisfyConstraints(location, candidate, sliceMask, pb)) {
				//Assume that possibleShapes is sorted by its area so the first candidate should give the maximum score;
				return candidate;
//				if (candidate == null || candidate.width*candidate.height < s.width*s.height) {
//					candidate = s;
//				}
			}
		}
		
		return null;
	}

	protected boolean satisfyConstraints(Pair location, Shape shape, int[][] sliceMask, PizzaProblem pb) {
//		System.out.println("Check location: "+location+" with shape "+shape);
		if (location.x + shape.height > pb.rows) {
			return false;
		}
		
		if (location.y + shape.width > pb.columns) {
			return false;
		}
		
		int mushrooms=0, tomatoes=0;
		for(int i = location.x; i < location.x + shape.height; i++) {
			for(int j = location.y; j < location.y + shape.width; j++) {
				if (sliceMask[i][j] != 0) {
					return false;
				}
				mushrooms += pb.pizza[i][j];
			}
		}
		
		
		tomatoes = (shape.width * shape.height) - mushrooms;
		if (tomatoes >= pb.min_topping && mushrooms >= pb.min_topping) {
//			System.out.println("Success");
			return true;
		}
		
		return false;
	}

	protected Pair getAvailableLocation(List<Pair> locationFrontier, int[][] sliceMask) {
		while (!locationFrontier.isEmpty()) {
			Pair candidate = locationFrontier.remove(0);
			if (sliceMask[candidate.x][candidate.y] == 0) {
				return candidate;
			}
		}
		
		return null;
	}
	
	
	protected List<Shape> generateAllShapes(int min_topping, int max_size) {
		List<Shape> possible_shapes = new ArrayList<Shape>();
		for(int size = max_size; size >= 2*min_topping; size--) {
			for(int i=1; i<=size; i++) {
				if (size%i==0) {
					possible_shapes.add(new Shape(i, size/i));
				}
			}
		}

		return possible_shapes;
	}
	
	protected void print(int[][] arr) {
		for(int i=0; i < arr.length; i++) {
	    	for(int j=0; j< arr[i].length; j++) {
	    		System.out.print(arr[i][j]+" ");
	    	}
	    	System.out.println();
	    }
    	System.out.println();
	}

}
