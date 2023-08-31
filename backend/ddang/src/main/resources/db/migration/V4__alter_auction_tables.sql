alter table auction add auctioneer_count integer;

UPDATE auction SET auctioneer_count = 0 where auctioneer_count is null;
