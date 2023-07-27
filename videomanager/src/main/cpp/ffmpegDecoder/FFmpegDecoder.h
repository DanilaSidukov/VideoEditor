/*
	It is FFmpeg decoder class. Sample for article from unick-soft.ru
*/

#ifndef __FFMPEG_DECODER__
#define __FFMPEG_DECODER__

#include "ffmpeg_headers_wrapper.h"
#include <string>

class FFmpegDecoder
{
    const char* path;
    FILE* inFile;
    // constructor.
public: FFmpegDecoder(const char* filePath) : pImgConvertCtx(NULL), audioBaseTime(0.0), videoBaseTime(0.0),
                          videoFramePerSecond(0.0), isOpen(false), audioStreamIndex(-1), videoStreamIndex(-1),
                          pAudioCodec(NULL), pAudioCodecCtx(NULL), pVideoCodec(NULL), pVideoCodecCtx(NULL), path(filePath),
                          pFormatCtx(NULL) {;}

    // destructor.
    virtual ~FFmpegDecoder()
    {
        CloseFile();
    }

    // Open file
    virtual bool OpenFile(std::string& inputFile);

    // Close file and free resourses.
    virtual bool CloseFile();

    // Return next frame FFmpeg.
    virtual AVFrame * GetNextFrame();

    // open video stream.
    bool OpenVideo();

    // open audio stream.
    bool OpenAudio();

    // close video stream.
    void CloseVideo();

    // close audio stream.
    void CloseAudio();

    // Decode audio from packet.
    int DecodeAudio(int nStreamIndex, const AVPacket *avpkt,
                    uint8_t* pOutBuffer, size_t nOutBufferSize);

    // Decode video buffer.
    bool DecodeVideo(const AVPacket *avpkt, AVFrame * pOutFrame);

    int GetWidth()
    {
        return width;
    }
    int GetHeight()
    {
        return height;
    }

    float GetBase(){
        return audioBaseTime;
    }

private:

    // return rgb image
    AVFrame * GetRGBAFrame(AVFrame *pFrameYuv);

    // FFmpeg file format.
    AVFormatContext* pFormatCtx;

    // FFmpeg codec context.
    AVCodecContext* pVideoCodecCtx;

    // FFmpeg codec for video.
    AVCodec* pVideoCodec;

    // FFmpeg codec context for audio.
    AVCodecContext* pAudioCodecCtx;

    // FFmpeg codec for audio.
    AVCodec* pAudioCodec;

    // Video stream number in file.
    int videoStreamIndex;

    // Audio stream number in file.
    int audioStreamIndex;

    // File is open or not.
    bool isOpen;

    // Video frame per seconds.
    double videoFramePerSecond;

    // FFmpeg timebase for video.
    double videoBaseTime;

    // FFmpeg timebase for audio.
    double audioBaseTime;

    // FFmpeg context convert image.
    struct SwsContext *pImgConvertCtx;

    // Width of image
    int width;

    // Height of image
    int height;
};

#endif