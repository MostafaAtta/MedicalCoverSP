package com.atta.medicalcoversp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context mContext;
    private static SessionManager mInstance;

    private static final String PREF_NAME = "med_pref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DOCTOR_ID = "doctor_id";
    private static final String KEY_PHARMACY_ID = "pharmacy_id";
    private static final String KEY_CENTER_ID = "center_id";
    private static final String KEY_IS_LOGIN = "is_login";

    //mode 0 private
    private static final int MODE = 0;

    private SessionManager(Context mContext) {
        this.mContext = mContext;
        pref = mContext.getSharedPreferences(PREF_NAME, MODE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context context){
        if (mInstance == null){
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }

    public void login(User user){
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_USERNAME, user.getFullName());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putInt(KEY_TYPE, user.getType());
        editor.putString(KEY_DOCTOR_ID, user.getDoctorId());
        editor.putString(KEY_PHARMACY_ID, user.getPharmacyId());
        editor.putString(KEY_CENTER_ID, user.getCenterId());
        editor.putBoolean(KEY_IS_LOGIN, true);

        editor.apply();
    }


    public String getEmail(){

        return pref.getString(KEY_EMAIL, "no mail");
    }


    public String getDoctorId(){

        return pref.getString(KEY_DOCTOR_ID, "");
    }

    public String getPharmacyId(){

        return pref.getString(KEY_PHARMACY_ID, "");
    }


    public String getCenterId(){

        return pref.getString(KEY_CENTER_ID, "");
    }



    public int getType(){

        return pref.getInt(KEY_TYPE, 4);
    }

    public String getUsername(){
        String name = pref.getString(KEY_USERNAME, "no name");

        return name;
    }

    public boolean isLogin(){
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public void logout(){
        editor.clear();
        editor.putBoolean(KEY_IS_LOGIN, false);
        editor.apply();
    }
}
