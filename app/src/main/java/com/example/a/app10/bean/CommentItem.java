package com.example.a.app10.bean;

/**
 * Created by 12917 on 2017/6/15.
 */

public class CommentItem {
    private String questionContent;
    private String userName;
    private String infoId;
    private String createTime_sys;
    private String photoUrl;
    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getCreateTime_sys() {
        return createTime_sys;
    }

    public void setCreateTime_sys(String createTime_sys) {
        this.createTime_sys = createTime_sys;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
