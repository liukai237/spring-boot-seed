# 微信小程序用户解密信息
create table t_mp_userinfo
(
    openid      varchar(50)  not null,
    nickname    varchar(50)  null,
    gender      int          null,
    city        varchar(50)  null,
    province    varchar(50)  null,
    country     varchar(50)  null,
    avatarurl   varchar(255) null,
    unionid     varchar(50)  null,
    groupid     int          null,
    create_time timestamp    null,
    update_time timestamp    null,
    source      int          null,
    constraint t_mp_userinfo_openid_uindex
        unique (openid)
) default charset = utf8mb4;

alter table t_mp_userinfo
    add primary key (openid);

