package com.suhong.khanacademybadges;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainBadgeActivity extends AppCompatActivity {

    private final String BADGE_URL = "http://www.khanacademy.org/api/v1/badges";
    private final String CATEGORY_URL = "http://www.khanacademy.org/api/v1/badges/categories";
    public final static String TAG = "LOG_TAG";

    private final static String DATA_STORE = "store_data";

    private HashMap<Integer, ArrayList<Badge>> badges;
    private HashMap<Integer, BadgeCategory> categories;

    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private RetainedFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_badge);

        FragmentManager fm = getSupportFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag(DATA_STORE);

        if (dataFragment == null) {
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, DATA_STORE).commit();
            badges = new HashMap<>();
            categories = new HashMap<>();
            getCategoryData();
        }
        else {
            badges = dataFragment.getBadgeData();
            categories = dataFragment.getCategoryData();
            setGrid();
        }

    }

    private void getCategoryData() {
        JsonArrayRequest jsonCatRequest = new JsonArrayRequest
                (Request.Method.GET, CATEGORY_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseCategories(response);
                        initializeBadgeDataset();
                        getBadgeData();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showRetryDialog(false);
                    }
                });
        RequestQueueSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsonCatRequest);
    }

    private void getBadgeData() {
        showLoadingDialog();

        JsonArrayRequest jsonBadgeRequest = new JsonArrayRequest
                (Request.Method.GET, BADGE_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseBadges(response);
                        dataFragment.setData(badges, categories);
                        setGrid();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showRetryDialog(true);
                    }
                });
        RequestQueueSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsonBadgeRequest);
    }

    private void showRetryDialog(final boolean forBadge) {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error);
        builder.setMessage(R.string.error_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (forBadge) {
                    getBadgeData();
                }
                else {
                    getCategoryData();
                }
            }
        });
        builder.create().show();
    }

    private void showLoadingDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_message));
        progressDialog.show();
    }

    private void parseCategories(JSONArray categoryResponse) {
        for (int i = 0; i < categoryResponse.length(); i++) {
            try {
                JSONObject categoryObj = categoryResponse.getJSONObject(i);
                String name = categoryObj.getString(getString(R.string.category_name));
                int id = categoryObj.getInt(getString(R.string.category_id));
                String description = categoryObj.getString(getString(R.string.category_description));
                String iconUrl = categoryObj.getString(getString(R.string.category_image));
                BadgeCategory badgeCategory = new BadgeCategory(id, name, description, iconUrl);
                categories.put(id, badgeCategory);
            } catch (JSONException e) {
                Log.e(TAG, "Bad category parse");
            }
        }
    }

    private void parseBadges(JSONArray badgeResponse) {
        for (int i = 0; i < badgeResponse.length(); i++) {
            try {
                JSONObject badgeObj = badgeResponse.getJSONObject(i);
                String name = badgeObj.getString(getString(R.string.badge_name));
                int category = badgeObj.getInt(getString(R.string.badge_category));
                String description = badgeObj.getString(getString(R.string.badge_description));
                int points = badgeObj.getInt(getString(R.string.points));
                JSONObject badgeIcon = badgeObj.getJSONObject(getString(R.string.badge_images));
                String iconCompactUrl = badgeIcon.getString(getString(R.string.badge_image_compact));
                String iconLargeUrl = badgeIcon.getString(getString(R.string.badge_image_large));
                Badge badge = new Badge(name, category, description, points, iconCompactUrl, iconLargeUrl);
                badges.get(badge.getCategory()).add(badge);
            } catch (JSONException e) {
                Log.e(TAG, "Bad badge parse");
            }
        }
    }

    private void setGrid() {
        gridLayoutManager = new GridLayoutManager(this, this.getResources().getInteger(R.integer.COLUMNS));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new MarginDecoration(this));
        final IconGridAdapter adapter = new IconGridAdapter(badges, categories, this);
        recyclerView.setAdapter(adapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void initializeBadgeDataset() {
        for(HashMap.Entry<Integer, BadgeCategory> entry: categories.entrySet()) {
            ArrayList<Badge> badgeList = new ArrayList<>();
            badges.put(entry.getKey(), badgeList);
        }
    }
}
