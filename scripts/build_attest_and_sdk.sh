#!/bin/bash
usage()
{
cat << EOF
    ./build_attest_and_sdk.sh [option]
        options:
                -s: build sdk
                -r: build remote attestation
                -m: remote attestation build mode, could just be HW/SIM
EOF
}

verbose()
{
    local type="$1"
    local info="$2"
    local time=`date "+%Y/%m/%d %T.%3N"`
    if [ x"$type" = x"ERROR" ]; then
        echo -e "${RED}$time [$type] $info${NC}" >&2 
        return
    fi
    echo -e "${GREEN}$time [$type] $info${NC}"
}

function checkPrerequisite()
{
    if ! echo "$LD_LIBRARY_PATH" | grep "libsample_libcrypto.so"; then
        verbose INFO "Add sample libcrypto diretory"
        export LD_LIBRARY_PATH=$libcryptodir:$LD_LIBRARY_PATH
    fi
}

function buildSDK()
{
    cd $sdkdir
    verbose INFO "Building SGX SDK..."
    ./scripts/build_sdk.sh
}

function buildRemoteAttestation()
{
    checkPrerequisite
    cd $attestdir
    if [ x"$attestMode" = x"HW" ]; then
        verbose INFO "Build attestation app with hardware mode..."
        ./scripts/genApp.sh -h
    elif [ x"$attestMode" = x"SIM" ]; then
        verbose INFO "Build attestation app with software mode..."
        ./scripts/genApp.sh -s
    else
        verbose INFO "Please indicate correct type for building remote attestation code(HW/SIM)!" >&2
    fi
}

############### MAIN BODY ###############

basedir=`dirname $0`
basedir=`cd $basedir; pwd`
sdkdir=$basedir/../linux-sgx
attestdir=$basedir/../linux-sgx-remoteattestation
libcryptodir=$attestdir/ServiceProvider/sample_libcrypto
attestMode="HW"
doBuildSDK=no
doBuildAttest=no

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'


while getopts "srhm:" OPT; do
    case $OPT in
        s)
            doBuildSDK="yes";;
        r)
            doBuildAttest="yes";;
        m)
            attestMode=$OPTARG;;
        h)
            usage;;
        ?)
            usage; exit 1;;
    esac
done

if [ x"$doBuildSDK" = x"no" -a x"$doBuildAttest" = x"no" ]; then
    usage
    exit 1
fi

if [ x"$doBuildSDK" = x"yes" ]; then
    buildSDK
fi

if [ x"$doBuildAttest" = x"yes" ]; then
    buildRemoteAttestation
fi

