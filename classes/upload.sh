#!/bin/bash
rm -f *.class
rm -f *.nxj
nxjc RobotTest.java
nxj -o RobotTest.nxj RobotTest