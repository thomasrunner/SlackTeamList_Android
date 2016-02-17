package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserModel implements Serializable {

    private SlackConstants slackconstants = new SlackConstants();

    public String id;

    /// Slack bot is a special user account on the Slack Network
    public Boolean IsSlackBot()
    {
        return id.contentEquals(("USLACKBOT").toUpperCase());
    }

    public String name ;
    public Boolean deleted ;
    public String color ;
    public UserProfileModel profile ;
    public Boolean is_admin ;
    public Boolean is_owner ;
    public Boolean has_files ;
    public Boolean has_2fa ;
    public String presence ;


    /// Since some values can be either "" or null it is safe to initialize here to prevent null return values;
    public UserModel()
    {
        id = "";
        name = "";
        name = "";
        deleted = false;
        color = "";
        is_admin = false;
        is_owner = false;
        has_files = false;
        has_2fa = false;
        presence = "";
    }

    /// Simple cosmetic formatting function
    public String NamewithAtSymbol()
    {
            return "@" + name;
    }


    /// Simple cosmetic option to show email icon in list using Binding.
    public int IsEmailAvailable()
    {
            if (profile.email.length() > 0)
            {
                return View.VISIBLE;
            }
            else
            {
                return View.INVISIBLE;
            }
    }


    /// Simple cosmetic option to show skype icon in list using Binding.
    public int IsSkypeAvailable()
    {
            if (profile.skype.length() > 0)
            {
                return View.VISIBLE;
            }
            else
            {
                return View.INVISIBLE;
            }
    }


    /// Simple cosmetic option to show skype icon in list using Binding.
    public int IsPhoneAvailable()
    {
            if (profile.phone.length() > 0)
            {
                return View.VISIBLE;
            }
            else
            {
                return View.INVISIBLE;
            }
    }


    /// Since Title can be "" this allows for a failover
    public String TitlewithNameFailover()
    {
        if(profile.title.length() > 0 && profile.title.toLowerCase().contentEquals("null") == false)
        {
            return profile.title;
        }
        else
        {
            if (IsSlackBot() == false)
            {
                return NamewithAtSymbol();
            }
            else
            {
                return "";
            }
        }
    }


    /// Since Title can be "" this allows for a failover
    public String RealNamewithNameFailover()
    {
            if (profile.real_name.length() > 0)
            {
                return profile.real_name;
            }
            else
            {
                if (IsSlackBot() == false)
                {
                    return NamewithAtSymbol();
                }
                else
                {
                    return "";
                }
            }
    }


    /// Nice little visual indicator to show the different types of users and the user status
    public int UserPresense()
    {
        if (deleted == false)
        {
            if (presence.contentEquals("away") == true)
            {
                return slackconstants.getColor("away");
            }
            else if (is_admin == true)
            {
                return slackconstants.getColor("admin");
            }
            else if (is_owner == true)
            {
                return slackconstants.getColor("owner");
            }
            else if (IsSlackBot() == true)
            {
                return slackconstants.getColor("bots");
            }
            else
            {
                return slackconstants.getColor("active");
            }
        }
        else
        {
            return slackconstants.getColor("deleted");
        }
    }


    /// Nice little visual indicator to show the different types of users and the user status, specific for user profile
    public int UserPresenceExcludingAwayStatusColor()
    {
        if (deleted == false)
        {
            if (is_admin == true)
            {
                return slackconstants.getColor("admin");
            }
            else if (is_owner == true)
            {
                return slackconstants.getColor("owner");
            }
            else if (IsSlackBot() == true)
            {
                return slackconstants.getColor("bots");
            }
            else
            {
                return slackconstants.getColor("active");
            }
        }
        else
        {
            return slackconstants.getColor("deleted");
        }
    }

}
