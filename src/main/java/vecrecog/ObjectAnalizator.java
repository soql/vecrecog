package vecrecog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerBoosting;
import org.opencv.tracking.TrackerCSRT;
import org.opencv.tracking.TrackerGOTURN;
import org.opencv.tracking.TrackerKCF;
import org.opencv.tracking.TrackerMIL;
import org.opencv.tracking.TrackerMOSSE;
import org.opencv.tracking.TrackerMedianFlow;
import org.opencv.tracking.TrackerTLD;

public class ObjectAnalizator implements Runnable{
	List<OneObject> objectsList=new ArrayList<OneObject>();
	
	private Mat mat;
	
	public void analize(MatOfRect matOfRect, Mat mat) {
		this.mat=mat;
		Rect[] rects = matOfRect.toArray();
			if(rects.length==0)
				return;
		for(int i=0; i<rects.length; i++) {
			inteligentAdd(rects[i]);		
		}
	}
	private void inteligentAdd(Rect rect) {
		Rect2d rect2d=new Rect2d(rect.tl(), rect.br());
		if(objectsList.isEmpty()) {			
			addObject(rect2d);
			return;
		}
		
		for(int i=0; i<objectsList.size(); i++) {
			OneObject object=objectsList.get(i);
			if(object.isLost())
				continue;
			if(isNear(object, rect2d)) {
				object.update(rect2d);
				System.out.println("OBJECT UPDATED "+object);
			}else {
				addObject(rect2d);
			}
		}
	}
	void addObject(Rect2d rect2d){
		OneObject oneObject=new OneObject(rect2d.x, rect2d.y, rect2d.x+rect2d.width, rect2d.y+rect2d.height, new Date().getTime());
		objectsList.add(oneObject);
		System.out.println("ADD OBJECT "+oneObject);
		Tracker tracker=TrackerCSRT.create();
		tracker.init(mat, rect2d);
		oneObject.setTracker(tracker);
		
	}
	private boolean isNear(OneObject object, Rect2d rect) {
		int margines=150;	
		if(
				(Math.abs(object.getX1()-rect.x)<margines) &&
				(Math.abs(object.getY1()-rect.y)<margines) &&
				(Math.abs(object.getX2()-(rect.x+rect.width))<margines) &&
				(Math.abs(object.getY2()-(rect.y+rect.height))<margines))
				{
					return true;
				}
		return false;
	}
	public List<OneObject> getObjectsList() {
		return objectsList;
	}
	@Override
	public void run() {
		while(true) {
			List<OneObject> toRemove=new ArrayList<OneObject>();
			for(int i=0; i<objectsList.size(); i++) {
				OneObject object=objectsList.get(i);
				if(object.getLastUpdateTime()+2500<new Date().getTime()) {
					System.out.println("Utracono obiekt o id "+object.getId());
					object.setLost();
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
