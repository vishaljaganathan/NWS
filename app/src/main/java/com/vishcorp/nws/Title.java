package com.vishcorp.nws;

public class Title {
    String  name,turl;

    public Title() {
    }

    public Title(String name, String turl) {
        this.name = name;
        this.turl = turl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }
}

