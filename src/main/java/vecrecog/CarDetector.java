package vecrecog;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.tracking.TrackerBoosting;

public class CarDetector {
	
	CascadeClassifier cascade;
			
	public CarDetector() {
		cascade = new CascadeClassifier();		
		cascade.load("resources/cars.xml");		
	}
	
	public MatOfRect detect(Mat frame)
	{
		int absoluteFaceSize=0;
		int absoluteMaxFaceSize=0;
		
		MatOfRect results=new MatOfRect();
		
		Mat grayFrame = new Mat();
		
		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);
		
		// compute minimum face size (20% of the frame height, in our case)
		if (absoluteFaceSize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.05f) > 0)
			{
				absoluteFaceSize = Math.round(height * 0.05f);
			}
		}
		if (absoluteMaxFaceSize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				absoluteMaxFaceSize = Math.round(height * 0.2f);
			}
		}
		
		try {
			cascade.detectMultiScale(grayFrame, results, 1.1, 8, 0 | Objdetect.CASCADE_SCALE_IMAGE,
					new Size(absoluteFaceSize, absoluteFaceSize), new Size(absoluteMaxFaceSize,absoluteMaxFaceSize));
		} catch (Exception e) {
			System.out.println("B³¹d "+e);
		}
						
		return results;												
	}

	
}
