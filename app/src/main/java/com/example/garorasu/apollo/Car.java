package com.example.garorasu.apollo;

/**
 * Created by garorasu on 20/10/16.
 */
public class Car {
    long _id;
    String _vid;
    String _ocp;
    String _inTime;
    String _outTime;
    boolean _img;
    int _pFee;

    //Empty Constructor
    public Car() {
    }


    //Constructor
    public Car(long id, String vid,String inTime, String outTime,String ocp, boolean img, int pFee) {
        this._id = id;
        this._vid = vid;
        this._ocp = ocp;
        this._inTime = inTime;
        this._outTime = outTime;
        this._img = img;
        this._pFee = pFee;
    }


    public void CarIn(long id,String vid,String inTime,boolean img,int pFee){
        this._id = id;
        this._vid = vid;
        this._ocp = "true";
        this._inTime = inTime;
        this._img = img;
        this._pFee = pFee;
    }
    public void CarOut(String outTime){
        this._outTime = outTime;
        this._ocp = "false";
    }
    public String get_vid(){
        return _vid;
    }
    public long get_id(){
        return _id;
    }
    public String get_ocp(){
        return _ocp;
    }
    public String get_inTime(){
        return _inTime;
    }
    public String get_outTime(){
        return _outTime;
    }
    public boolean get_img(){
        return _img;
    }
    public int get_pFee(){
        return _pFee;
    }
}

