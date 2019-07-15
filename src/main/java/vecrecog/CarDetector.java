package vecrecog;


import org.bytedeco.javacv.ImageTransformer;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.Objdetect;

public class CarDetector {
	
	CascadeClassifier cascade;
		
	int absoluteFaceSize=0;
	int absoluteMaxFaceSize=0;
	
	public CarDetector() {
		cascade = new CascadeClassifier();		
		cascade.load("/tmp/resources/cars.xml");		
	}
	
	public RectVector detect(Mat frame)
	{	
		RectVector results=new RectVector();
		
		Mat grayFrame = new Mat();
		
		// convert the frame in gray scale		
		cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		equalizeHist(grayFrame, grayFrame);
		
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
			if (Math.round(height * 0.5f) > 0)
			{
				absoluteMaxFaceSize = Math.round(height * 0.5f);
			}
		}
		
		try {
			cascade.detectMultiScale(grayFrame, results, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
					new Size(absoluteFaceSize, absoluteFaceSize), new Size(absoluteMaxFaceSize,absoluteMaxFaceSize));
			System.out.println("DETECTION! "+results.size());
		} catch (Exception e) {
			System.out.println("B³¹d "+e);
		}
						
		return results;												
	}

	
}
