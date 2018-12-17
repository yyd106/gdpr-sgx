#include "com_sgxtrial_yyy_Bridge.h"
#include <stdlib.h>
#include <limits.h>
#include <stdio.h>
#include <time.h>
#include <jni.h>


JNIEXPORT void JNICALL Java_com_sgxtrial_yyy_Bridge_sgxCreateEnclave
  (JNIEnv *env, jobject obj, jstring str, jboolean javaBoolean)
{
    const char *nativeString = env->GetStringUTFChars(str, 0);
    fprintf(stderr, "call sgx create <%s> successfully!\n", nativeString);
    return;
}

JNIEXPORT void JNICALL Java_com_sgxtrial_yyy_Bridge_doAttestation
  (JNIEnv *, jobject, jstring, jobject)
{
    fprintf(stderr, "calling sgx attestation successfully!\n");
    return;
}

