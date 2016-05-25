package com.uniquedu.cemetery;

/**
 * Created by baoyi on 2016/3/7.
 */
public class Address {
    public static final String URL = "http://www.whjfs.com/";
    //    public static final String URL = "http://219.140.77.9/";
    public static final String NEWS_INFOMATION_LIST = URL + "app-news/news-list.html";
    public static final String NEWS_SEARCH = URL + "app-news/news-search.html?keyword=";
    //网上灵堂分页带查询接口
    public static final String PersonalMemorial = URL + "mvcwebmis/nologin/FindWithPagerForPersonalMemorial";
    //图片前缀
    public static final String IMAGEADDRESS = URL + "mvcwebmis";
    public static final String GET_DIC = URL + "/mvcwebmis/nologin/GetDicForNoLogin";
    //歌曲播放地址
    public static final String MUSIC = URL + "mvcwebmis/nologin/ChangeMp3State";
    //灵堂初始化
    public static final String INFOMATION = URL + "mvcwebmis/nologin/InitPersonalMemorial";
    //获取祭扫日志
    public static final String WORSHIP_DAILY = URL + "mvcwebmis/nologin/FindWithPagerForPersonalMemorialNotePad";
    //历史照片
    public static final String WORSHIP_PHOTO = URL + "mvcwebmis/nologin/FindWithPagerForPersonalMemorialPhoto";
    //纪念文选
    public static final String WONSHIP_ANTHOLOGY = URL + "mvcwebmis/nologin/FindWithPagerForPMArticle";
    //获取文选详细信息
    public static final String WONSHIP_ANTHOLOGY_INFO = URL + "mvcwebmis/nologin/GetMemorialArticleById";
    //展示文选界面
    public static final String WONSHIP_ANTHOLOGY_WEB = URL + "app-news/app-memorial-article.html";

    //执行祭扫动作
    public static final String WONSHIP_THEME = URL + "mvcwebmis/nologin/MemorialAction?callback=callbakename&id=";
    //获得验证码
    public static final String GET_CODES = "http://www.whjfs.com/mvcwebmis/login/checkcode";
    //登录
    public static final String LOGIN = URL + "mvcwebmis/nologin/AppCheckUserByPwd";
}
