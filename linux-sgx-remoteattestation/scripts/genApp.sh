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

verbose()
{
    local type="$1"
    local info="$2"
    local color=$GREEN
    if [ x"$type" = x"ERROR" ]; then
        color=$RED
    fi
    echo -e "${color}`date '+%s'` $type $info${NC}"
}

function makeServer()
{
    verbose INFO "Making service provider..."
    cd $SerDir
    make clean
    make
}

function makeApp()
{
    verbose INFO "Making enclave application..."
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

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

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
        h)
            usage;;
        ?)
            usage; exit 1 ;;
    esac
done

verbose INFO "Type -h to get help info"

if [ x"$runtype" = x"" ]; then
    verbose INFO "Use HW mode to build app. If you don't have sgx hardware, please use '-s' to set simulation mode."
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
