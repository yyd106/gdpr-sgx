#!/bin/bash
usage()
{
cat << EOF
    ./install_deps.sh [option]
        options:
                -s: install sdk dependencies
                -r: install remote attestation dependencies
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

function apply_pkg_result()
{
    local ret=$1
    local pkg=$2
    if [ $ret -ne 0 ]; then
        verbose ERROR "Install $pkd failed!"
        exit 1
    fi
}

function install_attest_deps()
{
    verbose INFO "Installing remote attestation dependencies..."
    sudo dpkg -i $lib9v5pkg
    apply_pkg_result $? "$lib9v5pkg"
    sudo dpkg -i $liblite9v5pkg
    apply_pkg_result $? "$liblite9v5pkg"
    sudo dpkg -i $libdevpkg
    apply_pkg_result $? "$libdevpkg"
    sudo dpkg -i $libc9v5pkg
    apply_pkg_result $? "$libc9v5pkg"
    sudo dpkg -i $compilerpkg
    apply_pkg_result $? "$compilerpkg"
    sudo apt-get install libboost-thread-dev libboost-system-dev curl libcurl4-openssl-dev libssl-dev liblog4cpp5-dev libjsoncpp-dev
    apply_pkg_result $? "common tools"
}

function install_sdk_deps()
{
    verbose INFO "Installing SGX SDK dependencies..."
}

############### MAIN BODY ###############

basedir=`dirname $0`
basedir=`cd $basedir; pwd`
sourcedir=$basedir/../sources
sourcedir=`cd $sourcedir;pwd`

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

lib9v5pkg=$sourcedir/`ls $sourcedir | grep "f9v5" | grep "2.6.1"`
liblite9v5pkg=$sourcedir/`ls $sourcedir | grep "\-lite9v5" | grep "2.6.1"`
libdevpkg=$sourcedir/`ls $sourcedir | grep "\-dev" | grep "2.6.1"`
libc9v5pkg=$sourcedir/`ls $sourcedir | grep "libprotoc9v5" | grep "2.6.1"`
compilerpkg=$sourcedir/`ls $sourcedir | grep "compiler" | grep "2.6.1"`

installed="no"

while getopts "rs" OPT; do
    case $OPT in
        r)
            install_attest_deps
            installed="yes"
            ;;
        s)
            install_sdk_deps
            installed="yes"
            ;;
        ?)
            usage
            ;;
    esac
done

if [ x"$installed" = x"no" ]; then usage ; fi
