1. Создаем таблицу car для машин

CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    price NUMERIC(12,2) NOT NULL);

CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    has_driver_licence BOOLEAN NOT NULL,
    car_id BIGINT,
    CONSTRAINT fk_person_car FOREIGN KEY (car_id) REFERENCES car(id));