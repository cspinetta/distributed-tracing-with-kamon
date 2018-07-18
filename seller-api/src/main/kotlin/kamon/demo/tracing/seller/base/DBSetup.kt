package kamon.demo.tracing.seller.base

import kotliquery.HikariCP
import kotliquery.queryOf
import kotliquery.sessionOf
import kotliquery.using

object DBSetup {

    fun setupDB() {
        HikariCP.default("jdbc:h2:mem:seller", "user", "pass")
        // when using in memory DB, create the DDL

        using(sessionOf(HikariCP.dataSource())) { session ->
            session.run(queryOf("""
      create table seller (
        id serial not null primary key,
        first_name varchar(255),
        last_name varchar(255),
        birthday date,
        category varchar(255),
        registration_date timestamp not null default now(),
        grade decimal(20, 2),
        active boolean default false
      )
    """).asExecute) // returns Boolean
            session.run(queryOf("""
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('John D.', 'Rockefeller', '1839-05-23', 'PLATINUM', 9.9, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Adam', 'Smith', '1723-02-12', 'PLATINUM', 9.1, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Maurice', 'Allais', '1911-08-22', 'SILVER', 5.1, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Walter', 'Adams', '1922-11-07', 'PLATINUM', 7.9, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Alberto', 'Alesina', '1957-02-12', 'BRONZE', 7.8, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Philippe', 'Aghion', '1956-12-02', 'PLATINUM', 9.3, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Nicola', 'Acocella', '1939-07-12', 'BRONZE', 9.2, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Franklin', 'Allen', '1956-01-26', 'PLATINUM', 8.6, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Henry', 'Carter Adams', '1851-04-13', 'BRONZE', 7.7, true);
            insert into seller (first_name, last_name, birthday, category, grade, active)
              values ('Gary', 'Becker', '1930-02-23', 'SILVER', 5.9, true);""").asUpdate)
        }
    }
}