package com.dang.comicdownload.service.impl;

import com.dang.comicdownload.Thread.DownloadThread;
import com.dang.comicdownload.service.DownloadService;
import com.dang.comicdownload.tool.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.*;

@Service
public class DownloadServiceImpl implements DownloadService {
    @Value("${app.savePath}")
    private String save;
    private String savePath;
    @Value("${app.download.comicUrl}")
    private String commonUrl;
    @Value("${app.download.imagUrl}")
    private String imagUrl;
    public static CountDownLatch count = new CountDownLatch(3);
    @Override
    public void download(String url) {
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.submit(new DownloadThread(url,savePath));
        pool.shutdown();
    }


    @Override
    public void downloadById(String comicId) throws UnsupportedEncodingException {
        savePath = save;
        ExecutorService pool = Executors.newCachedThreadPool();
        String comicUrl = commonUrl+"/manhua/"+comicId+"/";
        String html = new String(HttpUtil.doGet(comicUrl),"gb2312");
        Document doc = Jsoup.parse(html);
        String title = doc.select("div[class=title]").get(0).select("h1").get(0).text();
        savePath+=(title+"/");//d:/download_test/ten counts
        Elements rows = doc.select("div[class=plist pnormal]").get(0).select("ul").get(0).select("li");
        for(int i=0;i<rows.size();i++){
            String name = rows.get(i).select("a").text();
            String saveByEpisode = savePath+name+"/";//d:/download_test/ten counts/第一话/
            System.out.println(new Date()+"正在下载"+name);
            String href = rows.get(i).select("a").attr("href");
            String episodeUrl = commonUrl+href;

            html = new String(HttpUtil.doGet(episodeUrl),"gb2312");
            String picUrl = html.split("imgurl = '")[1].split("';")[0];
            int totalPage = Jsoup.parse(html).select("option").size();
//            System.out.println("picUrl:"+picUrl+",totalPage"+totalPage);
            String[] urls = getUrls(picUrl,totalPage);
            String[] savePaths = getSavePaths(saveByEpisode,totalPage);
            count = new CountDownLatch(urls.length);

            for(int j=0;j<urls.length;j++)
                pool.submit(new DownloadThread(urls[j],savePaths[j]));
            try {
                count.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        pool.shutdown();
        System.out.println("下载完成");
    }

    @Override
    public void downloadByEpisode(String comicId, String EpisodeName) throws UnsupportedEncodingException {
        ExecutorService pool = Executors.newCachedThreadPool();
        String comicUrl = commonUrl+"/manhua/"+comicId+"/";
        String html = new String(HttpUtil.doGet(comicUrl),"gb2312");
        Document doc = Jsoup.parse(html);
        String title = doc.select("div[class=title]").get(0).select("h1").get(0).text();
        savePath+=(title+"/");//d:/download_test/ten counts
        Element episode = doc.select("a[title="+EpisodeName+"]").get(0);
        String name = episode.text();
        String saveByEpisode = savePath+name+"/";//d:/download_test/ten counts/第一话/
        System.out.println(new Date()+"正在下载"+name);
        String href = episode.attr("href");
        String episodeUrl = commonUrl+href;

        html = new String(HttpUtil.doGet(episodeUrl),"gb2312");
        Elements pages = Jsoup.parse(html).select("option");

        for(int i=0;i<pages.size();i++){
            pages.get(i).val();
            String picUrl = html.split("imgurl = '")[1].split("';")[0];
        }

    }

    private String[] getUrls(String picUrl,int totalPage){
        String common = imagUrl+picUrl.substring(0,picUrl.lastIndexOf("/")+1);
        String[] result = new String[totalPage];
        String num =  picUrl.substring(picUrl.lastIndexOf("/")+1,picUrl.lastIndexOf("."));
        String pre = "";
        if(num.contains("-")){
            pre += (num.split("-")[0]+"-");
            num = num.substring(pre.length());
        }
        int firstNum = 0;
        try {
            firstNum = Integer.parseInt(num);
        }catch(Exception E){
            pre += num.substring(0,getDigitalIndex(num));
            num = num.substring(pre.length());
            firstNum = Integer.parseInt(num);
        }
        common+=pre;
        if(num.length()==2){//01
            for (int i = 0; i < totalPage; i++) {
                int temp = i + firstNum;
                if (temp < 10) {
                    result[i] = common + "0" + temp + ".jpg";
                } else {
                    result[i] = common + temp + ".jpg";
                }
            }
        }else if(num.length()==3){//001
            for (int i = 0; i < totalPage; i++) {
                int temp = i + firstNum;
                if (temp < 10) {
                    result[i] = common + "00" + temp + ".jpg";
                } else if (temp < 100) {
                    result[i] = common + "0" + temp + ".jpg";
                } else {
                    result[i] = common + temp + ".jpg";
                }
            }
        }else if(num.length()==4){
            for (int i = 0; i < totalPage; i++) {
                int temp = i + firstNum;
                if (temp < 10) {
                    result[i] = common + "000" + temp + ".jpg";
                } else if (temp < 100) {
                    result[i] = common + "00" + temp + ".jpg";
                } else if(temp < 1000){
                    result[i] = common + "0" + temp + ".jpg";
                }else{
                    result[i] = common + temp + ".jpg";
                }
            }
        }else{//1
            for (int i = 0; i < totalPage; i++) {
                int temp = i + firstNum;
                result[i] = common + temp + ".jpg";
            }
        }
        return result;
    }
    private String[] getSavePaths(String save,int totalPage){
        String[] result = new String[totalPage];
        for(int i=0;i<totalPage;i++){
            if(i<10){
                result[i] = save+"00"+i+".jpg";
            }else if(i<100){
                result[i] = save+"0"+i+".jpg";
            }else{
                result[i] = save+i+".jpg";
            }
        }
        return result;
    }
    private int getDigitalIndex(String str){
        int index = -1;
        for(int i=0;i<str.length();i++)
            if(Character.isDigit(str.charAt(i)))
            {
                index = i;
                break;
            }
        return index;
    }
}
