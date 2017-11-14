package buaa.icourse;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Chen Dengbo on 2017/11/14.
 */

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder>{
    private Context mContext;
    private List<ResourceItem> mResourceList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView resourceImage;
        TextView resourceName;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView)view;
            resourceImage = (ImageView)view.findViewById(R.id.resource_image);
            resourceName = (TextView)view.findViewById(R.id.resource_name);
        }
    }
    public ResourceAdapter(List<ResourceItem> resourceItemList) {
        mResourceList = resourceItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.resource_item,
                parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ResourceItem item = mResourceList.get(position);
                Intent intent = new Intent(mContext,ResourceDetail.class);
                intent.putExtra(ResourceDetail.RESOURCE_NAME,item.getResourceName());
                intent.putExtra(ResourceDetail.RESOURCE_ID,item.getResourceId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        ResourceItem resourceItem = mResourceList.get(position);
        holder.resourceName.setText(resourceItem.getResourceName());
        Glide.with(mContext).load(R.drawable.main_icon).into(holder.resourceImage);
    }

    @Override
    public int getItemCount() {
        return mResourceList.size();
    }
}
