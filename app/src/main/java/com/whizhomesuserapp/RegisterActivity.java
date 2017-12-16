package com.whizhomesuserapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by smarhas on 7/5/2017.
 */

public class RegisterActivity extends AppCompatActivity {
    HashMap<String, String> postDataParams;
    private ProgressDialog pDialog;
    EditText etUsername, etPassword, etEmail, etPhone, etDOB;
    String username, password, email, phone, dob, homeId, response;
    JSONObject jsonResponse;
    Button btnRegister;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent accessTokenIntent=getIntent();
        homeId=accessTokenIntent.getStringExtra("homeId");

        etUsername=(EditText)findViewById(R.id.etUsername);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etDOB=(EditText)findViewById(R.id.etDOB);

        btnRegister=(Button)findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=etUsername.getText().toString();
                password=etPassword.getText().toString();
                email=etEmail.getText().toString();
                phone=etPhone.getText().toString();
                dob=etDOB.getText().toString();

                new RegisterActivity.MyAsyncTask().execute();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                HTTPURLConnection httpurlConnection = new HTTPURLConnection();
                postDataParams=new HashMap<String, String>();
                postDataParams.put("homeId",homeId);
                postDataParams.put("email",email);
                postDataParams.put("name",username);
                postDataParams.put("phone",phone);
                postDataParams.put("password",password);
                jsonResponse = httpurlConnection.invokeService(StaticValues.registerURL, postDataParams);
                try{
                    response=jsonResponse.get("key").toString();
                }
                catch (Exception e){
                    e.printStackTrace();
                    return StaticValues.registerServiceResponseIssue;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return StaticValues.registerServiceDown;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if(result!=null){
                if(result.equals("-1")){
                    Toast.makeText(getApplicationContext(), "ISSUE WITH REGISTER SERVICE", Toast.LENGTH_LONG).show();
                }
                else if(result.equals("P") || result.equals("F")){
                    Toast.makeText(getApplicationContext(), "REGISTER SUCCESSFULL", Toast.LENGTH_LONG).show();
                    Intent registerIntent=new Intent(RegisterActivity.this,MainActivity.class);
                    RegisterActivity.this.startActivity(registerIntent);
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
