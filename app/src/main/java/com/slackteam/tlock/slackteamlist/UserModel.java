package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.view.View;
import java.io.Serializable;

public class UserModel implements Serializable {

    private SlackConstants slackconstants = new SlackConstants();

    public String id;

    /// Slack bot is a special user account on the Slack Network
    public boolean IsSlackBot()
    {
        return id.contentEquals(("USLACKBOT").toUpperCase());
    }

    public String name ;
    public boolean deleted ;
    public String color ;
    public UserProfileModel profile ;
    public boolean is_admin ;
    public boolean is_owner ;
    public boolean has_files ;
    public boolean has_2fa ;
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
        if(profile.title.length() > 0 && !profile.title.toLowerCase().contentEquals("null"))
        {
            return profile.title;
        }
        else
        {
            if (!IsSlackBot())
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
            if (!IsSlackBot())
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
        if (!deleted)
        {
            if (presence.contentEquals("away"))
            {
                return slackconstants.getColor("away");
            }
            else if (is_admin)
            {
                return slackconstants.getColor("admin");
            }
            else if (is_owner)
            {
                return slackconstants.getColor("owner");
            }
            else if (IsSlackBot())
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
        if (!deleted)
        {
            if (is_admin)
            {
                return slackconstants.getColor("admin");
            }
            else if (is_owner)
            {
                return slackconstants.getColor("owner");
            }
            else if (IsSlackBot())
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
