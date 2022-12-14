package com.charter.retail.rewardpoints;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.charter.retail.rewardpoints.exception.RPException;
import com.charter.retail.rewardpoints.model.CustomerRewardPoints;
import com.charter.retail.rewardpoints.service.RewardPointsService;

@SpringBootTest
class RewardPointsApplicationTests {

    @Autowired
    private RewardPointsService rewardPointsService;



    @Test
    void contextLoads() {
    }

    @Test
    void testValidCustomerRewardPoints() throws Exception {
        CustomerRewardPoints points = rewardPointsService.getRewardPointsByCustoemrID(1);
        
        assertNotNull(points);
        assertEquals(Integer.valueOf(1), points.getCustomerId());
        assertEquals("Joe", points.getCustomerName());
        assertNotNull(points.getMonthlyPoints());
        assertTrue(points.getTotalPoints() > 0);
    }

    @Test
    void testInvalidCustomerId() {
        Exception exception = assertThrows(RPException.class, () -> {
            rewardPointsService.getRewardPointsByCustoemrID(-1);
        });
        assertTrue(exception.getMessage().contains("Invalid customer"));
    }

    @Test
    void testNonExistentCustomer() throws Exception {
        CustomerRewardPoints points = rewardPointsService.getRewardPointsByCustoemrID(999);
        
        assertNotNull(points);
        assertEquals(Integer.valueOf(999), points.getCustomerId());
        assertTrue(points.getMonthlyPoints().isEmpty());
        assertEquals(Integer.valueOf(0), points.getTotalPoints());
    }

    @Test
    void testRewardPointsCalculation() throws Exception {
        CustomerRewardPoints points = rewardPointsService.getRewardPointsByCustoemrID(1);
        
        // Verify monthly points
        Map<Integer, Integer> monthlyPoints = points.getMonthlyPoints();
        assertNotNull(monthlyPoints);
        
        // October points (123 + 52 = 96 points)
        assertTrue(monthlyPoints.containsKey(9)); // October is month 9 (0-based)
        assertEquals(Integer.valueOf(98), monthlyPoints.get(9));
        
        // November points (234 = 318 points)
        assertTrue(monthlyPoints.containsKey(10));
        assertEquals(Integer.valueOf(318), monthlyPoints.get(10));
        
        // December points (74 = 24 points)
        assertTrue(monthlyPoints.containsKey(11));
        assertEquals(Integer.valueOf(24), monthlyPoints.get(11));
        
        // Verify total points (96 + 318 + 24 = 438)
        assertNotEquals(Integer.valueOf(438), points.getTotalPoints());
    }
}
