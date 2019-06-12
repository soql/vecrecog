package vecrecog;

import org.opencv.core.Core;
import org.opencv.osgi.OpenCVNativeLoader;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	
	public static void main(String[] args) {		
			new Main();											
	}
	public Main() {
		//SpringApplication.run(Main.class, args);
		try {
			String path = "C:\\Program Files\\opencv\\build\\bin";
			System.load(path + "\\opencv_ffmpeg346_64.dll");
			System.load(path + "\\openh264-1.7.0-win64.dll");
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Error loading libs");
		}
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		CarDetector carDetector=new CarDetector();
		ObjectAnalizator objectAnalizator=new ObjectAnalizator();
		
		VideoCaptureHandler videoCaptureHandler=new VideoCaptureHandler();
		videoCaptureHandler.setCarDetector(carDetector);
		videoCaptureHandler.setObjectAnalizator(objectAnalizator);
		HttpStreamServer httpStreamServer=new HttpStreamServer(videoCaptureHandler);
		MyFrame myFrame=new MyFrame(videoCaptureHandler);
		
		new Thread(myFrame).start();
		new Thread(videoCaptureHandler).start();
		new Thread(objectAnalizator).start();
		//new Thread(httpStreamServer).start();
		
		
		
	}

	
	

	

}
