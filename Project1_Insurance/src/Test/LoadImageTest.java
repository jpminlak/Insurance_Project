package Test;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoadImageTest extends JFrame{

	private MyImagePanel myImagePanel;
	private JPanel jp = new JPanel();
	
	public LoadImageTest() {
		initData();
		setInitLayout();
		
	}

	private void initData() {
		setTitle("이미지 활용 연습");
		setSize(1200,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		myImagePanel = new MyImagePanel();
	}

	private void setInitLayout() {
		add(myImagePanel);
		setVisible(true);
	}
	
	static class MyImagePanel extends JPanel{
		private Image image;
		
		public MyImagePanel() {
			image = new ImageIcon("C:/work/loginimage.jpg").getImage();
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(image,0,0,1200,700,null);	// image,x,y,width,height,observer
		}
	}
}
