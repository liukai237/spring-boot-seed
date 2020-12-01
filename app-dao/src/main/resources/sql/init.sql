create schema test default character set utf8mb4 collate utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
drop table if exists `t_user`;
create table t_user
(
  user_id     bigint       not null,
  username    varchar(10)  null,
  gender      tinyint      null,
  birthday    date         null,
  passwd_hash varchar(200) null,
  tel         varchar(20)  null,
  avatar      varchar(100) null,
  province    smallint(6)  null,
  nickname    varchar(20)   null,
  email       varchar(20)  null,
  address     varchar(100) null,
  create_time datetime     null,
  update_time datetime     null,
  constraint t_user_id_uindex
    unique (user_id)
)
  comment '用户表';

alter table test.t_user
  add primary key (user_id);

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
drop table if exists `t_role`;
