import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollectionDetailsDaoImplTest {

    @Mock
    private DatabaseOperationHandler databaseOperationHandler;

    @Mock
    private CollectionDetailsRequest request;

    @InjectMocks
    private CollectionDetailsDaoImpl collectionDetailsDao;

    @Test
    public void testGetCollectionDetails_Success() {
        // Mocking databaseOperationHandler to return a dummy list
        List<PreCollectionDetails> dummyList = new ArrayList<>();
        when(databaseOperationHandler.executeReadStoredProcedure(any(String.class), any(Object[].class), any(RowMapper.class))).thenReturn(dummyList);

        // Mocking request parameters
        when(request.getReceiverGp()).thenReturn("ReceiverGP");
        when(request.getStartDate()).thenReturn("2024-01-01");
        when(request.getEndDate()).thenReturn("2024-01-31");
        when(request.getUniqueid()).thenReturn("uniqueId");
        when(request.getProduct()).thenReturn("product");

        // Calling the method under test
        List<PreCollectionDetails> result = collectionDetailsDao.getCollectionDetails(request);

        // Assertions
        assertEquals(dummyList, result);
    }

    @Test
    public void testGetCollectionDetails_QueryTimeoutException() {
        // Mocking databaseOperationHandler to throw QueryTimeoutException
        doThrow(QueryTimeoutException.class).when(databaseOperationHandler).executeReadStoredProcedure(any(String.class), any(Object[].class), any(RowMapper.class));

        // Mocking request parameters
        when(request.getReceiverGp()).thenReturn("ReceiverGP");
        when(request.getStartDate()).thenReturn("2024-01-01");
        when(request.getEndDate()).thenReturn("2024-01-31");
        when(request.getUniqueid()).thenReturn("uniqueId");
        when(request.getProduct()).thenReturn("product");

        // Calling the method under test and asserting exception
        assertThrows(VasproducerAPIException.class, () -> collectionDetailsDao.getCollectionDetails(request));
    }

    @Test
    public void testGetCollectionDetails_Exception() {
        // Mocking databaseOperationHandler to throw a generic exception
        doThrow(RuntimeException.class).when(databaseOperationHandler).executeReadStoredProcedure(any(String.class), any(Object[].class), any(RowMapper.class));

        // Mocking request parameters
        when(request.getReceiverGp()).thenReturn("ReceiverGP");
        when(request.getStartDate()).thenReturn("2024-01-01");
        when(request.getEndDate()).thenReturn("2024-01-31");
        when(request.getUniqueid()).thenReturn("uniqueId");
        when(request.getProduct()).thenReturn("product");

        // Calling the method under test and asserting exception
        assertThrows(VasproducerAPIException.class, () -> collectionDetailsDao.getCollectionDetails(request));
    }
}
