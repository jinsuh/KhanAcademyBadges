package com.suhong.khanacademybadges;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class RetainedFragment extends Fragment{

    private HashMap<Integer, ArrayList<Badge>> badgeData;
    private HashMap<Integer, BadgeCategory> categoryData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(HashMap<Integer, ArrayList<Badge>> badgeData, HashMap<Integer, BadgeCategory> categoryData) {
        this.badgeData = badgeData;
        this.categoryData = categoryData;
    }

    public HashMap<Integer, ArrayList<Badge>> getBadgeData() {
        return badgeData;
    }

    public HashMap<Integer, BadgeCategory> getCategoryData() {
        return categoryData;
    }
}
