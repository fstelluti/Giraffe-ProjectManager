package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.User;
import controller.DataManager;

public class ActivitiesTab extends JPanel {

    private static final long serialVersionUID = -5620207709069310909L;

    private JTable grid;

    public ActivitiesTab(User user) {
	this.setLayout(new BorderLayout());
	grid = new JTable(DataManager.buildActivityTableModel(user.getCurrentProject()));
	Font dataFont = new Font(null, 0, 12);
	Font headerFont = new Font(null, 0, 12);
	grid.setGridColor(Color.LIGHT_GRAY);
	grid.setRowHeight(25);
	grid.setFont(dataFont);
	grid.getTableHeader().setFont(headerFont);
	this.add(new JScrollPane(grid));
    }
}
