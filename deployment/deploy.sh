#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 ver"
    exit
fi

# Construct one_jar

ojdir=onejar_Master-$1
mkdir $ojdir
mkdir $ojdir/master
mkdir $ojdir/lib

cp ~/code/beast_and_friends/beast2/dist/beast2.jar $ojdir/master
cp ~/code/beast_and_friends/beast2/dist/lib/*.jar $ojdir/lib
cp ~/code/Master/dist/Master.jar $ojdir/lib
cp ~/code/Master/dist/lib/*.jar $ojdir/lib

cp one-jar-boot-0.97.jar $ojdir/
cd $ojdir
jar xf one-jar-boot-0.97.jar
rm -rf one-jar-boot-0.97.jar src/
echo "One-Jar-Main-Class: beast.app.beastapp.BeastMain" >> boot-manifest.mf
jar -cvfm ../Master-$1.jar boot-manifest.mf .
cd ..
rm -rf $ojdir


# Assemble archive

ardir=Master-$1
mkdir $ardir
mkdir $ardir/examples
cp ../examples/*.xml $ardir/examples
cp ../README.md $ardir/README
cp ../COPYING $ardir/COPYING
cp ../lib/LICENSE* $ardir/
cat master_linux.sh | sed 's/_MASTERJAR_/'Master-$1.jar'/' > $ardir/master_linux.sh
chmod a+x $ardir/master_linux.sh
mv Master-$1.jar $ardir/

zip -r Master-$1.zip $ardir/*