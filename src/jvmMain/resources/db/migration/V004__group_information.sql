create table room_group_information
(
    room_code  room_code                not null,
    user_id    uuid                     not null,
    group_name varchar(250)             not null,
    group_size int                      not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    foreign key (room_code) references rooms (code) on delete cascade,
    foreign key (user_id) references users (id) on delete cascade,
    primary key (room_code, user_id)
);

alter table rooms
    add column with_group_info boolean default false;
alter table rooms
    add column with_group_info_text varchar(2000);

update rooms
set with_group_info = false;

alter table rooms
    alter column with_group_info set not null;

