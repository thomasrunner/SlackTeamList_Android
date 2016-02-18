package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.graphics.Color;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// Slack Constant Class presently only used to store Colors
public class SlackConstants  implements Serializable {

    private Map<String, Integer> usercolorstatusdict;

    public SlackConstants()
    {
        //Setting custom color when app loads.
        if (usercolorstatusdict == null)
        {
            usercolorstatusdict = new HashMap<String, Integer>();
            usercolorstatusdict.put("all", Color.argb(255, 224, 224, 224));
            usercolorstatusdict.put("away",  Color.argb(255, 224, 224, 224));
            usercolorstatusdict.put("admin",  Color.argb(255, 255, 196, 32));
            usercolorstatusdict.put("owner",  Color.argb(255, 255, 128, 64));
            usercolorstatusdict.put("bots",  Color.argb(255, 64, 196, 255));
            usercolorstatusdict.put("deleted",  Color.argb(255, 255, 64, 64));
            usercolorstatusdict.put("active",  Color.argb(255, 64, 224, 128));
        }
    }

    public int getColor(String userstatus)
    {
        if(userstatus.contentEquals("")) return usercolorstatusdict.get("all");
        int requestcolor = usercolorstatusdict.get(userstatus);
        if (requestcolor != 0)
        {
            return requestcolor;
        }
        else
        {
            return usercolorstatusdict.get("all");
        }
    }
}
