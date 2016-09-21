/* 

* Copyright (C) 2014 Peter Cai

*

* This file is part of BlackLight

*

* BlackLight is free software: you can redistribute it and/or modify

* it under the terms of the GNU General Public License as published by

* the Free Software Foundation, either version 3 of the License, or

* (at your option) any later version.

*

* BlackLight is distributed in the hope that it will be useful,

* but WITHOUT ANY WARRANTY; without even the implied warranty of

* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the

* GNU General Public License for more details.

*

* You should have received a copy of the GNU General Public License

* along with BlackLight. If not, see <http://www.gnu.org/licenses/>.

*/


package com.wenming.weiswift.widget.emojitextview;


import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;


/*

This class maps emoticon strings to asset imgs

Thanks sina for those emoticons

*/

public class Emoticons {
    public static Map<String, String> emojiMap;

    static {

        emojiMap = new HashMap<String, String>();
        //默认的表情
        emojiMap.put("[爱你]", "d_aini");
        emojiMap.put("[奥特曼]", "d_aoteman");
        emojiMap.put("[拜拜]", "d_baibai");
        emojiMap.put("[悲伤]", "d_beishang");
        emojiMap.put("[鄙视]", "d_bishi");
        emojiMap.put("[闭嘴]", "d_bizui");
        emojiMap.put("[馋嘴]", "d_chanzui");
        emojiMap.put("[吃惊]", "d_chijing");
        emojiMap.put("[哈欠]", "d_dahaqi");
        emojiMap.put("[打脸]", "d_dalian");
        emojiMap.put("[顶]", "d_ding");
        emojiMap.put("[doge]", "d_doge");
        emojiMap.put("[肥皂]", "d_feizao");
        emojiMap.put("[感冒]", "d_ganmao");
        emojiMap.put("[鼓掌]", "d_guzhang");
        emojiMap.put("[哈哈]", "d_haha");
        emojiMap.put("[害羞]", "d_haixiu");
        emojiMap.put("[汗]", "d_han");
        emojiMap.put("[呵呵]", "d_hehe");
        emojiMap.put("[微笑]", "d_hehe");
        emojiMap.put("[黑线]", "d_heixian");
        emojiMap.put("[哼]", "d_heng");
        emojiMap.put("[花心]", "d_huaxin");
        emojiMap.put("[挤眼]", "d_jiyan");
        emojiMap.put("[可爱]", "d_keai");
        emojiMap.put("[可怜]", "d_kelian");
        emojiMap.put("[酷]", "d_ku");
        emojiMap.put("[困]", "d_kun");
        emojiMap.put("[懒得理你]", "d_landelini");
        emojiMap.put("[浪]", "d_lang");
        emojiMap.put("[泪]", "d_lei");
        emojiMap.put("[喵喵]", "d_miao");
        emojiMap.put("[男孩儿]", "d_nanhaier");
        emojiMap.put("[怒]", "d_nu");
        emojiMap.put("[愤怒]", "d_nu");
        emojiMap.put("[怒骂]", "d_numa");
        emojiMap.put("[女孩儿]", "d_nvhaier");
        emojiMap.put("[钱]", "d_qian");
        emojiMap.put("[亲亲]", "d_qinqin");
        emojiMap.put("[傻眼]", "d_shayan");
        emojiMap.put("[生病]", "d_shengbing");
        emojiMap.put("[神兽]", "d_shenshou");
        emojiMap.put("[草泥马]", "d_shenshou");
        emojiMap.put("[失望]", "d_shiwang");
        emojiMap.put("[衰]", "d_shuai");
        emojiMap.put("[睡觉]", "d_shuijiao");
        emojiMap.put("[睡]", "d_shuijiao");
        emojiMap.put("[思考]", "d_sikao");
        emojiMap.put("[太开心]", "d_taikaixin");
        emojiMap.put("[抱抱]", "d_taikaixin");
        emojiMap.put("[偷笑]", "d_touxiao");
        emojiMap.put("[吐]", "d_tu");
        emojiMap.put("[兔子]", "d_tuzi");
        emojiMap.put("[挖鼻]", "d_wabishi");
        emojiMap.put("[委屈]", "d_weiqu");
        emojiMap.put("[笑cry]", "d_xiaoku");
        emojiMap.put("[熊猫]", "d_xiongmao");
        emojiMap.put("[嘻嘻]", "d_xixi");
        emojiMap.put("[嘘]", "d_xu");
        emojiMap.put("[阴险]", "d_yinxian");
        emojiMap.put("[疑问]", "d_yiwen");
        emojiMap.put("[右哼哼]", "d_youhengheng");
        emojiMap.put("[晕]", "d_yun");
        emojiMap.put("[抓狂]", "d_zhuakuang");
        emojiMap.put("[猪头]", "d_zhutou");
        emojiMap.put("[最右]", "d_zuiyou");
        emojiMap.put("[左哼哼]", "d_zuohengheng");

        //浪小花表情
        emojiMap.put("[悲催]", "lxh_beicui");
        emojiMap.put("[被电]", "lxh_beidian");
        emojiMap.put("[崩溃]", "lxh_bengkui");
        emojiMap.put("[别烦我]", "lxh_biefanwo");
        emojiMap.put("[不好意思]", "lxh_buhaoyisi");
        emojiMap.put("[不想上班]", "lxh_buxiangshangban");
        emojiMap.put("[得意地笑]", "lxh_deyidexiao");
        emojiMap.put("[给劲]", "lxh_feijin");
        emojiMap.put("[好爱哦]", "lxh_haoaio");
        emojiMap.put("[好棒]", "lxh_haobang");
        emojiMap.put("[好囧]", "lxh_haojiong");
        emojiMap.put("[好喜欢]", "lxh_haoxihuan");
        emojiMap.put("[hold住]", "lxh_holdzhu");
        emojiMap.put("[杰克逊]", "lxh_jiekexun");
        emojiMap.put("[纠结]", "lxh_jiujie");
        emojiMap.put("[巨汗]", "lxh_juhan");
        emojiMap.put("[抠鼻屎]", "lxh_koubishi");
        emojiMap.put("[困死了]", "lxh_kunsile");
        emojiMap.put("[雷锋]", "lxh_leifeng");
        emojiMap.put("[泪流满面]", "lxh_leiliumanmian");
        emojiMap.put("[玫瑰]", "lxh_meigui");
        emojiMap.put("[噢耶]", "lxh_oye");
        emojiMap.put("[霹雳]", "lxh_pili");
        emojiMap.put("[瞧瞧]", "lxh_qiaoqiao");
        emojiMap.put("[丘比特]", "lxh_qiubite");
        emojiMap.put("[求关注]", "lxh_qiuguanzhu");
        emojiMap.put("[群体围观]", "lxh_quntiweiguan");
        emojiMap.put("[甩甩手]", "lxh_shuaishuaishou");
        emojiMap.put("[偷乐]", "lxh_toule");
        emojiMap.put("[推荐]", "lxh_tuijian");
        emojiMap.put("[互相膜拜]", "lxh_xianghumobai");
        emojiMap.put("[想一想]", "lxh_xiangyixiang");
        emojiMap.put("[笑哈哈]", "lxh_xiaohaha");
        emojiMap.put("[羞嗒嗒]", "lxh_xiudada");
        emojiMap.put("[许愿]", "lxh_xuyuan");
        emojiMap.put("[有鸭梨]", "lxh_youyali");
        emojiMap.put("[赞啊]", "lxh_zana");
        emojiMap.put("[震惊]", "lxh_zhenjing");
        emojiMap.put("[转发]", "lxh_zhuanfa");

        //其他
        emojiMap.put("[蛋糕]", "o_dangao");
        emojiMap.put("[飞机]", "o_feiji");
        emojiMap.put("[干杯]", "o_ganbei");
        emojiMap.put("[话筒]", "o_huatong");
        emojiMap.put("[蜡烛]", "o_lazhu");
        emojiMap.put("[礼物]", "o_liwu");
        emojiMap.put("[围观]", "o_weiguan");
        emojiMap.put("[咖啡]", "o_kafei");
        emojiMap.put("[足球]", "o_zuqiu");


        emojiMap.put("[ok]", "h_ok");
        emojiMap.put("[躁狂症]", "lxh_zaokuangzheng");
        emojiMap.put("[威武]", "weiwu");
        emojiMap.put("[赞]", "h_zan");
        emojiMap.put("[心]", "l_xin");
        emojiMap.put("[伤心]", "l_shangxin");
        emojiMap.put("[月亮]", "w_yueliang");
        emojiMap.put("[鲜花]", "w_xianhua");
        emojiMap.put("[太阳]", "w_taiyang");
        emojiMap.put("[威武]", "weiwu");
        emojiMap.put("[浮云]", "w_fuyun");
        emojiMap.put("[神马]", "shenma");
        emojiMap.put("[微风]", "w_weifeng");
        emojiMap.put("[下雨]", "w_xiayu");
        emojiMap.put("[色]", "huaxin");
        emojiMap.put("[沙尘暴]", "w_shachenbao");
        emojiMap.put("[落叶]", "w_luoye");
        emojiMap.put("[雪人]", "w_xueren");
        emojiMap.put("[good]", "h_good");
        emojiMap.put("[哆啦A梦吃惊]", "dorahaose_mobile");
        emojiMap.put("[哆啦A梦微笑]", "jqmweixiao_mobile");
        emojiMap.put("[哆啦A梦花心]", "dorahaose_mobile");
        emojiMap.put("[弱]", "ruo");
        emojiMap.put("[炸鸡啤酒]", "d_zhajipijiu");
        emojiMap.put("[囧]", "jiong");
        emojiMap.put("[NO]", "buyao");
        emojiMap.put("[来]", "guolai");
        emojiMap.put("[互粉]", "f_hufen");
        emojiMap.put("[握手]", "h_woshou");
        emojiMap.put("[haha]", "h_haha");
        emojiMap.put("[织]", "zhi");
        emojiMap.put("[萌]", "meng");
        emojiMap.put("[钟]", "o_zhong");
        emojiMap.put("[给力]", "geili");
        emojiMap.put("[喜]", "xi");
        emojiMap.put("[绿丝带]", "o_lvsidai");
        emojiMap.put("[围脖]", "weibo");
        emojiMap.put("[音乐]", "o_yinyue");
        emojiMap.put("[照相机]", "o_zhaoxiangji");
        emojiMap.put("[耶]", "h_ye");
        emojiMap.put("[拍照]", "lxhpz_paizhao");
        emojiMap.put("[白眼]", "landeln_baiyan");


        emojiMap.put("[作揖]", "o_zuoyi");
        emojiMap.put("[拳头]", "quantou_org");
        emojiMap.put("[X教授]", "xman_jiaoshou");
        emojiMap.put("[天启]", "xman_tianqi");
        emojiMap.put("[抢到啦]", "hb_qiangdao_org");
    }

    /**
     * @param emojiText like [挖鼻]
     * @return 返回图片的名字
     */
    public static String getImgName(String emojiText) {
        String ImgName = emojiMap.get(emojiText);
        if (!TextUtils.isEmpty(ImgName)) {
            return ImgName;
        } else {
            return null;
        }

    }

}
