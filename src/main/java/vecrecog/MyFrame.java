package vecrecog;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MyFrame extends JFrame implements Runnable{
	private JPanel contentPane;
	private VideoCaptureHandler videoCaptureHandler;
	public MyFrame(VideoCaptureHandler videoCaptureHandler) {
		                
	    this.setVisible(true);	              		
		this.videoCaptureHandler=videoCaptureHandler;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1280, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);


	}

	@Override
	public void paint(Graphics g) {
		g = contentPane.getGraphics();
		g.drawImage(videoCaptureHandler.getImage(), 0, 0, this);
	}

	public void run() {
		  for (;;){
              repaint();              
              try { Thread.sleep(10);
              } catch (InterruptedException e) {    }
          }
		
	}
	
	
}
