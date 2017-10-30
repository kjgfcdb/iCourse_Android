package buaa.icourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UploadFragment extends Fragment {

    public UploadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        return view;
    }

    public static UploadFragment newInstance(String message) {
        UploadFragment uploadFragment = new UploadFragment();
        return uploadFragment;
    }


}
