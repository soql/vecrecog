package vecrecog;

import org.opencv.core.Core;
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
		VideoCaptureHandler videoCaptureHandler=new VideoCaptureHandler();
		MyFrame myFrame=new MyFrame(videoCaptureHandler);
		new Thread(myFrame).start();
		new Thread(videoCaptureHandler).start();
		
	}

	
	

	

}
