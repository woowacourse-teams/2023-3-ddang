create table auction (
                         id bigint not null auto_increment,
                         created_time datetime(6) not null,
                         last_modified_time datetime(6) not null,
                         bid_unit integer,
                         closing_time datetime(6),
                         is_deleted bit,
                         description text,
                         last_bid_price integer,
                         start_price integer,
                         title varchar(30),
                         winning_bid_price integer,
                         sub_category_id bigint,
                         primary key (id)
);

create table auction_image (
                               id bigint not null auto_increment,
                               authenticated bit not null,
                               store_name varchar(255),
                               upload_name varchar(255),
                               auction_id bigint,
                               primary key (id)
);

create table auction_region (
                                id bigint not null auto_increment,
                                auction_id bigint,
                                third_region_id bigint,
                                primary key (id)
);

create table bid (
                     id bigint not null auto_increment,
                     created_time datetime(6) not null,
                     price integer,
                     auction_id bigint,
                     bidder_id bigint,
                     primary key (id)
);

create table categories (
                            id bigint not null auto_increment,
                            name varchar(30),
                            main_category_id bigint,
                            primary key (id)
);

create table chat_room (
                           id bigint not null auto_increment,
                           created_time datetime(6) not null,
                           auction_id bigint not null,
                           buyer_id bigint not null,
                           primary key (id)
);

create table message (
                         id bigint not null auto_increment,
                         created_time datetime(6) not null,
                         contents text not null,
                         chat_room_id bigint not null,
                         receiver_id bigint not null,
                         writer_id bigint not null,
                         primary key (id)
);

create table region (
                        id bigint not null auto_increment,
                        name varchar(30),
                        first_region_id bigint,
                        second_region_id bigint,
                        primary key (id)
);


create table users (
                       id bigint not null auto_increment,
                       name varchar(255),
                       profile_image varchar(255),
                       reliability float(53) not null,
                       primary key (id)
);

alter table auction add constraint fk_auction_sub_category foreign key (sub_category_id) references categories (id);
alter table auction_image add constraint fk_auction_image foreign key (auction_id) references auction (id);
alter table auction_region add constraint fk_auction_region_auction foreign key (auction_id) references auction (id);
alter table auction_region add constraint fk_auction_region_third_region foreign key (third_region_id) references region (id);
alter table bid add constraint fk_bid_auction foreign key (auction_id) references auction (id);
alter table bid add constraint fk_bid_user foreign key (bidder_id) references users (id);
alter table categories add constraint fk_category_main_sub foreign key (main_category_id) references categories (id);
alter table chat_room add constraint fk_chat_room_auction foreign key (auction_id) references auction (id);
alter table chat_room add constraint fk_chat_room_buyer foreign key (buyer_id) references users (id);
alter table message add constraint fk_message_chat_room foreign key (chat_room_id) references chat_room (id);
alter table message add constraint fk_message_receiver foreign key (receiver_id) references users (id);
alter table message add constraint fk_message_writer foreign key (writer_id) references users (id);
alter table region add constraint fk_region_first_second foreign key (first_region_id) references region (id);
alter table region add constraint fk_region_second_third foreign key (second_region_id) references region (id);
