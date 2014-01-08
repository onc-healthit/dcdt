drop table instance_configs;
create table instance_configs (
    domain varchar(253) not null primary key
);

drop table discovery_testcases;
create table discovery_testcases (
    name varchar(100) not null primary key,
    mail_addr varchar(507) not null
);

drop table discovery_testcase_creds;
create table discovery_testcase_creds (
    discovery_testcase_name varchar(100) references discovery_testcases(name),
    name varchar(100) not null primary key,
    private_key_data blob(8192) not null,
    cert_data blob(8192) not null
);
