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
@RequestMapping("/api/admin/img")
public class WebImageAdminAPI {

    final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    private String imageRepositoryPath;

    private String nginxContext;

    private ImageService imageService;

    @Value("${myConfig.image.repositoryPath}")
    public void setImageRepositoryPath(String imageRepositoryPath) {
        this.imageRepositoryPath = imageRepositoryPath;
    }

    @Value("${myConfig.image.nginxContext}")
    public void setNginxContext(String nginxContext) {
        this.nginxContext = nginxContext;
    }

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    // TODO: 理论上这个API能够存储任何上传的文件，应加入过滤
    @PutMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RestResponse<String> uploadImage(MultipartFile uploadImage) throws IOException {
        String targetFolderName = getYearMonthPrefix();
        File targetFolder = checkTargetDir(targetFolderName);
        var uploadImageStream = uploadImage.getInputStream();

        String md5 = DigestUtils.md5DigestAsHex(uploadImageStream);
        Integer queryID = imageService.checkImageExistsByMD5(md5);

        if(queryID != null) {
            uploadImageStream.close();
            return RestResponse.ok(nginxContext + getImageFileName(imageService.getImageMappingByID(queryID)));
        }

        String typeSuffix = parseTypeSuffix(Objects.requireNonNull(uploadImage.getOriginalFilename()));

        var image = new ImageMapping(md5, targetFolderName, uploadImage.getOriginalFilename(), typeSuffix);
        imageService.insertImageMapping(image);

        if(image.getId() != null)
            saveImageToLocal(uploadImage, targetFolder, image);

        uploadImageStream.close();
        return RestResponse.ok(nginxContext + getImageFileName(image));
    }

    private static void saveImageToLocal(MultipartFile uploadImage, File targetFolder, ImageMapping imageMapping) throws IOException {
        Integer imageID = imageMapping.getId();
        String typeSuffix = imageMapping.getTypeSuffix();

        String targetPath = targetFolder.getPath() + "/" + imageID;
        if(typeSuffix.length() > 0 && typeSuffix.length() < 11)
            targetPath += "." + typeSuffix;

        File targetFile = new File(targetPath);
        uploadImage.transferTo(targetFile);
    }

    private static String parseTypeSuffix(String fileName) {
        String typeSuffix = "";
        var split = fileName.split("\\.");
        if(split.length > 1)
            typeSuffix = split[split.length - 1];
        return typeSuffix;
    }

    private String getImageFileName(ImageMapping imageMapping){
        String ret = imageMapping.getFolder() + "/" + imageMapping.getId();
        if(imageMapping.getTypeSuffix() != null)
            ret += "." + imageMapping.getTypeSuffix();
        return ret;
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
