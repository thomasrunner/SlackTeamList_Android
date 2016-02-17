package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SlackAPIRequests slackapirequests;
    ListView SlackTeamListView;

    private List<UserModel> teamfulllist = new ArrayList();

    //This will be used as the actual UI list once search and sorting
    //private List<UserModel> teamlist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlackTeamListView = (ListView) findViewById(R.id.SlackTeamListView);

        slackapirequests = new SlackAPIRequests();
        slackapirequests.AddUrlParameter("presence", "1");

        //In a more complete solution I would put token as part of a logged in User Security Class, this allows for easier profile switching and portability as it can also be saved in the
        //users roaming profile for Android. HERE IS JUST A SAMPLE TOKEN JUST REPLACE WITH YOUR OWN TEAMS TOKEN TO PLAY WITH
        //This function calls back to RequestCallBack.
        String contentstring = slackapirequests.SlackAPIRequest(this, "xoxp-5048173296-5048487710-19045732087-b5427e3b46", "https://slack.com/api/users.list");

    }

    //Entry point to process JSON data
    public void RequestCallback()
    {
        String jsonresponse = slackapirequests.jsonresponse;
        if(jsonresponse.length() > 0) {
            ParseJSON(jsonresponse);
        }
    }

    //Parse JSON data and create
    public void ParseJSON(String jsonresponse)
    {
        if (jsonresponse != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonresponse);

                // Getting JSON Array node
                JSONArray members = null;
                members = jsonObj.getJSONArray("members");

                // looping through All Contacts
                for (int i = 0; i < members.length(); i++) {
                    JSONObject member = members.getJSONObject(i);

                    UserModel newmember = new UserModel();
                    newmember.id = member.getString("id");
                    newmember.name = member.getString("name");

                    if (member.has("deleted") == true)
                    {
                        if (member.getString("deleted").toLowerCase().contentEquals("true") == true )
                        {
                            newmember.deleted = true;
                        }
                    }

                    if (member.has("is_admin") == true)
                    {
                        if (member.getString("is_admin").toLowerCase().contentEquals("true") == true )
                        {
                            newmember.is_admin = true;
                        }
                    }

                    if (member.has("is_owner") == true)
                    {
                        if (member.getString("is_owner").toLowerCase().contentEquals("true") == true )
                        {
                            newmember.is_owner = true;
                        }
                    }

                    if (member.has("has_files") == true)
                    {
                        if (member.getString("has_files") != null)
                        {
                            if (member.getString("has_files").toLowerCase().contentEquals("true") == true )
                            {
                                newmember.has_files = true;
                            }
                        }
                    }

                    if (member.has("has_2fa") == true)
                    {
                        if (member.getString("has_2fa").toLowerCase().contentEquals("true") == true )
                        {
                            newmember.has_2fa = true;
                        }
                    }

                    if (member.has("presence")== true)
                    {
                        if (member.getString("presence").toLowerCase().contentEquals("") == false )
                        {
                            newmember.presence = member.getString("presence"); ;
                        }
                    }

                    //User Profile Class which is a subclass of User
                    UserProfileModel newmemberprofile = new UserProfileModel();
                    JSONObject profilejtoken = member.getJSONObject("profile");

                    if (profilejtoken.has("first_name") == true) newmemberprofile.first_name = profilejtoken.getString("first_name");
                    if (profilejtoken.has("last_name") == true) newmemberprofile.last_name = profilejtoken.getString("last_name");
                    if (profilejtoken.has("real_name") == true) newmemberprofile.real_name = profilejtoken.getString("real_name");
                    if (profilejtoken.has("title") == true) newmemberprofile.title = profilejtoken.getString("title");
                    if (profilejtoken.has("email") == true) newmemberprofile.email = profilejtoken.getString("email");
                    if (profilejtoken.has("skype") == true) newmemberprofile.skype = profilejtoken.getString("skype");
                    if (profilejtoken.has("phone") == true) newmemberprofile.phone = profilejtoken.getString("phone");
                    if (profilejtoken.has("image_24") == true) newmemberprofile.image_24 = profilejtoken.getString("image_24");
                    if (profilejtoken.has("image_32") == true) newmemberprofile.image_32 = profilejtoken.getString("image_32");
                    if (profilejtoken.has("image_48") == true) newmemberprofile.image_48 = profilejtoken.getString("image_48");
                    if (profilejtoken.has("image_72") == true) newmemberprofile.image_72 = profilejtoken.getString("image_72");
                    if (profilejtoken.has("image_192") == true) newmemberprofile.image_192 = profilejtoken.getString("image_192");

                    newmember.profile = newmemberprofile;

                    // Adding team member to full member list
                    teamfulllist.add(newmember);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            BindListViewtoAdapter();
        } else {
            Log.e("Slack Handler:", "Couldn't get any data from the url");
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
