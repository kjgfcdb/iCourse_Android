package buaa.icourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryFragment extends Fragment {
    private String message;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initArgs(view);
        return view;
    }

    private void initArgs(View view) {
        Bundle bundle = getArguments();
        TextView mTextView = view.findViewById(R.id.category_fragment);
        mTextView.setText(bundle == null ? "Category" : bundle.getString("message"));
    }

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


}
