package com.yodinfo.seed.constant;

public abstract class RedisKeyConstant {

    public static final String ACCESS_TOKEN = "Authorization";

    public static final String JWT_ACCESS_TOKEN = "jwt-access-key_";

    /**
     * 主动去刷新 token key(适用场景 比如修改了用户的角色/权限去刷新token)
     */
    public static final String JWT_REFRESH_KEY = "jwt-refresh-key_";

    /**
     * 刷新状态(适用场景如：一个功能点要一次性请求多个接口，当第一个请求刷新接口时候 加入一个节点下一个接口请求进来的时候直接放行)
     */
    public static final String JWT_REFRESH_STATUS = "jwt-refresj-status_";

    /**
     * 标记新的access_token
     */
    public static final String JWT_REFRESH_IDENTIFICATION = "jwt-refresj-identification_";

    /**
     * access_token 主动退出后加入黑名单 key
     */
    public static final String JWT_ACCESS_TOKEN_BLACKLIST = "jwt-access-token-blacklist_";

    /**
     * refresh_token 主动退出后加入黑名单 key
     */
    public static final String JWT_REFRESH_TOKEN_BLACKLIST = "jwt-refresh-token-blacklist_";

}
