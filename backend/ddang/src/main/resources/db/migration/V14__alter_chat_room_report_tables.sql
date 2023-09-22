alter table chat_room_report drop index fk_chat_room_report_auction;
alter table chat_room_report add constraint fk_chat_room_report_chat_room foreign key (chat_room_id) references chat_room (id);
