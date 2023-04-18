# Insert 10.000 Records (person + 1:1 address relation)

- Mongo: 10 sec
- Elastic: 90 sec

- H2 InMem : 10 sec
- Postgres : 60 sec

# Lastname Startswith "S" Query without Index

- Mongo: 30ms
- Elastic: 30ms

- H2 In Mem: 400ms (Hibernate makes single select per row)
- Postgres: 4000ms (Hibernate makes single select per row)

# Conclusion
- Mongodb is the fastest for insert + search
- Elastic is the slowest for insert + equally fast for search than mongodb
- Postgres + Hibernate is the slowest for insert + search, creating an index does not help
