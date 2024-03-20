import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {

    @Value("${jdbcTemplate.globalTimeoutMillis:500}") // Default timeout is 500 milliseconds
    private int globalTimeoutMillis;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class); // Set your driver class
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public StatementCallback<CallableStatement> statementCallback() {
        return new StatementCallback<CallableStatement>() {
            @Override
            public CallableStatement doInStatement(Statement statement) throws SQLException {
                CallableStatement callableStatement = statement.getConnection().prepareCall("");
                if (globalTimeoutMillis > 0) {
                    callableStatement.setQueryTimeout(globalTimeoutMillis / 1000);
                }
                return callableStatement;
            }
        };
    }
}
