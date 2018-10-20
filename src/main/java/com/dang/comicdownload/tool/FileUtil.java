package com.dang.comicdownload.tool;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static void downLoadImage(String imagUrl,String savePath){
        InputStream inputStream = null;
        try {
            byte[] data = HttpUtil.doGet(imagUrl);
            FileUtils.writeByteArrayToFile(new File(savePath),data);
        } catch (IOException e) {
        e.printStackTrace();
    }
    }
}
