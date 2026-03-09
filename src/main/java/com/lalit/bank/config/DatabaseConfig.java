package com.lalit.bank.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Database configuration for SQLite.
 *
 * SQLite-specific notes:
 * - SQLite has limited support for PESSIMISTIC_WRITE locks; we use
 *   application-level synchronization for critical sections when needed.
 * - The database file (banking.db) is created automatically in the project root.
 * - SQLite is suitable for development and small-scale deployments.
 *   For production with high concurrency, consider PostgreSQL or MySQL.
 *
 * @author Lalit
 */
@Configuration
public class DatabaseConfig {

    /**
     * Configures the SQLite data source.
     *
     * Note: SQLite JDBC driver handles connection pooling internally.
     * For production use with high concurrency, consider HikariCP with
     * appropriate settings for SQLite's single-writer limitation.
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.sqlite.JDBC")
                .url("jdbc:sqlite:banking.db")
                .build();
    }
}
