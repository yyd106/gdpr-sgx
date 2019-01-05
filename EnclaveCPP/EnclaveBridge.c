#include "EnclaveBridge.h"

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

jstring charTojstring(JNIEnv* env, const char* pat) {
    //定义java String类 strClass
    jclass strClass = (env)->FindClass("Ljava/lang/String;");
    //获取String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
    jmethodID ctorID = (env)->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    //建立byte数组
    jbyteArray bytes = (env)->NewByteArray(strlen(pat));
    //将char* 转换为byte数组
    (env)->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*) pat);
    // 设置String, 保存语言类型,用于byte数组转换至String时的参数
    jstring encoding = (env)->NewStringUTF("GB2312");
    //将byte数组转换为java String,并输出
    return (jstring) (env)->NewObject(strClass, ctorID, bytes, encoding);
}

string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

jbyteArray string2jbyteArray(JNIEnv *env, string str) {
    jbyteArray arr = env->NewByteArray(str.length());
    env->SetByteArrayRegion(arr, 0, str.length(), (jbyte*)str.c_str());
    return arr;
}


//=============== MAIN FUNCTION ===============//

JNIEXPORT jlong JNICALL Java_EnclaveBridge_createMessageHandlerOBJ(JNIEnv *env, jobject obj)
{
    return (jlong) new MessageHandler();
}

JNIEXPORT jbyteArray JNICALL Java_EnclaveBridge_handleMessages(JNIEnv *env, jobject obj, jlong msgHandlerAddr, jbyteArray msg)
{
    /*
    printf("call sgx\n");
    jclass cls = env->FindClass("java/lang/String");
    jobjectArray result = (jobjectArray)env->NewObjectArray(2, cls, NULL);

    int len = env->GetArrayLength(data);
    if(len != 2) {
        printf("[ERROR] Input array length can only be two!");
        return result;
    }
    jstring jType = (jstring) (env->GetObjectArrayElement(data, 0));
    jstring jMsg = (jstring) (env->GetObjectArrayElement(data, 1));
    string TYPE = jstring2string(env, jType);
    printf("type is:%s\n",TYPE.c_str());
    int rType = atoi(TYPE.c_str());
    string MSG = jstring2string(env, jMsg);

    string *res = ((MessageHandler*)msgHandlerAddr)->handleMessages(MSG,rType);
    env->SetObjectArrayElement(result, 0, charTojstring(env, res[0].c_str()));
    env->SetObjectArrayElement(result, 1, charTojstring(env, res[1].c_str()));
    return result;
    */
    //char p_msg[env->GetArrayLength(msg)]
    printf("this is a test");
    const char *p_msg = (const char*)env->GetByteArrayElements(msg,0);
    const char parry[8] = {0x08,0x05,0x1a,0x04,0x08,0x01,0x10,0x02};
    //string MSG(p_msg);
    string MSG(parry);
    //string res = ((MessageHandler*)msgHandlerAddr)->handleMessages(MSG);
    string res = "yaoz";
    return string2jbyteArray(env, res);

    /*
    jclass cls = env->FindClass("java/lang/String");
    jobjectArray result = (jobjectArray)env->NewObjectArray(2, cls, NULL);
    env->SetObjectArrayElement(result, 0, charTojstring(env, res[0].c_str()));
    env->SetObjectArrayElement(result, 1, charTojstring(env, res[1].c_str()));
    return result;
    */
}
