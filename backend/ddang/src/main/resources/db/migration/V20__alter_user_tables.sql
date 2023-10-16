alter table users add writer_id bigint;
alter table users add constraint fk_answer_writer foreign key (writer_id) references writer (id);
