create table users
(
    id       int auto_increment
        primary key,
    cf       varchar(16)     not null,
    name     varchar(50)     not null,
    surname  varchar(50)     not null,
    email    varchar(100)    not null,
    password char(64)        not null,
    status   varchar(50)     not null,
    phone    varchar(20)     null,
    credit   float default 0 null,
    token    varchar(32)     null,
    constraint cf
        unique (cf),
    constraint email
        unique (email)
);

create table transactions
(
    id       int auto_increment
        primary key,
    user_id  int                                  null,
    amount   float                                not null,
    datetime datetime default current_timestamp() null,
    mode     varchar(50)                          not null,
    constraint transactions_ibfk_1
        foreign key (user_id) references users (id)
);

create index user_id
    on transactions (user_id);

create definer = android@`%` trigger prevent_negative_credit
    before insert
    on transactions
    for each row
BEGIN
    DECLARE new_credit FLOAT;

    -- Calculate the new credit after transaction
    SELECT credit + NEW.amount INTO new_credit
    FROM users
    WHERE id = NEW.user_id;

    -- If the new credit would be negative throw an exception
    IF new_credit < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Transaction failed: Not enough credit';
    END IF;
END;

create definer = android@`%` trigger update_credito_after_transaction
    after insert
    on transactions
    for each row
BEGIN
    -- Update the credit of the user after transaction
    UPDATE users
    SET credit = credit + NEW.amount
    WHERE id = NEW.user_id;
END;
