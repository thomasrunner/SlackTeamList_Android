package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import java.io.Serializable;

// Sub class of User Model
public class UserProfileModel implements Serializable {

    public String first_name;
    public String last_name;
    public String real_name;
    public String title;
    public String email;
    public String skype;
    public String phone;
    public String image_24;
    public String image_32;
    public String image_48;
    public String image_72;
    public String image_192;

    /// Since some values can be either "" or null it is safe to initial here to prevent null return values;
    public UserProfileModel()
    {
        this.first_name = "";
        this.last_name = "";
        this.real_name = "";
        this.title = "";
        this.email = "";
        this.skype = "";
        this.phone = "";
        this.image_24 = "";
        this.image_32 = "";
        this.image_48 = "";
        this.image_72 = "";
        this.image_192 = "";
    }
}
