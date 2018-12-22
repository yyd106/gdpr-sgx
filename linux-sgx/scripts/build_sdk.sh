#!/bin/bash
verbose()
{
    local type="$1"
    local info="$2" local time=`date "+%Y/%m/%d %T.%3N"`
    if [ x"$type" = x"ERROR" ]; then
        echo -e "${RED}$time [$type] $info${NC}" >&2 
        return
    fi
    echo -e "${GREEN}$time [$type] $info${NC}"
}

function auto_install()
{
    verbose INFO "Installing auto tcl tools..."
    sudo apt-get install tcl tk expect
    if [ $? -ne 0 ]; then
        verbose INFO "Install tcl tools failed! Input manually."
        return 1
    fi

cat << EOF > $autoscript
#!/usr/bin/expect
set binfile [lrange \$argv 0 0]

spawn \$binfile
expect "*\[yes/no\]*"
send "no\r"
expect "*directory*"
send "/opt/intel\r"
interact
EOF

    chmod +x $autoscript
    sudo $autoscript "$binfile" > $TMPFILE
    if [ $? -ne 0 ]; then
        verbose INFO "Auto input failed! Input manually."
        return 1
    fi
    cat $TMPFILE

    envline=`cat $TMPFILE | tail -n 1`
    envline=`echo "$envline" | grep -Po "(?<=[a-z]).*(?=t)"`t
    if ! grep "\b$envline\b" ~/.bashrc &>/dev/null; then
        echo "$envline" >> ~/.bashrc
        verbose INFO "\t<<< Attetion >>>\n\t\t\t\tIf tips 'libsgx_xx: No such file or directory'\n\
            \t\t\tPlease run 'source ~/.bashrc' to refresh environment\n\t\t\t\t<<< Attetion >>>"
    fi

}

############### MAIN BODY ###############
basedir=`dirname $0` basedir=`cd $basedir;pwd` 
homedir=`cd $basedir/..;pwd`
sdkdir=$homedir/sdk
bindir=$homedir/linux/installer/bin

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

binfile=$bindir/sgx_linux_x64_sdk_2.3.101.46683.bin
autoscript=$bindir/autoscript_tmp.sh
TMPFILE=$bindir/tmp.$$

cd $sdkdir
make clean
make
cd -

cd $homedir
make sdk_install_pkg
cd -

auto_install
if [ $? -ne 0 ]; then
    sudo $binfile
    if [ $? -ne 0 ]; then
        verbose ERROR "Install SGX SDK failed!"
        exit 1
    fi
fi

rm -f $autoscript $TMPFILE
