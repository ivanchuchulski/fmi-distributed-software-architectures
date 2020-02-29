#!/bin/bash

_CMD_JAVA=`which java`;
_CMD_REG=`which rmiregistry`;

$_CMD_REG -J-Djava.rmi.server.codebase=http://rmi.yaht.net/repo/arith.bin/ -J-Djava.rmi.server.useCodebasyOnly=false
