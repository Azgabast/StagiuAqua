# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user_data (
  id                        varchar(255) not null,
  username                  varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  password1                 varchar(255),
  password2                 varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  constraint pk_user_data primary key (id))
;

create sequence user_data_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists user_data;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists user_data_seq;

