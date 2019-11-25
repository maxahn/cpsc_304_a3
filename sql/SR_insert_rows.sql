insert into vt values ('Economy', 'Cheap', 120.00, 20.00, 1.00, 30.00, 5.00, 0.25, 10.00);
insert into vt values ('Compact', 'Small', 280.00, 42.00, 2.00, 30.00, 5.00, 0.25, 12.00);
insert into vt values ('Mid-size', 'Medium', 450.00, 66.00, 3.00, 30.00, 5.00, 0.25, 15.00);
insert into vt values ('Standard', 'Regular', 450.00, 66.00, 3.00, 30.00, 5.00, 0.25, 15.00);
insert into vt values ('Full-size', 'Big', 650.00, 100.00, 5.00, 42.00, 6.50, 0.30, 20.00);
insert into vt values ('SUV', 'Large', 1100.00, 180.00, 8.00, 60.00, 10.00, 0.50, 25.00);
insert into vt values ('Truck', 'Giant', 1200.00, 200.00, 10.00, 90.00, 15.00, 0.75, 40.00);

insert into vehicle values ('CD388A', 10000, 'Nissan', 'Note', 2005, 'white', 37852, 'available', 'Economy', 1, 'Vancouver');
insert into vehicle values ('EF398D', 10001, 'Toyota', 'Corolla', 2010, 'blue', 5247, 'available', 'Compact', 1, 'Vancouver');
insert into vehicle values ('AB442E', 10002, 'Chevrolet', 'Malibu', 2008, 'silver', 7732, 'maintenance', 'Mid-size', 1, 'Vancouver');
insert into vehicle values ('DE213B', 10003, 'Ford', 'Mondeo', 2009, 'white', 9874, 'available', 'Standard', 2, 'Richmond');
insert into vehicle values ('ED001E', 10004, 'Nissan', 'Maxima', 2018, 'white', 8223, 'available', 'Full-size', 1, 'Vancouver');
insert into vehicle values ('DG156E', 10005, 'Chevrolet', 'Tahoe', 2015, 'black', 23145, 'maintenance', 'SUV', 3, 'Burnaby');
insert into vehicle values ('FA157B', 10006, 'Chevrolet', 'Montana', 2011, 'black', 8872, 'available', 'Truck', 3, 'Burnaby');
insert into vehicle values ('HI416A', 10007, 'Honda', 'City', 2013, 'silver', 77765, 'available', 'Economy', 'Richmond');
insert into vehicle values ('EA173E', 10008, 'Honda', 'Fit', 2005, 'red', 17835, 'available', 'Economy', 2, 'Richmond');
insert into vehicle values ('EF103A', 10009, 'Dodge', 'Dart', 2015, 'white', 45614, 'available', 'Compact', 1, 'Vancouver');
insert into vehicle values ('AF105E', 10010, 'Dodge', 'Charger', 2018, 'black', 7843, 'rented', 'Full-size', 1, 'Vancouver');
insert into vehicle values ('EH237G', 10011, 'Volkswagen', 'Tiguan', 2017, 'violet', 4528, 'rented', 'SUV', 1, 'Vancouver');
insert into vehicle values ('KH397E', 10012, 'Jeep', 'Cherokee', 2001, 'red', 872, 'available', 'SUV', 1, 'Vancouver');
insert into vehicle values ('ID253B', 10013, 'Volkswagen', 'Jetta', 2012, 'silver', 33241, 'rented', 'Standard', 2, 'Richmond');
insert into vehicle values ('EG624G', 10014, 'Toyota', 'Yaris', 2010, 'silver', 65247, 'available', 'Economy', 2, 'Richmond');

insert into customer values (8395728, 5558367, 'Bob Chen', '111 E. 11 St.');
insert into customer values (1739572, 5552734, 'Jack Lee', '222 E. 22 St.');
insert into customer values (2496723, 5551837, 'Carl Smith', '333 W. 33 Ave.');
insert into customer values (3495289, 5552863, 'Susan Hop', '444 E. 4 Ave.');

insert into reservation values (1000, 'Full-size', 1739572, TO_DATE('11/25/2019 08:00', 'mm/dd/yyyy hh24:mi'), TO_DATE('12/02/2019 17:00', 'mm/dd/yyyy hh24:mi'));
insert into reservation values (1001, 'Economy', 8395728, TO_DATE('12/25/2019 09:00', 'mm/dd/yyyy hh24:mi'), TO_DATE('01/01/2020 15:00', 'mm/dd/yyyy hh24:mi'));

insert into rent values (1000, 'AF105E', 1739572, TO_DATE('11/25/2019 08:00', 'mm/dd/yyyy hh24:mi'), TO_DATE('12/02/2019 17:00', 'mm/dd/yyyy hh24:mi'), 7843, 'Jack Lee', '3333444455556666', TO_DATE('08/22', 'mm/yy'), 1000);
insert into rent values (1001, 'EH237G', 2496723, TO_DATE('11/26/2019 08:00', 'mm/dd/yyyy hh24:mi'), TO_DATE('11/26/2019 14:00', 'mm/dd/yyyy hh24:mi'), 4528, 'Carl Smith', '1234567812344321', TO_DATE('04/20', 'mm/yy'), null);
insert into rent values (1002, 'ID253B', 1739572, TO_DATE('11/20/2019 12:00', 'mm/dd/yyyy hh24:mi'), TO_DATE('11/26/2019 09:00', 'mm/dd/yyyy hh24:mi'), 33241, 'Susan Hop', '9999876544443210', TO_DATE('07/20', 'mm/yy'), null);

insert into return values (1002, TO_DATE('11/26/2019 08:54', 'mm/dd/yyyy hh24:mi'), 34975, 'T', 480.00);

commit work;