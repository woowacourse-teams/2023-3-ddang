create table read_message_log (
                                  id bigint not null auto_increment,
                                  chat_room_id bigint,
                                  reader_id bigint,
                                  last_read_message_id bigint,
                                  primary key (id)
);

alter table read_message_log add constraint fk_read_message_log_chat_room foreign key (chat_room_id) references chatRoom (id);
alter table read_message_log add constraint fk_read_message_reader foreign key (reader_id) references users (id);
