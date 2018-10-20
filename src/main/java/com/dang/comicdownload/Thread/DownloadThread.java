package com.dang.comicdownload.Thread;

import com.dang.comicdownload.service.impl.DownloadServiceImpl;
import com.dang.comicdownload.tool.FileUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class DownloadThread implements Runnable{
    private String url;
    private String savePath;

    public DownloadThread(String url,String savePath){
        this.url = url;
        this.savePath = savePath;
    }
    @Override
    public void run() {
        FileUtil.downLoadImage(url,savePath);
        DownloadServiceImpl.count.countDown();
    }
}
