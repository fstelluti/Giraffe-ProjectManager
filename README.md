# Giraffe Project Management Software
#### Final Intro to Software Engineering project for Comp 354 at Concordia University
##### Completed with a team of 8 people

Giraffe Project Management software is used to manage projects for small to medium scale programs and let teams collaborate and complete activities/projects efficiently and on time. 

Several fully functional features are implemented, such as: the ability to create different users with various permission levels (such as admin, regular user, and project manager), adding/editing projects and activities (including activity dependencies), earned value analysis statistics, Gantt chart creation, PERT analysis and critical path display, and a project optimization button that optimizes all activities by converting them to a directed graph and then recursively traversing that graph to determine both the earliest start and earliest finish (while considering activity dependency relationships).

Also, there are numerous constrains implemented on various actions, such as not being able to add an activity with an end date that is before the start date, cycle detection for PERT/critical path graphs, and checking for division by zero in certain earned value analysis statistics.

Various white box and black box test cases were also written for this program.

I was was primarily responsible for refactoring the code base (sometimes by applying several software patterns) after the first iteration, writing code for various sections/features (about 8,000 lines total), and writing some of the documentation and Junit tests.

Login screen:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343886/ccf79dd0-45d2-11e5-8033-2ed20162d8dc.png)

Main screen:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343887/ccfc0852-45d2-11e5-9ee4-0fbd0787e066.png)

Adding an Activity:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343885/ccf75b22-45d2-11e5-86bf-54a943ee73e9.png)

Adding a project:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343888/ccfd47b2-45d2-11e5-8351-f7c1cb8f4722.png)

Earned Value Analysis:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343882/ccf54472-45d2-11e5-95e3-a3cce80c226d.png)

PERT analysis:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343884/ccf66348-45d2-11e5-8028-1db3184f088d.png)

Gantt Chart:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343881/ccf26810-45d2-11e5-9ea2-de4d2423aae6.png)

Critical path chart display:

![ScreenShot](https://cloud.githubusercontent.com/assets/10926088/9343883/ccf63cb0-45d2-11e5-9921-fd50b8deba3b.png)

External libraries used: JFreeChart + JCommon for Graphs

