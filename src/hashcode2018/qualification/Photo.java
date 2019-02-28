package hashcode2018.qualification;

import java.util.Iterator;
import java.util.List;

public class Photo {
	
	String type;
	int M, id;
	int lettercount=0;
	
	List<String> tags;
	
	public Photo(int id, String type, int M, List<String> tags) {
		this.id = id;
		this.type = type;
		this.M = M;
		this.tags = tags;
		calculateLetterCount();
		
	}
	public void calculateLetterCount() {
		for (Iterator<String> i = tags.iterator(); i.hasNext();) {
		    String item = i.next();
		    System.out.println(item);
		}
		
		this.lettercount
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getM() {
		return M;
	}
	public void setM(int m) {
		M = m;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
