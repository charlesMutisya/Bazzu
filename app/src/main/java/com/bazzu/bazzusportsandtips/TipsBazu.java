package com.bazzu.bazzusportsandtips;



public class TipsBazu {
    private String title;
    private String body;
    private Long time;

    public TipsBazu() {
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public TipsBazu(String title, String body, Long time) {
        this.title = title;
        this.body = body;
        this.time = time;
    }
}
