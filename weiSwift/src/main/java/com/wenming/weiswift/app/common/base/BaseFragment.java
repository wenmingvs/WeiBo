package com.wenming.weiswift.app.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by wenmingvs on 2017/4/3.
 */

public class BaseFragment extends Fragment {
    protected Context mContext;
    private ProgressDialog mProgressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * 如果Fragment已经跟Activity detach的话，getView就会返回null
     *
     * @param paramInt view resource id
     * @return View or null if the fragment is detached.
     */
    protected View findViewById(int paramInt) {
        if (getView() != null) {
            return getView().findViewById(paramInt);
        }
        return null;
    }

    protected void showLoadingDialog(int resId, boolean cancelable) {
        mProgressDialog = ProgressDialog.show(mContext, "", mContext.getString(resId), true);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(cancelable);
        mProgressDialog.show();
    }
    
    protected void dissLoadingDialog() {
        if (mProgressDialog != null && !getActivity().isFinishing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
