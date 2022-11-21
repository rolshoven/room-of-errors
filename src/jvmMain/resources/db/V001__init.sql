create table users (
    id uuid primary key,
    profession varchar(250),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

create type room_status as enum ('open', 'closed');
create domain room_code as varchar(8); -- id of room first block of an uuid -> 125ab6f9

create table rooms(
    code room_code primary key,
    status room_status not null default 'closed',
    title varchar(250) not null,
    description varchar(2000),
    question varchar(2000),
    time_limit_minutes integer,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

create table room_images(
    id uuid primary key,
    room_code room_code not null,
    title varchar(250) not null,
    url varchar(2000) not null,
    file boolean not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),

    foreign key (room_code) references rooms(code) on delete cascade,
    unique (id, room_code)
);

create table answers(
    id uuid primary key,
    room_code room_code not null,
    room_image_id uuid not null,
    user_id uuid not null,
    answer_number integer not null,
    answer varchar(2000) not null,
    x_coordinate integer not null,
    y_coordinate integer not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),

    foreign key (room_code) references rooms(code),
    foreign key (room_image_id) references room_images(id),
    foreign key (user_id) references users(id),
    unique (id, room_code, room_image_id)
);