alter table users add oauth_id varchar(50);
alter table users add created_time datetime(6) not null;
alter table users add last_modified_time datetime(6) not null;

alter table users add constraint uq_oauth_id unique(oauth_id);
alter table users add constraint uq_name unique(name);
