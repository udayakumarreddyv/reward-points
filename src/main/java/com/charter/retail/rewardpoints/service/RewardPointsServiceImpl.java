package com.charter.retail.rewardpoints.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charter.retail.rewardpoints.configuration.RewardPointsConfig;
import com.charter.retail.rewardpoints.exception.RPException;
import com.charter.retail.rewardpoints.model.CustomerRewardPoints;
import com.charter.retail.rewardpoints.model.CustomerTransaction;

@Service
public class RewardPointsServiceImpl implements RewardPointsService {
    private static final Logger logger = LoggerFactory.getLogger(RewardPointsServiceImpl.class);
    
    @Autowired
    private RewardPointsConfig config;

    @Override
    public CustomerRewardPoints getRewardPointsByCustoemrID(Integer customerId) throws RPException {
        if (customerId == null || customerId <= 0) {
            throw new RPException("Invalid customer ID", null);
        }

        try {
            List<CustomerTransaction> transactions = getCustomerTransactions(customerId);
            
            if (transactions.isEmpty()) {
                logger.info("No transactions found for customer ID: {}", customerId);
                return createEmptyRewardPoints(customerId);
            }

            CustomerRewardPoints rewardPoints = new CustomerRewardPoints();
            rewardPoints.setCustomerId(customerId);
            rewardPoints.setCustomerName(transactions.get(0).getCustomerName());
            rewardPoints.setMonthlyPoints(new HashMap<>());
            rewardPoints.setTotalPoints(0);

            calculatePoints(rewardPoints, transactions);
            
            logger.debug("Calculated reward points for customer {}: {}", customerId, rewardPoints);
            return rewardPoints;

        } catch (Exception e) {
            logger.error("Error calculating reward points for customer {}: {}", customerId, e.getMessage());
            throw new RPException("Error calculating reward points", e);
        }
    }

    private CustomerRewardPoints createEmptyRewardPoints(Integer customerId) {
        CustomerRewardPoints emptyPoints = new CustomerRewardPoints();
        emptyPoints.setCustomerId(customerId);
        emptyPoints.setMonthlyPoints(new HashMap<>());
        emptyPoints.setTotalPoints(0);
        return emptyPoints;
    }

    private void calculatePoints(CustomerRewardPoints rewardPoints, List<CustomerTransaction> transactions) {
        transactions.forEach(transaction -> {
            int points = calculatePointsForTransaction(transaction.getTransactionAmount());
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(transaction.getTransactionDate());
            int month = cal.get(Calendar.MONTH);
            rewardPoints.getMonthlyPoints().merge(month, points, Integer::sum);
            rewardPoints.setTotalPoints(rewardPoints.getTotalPoints() + points);
        });
    }

    private int calculatePointsForTransaction(BigDecimal amount) {
        int points = 0;
        
        // First calculate base points: 1 point for every dollar over $50
        if (amount.compareTo(new BigDecimal(config.getThreshold().getFirst())) > 0) {
            points += (amount.subtract(new BigDecimal(config.getThreshold().getFirst()))).intValue();
        }
        
        // Add bonus points: 1 additional point for every dollar over $100
        if (amount.compareTo(new BigDecimal(config.getThreshold().getSecond())) > 0) {
            points += (amount.subtract(new BigDecimal(config.getThreshold().getSecond()))).intValue();
        }
        
        return points;
    }

    private List<CustomerTransaction> getCustomerTransactions(Integer customerId) {
        // TODO: Replace with database implementation
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        List<CustomerTransaction> allTransactions = new ArrayList<>();
        
        try {
            allTransactions.add(new CustomerTransaction(1, "Joe", formatter.parse("10-10-2022"), new BigDecimal(123)));
            allTransactions.add(new CustomerTransaction(1, "Joe", formatter.parse("10-26-2022"), new BigDecimal(52)));
            allTransactions.add(new CustomerTransaction(1, "Joe", formatter.parse("11-10-2022"), new BigDecimal(35)));
            allTransactions.add(new CustomerTransaction(1, "Joe", formatter.parse("11-14-2022"), new BigDecimal(234)));
            allTransactions.add(new CustomerTransaction(1, "Joe", formatter.parse("12-04-2022"), new BigDecimal(74)));
            allTransactions.add(new CustomerTransaction(2, "Smith", formatter.parse("10-10-2022"), new BigDecimal(74)));
            allTransactions.add(new CustomerTransaction(2, "Smith", formatter.parse("11-11-2022"), new BigDecimal(234)));
            allTransactions.add(new CustomerTransaction(2, "Smith", formatter.parse("12-01-2022"), new BigDecimal(78)));
            allTransactions.add(new CustomerTransaction(2, "Smith", formatter.parse("12-03-2022"), new BigDecimal(123)));
        } catch (ParseException e) {
            logger.error("Error parsing transaction dates", e);
            return new ArrayList<>();
        }

        return allTransactions.stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
}
