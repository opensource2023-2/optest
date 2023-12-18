package com.example.test;

public class Note {
    String key, bnum, title, material, content, writerID;

    public Note(){

    }

    public String getTitle(){
        return title;
    }

    public String getMaterial() { return material;}

    public String getContent(){
        return content;
    }

    public String getWriterID() {return writerID;}

    public String getBnum() { return bnum; }

    public String getKey(){
        return key;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMaterial(String material) { this.material = material; }

    public void setWriterID(String writerID) { this.writerID = writerID; }

    public void setBnum(String bnum) { this.bnum = bnum; }

    public void setContent(String content){
        this.content = content;
    }

    public void setKey(String key){
        this.key = key;
    }
}
