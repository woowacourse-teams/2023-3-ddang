create table review (
    id bigint not null auto_increment,
    created_time datetime(6) not null,
    content varchar(255),
    score float,
    auction_id bigint not null,
    writer_id bigint not null,
    target_id bigint not null,
    primary key (id)
);

alter table review add constraint fk_review_auction foreign key (auction_id) references auction (id);
alter table review add constraint fk_review_writer foreign key (writer_id) references users (id);
alter table review add constraint fk_review_target foreign key (target_id) references users (id);
