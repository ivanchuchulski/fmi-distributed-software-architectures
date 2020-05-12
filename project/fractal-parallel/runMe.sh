#!/bin/bash

_CMD_JAVA=`which java`;

options=$@;
#echo $options;

$_CMD_JAVA -classpath "./bin:./lib/commons-math3-3.5.jar:./lib/commons-cli-1.4.jar" Main ${options}