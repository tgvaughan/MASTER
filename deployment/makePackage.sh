#!/bin/bash
#
# Assemble BEAST 2 package
#

PKGNAME=MASTER

rm -rf $PKGNAME $PKGNAME.zip
mkdir $PKGNAME
mkdir $PKGNAME/examples
mkdir $PKGNAME/lib
mkdir $PKGNAME/doc

# Create source jar file
pushd ..
jar cf $PKGNAME.src.jar src test
popd
mv ../$PKGNAME.src.jar $PKGNAME

# Copy over examples
cp ../examples/*.xml $PKGNAME/examples

# Copy over binaries
cp ../dist/$PKGNAME.jar $PKGNAME/lib
cp ../lib/*.jar $PKGNAME/lib

# Copy over documentation

# Create version.xml
cat <<EOF > $PKGNAME/version.xml
<addon name="$PKGNAME" version="1.4.2">
    <depends on="beast2" atleast="2.0.2"/>
</addon>

EOF

# Create archive and clean up
zip -r $PKGNAME.zip $PKGNAME
rm -rf $PKGNAME
