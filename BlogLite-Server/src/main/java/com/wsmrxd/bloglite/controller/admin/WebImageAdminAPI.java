package com.wsmrxd.bloglite.controller.admin;

import com.wsmrxd.bloglite.entity.ImageMapping;
import com.wsmrxd.bloglite.service.ImageService;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/api/img")
public class WebImageAdminAPI {

    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    private String imageRepositoryPath;

    private ImageService imageService;

    @Value("${myConfig.image.repositoryPath}")
    public void setImageRepositoryPath(String imageRepositoryPath) {
        this.imageRepositoryPath = imageRepositoryPath;
    }

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @PutMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResponse uploadImage(MultipartFile uploadImage) throws IOException {
        String targetFolderName = getYearMonthPrefix();
        File targetFolder = checkTargetDir(targetFolderName);
        var uploadImageStream = uploadImage.getInputStream();

        String md5 = DigestUtils.md5DigestAsHex(uploadImageStream);
        String typeSuffix = "";
        var split = Objects.requireNonNull(uploadImage.getOriginalFilename()).split("\\.");
        if(split.length > 1)
            typeSuffix = split[split.length - 1];

        var image = new ImageMapping();
        image.setMd5(md5);
        image.setOriginalName(uploadImage.getOriginalFilename());
        image.setFolder(targetFolderName);
        image.setTypeSuffix(typeSuffix);

        Integer imageID = imageService.insertImageMapping(image);

        if(imageID != null){
            String targetPath = targetFolder.getPath() + "/" + imageID;
            if(typeSuffix.length() > 0 && typeSuffix.length() < 11)
                targetPath += "." + typeSuffix;

            File targetFile = new File(targetPath);
            uploadImage.transferTo(targetFile);
        }

        uploadImageStream.close();
        return RestResponse.ok(imageID);
    }

    private File checkTargetDir(String folderName) {
        File targetFolder = new File(imageRepositoryPath + "/" + folderName);
        if(!targetFolder.exists())
            targetFolder.mkdir();
        return targetFolder;
    }

    private String getYearMonthPrefix(){
        return dateFormat.format(new Date());
    }
}
