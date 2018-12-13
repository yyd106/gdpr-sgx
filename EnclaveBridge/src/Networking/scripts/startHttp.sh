#!/bin/bash
function start()
{
    cd $builddir
    make -j4
    cd $homedir
    ./build/http_examples &
}

function stop()
{
    killall http_examples
}

function restart()
{
    killall http_examples
    cd $builddir
    make -j4
    cd $homedir
    ./build/http_examples &
}

basedir=`dirname $0`
basedir=`cd $basedir;pwd`
homedir=$basedir/..
builddir=$homedir/build

para="$1"

if [ x"$para" = x"" ]; then
    echo "[ERROR] please give a parameter..." >&2
    exit 1
elif [ x"$para" = x"start" ]; then
    start
elif [ x"$para" = x"stop" ]; then
    stop
elif [ x"$para" = x"restart" ]; then
    echo "[INFO] restarting service..."
    restart
    exit 1
fi
