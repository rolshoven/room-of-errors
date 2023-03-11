create table users (
    id uuid primary key,
    profession varchar(250),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

create type room_status as enum ('not_ready', 'open', 'closed');
create domain room_code as varchar(8); -- id of room first block of an uuid -> 125ab6f9

create table rooms(
    code room_code primary key,
    status room_status not null default 'not_ready',
    title varchar(100) not null,
    description varchar(2000),
    question varchar(2000),
    time_limit_minutes integer,
    intro_text varchar(2000),
    intro_video_title varchar(2000),
    intro_video_url varchar(2000),
    outro_text varchar(2000),
    outro_video_title varchar(2000),
    outro_video_url varchar(2000),
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
    x_coordinate double precision not null,
    y_coordinate double precision not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),

    foreign key (room_code) references rooms(code) on delete cascade,
    foreign key (room_image_id) references room_images(id) on delete cascade,
    foreign key (user_id) references users(id) on delete cascade,
    unique (id, room_code, room_image_id)
);

create type users_room_participation_status as enum (
    'not_started',
    'started',
    'finished'
    );

create table users_room_status(
    room_code room_code not null,
    user_id uuid not null,
    participation_status users_room_participation_status not null default 'not_started',
    started_at timestamp with time zone,
    finished_at timestamp with time zone,
    foreign key (room_code) references rooms(code) on delete cascade,
    foreign key (user_id) references users(id) on delete cascade,
    primary key (room_code, user_id)
);