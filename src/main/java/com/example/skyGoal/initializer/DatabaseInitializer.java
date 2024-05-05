//package com.example.skyGoal.initializer;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseInitializer {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @PostConstruct
//    public void initializeDatabase() {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcTemplate.execute("ALTER TABLE public.football_matches ALTER COLUMN timestamp TYPE bigint USING timestamp::bigint");
//        jdbcTemplate.execute("ALTER TABLE public.weather_data ALTER COLUMN timestamp TYPE bigint USING timestamp::bigint");
//    }
//}
