alter table rooms
    add column auto_start_next_room boolean default false;

update rooms
set auto_start_next_room = false;
