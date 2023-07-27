//
// Created by siduk on 17.07.2023.
//
#include "VideoPlayer.h"
#include "../utils/Extensions.h"

#define FRAME_COUNT 50

void VideoPlayer::init(std::string path) {
    decoder = std::make_unique<FFmpegDecoder>(path.c_str());

    if (decoder->OpenFile(path)){
        int _width = decoder->GetWidth();
        int _height = decoder->GetHeight();

        for (int i = 0; i < FRAME_COUNT; i++){
            LOGD("BEFORE GETNEXTFRAME()");
            AVFrame* frame = decoder->GetNextFrame();
            LOGD("AFTER GETNEXTFRAME()");
            if (frame){
                char fileName[path.size()];
                sprintf(fileName, GetBMPFormat(path).c_str(), i);
                if (!BMPSave(fileName, frame, frame->width, frame->height)){
                    LOGD("Cannot save file %s\n", fileName);
                }
                av_free(frame->data[0]);
                av_free(frame);
            }
        }
        decoder->CloseFile();
    } else {
        LOGD("Cannot open file %s\n", filePath);
    }
}

bool VideoPlayer::BMPSave(const char *pFileName, AVFrame *frame, int width, int height) {
    bool bResult = false;

    if (frame){
        FILE* file = fopen(pFileName, "wb");

        if (file){
            // RGB Image
            int imageSizeInBytes = 3 * width * height;
            int headersSize = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);
            int fileSize = headersSize + imageSizeInBytes;

            auto* pData = new uint8_t[headersSize];

            if (pData != NULL){
                BITMAPFILEHEADER& bfHeader = *((BITMAPFILEHEADER*) (pData));

                bfHeader.bfType = 'MB';
                bfHeader.bfSize = fileSize;
                bfHeader.bfOffBits = headersSize;
                bfHeader.bfReserved1 = bfHeader.bfReserved2 = 0;

                BITMAPINFOHEADER& bmiHeader = *((BITMAPINFOHEADER*) (pData + headersSize - sizeof(BITMAPINFOHEADER)));

                bmiHeader.biBitCount = 3 * 8;
                bmiHeader.biHeight = height;
                bmiHeader.biWidth = width;
                bmiHeader.biPlanes = 1;
                bmiHeader.biSize = sizeof(bmiHeader);
                bmiHeader.biCompression = AV_PIX_FMT_RGB24;
                bmiHeader.biClrImportant = bmiHeader.biClrUsed =
                        bmiHeader.biSizeImage = bmiHeader.biXPelsPerMeter =
                                bmiHeader.biYPelsPerMeter = 0;
                fwrite(pData, headersSize, 1, file);
                uint8_t *pBits = frame->data[0] + frame->linesize[0] * height - frame->linesize[0];
                int nSpan = -frame->linesize[0];

                int numberOfBytesToWrite = 3 * width;

                for (size_t i = 0; i < height; ++i, pBits += nSpan) {
                    fwrite(pBits, numberOfBytesToWrite, 1, file);
                }

                bResult = true;
                delete[] pData;
            }
            fclose(file);
        }
    }
    return bResult;
}


