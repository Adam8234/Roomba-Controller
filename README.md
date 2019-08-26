# Roomba-Controller
UI used to communicate with a Texas Instruments micro controller to control a "Mars Rover" roomba

Spent about 1.5 weeks designing and coding this project.

# Project

As part of a final project in Embedded Systems at Iowa State University, the goal was to navigate a Roomba "Rover" in a field where we can't see the rover. Our goal was to avoid obsticals, don't fall through the floor, and navigate into a designated section on the field. This UI takes data that is sent from the Texas Instruments micro controller to create a graphical interface for the driver to navigate the field.


![UI](https://github.com/Adam8234/Roomba-Controller/raw/master/UI.PNG)

The obstacles were filtered from the robot to see what type they were. Part of the requirement for our project was to know the width of the PVC poles that were used as obsicals as the smaller ones were used to determine the square area that we had to navigate to. We used different colors to show this.

green for "goal" poles
red as non "goal" poles
black as cliff/crater
white as border of the field obstacles 
