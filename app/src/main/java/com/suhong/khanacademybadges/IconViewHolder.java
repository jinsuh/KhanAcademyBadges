package com.suhong.khanacademybadges;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class IconViewHolder extends RecyclerView.ViewHolder{

    public NetworkImageView icon;
    public TextView name;

    public IconViewHolder(View itemView) {
        super(itemView);
        icon = (NetworkImageView) itemView.findViewById(R.id.iconview_icon);
        name = (TextView) itemView.findViewById(R.id.iconview_name);
    }
}
