package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.DataManager;
import model.Project;
import model.User;

public class DetailsTab extends JPanel {
	
	private static final long serialVersionUID = 1L;
	

    public DetailsTab(Project project, User user) {
		this.setLayout(new BorderLayout());
		Font dataFont = new Font(null, 0, 12);
		Font headerFont = new Font(null, 0, 12);
    }
    
    ImageIcon icon = new ImageIcon(MainPanel.class.getResource("images/welcomegiraffe.png"));
	JLabel label = new JLabel(icon);
	

}
