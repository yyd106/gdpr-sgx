#!/bin/bash
function makeServer()
{
    echo "[INFO] making service provider..."
    cd $SerDir
    make clean
    make
}

function makeApp()
{
    echo "[INFO] making enclave application..."
    cd $AppDir
    make clean
    make SGX_MODE=HW SGX_PRERELEASE=1
}

function makeClean() 
{
    cd $SerDir
    make clean
    cd $AppDir
    make clean
}


########## MAIN BODY ##########
basedir=`dirname $0`
basedir=`cd $basedir;pwd`

makeType=$1

AppDir=$basedir/../Application
SerDir=$basedir/../ServiceProvider

if [ "$makeType" = "server" ]; then
    makeServer
elif [ "$makeType" = "app" ]; then
    makeApp
elif [ "$makeType" = "clean" ]; then
    makeClean
else
    makeServer
    makeApp
fi

