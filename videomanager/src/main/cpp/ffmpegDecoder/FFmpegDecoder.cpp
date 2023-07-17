#include "FFmpegDecoder.h"


#define min(a, b) (a > b ? b : a)

#define MAX_AUDIO_PACKET (2 * 1024 * 1024)

bool FFmpegDecoder::OpenFile(std::string &inputFile) {
    CloseFile();

    // Register all components DEPRECATED, it's not necessary

    // Open media file.
    if (avformat_open_input(&pFormatCtx, inputFile.c_str(), nullptr, nullptr) != 0) {
        CloseFile();
        return false;
    }

    // Get format info.
    if (avformat_find_stream_info(pFormatCtx, nullptr) < 0) {
        CloseFile();
        return false;
    }

    // open video and audio stream.
    bool hasVideo = OpenVideo();
    bool hasAudio = OpenAudio();

    if (!hasVideo) {
        CloseFile();
        return false;
    }

    isOpen = true;

    // Get file information.
    if (videoStreamIndex != -1) {
        videoFramePerSecond = av_q2d(pFormatCtx->streams[videoStreamIndex]->r_frame_rate);
        // Need for convert time to ffmpeg time.
        videoBaseTime = av_q2d(pFormatCtx->streams[videoStreamIndex]->time_base);
    }

    if (audioStreamIndex != -1) {
        audioBaseTime = av_q2d(pFormatCtx->streams[audioStreamIndex]->time_base);
    }

    return true;
}


bool FFmpegDecoder::CloseFile() {
    isOpen = false;

    // Close video and audio.
    CloseVideo();
    CloseAudio();

    if (pFormatCtx) {
        avformat_close_input(&pFormatCtx);
        pFormatCtx = nullptr;
    }

    return true;
}


AVFrame *FFmpegDecoder::GetNextFrame() {
    AVFrame *res = nullptr;

    if (videoStreamIndex != -1) {
        AVFrame *pVideoYuv = av_frame_alloc();
        AVPacket packet;

        if (isOpen) {
            // Read packet.
            while (av_read_frame(pFormatCtx, &packet) >= 0) {
                int64_t pts = 0;
                pts = (packet.dts != AV_NOPTS_VALUE) ? packet.dts : 0;

                if (packet.stream_index == videoStreamIndex) {
                    // Convert ffmpeg frame timestamp to real frame number.
                    int64_t numberFrame = (double) ((int64_t) pts -
                                                    pFormatCtx->streams[videoStreamIndex]->start_time) *
                                          videoBaseTime * videoFramePerSecond;

                    // Decode frame
                    bool isDecodeComplete = DecodeVideo(&packet, pVideoYuv);
                    if (isDecodeComplete) {
                        res = GetRGBAFrame(pVideoYuv);
                    }
                    break;
                } else if (packet.stream_index == audioStreamIndex) {
                    if (packet.dts != AV_NOPTS_VALUE) {
                        int audioFrameSize = MAX_AUDIO_PACKET;
                        auto *pFrameAudio = new uint8_t[audioFrameSize];
                        if (pFrameAudio) {
                            double fCurrentTime = (double) (pts -
                                                            pFormatCtx->streams[videoStreamIndex]->start_time)
                                                  * audioBaseTime;
                            double fCurrentDuration = (double) packet.duration * audioBaseTime;

                            // Decode audio
                            int nDecodedSize = DecodeAudio(audioStreamIndex, &packet,
                                                           pFrameAudio, audioFrameSize);

                            if (nDecodedSize > 0 &&
                                pAudioCodecCtx->sample_fmt == AV_SAMPLE_FMT_FLTP) {
                                // Process audio here.
                                /* Uncommend sample if you want write raw data to file.
                                {
                                    int size = nDecodedSize / sizeof(float);
                                    signed short * ar = new signed short[nDecodedSize / sizeof(float)];
                                    float* pointer = (float*)pFrameAudio;
                                    // Convert float to S16.
                                    for (int i = 0; i < size / 2; i ++)
                                    {
                                        ar[i] = pointer[i] * 32767.0f;
                                    }

                                    FILE* file = fopen("c:\\temp\\AudioRaw.raw", "ab");
                                    fwrite(ar, 1, size * sizeof (signed short) / 2, file);
                                    fclose(file);
                                }
                                */
                            }

                            // Delete buffer.
                            delete[] pFrameAudio;
                            pFrameAudio = nullptr;
                        }
                    }
                }

                av_packet_free(reinterpret_cast<AVPacket **>(&packet));
                packet = AVPacket();
            }

            av_free(pVideoYuv);
        }
    }

    return res;
}


AVFrame *FFmpegDecoder::GetRGBAFrame(AVFrame *pFrameYuv) {
    AVFrame *frame = nullptr;
    int _width = pVideoCodecCtx->width;
    int _height = pVideoCodecCtx->height;
    // Determine required buffer size and allocate buffer
    // avpicture_get_size deprecated, instead use av_image_get_buffer_size()
    // align may be 1, I don't know why))), see - https://stackoverflow.com/questions/35678041/what-is-linesize-alignment-meaning
    int bufferImgSize = av_image_get_buffer_size(AV_PIX_FMT_BGR24, _width, _height, 1);
    frame = av_frame_alloc();
    auto *buffer = (uint8_t *) av_mallocz(bufferImgSize);
    if (frame) {
        // avpicture_fill() is deprecated, instead use av_image_fill_arrays()
        // align may be 1, I don't know why))), see - https://stackoverflow.com/questions/35678041/what-is-linesize-alignment-meaning
        av_image_fill_arrays(frame->data, frame->linesize, buffer, AV_PIX_FMT_BGR24, _width,
                             _height, 1);
        frame->width = _width;
        frame->height = _height;
        //frame->data[0] = buffer;

        sws_scale(pImgConvertCtx, pFrameYuv->data, pFrameYuv->linesize,
                  0, _height, frame->data, frame->linesize);
    }

    return (AVFrame *) frame;
}


