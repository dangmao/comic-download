package com.dang.comicdownload.Controller;

import com.dang.comicdownload.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping(value = "/downloadView")
    public String download(){
        return "download";
    }
    @RequestMapping("/down.do")
    @ResponseBody
    public String down(String url){
        downloadService.download(url);
        return "已下载";
    }
    @RequestMapping("/down2.do")
    @ResponseBody
    public String down2(String number){
        try {
            downloadService.downloadById(number);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "已下载";
    }

}
