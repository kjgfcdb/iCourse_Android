package buaa.icourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    private String message;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initArgs(view);
        return view;
    }

    private void initArgs(View view) {
        TextView mTextView = view.findViewById(R.id.home_fragment);
        mTextView.setText(message);
    }

    public static HomeFragment newInstance(String message) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.message = message;
        return homeFragment;
    }


}
