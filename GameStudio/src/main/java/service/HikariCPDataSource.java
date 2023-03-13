package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariCPDataSource {
    public static HikariDataSource getHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/gamestudio");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("root");

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setAutoCommit(false);

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        return ds;
    }
}
