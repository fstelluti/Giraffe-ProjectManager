package model;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

/**
 * @author Matthew Mongrain
 */
public class Gantt {
	
	private Project project;
	private JFreeChart chart;

	public Gantt() {}
	
	public Gantt(Project project) {
		this.project = project;
		createChart(createDataset());
	}
	
	/**
	 * Creates the dataset that will be used by the Gantt chart by
	 * iterating over the Activities within a Project and converting their
	 * start and due dates to data members of the appropriate format
	 */
	public IntervalCategoryDataset createDataset() {
		TaskSeries scheduledTasks = new TaskSeries("Scheduled Tasks");
		ArrayList<Activity> activities = project.getActivities();
		for (Activity activity : activities) {
			System.out.println(activity);

		    if (activity.getStartDate() != null && activity.getDueDate() != null)
			scheduledTasks.add(new Task(
					activity.getName(),
					new SimpleTimePeriod(activity.getStartDate(), activity.getDueDate())
					)
			);
		}
		TaskSeriesCollection dataset = new TaskSeriesCollection();
		dataset.add(scheduledTasks);
		return dataset;
	}
	
	/**
	 * Builds the actual Gantt chart object as a JFreeChart
	 */
	private void createChart(IntervalCategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createGanttChart(
			project.getName(),
			"Activity",
			"Date",
			dataset,
			true,
			true,
			false
			);
		this.chart = chart;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public JFreeChart getChart() {
		return chart;
	}
}