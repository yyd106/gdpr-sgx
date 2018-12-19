#include <iostream>
#include <unistd.h>

#include "LogBase.h"
#include "NetworkManager.h"
#include "VerificationManager.h"
#include "UtilityFunctions.h"

using namespace util;

int Main(int argc, char *argv[]) {
    LogBase::Inst();

    int ret = 0;

    int isVerify = 0;
    if(argc != 0) {
        isVerify = atoi(argv[0]);
    }

    VerificationManager *vm = VerificationManager::getInstance();
    vm->init(isVerify);
    vm->start();

    return ret;
}


int main( int argc, char **argv ) {
    try {
        int ret = Main(argc, argv);
        return ret;
    } catch (std::exception & e) {
        Log("exception: %s", e.what());
    } catch (...) {
        Log("unexpected exception");
    }

    return -1;
}





