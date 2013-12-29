#!/bin/bash
rm -f *.class
rm -f *.nxj
nxjc MotorTest.java
nxjc SensorTest.java
nxj -o MotorTest.nxj MotorTest
nxj -o SensorTest.nxj SensorTest