#!/bin/bash

_CMD_JAVAC=`which javac`

$_CMD_JAVAC -cp ./bin -d ./bin src/*.java
