package com.example.nyy.crimereport.model;

/**
 * Created by nyy on 4/14/15.
 */
public class Crime {
    private String address;
    private int age;
    private String charge_code;
    private int city_id;
    private String city_name;
    private String type;
    private String date_arrested;
    private String fname;
    private String mname;
    private String lname;
    private int record_id;
    private String state_code;
    private int state_id;
    private String state_name;
    private String date_reported;


    public String get_address(){return address;}
    public void set_address(String address){this.address=address;}
    public int get_age(){return age;}
    public void set_age(int age){this.age=age;}
    public String get_charge_code(){return charge_code;}
    public void set_charge_code(String charge_code){this.charge_code=charge_code;}
    public int get_city_id(){return city_id;}
    public void set_city_id(int city_id){this.city_id=city_id;}
    public String get_city_name(){return city_name;}
    public void set_city_name(String city_name){this.city_name=city_name;}
    public String get_type(){return type;}
    public void set_type(String type){this.type=type;}
    public String get_date_arrested(){return date_arrested;}
    public void set_date_arrested(String date_arrested){this.date_arrested = date_arrested;}
    public String get_fname(){return fname;}
    public void set_fname(String fname){this.fname = fname;}
    public String get_mname(){return mname;}
    public void set_mname(String mname){this.mname = mname;}
    public String get_lname(){return lname;}
    public void set_lname(String lname){this.lname = lname;}
    public int get_record_id(){return record_id;}
    public void set_Record_id(int record_id){this.record_id=record_id;}
    public String get_state_code(){return state_code;}
    public void set_state_code(String state_code){this.state_code=state_code;}
    public int get_state_id(){return state_id;}
    public void set_state_id(int state_id){this.state_id=state_id;}
    public String get_state_name(){return state_name;}
    public void set_state_name(String state_name){this.state_name=state_name;}
    public String get_date_reported(){return date_reported;}
    public void set_date_reported(String date_reported){this.date_reported=date_reported;}
}
