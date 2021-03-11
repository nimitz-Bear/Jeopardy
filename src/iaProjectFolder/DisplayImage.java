package iaProjectFolder;

import java.awt.BorderLayout;
//import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class DisplayImage extends JFrame {

	private JPanel contentPane;

//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					DisplayImage frame = new DisplayImage();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public DisplayImage(String imageToDisplay) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int width;
		int height;
		setBounds(100, 100, width = 1500, height = 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel imgLabel = new JLabel("");
		imgLabel.setToolTipText("click to close");
		imgLabel.setSize(width, height);
		contentPane.add(imgLabel, BorderLayout.CENTER);
		imgLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//                mouseClicked(e);
				System.out.println("" + e);

//				QuestionEdit frame = new QuestionEdit();
//				frame.setVisible(true);
				dispose();
			}
		});
		
		BufferedImage imgToScale = null;
		try {
			imgToScale = ImageIO.read(new File(imageToDisplay));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int imgHeight = imgToScale.getHeight();
		int imgWidth = imgToScale.getWidth();
//		System.out.println
		
		Image dimg = null;
		if (imgWidth < imgLabel.getWidth() && imgHeight < imgLabel.getHeight()) {
//			System.out.println("");
			dimg = imgToScale.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
//			ImageIcon imageIcon = new ImageIcon(dimg);
			setBounds(100, 100, width = imgWidth, height = imgHeight);
			imgLabel.setSize(imgWidth, imgHeight);
			
		} else {
			dimg = imgToScale.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), Image.SCALE_SMOOTH);
//			ImageIcon imageIcon = new ImageIcon(dimg);
			
		}

//		dimg = imgToScale.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), Image.SCALE_SMOOTH);
		imgLabel.setHorizontalAlignment(JLabel.CENTER);
		ImageIcon imageIcon = new ImageIcon(dimg);
		imgLabel.setIcon(imageIcon);

	}

}
