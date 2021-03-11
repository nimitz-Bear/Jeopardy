package iaProjectFolder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DataSuperClass extends JFrame {

	// https://www.youtube.com/watch?v=Z2O-V3TLPlQ&list=PLQ0bESK3a8XjkjI087CT8uC61FzC8idlM&index=2

	// used for editing questions/ QuestionEdit
	DefaultTableModel questionDTM = new DefaultTableModel();
	DefaultTableModel categoriesDTM = new DefaultTableModel();

	String[] databaseColumnNames = new String[] { "Question", "Answer", "Difficulty", "imageFilePath", "Category" };
	String[] columns = { "Question", "Answer", "Difficulty", "Image Path", "Category" };
	// changed some public functions' modifiers from static to non-static

	// used for the actual Jeopardy Game
	static LinkedList<String> selectedCategories = new LinkedList<String>();
	static LinkedList<Integer> teamPoints = new LinkedList<Integer>();
	static String[][] questionInfo;
	static String[][] answerInfo;
	static String[][] imageInfo;
	static int currentTeam = 1;
	static int teamCount;
	static boolean questionInfoGenerated = false;

	// primaryKey implementation no0tes
	// and, or, not: https://www.w3schools.com/sql/sql_and_or.asp
	// also reference mr fulo's notes

	private static Connection connect() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
//			con = DriverManager.getConnection("jdbc:sqlite::memory:"); // making a database in memory
			con = DriverManager.getConnection("jdbc:sqlite:jeaprodyQuestions.db"); // connecting to our database

			// need absolute classpath sometimes
			// jar has to be in the root folder for it to run on macos
//			con = DriverManager.getConnection("jdbc:sqlite: /Users/isaacdestura/Desktop/jeaprodyQuestions.db");
//			System.out.println("Connected!");
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e + "");
			e.printStackTrace();
		}
		return con;
	}

