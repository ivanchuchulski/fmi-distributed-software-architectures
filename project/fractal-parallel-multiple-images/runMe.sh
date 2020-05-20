#!/bin/bash

_CMD_JAVA=`which java`;

$_CMD_JAVA -classpath "./bin:./lib/commons-math3-3.5.jar:./lib/commons-cli-1.4.jar" Main -t 12 -o "zad17-colored.png"