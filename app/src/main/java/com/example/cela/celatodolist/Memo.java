package com.example.cela.celatodolist;
//資料規格 (資料格式)
class Memo {
    private int id;
    private String memo;
    private String date;
    private String bg_color;
    private int remind;
    //建立建構子
    public Memo(int id, String memo, String date, String bg_color, int remind) {
        this.id = id;
        this.memo = memo;
        this.date = date;
        this.bg_color = bg_color;
        this.remind = remind;
    }
    //getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }
}


