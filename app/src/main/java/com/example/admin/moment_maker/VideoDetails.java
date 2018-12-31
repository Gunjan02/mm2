package com.example.admin.moment_maker;

public class VideoDetails {
    private String vidId;
    private String name;
    private String date;
    private String videoUrl;
    VideoDetails(){
    }

    VideoDetails(String vidId,String name,String date,String videoUrl){
        this.vidId=vidId;
        this.name=name;
        this.date=date;
        this.videoUrl=videoUrl;

    }

    public String getVidId() {
        return vidId;
    }

    public void setVidId(String vidId) {
        this.vidId = vidId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
