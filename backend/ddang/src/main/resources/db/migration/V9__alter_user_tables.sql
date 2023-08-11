alter table users add is_deleted bit;

UPDATE users SET is_deleted = 0 where auctioneer_count is null;
