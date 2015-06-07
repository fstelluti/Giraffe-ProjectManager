package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import controller.DataManager;
import controller.DatabaseConstants;
import controller.ProjectDB;
import model.Project;
import model.User;

//NOT FINISHED
public class GridProjects extends JPanel
{
	private JTable grid;
	private User user;

	
	public GridProjects(User user)
	{
		this.user = user;
		grid = new JTable();
	}
}
