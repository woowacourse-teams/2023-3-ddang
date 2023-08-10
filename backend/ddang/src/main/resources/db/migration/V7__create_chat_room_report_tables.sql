create table chat_room_report (
     id bigint not null auto_increment,
     created_time datetime(6) not null,
     description text,
     chat_room_id bigint,
     reporter_id bigint,
     primary key (id)
);

alter table chat_room_report add constraint fk_chat_room_report_auction foreign key (chat_room_id) references auction (id);
alter table chat_room_report add constraint fk_chat_room_report_reporter foreign key (reporter_id) references users (id);
