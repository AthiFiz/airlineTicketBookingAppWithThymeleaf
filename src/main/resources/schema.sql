
CREATE TABLE IF NOT EXISTS users(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL
);

CREATE TABLE airports (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code CHAR(3) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  city VARCHAR(50) NOT NULL,
  country VARCHAR(50) NOT NULL
);

CREATE TABLE airplanes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tail_number VARCHAR(20) NOT NULL UNIQUE,
  size VARCHAR(10) NOT NULL,
  model VARCHAR(50) NOT NULL,
  home_airport_id BIGINT NOT NULL,
  CONSTRAINT fk_airplane_airport FOREIGN KEY (home_airport_id)
    REFERENCES airports(id)
);

CREATE TABLE flights (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  airplane_id BIGINT NOT NULL,
  departure_airport_id BIGINT NOT NULL,
  arrival_airport_id BIGINT NOT NULL,
  departure_time TIMESTAMP NOT NULL,
  arrival_time   TIMESTAMP NOT NULL,
  total_first_class_seats    INT NOT NULL,
  total_business_class_seats INT NOT NULL,
  total_economy_class_seats  INT NOT NULL,
  CONSTRAINT fk_flight_airplane FOREIGN KEY (airplane_id) REFERENCES airplanes(id),
  CONSTRAINT fk_flight_dep_airport FOREIGN KEY (departure_airport_id) REFERENCES airports(id),
  CONSTRAINT fk_flight_arr_airport FOREIGN KEY (arrival_airport_id) REFERENCES airports(id)
);

CREATE TABLE tickets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  flight_id  BIGINT NOT NULL,
  user_id    BIGINT NOT NULL,
  ticket_class VARCHAR(10) NOT NULL,
  seat_number  VARCHAR(5) NOT NULL,
  CONSTRAINT fk_ticket_flight FOREIGN KEY (flight_id) REFERENCES flights(id),
  CONSTRAINT fk_ticket_user   FOREIGN KEY (user_id)   REFERENCES users(id),
  UNIQUE (flight_id, seat_number)
);
