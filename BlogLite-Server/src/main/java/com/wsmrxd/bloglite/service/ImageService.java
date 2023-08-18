package com.wsmrxd.bloglite.service;

import com.wsmrxd.bloglite.entity.ImageMapping;

public interface ImageService {

    ImageMapping getImageMappingByID(int imgID);

    Integer insertImageMapping(ImageMapping img);
}
