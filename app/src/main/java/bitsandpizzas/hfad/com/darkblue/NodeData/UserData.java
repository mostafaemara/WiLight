package bitsandpizzas.hfad.com.darkblue.NodeData;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class UserData {
    private static String MY_PREFS_NAME="userdata";
    private static String USERNAME_PREFS_KEY="username";
    private static String PASSWORD_PREFS_KEY="password";
    private static String HOST_PREFS_KEY="host";
    private static String PORT_PREFS_KEY="port";
    private static String HUB_ID_PREFS_KEY="hubid";
    private static String IS_EMPTY_PREFS_KEY="empty";

  static private   String username;
     static private  String password;
     static private  String host;
     static private  String port;


     static private  long hubId;


    public UserData() {

    }

    public static  long getHubId() {
        return hubId;
    }

    public static String getHost() {
        return host;
    }

    public static String getPassword() {
        return password;
    }

    public static String getPort() {
        return port;
    }

    public static String getUsername() {
        return username;
    }
    static public boolean isEmpty(Context context){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean empty = prefs.getBoolean(IS_EMPTY_PREFS_KEY, true);




        return empty;

    }
    static public void getSaved(Context context){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
       username= prefs.getString(USERNAME_PREFS_KEY, null);
        password= prefs.getString(PASSWORD_PREFS_KEY, null);
        host= prefs.getString(HOST_PREFS_KEY, null);
        port= prefs.getString(PORT_PREFS_KEY, null);
        hubId=prefs.getLong(HUB_ID_PREFS_KEY,12);







    }


    static public void setData(Context context,String usrname,String psswrd,String sHost,String sPort,long sHubID) {

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(USERNAME_PREFS_KEY,usrname);
        editor.putString(PASSWORD_PREFS_KEY,psswrd);
        editor.putString(HOST_PREFS_KEY,sHost);
        editor.putString(PORT_PREFS_KEY,sPort);
        editor.putLong(HUB_ID_PREFS_KEY,sHubID);
        editor.putBoolean(IS_EMPTY_PREFS_KEY,false);
        editor.apply();

    }
}
