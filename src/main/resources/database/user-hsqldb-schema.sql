-- drop table account;
-- drop table role;
-- drop table permission;
-- drop table user_role;
-- drop table role_permission;

create table U001 (
    userid varchar(25) not null,
    email varchar(80) null,
    firstname varchar(80) null,
    lastname varchar(80) null,
    addr varchar(80) null,
    zip varchar(20) null,
    country varchar(20) null,
    phone varchar(80) null,
    sex varchar(1) null,
    addrzip varchar(200) null,
    login_failure_count int default 0 null,
    last_login_timestamp timestamp null,
    constraint pk_account primary key (userid)
);


create table t_member_base (
	id	varchar(255),
	bank	varchar(255),
	bankaccount	varchar(255),
	category	varchar(255),
	city	varchar(255),
	cityname	varchar(255),
	createdate	date,
	eaddress	varchar(255),
	ebuscode	varchar(255),
	econtact	varchar(255),
	econtactfax	varchar(255),
	econtactmobile	varchar(255),
	econtactphone	varchar(255),
	efax	varchar(255),
	elegalperson	varchar(255),
	emobile	varchar(255),
	ename	varchar(255),
	enature	varchar(255),
	eorgcode	varchar(255),
	ephone	varchar(255),
	epostcode	varchar(255),
	estate	varchar(4000),
	etaxcode	varchar(255),
	ebrandname	varchar(255),
	orgno	varchar(255),
	paddress	varchar(255),
	pbirthday	date,
	pmobile	varchar(255),
	pname	varchar(255),
	pphone	varchar(255),
	psex	varchar(255),
	ppostcode varchar(255),
	province	varchar(255),
	provincename	varchar(255),
	state	varchar(255),
	buslicense_id	varchar(255),
	companyproperty_id	decimal(19),
	industry_id	decimal(255),
	legalpersonidcard_id	varchar(255),
	memberlevel_id	varchar(255),
	membertype_id	varchar(255),
	orgcertificate_id	varchar(255),
	pidcard_id	varchar(255),
	taxregcertificate_id	varchar(255),
	user_id	decimal(19),
	idcardno	varchar(255),
	accountapplicationimg_id	varchar(255),
	bankcardfrontimg_id	varchar(255),
	idcardbackimg_id	varchar(255),
	idcardfrontimg_id	varchar(255),
	accountapplicationimgl_id	varchar(255),
	changer_id	decimal(19),
	creator_id	decimal(19),
	updatedate	date,
	yearfeedate	date,
	yearfeeenddate	date,
	yearfeenote	varchar(255),
	yearfeestartdate	date,
	jingbanren	varchar(255),
	bank_id	varchar(255),
	relaacctbank	varchar(255),
	relaacctbankaddr	varchar(255),
	relaacctbankcode	varchar(255),
	logoname	varchar(150),
	creditfeedate	date,
	creditfeeenddate	date,
	creditfeenote	varchar(2000),
	creditfeestartdate	date,
	email	varchar(255),
	qq	varchar(255),
	constraint pk_member primary key (id)
);

create table sys_user (
    ID decimal(19) not null,
    USERNAME varchar(255) null,
    PASSWORD varchar(255) null,
    USERTYPE varchar(255) null,
    CREATEBY_ID decimal(19) null,
    REALNAME varchar(255) null,
    CODING varchar(255) null,
    PHONE varchar(255) null,
    EMAIL varchar(255) null,
    constraint pk_sys_user primary key (ID)
);

create table t_member_types (
    ID varchar(25) not null,
    NAME varchar(80) null,
    CODE varchar(80) null,
    CREATEDATE date null,
    UPDATEDATE date null,
    constraint pk_t_member_types primary key (ID)
);

create table t_industry (
    ID decimal(25) not null,
    NAME varchar(80) null,
    NOTE varchar(80) null,
    constraint pk_t_industry primary key (ID)
);

create table t_companyproperty (
    ID decimal(25) not null,
    NAME varchar(80) null,
    LEVEL varchar(80) null,
    constraint pk_t_companyproperty primary key (ID)
);

create table t_banklibrary (
 	ID varchar(80) not null,
    CAPTION varchar(80) null,
    CODE varchar(80) null,
    CREATETIME date null,
    BANKORDER decimal(80) null,
    constraint pk_t_banklibrary primary key (ID)
);

