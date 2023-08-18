package com.wsmrxd.bloglite.service.impl;

import com.wsmrxd.bloglite.entity.ImageMapping;
import com.wsmrxd.bloglite.mapping.ImageMapper;
import com.wsmrxd.bloglite.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageMapper mapper;

    @Autowired
    public void setMapper(ImageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Cacheable(value = "ImageMapping", key = "#imgID")
    public ImageMapping getImageMappingByID(int imgID) {
        return mapper.selectImageByID(imgID);
    }

    @Override
    @Cacheable(value = "ImageMappingMD5", key = "#md5", unless = "#result == null")
    public Integer checkImageExistsByMD5(String md5) {
        return mapper.selectImageByMD5(md5);
    }

    @Override
    public Integer insertImageMapping(ImageMapping img) {
        mapper.insertImageMapping(img);
        return img.getId();
    }
}
