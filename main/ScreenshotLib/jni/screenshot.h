/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class pkg_screenshot_Screenshot */

#ifndef _Included_pkg_screenshot_Screenshot
#define _Included_pkg_screenshot_Screenshot
#ifdef __cplusplus
extern "C" {
#endif
#undef pkg_screenshot_Screenshot_SCREENSHOT_OK
#define pkg_screenshot_Screenshot_SCREENSHOT_OK 0L
#undef pkg_screenshot_Screenshot_SCREENSHOT_ERROR_PERMISSION
#define pkg_screenshot_Screenshot_SCREENSHOT_ERROR_PERMISSION 1L
#undef pkg_screenshot_Screenshot_SCREENSHOT_ERROR_OUTOFMEMORY
#define pkg_screenshot_Screenshot_SCREENSHOT_ERROR_OUTOFMEMORY 2L
#undef pkg_screenshot_Screenshot_SCREENSHOT_ERROR_UNKNOWN
#define pkg_screenshot_Screenshot_SCREENSHOT_ERROR_UNKNOWN 3L
#undef pkg_screenshot_Screenshot_SCREENSHOT_ARE_SAME
#define pkg_screenshot_Screenshot_SCREENSHOT_ARE_SAME 4L
#undef pkg_screenshot_Screenshot_CONFIG_RGB_565
#define pkg_screenshot_Screenshot_CONFIG_RGB_565 16L
#undef pkg_screenshot_Screenshot_CONFIG_ARGB_8888
#define pkg_screenshot_Screenshot_CONFIG_ARGB_8888 32L
/*
 * Class:     pkg_screenshot_Screenshot
 * Method:    takeScreenshot
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_pkg_screenshot_Screenshot_takeScreenshot
  (JNIEnv *, jobject);

/*
 * Class:     pkg_screenshot_Screenshot
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_pkg_screenshot_Screenshot_free
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif