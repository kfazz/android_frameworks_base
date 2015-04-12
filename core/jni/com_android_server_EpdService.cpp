#define LOG_TAG "EpdJNI"

#include "utils/Log.h"

#include "jni.h"
#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"

#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <termios.h>

using namespace android;

/* Globals */
const char* fb = "/dev/graphics/fb0";
static int fd;

#define OMAP3EPFB_IO_SET_REGION     	_IOW('R', 0x80, struct omap3epfb_area)
#define OMAP3EPFB_IO_GET_REGION     	_IOWR('R', 0x81, struct omap3epfb_area)
#define OMAP3EPFB_IO_RESET_REGION     	_IOWR('R', 0x82, int)
#define OMAP3EPFB_IO_FILL_REGION     	_IOW('R', 0x83, struct omap3epfb_area)

struct omap3epfb_update_area {
	/* input parameters, provided by the user-space application */
	unsigned int x0;        /* area's horizontal position */
	unsigned int y0;        /* area's vertical position */
	unsigned int x1;        /* area last horizontal pixel */
	unsigned int y1;        /* area last vertical pixel */
	unsigned int wvfid;     /* waveform ID */
    unsigned int threshold; /* threshold for BW images */
};

struct omap3epfb_area {
	int index;
	int effect_flags;
	struct omap3epfb_update_area effect_area;
};

/*
 * Method:    nativeClassInit
 * Signature: ()V
 */

JNIEXPORT void JNICALL nativeClassInit
  (JNIEnv * je, jclass jc)
  {
     __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"nativeClassInit\n");
    fd = open (fb, O_RDWR);
    if (fd <0) //TODO use errno macro
       __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"nativeClassInit failed to open %s. returned %d\n",fb,fd);
    else
         __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"nativeClassInit got fd %d\n",fd);
    return;
  }

/*
 * Method:    setEpdRegionNative
 * Signature: (IIIIIIII)I
 */
JNIEXPORT jint JNICALL setEpdRegionNative
  (JNIEnv * je, jclass jc, jint region, jint x0, jint y0, jint x1, jint y1, jint wvfid, jint thresh, jint mode)

  {
      __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"setEpdRegionNative:");
            __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"using fd %d\n",fd);
   struct omap3epfb_update_area update_area = {x0,y0,x1,y1,wvfid,thresh};
   struct omap3epfb_area area ={region, mode, update_area};
   return ioctl (fd,OMAP3EPFB_IO_SET_REGION, &area);
  }

/*
 * Method:    getEpdRegionNative
 * Signature: (I)Lcom/android/server/EpdService$NativeParams;
 */
JNIEXPORT jobject JNICALL getEpdRegionNative
  (JNIEnv *jenv, jclass jc, jint area_id)
  {
  //No known users of this function
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"getEpdRegionNative:");
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"using fd %d\n",fd);
    struct omap3epfb_update_area update_area ={0,0,0,0,0,0};
    struct omap3epfb_area a = {area_id,0,update_area};
    ioctl(fd,OMAP3EPFB_IO_GET_REGION,&a);
    jclass cls = jenv->FindClass("com/android/server/EpdService.NativeParams");

    jmethodID methodId = jenv->GetMethodID(cls, "<init>", "(IIIIIII)V");

    jobject obj = jenv->NewObject(cls, methodId, a.effect_area.x0, a.effect_area.y0, \
                                                     a.effect_area.x1, a.effect_area.y1, \
                             a.effect_area.wvfid, a.effect_area.threshold,a.effect_flags);
         return obj;
  }

/*
 * Method:    resetEpdRegionNative
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL resetEpdRegionNative
  (JNIEnv * je, jclass jc, jint region_id)
  {
     __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"resetEpdRegionNative\n");
        return ioctl (fd,OMAP3EPFB_IO_RESET_REGION, &region_id);
  }

/*
 * Method:    fillEpdRegionNative
 * Signature: (IIIIII)I
 */
JNIEXPORT jint JNICALL fillEpdRegionNative
  (JNIEnv *je, jclass jc, jint x0, jint y0, jint x1, jint y1, jint wvfid, jint threshold)
  {
    int ret =0;
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"fillEpdRegionNative:");
                __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,"using fd %d\n",fd);
    struct omap3epfb_update_area update_area = {x0,y0,x1,y1,wvfid,threshold};
    struct omap3epfb_area area ={0,0,update_area};
    ret = ioctl(fd,OMAP3EPFB_IO_FILL_REGION, &area);
    return ret;
  }

static JNINativeMethod method_table[] = {
    {"nativeClassInit"	,	"()V",	(void *)nativeClassInit},
    {"setEpdRegionNative",	"(IIIIIIII)I",	(void *)setEpdRegionNative},
    {"getEpdRegionNative",	"(I)Lcom/android/server/EpdService$NativeParams;",	(void *)getEpdRegionNative},
    {"resetEpdRegionNative",	"(I)I",		(void *)resetEpdRegionNative},
    {"fillEpdRegionNative",      "(IIIIII)I",	(void *)fillEpdRegionNative},
};

int register_com_android_server_EpdService(JNIEnv *env)
{
    return AndroidRuntime::registerNativeMethods(env, "com/android/server/EpdService",
            method_table, NELEM(method_table));
}

