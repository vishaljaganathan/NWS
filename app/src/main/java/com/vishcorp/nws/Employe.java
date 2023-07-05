package com.vishcorp.nws;

public class Employe {
    String ename,eimage,ejob;

    public Employe() {

    }

    public Employe(String ename, String eimage, String ejob) {
        this.ename = ename;
        this.eimage = eimage;
        this.ejob = ejob;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEimage() {
        return eimage;
    }

    public void setEimage(String eimage) {
        this.eimage = eimage;
    }

    public String getEjob() {
        return ejob;
    }

    public void setEjob(String ejob) {
        this.ejob = ejob;
    }
}
