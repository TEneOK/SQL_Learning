UPDATE student
SET age = 16
WHERE age < 16;

ALTER TABLE student
ADD CONSTRAINT chk_student_age CHECK (age >= 16);

ALTER TABLE student
ADD CONSTRAINT uq_student_name UNIQUE (name);

ALTER TABLE faculty
ADD CONSTRAINT uq_faculty_name_color UNIQUE (faculty_name, color);

UPDATE student
SET age = 20
WHERE age IS NULL;

ALTER TABLE student
ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE student
ALTER COLUMN age SET NOT NULL;