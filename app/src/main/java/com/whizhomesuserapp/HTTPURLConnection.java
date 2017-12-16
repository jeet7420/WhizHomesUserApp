package com.whizhomesuserapp;
/**
 * Created by smarhas on 7/8/2017.
 */
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HTTPURLConnection {

    JSONObject json;
    String input;

    public JSONObject invokeService(String path, HashMap<String, String> params) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            input = getPostDataString(params);

            System.out.println("HTTP Data : " + input);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() == 200) {
                System.out.println("success");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            System.out.println("Output from Server .... \n");
            String jsonText = readAll(br);
            json = new JSONObject(jsonText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        JSONObject dataAsJson = new JSONObject(params);
        return dataAsJson.toString();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}