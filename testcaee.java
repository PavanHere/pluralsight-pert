import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.QueryTimeoutException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseOperationHandlerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void testExecuteReadStoredProcedure_Success() {
        // Mocking parameters
        String storedProcedureName = "testProcedure";
        Object[] params = new Object[]{1, 2, 3};
        RowMapper<String> rowMapper = mock(RowMapper.class);

        // Mocking expected result
        List<String> expectedResult = Arrays.asList("result1", "result2");
        when(jdbcTemplate.query(storedProcedureName, params, rowMapper)).thenReturn(expectedResult);

        // Creating instance of DatabaseOperationHandler with mocked JdbcTemplate
        DatabaseOperationHandler handler = new DatabaseOperationHandler(jdbcTemplate);

        // Calling the method under test
        List<String> result = handler.executeReadStoredProcedure(storedProcedureName, params, rowMapper);

        // Verifying that jdbcTemplate.query() method was called with correct parameters
        verify(jdbcTemplate, times(1)).setQueryTimeout(anyInt());
        verify(jdbcTemplate, times(1)).query(storedProcedureName, params, rowMapper);

        // Verifying the result
        assertEquals(expectedResult, result);
    }

    @Test
    void testExecuteReadStoredProcedure_QueryTimeoutException() {
        // Mocking parameters
        String storedProcedureName = "testProcedure";
        Object[] params = new Object[]{1, 2, 3};
        RowMapper<String> rowMapper = mock(RowMapper.class);

        // Mocking jdbcTemplate to throw QueryTimeoutException
        when(jdbcTemplate.query(storedProcedureName, params, rowMapper)).thenThrow(QueryTimeoutException.class);

        // Creating instance of DatabaseOperationHandler with mocked JdbcTemplate
        DatabaseOperationHandler handler = new DatabaseOperationHandler(jdbcTemplate);

        // Calling the method under test and verifying that it throws VasproducerAPIException
        assertThrows(VasproducerAPIException.class, () -> handler.executeReadStoredProcedure(storedProcedureName, params, rowMapper));
    }

    @Test
    void testExecuteReadStoredProcedure_OtherException() {
        // Mocking parameters
        String storedProcedureName = "testProcedure";
        Object[] params = new Object[]{1, 2, 3};
        RowMapper<String> rowMapper = mock(RowMapper.class);

        // Mocking jdbcTemplate to throw generic exception
        when(jdbcTemplate.query(storedProcedureName, params, rowMapper)).thenThrow(RuntimeException.class);

        // Creating instance of DatabaseOperationHandler with mocked JdbcTemplate
        DatabaseOperationHandler handler = new DatabaseOperationHandler(jdbcTemplate);

        // Calling the method under test and verifying that it throws VasproducerAPIException
        assertThrows(VasproducerAPIException.class, () -> handler.executeReadStoredProcedure(storedProcedureName, params, rowMapper));
    }
}
