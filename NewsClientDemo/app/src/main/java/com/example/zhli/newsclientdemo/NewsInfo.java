package com.example.zhli.newsclientdemo;

/**
 * Created by zhli on 2015/1/21.
 * 新闻信息实体类
 */
public class NewsInfo {
    private String title;       // title
    private String detail;      // detail
    private Integer comment;    // 跟帖数量
    private String imageUrl;    // 图片链接

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "NewsInfo{" +
                "title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", comment=" + comment +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public NewsInfo(String title, String detail, Integer comment, String imageUrl) {
        this.title = title;
        this.detail = detail;
        this.comment = comment;
        this.imageUrl = imageUrl;
    }

    public NewsInfo() {
    }

}
