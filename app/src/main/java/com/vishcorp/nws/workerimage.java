package com.vishcorp.nws;

public class workerimage {
    String image,workername ,workeremail;

    public workerimage(String image, String workername, String workeremail) {
        this.image = image;
        this.workername = workername;
        this.workeremail = workeremail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWorkername() {
        return workername;
    }

    public void setWorkername(String workername) {
        this.workername = workername;
    }

    public String getWorkeremail() {
        return workeremail;
    }

    public void setWorkeremail(String workeremail) {
        this.workeremail = workeremail;
    }
}
