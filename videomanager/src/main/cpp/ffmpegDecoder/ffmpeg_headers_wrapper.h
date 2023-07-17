#pragma once

extern "C" {
#include <libavformat/avformat.h>
#include <libavformat/avio.h>
#include <libavcodec/avcodec.h>
#include <libavutil/log.h>
#include <libswresample/swresample.h>
#include <libswscale/swscale.h>
#include <libavcodec/packet.h>
#include <libavutil/imgutils.h>
#include "../utils/Logger.h"
}