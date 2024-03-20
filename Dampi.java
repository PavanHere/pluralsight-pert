import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@Aspect
public class DataSourceConfig {

    @Value("${jdbcTemplate.globalTimeoutMillis:500}") // Default timeout is 500 milliseconds
    private int globalTimeoutMillis;

    @Bean
    public DataSource dataSource() {
        // Configure and return your data source here
        return null;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @AfterReturning(pointcut = "execution(* javax.sql.DataSource.getConnection(..))", returning = "connection")
    public void setQueryTimeout(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            if (statement instanceof com.mysql.cj.jdbc.ConnectionImpl) {
                com.mysql.cj.jdbc.ConnectionImpl mysqlConnection = (com.mysql.cj.jdbc.ConnectionImpl) connection;
                mysqlConnection.setQueryTimeout(globalTimeoutMillis / 1000); // Set query timeout in seconds
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
