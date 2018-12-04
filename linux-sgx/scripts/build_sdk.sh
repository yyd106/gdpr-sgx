#!/bin/bash

basedir=`dirname $0`
basedir=`cd $basedir;pwd`

homeDir=`cd ../$basedir;pwd`
sdkDir=$homeDir/sdk
binDir=$homeDir/linux/installer/bin
binFile=sgx_linux_x64_sdk_2.3.101.46683.bin

cd $sdkDir
make clean
make

cd $homeDir
make sdk_install_pkg

cd $binDir
sudo ./$binFile
