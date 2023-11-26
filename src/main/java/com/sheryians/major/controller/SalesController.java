package com.sheryians.major.controller;

import com.sheryians.major.domain.Orders;
import com.sheryians.major.service.OrderService;
import com.sheryians.major.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SalesController {

    @Autowired
    private  SalesReportService salesReportService;
    
    @Autowired
    OrderService orderService;




    @GetMapping("/weekly")
    public String showWeeklySales(Model model) {
//        List<Orders> dailyOrderCounts = salesReportService.getWeeklySales();
//        List<Long> count = new ArrayList<>();
//        System.out.println(dailyOrderCounts);
////        // Assuming you have a method to calculate total sales amount for each day
////        List<Double> dailySalesAmounts = salesReportService.calculateDailySalesAmounts();
//
//        model.addAttribute("dailyOrderCounts", dailyOrderCounts);
////        model.addAttribute("dailySalesAmounts", dailySalesAmounts);
//
//        return "weeklySales"; // Assuming you have a Thymeleaf template for displaying weekly sales

        LocalDateTime now = LocalDateTime.now();
        List<Orders> orderDates = orderService.getAllOrderDate();
        List<Orders> orderPrice = orderService.getAllOrderPrice();
//        double oneWeekPrice = 0.0;
        double oneMonthPrice = 0.0;
        double[] dailyPrices = new double[7];
        double[] weeklyPrices = new double[4];

        for (Orders order : orderDates) {
            LocalDateTime orderDate = order.getLocalDate();
            int price = (int) calculateOrderPriceForDay(order, orderPrice);
            long weekIndex = (now.toLocalDate().toEpochDay() - orderDate.toLocalDate().toEpochDay()) / 7;
            if (weekIndex >= 0 && weekIndex < 4) {
                weeklyPrices[Math.toIntExact(weekIndex)] += price;
            }
            if (now.minusMonths(1).isBefore(orderDate)) {
                oneMonthPrice += price;
            }
            long dayIndex = now.toLocalDate().toEpochDay() - orderDate.toLocalDate().toEpochDay();
            if (dayIndex >= 0 && dayIndex < 7) {
                dailyPrices[Math.toIntExact(dayIndex)] += price;
            }
        }
        model.addAttribute("dailyPrices", dailyPrices);
        model.addAttribute("weeklyPrices", weeklyPrices);
        return "weeklySales";
    }

        private double calculateOrderPriceForDay(Orders order, List<Orders> orderPrice) {
            double price = 0.0;
            for (Orders orderItem : orderPrice) {
                if (orderItem.equals(order)) {
                    price += orderItem.getAmount();
                }
            }
            return price;
        }

}
