alter table auction add seller_id bigint;

alter table auction add constraint fk_auction_seller foreign key (seller_id) references users (id);
