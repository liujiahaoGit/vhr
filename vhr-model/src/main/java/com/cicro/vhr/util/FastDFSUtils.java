package com.cicro.vhr.util;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FastDFSUtils {
    private static StorageClient1 storageClient1 = null;

    static {
        //加载配置文件
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            //构造TrackerClient 对象
            TrackerClient trackerClient = new TrackerClient();

            //获取TrackerServer
            TrackerServer trackerServer = trackerClient.getConnection();

            StorageServer storageServer = null;
            //构造StorageClient1
            storageClient1 = new StorageClient1(trackerServer, storageServer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    public static String uploadFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        try {
            return storageClient1.upload_file1(file.getBytes(), extName, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return "";
    }

}
