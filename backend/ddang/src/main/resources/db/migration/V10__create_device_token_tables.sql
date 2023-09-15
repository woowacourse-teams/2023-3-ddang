create table device_token (
                              id bigint not null auto_increment,
                              device_token varchar(255) not null,
                              user_id bigint,
                              primary key (id)
);

alter table device_token add constraint fk_device_token_user foreign key (user_id) references users (id);
