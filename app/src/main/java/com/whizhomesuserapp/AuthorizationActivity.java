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
 * Created by smarhas on 7/4/2017.
 */

public class AuthorizationActivity extends AppCompatActivity {
    HashMap<String, String> postDataParams;
    private ProgressDialog pDialog;
    EditText etAccessToken;
    String accessToken, response;
    JSONObject jsonResponse;
    Button btnSubmit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        etAccessToken=(EditText)findViewById(R.id.etAccessToken);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accessToken=etAccessToken.getText().toString();
                new AuthorizationActivity.MyAsyncTask().execute();
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AuthorizationActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                HTTPURLConnection httpurlConnection = new HTTPURLConnection();
                postDataParams=new HashMap<String, String>();
                postDataParams.put("token",accessToken);
                jsonResponse = httpurlConnection.invokeService(StaticValues.authURL, postDataParams);
                try{
                    response=jsonResponse.get("key").toString();
                }
                catch (Exception e){
                    e.printStackTrace();
                    return StaticValues.authServiceResponseIssue;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return StaticValues.authServiceDown;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if(result!=null){
                if(result.equals("-1")){
                    Toast.makeText(getApplicationContext(), "ISSUE WITH AUTH SERVICE", Toast.LENGTH_LONG).show();
                }
                else if(result.equals("-999")){
                    Toast.makeText(getApplicationContext(), "INVALID ACCESS TOKEN", Toast.LENGTH_LONG).show();
                }
                else if(result.charAt(0)=='H'){
                    Toast.makeText(getApplicationContext(), "HOME ID : " + result, Toast.LENGTH_LONG).show();
                    Intent authorizeIntent = new Intent(AuthorizationActivity.this,RegisterActivity.class);
                    authorizeIntent.putExtra("homeId", result);
                    AuthorizationActivity.this.startActivity(authorizeIntent);
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
