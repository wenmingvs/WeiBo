package com.wenming.weiswift;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by wenmingvs on 2016/1/4.
 */
public class SimulationString {
    private String mData;
    private Context mContext;

    public SimulationString(Context context) {
        mContext = context;
    }

    public String getData() {
        readFromRaw();
        return mData;
    }

    private void readFromRaw() {
        try {
            InputStream is = mContext.getResources().openRawResource(R.raw.json);
            mData = readTextFromSDcard(is);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        reader.close();
        bufferedReader.close();
        return buffer.toString();
    }


}
