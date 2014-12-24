Checkers Robot
============

About
-----

This is the source code (and assisting files) of the Wilmington Christian School Class of 2014's senior computer science project. The project is a robot built from Legos and the Lego Mindstorms NXT robotics kit. 

At the time of completion, the robot was able to play a game of checkers against a person (or itself). The only interaction with the robot required by the person was to press a button on the robot to signal completion of the player's turn. The robot is fully capable of efficiently scanning the board via light sensor to determine the player's move, calculating the best possible move based upon [minimax game theory](http://en.wikipedia.org/wiki/Minimax) calculating 8-10 moves ahead, moving it's own pieces, and removing captured pieces from the board. At the time of writing, the robot has not been defeated by a human opponent.

Video coming soon.

Developers:
* James Dingwall
* Michael Grey
* Corbin McNeill
* Aaron Miller

How To Install
--------------
While any robotics functionality would be extrememly difficult to replicate, playing against the AI in text mode is quite simple. Instructions have been included to compile and run the checkers program in your local environment: 

1. Download the entire git project to your chosen directory.
2. Install Apache Ant.
  - [Window's Instructions](http://dita-ot.sourceforge.net/doc/ot-userguide13/xhtml/installing/windows_installingant.html)
  - [OSX Instructions](http://stackoverflow.com/questions/3222804/how-can-i-install-apache-ant-on-mac-os-x)
  - Linux Instructions: Install with package manager. The package you are looking for is likely called `ant`.
3. Navigate to the local project home directory.
4. Build the executable jar with the command: `ant jar`.
5. Navigate to `<project_home>/build/jar`
6. Execute the compiled jar with the command: `java -jar CheckersBot.jar`.
7. Enjoy loosing at checkers! 

Instructions for Gameplay
-------------------------
Confused on how to play? Just type `help` or `instructions` when prompted to enter a move.

FAQ
---
No one has asked us any questions yet. **Be the first!** Shoot either Corbin McNeill or Aaron Miller an email at their GitHub listed emails.
