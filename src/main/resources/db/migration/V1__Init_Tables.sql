-- auto-generated definition
create table blog
(
    id         bigint auto_increment
        primary key,
    content    mediumtext   null,
    created_at datetime(6)  null,
    title      varchar(255) not null
);
-- auto-generated definition
create table comment
(
    id         bigint auto_increment
        primary key,
    blog_id    bigint                             not null,
    nickname   varchar(50)                        not null,
    content    text                               not null,
    created_at datetime default CURRENT_TIMESTAMP null,
    constraint comment_ibfk_1
        foreign key (blog_id) references blog (id)
            on delete cascade
);

create index blog_id
    on comment (blog_id);

-- auto-generated definition
create table artwork
(
    id          bigint auto_increment
        primary key,
    title       varchar(50)                        not null,
    description text                               null,
    file_path   varchar(255)                       not null,
    file_type   varchar(50)                        not null,
    category    varchar(25)                        not null,
    created_at  datetime default CURRENT_TIMESTAMP null,
    updated_at  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '当更新（UPDATE）这条记录时（无论修改了哪个字段的值），只要这一行数据发生了变化，数据库就会自动把 updated_at 的值修改为修改发生时的当前时间'
);


