package com.wsmrxd.bloglite.mapping;

import com.wsmrxd.bloglite.entity.ImageMapping;
import com.wsmrxd.bloglite.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageMappingTest {

    @Autowired
    ImageMapper mapper;

    @Autowired
    ImageService imageService;

    @Test
    public void testImageMapping(){
        ImageMapping image = new ImageMapping();
        image.setFolder("114514");
        image.setMd5("1919");

        var result = mapper.selectImageByID(1);
        System.out.println(result);

        System.out.println(imageService.insertImageMapping(image));
    }
}
