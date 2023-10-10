create table question
(
    id           bigint      not null auto_increment,
    created_time datetime(6) not null,
    content      text,
    is_deleted   bit,
    auction_id   bigint,
    writer_id    bigint,
    primary key (id)
);
create table answer
(
    id           bigint      not null auto_increment,
    created_time datetime(6) not null,
    content      text,
    is_deleted   bit,
    question_id  bigint,
    primary key (id)
);

create table question_report
(
    id           bigint      not null auto_increment,
    created_time datetime(6) not null,
    description  text,
    question_id bigint,
    reporter_id  bigint,
    primary key (id)
);
create table answer_report
(
    id           bigint      not null auto_increment,
    created_time datetime(6) not null,
    description  text,
    answer_id bigint,
    reporter_id  bigint,
    primary key (id)
);

alter table question add constraint fk_question_auction foreign key (auction_id) references auction (id);
alter table question add constraint fk_question_writer foreign key (writer_id) references users (id);
alter table answer add constraint fk_answer_question foreign key (question_id) references question (id);

alter table question_report add constraint fk_question_report_question foreign key (question_id) references question (id);
alter table question_report add constraint fk_question_report_reporter foreign key (reporter_id) references users (id);
alter table answer_report add constraint fk_answer_report_answer foreign key (answer_id) references answer (id);
alter table answer_report add constraint fk_answer_report_reporter foreign key (reporter_id) references users (id);
