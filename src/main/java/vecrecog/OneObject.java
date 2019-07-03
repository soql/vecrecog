package vecrecog;

import java.util.Date;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect2d;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_tracking.Tracker;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class OneObject {
	private double x1,y1,x2,y2;

	private long detectionTime;
	
	private long lastUpdateTime;
	
	private int id;
	
	private static int id_seq=0;
	
	private Tracker tracker;
	
	private Rect2d trackerRect=new Rect2d();
	
	private boolean lost=false;
	
	public OneObject(double x1, double y1, double x2, double y2, long detectionTime) {
		super();
		id=id_seq++;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.detectionTime = detectionTime;
		this.lastUpdateTime= detectionTime;
	}
	@Override
	public String toString() {	
		return "ObjectId: "+id+" ("+x1+" "+y1+" "+x2+" "+y2+") "+detectionTime;
	}
	public double getX1() {
		return x1;
	}
	public void setX1(double x1) {
		this.x1 = x1;
	}
	public double getY1() {
		return y1;
	}
	public void setY1(double y1) {
		this.y1 = y1;
	}
	public double getX2() {
		return x2;
	}
	public void setX2(double x2) {
		this.x2 = x2;
	}
	public double getY2() {
		return y2;
	}
	public void setY2(double y2) {
		this.y2 = y2;
	}
	public void update(Rect2d rect2d) {
		x1=rect2d.x();
		y1=rect2d.y();
		x2=rect2d.x()+rect2d.width();
		y2=rect2d.y()+rect2d.height();
		lastUpdateTime=new Date().getTime();
	}
	public Tracker getTracker() {
		return tracker;
	}
	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}
	public void updateTracker(Mat mat) {
		boolean ok=this.tracker.update(mat, trackerRect);
		if(!ok) {
			System.out.println("Utrata trackingu objektu "+id);
		}		
	}
	
	public Rect2d getTrackerRect() {
		return trackerRect;
	}
	public Rect2d getRectToDisplay() {
		//return trackerRect;
		return new Rect2d(x1,y1,x2-x1,y2-y1);
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public int getId() {
		return id;
	}
	public void drawAnalizator(Mat frame) {
		if(lost) {
			return;
		}
		if (getRectToDisplay() == null) {
			System.out.println("NULL");
			return;
		}
		
		rectangle(frame, new Point((int)getRectToDisplay().tl().x(),(int)getRectToDisplay().tl().y()), new Point((int)getRectToDisplay().br().x(), (int)getRectToDisplay().br().y()), new Scalar(0, 0, 0,0));		
	}
	
	public void drawTracker(Mat frame) {
		if (getTrackerRect() == null) {
			System.out.println("NULL");
			return;
		}
	
		rectangle(frame, new Point((int)getRectToDisplay().tl().x(),(int)getRectToDisplay().tl().y()), new Point((int)getRectToDisplay().br().x(), (int)getRectToDisplay().br().y()), new Scalar(0, 0, 0,0));	
	}
	public void setLost() {
		lost=true;
	}
	public boolean isLost() {
		return lost;
	}
	
	
	
	
	
}
