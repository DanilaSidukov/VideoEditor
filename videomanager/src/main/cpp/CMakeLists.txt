
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.22.1)

# Declares and names the project.
project("videomanager")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# libavcodec
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/libavcodec.so)

# libavdevice
add_library(avdevice SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/libavdevice.so)

# libavfilter
add_library(avfilter SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/libavfilter.so)

# libavformat
add_library(avformat SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/libavformat.so)

# libavutil
add_library(avutil SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/libavutil.so)

# libswresample
add_library(wresample SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/libswresample.so)

# libswscale
add_library(wscale SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/ffmpeg/${ANDROID_ABI}/libswscale.so)

add_library( # Sets the name of the library.
        video_manager

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        # temporary measure, these two should be header files
        video_manager.cpp
        ffmpegDecoder/FFmpegDecode.cpp
)

# Specifying location of ffmpeg headers
include_directories(ffmpeg/${ANDROID_ABI}/include)
# Specify folders included into this module
include_directories(ffmpegDecoder)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
target_link_directories(
        video_manager

        PRIVATE log

        avformat
        avcodec
        avio
        avdevice
        avfilter
        avutil
        imgutils
        packet
        log
        wresample
        wscale
)