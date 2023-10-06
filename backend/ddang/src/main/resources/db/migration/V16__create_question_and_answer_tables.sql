create table question (
     id bigint not null auto_increment,
     created_time datetime(6) not null,
     content text,
     is_deleted bit,
     auction_id bigint,
     writer_id bigint,
     primary key (id)
);
create table answer (
    id bigint not null auto_increment,
    created_time datetime(6) not null,
    content text,
    is_deleted bit,
    question_id bigint,
    primary key (id)
);

alter table question add constraint fk_question_auction foreign key (auction_id) references auction (id);
alter table question add constraint fk_question_writer foreign key (writer_id) references users (id);
alter table answer add constraint fk_answer_question foreign key (question_id) references question (id);
