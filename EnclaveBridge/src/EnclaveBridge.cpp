#include "EnclaveBridge.h"
#include "MessageHandler.h"

char* jstringToChar(JNIEnv* env, jstring jstr) {
    char* rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("GB2312");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char*) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}

JNIEXPORT jlong JNICALL Java_EnclaveBridge_createMessageHandlerOBJ(JNIEnv *env, jobject obj)
{
    return (jlong) new MessageHandler();
}

JNIEXPORT jstring JNICALL Java_EnclaveBridge_handleVerification(JNIEnv *env, jobject obj, jlong msgHandlerAddr)
{
    char *resbuf = ((MessageHandler*)msgHandlerAddr)->handleVerification();
    return (*env)->NewStringUTF(env, resbuf);
}

JNIEXPORT jstring JNICALL Java_EnclaveBridge_handleMSG0(JNIEnv *env, jobject obj, jlong msgHandlerAddr, jstring msg0)
{
    char *MSG0 = jstringToChar(env, msg0);
    char *resbuf = ((MessageHandler*)msgHandlerAddr)->handleMSG0(MSG0);
    return (*env)->NewStringUTF(env, resbuf);
}

JNIEXPORT jstring JNICALL Java_EnclaveBridge_handleMSG2(JNIEnv *env, jobject obj, jlong msgHandlerAddr, jstring msg2)
{
    char *MSG2 = jstringToChar(env, msg2);
    char *resbuf = ((MessageHandler*)msgHandlerAddr)->handleMSG2(MSG2);
    return (*env)->NewStringUTF(env, resbuf);
}

JNIEXPORT jstring JNICALL Java_EnclaveBridge_handleAttestationResult(JNIEnv *env, jobject obj, jlong msgHandlerAddr, jstring msg)
{
    char *MSG = jstringToChar(env, msg);
    char *resbuf = ((MessageHandler*)msgHandlerAddr)->handleAttestationResult(msg);
    return (*env)->NewStringUTF(env, resbuf);
}
