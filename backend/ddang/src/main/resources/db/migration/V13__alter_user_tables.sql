UPDATE users SET profile_image_id = 1 WHERE profile_image_id = null;
ALTER TABLE users MODIFY profile_image_id bigint NOT NULL;
