create table instance_configs (
    domain varchar(253) not null primary key
);

create table discovery_testcases (
    name varchar(100) not null primary key,
    mail_addr varchar(507) not null
);

create table discovery_testcase_creds (
    discovery_testcase_name varchar(100) references discovery_testcases(name),
    issuer_name varchar(100) references discovery_testcase_creds(name),
    name varchar(100) not null primary key,
    private_key_data blob(8192) not null,
    cert_data blob(8192) not null
);
