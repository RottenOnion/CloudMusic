package com.example.cloudmusic.bean;

/**
 * Created by py on 2016/12/20.
 */

public class SearchData {
    private int ret_code;
    private PageBean pagebean;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public PageBean getPagebean() {
        return pagebean;
    }

    public void setPagebean(PageBean pagebean) {
        this.pagebean = pagebean;
    }
}
