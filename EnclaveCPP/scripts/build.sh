#!/bin/bash
usage()
{
cat << EOF
    usage:
          build.sh [option] -l <jnilib_des_path> -j <javafile_des_path>
            options:
                -l: the destination path of the new created jni library
                -j: the destination path of the new java file
                -m: make mode, can only be HW or SIM, default is HW
                -t: for test, copy java and .so file to my test project
EOF
}

verbose()
{
    local type=$1
    local info=$2
    local tips=$3
    local color=$GREEN
    local time=`date "+%Y/%m/%d %T.%3N"`
    if [ x"$type" = x"ERROR" ]; then
        echo -e "${HRED}$time [$type] $info${NC}" >&2 
        return
    fi
    if [ x"$tips" != x"" ]; then color=$HGREEN; fi
    echo -e "${color}$time [$type] $info${NC}"
}

############### MAIN BODY ###############

basedir=`dirname $0`
basedir=`cd $basedir; pwd`
isvSignFile="isv_enclave.signed.so"
libfile="libEnclaveBridge.so"
javafile="EnclaveBridge.java"
headerfile="EnclaveBridge.h"
projectdir=`cd $basedir/../../tools/EnclaveBridge/src/main/;pwd`
jnidir=$projectdir/jnilib
javadir=$projectdir/java
homedir=$basedir/..
targetdir=$homedir/../target
TMPFILE=$basedir/tmp.$$

RED='\033[0;31m'
HRED='\033[1;31m'
GREEN='\033[0;32m'
HGREEN='\033[1;32m'
NC='\033[0m'

is_test="no"
make_mode="HW"

if [ ! -e "$targetdir" ]; then
    mkdir -p $targetdir
fi

CMD=`getopt -o "tl:j:d:m:" -a -l "test,jnilib:,javafile:,targetdir:,mode:" -n "usage" -- "$@"`
if [ $? != 0 ]; then
    verbose ERROR "Parameter error, terminate" >&2
    exit 1
fi

eval set -- "$CMD"

while true; do
    case "$1" in
        -t|--test|-test)
            is_test="yes"
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
        -m|--mode|-mode)
            make_mode=$2
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

##### regenerate header file according to java file {{{
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
cp $headerfile $TMPFILE
line=`tac $TMPFILE | sed '0,/^#include/{s/^#include/#include "MessageHandler.h"\n#include/}' | tac > $headerfile`
unlink $TMPFILE
cd -
##### }}}

##### building .so file {{{
cd $homedir
verbose INFO "Rebuilding jnilib file($make_mode mode)..."
# check if JAVA_HOME is set
if [ x"$JAVA_HOME" = x"" ]; then
    verbose ERROR "JAVA_HOME is not set! Please check and set." h
    exit 1
fi
# check if sgxsdk lib has been imported
sgxsdkdir=/opt/intel/sgxsdk
if [ ! -d "$sgxsdkdir" ]; then
    verbose ERROR "SGX is not installed in $sgxsdkdir!" h
    exit 1
fi
if ! echo "$LD_LIBRARY_PATH" | grep "$sgxsdkdir/sdk_libs" &>/dev/null; then
    export LD_LIBRARY_PATH=$sgxsdkdir/sdk_libs:$LD_LIBRARY_PATH
fi
# make target
make clean
make SGX_MODE=$make_mode SGX_PRERELEASE=1
if [ $? -ne 0 ]; then
    verbose ERROR "Building jni library failed!"
    exit 1
fi
verbose INFO "Copying java and .so file to $targetdir..."
cp $isvSignFile $libfile $javafile $targetdir
cd -
##### }}}

##### for test {{{
if [ x"$is_test" = x"yes" ]; then
    verbose INFO "Copying jnilib file and java file to indicated directory"
    verbose INFO "JNI  des dir is:$jnidir"
    verbose INFO "JAVA des dir is:$javadir"
    cp $libfile $jnidir
    cp $javafile $javadir
fi
##### }}}

verbose INFO "Build jni library Succeed!" h
