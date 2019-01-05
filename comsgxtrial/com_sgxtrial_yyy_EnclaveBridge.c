#include <jni.h>
#include "com_sgxtrial_yyy_EnclaveBridge.h"


JNIEXPORT jlong JNICALL Java_com_sgxtrial_yyy_EnclaveBridge_createMessageHandlerOBJ
  (JNIEnv* env, jobject obj) {
    printf("create msg\n");
    return 1;
}

JNIEXPORT jbyteArray JNICALL Java_com_sgxtrial_yyy_EnclaveBridge_handleMessages
  (JNIEnv* env, jobject obj, jlong jl, jbyteArray jba){
    printf("handle msg\n");
    return jba;
}


