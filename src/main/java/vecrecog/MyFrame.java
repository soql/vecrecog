package vecrecog;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MyFrame extends JFrame implements Runnable, KeyListener{
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
		addKeyListener(this);

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

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		System.out.println(arg0.getKeyCode());
		if(arg0.getKeyCode()==75) {
			videoCaptureHandler.enableAnalize();
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
