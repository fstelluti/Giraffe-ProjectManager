package view;

/**
 * 
 * @author Zachary Bergeron
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.User;
import controller.DataManager;
import controller.DatabaseConstants;
import controller.ProjectDB;

//NOT FINISHED
@SuppressWarnings("all")
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
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			c = DataManager.getConnection(connectionString);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			rs = stmt
					.executeQuery("SELECT p.name, p.startDate, p.dueDate"
							+ " FROM PROJECTS p, USERS u, USERROLES ur"
							+ " WHERE ur.PROJECTID = p.id AND ur.USERID = u.ID AND ur.USERID = " + user.getId() + ";");
			grid = new JTable(buildTableModel(rs) );
			Font dataFont = new Font(null, 2, 18);
			Font headerFont = new Font(null, 1, 20);
			grid.setGridColor(Color.LIGHT_GRAY);
			grid.setRowHeight(25);
			grid.setFont(dataFont);
			grid.getTableHeader().setFont(headerFont);
			this.add(new JScrollPane(grid));
			rs.close();
			stmt.close();
			c.close();
			
		}
		catch (SQLException e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
	}
	
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();
	    int columnCount = metaData.getColumnCount();
	   
	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
//	    for (int column = 1; column <= columnCount; column++) {
//	        columnNames.add(metaData.getColumnName(column));
//	    }
	    columnNames.add("Name");
	    columnNames.add("Start Date");
	    columnNames.add("Due Date");

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
}
