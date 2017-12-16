package com.whizhomesuserapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout.LayoutParams layoutParamsForButtons=null;
    LinearLayout.LayoutParams layoutParams=null;
    TreeMap<Integer, String> roomIdMap=null;
    HashMap<String, String> roomMap=null;
    LinearLayout linearLayout=null;
    LinearLayout row=null;
    String roomId, roomName;
    Button button=null;
    Intent roomIntent=null;
    ImageButton imageButton=null;
    int position=1;
    int buttonId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("INSIDE MAIN ACTIVITY");
        roomIdMap = new TreeMap<Integer, String>();
        roomMap = new HashMap<String, String>();
        roomMap = StaticValues.serverResult.get("Rooms");

        linearLayout = (LinearLayout) findViewById(R.id.LL2);
        /*imageButton=new ImageButton(this);
        imageButton.setImageResource(R.drawable.deviceoff);
        linearLayout.addView(imageButton);*/
        for (String key : roomMap.keySet()) {
            roomId = key;
            roomName = roomMap.get(key);
            System.out.println("Room Id : " + roomId);
            button = new Button(this);
            //button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            //button.setImageResource(R.drawable.deviceoff);
            button.setBackgroundColor(Color.BLACK);
            button.setText(roomName);
            button.setTextColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            //button.setWidth(40);
            //button.setHeight(150);
            //imageButton.setWidth(20);
            //imageButton.setHeight(20);
            layoutParamsForButtons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 150);
            if (roomId.length() == 2) {
                button.setId(Integer.parseInt(roomId.substring(1, 2)));
            } else {
                button.setId(Integer.parseInt(roomId.substring(1, 3)));
            }
            roomIdMap.put(button.getId(), roomId);
            if ((position % 2) == 1) {
                System.out.println("check1");
                row = new LinearLayout(this);
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(120, 20, 120, 10);
                layoutParamsForButtons.setMargins(0, 0, 60, 0);
                button.setLayoutParams(layoutParamsForButtons);
                //button.setPadding(0, 0, 0, 0);
                //layoutParams.height = 75;
                //layoutParams.width = 75;
                //row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setGravity(Gravity.CENTER);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(layoutParams);
                row.addView(button);
            } else {
                layoutParamsForButtons.setMargins(60, 0, 0, 0);
                button.setLayoutParams(layoutParamsForButtons);
                row.addView(button);
                linearLayout.addView(row);
            }
            //imageButton.setText(roomName);
            position++;
        }
        System.out.println("ROOM ID DETAILS");
        System.out.println();

        for (int i : roomIdMap.keySet()) {
            System.out.println(i + " : " + roomIdMap.get(i));
        }

        for (int buttonId : roomIdMap.keySet()) {
            button = (Button) findViewById(buttonId);
            button.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        buttonId=v.getId();
        System.out.println("BUTTON CLICKED : " + buttonId);

        roomIntent=new Intent(MainActivity.this, RoomActivity.class);
        roomIntent.putExtra("roomId", roomIdMap.get(buttonId));
        StaticValues.roomId=roomIdMap.get(buttonId);
        System.out.println("Room Opened : " + roomIdMap.get(buttonId));
        MainActivity.this.startActivity(roomIntent);
    }
}