//	private void createPrimaryKey() {
//		//TODO primaryKey implementation
//	}

	// this whole function is temporary, and will eventually be replaced with sql
	// specific methods because it currently is inefficient
	public void tempWriteData() {
		Connection con = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		LinkedList<String> tempQuestions = new LinkedList<String>();
		System.out.println("Writing changes start");
		// loading Data to delete (temporary)
		String sql = "Select * FROM questions"; // telling sql what data is needed
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next() == true) { // checks if the next wrong is null or not
				String question = rs.getString("Question");
				tempQuestions.add(question);

				// may have to uncomment this if database is locked
//				String answer = rs.getString("Answer");
//				int difficulty = rs.getInt("Difficulty");
//				String imageFilePath = rs.getString("imageFilePath");
//				String category = rs.getString("Category");
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} finally {
			try {
				// close connections
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}

		// printing the questions column to clear the table
//		for (int i = 0; i < tempQuestions.size(); i++) {
//			System.out.println(tempQuestions.get(i));
//		}

		// deleting every row from questionDTM based on the row
		while (tempQuestions.size() != 0) {
			deleteQuestion(tempQuestions.get(0));
			tempQuestions.remove(0);
		}

		// cycling through the table to reupload all the data to the db file
		for (int row = 0; row < questionDTM.getRowCount(); row++) {
			Connection con2 = connect();
			PreparedStatement ps2 = null;
			try {
				String sql2 = "INSERT INTO questions(Question, Answer, Difficulty, imageFilePath, Category) VALUES(?,?,?,?,?) ";
				ps2 = con2.prepareStatement(sql2);

//				for (int column = 0; column < questionDTM.getColumnCount(); column++) {
//					if (column != 2) {
//						ps2.setString(column + 1, (String) questionDTM.getValueAt(row, column));
//					} else {
//						ps2.setInt(column + 1, (Integer) questionDTM.getValueAt(row, column));
//					}
				ps2.setString(1, (String) questionDTM.getValueAt(row, 0));
				ps2.setString(2, (String) questionDTM.getValueAt(row, 1));
//				ps2.setInt(3, (Integer) questionDTM.getValueAt(row, 2));
				ps2.setInt(3, Integer.parseInt(questionDTM.getValueAt(row, 2).toString()));
				ps2.setString(4, (String) questionDTM.getValueAt(row, 3));
				ps2.setString(5, (String) questionDTM.getValueAt(row, 4));

//				}
				ps2.execute();
				System.out.println("Data has been inserted sucessfully");
			} catch (SQLException f) {
				System.out.println("Data insertion has failed");
				f.printStackTrace();
			}

			// close connection
			try {
				ps2.close();
				con2.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(
						"Error occured when trying to close the connection, while writing the changes to SQLite");
			}
		}
		System.out.println("Writing changes to DB file end.");
	}

	// sql operates from open connection -> read/write -> close connection
	public void loadTableData() {
		Connection con = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		// otherwise, data might not be printed
		questionDTM.setColumnIdentifiers(columns);

		try {
//			String sql = "Select * FROM questions WHERE 6 = ?";
			String sql = "Select * FROM questions"; // telling sql what data is needed
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next() == true) { // checks if the next wrong is null or not

				Object[] entry = new Object[5];
//				Object[] entry = new Object[6];
				String question = rs.getString("Question");
				String answer = rs.getString("Answer");
				int difficulty = rs.getInt("Difficulty");
				String imageFilePath = rs.getString("imageFilePath");
				String category = rs.getString("Category");
//				int id = rs.getInt("primaryKey");

				// pretty inefficient
				entry[0] = question;
				entry[1] = answer;
				entry[2] = (Integer) difficulty;
				entry[3] = imageFilePath;
				entry[4] = category;
//				entry[5] = id;
//				primaryKey.add(id);
				questionDTM.addRow(entry);
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} finally {
			try {
				// close connections
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}

//		System.out.println("Print table");
//		for(int row = 0; row < questionDTM.getRowCount(); row++) {
//			for(int column = 0; column < questionDTM.getColumnCount(); column++) {
//				System.out.println(questionDTM.getValueAt(row, column));		
//			}
//		}
//		
//		for(int i = 0; i < primaryKey.size(); i++) {
//			System.out.println(primaryKey.get(i));
//		}

	}

	public LinkedList<String> loadCategories() {
		// TODO can also add quantity for each
		// linkedlist is necessary, because quantity will be included later

		categoriesDTM.setColumnIdentifiers(new Object[] { "Categories" });

		System.out.println("Loading categories");
		LinkedList<String> categoriesList = new LinkedList<String>();

		for (int row = 0; row < questionDTM.getRowCount(); row++) {
			if (categoriesDTM.getRowCount() == 0) {
//				categoriesDTM.setRowCount(categoriesDTM.getRowCount() + 1);
				categoriesDTM.addRow(new Object[] { questionDTM.getValueAt(0, 4) });
				categoriesList.add((String) questionDTM.getValueAt(0, 4));
			}

			for (int i = 0; i < categoriesList.size(); i++) {
				if (questionDTM.getValueAt(row, 4).toString().contentEquals(categoriesList.get(i))) {
					break;
				} else if (i == categoriesDTM.getRowCount() - 1) {
//					System.out.println(questionDTM.getValueAt(row, 4));
//					categoriesDTM.setRowCount(categoriesDTM.getRowCount() + 1);
					categoriesDTM.addRow(new Object[] { questionDTM.getValueAt(row, 4) });
					categoriesList.add((String) questionDTM.getValueAt(row, 4));
				}
			}

//			if (row == questionDTM.getRowCount() - 1) {

			
		}
		return categoriesList;
//		for (int row = 0; row < categoriesDTM.getRowCount(); row++) {
//			for (int column = 0; column < categoriesDTM.getColumnCount(); column++) {
//				System.out.println(categoriesDTM.getValueAt(row, column));
//			}
//		}
	}

	// AFAIK, this method is not fully functional
	public static void readSpecificRow() {
		Connection con = connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
//		String columnToPrint = "Question"; //designates column to print
		int columnToPrint = 1; // designates column to print
		String columnToSearch = "Category";
		String searchTerm = "Lol";

		try {
			String sql = "Select " + columnToPrint + " from questions where " + columnToSearch + " = ? ";
			// above telling sql what data is needed
			ps = con.prepareStatement(sql);
			ps.setString(1, searchTerm); // tells sql what it's looking for;
			// number refers to the column parameter which is ? in sql string
			rs = ps.executeQuery();

			// reads and prints result
			String question = rs.getString(1); // column to print
			System.out.println(question);
			// TODO print the other columns

		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} finally {
			try {
				// close connections
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
	}

	public static void insertQuestion(String Question, String Answer, int Difficulty, String imageFilePath,
			String Category) {
		Connection con = connect();
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO questions(Question, Answer, Difficulty, imageFilePath, Category) VALUES(?,?,?,?,?) ";
			ps = con.prepareStatement(sql);
			ps.setString(1, Question);
			ps.setString(2, Answer);
			ps.setInt(3, Difficulty);
			ps.setString(4, imageFilePath);
			ps.setString(5, Category);
//			ps.setString(6, tableName);
			// TODO implement ID system random number from 1 to 100 to ensure correct entry
			// is being edited
			ps.execute();

			// closes connection to db file

			System.out.println("Data has been inserted sucessfully");
		} catch (SQLException f) {
			System.out.println("Data insertion has failed");
			f.printStackTrace();
		}

		// close connection
		try {
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteQuestion(String questionValue) {
		Connection con = connect();
		PreparedStatement ps = null;
		try {
//			String sql = "delete from questions WHERE Question = ? and Answer = ?";
			String sql = "delete from questions WHERE Question = ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, questionValue);
//			ps.setString(1, "Whats the meaning of life");
//			ps.setString(2, "Hakuna Matata");
			ps.execute();
			System.out.println("Data has been deleted.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSQL(int selectedRow, int selectedColumn, JTextField searchInput,
			LinkedList<Integer> searchResults, DefaultTableModel questionDTM) {
		// TODO to make the saving manual, you can save it batches, and only send the
		// batch when the user presses the save button
		// http://tutorials.jenkov.com/jdbc/batchupdate.html

		Connection con = connect();
		PreparedStatement ps = null;
		String referenceColumn = "";

		// changes which column is used to find the cell, if the cell being edited is
		// the same as question
		if (selectedColumn == 0) {
			referenceColumn = databaseColumnNames[1]; // answer
		} else {
			referenceColumn = databaseColumnNames[0]; // question
		}

		try {
			String sql = "UPDATE questions set " + databaseColumnNames[selectedColumn] + " = ? Where " + referenceColumn
					+ " = ?";
			ps = con.prepareStatement(sql);

			// if statement accounts for updating from while searching and while not
			// searching
			if (searchInput.getText().contentEquals("")) {
//				System.out.println("Not searching");
				ps.setString(1, questionDTM.getValueAt(selectedRow, selectedColumn).toString());
//				System.out.println("updating value: " + questionDTM.getValueAt(selectedRow, selectedColumn));
				// if statement changes depending on which column was chosen
				// because column being updated and column being referenced cannot be the same
				if (selectedColumn == 0) {
//					System.out.println("reference: " + questionDTM.getValueAt(selectedRow, 1));
					ps.setString(2, (String) questionDTM.getValueAt(selectedRow, 1));
				} else {
//					System.out.println("reference: " + questionDTM.getValueAt(selectedRow, 0));
					ps.setString(2, (String) questionDTM.getValueAt(selectedRow, 0));
				}
				ps.execute();
//				System.out.println("Cell was edited");
			} else {
				// TODO uncomment this
				System.out.println("Currently searching");
				ps.setString(1, (String) questionDTM.getValueAt(searchResults.get(selectedRow), selectedColumn));
				// if statement changes depending on which column was chosen
				// because column being updated and column being referenced cannot be the same
				if (selectedColumn == 0) {
					ps.setString(2, (String) questionDTM.getValueAt(searchResults.get(selectedRow), 1));
				} else {
					ps.setString(2, (String) questionDTM.getValueAt(searchResults.get(selectedRow), 0));
				}
				ps.execute();
				System.out.println("Cell was edited");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// could use rs.next();
//		String sql = "UPDATE questions set Question = ?, set Answer = ?, set Difficulty = ?, imageFilePath = ?, Category = ? WHERE Category = ?";
		// basically update the whole row based on one value. List every value. However,
		// if value changed, we wont know where to refer
	}

	public int updateDisplay(JTextArea answerDisplay, JTextArea questionDisplay, JLabel imageLabel, JTable table,
			int lastSelectedRow, JTextField searchInput) {

		updateImageText(table, imageLabel, searchInput);

		// this code block updates the image display
		if (table.getSelectedRow() >= 0) {
			if (questionDTM.getValueAt(table.getSelectedRow(), 3).toString().contentEquals("") == false) {
				File imageFile = new File(questionDTM.getValueAt(table.getSelectedRow(), 3).toString());
				if (imageFile.exists()) {
					ImageIcon icon = new ImageIcon(questionDTM.getValueAt(table.getSelectedRow(), 3).toString());
					imageLabel.setIcon(icon);
				} else {
					imageLabel.setIcon(null);
					imageLabel.setText("File does not exist.");
				}
			} else {
				imageLabel.setIcon(null);
			}
		} else {
			// sets image to null if nothing is selected
			imageLabel.setIcon(null);
		}

		// this code block updates the text areas with the values of question
		// and answer columns, to make them more readable
//		System.out.println("Answer Display: " + answerDisplay.isFocusOwner());
//		System.out.println("Question Display: " + questionDisplay.isFocusOwner());

		if (table.getSelectedRow() == -1) {
			questionDisplay.setText("Question");
			answerDisplay.setText("Answer");
		}

		if ((answerDisplay.isFocusOwner() || questionDisplay.isFocusOwner()) && lastSelectedRow >= 0) {
//			userEditing = true;
//			System.out.println("inner a:" + answerDisplay.isFocusOwner() + " q:" + questionDisplay.isFocusOwner());

			table.setValueAt(questionDisplay.getText(), lastSelectedRow, 0);
			table.setValueAt(answerDisplay.getText(), lastSelectedRow, 1);
			return lastSelectedRow;
		} else {
			// first part ensures row is selected, second part ensures text isn't
			// overwritten

			// when not searching
			if (searchInput.getText().contentEquals("") && table.getSelectedRow() >= 0) {
				// below actually updates text area values
				questionDisplay.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				answerDisplay.setText(table.getValueAt(table.getSelectedRow(), 1).toString());

				lastSelectedRow = table.getSelectedRow(); // resets the last selected row, when not searching
			}

			// when searching
			else if (table.getSelectedRow() >= 0) {
				// below actually changes text areas, while searching
				questionDisplay.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				answerDisplay.setText(table.getValueAt(table.getSelectedRow(), 1).toString());

				lastSelectedRow = table.getSelectedRow(); // resets the last selected row, but for searching
			}
			return table.getSelectedRow();
		}

	}

	// updates the text in the image preview
	private void updateImageText(JTable table, JLabel imageLabel, JTextField searchInput) {

		if (table.getSelectedRow() >= 0) {
			if (searchInput.getText().contentEquals("")) {
				File fileToCheck = new File((String) questionDTM.getValueAt(table.getSelectedRow(), 3));

				if (fileToCheck.exists()) {
					imageLabel.setText(""); // clears the text on the screen so it won't show up
				} else {
					// should only appear when file found isn't an image or file doesn't exist
					imageLabel.setText("No Image found. Either file selected isn't an image or doesn't exist.");
				}
			} else {
				File fileToCheck = new File((String) table.getValueAt(table.getSelectedRow(), 3));

				if (fileToCheck.exists()) {
					imageLabel.setText(""); // clears the text on the screen so it won't show up
				} else {
					imageLabel.setText("No Image found. Either file selected isn't an image or doesn't exist.");
					// should only appear when file found isn't an image or file doesn't exist
				}
			}
		}

	}

	public void scaleImage(String imageToDisplay, JLabel imgLabel, JDialog displayImage, int width, int height) {
		BufferedImage imageInput = null;
		try {
			imageInput = ImageIO.read(new File(imageToDisplay));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int imgHeight = imageInput.getHeight();
		int imgWidth = imageInput.getWidth();

		Image dimg = null;
		if (imgWidth < imgLabel.getWidth() && imgHeight < imgLabel.getHeight()) {
//			System.out.println("");
			dimg = imageInput.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
//			ImageIcon imageIcon = new ImageIcon(dimg);
			displayImage.setBounds(100, 100, width = imgWidth, height = imgHeight);
			imgLabel.setSize(imgWidth, imgHeight);

		} else {
			dimg = imageInput.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), Image.SCALE_SMOOTH);
//			ImageIcon imageIcon = new ImageIcon(dimg);

		}

//		dimg = imgToScale.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), Image.SCALE_SMOOTH);
		imgLabel.setHorizontalAlignment(JLabel.CENTER);
		ImageIcon imageIcon = new ImageIcon(dimg);
		imgLabel.setIcon(imageIcon);
	}

	public void search(LinkedList<Integer> searchResults, DefaultTableModel searchDTM, DefaultTableModel questionDTM,
			JTable table, JTextField searchInput) {
		searchResults.clear(); // clear it so that the list wont keep doubling

		searchDTM.setRowCount(0); // clear searchTable, too
//		mainScrollPane.setViewportView(searchTable);
		table.setModel(searchDTM);

		String searchValue = searchInput.getText().toLowerCase();
		// the .toLowerCase basically means lower or upper case is ignored

		for (int row = 0; row < questionDTM.getRowCount(); row++) {
			for (int column = 0; column < questionDTM.getColumnCount(); column++) {

//				System.out.println(questionDTM.getValueAt(row, column));
				String tableValue = questionDTM.getValueAt(row, column).toString().toLowerCase();
				// the .toLowerCase basically means lower or upper case is ignored
				if (tableValue.contains(searchValue)) {

					searchResults.add(row);
					break; // so that it doesn't repeat rows with .contains
				}
			}
		}

		searchDTM.setRowCount(searchResults.size()); // need to set row count before adding
		for (int column = 0; column < searchDTM.getColumnCount(); column++) {
			for (int row = 0; row < searchDTM.getRowCount(); row++) {
				searchDTM.setValueAt(questionDTM.getValueAt(searchResults.get(row), column), row, column);
			}
		}

		if (searchInput.getText().contentEquals("")) {
			searchInput.setText(""); // lol
//			mainScrollPane.setViewportView(table);
			table.setModel(questionDTM);
		}
	}

}
