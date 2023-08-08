alter table users
    add oauth_id varchat(50);

alter table users
    add constraint uq_oauth_id unique (oauth_id);
alter table users
    add constraint uq_name unique (name);
