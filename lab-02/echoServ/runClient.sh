#!/bin/bash

_CMD_JAVA=`which java`;
## _CMD_REG=`which rmiregistry`;

_N=127.0.0.1

if [[ -n $1 ]] ; then
	_N=$1 ;
fi

$_CMD_JAVA -cp ./bin EchoClient $_N


