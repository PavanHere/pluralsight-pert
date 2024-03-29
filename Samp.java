import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectionDetailsDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private DatabaseOperationHandler storedProcCallUtility;

    @InjectMocks
    private CollectionDetailsDaoImpl collectionDetailsDao;

    @Test
    public void testGetCollectionDetails_WithValidRequest() {
        // Arrange
        CollectionDetailsRequest request = new CollectionDetailsRequest(/* provide required parameters */);
        List<PreCollectionDetails> expectedResult = new ArrayList<>();
        when(storedProcCallUtility.executeReadStoredProcedure(anyString(), any(Object[].class), any(PreCollectionMapper.class)))
                .thenReturn(expectedResult);

        // Act
        List<PreCollectionDetails> result = collectionDetailsDao.getCollectionDetails(request);

        // Assert
        assertEquals(expectedResult, result);
        verify(storedProcCallUtility).executeReadStoredProcedure(
                eq(StoredProcConstants.CALL_SP_VAS_PRECOLLECTIONS),
                any(Object[].class),
                any(PreCollectionMapper.class)
        );
        verifyNoMoreInteractions(storedProcCallUtility);
    }

    @Test
    public void testGetCollectionDetails_WithNullRequest() {
        // Act
        List<PreCollectionDetails> result = collectionDetailsDao.getCollectionDetails(null);

        // Assert
        assertNull(result);
        verifyNoInteractions(storedProcCallUtility);
    }

    @Test
    public void testGetCollectionDetails_WithEmptyResult() {
        // Arrange
        CollectionDetailsRequest request = new CollectionDetailsRequest(/* provide required parameters */);
        when(storedProcCallUtility.executeReadStoredProcedure(anyString(), any(Object[].class), any(PreCollectionMapper.class)))
                .thenReturn(null); // Simulate empty result

        // Act
        List<PreCollectionDetails> result = collectionDetailsDao.getCollectionDetails(request);

        // Assert
        assertNull(result);
        verify(storedProcCallUtility).executeReadStoredProcedure(
                eq(StoredProcConstants.CALL_SP_VAS_PRECOLLECTIONS),
                any(Object[].class),
                any(PreCollectionMapper.class)
        );
        verifyNoMoreInteractions(storedProcCallUtility);
    }

    // Add more edge cases as needed
}
