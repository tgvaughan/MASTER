#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 ver"
    exit
fi

# Assemble archive

ardir=MASTER-$1
mkdir $ardir
mkdir $ardir/examples
cp ../examples/* $ardir/examples
cp ../README.md $ardir/README
cp ../COPYING $ardir/COPYING
cp ../lib/LICENSE* $ardir/
cat master_linux.sh | sed 's/_MASTERJAR_/'MASTER-$1.jar'/' > $ardir/master_linux.sh
chmod a+x $ardir/master_linux.sh
cp ../dist/MASTER.jar $ardir/MASTER-$1.jar
cp -R ../dist/lib $ardir/

zip -r MASTER-$1.zip $ardir/*
