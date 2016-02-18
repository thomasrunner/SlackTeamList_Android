package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SlackAPIRequests slackapirequests;
    ListView SlackTeamListView;
    SwipeRefreshLayout PulltoRefresh;

    private List<UserModel> teamfulllist = new ArrayList();

    //This will be used as the actual UI list once search and sorting
    //private List<UserModel> teamlist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlackTeamListView = (ListView) findViewById(R.id.SlackTeamListView);
        PulltoRefresh = (SwipeRefreshLayout) findViewById(R.id.PulltoRefresh);

        PulltoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                PulltoRefresh.setRefreshing(false);
                if(Utils.isNetworkAvailable(MainActivity.this)) {
                    slackapirequests.AddUrlParameter("presence", "1");
                    slackapirequests.SlackAPIRequest(MainActivity.this, "xoxp-5048173296-5048487710-19045732087-b5427e3b46", "https://slack.com/api/users.list");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You are offline or airplane mode.", Toast.LENGTH_LONG).show();
                    LoadJSONResponse();
                }
            }
        });

        // Configure the refreshing colors, like things colorful :)
        PulltoRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        slackapirequests = new SlackAPIRequests();
        slackapirequests.AddUrlParameter("presence", "1");

        //In a more complete solution I would put token as part of a logged in User Security Class, this allows for easier profile switching and portability as it can also be saved in the
        //users roaming profile for Android. HERE IS JUST A SAMPLE TOKEN JUST REPLACE WITH YOUR OWN TEAMS TOKEN TO PLAY WITH
        //This function calls back to RequestCallBack.
        if(Utils.isNetworkAvailable(MainActivity.this)) {
            slackapirequests.SlackAPIRequest(this, "xoxp-5048173296-5048487710-19045732087-b5427e3b46", "https://slack.com/api/users.list");
        }
        else
        {
            LoadJSONResponse();
        }
    }

    //Entry point to process JSON data
    public void RequestCallback(String jsonresponse)
    {
        if(jsonresponse.length() > 0) {
            ParseJSON(jsonresponse);
        }
    }

    //Parse JSON data and create
    public void ParseJSON(String jsonresponse)
    {
        if (jsonresponse != null) {
            teamfulllist.clear();
            try {
                JSONObject jsonObj = new JSONObject(jsonresponse);

                // Getting JSON Array node
                JSONArray members;
                members = jsonObj.getJSONArray("members");

                // looping through All Contacts
                for (int i = 0; i < members.length(); i++) {
                    JSONObject member = members.getJSONObject(i);

                    UserModel newmember = new UserModel();
                    newmember.id = member.getString("id");
                    newmember.name = member.getString("name");

                    if (member.has("deleted"))
                    {
                        if (member.getString("deleted").toLowerCase().contentEquals("true") )
                        {
                            newmember.deleted = true;
                        }
                        else
                        {
                            newmember.deleted = false;
                        }
                    }

                    if (member.has("is_admin"))
                    {
                        if (member.getString("is_admin").toLowerCase().contentEquals("true") )
                        {
                            newmember.is_admin = true;
                        }
                    }

                    if (member.has("is_owner"))
                    {
                        if (member.getString("is_owner").toLowerCase().contentEquals("true") )
                        {
                            newmember.is_owner = true;
                        }
                    }

                    if (member.has("has_files"))
                    {
                        if (member.getString("has_files") != null)
                        {
                            if (member.getString("has_files").toLowerCase().contentEquals("true") )
                            {
                                newmember.has_files = true;
                            }
                        }
                    }

                    if (member.has("has_2fa"))
                    {
                        if (member.getString("has_2fa").toLowerCase().contentEquals("true") )
                        {
                            newmember.has_2fa = true;
                        }
                    }

                    if (member.has("presence"))
                    {
                        newmember.presence = member.getString("presence");
                    }

                    //User Profile Class which is a subclass of User
                    UserProfileModel newmemberprofile = new UserProfileModel();
                    JSONObject profilejtoken = member.getJSONObject("profile");

                    if (profilejtoken.has("first_name")) newmemberprofile.first_name = profilejtoken.getString("first_name");
                    if (profilejtoken.has("last_name")) newmemberprofile.last_name = profilejtoken.getString("last_name");
                    if (profilejtoken.has("real_name")) newmemberprofile.real_name = profilejtoken.getString("real_name");
                    if (profilejtoken.has("title")) newmemberprofile.title = profilejtoken.getString("title");
                    if (profilejtoken.has("email")) newmemberprofile.email = profilejtoken.getString("email");
                    if (profilejtoken.has("skype")) newmemberprofile.skype = profilejtoken.getString("skype");
                    if (profilejtoken.has("phone")) newmemberprofile.phone = profilejtoken.getString("phone");
                    if (profilejtoken.has("image_24")) newmemberprofile.image_24 = profilejtoken.getString("image_24");
                    if (profilejtoken.has("image_32")) newmemberprofile.image_32 = profilejtoken.getString("image_32");
                    if (profilejtoken.has("image_48")) newmemberprofile.image_48 = profilejtoken.getString("image_48");
                    if (profilejtoken.has("image_72")) newmemberprofile.image_72 = profilejtoken.getString("image_72");
                    if (profilejtoken.has("image_192")) newmemberprofile.image_192 = profilejtoken.getString("image_192");

                    newmember.profile = newmemberprofile;

                    // Adding team member to full member list
                    teamfulllist.add(newmember);

                    //Save only good JSON responses
                    SaveJSONResponse(jsonresponse);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            BindListViewtoAdapter();
        } else {
            Log.e("Slack Handler:", "Couldn't get any data from the url");
        }
    }

    void SaveJSONResponse(String jsonresponse)
    {
        SharedPreferences preferences = getSharedPreferences("MEMBERS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("members",jsonresponse);

        editor.apply();
    }

    void LoadJSONResponse()
    {
        SharedPreferences prfs = getSharedPreferences("MEMBERS", Context.MODE_PRIVATE);
        String jsonresponse = prfs.getString("members", "");
        if(jsonresponse.length() > 0)
        {
            ParseJSON(jsonresponse);
        }
    }

    private void BindListViewtoAdapter()
    {
        // Create custom adapter
        ListAdapter adapter = new MemberAdapter(this, teamfulllist);

        // Assign adapter to ListView
        SlackTeamListView.setAdapter(adapter);

        // ListView Item Click Listener
        SlackTeamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                UserModel  member = (UserModel) SlackTeamListView.getItemAtPosition(position);

                // Navigate User to Profile Activity
                Intent i = new Intent(getBaseContext(), ProfileActivity.class);
                i.putExtra("member", member);
                startActivity(i);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
