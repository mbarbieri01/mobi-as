
    create table Member (
        id bigint not null auto_increment,
        email varchar(255),
        name varchar(255),
        passkey tinyblob,
        password varchar(255),
        passwordHash integer not null,
        phone_number varchar(255),
        primary key (id),
        unique (email)
    );
