//
// Created by xjx on 2020-12-19.
//
#include "onload.h"
#ifdef __cplusplus
extern "C" {
#endif
#define LOG_TAG "onload"

extern int register_flexcan(JNIEnv *env);

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        LOGE("GetEnv failed!");
        return result;
    }
    register_flexcan(env);

    return JNI_VERSION_1_6;
}
#ifdef __cplusplus
}
#endif