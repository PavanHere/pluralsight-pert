import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@Aspect
public class DataSourceConfig {

    @Value("${jdbcTemplate.globalTimeoutMillis:500}") // Default timeout is 500 milliseconds
    private int globalTimeoutMillis;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterReturning(
            pointcut = "execution(java.sql.Connection javax.sql.DataSource.getConnection(..))",
            returning = "connection"
    )
    public void setQueryTimeout(Connection connection) throws SQLException {
        if (globalTimeoutMillis > 0) {
            // Obtain a CallableStatement and set the query timeout
            CallableStatement callableStatement = connection.prepareCall("{call sp_dummy()}");
            callableStatement.setQueryTimeout(globalTimeoutMillis / 1000); // Set query timeout in seconds
            callableStatement.close();
        }
    }

    // Other beans and configurations...
}
