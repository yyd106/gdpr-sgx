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
    local time=`date "+%Y/%m/%d %T.%3N"`
    if [ x"$type" = x"ERROR" ]; then
        echo -e "${RED}$time [$type] $info${NC}" >&2 
        return
    fi
    echo -e "${GREEN}$time [$type] $info${NC}"
}

############### MAIN BODY ###############

basedir=`dirname $0`
basedir=`cd $basedir; pwd`
libfile="libEnclaveBridge.so"
javafile="EnclaveBridge.java"
headerfile="EnclaveBridge.h"
projectdir=`cd $basedir/../../tools/EnclaveBridge/src/main/;pwd`
jnidir=$projectdir/jnilib
javadir=$projectdir/java
homedir=$basedir/..

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

rebuild="no"

CMD=`getopt -o "rl:j:d:" -a -l "rebuild,jnilib:,javafile:,targetdir:" -n "usage" -- "$@"`
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
        -d|--targetdir|-targetdir)
            targetdir=$2
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

if [ ! -d "$targetdir" ]; then
    verbose ERROR "$targetdir doesn't exist or is not a directory"
    exit 1
fi

if [ x"$rebuild" = x"yes" ]; then
    if [ ! -d "$jnidir" ]; then
        verbose ERROR "JNI  directory($jnidir) doesn't exist"
        exit 1
    fi
    if [ ! -d "$javadir" ]; then
        verbose ERROR "JAVA directory($javadir) doesn't exist"
        exit 1
    fi
    cd $homedir
    javac $javafile
    javah ${javafile%.*}
    #line=`sed -n '/^#include/h;${x;=}' $headerfile`
    line=`grep -rin "^#include" $headerfile | tail -n 1 | awk -F: '{print $1}'`
    sed -i "$line a#include \"MessageHandler.h\"" $headerfile
fi

verbose INFO "Rebuilding jnilib file..."

make clean
make
if [ $? -ne 0 ]; then
    verbose ERROR "Building jni library failed!"
    exit 1
fi
cp $libfile $javafile $targetdir

#verbose INFO "Please copy $libfile and $javafile to your webserver project"
#verbose INFO "Copying jnilib file and java file to indicated directory..."
#verbose INFO "JNI  des dir is:$jnidir"
#verbose INFO "JAVA des dir is:$javadir"
#cp $libfile $jnidir
#cp $javafile $javadir
