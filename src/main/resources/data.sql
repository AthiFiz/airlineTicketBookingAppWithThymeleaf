-- users
INSERT INTO users (username,password,role,active) VALUES
  ('customer','{noop}customer123','ROLE_CUSTOMER',TRUE),
  ('operator','{noop}operator123!','ROLE_OPERATOR',TRUE),
  ('admin','{noop}admin123','ROLE_ADMIN',TRUE);

-- airports
INSERT INTO airports (code,name,city,country) VALUES
  ('JFK','John F. Kennedy Intl Airport','New York','USA'),
  ('SYD','Sydney Kingsford Smith Airport','Sydney','Australia'),
  ('LHR','Heathrow Airport','London','UK'),
  ('CMB','Bandaranaike Intl Airport','Colombo','Sri Lanka'),
  ('DXB','Dubai Intl Airport','Dubai','UAE'),
  ('SIN','Changi Airport','Singapore','Singapore');

-- airplanes (stationed at the airports below)
INSERT INTO airplanes (tail_number,size,model,home_airport_id) VALUES
  ('VT-001','LARGE','Boeing 777', 1),  -- JFK
  ('VT-002','MEDIUM','Airbus A320', 2), -- SYD
  ('VT-003','SMALL','ATR 72',    4),    -- CMB
  ('VT-004','LARGE','Boeing 777',3),    -- LHR
  ('VT-005','MEDIUM','Airbus A330',5);  -- DXB

-- lights:
--   JFK→SYD (sample long-haul)
--   SYD→LHR (connecting)
--   LHR→CMB, CMB→DXB, DXB→JFK (new direct legs)
INSERT INTO flights (
  airplane_id, departure_airport_id, arrival_airport_id,
  departure_time,          arrival_time,
  total_first_class_seats,
  total_business_class_seats,
  total_economy_class_seats
) VALUES
  (1,1,2,'2025-06-10T08:00:00','2025-06-10T20:00:00', 10,20,100),  -- JFK→SYD
  (1,2,3,'2025-06-10T22:00:00','2025-06-11T00:00:00', 10,20,100),  -- SYD→LHR
  (4,3,4,'2025-06-12T09:00:00','2025-06-12T21:00:00', 10,20,200),  -- LHR→CMB
  (3,4,5,'2025-06-13T06:00:00','2025-06-13T09:00:00',  4,12, 60),  -- CMB→DXB
  (5,5,1,'2025-06-14T02:00:00','2025-06-14T14:00:00', 10,24,210);  -- DXB→JFK

-- Transit route example: CMB→DUB via LHR on 2025-06-15
INSERT INTO flights (
  airplane_id, departure_airport_id, arrival_airport_id,
  departure_time,          arrival_time,
  total_first_class_seats,
  total_business_class_seats,
  total_economy_class_seats
) VALUES
  (4,4,3,'2025-06-15T01:00:00','2025-06-15T11:00:00', 10,20,200),  -- Leg 1: CMB→LHR
  (2,3,2,'2025-06-15T13:00:00','2025-06-15T15:00:00', 10,20,100);  -- Leg 2: LHR→DUB

-- tickets
INSERT INTO tickets (flight_id,user_id,ticket_class,seat_number) VALUES
  (1,1,'ECONOMY','12A'),   -- customer on JFK→SYD
  (1,1,'BUSINESS','2B'),   -- customer business
  (3,1,'FIRST','1A'),      -- customer first on LHR→CMB
  (4,2,'ECONOMY','8C'),    -- operator on CMB→DXB
  (5,3,'BUSINESS','3D'),   -- admin on DXB→JFK
  (6,1,'ECONOMY','15F'),   -- customer on transit leg 1
  (7,1,'ECONOMY','22C');   -- customer on transit leg 2
