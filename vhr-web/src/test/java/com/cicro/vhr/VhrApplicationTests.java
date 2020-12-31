package com.cicro.vhr;

import com.cicro.vhr.mapper.MenuMapper;
import com.cicro.vhr.model.Menu;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@SpringBootTest(classes = VhrApplication.class)
@RunWith(SpringRunner.class)
class VhrApplicationTests {


    @Autowired
    private MenuMapper menuMapper;

    @Test
    void contextLoads() {
        List<Menu> allRolesByMenus = menuMapper.getAllRolesByMenus();
        System.out.println(allRolesByMenus);
    }

    @Test
    void test() throws IOException, MyException {
        //加载配置文件
        ClientGlobal.initByProperties("fastdfs-client.properties");

        //构造TrackerClient 对象
        TrackerClient trackerClient=new TrackerClient();

        //获取TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageServer storageServer=null;
        //构造StorageClient1
        StorageClient1 storageClient1=new StorageClient1(trackerServer,storageServer);

        //NameValuePair 中保存的是文件的元数据信息，如果有的话，就以 key/value 的方式来设置，如果没有的话，直接给一个 null 即可。
        NameValuePair[] pairs=null;

        //文件上传
        String strings = storageClient1.upload_file1("D:\\downLoad\\微信截图_20200813114949.png", "png", pairs);
        System.out.println(strings);

    }

    @Test
    void testDownload() {
        try {
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient1 client = new StorageClient1(trackerServer, storageServer);

            //直接调用 download_file1 方法获取到一个 byte 数组，然后通过 IO 流写出到本地文件即可
            byte[] bytes = client.download_file1("group1/M00/00/00/wKgCgF81dHqAQXGbAAAaiSddXhw675.png");
            FileOutputStream fos = new FileOutputStream(new File("D:\\downLoad\\1.png"));
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void getToken() throws Exception {
        int ts = (int) Instant.now().getEpochSecond();
        String token = ProtoCommon.getToken("M00/00/00/wKgCgF81e3qAQjGeAAAaiSddXhw299.png", ts, "FastDFS1234567890");
        StringBuilder sb = new StringBuilder();
        sb.append("http://192.168.2.128")
            .append("/group1/M00/00/00/wKgCgF81e3qAQjGeAAAaiSddXhw299.png")
            .append("?token=")
            .append(token)
            .append("&ts=")
            .append(ts);
        System.out.println(sb.toString());
    }

}
