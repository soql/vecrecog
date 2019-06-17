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
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerBoosting;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

public class VideoCaptureHandler implements Runnable {
	private Mat mat = new Mat();

	private VideoCapture capturedVideo;

	private VideoWriter videoWriter=null;

	private CarDetector carDetector = null;

	private ObjectAnalizator objectAnalizator = null;
	
	private boolean analizeEnabled=false;

	public VideoCaptureHandler(String arg) {
		if(arg!=null && !arg.equals("")) {
			System.out.println("Running local file video capture");
			 capturedVideo = getFileVideoCapture(arg);			
		}else{
			System.out.println("Running live video capture");
			capturedVideo = getLiveVideoCapture();
		}
		initVideoWriter();
	}

	public void initVideoWriter() {
		Size size = new Size(capturedVideo.get(Videoio.CAP_PROP_FRAME_WIDTH),
				capturedVideo.get(Videoio.CAP_PROP_FRAME_HEIGHT));

		videoWriter = new VideoWriter("appsrc ! queue ! videoconvert ! video/x-raw ! omxh264enc ! video/x-h264 ! h264parse ! rtph264pay ! udpsink host=localhost port=5000 sync=false", VideoWriter.fourcc('m', 'p', '4', 'v'), 15,
				size, true);

		System.out.println(videoWriter.isOpened());
	}

	public VideoCapture getFileVideoCapture(String arg) {
		System.out.println("Getting file");
		capturedVideo = new VideoCapture();
		boolean isOpened = capturedVideo.open(arg);		
		System.out.println("Getting file ok");
		return capturedVideo;
	}

	public VideoCapture getLiveVideoCapture() {
		capturedVideo = new VideoCapture();

		String addressString = "rtsp://admin:@192.168.1.10:554/mode=real&idc=1&ids=1";
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

	public void display(Mat frame) {
		objectAnalizator.getObjectsList().stream().forEach(object -> {
			object.drawAnalizator(frame);
			object.drawTracker(frame);
					});
		// each rectangle in faces is a face: draw them!
		/*
		 * Rect[] facesArray = faces.toArray(); for (int i = 0; i < facesArray.length;
		 * i++) { Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new
		 * Scalar(7, 255, 90), 4); System.out.println(facesArray[i].tl());
		 * System.out.println(facesArray[i].br()); } Imgproc.rectangle(mat, r.tl(),
		 * r.br(),new Scalar(0, 0, 0), 4);
		 */
	}

	public BufferedImage getImage() {
		display(mat);
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
		int i = 0;
		while (capturedVideo.read(mat)) {
			i++;
			if(analizeEnabled) {
				analizeFrame();
				updateTrackers();
			}
			if(videoWriter!=null)
				videoWriter.write(mat);
			if(!analizeEnabled) {
			 try { Thread.sleep(10); } catch (InterruptedException e) { }
			}
			
		}
		capturedVideo.release();
		videoWriter.release();

	}

	private void updateTrackers() {
		objectAnalizator.getObjectsList().forEach(object -> object.updateTracker(mat));

	}

	private void analizeFrame() {
		if (carDetector != null) {
			MatOfRect matOfRect = carDetector.detect(mat);
			if (objectAnalizator != null) {
				objectAnalizator.analize(matOfRect, mat);
			}
		}

	}

	public void setCarDetector(CarDetector carDetector) {
		this.carDetector = carDetector;
	}

	public void setObjectAnalizator(ObjectAnalizator objectAnalizator) {
		this.objectAnalizator = objectAnalizator;
	}

	public void enableAnalize() {
		analizeEnabled=true;
		
	}

}
