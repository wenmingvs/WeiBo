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

package com.wenming.weiswift.app.common.entity.list;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.wenming.weiswift.app.common.entity.User;
import com.wenming.weiswift.app.common.FillContentHelper;

import java.util.ArrayList;

/**
 * 微博列表结构。
 *
 * @author SINA
 * @see <a href="http://t.cn/zjM1a2W">常见返回对象数据结构</a>
 * @since 2013-11-22
 */
public class UserList {

    public ArrayList<User> users = new ArrayList<User>();
    public boolean hasvisible;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;
    public int display_total_number;


    public static UserList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        UserList userList = new Gson().fromJson(jsonString, UserList.class);

        //对status中的本地私有字段进行赋值
        for (User user : userList.users) {
            //提取微博来源的关键字
            if (user.status != null) {
                //服务器并没有返回我们单张图片的随机尺寸，这里我们手动需要随机赋值
                FillContentHelper.setSingleImgSizeType(user.status);
                //提取微博来源的关键字
                FillContentHelper.setSource(user.status);
                //设置三种类型图片的url地址
                FillContentHelper.setImgUrl(user.status);

                //user的status字段中，不再包含有retweet_status字段了，所以不再进行处理
            }
        }
        return userList;
    }

}
