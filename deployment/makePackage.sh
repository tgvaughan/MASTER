#!/bin/bash
#
# Assemble BEAST 2 package
#

if [ $# -lt 1 ]; then
    echo "Usage: $0 version"
    exit
fi

PKGNAME=MASTER
VERSION=$1

FULLNAME=$PKGNAME.addon.v$VERSION
rm -rf $FULLNAME $FULLNAME.zip
mkdir $FULLNAME
mkdir $FULLNAME/examples
mkdir $FULLNAME/lib
mkdir $FULLNAME/doc

# Create source jar file
pushd ..
jar cf $FULLNAME.src.jar src #test
popd
mv ../$FULLNAME.src.jar $FULLNAME

# Copy over examples
cp ../examples/*.xml $FULLNAME/examples

# Copy over binaries
cp ../dist/$PKGNAME.jar $FULLNAME/lib
cp ../lib/*.jar $FULLNAME/lib

# Copy over documentation
cp ../README.md $FULLNAME/README
cp ../COPYING $FULLNAME/
cp ../lib/LICENSE* $FULLNAME/

# Create version.xml
cat <<EOF > $FULLNAME/version.xml
<addon name="$PKGNAME" version="$VERSION">
    <depends on="beast2" atleast="2.1.0"/>
</addon>
EOF

# Create archive and clean up
cd $FULLNAME
zip -r ../$FULLNAME.zip .
cd ..
rm -rf $FULLNAME
