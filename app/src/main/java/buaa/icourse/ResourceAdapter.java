package buaa.icourse;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static buaa.icourse.CourseAdapter.TAG;


public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {
    private Context mContext;
    private List<ResourceItem> mResourceList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        //资源图片以及资源名
        ImageView resourceImage;
        TextView resourceName;

        ViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view;
            resourceImage = view.findViewById(R.id.resource_image);
            resourceName = view.findViewById(R.id.resource_item_name);
        }
    }

    ResourceAdapter(List<ResourceItem> resourceItemList) {
        mResourceList = resourceItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.resource_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ResourceItem item = mResourceList.get(position);
                Intent intent = new Intent(mContext, ResourceDetail.class);
                intent.putExtra(ResourceDetail.RESOURCE_NAME, item.getResourceName());
                intent.putExtra(ResourceDetail.RESOURCE_TYPE, item.getResourceType());
                intent.putExtra(ResourceDetail.RESOURCE_INFO, item.getResourceInfo());
                intent.putExtra(ResourceDetail.RESOURCE_UPLOADER, item.getResourceUploaderName());
                intent.putExtra(ResourceDetail.RESOURCE_DOWNLOAD_COUNT, item.getResourceDownloadCount());
                intent.putExtra(ResourceDetail.RESOURCE_URL,item.getResourceUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ResourceItem resourceItem = mResourceList.get(position);
        holder.resourceName.setText(resourceItem.getResourceName());
        try {
            Log.d(TAG, "onBindViewHolder: "+resourceItem.getResourceType());
            holder.resourceImage.setImageResource((int) MainActivity.pictures.get(resourceItem.getResourceType()));
        } catch (Exception e) {
            holder.resourceImage.setImageResource((int) MainActivity.pictures.get("file"));
        }
    }

    @Override
    public int getItemCount() {
        return mResourceList.size();
    }
}
