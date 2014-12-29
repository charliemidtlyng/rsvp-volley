package no.charlie.rsvp.api.config

import org.apache.commons.dbcp.BasicDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

import javax.sql.DataSource

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Configuration
@Profile('prod')
class DatabaseConfig {

        @Bean
        public DataSource dataSource()  {

            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(dbUrl);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            basicDataSource.setDriverClassName("org.postgresql.Driver")
            return basicDataSource;
        }
}
