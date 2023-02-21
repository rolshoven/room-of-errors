
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


    foreign key (room_code) references rooms(code),
    foreign key (user_id) references users(id),
    primary key (room_code, user_id)
);


alter table rooms add column starting_text varchar(2000);
alter table rooms add column starting_video_title varchar(2000);
alter table rooms add column starting_video_url varchar(2000);
alter table rooms add column ending_text varchar(2000);
alter table rooms add column ending_video_title varchar(2000);
alter table rooms add column ending_video_url varchar(2000);