package iaProjectFolder;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class OpenQuestionTable extends JFrame {

	private JPanel contentPane;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpenQuestionTable frame = new OpenQuestionTable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public OpenQuestionTable() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 400, 600, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(87, 48, 439, 266);
		contentPane.add(scrollPane);
		
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JLabel titleLbl = new JLabel("Select a Question Table to Edit");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setBounds(153, 6, 314, 16);
		contentPane.add(titleLbl);
		

	}
}
