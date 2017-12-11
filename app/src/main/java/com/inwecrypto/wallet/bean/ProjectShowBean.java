package com.inwecrypto.wallet.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xiaoji06 on 2017/11/14 15:19
 * github：https://github.com/xiaoji06
 * 功能：
 */

public class ProjectShowBean implements Serializable {

    private int type;
    private ProjectBean project1;
    private ProjectBean project2;
    private ProjectBean project3;
    private ProjectBean project4;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ProjectBean getProject1() {
        return project1;
    }

    public void setProject1(ProjectBean project1) {
        this.project1 = project1;
    }

    public ProjectBean getProject2() {
        return project2;
    }

    public void setProject2(ProjectBean project2) {
        this.project2 = project2;
    }

    public ProjectBean getProject3() {
        return project3;
    }

    public void setProject3(ProjectBean project3) {
        this.project3 = project3;
    }

    public ProjectBean getProject4() {
        return project4;
    }

    public void setProject4(ProjectBean project4) {
        this.project4 = project4;
    }
}
