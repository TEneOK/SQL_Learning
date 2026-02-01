-- liquibase formatted sql

-- changeset iershov:1
- createIndex:
            indexName: idx_students_name
            tableName: students
            columns:
              - column:
                  name: name
                  descending: false

-- changeset iershov:2
- createIndex:
            indexName: idx_faculties_name_color
            tableName: faculties
            columns:
              - column:
                  name: name
                  descending: false
              - column:
                  name: color
                  descending: false