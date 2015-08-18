# Giraffe Project Management Software
#### Final intro to software engineering project for Comp 354 at Concordia University
##### Completed with a team of 8 people

Giraffe Project Management software is used to manage projects for small to medium scale programs and let teams collaborate and complete activities/projects efficiently and on time. 

Several fully functional features are implemented, such as: the ability to create different users with various permission levels (such as admin, regular user, and project manager), adding/editing projects and activities (including activity dependencies), earned value analysis statistics, Gantt chart creation, PERT analysis and critical path display, and a project optimization button that optimizes all activities by converting them to a directed graph and then recursively traversing that graph to determine both the earliest start and earliest finish (while considering activity dependency relationships).

Also, there are numerous constrains implemented on various actions, such as not being able to add an activity with an end date that is before the start date, cycle detection for PERT/critical path graphs, and checking for division by zero in certain earned value analysis statistics.

Various white box and black box test cases were also written for this program.

I was was primarily responsible for refactoring the code base (sometimes by applying several software patterns) after the first iteration, writing code for various sections/features (about 8,000 lines total), and writing some of the documentation and Junit tests.

Login screen:

Adding an Activity:

Adding a project:

Editing a project:

Earned Value Analysis:

PERT analysis:

Gantt Chart:

Critical path chart display:

External libraries used: JFreeChart + JCommon for Graphs

