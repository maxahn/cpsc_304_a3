create table vt ( 
	vtname varchar(20) not null PRIMARY KEY,
	feature varchar(20) not null,
	wrate numeric(6,2) not null,
	drate numeric(6,2) not null,
	hrate numeric(6,2) not null,
	wirate numeric(6,2) not null,
	dirate numeric(6,2) not null,
	hirate numeric(6,2) not null,
	krate numeric(6,2) not null
);

create table vehicle (
	vlicence char(6) not null PRIMARY KEY,
	vid integer not null,
	make varchar(20) not null,
	model varchar(20) not null,
	year integer not null,
	color varchar(20) not null,
	odometer integer not null,
	status varchar(20) not null,
	vtname varchar(20) not null,
	location integer,
	city varchar(20),
	foreign key (vtname) references vt,
	unique (vlicence, vid)
);

create table customer ( 
	dlicence integer not null PRIMARY KEY,
	cellphone integer not null,
	name varchar(50) not null,
	address varchar(50) not null,
	unique (cellphone)
);

create table reservation (
	confNo integer not null PRIMARY KEY,
	vtname varchar(20) not null,
	dlicence integer not null,
	fromDate timestamp not null,
	toDate timestamp not null,
	foreign key (vtname) references vt,
	foreign key (dlicence) references customer
);

create table rent ( 
	rid integer not null PRIMARY KEY,
	vlicence char(6) not null,
	dlicence integer not null,
	fromDate timestamp not null,
	toDate timestamp not null,
	odometer integer not null,
	cardName varchar(20) not null,
	cardNo char(16) not null,
	expDate date not null,
	confNo integer,
	foreign key (vlicence) references vehicle,
	foreign key (dlicence) references customer,
	foreign key (confNo) references reservation
);

create table return ( 
	rid integer not null PRIMARY KEY,
	return_date timestamp not null,
	odometer integer not null,
	fulltank char(1) not null,
	value numeric(12,2) not null,
	foreign key (rid) references rent
);

commit;