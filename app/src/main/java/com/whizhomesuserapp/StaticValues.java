package com.whizhomesuserapp;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by smarhas on 7/7/2017.
 */

public class StaticValues {
    public static final String loginURL="http://www.whizindia.com/rest/login/authenticateUser";
    public static final String authURL="http://www.whizindia.com/rest/login/authenticateToken";
    public static final String registerURL="http://www.whizindia.com/rest/register/user";
    public static final String deviceActionURL="http://www.whizindia.com/rest/action/device";
    public static Context globalRoomContext;
    public static Intent globalRoomIntent;
    public static boolean connectionStatus=false;
    //public static final String loginURL="http://192.168.0.27:8899/SmHome/rest/login/authenticateUser";
    //public static final String authURL="http://192.168.0.27:8899/SmHome/rest/login/authenticateToken";
    //public static final String registerURL="http://192.168.0.27:8899/SmHome/rest/register/user";
    //public static final String deviceActionURL="http://192.168.0.27:8899/SmHome/rest/action/device";

    public static final String loginServiceDown="LOGIN SERVICE DOWN";
    public static final String authServiceDown="AUTHORIZE TOKEN SERVICE DOWN";
    public static final String registerServiceDown="REGISTER SERVICE DOWN";
    public static final String deviceActionServiceDown="DEVICE ACTION SERVICE DOWN";

    public static final String loginServiceResponseIssue="LOGIN SERVICE RESPONSE ISSUE";
    public static final String authServiceResponseIssue="AUTHORIZE TOKEN SERVICE RESPONSE ISSUE";
    public static final String registerServiceResponseIssue="REGISTER SERVICE RESPONSE ISSUE";
    public static final String deviceActionResponseIssue="DEVICE ACTION RESPONSE ISSUE";

    public static String USERNAME="";
    public static final String SOURCEMANUAL="MANUAL";

    public static boolean ISLIGHTON=false;
    public static int FANSTATUS=0;
    public static boolean ISWINDOWOPEN=false;
    public static int ACSTATUS=0;

    public static final String LIGHTOF="0";
    public static final String LIGHTON="1";

    public static final String FANOF="0";
    public static final String FAN1="1";
    public static final String FAN2="2";
    public static final String FAN3="3";
    public static final String FAN4="4";
    public static final String FAN5="5";

    public static final String ACOF="0";
    public static final String AC1="1";
    public static final String AC2="2";
    public static final String AC3="3";
    public static final String AC4="4";
    public static final String AC5="5";

    public static final String WINDOWCLOSE="0";
    public static final String WINDOWOPEN="1";

    public static final String LIGHTOFMESSAGE="0";
    public static final String LIGHTONMESSAGE="1";

    public static final String FANOFMESSAGE="0";
    public static final String FAN1MESSAGE="1";
    public static final String FAN2MESSAGE="2";
    public static final String FAN3MESSAGE="3";
    public static final String FAN4MESSAGE="4";
    public static final String FAN5MESSAGE="5";

    public static final String ACOFMESSAGE="0";
    public static final String AC1MESSAGE="1";
    public static final String AC2MESSAGE="2";
    public static final String AC3MESSAGE="3";
    public static final String AC4MESSAGE="4";
    public static final String AC5MESSAGE="5";

    public static final String WINDOWCLOSEMESSAGE="0";
    public static final String WINDOWOPENMESAGE="1";

    public static final String TYPEISFAN="Fan";
    public static final String TYPEISLIGHT1="Light 1";
    public static final String TYPEISLIGHT2="Light 2";
    public static final String TYPEISLIGHT3="Light1";
    public static final String TYPEISLIGHT4="Light2";
    public static final String TYPEISWINDOW="Window";
    public static final String TYPEISAC="AC";

    public static HashMap<String, HashMap<String,String>> serverResult;
    public static HashMap<String,String> deviceStatus;
    public static TreeMap<Integer, String> deviceIdMapFull=new TreeMap<Integer, String>();

    public static String roomId="R0";
}
