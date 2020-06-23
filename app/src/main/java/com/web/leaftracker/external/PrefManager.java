package com.web.leaftracker.external;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Ravi on 08/07/15.
 */
public class PrefManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Survey_Pref";

    // All Shared Preferences Keys
    private static final String KEY_REGISTRATION = "registration_no";
    private static final String KEY_USER_KEY = "userkey";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_FATHER_NAME = "fathername";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DIST = "dist";
    private static final String KEY_STATE = "state";
    private static final String KEY_PIN = "pin";
    private static final String KEY_STG = "isStg";
    private static final String KEY_COLLECTOR = "isCollector";
    private static final String KEY_BLF = "isBlf";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILE = "phone";
    private static final String KEY_CURRENT_LOGIN = "login_type";
    private static final String KEY_ISONTRIP = "is_on_trip";



    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public String getKeyRegistration() {
        return pref.getString(KEY_REGISTRATION, null);
    }

    public String getKeyCurrentLogin() {
        return pref.getString(KEY_CURRENT_LOGIN,null);
    }

    public String setKeyCurrentLogin(String data){
        editor.putString(KEY_CURRENT_LOGIN,data);
        editor.commit();
        return data;
    }

    public boolean getKeyIsOnTrip() {
        return pref.getBoolean(KEY_ISONTRIP, false);
    }

    public void setKeyIsOnTrip(boolean isOnTrip){
        editor.putBoolean(KEY_ISONTRIP,isOnTrip);
        editor.commit();
    }

    public String getKeyName() {
        return pref.getString(KEY_NAME, null);
    }

    public String getKeyFatherName() {
        return pref.getString(KEY_FATHER_NAME, null);
    }

    public String getKeyAddress() {
        return pref.getString(KEY_ADDRESS, null);
    }

    public String getKeyDist() {
        return pref.getString(KEY_DIST, null);
    }

    public String getKeyState(){
        return pref.getString(KEY_STATE, null);
    }

    public String getKeyPin(){
        return pref.getString(KEY_PIN, null);
    }

    public String getKeyStg() {
        return pref.getString(KEY_STG, null);
    }

    public String getKeyCollector() {
        return pref.getString(KEY_COLLECTOR, null);
    }

    public String getKeyBlf() {
        return pref.getString(KEY_BLF, null);
    }

    public String getKeyEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getKeyMobile() {
        return pref.getString(KEY_MOBILE, null);
    }

    public void createLogin(String name,String email,String phone,String registration_no,String fathername,String address,
                            String  dist,String  state,String  pin,String isStg,String isCollector,String  isBlf) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, phone);
        editor.putString(KEY_REGISTRATION, registration_no);
        editor.putString(KEY_FATHER_NAME, fathername);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_DIST, dist);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_PIN, pin);
        editor.putString(KEY_STG, isStg);
        editor.putString(KEY_COLLECTOR, isCollector);
        editor.putString(KEY_BLF, isBlf);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

}
