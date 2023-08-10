create table auction_report
(
    id           bigint not null auto_increment,
    created_time datetime(6) not null,
    description  text,
    auction_id   bigint,
    reporter_id  bigint,
    primary key (id)
);

alter table auction_report
    add constraint fk_auction_report_auction foreign key (auction_id) references auction (id);
alter table auction_report
    add constraint fk_auction_report_reporter foreign key (reporter_id) references users (id);
