package com.wenming.weiswift.fragment.post;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenming.weiswift.R;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class PostFragment extends Fragment {
    private View mView;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.postfragment_layout, container, false);
        return mView;
    }


}
