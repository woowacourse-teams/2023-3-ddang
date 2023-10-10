create table reliability_update_history
(
    id                     bigint not null auto_increment,
    created_time           datetime(6) not null,
    last_applied_review_id bigint,
    primary key (id)
);

create table user_reliability
(
    id                   bigint not null auto_increment,
    applied_review_count integer not null,
    reliability          float(53),
    user_id              bigint  not null,
    primary key (id)
);

alter table user_reliability add constraint fk_user_reliability_user foreign key (user_id) references users (id);
