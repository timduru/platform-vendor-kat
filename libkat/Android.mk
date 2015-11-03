LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SHARED_LIBRARIES := liblog libcutils libgui libbinder libutils
LOCAL_SRC_FILES := libkatcam.c
LOCAL_MODULE := libkatcam
LOCAL_MODULE_TAGS := optional
include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

LOCAL_SHARED_LIBRARIES := liblog libcutils libgui
LOCAL_SRC_FILES := libkatomx.c
LOCAL_MODULE := libkatomx
LOCAL_MODULE_TAGS := optional
include $(BUILD_SHARED_LIBRARY)
