package com.unccstudio.gems.gems;

/**
 * Created by Administrator on 3/24/2015.
 */
public class GEM {
    private String name, macAddress, timeSinceLastMoved, color;
    private int age; //time since was added
    private int temperature, shelfLife;
    private boolean attached;

    public GEM() {
        this.name = "";
        this.macAddress = "";
        this.timeSinceLastMoved = "";
        this.color = "";
        this.age = 0;
        this.temperature = 0;
        this.shelfLife = 0;
        this.attached = false;
    }

    public GEM(String name, String macAddress, String timeSinceLastMoved, String color, int age, int temperature, int shelfLife, boolean attached) {
        this.name = name;
        this.macAddress = macAddress;
        this.timeSinceLastMoved = timeSinceLastMoved;
        this.color = color;
        this.age = age;
        this.temperature = temperature;
        this.shelfLife = shelfLife;
        this.attached = attached;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getTimeSinceLastMoved() {
        return timeSinceLastMoved;
    }

    public void setTimeSinceLastMoved(String timeSinceLastMoved) {
        this.timeSinceLastMoved = timeSinceLastMoved;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public boolean isAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }
}
