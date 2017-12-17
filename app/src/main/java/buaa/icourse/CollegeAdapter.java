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


public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.ViewHolder> {
    private Context mContext;
    private List<CollegeItem> mCollegeList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView collegePic;
        TextView collegeName;

        ViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view;
            collegePic = view.findViewById(R.id.college_pic);
            collegeName = view.findViewById(R.id.college_name);
        }
    }

    CollegeAdapter(List<CollegeItem> collegeList) {
        mCollegeList = collegeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.college_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                CollegeItem item = mCollegeList.get(position);
                Intent intent = new Intent(mContext, CollegeDetail.class);
                intent.putExtra(CollegeDetail.COLLEGE_ID,item.getCollegeId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CollegeItem collegeItem = mCollegeList.get(position);
        holder.collegeName.setText(collegeItem.getCollegeName());
        try {
            holder.collegePic.setImageResource((int) MainActivity.collegePics.get(collegeItem.getCollegeId()));
        } catch (Exception e) {
            //默认显示6系
            holder.collegePic.setImageResource((int) MainActivity.collegePics.get(6));
        }
    }

    @Override
    public int getItemCount() {
        return mCollegeList.size();
    }
}
