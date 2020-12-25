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
#include <linux/can.h>
#include "trace.h"
#include "canutils.h"
#ifdef __cplusplus
extern "C" {
#endif

#define LOG_TAG "zl_can_CanUtils"
#define NELEM(p) ((int) sizeof(p) / sizeof(p[0]))
int data[8] = {0};
int s;
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
    LOGD("load so and init success");
    return true;
}

static jboolean flexcan_native_config(JNIEnv* env, jclass clazz,
     jint bitrate, jint loopback, jint restart_ms) {
    LOGE("Start Config can!");
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

//static void ThrowException(JNIEnv *env, const char *className, const char *message) {
//    jclass objClass = env->FindClass(className);
//    if (objClass != NULL) {
//    //注意，这种方式抛出的异常一般不会导致程序崩溃，因为该异常和jvm无关联，但是如果调用的java方法抛出的异常，有可能导致程序崩溃
//        env->ThrowNew(objClass, message);
//        if (0 != env->ExceptionOccurred())//检测是否有异常发生
//        {
//            env->ExceptionClear();//清除异常堆栈
//        }
//        env->DeleteLocalRef(objClass);
//    }
//}

static jboolean flexcan_native_readloop(JNIEnv* env, jclass clazz,
        jint id, jint mask, jobject jframe) {
    s = can_dump_open((int)id, (int)mask);
    int i;
    struct can_frame frame;
    jclass frame_cls = env->FindClass("com/zl/can/Frame");
    if(frame_cls == NULL) {
        LOGE("find class FlexcanService error!");
        return -1;
    }
    //get frame method
    jmethodID setID = env->GetMethodID(frame_cls,"setID","(I)V");
    if( setID == NULL) {
        LOGE("get setID error!");
        return -1;
    }
    jmethodID setBuf = env->GetMethodID(frame_cls,"setBuf","([I)V");
    if(setBuf == NULL){
        LOGE("get setBuf error!");
        return -1;
    }
    jmethodID setDlc= env->GetMethodID(frame_cls,"setDlc","(I)V");
    if(setDlc == NULL){
        LOGE("get setDlc error!");
        return -1;
    }
    jmethodID setRemote= env->GetMethodID(frame_cls,"setRemote","(I)V");
    if(setRemote == NULL){
        LOGE("get setRemote error!");
        return -1;
    }

    jmethodID dataReadyNotifly= env->GetMethodID(frame_cls,"dataReadyNotifly","()V");
    if(dataReadyNotifly == NULL){
        LOGE("get setRemote error!");
        return -1;
    }

    if(jframe == NULL) {
        LOGE("frame NULL error!");
        return -1;
    }

    if(s > 0) {
        while(can_dump_start(s, &frame) > 0) {
            if (frame.can_id & CAN_EFF_FLAG)
                env->CallVoidMethod(jframe, setID, frame.can_id & CAN_EFF_MASK);
            else
                env->CallVoidMethod(jframe, setID, frame.can_id & CAN_SFF_MASK);
            jintArray arr;
            arr = env->NewIntArray(8);
            if(arr == NULL) {
                LOGE("arr init error!");
                return -1;
            }
            jint data[8];
            for(i=0; i < frame.can_dlc; i++) {
                data[i] = (jint)frame.data[i];
            }
            env->SetIntArrayRegion(arr, 0, 8, data);
            env->CallVoidMethod(jframe, setBuf, arr);
            env->DeleteLocalRef(arr);

            if (frame.can_id & CAN_RTR_FLAG) {
                env->CallVoidMethod(jframe, setRemote, 1);
            } else {
                env->CallVoidMethod(jframe, setRemote, 0);
            }

            env->CallVoidMethod(jframe, dataReadyNotifly);
        }
        //ThrowException(env, "java/lang/InterruptedException", "error occured when read data");
        LOGE("a error occured when read data, please reopen CAN");
        return false;
    }
    return false;
}

static jboolean flexcan_native_stopread(JNIEnv* env, jclass clazz) {
    return can_dump_stop(s);
}

static JNINativeMethod methods[] = {
        {"init_native","()Z",(void*)flexcan_init},
        {"flexcan_native_config","(III)Z",(void*)flexcan_native_config},
        {"flexcan_native_send","(IIIIII[I)Z",(void*)flexcan_native_send},
        {"flexcan_native_readloop","(IILcom/zl/can/Frame;)Z",(void*)flexcan_native_readloop},
        {"flexcan_native_stopread","()Z",(void*)flexcan_native_stopread},
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