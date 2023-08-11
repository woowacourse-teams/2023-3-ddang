create table black_list_token (
                                  id bigint not null auto_increment,
                                  token_type varchar(7) not null,
                                  token varchar(200) not null,
                                  primary key (id)
);

alter table black_list_token add constraint uq_token unique(token);
