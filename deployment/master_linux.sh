#!/bin/bash

ONEJAR=_MASTERJAR_

OLDDIR=$PWD
MASTERDIR=$(dirname $(readlink -f $0))
if [ -z "$1" ]; then
    java -jar $MASTERDIR/$ONEJAR
else
    java -jar $MASTERDIR/$ONEJAR $1
fi
cd $OLDDIR

