package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    //注册Fast系统客户端
    @Autowired
    private FastFileStorageClient storageClient;

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif","image/jpeg");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    public String uploadImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String ext = StringUtils.substringAfterLast(fileName, ".");
        //检验文件类型
            String contenType = file.getContentType();

            if (!CONTENT_TYPES.contains(contenType)) {
                LOGGER.info("文件类型不合法: {}", fileName);
                return null;
            }
        try {
            //检验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法：{}", fileName);
                return null;
            }

            //保存到服务器
            //file.transferTo(new File("C:\\Users\\Hello\\Desktop\\image\\" + fileName));
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            //返回url

            //return "http://image.leyou.com/" + fileName;
            return "http://image.leyou.com/"+storePath.getFullPath();
        }catch (Exception e){
            LOGGER.info("服务器内部：{}",fileName);
            e.printStackTrace();
        }
        return null;
    }
}
