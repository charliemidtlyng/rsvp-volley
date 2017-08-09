package no.charlie.rsvp.api.config

import org.apache.commons.dbcp.BasicDataSource
import org.slf4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

import javax.sql.DataSource

import static org.slf4j.LoggerFactory.getLogger

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Configuration
@Profile('prod')
class DatabaseConfig {

    private static final Logger LOGGER = getLogger(DatabaseConfig)

        @Bean
        DataSource dataSource()  {


            LOGGER.info("----- Setting up datasource ---- " )
            String dbName = System.getenv("RDS_DB_NAME")
            String username = System.getenv("RDS_USERNAME")
            String password = System.getenv("RDS_PASSWORD")
            String hostname = System.getenv("RDS_HOSTNAME")
            String port = System.getenv("RDS_PORT")

            String dbUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password

            BasicDataSource basicDataSource = new BasicDataSource()
            basicDataSource.setUrl(dbUrl)
            basicDataSource.setUsername(username)
            basicDataSource.setPassword(password)
            basicDataSource.setDriverClassName("org.postgresql.Driver")
            return basicDataSource
        }
}
