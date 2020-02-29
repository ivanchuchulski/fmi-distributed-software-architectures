#!/bin/bash

_CMD_JAVA=`which java`;
_CMD_REG=`which rmiregistry`;

$_CMD_JAVA -cp ./bin -Djava.rmi.server.codebase=http://rmi.yaht.net/repo/arith.bin/ -Djava.rmi.server.useCodebaseOnly=false ArithServer
