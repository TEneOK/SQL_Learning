CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR NOT NULL,
    model VARCHAR NOT NULL,
    price NUMERIC(12, 2) NOT NULL,
    year_of_manufacture INTEGER,

     CONSTRAINT chk_car_price CHECK (price > 10000),
    CONSTRAINT chk_car_year CHECK (year_of_manufacture >= 1990 AND year_of_manufacture <= EXTRACT(YEAR FROM CURRENT_DATE))
);


CREATE TABLE persons (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    age INTEGER NOT NULL,
    has_license BOOLEAN DEFAULT FALSE,

    CONSTRAINT chk_person_age CHECK (age >= 18 AND age <= 112),
    CONSTRAINT uq_person_name UNIQUE (name)
);

CREATE TABLE person_car (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    is_owner BOOLEAN DEFAULT FALSE,
    usage_percentage INTEGER DEFAULT 100,

     CONSTRAINT chk_usage_percentage CHECK (usage_percentage >= 0 AND usage_percentage <= 100),

      CONSTRAINT fk_person_car_person
        FOREIGN KEY (person_id)
        REFERENCES persons(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_person_car_car
        FOREIGN KEY (car_id)
        REFERENCES cars(id)
        ON DELETE CASCADE,

        CONSTRAINT uq_person_car UNIQUE (person_id, car_id)
);