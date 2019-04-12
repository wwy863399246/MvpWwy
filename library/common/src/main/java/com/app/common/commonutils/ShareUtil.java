package com.app.common.commonutils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.common.baseapp.BaseApplication;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class ShareUtil
{
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "mini_data";

    /** 应用  机型相关 **/
    public static final String VERSION_NAME = "versionName";//app版本号
    public static final String VERSION_CODE = "versionCode";//版本号
    public static final String PHONE_MANUFACTURER = "phone_manufacturer";//制造商_型号
    public static final String VERSIONSYS = "versionsys";//系统版本
    public static final String DEVICEID = "deviceId";//设备号
    public static final String LANGUAGE_COUNTRY = "language_country";//语言国家

    /** 用户相关 **/
    public static final String TOKEN_LOGIN = "token_login";//token
    public static final String TIME_LOGIN = "time_login";//登录时间
    public static final String UIN_LOGIN = "Uin_login";//登录uin
    public static final String PKG_INFOS = "pkg_infos";//账号渠道包名
    public static final String PKG_AVILIBLE = "pkg_avilible";//安装游戏包名
    public static final String MAP_LIST_STR = "map_list_str";//下载地图名称
    public static final String IS_INSPECTOR = "is_Inspector";//是否是巡查员

    public static final String UC_HEAD = "uc_head";//头像
    public static final String UC_NICKNAME = "uc_nickname";//昵称
    public static final String SHIELD_FANS = "shield_fans";//是否屏蔽粉丝（1-是；0-否）
    public static final String SELF_GUILD = "self_guild";//我的社团名称
    public static final String GUILD_HEAD_URL = "guild_head_url";//我的社团头像

    public static final String WEB_URL_IM = "web_url_im";//IM登陆地址

    public static final String Day_DrawOut_Bean_From = "day_drawout_bean_from";//迷你豆提现开始日期
    public static final String Day_DrawOut_Bean_To = "day_drawout_bean_to";//迷你豆提现截至日期
    public static final String Day_DrawOut_Contribution_From = "day_drawout_contribution_from";//贡献点提现开始日期
    public static final String Day_DrawOut_Contribution_To = "day_drawout_contribution_to";//贡献点提现截至日期
    public static final String Min_DrawOut_Contribution = "min_drawout_contribution";//最小贡献点提现金额
    public static final String Min_DrawOut_Bean = "min_drawout_bean";//最小迷你豆提现数量


    private static SharedPreferences sharedPref;


    private static class LazyHolder
    {
        private static final ShareUtil INSTANCE = new ShareUtil();
    }

    private ShareUtil()
    {
        sharedPref = BaseApplication.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static final ShareUtil get()
    {
        return LazyHolder.INSTANCE;
    }

    public void clear()
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public void saveVersionName(String versionName)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(VERSION_NAME, versionName);
        editor.commit();
    }

    public void saveIsInspector(int isInspector)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(IS_INSPECTOR, isInspector == 1);
        editor.commit();
    }

    public boolean getIsInspector()
    {
        return sharedPref.getBoolean(IS_INSPECTOR, false);
    }

    public String getVersionName()
    {
        return sharedPref.getString(VERSION_NAME, "2.3.1");//TODO
    }

    public void saveVersionCode(int versionCode)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(VERSION_CODE, versionCode);
        editor.commit();
    }

    public int getVersionCode()
    {
        return sharedPref.getInt(VERSION_CODE, 1);
    }

    public void savePhoneFacturer(String phone_manufacturer)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PHONE_MANUFACTURER, phone_manufacturer);
        editor.commit();
    }

    public String getPhoneFacturer()
    {
        return sharedPref.getString(PHONE_MANUFACTURER, "");
    }

    public void saveVersionsys(String versionsys)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(VERSIONSYS, versionsys);
        editor.commit();
    }

    public String getVersionsys()
    {
        return sharedPref.getString(VERSIONSYS, "");
    }

    public void saveDeviceId(String deviceId)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DEVICEID, deviceId);
        editor.commit();
    }

    public String getDeviceId()
    {
        return sharedPref.getString(DEVICEID, "123456789");
    }

    public void saveLanguageCountry(String language_country)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LANGUAGE_COUNTRY, language_country);
        editor.commit();
    }

    public String getLanguageCountry()
    {
        return sharedPref.getString(LANGUAGE_COUNTRY, "zh_cn");
    }

    public void saveTokenLogin(String token_login)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TOKEN_LOGIN, token_login);
        editor.commit();
    }

    public String getTokenLogin()
    {
        return sharedPref.getString(TOKEN_LOGIN, "");
    }

    public void saveTimeLogin(String time_login)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TIME_LOGIN, time_login);
        editor.commit();
    }

    public String getTimeLogin()
    {
        return sharedPref.getString(TIME_LOGIN, "");
    }


    public void saveUinLogin(String uin_login)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(UIN_LOGIN, uin_login);
        editor.commit();
    }

    public String getUinLogin()
    {
        return sharedPref.getString(UIN_LOGIN, "");
    }

    public void savePkgInfos(String pkg_infos)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PKG_INFOS, pkg_infos);
        editor.commit();
    }

    public String getPkgInfos()
    {
        return sharedPref.getString(PKG_INFOS, "");
    }


    public void saveUcHead(String uc_head)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(UC_HEAD, uc_head);
        editor.commit();
    }


    public String getUcHead()
    {
        return sharedPref.getString(UC_HEAD, "");
    }


    public void saveUcNickname(String uc_nickname)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(UC_NICKNAME, uc_nickname);
        editor.commit();
    }


    public String getUcNickname()
    {
        return sharedPref.getString(UC_NICKNAME, "");
    }


    public void saveShieldFans(int shield_fans)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SHIELD_FANS, shield_fans);
        editor.commit();
    }


    public int getShieldFans()
    {
        return sharedPref.getInt(SHIELD_FANS, 0);
    }

    public void savePkgAvilible(String pkg_avilible)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PKG_AVILIBLE, pkg_avilible);
        editor.commit();
    }


    public String getPkgAvilible()
    {
        return sharedPref.getString(PKG_AVILIBLE, "");
    }


    public void saveMapListStr(String map_list_str)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MAP_LIST_STR, map_list_str);
        editor.commit();
    }


    public String getMapListStr()
    {
        return sharedPref.getString(MAP_LIST_STR, "");
    }


    public void saveSelfGuild(String self_guild)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SELF_GUILD, self_guild);
        editor.commit();
    }

    public String getSelfGuild()
    {
        return sharedPref.getString(SELF_GUILD, "");
    }

    public void saveGuildHeadUrl(String guild_head_url)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(GUILD_HEAD_URL, guild_head_url);
        editor.commit();
    }

    public String getGuildHeadUrl()
    {
        return sharedPref.getString(GUILD_HEAD_URL, "");
    }


    public void saveWebUrl(String webUrl)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(WEB_URL_IM, webUrl);
        editor.commit();
    }

    public String getWebUrl()
    {
        return sharedPref.getString(WEB_URL_IM, "");
    }

    public void saveBeanFrom(int from)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Day_DrawOut_Bean_From, from);
        editor.commit();
    }

    public int getBeanFrom()
    {
        return sharedPref.getInt(Day_DrawOut_Bean_From, 1);
    }

    public void saveBeanTo(int to)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Day_DrawOut_Bean_To, to);
        editor.commit();
    }

    public int getBeanTo()
    {
        return sharedPref.getInt(Day_DrawOut_Bean_To, 30);
    }

    public void saveContributionFrom(int from)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Day_DrawOut_Contribution_From, from);
        editor.commit();
    }

    public int getContributionFrom()
    {
        return sharedPref.getInt(Day_DrawOut_Contribution_From, 15);
    }

    public void saveContributionTo(int to)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Day_DrawOut_Contribution_To, to);
        editor.commit();
    }

    public int getContributionTo()
    {
        return sharedPref.getInt(Day_DrawOut_Contribution_To, 20);
    }

    public void saveMinContribution(int coin)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Min_DrawOut_Contribution, coin);
        editor.commit();
    }


    public int getMinContribution()
    {
        return sharedPref.getInt(Min_DrawOut_Contribution, 100);
    }

    public void saveMinBean(int bean)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Min_DrawOut_Bean, bean);
        editor.commit();
    }


    public int getMinBean()
    {
        return sharedPref.getInt(Min_DrawOut_Bean, 50);
    }
}
