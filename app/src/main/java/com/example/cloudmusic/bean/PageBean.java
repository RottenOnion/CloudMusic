package com.example.cloudmusic.bean;

import java.util.List;

/**
 * Created by py on 2016/12/20.
 */

public class PageBean {
    private String w;
    private int allPages;
    private int ret_code;

    private List<SongData> contentlist;

    private int currentPage;
    private String notice;
    private int allNum;
    private int maxResult;

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<SongData> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<SongData> contentlist) {
        this.contentlist = contentlist;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }
}
