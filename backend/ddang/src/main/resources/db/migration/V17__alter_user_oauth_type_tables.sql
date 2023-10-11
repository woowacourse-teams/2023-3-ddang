ALTER TABLE users ADD oauth2_type VARCHAR(10);
UPDATE users SET oauth2_type = 'KAKAO' WHERE oauth2_type is null;
ALTER TABLE users MODIFY oauth2_type VARCHAR(10) NOT NULL;
