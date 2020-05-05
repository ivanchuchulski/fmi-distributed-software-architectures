#!/bin/bash

_CMD_JAVAC=`which javac`

$_CMD_JAVAC -classpath "./lib/commons-math3-3.5.jar" -d ./bin ./src/Main.java