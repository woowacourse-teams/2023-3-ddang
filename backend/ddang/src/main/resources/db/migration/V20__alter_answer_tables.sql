alter table answer add writer_id bigint;
alter table answer add constraint fk_answer_writer foreign key (writer_id) references users (id);
