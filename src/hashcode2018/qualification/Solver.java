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
	
	public int calculateScore(Slide a, Slide b) {
		ArrayList<Slide> slide = new ArrayList<Slide>();
		int common = 0, onlyfirst = 0, onlysecond = 0, minscore = 0, finalscore = 0;
		Slide slide1, slide2;
		ArrayList<String> commontags;
		
		slide1 = a;
		slide2 = b;
		commontags = new ArrayList<String>(slide1.tags);
		commontags.retainAll(slide2.tags);
		common = commontags.size();
		onlyfirst = slide2.tags.size()- common;
		onlysecond = slide1.tags.size() - common;
		minscore = Math.min(common, Math.min(onlyfirst, onlysecond));
		finalscore += minscore;
		
		return finalscore;
	}
	
	public int calculateScore(List<Slide> slides) {
		List<Slide> slide = slides;
		int common = 0, onlyfirst = 0, onlysecond = 0, minscore = 0, finalscore = 0;
		Slide slide1, slide2;
		ArrayList<String> commontags;

		int i = 1;
		while (i < slides.size()) {
			slide1 = slide.get(i-1);
			slide2 = slide.get(i);
			
			commontags = new ArrayList<String>(slide1.tags);
			commontags.retainAll(slide2.tags);
			common = commontags.size();
			onlyfirst = slide2.tags.size() - common;
			onlysecond = slide1.tags.size() - common;
			minscore = Math.min(common, Math.min(onlyfirst, onlysecond));
			finalscore += minscore;
//			slide.remove(0);
			i ++;
		}
		return finalscore;
	}
	
	public List<Slide> solve(PhotoSlideShowProblem pb) {
		List<Slide> slides = new ArrayList<Slide>();
		
		List<List<Photo>> allPhotos = separateHorizontalAndVertical(pb.photos);
		List<Photo> horizontalPhotos = allPhotos.get(0);
		List<Photo> verticalPhotos = allPhotos.get(1);
		
		int V = 0, H=0;
		if (verticalPhotos.size() > 0) {
			V = verticalPhotos.get(0).M;
		}
		
		if (horizontalPhotos.size() > 0) {
			H = horizontalPhotos.get(0).M;
		}
		
		Collections.sort(verticalPhotos, new Comparator<Photo>(){
		    public int compare(Photo s2, Photo s1) {
		        return s1.tags.size() - s2.tags.size();
		    }
		});
		
		Collections.sort(horizontalPhotos, new Comparator<Photo>(){
		    public int compare(Photo s2, Photo s1) {
		        return s1.tags.size() - s2.tags.size();
		    }
		});
		
		for(int i = 0; i < verticalPhotos.size()/2; i++) {
			Photo a = verticalPhotos.get(i);
			Photo b = verticalPhotos.get(verticalPhotos.size() - 1 - i);
			
			slides.add(new Slide(a, b));
		}
		
		for(int i = 0; i < horizontalPhotos.size(); i++) {
			Photo currentPhoto = horizontalPhotos.get(i);
			slides.add(Slide.horizontalToSlide(currentPhoto));
		}
		
		Collections.sort(slides, new Comparator<Photo>(){
		    public int compare(Photo s2, Photo s1) {
//		    	return s1.tags.size() - s2.tags.size();
		    	if (s1.getTags().size() == s2.getTags().size()) {
		    		return s1.mm.compareTo(s2.mm);
		    	} else {
			        return s1.tags.size() - s2.tags.size();
		    	}
		    }
		});
		
		int W = 200;
		for(int i=0; i<slides.size()-1; i++) {
			Slide a = slides.get(i);
			
			int m = i+1;
			int maxScore = -1;
			for(int j=1; j < W && i+j< slides.size(); j++) {
				Slide b = slides.get(i+j);
				int score = calculateScore(a, b);
//				System.out.println(score);
				if (score > maxScore) {
					maxScore = score;
					m = i+j;
				}
			}
			
//			System.out.println((i+1)+" "+m+" "+maxScore);
//			System.out.println(slides.get(i+1).id+" "+slides.get(m).id);
			Collections.swap(slides, i+1, m);
//			System.out.println(slides.get(i+1).id+" "+slides.get(m).id);
//			break;
		}
//		pb.photos.get(0).tags.get(0)

		System.out.println("SCORE:"+calculateScore(slides));
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
