package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    UserModel member;

    ImageView UserProfilePhotoImageView;
    TextView UserProfileNameBelowPhotoTextView;
    TextView UserProfileRoleTextView;
    ImageView ProfileUserPresenseImageView;

    Button MessageButton;
    Button EmailButton;
    Button SkypeButton;
    Button CallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        UserProfilePhotoImageView = (ImageView) findViewById(R.id.UserProfilePhotoImageView);
        UserProfileNameBelowPhotoTextView = (TextView) findViewById(R.id.UserProfileNameBelowPhotoTextView);
        UserProfileRoleTextView  = (TextView) findViewById(R.id.UserProfileRoleTextView);
        ProfileUserPresenseImageView = (ImageView) findViewById(R.id.ProfileUserPresenseImageView);

        MessageButton = (Button) findViewById(R.id.MessageButton);
        EmailButton = (Button) findViewById(R.id.EmailButton);
        SkypeButton = (Button) findViewById(R.id.SkypeButton);
        CallButton = (Button) findViewById(R.id.CallButton);

        Intent intent = getIntent();
        member = (UserModel) intent.getExtras().getSerializable("member");

        ImageDownloaderTask downloadtask = new  ImageDownloaderTask(UserProfilePhotoImageView,this);
        downloadtask.execute(member.profile.image_192);

        //new ImageDownloaderTask(UserProfilePhotoImageView).execute(member.profile.image_192);

        this.setTitle(member.NamewithAtSymbol());
        UserProfileNameBelowPhotoTextView.setText(member.RealNamewithNameFailover());
        UserProfileRoleTextView.setText(member.TitlewithNameFailover());
        ProfileUserPresenseImageView.setBackgroundColor(member.UserPresense());

        //Message Button
        MessageButton.setText("Message " + member.NamewithAtSymbol());

        //Email Button
        if(member.profile.email.length() > 0 && member.profile.email.toLowerCase().contentEquals("null") == false)
        {
            EmailButton.setVisibility(View.VISIBLE);
            EmailButton.setText(member.profile.email);
        }


        //Skype Button
        if (member.profile.skype.length() > 0)
        {
            SkypeButton.setVisibility(View.VISIBLE);
            SkypeButton.setText(member.profile.skype);
        }


        //Phone Button
        if (member.profile.phone.length() > 0)
        {
            CallButton.setVisibility(View.VISIBLE);
            CallButton.setText(member.profile.phone);
        }

        if(!Utils.isNetworkAvailable(ProfileActivity.this))
        {
            Toast.makeText(getApplicationContext(), "You are offline or airplane mode.", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    // Launch Caller
    public void Call_onClick(View view)
    {
        String telephonenumberformatted = member.profile.phone.replace(" ", "");
        telephonenumberformatted = telephonenumberformatted.replace("(", "");
        telephonenumberformatted = telephonenumberformatted.replace(")", "");
        telephonenumberformatted = telephonenumberformatted.replace("-", "");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telephonenumberformatted));
    }

    // Launch Email
    public void EmailButton_onClick(View view)
    {
        Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
        emailintent.setType("plain/text");
        emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {member.profile.email });
        emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
        startActivity(Intent.createChooser(emailintent, "Send mail..."));
    }
}
