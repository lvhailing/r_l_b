package com.rulaibao.bean;


import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

import java.util.ArrayList;

// 课程详情 PPT
public class ResultClassDetailsPPTBean implements IMouldType {


    ArrayList<String> pptImgs;      //

    public ArrayList<String> getPptImgs() {
        return pptImgs;
    }

    public void setPptImgs(ArrayList<String> pptImgs) {
        this.pptImgs = pptImgs;
    }
}