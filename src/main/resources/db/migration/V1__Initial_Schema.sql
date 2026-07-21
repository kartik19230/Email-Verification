 CREATE TABLE users(
	id SERIAL PRIMARY KEY,
	name VARCHAR(255),
	email VARCHAR(255),
	password VARCHAR(255),
	enabled BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP
 );
 
 CREATE TABLE verification_token(
	id BIGSERIAL PRIMARY KEY,
	token VARCHAR(255),
	user_id INT,
	expiry_date TIMESTAMP,
	CONSTRAINT fk_verification_token_user
		FOREIGN KEY (user_id)
		REFERENCES users (id)
		ON DELETE CASCADE
);