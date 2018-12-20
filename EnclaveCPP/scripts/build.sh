#!/bin/bash
function addHeader()
{
    local header="$1"
}

basedir=`dirname $0`
basedir=`cd $basedir; pwd`
target="libEnclaveBridge.so"
javafile="EnclaveBridge.java"
headerfile="EnclaveBridge.h"
targetdir="/home/vdeadmin/errands/gdpr-sgx/tools/EnclaveBridge/src/main/jnilib"
javadir="/home/vdeadmin/errands/gdpr-sgx/tools/EnclaveBridge/src/main/java"
homedir=$basedir/..

rebuild="$1"

cd $homedir

if [ x"$rebuild" = x"yes" ]; then
    javac $javafile
    javah ${javafile%.*}
    line=`sed -n '/^#include/h;${x;=}' $headerfile`
    echo "line:$line"
    sed -i "$line a#include \"MessageHandler.h\"" $headerfile
fi

make clean
make
cp $target $targetdir 
cp $javafile $javadir 
