#!/bin/bash
usage()
{
cat << EOF
    ./genApp.sh [option] -d <maketype>
        options:
                -h: use hardware mode to build
                -s: use simulation mode to build
                -d: make type which can only be server, app and clean
EOF
}

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
    make SGX_MODE=$runtype SGX_PRERELEASE=1
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
AppDir=$basedir/../Application
SerDir=$basedir/../ServiceProvider

runtype="HW"
maketype=""

while getopts 'hsd:' OPT; do
    case $OPT in
        h)
            runtype="HW";;
        s)
            runtype="SIM";;
        d)
            maketype=$OPTARG;;
        ?)
            usage; exit 1 ;;
    esac
done

usage

if [ x"$runtype" = x"" ]; then
    echo "[INFO] use HW mode to build app. If you don't have sgx hardware, please use '-s' to set simulation mode."
fi

if [ x"$maketype" = x"server" ]; then
    makeServer
elif [ x"$maketype" = x"app" ]; then
    makeApp
elif [ x"$maketype" = x"clean" ]; then
    makeClean
else
    makeServer
    makeApp
fi
