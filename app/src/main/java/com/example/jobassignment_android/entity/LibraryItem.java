package com.example.jobassignment_android.entity;



public class LibraryItem {

    private int id;


    private String name;


    private String registerNameEng;


    private int count;



    public LibraryItem(int id, String name, String registerNameEng, int count) {
        this.id = id;
        this.name = name;
        this.registerNameEng = registerNameEng;
        this.count = count;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterNameEng() {
        return registerNameEng;
    }

    public void setRegisterNameEng(String registerNameEng) {
        this.registerNameEng = registerNameEng;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
