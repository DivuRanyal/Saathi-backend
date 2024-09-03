import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import model.SubscriptionPackage;
import repository.SubscriptionPackageRepository;
import service.impl.SubscriptionPackageServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SubscriptionPackageServiceTest {
	@Mock
	private SubscriptionPackageRepository subscriptionPackageRepository;

	@InjectMocks
	private SubscriptionPackageServiceImpl subscriptionPackageService;

	@Test
	public void testUpdate_AllFields() {
	    int packageID = 1;
	    SubscriptionPackage existingPackage = new SubscriptionPackage("Old Name", "Old Description", new BigDecimal("10.00"), new BigDecimal("700.00"), new Date(), new Date(), 1);
	    existingPackage.setPackageID(packageID);

	    SubscriptionPackage updatedPackage = new SubscriptionPackage("New Name", "New Description", new BigDecimal("20.00"), new BigDecimal("1500.00"), new Date(), new Date(), 2);
	    updatedPackage.setUpdatedBy(2);

	    // Mock the behavior of the repository
	    when(subscriptionPackageRepository.findById(packageID)).thenReturn(Optional.of(existingPackage));
	    when(subscriptionPackageRepository.save(any(SubscriptionPackage.class))).thenReturn(updatedPackage);

	    // Call the service method
	    SubscriptionPackage result = subscriptionPackageService.update(packageID, updatedPackage);

	    // Assertions to verify the expected behavior
	    assertNotNull(result);
	    assertEquals("New Name", result.getPackageName());
	    assertEquals("New Description", result.getPackageDescription());
	    assertEquals(new BigDecimal("20.00"), result.getPriceUSD());
	    assertEquals(new BigDecimal("1500.00"), result.getPriceINR());
	    assertEquals(2, result.getStatus());

	    // Verify that the save method was called
	    verify(subscriptionPackageRepository).save(existingPackage);
	}
	
	 @Test
	    public void test_Update_AllFields() {
	        int packageID = 1;
	        SubscriptionPackage existingPackage = new SubscriptionPackage("Old Name", "Old Description", new BigDecimal("10.00"), new BigDecimal("700.00"), new Date(), new Date(), 1);
	        existingPackage.setPackageID(packageID);

	        SubscriptionPackage updatedPackage = new SubscriptionPackage("New Name", "New Description", new BigDecimal("20.00"), new BigDecimal("1500.00"), new Date(), new Date(), 2);
	        updatedPackage.setUpdatedBy(2);

	        // Mock the behavior of the repository
	        when(subscriptionPackageRepository.findById(packageID)).thenReturn(Optional.of(existingPackage));
	        when(subscriptionPackageRepository.save(any(SubscriptionPackage.class))).thenReturn(updatedPackage);

	        // Call the service method
	        SubscriptionPackage result = subscriptionPackageService.update(packageID, updatedPackage);

	        // Assertions to verify the expected behavior
	        assertNotNull(result);
	        assertEquals("New Name", result.getPackageName());
	        assertEquals("New Description", result.getPackageDescription());
	        assertEquals(new BigDecimal("20.00"), result.getPriceUSD());
	        assertEquals(new BigDecimal("1500.00"), result.getPriceINR());
	        assertEquals(2, result.getStatus());

	        // Verify that the save method was called
	        verify(subscriptionPackageRepository).save(existingPackage);
	    }

	 @Test
	    public void testGetSubscriptionPackageById() {
	        int packageID = 3;
	        SubscriptionPackage expectedPackage = new SubscriptionPackage("Basic Package", "Description", new BigDecimal("10.00"), new BigDecimal("700.00"), new Date(), new Date(), 1);
	        expectedPackage.setPackageID(packageID);

	        // Mock the repository to return the expected package
	        when(subscriptionPackageRepository.findById(packageID)).thenReturn(Optional.of(expectedPackage));

	        // Call the service method
	        SubscriptionPackage result = subscriptionPackageService.getSubscriptionPackageById(packageID);

	        // Assertions to verify the expected behavior
	        assertNotNull(result);
	        assertEquals(expectedPackage.getPackageID(), result.getPackageID());
	        assertEquals(expectedPackage.getPackageName(), result.getPackageName());
	        assertEquals(expectedPackage.getPackageDescription(), result.getPackageDescription());
	        assertEquals(expectedPackage.getPriceUSD(), result.getPriceUSD());
	        assertEquals(expectedPackage.getPriceINR(), result.getPriceINR());
	        assertEquals(expectedPackage.getStatus(), result.getStatus());

	        // Verify that the findById method was called
	        verify(subscriptionPackageRepository).findById(packageID);
	    }
	 
	 @Test
	 public void test_Update_PackageNameOnly() {
	     int packageID = 1;
	     SubscriptionPackage existingPackage = new SubscriptionPackage("Old Name", "Old Description", new BigDecimal("10.00"), new BigDecimal("700.00"), new Date(), new Date(), 1);
	     existingPackage.setPackageID(packageID);

	     // Create a new package with only the packageName updated
	     SubscriptionPackage updatedPackage = new SubscriptionPackage();
	     updatedPackage.setPackageName("New Name");
	     updatedPackage.setUpdatedBy(2); // Assume this field is always set during updates

	     // Mock the behavior of the repository
	     when(subscriptionPackageRepository.findById(packageID)).thenReturn(Optional.of(existingPackage));
	     when(subscriptionPackageRepository.save(any(SubscriptionPackage.class))).thenReturn(existingPackage);

	     // Call the service method
	     SubscriptionPackage result = subscriptionPackageService.update(packageID, updatedPackage);

	     // Assertions to verify the expected behavior
	     assertNotNull(result);
	     assertEquals("New Name", result.getPackageName());
	     assertEquals("Old Description", result.getPackageDescription()); // Should remain unchanged
	     assertEquals(new BigDecimal("10.00"), result.getPriceUSD()); // Should remain unchanged
	     assertEquals(new BigDecimal("700.00"), result.getPriceINR()); // Should remain unchanged
	     assertEquals(1, result.getStatus()); // Should remain unchanged

	     // Verify that the save method was called
	     verify(subscriptionPackageRepository).save(existingPackage);
	 }

}
