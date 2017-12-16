package com.whizhomesuserapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by smarhas on 7/3/2017.
 */

public class LoginActivity extends AppCompatActivity {

    HashMap<String, String> postDataParams;
    EditText etUsername, etPassword;
    private ProgressDialog pDialog;
    String username, password, response;
    JSONObject jsonResponse, jsonObject;
    Button btnLogin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername=(EditText)findViewById(R.id.etUsername);
        etPassword=(EditText)findViewById(R.id.etPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=etUsername.getText().toString();
                password=etPassword.getText().toString();
                StaticValues.USERNAME=username;
                new MyAsyncTask().execute();
            }
        });
    }

    public void registerAction(View view) {
        Intent registerIntent = new Intent(LoginActivity.this,AuthorizationActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    public static HashMap<String, HashMap<String,String>> jsonToHashMap(JSONObject object) throws JSONException {
        HashMap<String, HashMap<String,String>> map = new HashMap<String, HashMap<String,String>>();
        HashMap<String, String> innerMap=null;
        Iterator<String> keysItrOuter = object.keys();
        while(keysItrOuter.hasNext()) {
            String keyOuter = keysItrOuter.next();
            Object valueOuter = object.get(keyOuter);
            JSONObject innerJsonObject = (JSONObject)valueOuter;
            Iterator<String> keysItrInner = innerJsonObject.keys();
            innerMap = new HashMap<String, String>();
            while(keysItrInner.hasNext()){
                String keyInner = keysItrInner.next();
                Object valueInner = innerJsonObject.get(keyInner);
                String valueInnerAsString = valueInner.toString();
                innerMap.put(keyInner, valueInnerAsString);
            }
            map.put(keyOuter, innerMap);
        }
        return map;
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                StaticValues.serverResult=new HashMap<String, HashMap<String,String>>();
                HTTPURLConnection httpurlConnection = new HTTPURLConnection();
                postDataParams=new HashMap<String, String>();
                postDataParams.put("email",username);
                postDataParams.put("password",password);
                StaticValues.USERNAME=username;
                jsonResponse = httpurlConnection.invokeService(StaticValues.loginURL, postDataParams);
                StaticValues.serverResult=jsonToHashMap(jsonResponse);
                StaticValues.deviceStatus=StaticValues.serverResult.get("Devices");
                System.out.println("CHECK 1 : " + StaticValues.serverResult.get("key"));
                System.out.println("CHECK 2 : " + StaticValues.serverResult.get("homeId"));
                System.out.println("CHECK 3 : " + StaticValues.serverResult.get("Rooms"));
                System.out.println("CHECK 4 : " + StaticValues.serverResult.get("R1"));
                System.out.println("CHECK 5 : " + StaticValues.serverResult.get("R2"));
                System.out.println("CHECK 6 : " + StaticValues.serverResult.get("R3"));
                System.out.println("CHECK 7 : " + StaticValues.serverResult.get("R4"));
                System.out.println("CHECK 8 : " + StaticValues.serverResult.get("R5"));
                System.out.println("CHECK 9 : " + StaticValues.serverResult.get("R6"));
                System.out.println("CHECK 9 : " + StaticValues.serverResult.get("Devices"));
                try{
                    jsonObject=(JSONObject)jsonResponse.get("key");
                    response=jsonObject.get("access").toString();
                }
                catch (Exception e){
                    e.printStackTrace();
                    return StaticValues.loginServiceResponseIssue;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return StaticValues.loginServiceDown;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if(result!=null){
                if(result.equals("-1")){
                    Toast.makeText(getApplicationContext(), "ISSUE WITH LOGIN SERVICE", Toast.LENGTH_LONG).show();
                }
                else if(result.equals("-999")){
                    Toast.makeText(getApplicationContext(), "INVALID CREDENTIALS", Toast.LENGTH_LONG).show();
                }
                else if(result.equals("F")){
                    Toast.makeText(getApplicationContext(), "ADMIN LOGIN SUCCESSFULL", Toast.LENGTH_LONG).show();
                    Intent loginAdminIntent=new Intent(LoginActivity.this,MainActivity.class);
                    LoginActivity.this.startActivity(loginAdminIntent);
                }
                else if(result.equals("P")){
                    Toast.makeText(getApplicationContext(), "USER LOGIN SUCCESSFULL", Toast.LENGTH_LONG).show();
                    Intent loginUserIntent=new Intent(LoginActivity.this,MainActivity.class);
                    LoginActivity.this.startActivity(loginUserIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "RESPONSE FROM SEVER IS NULL", Toast.LENGTH_LONG).show();
            }

            System.out.println(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}
