package com.slackteam.tlock.slackteamlist;

/**
 * Created by Thomas Lock on 2/16/2016.
 * tlock@fhotoroom.com
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class MemberAdapter extends ArrayAdapter<UserModel>{

    MemberAdapter(Context context, List<UserModel> members){
        super(context,R.layout.member_listitem, members);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater memberinflater = LayoutInflater.from(getContext());
        View memberview = memberinflater.inflate(R.layout.member_listitem, parent, false);

        UserModel memberuser = getItem(position);

        TextView ProfileFirstNameTextBlock = (TextView) memberview.findViewById(R.id.ProfileFirstNameTextBlock);
        TextView ProfileRoleTextBlock = (TextView) memberview.findViewById(R.id.ProfileRoleTextBlock);
        ImageView ProfilePhotoImage = (ImageView) memberview.findViewById(R.id.ProfilePhotoImage);

        ImageView ProfileUserPresenseImageView = (ImageView) memberview.findViewById(R.id.ProfileUserPresenseImageView);
        ImageView CallIconImageView = (ImageView) memberview.findViewById(R.id.CallIconImageView);
        ImageView EmailIconImageView = (ImageView) memberview.findViewById(R.id.EmailIconImageView);
        ImageView SkypeIconImageView = (ImageView) memberview.findViewById(R.id.SkypeIconImageView);

        ProfileFirstNameTextBlock.setText(memberuser.RealNamewithNameFailover());
        ProfileRoleTextBlock.setText(memberuser.TitlewithNameFailover());

        new ImageDownloaderTask(ProfilePhotoImage).execute(memberuser.profile.image_72);
        ProfileUserPresenseImageView.setBackgroundColor(memberuser.UserPresense());

        if(memberuser.profile.email.length() > 0)
        {
            EmailIconImageView.setVisibility((View.VISIBLE));
        }

        if(memberuser.profile.skype.length() > 0)
        {
            SkypeIconImageView.setVisibility((View.VISIBLE));
        }

        if(memberuser.profile.phone.length() > 0)
        {
            CallIconImageView.setVisibility((View.VISIBLE));
        }

        return memberview;
    }
}
