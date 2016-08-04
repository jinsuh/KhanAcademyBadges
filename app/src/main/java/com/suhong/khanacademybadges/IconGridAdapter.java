package com.suhong.khanacademybadges;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class IconGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private HashMap<Integer, BadgeCategory> categoryDataset;
    private Object[] dataset;
    private ImageLoader imageLoader;
    private Context context;

    public IconGridAdapter(HashMap<Integer, ArrayList<Badge>> badgeDataset, HashMap<Integer, BadgeCategory> categoryDataset, Context context) {
        organizeData(badgeDataset, categoryDataset);
        this.categoryDataset = categoryDataset;
        imageLoader = RequestQueueSingleton.getInstance(context).getImageLoader();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)  {
            case TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_header, parent, false);
                CategoryViewHolder headerViewHolder = new CategoryViewHolder(headerView);
                return headerViewHolder;
            case TYPE_ITEM:
                View iconView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.icon_view, parent, false);
                IconViewHolder iconViewHolder = new IconViewHolder(iconView);
                return iconViewHolder;
            default:
                Log.e(MainBadgeActivity.TAG, "Bad viewType passed");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object properItem = dataset[position];
        if (properItem instanceof Badge) {
            final Badge currBadge = (Badge) properItem;
            final IconViewHolder iconViewHolder = (IconViewHolder) holder;
            final ImageView badgeIcon = iconViewHolder.icon;
            badgeIcon.setImageDrawable(null);
            final View loadingIcon = iconViewHolder.loading;
            final View container = iconViewHolder.container;
            imageLoader.get(currBadge.getIconLargeUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response != null) {
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null) {
                            badgeIcon.setImageBitmap(bitmap);
                            loadingIcon.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(MainBadgeActivity.TAG, "Error getting image from url");
                    badgeIcon.setImageResource(R.mipmap.ic_launcher);
                    loadingIcon.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                }
            });

            iconViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(currBadge.getName());
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_badge, null);
                    setupDialogView(dialogView, currBadge);
                    builder.setView(dialogView);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
            iconViewHolder.name.setText(currBadge.getName());
        }
        else if (properItem instanceof BadgeCategory) {
            BadgeCategory category = (BadgeCategory) properItem;
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.categoryTitle.setText(category.getName());
        }

    }

    private void setupDialogView(final View dialogView, Badge currBadge) {
        final ImageView largeBadgeIcon = (ImageView) dialogView.findViewById(R.id.dialog_icon);
        imageLoader.get(currBadge.getIconLargeUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response != null) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        largeBadgeIcon.setImageBitmap(bitmap);
                        View loadingBar = dialogView.findViewById(R.id.dialog_loading);
                        loadingBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MainBadgeActivity.TAG, "Error getting image from url");
                largeBadgeIcon.setImageResource(R.mipmap.ic_launcher);
                View loadingBar = dialogView.findViewById(R.id.dialog_loading);
                loadingBar.setVisibility(View.GONE);
            }
        });

        TextView description = (TextView) dialogView.findViewById(R.id.dialog_description);
        description.setText(currBadge.getDescription());

        TextView categoryPoints = (TextView) dialogView.findViewById(R.id.dialog_categorypoints);
        String catPointString = categoryDataset.get(currBadge.getCategory()).getName();
        if (currBadge.getPoints() > 0) {
            catPointString += " (" + currBadge.getPoints() + " "
                    + dialogView.getContext().getResources().getString(R.string.points)
                    + ")";
        }
        categoryPoints.setText(catPointString);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position))
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        Object properItem = dataset[position];
        if (properItem instanceof BadgeCategory) {
            return true;
        }
        else {
            return false;
        }
    }

    private void organizeData(HashMap<Integer, ArrayList<Badge>> badgeDataset, HashMap<Integer, BadgeCategory> categoryDataset) {
        int dataCount = 0;
        for(HashMap.Entry<Integer, ArrayList<Badge>> entry: badgeDataset.entrySet()) {
            dataCount += 1 + entry.getValue().size();
        }

        dataset = new Object[dataCount];
        int count = 0;
        for(HashMap.Entry<Integer, BadgeCategory> entry: categoryDataset.entrySet()) {
            dataset[count++] = entry.getValue();
            ArrayList<Badge> badgesInCategory = badgeDataset.get(entry.getKey());
            for (int i = 0; i < badgesInCategory.size(); i++) {
                dataset[count++] = badgesInCategory.get(i);
            }
        }
    }
}
