package com.rulaibao.bean;


import com.rulaibao.network.types.IMouldType;

// 课程详情 简介
public class ResultClassDetailsIntroductionBean implements IMouldType {


    private ResultClassDetailsIntroductionItemBean course;

    public ResultClassDetailsIntroductionItemBean getCourse() {
        return course;
    }

    public void setCourse(ResultClassDetailsIntroductionItemBean course) {
        this.course = course;
    }
}