bool FFmpegDecoder::OpenVideo() {
    bool res = false;

    if (pFormatCtx) {
        videoStreamIndex = -1;

        for (unsigned int i = 0; i < pFormatCtx->nb_streams; i++) {
            if (pFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
                videoStreamIndex = i;
                pVideoCodecCtx = reinterpret_cast<AVCodecContext *>(pFormatCtx->streams[i]->codecpar);
                pVideoCodec = (AVCodec *) avcodec_find_decoder(pVideoCodecCtx->codec_id);

                if (pVideoCodec) {
                    res = avcodec_open2(pVideoCodecCtx, pVideoCodec, nullptr) >= 0;
                    width = pVideoCodecCtx->coded_width;
                    height = pVideoCodecCtx->coded_height;
                }

                break;
            }
        }

        if (!res) {
            CloseVideo();
        } else {
            pImgConvertCtx = sws_getContext(pVideoCodecCtx->width, pVideoCodecCtx->height,
                                            pVideoCodecCtx->pix_fmt,
                                            pVideoCodecCtx->width, pVideoCodecCtx->height,
                                            AV_PIX_FMT_BGR24,
                                            SWS_BICUBIC, nullptr, nullptr, nullptr);
        }
    }

    return res;
}

bool FFmpegDecoder::DecodeVideo(const AVPacket *avpkt, AVFrame *pOutFrame) {
    bool res = false;

    if (pVideoCodecCtx) {
        if (avpkt && pOutFrame) {
            // avcodec_decode_video2() deprecated, instead - avcodec_send_packet and avcodec_receive_frame
            int got_picture_ptr = 0;
            avcodec_send_packet(pVideoCodecCtx, avpkt);
            int videoFrameBytes = avcodec_receive_frame(pVideoCodecCtx, pOutFrame);

//			avcodec_decode_video(pVideoCodecCtx, pOutFrame, &videoFrameBytes, pInBuffer, nInbufferSize);
            res = (videoFrameBytes > 0);
        }
    }

    return res;
}


bool FFmpegDecoder::OpenAudio() {
    bool res = false;

    if (pFormatCtx) {
        audioStreamIndex = -1;

        for (unsigned int i = 0; i < pFormatCtx->nb_streams; i++) {
            if (pFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_AUDIO) {
                audioStreamIndex = i;
                pAudioCodecCtx = reinterpret_cast<AVCodecContext *>(pFormatCtx->streams[i]->codecpar);
                pAudioCodec = (AVCodec *) avcodec_find_decoder(pVideoCodecCtx->codec_id);
                if (pAudioCodec) {
                    res = avcodec_open2(pAudioCodecCtx, pAudioCodec, nullptr) >= 0;
                }
                break;
            }
        }

        if (!res) {
            CloseAudio();
        }
    }

    return res;
}


void FFmpegDecoder::CloseVideo() {
    if (pVideoCodecCtx) {
        avcodec_close(pVideoCodecCtx);
        pVideoCodecCtx = nullptr;
        pVideoCodec = nullptr;
        videoStreamIndex = 0;
    }
}


void FFmpegDecoder::CloseAudio() {
    if (pAudioCodecCtx) {
        avcodec_close(pAudioCodecCtx);
        pAudioCodecCtx = nullptr;
        pAudioCodec = nullptr;
        audioStreamIndex = 0;
    }
}


int FFmpegDecoder::DecodeAudio(int nStreamIndex, const AVPacket *avpkt, uint8_t *pOutBuffer,
                               size_t nOutBufferSize) {
    int decodedSize = 0;

    int packetSize = avpkt->size;
    auto *pPacketData = (uint8_t *) avpkt->data;

    while (packetSize > 0) {
        int sizeToDecode = nOutBufferSize;
        uint8_t *pDest = pOutBuffer + decodedSize;
        int got_picture_ptr = 0;
        AVFrame *audioFrame = av_frame_alloc();

        // avcodec_decode_video2() deprecated, instead - avcodec_send_packet and avcodec_receive_frame
        avcodec_send_packet(pAudioCodecCtx, avpkt);
        int packetDecodedSize = avcodec_receive_frame(pAudioCodecCtx, audioFrame);

        if (packetDecodedSize > 0) {
            sizeToDecode = av_samples_get_buffer_size(nullptr, audioFrame->ch_layout.nb_channels,
                                                      audioFrame->nb_samples,
                                                      (AVSampleFormat) audioFrame->format, 1);

            // Currently we process only AV_SAMPLE_FMT_FLTP.
            if ((AVSampleFormat) audioFrame->format == AV_SAMPLE_FMT_FLTP) {
                // Copy each channel plane.
                //channels is deprecated, instead is ch_layout.nb_channels
                for (int i = 0; i < audioFrame->ch_layout.nb_channels; i++) {

                    memcpy(pDest + i * sizeToDecode / audioFrame->ch_layout.nb_channels,
                           audioFrame->extended_data[i],
                           sizeToDecode / audioFrame->ch_layout.nb_channels);
                }
            }
        }

        if (packetDecodedSize < 0) {
            decodedSize = 0;
            break;
        }

        packetSize -= packetDecodedSize;
        pPacketData += packetDecodedSize;

        if (sizeToDecode <= 0) {
            continue;
        }

        decodedSize += sizeToDecode;
        av_frame_free(&audioFrame);
        audioFrame = nullptr;
    }

    return decodedSize;
}