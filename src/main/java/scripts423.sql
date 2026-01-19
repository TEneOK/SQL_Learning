SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name,
FROM students s
INNER JOIN faculties f ON s.faculty_id = f.id
ORDER BY s.name;


SELECT
    s.name AS student_name,
    s.age AS student_age,
    a.id AS avatar_id,
    a.file_path,
    a.file_size,
    a.media_type,
    f.name AS faculty_name
FROM students s
INNER JOIN avatars a ON s.id = a.student_id
LEFT JOIN faculties f ON s.faculty_id = f.id
ORDER BY s.name;