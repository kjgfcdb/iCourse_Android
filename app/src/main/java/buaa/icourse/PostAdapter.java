package buaa.icourse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chen Dengbo on 2018/1/27.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mContext;
    private List<Post> posts;

    PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.postitem, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:点击进入帖子详情
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.postName.setText(post.postName);
        holder.userName.setText(post.userName);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView userName;
        TextView postName;

        public ViewHolder(View itemView) {
            super(itemView);
            //布局
            layout = itemView.findViewById(R.id.postLayout);
            //发表此帖子的用户名
            userName = itemView.findViewById(R.id.post_user_name);
            //帖子名
            postName = itemView.findViewById(R.id.post_post_name);
        }
    }

}
