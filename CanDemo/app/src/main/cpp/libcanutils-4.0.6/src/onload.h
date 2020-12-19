//
// Created by xjx on 2020-12-19.
//

#ifndef ONLOAD_H
#define ONLOAD_H

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <jni.h>
#include <assert.h>
#include<android/log.h>
#include "trace.h"
#ifdef __cplusplus
extern "C" {
#endif

jint JNI_OnLoad(JavaVM *vm, void *reserved);

#ifdef __cplusplus
}
#endif

#endif //CAN_DEMO_ONLOAD_H
