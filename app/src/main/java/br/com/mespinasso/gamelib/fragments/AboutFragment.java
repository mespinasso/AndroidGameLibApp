package br.com.mespinasso.gamelib.fragments;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import br.com.mespinasso.gamelib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private TextView tvVersion;

    public AboutFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);

        try {
            PackageInfo pInfo = view.getContext().getPackageManager().getPackageInfo(view.getContext().getPackageName(), 0);
            String version = pInfo.versionName;

            tvVersion.setText(tvVersion.getText() + ": " + version);

        } catch (PackageManager.NameNotFoundException e) {
            tvVersion.setText("");
        }

        return view;
    }
}
