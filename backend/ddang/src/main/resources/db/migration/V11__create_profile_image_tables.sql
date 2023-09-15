create table profile_image (
                               id bigint not null auto_increment,
                               store_name varchar(255),
                               upload_name varchar(255),
                               primary key (id)
);

alter table users drop profile_image;
alter table users add profile_image_id bigint;
alter table users add constraint fk_user_profile_image foreign key (profile_image_id) references profile_image (id);
