#!/bin/bash

basedir=$(cd `dirname $0`;pwd)
libfile="/home/vdeadmin/errands/sgx/target/libEnclaveBridge.so"
signedfile="/home/vdeadmin/errands/sgx/target/isv_enclave.signed.so"

cp $libfile $basedir/shared/lib
cp $signedfile $basedir
$basedir/bin/shutdown.sh
$basedir/bin/startup.sh
tail -f $basedir/logs/catalina.out
