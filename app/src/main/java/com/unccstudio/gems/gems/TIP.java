package com.unccstudio.gems.gems;

/**
 * Created by Administrator on 4/21/2015.
 */
public class TIP {
    String title, description;
    int resID;

    public TIP() {

    }

    public TIP(String title, int resID, String description) {
        this.title = title;
        this.resID = resID;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
