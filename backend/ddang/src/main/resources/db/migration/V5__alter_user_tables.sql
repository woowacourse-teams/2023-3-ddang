alter table users add oauth_id varchar(50);
alter table users add created_time datetime(6);
alter table users add last_modified_time datetime(6);

update users set created_time = now() where created_time is null;
update users set last_modified_time = now() where last_modified_time is null;

alter table users modify created_time datetime(6) not null;
alter table users modify last_modified_time datetime(6) not null;

alter table users add constraint uq_oauth_id unique(oauth_id);
alter table users add constraint uq_name unique(name);
