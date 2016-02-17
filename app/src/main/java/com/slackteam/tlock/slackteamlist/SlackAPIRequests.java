package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Random;

// Specific class as the interface for all Slack API calls and output JSON results
public class SlackAPIRequests {

    LinkedHashMap<String, String> urlparameters = new LinkedHashMap<>();
    String jsonresponse = "";

    MainActivity parent = null;

    public void ClearUrlParameters()
    {
        if (urlparameters.size() > 0) urlparameters.clear();
    }

    public void AddUrlParameter(String Key, String Value)
    {
        if(Key.length() > 0 && Value.length() > 0)
        {
            urlparameters.put(Key, Value);
        }
    }

    public String SlackAPIRequest(MainActivity mainactivity, String token, String apiurl)
    {
        APIRequest task = new APIRequest();
        Random r = new Random();
        String rv = String.valueOf(r.nextInt(32));

        parent = mainactivity;
        String encodedmessage = "";

        try {
            encodedmessage = (String) URLEncoder.encode(encodedmessage, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        task.execute(new String[]{token, apiurl, ""});
        return jsonresponse;
    }

    //  Actual API call Task, the User Token must be added as part of this call and not in the dictionary.
    private class APIRequest extends AsyncTask<String, String, String> {
        //EXCELLENT CODE FOR LIKES AND COMMENTS
        @Override
        protected String doInBackground(String... s) {
            String output = null;
            String token = s[0];
            String apiurl = s[1];

            if (token.length() == 0) return "Error: No token provided.";
            if (apiurl.length() == 0) return "Error: No api url provided.";

            StringBuilder urlrequeststring = new StringBuilder();

            urlrequeststring.append(apiurl);
            urlrequeststring.append("?token=");
            urlrequeststring.append(token);

            if(urlparameters.size() > 0)
            {
                for(LinkedHashMap.Entry<String,String> lhm: urlparameters.entrySet()){
                    urlrequeststring.append("&");
                    urlrequeststring.append(lhm.getKey());
                    urlrequeststring.append("=");
                    urlrequeststring.append(lhm.getValue());
                }
            }

            urlparameters.clear();

            output = getOutputFromUrl(urlrequeststring.toString());

            return output;
        }

        private String getOutputFromUrl(String url) {


            StringBuffer output = new StringBuffer("");
            try {

                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        protected void onPostExecute(String output) {

            jsonresponse = output;

            parent.RequestCallback();
            //System.out.println(jsonresponse);

        }
    }
    

}
