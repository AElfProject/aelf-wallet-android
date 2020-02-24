package com.github.ont.connector.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class CyanoBaseFragment extends Fragment {

    public CyanoBaseActivity baseActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (CyanoBaseActivity) getActivity();
    }
}
