package com.example.nyy.crime_info_portal.model;

/**
 * Created by nyy on 4/19/15.
 */
public class Usr_info {
    private String fname;
    private String lname;
    private String mname;
    private int password;
    private int usrid;

    public String getfname() {
        return fname;
    }
    public void setfname(String fname) {
        this.fname = fname;
    }
    public String getlname() {
        return lname;
    }
    public void setlname(String lname) {
        this.lname = lname;
    }
    public String getmname() {
        return mname;
    }
    public void setmname(String mname) {
        this.mname = mname;
    }
    public int getpassword() {
        return password;
    }
    public void setpassword(int password) {
        this.password = password;
    }
    public int getusr_id() {
        return usrid;
    }
    public void setusr_id(int usr_id) {
        this.usrid = usrid;
    }
}
