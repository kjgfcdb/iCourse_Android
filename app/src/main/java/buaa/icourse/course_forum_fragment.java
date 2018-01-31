package buaa.icourse;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class course_forum_fragment extends Fragment {
    SwipeRefreshLayout forumSwipeLayout;
    PostAdapter postAdapter;
    List<Post> posts = new ArrayList<>();

    public course_forum_fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_forum_fragment, container, false);
        postAdapter = new PostAdapter(posts);
        forumSwipeLayout = view.findViewById(R.id.forum_swipe_layout);
        RecyclerView forumPostList = view.findViewById(R.id.forum_post_list);
        //下滑刷新帖子
        forumSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });
        return view;
    }

    //刷新帖子
    void refreshPosts(){
        //下滑刷新页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        postAdapter.notifyDataSetChanged();
                        forumSwipeLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
