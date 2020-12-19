//
// Created by xjx on 2020-12-19.
//
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <assert.h>
#include<android/log.h>
#include <android/native_window_jni.h>
#include "trace.h"
#ifdef __cplusplus
extern "C" {
#endif

#define LOG_TAG "zl_can_CanUtils"
#define NELEM(p) ((int) sizeof(p) / sizeof(p[0]))
jint registerNativeMethods(JNIEnv* env, const char *class_name, JNINativeMethod *methods, int num_methods) {
    int result = 0;

    jclass clazz = env->FindClass(class_name);
    if (LIKELY(clazz)) {
        int result = env->RegisterNatives(clazz, methods, num_methods);
        if (UNLIKELY(result < 0)) {
            LOGE("registerNativeMethods failed(class=%s)", class_name);
        }
    } else {
        LOGE("registerNativeMethods: class'%s' not found", class_name);
    }
    return result;
}
static jboolean flexcan_init(JNIEnv* env,jclass clazz) {
    LOGE("load so and init success");
    return true;
}
static JNINativeMethod methods[] = {
        {"init_native","()Z",(void*)flexcan_init},
};

int register_flexcan(JNIEnv* env) {
    LOGD("register flexcan");
    if (registerNativeMethods(env,
            "com/zl/can/CanUtils",
            methods, NELEM(methods)) < 0) {
        return -1;
    }
    return 0;
}
#ifdef __cplusplus
}
#endif