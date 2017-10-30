package buaa.icourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserFragment extends Fragment {
    private String message;

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initArgs(view);
        return view;
    }

    private void initArgs(View view) {
        TextView mTextView = view.findViewById(R.id.user_fragment);
        mTextView.setText(message);
    }

    public static UserFragment newInstance(String message) {
        UserFragment userFragment = new UserFragment();
        userFragment.message = message;
        return userFragment;
    }


}
