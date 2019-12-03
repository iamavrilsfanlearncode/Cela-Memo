package com.example.cela.celatodolist;

public class ColorData {
    String name;
    String code;

        // 建構子生成
    public ColorData(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }
    // 右鍵 getter & setter
    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}// ColorDate End
