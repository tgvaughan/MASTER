#!/bin/bash

ONEJAR=_MASTERJAR_

SCRIPTPATH=$(readlink $0)

if [ -z "$SCRIPTPATH" ]; then
    MASTERDIR=$PWD
else
    MASTERDIR=$(dirname "$SCRIPTPATH")
fi

java -jar "$MASTERDIR"/$ONEJAR $@


