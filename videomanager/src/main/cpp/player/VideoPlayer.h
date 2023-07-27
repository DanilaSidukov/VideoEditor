#pragma once

#include <cstdio>
#include <memory>
#include "FFmpegDecoder.h"


class VideoPlayer {

private:
    std::unique_ptr<FFmpegDecoder> decoder;
    const char* filePath;

    void init(std::string path);

    bool BMPSave(const char* pFileName, AVFrame* frame, int width, int height);

public:
    VideoPlayer(const char* path): filePath(std::move(path)){
        init(filePath);
    }

};
