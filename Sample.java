import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseOperationHandlerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DatabaseOperationHandler databaseOperationHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteReadStoredProcedure_Success() {
        // Mock data
        String storedProcedureName = "testProcedure";
        Object[] params = new Object[]{};
        RowMapper<Object> rowMapper = (resultSet, i) -> new Object(); // Mock RowMapper

        List<Object> mockResult = new ArrayList<>();
        // Add some mock result data here if needed

        // Mock behavior
        when(jdbcTemplate.query(eq(storedProcedureName), eq(rowMapper), eq(params))).thenReturn(mockResult);

        // Call the method under test
        List<Object> result = databaseOperationHandler.executeReadStoredProcedure(storedProcedureName, params, rowMapper);

        // Assertions
        assertNotNull(result);
        assertEquals(mockResult, result);

        // Verify interactions
        verify(jdbcTemplate, times(1)).setQueryTimeout(anyInt());
        verify(jdbcTemplate, times(1)).query(eq(storedProcedureName), eq(rowMapper), eq(params));
    }

    @Test
    public void testExecuteReadStoredProcedure_TimeoutException() {
        // Mock data
        String storedProcedureName = "testProcedure";
        Object[] params = new Object[]{};
        RowMapper<Object> rowMapper = (resultSet, i) -> new Object(); // Mock RowMapper

        // Mock behavior to throw QueryTimeoutException
        when(jdbcTemplate.query(eq(storedProcedureName), eq(rowMapper), eq(params))).thenThrow(QueryTimeoutException.class);

        // Call the method under test and assert exception
        assertThrows(VasproducerAPIException.class, () -> databaseOperationHandler.executeReadStoredProcedure(storedProcedureName, params, rowMapper));
    }

    @Test
    public void testExecuteReadStoredProcedure_Exception() {
        // Mock data
        String storedProcedureName = "testProcedure";
        Object[] params = new Object[]{};
        RowMapper<Object> rowMapper = (resultSet, i) -> new Object(); // Mock RowMapper

        // Mock behavior to throw a generic exception
        when(jdbcTemplate.query(eq(storedProcedureName), eq(rowMapper), eq(params))).thenThrow(RuntimeException.class);

        // Call the method under test and assert exception
        assertThrows(VasproducerAPIException.class, () -> databaseOperationHandler.executeReadStoredProcedure(storedProcedureName, params, rowMapper));
    }
}
