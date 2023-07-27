
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.
project("video_engine")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# Specifying location of ffmpeg shared objects
set(ffmpeg_DIR ffmpeg)
# libavcodec
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavcodec.so)

# libavdevice
add_library(avdevice SHARED IMPORTED)
set_target_properties(avdevice PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavdevice.so)

# libavfilter
add_library(avfilter SHARED IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavfilter.so)

# libavformat
add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavformat.so)

# libavutil
add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libavutil.so)

# libswresample
add_library(wresample SHARED IMPORTED)
set_target_properties(wresample PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libswresample.so)

# libswscale
add_library(wscale SHARED IMPORTED)
set_target_properties(wscale PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/${ffmpeg_DIR}/${ANDROID_ABI}/libswscale.so)

add_library( # Sets the name of the library.
        video_engine

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        # temporary measure, these two should be header files
        video_engine.cpp
        ffmpegDecoder/FFmpegDecoder.cpp
        player/VideoPlayer.cpp
        utils/Extensions.cpp
        )

# Specifying location of ffmpeg headers
include_directories(${ffmpeg_DIR}/${ANDROID_ABI}/include)
# Specify folders included into this module
include_directories(ffmpegDecoder)
include_directories(player)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
target_link_libraries(
        video_engine

        PRIVATE log

        avcodec
        avdevice
        avfilter
        avformat
        avutil
        wresample
        wscale
)