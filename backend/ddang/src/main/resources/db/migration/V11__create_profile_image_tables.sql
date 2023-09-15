create table profile_image (
       id bigint not null auto_increment,
       store_name varchar(255),
       upload_name varchar(255),
       primary key (id)
);

alter table profile_image drop users;
alter table users add constraint fk_user_profile_image foreign key (profile_image_id) references profile_image (id);
