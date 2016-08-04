package com.suhong.khanacademybadges;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public TextView categoryTitle;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
    }


}
