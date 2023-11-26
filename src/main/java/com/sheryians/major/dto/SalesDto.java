//package com.sheryians.major.dto;
//
//
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//@Getter
//@Setter
//public class SalesDto {
//
//    private LocalDate startDate;
//    private LocalDate endDate;
//    private Double totalRevenue;
//    private Integer totalOrderCount;
//
//    private List<String> monthlyLabels;
//    private List<Double> monthlyAmounts;
//    private Map<LocalDate, Long> dailyOrderCounts;
//
//    public void setMonthlySales(List<String> monthlyLabels, List<Double> monthlyAmounts) {
//        this.monthlyLabels = monthlyLabels;
//        this.monthlyAmounts = monthlyAmounts;
//
//    }
//    public void setDailyOrderCounts(Map<LocalDate, Long> dailyOrderCounts) {
//        this.dailyOrderCounts = dailyOrderCounts;
//    }
//}

package com.sheryians.major.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SalesDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double totalRevenue;
    private Integer totalOrderCount;

    private List<String> monthlyLabels;
    private List<Double> monthlyAmounts;
    private Map<LocalDateTime, Long> dailyOrderCounts;

    public void setMonthlySales(List<String> monthlyLabels, List<Double> monthlyAmounts) {
        this.monthlyLabels = monthlyLabels;
        this.monthlyAmounts = monthlyAmounts;

    }
    public void setDailyOrderCounts(Map<LocalDateTime, Long> dailyOrderCounts) {
        this.dailyOrderCounts = dailyOrderCounts;
    }
}