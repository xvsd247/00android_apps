//
// Created by xjx on 2020-12-19.
//
#include "onload.h"
#ifdef __cplusplus
extern "C" {
#endif
#define LOG_TAG "onload"

//static const JNINativeMethod method_table[] = {
//        {"init_native","()Z",(void*)flexcan_init},
//        {"flexcan_native_send","([IIIIIII)I",(void*)flexcan_native_send},
//        {"flexcan_native_dump","(IILcom/android/server/Frame;)I",(void*)flexcan_native_dump},
//}
extern int register_flexcan(JNIEnv *env);

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        LOGE("GetEnv failed!");
        return result;
    }
    register_flexcan(env);

    return JNI_VERSION_1_4;
}
#ifdef __cplusplus
}
#endif