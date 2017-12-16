package com.whizhomesuserapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by smarhas on 7/7/2017.
 */

public class RoomActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout.LayoutParams layoutParamsForButtons=null;
    LinearLayout.LayoutParams layoutParams=null;
    RelativeLayout.LayoutParams layoutParamsForLabel=null;
    TreeMap<Integer, String> deviceIdMap=null;
    HashMap<String, String> deviceMap=null;
    LinearLayout linearLayout=null;
    RelativeLayout rowLabel=null;
    LinearLayout row=null;
    TextView tvDeviceLabel;
    Intent deviceIntent=null;
    ImageButton imageButton=null;
    int position=3;
    int tvDeviceLabelId=1;
    String roomName=null;
    ImageButton btnLight, btnFan, btnWindow, btnAc;
    String signal, deviceId, deviceType, deviceStatus, roomId, homeId, response;
    TextView roomHeader;
    HashMap<String, String> postDataParams;
    JSONObject jsonResponse, jsonObject;
    private ProgressDialog pDialog;
    public Context roomContext;
    WebView mWebView;
    String username;
    private Handler uiHandler;
    int buttonId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        roomContext=RoomActivity.this;
        username=StaticValues.USERNAME;
        uiHandler = new Handler();
        System.out.println("USERNAME : " + username);
        homeId=StaticValues.serverResult.get("homeId").get("homeId");
        deviceIdMap=new TreeMap<Integer, String>();
        roomHeader = (TextView) findViewById(R.id.roomHeader);
        Intent roomIntent = getIntent();
        StaticValues.globalRoomIntent=roomIntent;
        roomId=roomIntent.getStringExtra("roomId");
        roomName=StaticValues.serverResult.get("Rooms").get(roomId);
        System.out.println("INSIDE ROOM ACTIVITY FOR ROOM : " + roomName);
        //Toast.makeText(getApplicationContext(), roomIntent.getStringExtra("p_roomnumber"), Toast.LENGTH_LONG).show();
        //roomHeader.setText("JEET");
        roomHeader.setText(roomName);
        roomHeader.setTextColor(Color.BLACK);

        System.out.println("RoomId Inside Room : " + roomId);
        //setLightStatus(StaticValues.ISLIGHTON);
        //setFanStatus(StaticValues.FANSTATUS);
        //setWindowStatus(StaticValues.ISWINDOWOPEN);


        deviceMap=StaticValues.serverResult.get(roomId);

        linearLayout=(LinearLayout)findViewById(R.id.LL2);
        /*imageButton=new ImageButton(this);
        imageButton.setImageResource(R.drawable.deviceoff);
        linearLayout.addView(imageButton);*/
        for (String key : deviceMap.keySet()) {
            deviceId=key;
            deviceType=deviceMap.get(key);
            System.out.println("Device Id : " + deviceId);
            imageButton=new ImageButton(this);
            imageButton.setImageResource(R.drawable.deviceoff);
            imageButton.setBackgroundColor(Color.TRANSPARENT);
            layoutParamsForButtons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(deviceId.length()==2){
                imageButton.setId(Integer.parseInt(deviceId.substring(1,2)));
            }
            else{
                imageButton.setId(Integer.parseInt(deviceId.substring(1,3)));
            }
            deviceIdMap.put(imageButton.getId(), deviceId);
            StaticValues.deviceIdMapFull.put(imageButton.getId(), deviceId);
            //tvDeviceLabel=new TextView(this);
            //tvDeviceLabel.setText(StaticValues.serverResult.get(roomId).get(deviceId));
            //tvDeviceLabel.setTextColor(Color.BLACK);
            if((position%3)==0){
                //rowLabel=new RelativeLayout(this);
                //layoutParamsForLabel=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                //layoutParamsForLabel.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                //layoutParamsForLabel.setMargins(0, 5, 30, 0);
                //rowLabel.setLayoutParams(layoutParamsForLabel);
                //rowLabel.addView(tvDeviceLabel);
                System.out.println("check1");
                row=new LinearLayout(this);
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(120, 60, 120, 10);
                layoutParamsForButtons.setMargins(0, 0, 30, 0);
                imageButton.setLayoutParams(layoutParamsForButtons);
                row.setGravity(Gravity.CENTER);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(layoutParams);
                row.addView(imageButton);
            }
            else if(((position+1)%3)==0){
                System.out.println("check2");
                layoutParamsForButtons.setMargins(30, 0, 0, 0);
                //layoutParamsForLabel.setMargins(30, 5, 0, 0);
                imageButton.setLayoutParams(layoutParamsForButtons);
                //rowLabel.addView(tvDeviceLabel);
                row.addView(imageButton);
                //row.addView(rowLabel);
                linearLayout.addView(row);
            }
            else{
                System.out.println("check3");
                layoutParamsForButtons.setMargins(30, 0, 20, 0);
                //layoutParamsForLabel.setMargins(30, 5, 20, 0);
                imageButton.setLayoutParams(layoutParamsForButtons);
                //rowLabel.addView(tvDeviceLabel);
                row.addView(imageButton);
            }
            position++;
        }

        if((deviceIdMap.size()%3)!=0){
            //row.addView(rowLabel);
            linearLayout.addView(row);
        }

        System.out.println("DEVICE ID DETAILS");
        System.out.println();

        for(int i : deviceIdMap.keySet()){
            System.out.println(i + " : " + deviceIdMap.get(i));
        }

        setInitialDeviceStatus();

        for(int buttonId : deviceIdMap.keySet()){
            imageButton=(ImageButton)findViewById(buttonId);
            imageButton.setOnClickListener(this);
        }

        setupViews();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this,MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupViews() {
        StringGetter stringGetter = new StringGetter(this);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings mWebSettings = mWebView.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        //Load URL inside WebView


        if(!StaticValues.connectionStatus){
            mWebView.loadUrl("file:///android_asset/Demo.html");
            StaticValues.connectionStatus=true;
        }

   /* mButton = (Button) findViewById(R.id.button);
    mButton.setOnClickListener(new Button.OnClickListener() {
        public void onClick(View v) {
            mWebView.loadUrl("javascript:fillContent()");
        }
    });*/
    }

    public class WebAppInterface {

        Context mContext;
        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            String [] devicesList=toast.split("&");
            String deviceId=devicesList[2];
            //for(Integer i : StaticValues.deviceIdMapFull.keySet()){
            //    if(StaticValues.deviceIdMapFull.get(i).equals(deviceId)){
            //        buttonId=i;
            //    }
            //}
            String roomIdFromWebSocket=devicesList[1];
            deviceStatus=devicesList[4];
            //String deviceType=StaticValues.serverResult.get(roomId).get(deviceId);
            deviceType=devicesList[3];
            System.out.println(".....Device Type : " + deviceType + " Device Status : " + deviceStatus);
            StaticValues.deviceStatus.put(deviceId, deviceStatus);
            if(!(StaticValues.roomId.equals(roomIdFromWebSocket))){
                Toast.makeText(getApplicationContext(), "Web Socket : " + roomIdFromWebSocket, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Room : " + StaticValues.roomId, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplication(), toast, Toast.LENGTH_LONG).show();
            //Intent intent = getIntent();
            finish();
            startActivity(StaticValues.globalRoomIntent);
            /*if(deviceType.equals(StaticValues.TYPEISAC)){
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("AC : " + buttonId);
                        setAcStatus(deviceStatus, buttonId);
                    }
                });
            }
            if(deviceType.equals(StaticValues.TYPEISFAN)){
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("FAN : " + buttonId);
                        setFanStatus(deviceStatus, buttonId);
                    }
                });
            }
            if(deviceType.equals(StaticValues.TYPEISWINDOW)){
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("WINDOW : " + buttonId);
                        setWindowStatus(deviceStatus, buttonId);
                    }
                });
            }
            if(deviceType.equals(StaticValues.TYPEISLIGHT1) || deviceType.equals(StaticValues.TYPEISLIGHT2)
               || deviceType.equals(StaticValues.TYPEISLIGHT3) || deviceType.equals(StaticValues.TYPEISLIGHT4)){
                System.out.println("Device Type : " + deviceType + " Device Status : " + deviceStatus);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("LIGHT : " + buttonId);
                        setLightStatus(deviceStatus, buttonId);
                    }
                });

            }*/
            //toast=devicesList[0].substring(1,2)+devicesList[1];
            //serialPort.write(toast.getBytes());
            //Serial port code goes here fm
            // Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String getString() {
            return username;

        }
    }

    public void setInitialDeviceStatus(){
        for(int buttonId : deviceIdMap.keySet()){
            imageButton=(ImageButton)findViewById(buttonId);
            deviceId=deviceIdMap.get(buttonId);
            deviceType=StaticValues.serverResult.get(roomId).get(deviceId);
            deviceStatus=StaticValues.deviceStatus.get(deviceId);
            if(deviceType.equals(StaticValues.TYPEISLIGHT1) || deviceType.equals(StaticValues.TYPEISLIGHT2)
                    || deviceType.equals(StaticValues.TYPEISLIGHT3) || deviceType.equals(StaticValues.TYPEISLIGHT4)
                    || deviceType.equals(StaticValues.TYPEISWINDOW)){
                if(deviceStatus.equals("0")){
                    imageButton.setImageResource(R.drawable.deviceoff);
                }
                else{
                    imageButton.setImageResource(R.drawable.lighton);
                }
            }

            if(deviceType.equals(StaticValues.TYPEISFAN) || deviceType.equals(StaticValues.TYPEISAC)){
                if(deviceStatus.equals("0")){
                    imageButton.setImageResource(R.drawable.deviceoff);
                }
                else if(deviceStatus.equals("1")){
                    imageButton.setImageResource(R.drawable.fan1);
                }
                else if(deviceStatus.equals("2")){
                    imageButton.setImageResource(R.drawable.fan2);
                }
                else if(deviceStatus.equals("3")){
                    imageButton.setImageResource(R.drawable.fan3);
                }
                else if(deviceStatus.equals("4")){
                    imageButton.setImageResource(R.drawable.fan4);
                }
                else{
                    imageButton.setImageResource(R.drawable.fan5);
                }
            }
        }
    }

    public class StringGetter {
        Context jContext;

        StringGetter(Context context){
            jContext = context;
        }
        @JavascriptInterface
        public String getString() {
            return username;

        }
    }

    @Override
    public void onClick(View v) {
        buttonId=v.getId();
        System.out.println("BUTTON CLICKED : " + buttonId);
        deviceId=deviceIdMap.get(buttonId);
        deviceType=StaticValues.serverResult.get(roomId).get(deviceId);

        System.out.println("BUTTON ID : " + deviceId);
        System.out.println("BUTTON TYPE : " + deviceType);

        if(deviceType.equals(StaticValues.TYPEISLIGHT1) || deviceType.equals(StaticValues.TYPEISLIGHT2)
                || deviceType.equals(StaticValues.TYPEISLIGHT3) || deviceType.equals(StaticValues.TYPEISLIGHT4)){
            if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.LIGHTON)) {
                signal = StaticValues.LIGHTOFMESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.LIGHTOF);
                setLightStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else {
                signal = StaticValues.LIGHTONMESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.LIGHTON);
                setLightStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
        }
        if(deviceType.equals(StaticValues.TYPEISFAN)){
            if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.FANOF)){
                signal=StaticValues.FAN1MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.FAN1);
                setFanStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.FAN1)){
                signal=StaticValues.FAN2MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.FAN2);
                setFanStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.FAN2)){
                signal=StaticValues.FAN3MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.FAN3);
                setFanStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.FAN3)){
                signal=StaticValues.FAN4MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.FAN4);
                setFanStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.FAN4)){
                signal=StaticValues.FAN5MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.FAN5);
                setFanStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else{
                signal=StaticValues.FANOFMESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.FANOF);
                setFanStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
        }
        if(deviceType.equals(StaticValues.TYPEISWINDOW)){
            if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.WINDOWOPEN)) {
                signal = StaticValues.WINDOWCLOSEMESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.WINDOWCLOSE);
                setWindowStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else {
                signal = StaticValues.WINDOWOPENMESAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.WINDOWOPEN);
                setWindowStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
        }
        if(deviceType.equals(StaticValues.TYPEISAC)){
            if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.ACOF)){
                signal=StaticValues.AC1MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.AC1);
                setAcStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.AC1)){
                signal=StaticValues.AC2MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.AC2);
                setAcStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.AC2)){
                signal=StaticValues.AC3MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.AC3);
                setAcStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.AC3)){
                signal=StaticValues.AC4MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.AC4);
                setAcStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else if(StaticValues.deviceStatus.get(deviceId).equals(StaticValues.AC4)){
                signal=StaticValues.AC5MESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.AC5);
                setAcStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
            else{
                signal=StaticValues.ACOFMESSAGE;
                StaticValues.deviceStatus.put(deviceId, StaticValues.ACOF);
                setAcStatus(StaticValues.deviceStatus.get(deviceId), buttonId);
                new MyAsyncTask().execute();
            }
        }
    }

    public void setLightStatus(String status, int btnId){
        System.out.println("lightcheck4");
        btnLight=(ImageButton)findViewById(btnId);
        if(status.equals("1")){
            System.out.println("lightcheck5");
            btnLight.setImageResource(R.drawable.lighton);
        }
        else{
            System.out.println("lightcheck6");
            btnLight.setImageResource(R.drawable.deviceoff);
        }
    }

    public void setFanStatus(String status, int btnId){
        btnFan=(ImageButton)findViewById(btnId);
        if(status.equals("1")){
            System.out.println("check3 " + StaticValues.FANSTATUS);
            btnFan.setImageResource(R.drawable.fan1);
            System.out.println("check4 " + StaticValues.FANSTATUS);
        }
        else if(status.equals("2")){
            btnFan.setImageResource(R.drawable.fan2);
        }
        else if(status.equals("3")){
            btnFan.setImageResource(R.drawable.fan3);
        }
        else if(status.equals("4")){
            btnFan.setImageResource(R.drawable.fan4);
        }
        else if(status.equals("5")){
            btnFan.setImageResource(R.drawable.fan5);
        }
        else{
            btnFan.setImageResource(R.drawable.deviceoff);
            System.out.println("check1 " + status);
        }
    }

    public void setWindowStatus(String status, int btnId){
        btnWindow=(ImageButton)findViewById(btnId);
        if(status.equals("1")){
            btnWindow.setImageResource(R.drawable.lighton);
        }
        else{
            btnWindow.setImageResource(R.drawable.deviceoff);
        }
    }

    public void setAcStatus(String status, int btnId){
        btnAc=(ImageButton)findViewById(btnId);
        if(status.equals("1")){
            System.out.println("check3 " + StaticValues.ACSTATUS);
            btnAc.setImageResource(R.drawable.fan1);
            System.out.println("check4 " + StaticValues.ACSTATUS);
        }
        else if(status.equals("2")){
            btnAc.setImageResource(R.drawable.fan2);
        }
        else if(status.equals("3")){
            btnAc.setImageResource(R.drawable.fan3);
        }
        else if(status.equals("4")){
            btnAc.setImageResource(R.drawable.fan4);
        }
        else if(status.equals("5")){
            btnAc.setImageResource(R.drawable.fan5);
        }
        else{
            btnAc.setImageResource(R.drawable.deviceoff);
            System.out.println("check1 " + status);
        }
    }


    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //pDialog = new ProgressDialog(RoomActivity.this);
            //pDialog.setMessage("Please wait...");
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                HTTPURLConnection httpurlConnection = new HTTPURLConnection();
                postDataParams=new HashMap<String, String>();
                postDataParams.put("deviceId",deviceId);
                postDataParams.put("homeId",homeId);
                postDataParams.put("userId",StaticValues.USERNAME);
                postDataParams.put("status",signal);
                postDataParams.put("source",StaticValues.SOURCEMANUAL);
                jsonResponse = httpurlConnection.invokeService(StaticValues.deviceActionURL, postDataParams);
                try{
                    response=jsonResponse.get("response").toString();
                }
                catch (Exception e){
                    e.printStackTrace();
                    return StaticValues.deviceActionResponseIssue;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return StaticValues.deviceActionServiceDown;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            System.out.println(result);
            //if (pDialog.isShowing())
              //  pDialog.dismiss();
        }
    }
}

