package com.example.joeyb.remotecontrol;

/**
 * Created by joeyb on 8/15/2017.
 */

class Project {
    private String title, name, address, type;

    public Project(String title, String name, String address, String type) {
        this.title = title;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
