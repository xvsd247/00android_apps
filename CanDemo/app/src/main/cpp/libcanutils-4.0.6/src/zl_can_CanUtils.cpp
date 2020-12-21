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
#include "canutils.h"
#ifdef __cplusplus
extern "C" {
#endif

#define LOG_TAG "zl_can_CanUtils"
#define NELEM(p) ((int) sizeof(p) / sizeof(p[0]))
int data[8] = {0};
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

static jboolean flexcan_native_config(JNIEnv* env, jclass clazz,
     jint bitrate, jint loopback, jint restart_ms) {
    if(can_config((int)bitrate, (int)loopback, (int)restart_ms)) {
        LOGE("Config can error!");
        return false;
    }
    return true;
}

static jboolean  flexcan_native_send(JNIEnv* env, jclass clazz,
        jint id, jint dlc, jint extended, jint rtr, jint infinite, jint loopcount, jintArray data) {
    jint buf[8];
    env->GetIntArrayRegion(data, 0 ,8, buf);
    if(can_send((int)id, 8, (int)extended, (int)rtr, (int)infinite, (int)loopcount, buf)) {
        LOGE("Send data error!");
        return false;
    }
    return true;
}

static int callback(int id, int r, int idc) {
    LOGD("data come");
}

static jboolean flexcan_native_readloop(JNIEnv* env, jclass clazz,
        jint family, jint type, jint proto, jint filter_count, jintArray filter_p, jintArray data, jobject callback) {

    jint tdata[8];
    if(can_dump((int)family, (int)type, (int)proto, (int)filter_count, filter_p, tdata, callback)) {
        LOGE("Start read data loop error!");
        return false;
    }
    return true;
}

static JNINativeMethod methods[] = {
        {"init_native","()Z",(void*)flexcan_init},
        {"flexcan_native_config","(III)Z",(void*)flexcan_native_config},
        {"flexcan_native_send","(IIIIII[I)Z",(void*)flexcan_native_send},
        {"flexcan_native_readloop","(IIII[I[ILcom/zl/can/CanCallback)Z",(void*)flexcan_native_readloop},
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