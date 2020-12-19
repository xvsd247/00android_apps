//
// Created by xjx on 2020-12-19.
//
#ifndef _TRACE_H
#define _TRACE_H

#include <android/log.h>
#include <unistd.h>
#include <libgen.h>
#define		SAFE_FREE(p)				{ if (p) { free((p)); (p) = NULL; } }
#define		SAFE_DELETE(p)				{ if (p) { delete (p); (p) = NULL; } }
#define		SAFE_DELETE_ARRAY(p)		{ if (p) { delete [](p); (p) = NULL; } }
#define		NUM_ARRAY_ELEMENTS(p)		((int) sizeof(p) / sizeof(p[0]))

#if defined(__GNUC__)
// the macro for branch prediction optimaization for gcc(-O2/-O3 required)
#define		CONDITION(cond)				((__builtin_expect((cond)!=0, 0)))
#define		LIKELY(x)					((__builtin_expect(!!(x), 1)))	// x is likely true
#define		UNLIKELY(x)					((__builtin_expect(!!(x), 0)))	// x is likely false
#else
#define		CONDITION(cond)				((cond))
#define		LIKELY(x)					((x))
#define		UNLIKELY(x)					((x))
#endif

#define LOGI(FMT, ...) \
__android_log_print(ANDROID_LOG_INFO, LOG_TAG, "[%s:%d]" FMT, __FUNCTION__, __LINE__, ##__VA_ARGS__)

#define LOGD(FMT, ...) \
__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "[%s:%d]" FMT, __FUNCTION__, __LINE__, ##__VA_ARGS__)

#define LOGW(FMT, ...) \
__android_log_print(ANDROID_LOG_WARN, LOG_TAG, "[%s:%d]" FMT, __FUNCTION__, __LINE__, ##__VA_ARGS__)

#define LOGE(FMT, ...) \
__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "[%s:%d]" FMT, __FUNCTION__, __LINE__, ##__VA_ARGS__)

#endif
