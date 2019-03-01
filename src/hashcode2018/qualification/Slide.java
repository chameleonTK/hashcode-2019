package hashcode2018.qualification;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Slide extends Photo{
	Photo a, b;
	
	

	public Slide(Photo a, Photo b) {
		super(a.id, a.type, a.M, a.tags);


		if (b!=null) {
			Set<String> mergetags = new LinkedHashSet<String>();
			mergetags.addAll(a.tags);
			mergetags.addAll(b.tags);
	
			List<String> mergetagsLst = new ArrayList<String>();
			for(String t:mergetags) {
				mergetagsLst.add(t);
			}
			this.tags = mergetagsLst;
			this.M = this.tags.size();
		}

		this.a = a;
		this.b = b;
	}

	public static void toSlide(List<Photo> photos) {
		List<Slide> slides = new ArrayList<Slide>();
		for(int i=0; i<photos.size(); i++) {
			Photo ph = photos.get(i);
			if (ph.type.equals("H")) {
				slides.add(new Slide(ph, null));
			} else {
				slides.add(new Slide(ph, photos.get(i+1)));
				i++;
			}
		}
	}

	public static Slide horizontalToSlide(Photo photo) {
		if (photo.type.equals("H")) {
			return new Slide(photo, null);
		} else {
			return null;
		}
	}
}
