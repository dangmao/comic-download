package com.dang.comicdownload.service;

import java.io.UnsupportedEncodingException;

public interface DownloadService {
    public void download(String url);
    public void downloadById(String comicId) throws UnsupportedEncodingException;
    public void downloadByEpisode(String comicId,String EpisodeName) throws UnsupportedEncodingException;

}
