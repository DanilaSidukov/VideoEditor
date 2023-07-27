#include <jni.h>
#include "FFmpegDecoder.h"
#include "VideoPlayer.h"


static std::unique_ptr<VideoPlayer> videoPlayer;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_videomanager_VideoManager_nativeLoad(JNIEnv *env, jobject thiz, jstring uri) {

    const char* path = env->GetStringUTFChars(uri, nullptr);

    videoPlayer = std::make_unique<VideoPlayer>(path);

    env->ReleaseStringUTFChars(uri, path);
}
