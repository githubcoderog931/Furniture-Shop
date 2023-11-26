package com.sheryians.major.service;

import com.sheryians.major.domain.Sale;
import com.sheryians.major.dto.SalesDto;
import com.sheryians.major.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public SalesDto calculateWeeklySales() {
        SalesDto salesDto = new SalesDto();

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));

        List<Sale> weeklySales = saleRepository.findBySaleDateBetween(startDate, endDate);
        Map<LocalDateTime, Long> dailyOrderCounts = calculateDailyOrderCounts(weeklySales);

        salesDto.setStartDate(startDate);
        salesDto.setEndDate(endDate);
        salesDto.setDailyOrderCounts(dailyOrderCounts);

        return salesDto;
    }

    private Map<LocalDateTime, Long> calculateDailyOrderCounts(List<Sale> weeklySales) {
        Map<LocalDateTime, Long> dailyOrderCounts = new HashMap<>();

        for (Sale sale : weeklySales) {
            LocalDateTime saleDate = sale.getSaleDate();
            dailyOrderCounts.merge(saleDate, 1L, Long::sum);
        }

        return dailyOrderCounts;
    }
}
