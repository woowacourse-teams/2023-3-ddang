alter table users add oauth_id bigint;

UPDATE users SET oauth_id = 0 where oauth_id is null;
