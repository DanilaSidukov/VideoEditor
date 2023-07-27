
#include "Extensions.h"

std::string GetBMPFormat(std::string &path){
    size_t lastChar = path.find_last_of('\'');
    std::string bmpPath = path.erase(lastChar + 1, path.size() - 1) + "image%d.bmp";
    return bmpPath;
}
