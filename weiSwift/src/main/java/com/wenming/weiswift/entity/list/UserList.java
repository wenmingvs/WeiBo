/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wenming.weiswift.entity.list;

import android.text.TextUtils;

import com.wenming.weiswift.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 微博列表结构。
 *
 * @author SINA
 * @see <a href="http://t.cn/zjM1a2W">常见返回对象数据结构</a>
 * @since 2013-11-22
 */
public class UserList {

    /**
     * 微博列表
     */
    public ArrayList<User> usersList = new ArrayList<User>();
    public boolean hasvisible;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;
    public int display_total_number;

    public static UserList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        UserList users = new UserList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            users.hasvisible = jsonObject.optBoolean("hasvisible", false);
            users.previous_cursor = jsonObject.optString("previous_cursor", "0");
            users.next_cursor = jsonObject.optString("next_cursor", "0");
            users.total_number = jsonObject.optInt("total_number", 0);
            users.display_total_number = jsonObject.optInt("display_total_number", 0);

            JSONArray jsonArray = jsonObject.optJSONArray("users");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                users.usersList = new ArrayList<User>(length);
                for (int ix = 0; ix < length; ix++) {
                    users.usersList.add(User.parse(jsonArray.getJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }



}
