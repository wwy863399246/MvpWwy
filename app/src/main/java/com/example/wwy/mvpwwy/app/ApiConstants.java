package com.example.wwy.mvpwwy.app;


/**
 * Created by Sunny on 2018/4/20.
 */

public class ApiConstants {

    /**
     * true为测试反之.. 正式
     */
    public static boolean isDebug = true;
    //       public static boolean isDebug = false;

    /**
     * 接口请求的Url
     */
    public static final String BASEURL = "http://gank.io/api/";
    /**
     * 接口请求的Url2 (Mob官网API)
     */
    public static final String URL_Mob = "http://apicloud.mob.com/";
    //标签
    public static final String FlagFragment = "Flag";
    public static final String FlagWelFare = "福利";
    public static final String FlagAndroid = "Android";
    public static final String FlagIOS = "iOS";
    public static final String FlagVideo = "休息视频";
    public static final String FlagJS = "前端";
    public static final String FlagExpand = "拓展资源";
    public static final String FlagRecommend = "瞎推荐";

    /**
     * 获取对应的host
     *
     * @param hostType host类型(根据HOSTTYPE中的TYPE类型定义去获取)
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.BASE_URL:
                host = BASEURL;
                break;
            case HostType.BASE_URL_2:
                host = URL_Mob;
                break;
//            case HostType.BASE_URL_3:
//                host = URL_Mob;
//                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
