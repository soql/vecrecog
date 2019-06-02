package vecrecog;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;


public class VideoCaptureHandler implements Runnable{
	public Mat mat;
	
	CascadeClassifier faceCascade;
	
	VideoCapture capturedVideo;
	   
	MatOfRect faces = new MatOfRect();

	VideoWriter videoWriter;
	
	public VideoCaptureHandler() {
		mat = new Mat();
		faceCascade = new CascadeClassifier();		
		faceCascade.load("resources/cars.xml");
		//faceCascade.load("C:\\Program Files\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalcatface.xml");
		
		capturedVideo = getFileVideoCapture();			
		//capturedVideo = getLiveVideoCapture();
		Size size = new Size(capturedVideo.get(Videoio.CAP_PROP_FRAME_WIDTH), capturedVideo.get(Videoio.CAP_PROP_FRAME_HEIGHT));
		
		videoWriter = new VideoWriter("http://localhost:12500/feed1.ffm", VideoWriter.fourcc('m','j','p','g'),
                15, size, true);
		
		
		
		   
    		
		
	}
	
	public VideoCapture getFileVideoCapture (){
		System.out.println("Getting file");
		//capturedVideo = new VideoCapture("c:\\TEMP\\Camera1_21-34-07.avi");
		capturedVideo = new VideoCapture("c:\\TEMP\\ok.mp4");
							
				
		boolean isOpened = capturedVideo.open("c:\\TEMP\\ok.mp4");
		System.out.println("Getting file ok");	
		return capturedVideo;
	}
	
	
	public VideoCapture getLiveVideoCapture(){
		capturedVideo = new VideoCapture();
							
		String addressString = "rtsp://admin:@zjc.oth.net.pl:554/mode=real&idc=1&ids=1";		
		boolean isOpened = capturedVideo.open(addressString);
		openRTSP(isOpened, capturedVideo, mat);
		return capturedVideo;
	}
	
	
	private void openRTSP(boolean isOpened, VideoCapture capturedVideo, Mat cameraMat) {
		if (isOpened) {
			boolean tempBool = capturedVideo.read(cameraMat);
			System.out.println("VideoCapture returned mat? " + tempBool);

			if (!cameraMat.empty()) {
				System.out.println("Print image size: " + cameraMat.size());
				// processing image captured in cameraMat object

			} else {
				System.out.println("Mat is empty.");
			}
		} else {
			System.out.println("Camera connection problem. Check addressString");
		}
	}
	public void detectAndDisplay(Mat frame, CascadeClassifier faceCascade)
	{
		int absoluteFaceSize=0;
		
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
		
		// detect faces
		
		faceCascade.detectMultiScale(grayFrame, faces, 1.1, 5, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(absoluteFaceSize, absoluteFaceSize), new Size());
				
		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();		
		for (int i = 0; i < facesArray.length; i++)
		{
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(7, 255, 90), 4);
			System.out.println(facesArray[i].tl());	
			System.out.println(facesArray[i].br());	
		}
		
			
		
	
	}
	
	public void display(Mat frame, CascadeClassifier faceCascade)
	{	
		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();		
		for (int i = 0; i < facesArray.length; i++)
		{
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(7, 255, 90), 4);
			System.out.println(facesArray[i].tl());	
			System.out.println(facesArray[i].br());	
		}
		
			
		
	
	}
	
	public BufferedImage getImage() {		
		display(mat, faceCascade);
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".jpg", mat, mob);
		byte ba[] = mob.toArray();

		BufferedImage bi;
		try {
			bi = ImageIO.read(new ByteArrayInputStream(ba));
			return bi;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void run() {		
	while (capturedVideo.read(mat)) {		
			detectAndDisplay(mat, faceCascade);			
            videoWriter.write(mat);         
			 /*try { Thread.sleep(250);
             } catch (InterruptedException e) {    }*/
        }
		capturedVideo.release();
        videoWriter.release();
		
	}
}
