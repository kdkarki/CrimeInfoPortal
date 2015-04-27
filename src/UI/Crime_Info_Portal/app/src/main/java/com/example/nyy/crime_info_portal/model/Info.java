package com.example.nyy.crime_info_portal.model;

/**
 * Created by nyy on 4/23/15.
 */
public class Info {
    private String usr_name;
    private int password;
    private int state_id;
    private String state_code;
    private String state_name;
    private int city_id;
    private String city_name;
    private String address1;
    private String address2;
    private int zipcode;

    public String get_usr_name(){return usr_name;}
    public void set_usr_name(String usr_name){this.usr_name=usr_name;}
    public int get_password(){return password;}
    public void set_password(int password){this.password = password;}
    public String get_address1(){return address1;}
    public void set_address1(String address1){this.address1=address1;}
    public String get_address2(){return address2;}
    public void set_address2(String address2){this.address2=address2;}
    public int get_city_id(){return city_id;}
    public void set_city_id(int city_id){this.city_id=city_id;}
    public String get_city_name(){return city_name;}
    public void set_city_name(String city_name){this.city_name=city_name;}
    public String get_state_code(){return state_code;}
    public void set_state_code(String state_code){this.state_code=state_code;}
    public int get_state_id(){return state_id;}
    public void set_state_id(int state_id){this.state_id=state_id;}
    public String get_state_name(){return state_name;}
    public void set_state_name(String state_name){this.state_name=state_name;}
    public int get_zipcode(){return zipcode;}
    public void set_zipcode(int zipcode){this.zipcode=zipcode;}
}

