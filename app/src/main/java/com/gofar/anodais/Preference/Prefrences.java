package com.gofar.anodais.Preference;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefrences {
    static final String KEY_USER_TEREGISTER ="user", KEY_PASS_TEREGISTER ="pass";
    static final String KEY_USERNAME_SEDANG_LOGIN = "Username_logged_in";
    static final String KEY_STATUS_SEDANG_LOGIN = "Status_logged_in";
    static final String KEY_STATUS_ROLE="role";
    static final String KEY_STATUS_ID="id";


    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setRegisteredUser(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USER_TEREGISTER, username);
        editor.apply();
    }

    public static String getRegisteredUser(Context context){
        return getSharedPreference(context).getString(KEY_USER_TEREGISTER,"");
    }

    public static void setRegisteredPass(Context context, String password){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_PASS_TEREGISTER, password);
        editor.apply();
    }

    public static String getRegisteredPass(Context context){
        return getSharedPreference(context).getString(KEY_PASS_TEREGISTER,"");
    }

    public static void setLoggedInUser(Context context, String username, String role, String id){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USERNAME_SEDANG_LOGIN, username);
        editor.putString(KEY_STATUS_ROLE,role);
        editor.putString(KEY_STATUS_ID,id);
        editor.apply();
    }

    public static String getLoggedInUser(Context context){
        return getSharedPreference(context).getString(KEY_USERNAME_SEDANG_LOGIN,"");
    }

    public static void setLoggedInStatus(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_SEDANG_LOGIN,status);
        editor.apply();
    }

    public static boolean getLoggedInStatus(Context context){
        return getSharedPreference(context).getBoolean(KEY_STATUS_SEDANG_LOGIN,false);
    }

    public static void clearLoggedInUser (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_STATUS_ID);
        editor.remove(KEY_USERNAME_SEDANG_LOGIN);
        editor.remove(KEY_STATUS_SEDANG_LOGIN);
        editor.apply();
    }

    public static String getRole(Context context)
    {
        return getSharedPreference(context).getString(KEY_STATUS_ROLE,"");
    }


    public static void setID(Context context,String id)
    {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_USERNAME_SEDANG_LOGIN, id);
        editor.apply();

    }

    public static String getID(Context context)
    {
        return getSharedPreference(context).getString(KEY_STATUS_ID,"");
    }


}
