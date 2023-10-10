alter table auction drop winning_bid_price;

alter table auction change last_bid_price last_bid_id bigint;

alter table auction add constraint fk_auction_last_bid foreign key (last_bid_id) references bid (id);
