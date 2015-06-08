package view;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.User;
import controller.DatabaseConstants;
import controller.ProjectDB;

//NOT FINISHED
public class GridProjects extends JPanel
{
	private JTable grid;
	private User user;
	private JPanel content;
	private String connectionString = DatabaseConstants.PROJECT_MANAGEMENT_DB;
	
	public GridProjects(User user)
	{
		
		this.user = user;
		this.setLayout(new BorderLayout());
		
		//UNCOMMENT THE FOLLOWING TO ACTIVATE THE INITIAL GRID VIEW (VERY EARLY STAGES)
		
//		try {
//			grid = new JTable(buildTableModel(ProjectDB.RSgetUserProjects(connectionString, user.getId())) );
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
//		this.add(grid, BorderLayout.CENTER);
		
	}
	
//	public static DefaultTableModel buildTableModel(ResultSet rs)
//	        throws SQLException {
//
//	    ResultSetMetaData metaData = rs.getMetaData();
//
//	    // names of columns
//	    Vector<String> columnNames = new Vector<String>();
//	    int columnCount = metaData.getColumnCount();
//	    for (int column = 1; column <= columnCount; column++) {
//	        columnNames.add(metaData.getColumnName(column));
//	    }
//
//	    // data of the table
//	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
//	    while (rs.next()) {
//	        Vector<Object> vector = new Vector<Object>();
//	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
//	            vector.add(rs.getObject(columnIndex));
//	        }
//	        data.add(vector);
//	    }
//
//	    return new DefaultTableModel(data, columnNames);
//
//	}
}
