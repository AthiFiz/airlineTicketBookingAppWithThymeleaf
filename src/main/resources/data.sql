
-- Users
--INSERT INTO users (username,password,role) VALUES
-- ('alice','password123','ROLE_CUSTOMER'),
-- ('bob','secret!','ROLE_OPERATOR'),
-- ('admin','admin123','ROLE_ADMIN');
INSERT INTO users (username,password,role) VALUES
  ('alice','{noop}password123','ROLE_CUSTOMER'),
  ('bob','{noop}secret!','ROLE_OPERATOR'),
  ('admin','{noop}admin123','ROLE_ADMIN');

-- Airports
INSERT INTO airports (code,name,city,country) VALUES
 ('JFK','John F. Kennedy Intl Airport','New York','USA'),
 ('DUB','Dublin Airport','Dublin','Ireland'),
 ('LHR','Heathrow Airport','London','UK');

-- Airplanes
INSERT INTO airplanes (tail_number,size,model,home_airport_id) VALUES
 ('VT-001','LARGE','Boeing 777', 1),
 ('VT-002','MEDIUM','Airbus A320', 2);

-- Flights: JFK→DUB on 2025-06-10
INSERT INTO flights (airplane_id,departure_airport_id,arrival_airport_id,departure_time,arrival_time,
                     total_first_class_seats,total_business_class_seats,total_economy_class_seats)
 VALUES
 (1,1,2,'2025-06-10T08:00:00','2025-06-10T20:00:00', 10,20,100),
 (1,2,3,'2025-06-10T22:00:00','2025-06-11T00:00:00', 10,20,100);

-- Tickets (example)
INSERT INTO tickets (flight_id,user_id,ticket_class,seat_number) VALUES
 (1,1,'ECONOMY','12A'),
 (1,1,'BUSINESS','2B');
