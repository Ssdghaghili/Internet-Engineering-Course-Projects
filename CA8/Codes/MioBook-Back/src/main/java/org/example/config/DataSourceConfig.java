package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(Environment env) throws Exception {

        Binder binder = Binder.get(env);
        String url = binder.bind("spring.datasource.url", String.class)
                .orElseThrow(() -> new IllegalStateException("Missing spring.datasource.url"));
        String user = binder.bind("spring.datasource.username", String.class)
                .orElseThrow(() -> new IllegalStateException("Missing spring.datasource.username"));


        String password;
        String pwFile = env.getProperty("SPRING_DATASOURCE_PASSWORD_FILE");
        if (pwFile != null && Files.exists(Path.of(pwFile))) {
            password = Files.readString(Path.of(pwFile)).trim();
        } else {
            password = env.getProperty("spring.datasource.password");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);

        return new HikariDataSource(config);
    }
}
