create table instance_configs (
    domain_name varchar(253) not null primary key,
    ip_address varchar(15) not null
);

create table discovery_testcases (
    name varchar(100) not null primary key
);

create table discovery_testcase_creds (
    name varchar(100) not null primary key,
    private_key_data blob(8192) not null,
    cert_data blob(8192) not null
);

create table discovery_testcase_mail_mappings (
    direct_address varchar(253) not null primary key,
    results_address varchar(253) not null
);
