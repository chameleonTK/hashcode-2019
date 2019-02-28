package hashcode2018.qualification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class Solver {
	Hashtable<Integer, List<Photo>> hashtable;
	
	public Solver() {
		this.hashtable = new Hashtable<Integer, List<Photo>>();
	}
	
	public int calculateScore(ArrayList<Slide> inputSlide) {
		ArrayList<Slide> slide = new ArrayList<Slide>();
		int common = 0, onlyfirst = 0, onlysecond = 0, minscore = 0, finalscore = 0;
		Slide slide1, slide2;
		ArrayList<String> commontags;

		while (slide.size() > 1) {
			slide1 = slide.get(0);
			slide2 = slide.get(1);
			commontags = new ArrayList<String>(slide1.tags);
			commontags.retainAll(slide2.tags);
			common = commontags.size();
			onlyfirst = common - slide2.tags.size();
			onlysecond = common - slide1.tags.size();
			minscore = Math.min(common, Math.min(onlyfirst, onlysecond));
			finalscore += minscore;
			slide.remove(0);
		}
		return finalscore;
	}
	
	public List<Slide> solve(PhotoSlideShowProblem pb) {
		List<Slide> slides = new ArrayList<Slide>();
		
		List<List<Photo>> allPhotos = separateHorizontalAndVertical(pb.photos);
		List<Photo> horizontalPhotos = allPhotos.get(0);
		List<Photo> verticalPhotos = allPhotos.get(1);
		
//		Collections.sort(pb.photos, new Comparator<Photo>(){
//		    public int compare(Photo s2, Photo s1) {
//		        return s1.tags.size() - s2.tags.size();
//		    }
//		});
		Collections.sort(horizontalPhotos, new Comparator<Photo>(){
		    public int compare(Photo s2, Photo s1) {
		        return s1.tags.size() - s2.tags.size();
		    }
		});
		//slides.add(Slide.toSlide(photos);)
		for(int i = 0; i < horizontalPhotos.size(); i++) {
			Photo currentPhoto = horizontalPhotos.get(i);
			slides.add(Slide.horizontalToSlide(currentPhoto));
//			if (i == 0 || i == horizontalPhotos.size()-1) {
//				slides.add(Slide.horizontalToSlide(currentPhoto));
//			}
//			else {
//				//System.out.println(currentPhoto.id+" "+currentPhoto.tags.size());
//				currentPhoto.getTags().size();
//				for (int j = i; j < horizontalPhotos.size(); j++) {
//					
//				}
//			}
			
		}
//		pb.photos.get(0).tags.get(0)

		return slides;
	}
	
	public List<List<Photo>> separateHorizontalAndVertical(List<Photo> photoList) {
		List<Photo> verticalPhotos = new ArrayList<Photo>();
		List<Photo> horizontalPhotos = new ArrayList<Photo>();
		List<List<Photo>> result = new ArrayList<List<Photo>>();
		for(Photo photo : photoList) {
			if (photo.type.equals("H")) {
				horizontalPhotos.add(photo);
			}
			else if (photo.type.equals("V")) {
				verticalPhotos.add(photo);
			}
		}
		result.add(horizontalPhotos);
		result.add(verticalPhotos);
		return result;
	}
}
