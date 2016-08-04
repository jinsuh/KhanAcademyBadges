package com.suhong.khanacademybadges;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IconViewHolder extends RecyclerView.ViewHolder{

    public ImageView icon;
    public TextView name;
    public View loading;
    public View container;

    public IconViewHolder(View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.iconview_icon);
        name = (TextView) itemView.findViewById(R.id.iconview_name);
        loading = itemView.findViewById(R.id.iconview_loading);
        container = itemView.findViewById(R.id.iconview_container);
    }
}
