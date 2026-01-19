-- liquibase formatted sql

-- changeset iershov:1
- createIndex:
            indexName: idx_student_name
            tableName: student
            columns:
              - column:
                  name: name

              -rollback:
                 - dropIndex:
                 indexName: idx_student_name
                 tableName: student

-- changeset iershov:2
- createIndex:
            indexName: idx_faculty_name_color
            tableName: faculty
            columns:
              - column:
                  name: name
              - column:
                  name: color
            clustered: false
            unique: false