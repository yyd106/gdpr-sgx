#!/bin/bash
usage()
{
cat << EOF
    build.sh [option] -l <jnilib_des_path> -j <javafile_des_path>
        options:
                -r: indicate rebuilding java file to create new c header file
                -l: the destination path of the new created jni library
                -j: the destination path of the new java file
EOF
}

verbose()
{
    local type=$1
    local info=$2
    if [ x"$type" = x"ERROR" ]; then
        echo -e "${RED}`date "+%s"` $type $info${NC}" >&2 
        return
    fi
    echo -e "${GREEN}`date "+%s"` $type $info${NC}"
}

############### MAIN BODY ###############

basedir=`dirname $0`
basedir=`cd $basedir; pwd`
target="libEnclaveBridge.so"
javafile="EnclaveBridge.java"
headerfile="EnclaveBridge.h"
jnidir=`cd $basedir/../../tools/EnclaveBridge/src/main/jnilib; pwd`
javadir=`cd $basedir/../../tools/EnclaveBridge/src/main/java; pwd`
homedir=$basedir/..

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

rebuild="no"

CMD=`getopt -o "rl:j:" -a -l "rebuild,jnilib:,javafile:" -n "usage" -- "$@"`
if [ $? != 0 ]; then
    verbose ERROR "Parameter error, terminate" >&2
    exit 1
fi


eval set -- "$CMD"

while true; do
    case "$1" in
        -r|--rebuild|-rebuild)
            rebuild="yes"
            shift
            ;;
        -l|--jnilib|-jnilib)
            jnidir=$2
            shift 2
            ;;
        -j|--javafile|-javafile)
            javadir=$2
            shift 2
            ;;
        --)
            shift
            break
            ;;
        *)
            usage
            exit 1
            ;;
    esac
done


cd $homedir

if [ x"$rebuild" = x"yes" ]; then
    if [ ! -d "$jnidir" ]; then
        verbose ERROR "JNI directory($jnidir) doesn't exist"
        exit 1
    fi
    if [ ! -d "$javadir" ]; then
        verbose ERROR "JAVA directory($javadir) doesn't exist"
        exit 1
    fi
    javac $javafile
    javah ${javafile%.*}
    #line=`sed -n '/^#include/h;${x;=}' $headerfile`
    line=`grep -rin "^#include" $headerfile | tail -n 1 | awk -F: '{print $1}'`
    sed -i "$line a#include \"MessageHandler.h\"" $headerfile
fi

verbose INFO "Rebuilding jnilib file..."
verbose INFO "JNI des dir is:$jnidir"
verbose INFO "JAVA des dir is:$javadir"

make clean
make
cp $target $jnidir
cp $javafile $javadir
