package JDBCPackage;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;

@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener {
	String cmd = null;

	// DB Connectivity Attributes
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	private Container content;
	private JPanel detailsPanel;
	private JPanel exportButtonPanel;
	private JScrollPane dbContentsPanel;

	private Border lineBorder;

	private JLabel DexNoLabel = new JLabel("Dex Number:                 ");
	private JLabel NameLabel = new JLabel("Name:               ");
	private JLabel BSTLabel = new JLabel("Base Stat Total (BST):      ");
	private JLabel TypeLabel = new JLabel("Type:        ");
	private JLabel RegionLabel = new JLabel("Region:                 ");
	private JLabel ColourLabel = new JLabel("Colour:               ");
	private JLabel DexEntryLabel = new JLabel("Dex Entry:      ");
	private JLabel TrainerIDLabel = new JLabel("Trainer ID:      ");

	private JLabel NoOfPokemonLabel = new JLabel("No Of Pokemon:	");
	private JLabel RoleLabel = new JLabel("Role:	");
	private JLabel QuoteLabel = new JLabel("Quote:	");

	private JTextField DexNoTF = new JTextField(10);
	private JTextField NameTF = new JTextField(10);
	private JTextField BSTTF = new JTextField(10);
	private JTextField TypeTF = new JTextField(10);
	private JTextField RegionTF = new JTextField(10);
	private JTextField ColourTF = new JTextField(10);
	private JTextField DexEntryTF = new JTextField(50);
	private JTextField TrainerIDTF = new JTextField(10);

	private JTextField NoOfPokemonTF = new JTextField(10);
	private JTextField RoleTF = new JTextField(10);
	private JTextField QuoteTF = new JTextField(10);

	private static QueryTableModel TableModel = new QueryTableModel();
	// Add the models to JTabels
	private JTable TableofDBContents = new JTable(TableModel);
	// Buttons for inserting, and updating members
	// also a clear button to clear details panel
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton exportButton = new JButton("Export");
	private JButton deleteButton = new JButton("Delete");
	private JButton clearButton = new JButton("Clear");

	private JButton FindByRegion = new JButton("Find By Region:");
	private JTextField FindByRegionTF = new JTextField(12);
	private JButton FindByName = new JButton("Find By Name");
	private JTextField FindByNameTF = new JTextField(12);
	private JButton ListAllPokemon = new JButton("ListAllPokemon");
	private JButton ListAllTrainers = new JButton("ListAllTrainers");
	private JButton ViewAllData = new JButton("View All Data");
	private JButton getRandomButton = new JButton("Random Pokemon");

	// Variables to store selected entry
	private String selectedDexNo, selectedName, selectedBST, selectedType, selectedRegion, selectedColour,
			selectedDexEntry, selectedTrainerID;
	private String currentTable = "";

	public JDBCMainWindowContent(String aTitle) {
		// setting up the GUI
		super(aTitle, false, false, false, false);
		setEnabled(true);

		initiate_db_conn();
		// add the 'main' panel to the Internal Frame
		content = getContentPane();
		content.setLayout(null);
		content.setBackground(Color.LIGHT_GRAY);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

		// setup details panel and add the components to it
		detailsPanel = new JPanel();
		detailsPanel.setLayout(new GridLayout(11, 2));
		detailsPanel.setBackground(Color.LIGHT_GRAY);
		detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));

		detailsPanel.add(DexNoLabel);
		detailsPanel.add(DexNoTF);
		detailsPanel.add(NameLabel);
		detailsPanel.add(NameTF);
		detailsPanel.add(BSTLabel);
		detailsPanel.add(BSTTF);
		detailsPanel.add(TypeLabel);
		detailsPanel.add(TypeTF);
		detailsPanel.add(RegionLabel);
		detailsPanel.add(RegionTF);
		detailsPanel.add(ColourLabel);
		detailsPanel.add(ColourTF);
		detailsPanel.add(DexEntryLabel);
		detailsPanel.add(DexEntryTF);
		detailsPanel.add(TrainerIDLabel);
		detailsPanel.add(TrainerIDTF);

		// setup details panel and add the components to it
		exportButtonPanel = new JPanel();
		exportButtonPanel.setLayout(new GridLayout(6, 2));
		exportButtonPanel.setBackground(Color.LIGHT_GRAY);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
		exportButtonPanel.add(FindByRegion);
		exportButtonPanel.add(FindByRegionTF);
		exportButtonPanel.add(FindByName);
		exportButtonPanel.add(FindByNameTF);
		exportButtonPanel.add(ListAllPokemon);
		exportButtonPanel.add(ListAllTrainers);
		exportButtonPanel.add(ViewAllData);
		exportButtonPanel.add(getRandomButton);

		exportButtonPanel.setSize(500, 200);
		exportButtonPanel.setLocation(3, 300);
		content.add(exportButtonPanel);

		insertButton.setSize(100, 30);
		updateButton.setSize(100, 30);
		exportButton.setSize(100, 30);
		deleteButton.setSize(100, 30);
		clearButton.setSize(100, 30);

		insertButton.setLocation(370, 10);
		updateButton.setLocation(370, 110);
		exportButton.setLocation(370, 160);
		deleteButton.setLocation(370, 60);
		clearButton.setLocation(370, 210);

		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		exportButton.addActionListener(this);
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);

		this.ListAllPokemon.addActionListener(this);
		this.ListAllTrainers.addActionListener(this);
		this.FindByRegion.addActionListener(this);
		this.FindByName.addActionListener(this);
		this.ViewAllData.addActionListener(this);
		this.getRandomButton.addActionListener(this);

		content.add(insertButton);
		content.add(updateButton);
		content.add(exportButton);
		content.add(deleteButton);
		content.add(clearButton);

		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbContentsPanel = new JScrollPane(TableofDBContents, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbContentsPanel.setBackground(Color.LIGHT_GRAY);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Database Content"));

		detailsPanel.setSize(360, 300);
		detailsPanel.setLocation(3, 0);
		dbContentsPanel.setSize(700, 300);
		dbContentsPanel.setLocation(477, 0);

		content.add(detailsPanel);
		content.add(dbContentsPanel);

		setSize(982, 645);
		setVisible(true);

		TableModel.refreshFromDB(stmt, "SELECT * FROM Pokemon");

		// Add mouse listener to detect when a row is selected, for update and deleting
		TableofDBContents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				System.out.println("Mouse clicked, currentTable = " + currentTable);

				int selectedRow = TableofDBContents.getSelectedRow();

				selectedDexNo = (String) TableModel.getValueAt(selectedRow, 0);
				selectedName = (String) TableModel.getValueAt(selectedRow, 1);
				selectedBST = (String) TableModel.getValueAt(selectedRow, 2);
				selectedType = (String) TableModel.getValueAt(selectedRow, 3);
				selectedRegion = (String) TableModel.getValueAt(selectedRow, 4);
				selectedColour = (String) TableModel.getValueAt(selectedRow, 5);
				selectedDexEntry = (String) TableModel.getValueAt(selectedRow, 6);
				selectedTrainerID = (String) TableModel.getValueAt(selectedRow, 7);

				System.out.println(selectedDexNo);
				System.out.println(selectedName);

				System.out.println("Col 0 = " + TableModel.getValueAt(selectedRow, 0));
				System.out.println("Col 1 = " + TableModel.getValueAt(selectedRow, 1));
				System.out.println("Col 2 = " + TableModel.getValueAt(selectedRow, 2));
				System.out.println("Col 3 = " + TableModel.getValueAt(selectedRow, 3));
				System.out.println("Col 4 = " + TableModel.getValueAt(selectedRow, 4));
				System.out.println("Col 5 = " + TableModel.getValueAt(selectedRow, 5));
				System.out.println("Col 6 = " + TableModel.getValueAt(selectedRow, 6));
				System.out.println("Col 7 = " + TableModel.getValueAt(selectedRow, 7));

				if (selectedRow != -1 && currentTable.equals("Pokemon")) {
					// Extract values from the selected row
					System.out.println("If Statement Success");
					DexNoTF.setText(selectedDexNo);
					NameTF.setText(String.valueOf(TableofDBContents.getValueAt(selectedRow, 1)));
					BSTTF.setText(String.valueOf(TableofDBContents.getValueAt(selectedRow, 2)));
					TypeTF.setText(String.valueOf(TableofDBContents.getValueAt(selectedRow, 3)));
					RegionTF.setText(String.valueOf(TableofDBContents.getValueAt(selectedRow, 4)));
					ColourTF.setText(String.valueOf(TableofDBContents.getValueAt(selectedRow, 5)));
					DexEntryTF.setText(String.valueOf(TableofDBContents.getValueAt(selectedRow, 6)));
					TrainerIDTF.setText(String.valueOf(TableofDBContents.getValueAt(selectedRow, 7)));
				}
			}
		});
	}

	public void initiate_db_conn() {
		try {
			// Load the JConnector Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Specify the DB Name
			String url = "jdbc:mysql://localhost:3308/JDBCProject";

			// Connect to DB using DB URL, Username and password
			con = DriverManager.getConnection(url, "root", "");

			// Create a generic statement which is passed to the TestInternalFrame1
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("Error: Failed to connect to database\n" + e.getMessage());
		}
	}

	// simple clear text fields function
	public void clearFields() {
		for (Component c : detailsPanel.getComponents()) {
			if (c instanceof JTextField textField) {
				textField.setText("");
			}
		}
	}

	// event handling for buttons
	public void actionPerformed(ActionEvent e) {
		Object target = e.getSource();
		if (target == clearButton) {
			clearFields();
		}

		// INSERT feature, i used a PreparedStatement object because we used it during
		// work placement and it worked better for dynamic entries
		if (target == insertButton) {
			try {
				// PreparedStatements also don't handle special characters well
//				String updateTemp ="INSERT INTO Pokemon VALUES("+
//				DexNoTF.getText() +",'"+
//				NameTF.getText()+"','"+
//				BSTTF.getText()+"','"+
//				TypeTF.getText()+"','"+
//				RegionTF.getText()+"','"+
//				ColourTF.getText()+"','"+
//				DexEntryTF.getText()+"',"+
//				TrainerIDTF.getText()+");";
//				stmt.executeUpdate(updateTemp);

				String sql = "INSERT INTO Pokemon (DexNo, Name, BST, Type, Region, Colour, DexEntry, TrainerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = con.prepareStatement(sql);

				pstmt.setString(1, DexNoTF.getText());
				pstmt.setString(2, NameTF.getText());
				pstmt.setString(3, BSTTF.getText());
				pstmt.setString(4, TypeTF.getText());
				pstmt.setString(5, RegionTF.getText());
				pstmt.setString(6, ColourTF.getText());
				pstmt.setString(7, DexEntryTF.getText());
				pstmt.setString(8, TrainerIDTF.getText());

				pstmt.executeUpdate();
				pstmt.close();

				JOptionPane.showMessageDialog(this, "New Pokemon inserted successfully!");
				TableModel.refreshFromDB(pstmt, "SELECT * FROM Pokemon");
				clearFields();

			} catch (SQLException sqle) {
				sqle.printStackTrace();
				System.err.println("Error with  insert:\n" + sqle.toString());
			} finally {
				TableModel.refreshFromDB(stmt, "SELECT * FROM Pokemon");
			}
		}

		// uses a prepared statement to handle the special characters that can handle
		// the special characters that can be used for the DexEntry field
		if (target == updateButton) {
			try {
				String sql = "UPDATE Pokemon SET Name=?, BST=?, Type=?, Region=?, colour=?, DexEntry=?, TrainerID=? WHERE DexNo=?";
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, NameTF.getText());
				pstmt.setString(2, BSTTF.getText());
				pstmt.setString(3, TypeTF.getText());
				pstmt.setString(4, RegionTF.getText());
				pstmt.setString(5, ColourTF.getText());
				pstmt.setString(6, DexEntryTF.getText());
				pstmt.setString(7, TrainerIDTF.getText());
				pstmt.setString(8, DexNoTF.getText());

				int rows = pstmt.executeUpdate();
				pstmt.close();

				if (rows > 0) {
					JOptionPane.showMessageDialog(null, "Record Updated successfully");
				} else {
					JOptionPane.showMessageDialog(null, "No record found to update. ");
				}
				TableModel.refreshFromDB(pstmt, "SELECT * FROM Pokemon");

			} catch (Exception e1) {
				e1.printStackTrace();
			}

//		        String updateSQL = "UPDATE Pokemon SET " +
//		                "Name = '" + NameTF.getText() + "', " +
//		                "BST = '" + BSTTF.getText() + "', " +
//		                "Type = '" + TypeTF.getText() + "', " +
//		                "Region = '" + RegionTF.getText() + "', " +
//		                "colour = '" + ColourTF.getText() + "', " +
//		                "DexEntry = '" + DexEntryTF.getText() + "', " +
//		                "TrainerID = '" + TrainerIDTF.getText() + "' " +
//		                "WHERE DexNo = '" + DexNoTF.getText() + "';";
//
//		        stmt.executeUpdate(updateSQL);
//		        JOptionPane.showMessageDialog(null, "Record updated successfully!");
//		    }
//		    catch (SQLException sqle)
//		    {
//		        sqle.printStackTrace();
//		        JOptionPane.showMessageDialog(null, "Error updating record: " + sqle.getMessage());
//		    }
//		    finally
//		    {
//		        TableModel.refreshFromDB(stmt, "SELECT * FROM Pokemon");
//		    }
		}

		// delete function, has sql function so i could convert that to java
		if (target == deleteButton) {
			try {
//				DELETE FROM Pokemon 
//				WHERE 
//				DexNo LIKE '1' AND
//				Name LIKE 'Bulb' AND
//				BST LIKE '313' AND
//				Type LIKE 'grass' AND
//				Region LIKE 'kanto' AND
//				colour LIKE 'green' AND
//				DexEntry LIKE 'test' AND
//				TrainerID LIKE '1'; 
				String updateTemp = "DELETE FROM Pokemon WHERE DexNo LIKE '" + DexNoTF.getText() + "' AND Name LIKE '"
						+ NameTF.getText() + "' AND BST LIKE '" + BSTTF.getText() + "' AND Type LIKE '"
						+ TypeTF.getText() + "' AND Region LIKE '" + RegionTF.getText() + "' AND colour LIKE '"
						+ ColourTF.getText() + "' AND DexEntry LIKE '" + DexEntryTF.getText() + "' AND TrainerID LIKE '"
						+ TrainerIDTF.getText() + "';";

				stmt.executeUpdate(updateTemp);

				clearFields();

			} catch (SQLException sqle) {
				sqle.printStackTrace();
				System.err.println("Error with  insert:\n" + sqle.toString());
			} finally {
				TableModel.refreshFromDB(stmt, "SELECT * FROM Pokemon");
			}
		}

		// sets shown table to Pokemon, also used to refresh when data is updated
		if (target == this.ListAllPokemon) {
			// if this if statement doesn't exist, shown table wont switch

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ignore) {
				}
				rs = null;
			}

			cmd = "select * from Pokemon;";
			currentTable = "Pokemon";

			try {

				TableModel.refreshFromDB(stmt, cmd);
				rebuildCrudPanel("Pokemon");
				System.out.println(rs);
				System.out.println("Saving file to: " + new File("MyOutput.csv").getAbsolutePath());
				rs = stmt.executeQuery(cmd);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

		// same as List All Pokemon but for Trainers
		if (target == this.ListAllTrainers) {
			// if this if statement doesn't exist, shown table wont switch

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ignore) {
				}
				rs = null;
			}

			cmd = "SELECT * FROM trainers;";
			try {
				currentTable = "Trainers";
				TableModel.refreshFromDB(stmt, cmd);
				rebuildCrudPanel("Trainers");
				System.out.println(rs);
				System.out.println("Saving file to: " + new File("MyOutput.csv").getAbsolutePath());
				rs = stmt.executeQuery(cmd);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		// simple select statement using WHERE clause
		if (target == this.FindByRegion) {
			String region = this.FindByRegionTF.getText().trim();

			if (region.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a region name first");
				return;
			}

			try {
				// function depends on which table is selected
				if (currentTable.equals("Pokemon")) {
					cmd = "SELECT * FROM Pokemon WHERE Region = '" + region + "';";
				} else {
					cmd = "SELECT * FROM Trainers WHERE Region = '" + region + "';";
				}
				System.out.println("Executing query: " + cmd);
				TableModel.refreshFromDB(stmt, cmd);
				rs = stmt.executeQuery(cmd);
			}

			catch (Exception e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error in finding region");
			}
		}

		// simple SELECT query using WHERE for Name field
		if (target == this.FindByName) {
			String name = this.FindByNameTF.getText().trim();

			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a  name first");
				return;
			}

			try {
				// function depends on which table is selected
				if (currentTable.equals("Pokemon")) {
					cmd = "SELECT * FROM Pokemon WHERE Name = '" + name + "';";
				} else {
					cmd = "SELECT * FROM Trainers WHERE Name = '" + name + "';";
				}
				System.out.println("Executing query: " + cmd);
				TableModel.refreshFromDB(stmt, cmd);
				rs = stmt.executeQuery(cmd);
			}

			catch (Exception e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error in finding name");
			}
		}

		// uses INNER JOIN to combine both tables
		if (target == this.ViewAllData) {
			try {
				String joinQuery = "SELECT P.DexNo, P.Name AS PokemonName, P.BST, P.Type, P.Region AS PokemonRegion, P.colour, P.TrainerID AS Owner_ID, "
						+ " T.Name AS TrainerName, T.NoOfPokemon, T.Region AS TrainerRegion, T.Role, T.Quote "
						+ " FROM Pokemon P INNER JOIN Trainers T " + " ON P.TrainerID = T.TrainerID;";

				System.out.println("Executing join query: " + joinQuery);

				TableModel.refreshFromDB(stmt, joinQuery);
				rs = stmt.executeQuery(joinQuery);

			} catch (Exception e3) {
				e3.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error performing table join");
			}
		}

		// uses stored procedure in SQL database to get a random Pokemon
		if (target == getRandomButton) {

			try {
				String sql = "{CALL GetRandomPokemon()}";
				CallableStatement cstmt = con.prepareCall(sql);
				ResultSet rs = cstmt.executeQuery();

				if (rs.next()) {
					String dexNo = rs.getString("DexNo");
					String name = rs.getString("Name");
					String bst = rs.getString("BST");
					String type = rs.getString("Type");
					String region = rs.getString("Region");
					String colour = rs.getString("colour");
					String dexEntry = rs.getString("DexEntry");
					String trainerID = rs.getString("TrainerID");

					DexNoTF.setText(dexNo);
					NameTF.setText(name);
					BSTTF.setText(bst);
					TypeTF.setText(type);
					RegionTF.setText(region);
					ColourTF.setText(colour);
					DexEntryTF.setText(dexEntry);
					TrainerIDTF.setText(trainerID);

					TableModel.refreshFromDB(stmt, "SELECT * FROM Pokemon WHERE DexNo = " + dexNo);

					JOptionPane.showMessageDialog(this, "Random Pokemon: " + name + " (BST " + bst + ")");
				} else {
					JOptionPane.showMessageDialog(this, "No Pokemon found in database");
				}

				rs.close();
				cstmt.close();
			} catch (SQLException e3) {
				e3.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error fetching random Pokemon: " + e3.getMessage());
			}
		}
		// Exports data dynamically, so the selected table doesn't affect it or cause
		// issues
		if (target == exportButton) {
			try {
				if (currentTable == null || currentTable.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No table selected");
					return;
				}
				String exportQuery;
				if ("Pokemon".equalsIgnoreCase(currentTable)) {
					exportQuery = "SELECT * FROM Pokemon";
				} else if ("Trainers".equalsIgnoreCase(currentTable)) {
					exportQuery = "SELECT * FROM Trainers";
				} else {
					// shouldn't ever go here
					exportQuery = null;
				}
				if (exportQuery != null) {
					ResultSet rs = stmt.executeQuery(exportQuery);
					writeCsvFileFromResultSet(rs, "MyOutput.csv");
					rs.close();
					JOptionPane.showMessageDialog(this, "Exported current table to MyOutput.csv");
				} else {
					writeCsvFileFromTableModel(TableModel, "MyOutput.csv");
					JOptionPane.showMessageDialog(this, "Exported current view to MyOutput.csv");
				}
			} catch (Exception exportException) {
				exportException.printStackTrace();
				JOptionPane.showMessageDialog(this, "Exported failed: " + exportException.getMessage());
			}
		}
	}

	// handles CSV generation so that the number of rows and columns doesn't cause
	// issues
	private void writeCsvFileFromTableModel(QueryTableModel model, String filePath) {
		int cols = model.getColumnCount();
		try (FileOutputStream fos = new FileOutputStream(filePath);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				PrintWriter pw = new PrintWriter(osw)) {
			// For converting the data to Excel compatible formats
			fos.write(0xEF);
			fos.write(0xBB);
			;
			fos.write(0xBF);
			// to print headers
			for (int c = 0; c < cols; c++) {
				pw.print(escapeCsvField(model.getColumnName(c)));
				if (c < cols - 1)
					pw.print(",");
			}
			pw.print("\n");
			// to print rows
			int rows = model.getRowCount();
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					Object val = model.getValueAt(r, c);
					pw.print(escapeCsvField(val == null ? "" : String.valueOf(val)));
					if (c < cols - 1)
						pw.print(",");
				}
				pw.print("\n");
				pw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// same as previous function but if the selected table is a smaller version, ie.
	// if only a few rows are selected
	private void writeCsvFileFromResultSet(ResultSet rs, String filePath) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();

		try (FileOutputStream fos = new FileOutputStream(filePath);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				PrintWriter pw = new PrintWriter(osw)) {
			// Have to include this so excel detects the data
			fos.write(0xEF);
			fos.write(0xBB);
			fos.write(0xBF);

			// To print the header rows
			for (int i = 1; i <= numColumns; i++) {
				pw.print(escapeCsvField(rsmd.getColumnLabel(i)));
				if (i < numColumns)
					pw.print(",");
			}
			pw.print("\n");

			// To print the data rows
			while (rs.next()) {
				for (int i = 1; i <= numColumns; i++) {
					String value = rs.getString(i);
					pw.print(escapeCsvField(value));
					if (i < numColumns)
						pw.print(",");
				}
				pw.print("\n");
			}
			pw.flush();
		}
	}

	// Function used to escape the quotes in CSV file, found online on stack
	// overflow
	private String escapeCsvField(String value) {
		if (value == null)
			return "";
		boolean needQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
		String v = value.replace("\"", "\"\""); // double the quotes
		if (needQuote)
			return "\"" + v + "\"";
		return v;
	}

	// refreshes left panel on java gui
	private void rebuildCrudPanel(String tableName) {
		currentTable = tableName;
		detailsPanel.removeAll();

		if ("Pokemon".equalsIgnoreCase(tableName)) {
			detailsPanel.add(DexNoLabel);
			detailsPanel.add(DexNoTF);
			detailsPanel.add(NameLabel);
			detailsPanel.add(NameTF);
			detailsPanel.add(BSTLabel);
			detailsPanel.add(BSTTF);
			detailsPanel.add(TypeLabel);
			detailsPanel.add(TypeTF);
			detailsPanel.add(RegionLabel);
			detailsPanel.add(RegionTF);
			detailsPanel.add(ColourLabel);
			detailsPanel.add(ColourTF);
			detailsPanel.add(DexEntryLabel);
			detailsPanel.add(DexEntryTF);
			detailsPanel.add(TrainerIDLabel);
			detailsPanel.add(TrainerIDTF);
		}

		else if ("Trainers".equalsIgnoreCase(tableName)) {
			detailsPanel.add(TrainerIDLabel);
			detailsPanel.add(TrainerIDTF);
			detailsPanel.add(NameLabel);
			detailsPanel.add(NameTF);
			detailsPanel.add(NoOfPokemonLabel);
			detailsPanel.add(NoOfPokemonTF);
			detailsPanel.add(RegionLabel);
			detailsPanel.add(RegionTF);
			detailsPanel.add(RoleLabel);
			detailsPanel.add(RoleTF);
			detailsPanel.add(QuoteLabel);
			detailsPanel.add(QuoteTF);
		}

		detailsPanel.revalidate();
		detailsPanel.repaint();

		System.out.println("Details panel updated, set to: " + tableName);

		// old code, broke when adding Trainer table
//		currentTable = tableName;
//		detailsPanel.removeAll();
//		detailsPanel.revalidate();
//		detailsPanel.repaint();
//		
//		try {
//			String metaQuery = "SELECT * FROM " + tableName + " LIMIT 1;";
//			ResultSet rsMeta = stmt.executeQuery(metaQuery);
//			
//			if (rsMeta == null) {
//				System.out.println("No metadata found for table: "+ tableName);
//				return;
//			}
//			
//			ResultSetMetaData meta = rsMeta.getMetaData();
//			int columnCount = meta.getColumnCount();
//			
//			// Create the labels and text fields for the details panel
//			for(int i = 1; i<= columnCount; i++) {
//				String columnName = meta.getColumnName(i);
//				JLabel label = new JLabel(columnName + ": ");
//				JTextField textField = new JTextField(25);
//				textField.setName(columnName);
//				
//				detailsPanel.add(label);
//				detailsPanel.add(textField);
//				
//			}
//			
//			rsMeta.close();
//			detailsPanel.revalidate();
//			detailsPanel.repaint();
//			System.out.println("Details panel updated, set to: " + tableName);		
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	// old unused function, not needed anymore
	private void writeToFile(ResultSet rs) {
		try {
			System.out.println("In writeToFile");
			FileWriter outputFile = new FileWriter("MyOutput.csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for (int i = 0; i < numColumns; i++) {
				printWriter.print(rsmd.getColumnLabel(i + 1) + ",");
			}
			printWriter.print("\n");
			while (rs.next()) {
				for (int i = 0; i < numColumns; i++) {
					printWriter.print(rs.getString(i + 1) + ",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
