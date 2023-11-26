package com.sheryians.major.controller;

import com.sheryians.major.constants.OrderStatus;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.Referral;
import com.sheryians.major.domain.TimePeriod;
import com.sheryians.major.domain.User;
import com.sheryians.major.dto.SalesDto;
import com.sheryians.major.repository.ReferralRepository;
import com.sheryians.major.service.ReferralService;
import com.sheryians.major.service.SalesReportService;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sheryians.major.domain.TimePeriod.MONTHLY;
import static com.sheryians.major.domain.TimePeriod.WEEKLY;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    SalesReportService salesReportService;

    @Autowired
    ReferralRepository referralRepository;

    @Autowired
    UserService userService;

    @Autowired
    ReferralService referralService;

    private static final String TOKEN_ATTRIBUTE = "token";


    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showAdminPage(Model model, Principal principal){
        User user = userService.findByUsername(principal.getName());

        SalesDto salesDataForOneDay = salesReportService.getSalesForOneDay();
        SalesDto salesDataForOneWeek = salesReportService.getSalesForOneWeek();
        SalesDto salesDataForOneMonth = salesReportService.getSalesForOneMonth();
        SalesDto salesDataForOneYear = salesReportService.getSalesForOneYear();
        SalesDto getAllSales = salesReportService.getSalesForAllTime();



        List<Referral> referral = referralRepository.findAll();
        Long referralCount = (long) referral.size();
        List<Referral> referralComplete = referralRepository.findByCompleted(true);
        Long successfullReferral = (long) referralComplete.size();


        model.addAttribute("successfullReferral",successfullReferral);
        model.addAttribute("totalReferral",referralCount);
        model.addAttribute("salesDataForOneDay",salesDataForOneDay);
        model.addAttribute("salesDataForOneWeek",salesDataForOneWeek);
        model.addAttribute("salesDataForOneMonth",salesDataForOneMonth);
        model.addAttribute("salesDataForOneYear",salesDataForOneYear);
        model.addAttribute("getAllSales",getAllSales);
        System.out.println();
        return "dashboard";
    }

    @GetMapping("/dashboard/graph")
    @ResponseBody
    public ResponseEntity<SalesDto> showGraph(){
        SalesDto getAllSales = salesReportService.getSalesForAllTime();
        return ResponseEntity.ok(getAllSales);
    }
    @GetMapping("/dashboard/daily-graph")
    @ResponseBody
    public ResponseEntity<SalesDto>showGraphWeekly(@RequestParam(value = "selectedPeriod") TimePeriod selectedPeriod){
        SalesDto salesDto= null;

        switch (selectedPeriod){
            case WEEKLY -> salesDto = salesReportService.getSalesForOneWeek();
            case MONTHLY -> salesDto = salesReportService.getSalesForOneMonth();
            case DAILY -> salesDto = salesReportService.getSalesForOneDay();

        }
        System.out.println(salesDto.getDailyOrderCounts());
        System.out.println(salesDto.getMonthlyAmounts());
        System.out.println(salesDto.getEndDate());
        System.out.println(salesDto.getStartDate());
        System.out.println(salesDto.getMonthlyLabels());
        System.out.println(salesDto.getTotalOrderCount());
        System.out.println(salesDto.getTotalRevenue());
        return ResponseEntity.ok(salesDto);
    }
    @PostMapping("/dashboard-sales")
    public String adminDashBoard(@RequestParam(value = "selectedTimePeriod")TimePeriod selectedTimePeriod,
                                 HttpSession session, Model model){

        if (selectedTimePeriod != null) {
            List<Orders> orders = salesReportService.getOrderByTimePeriod(selectedTimePeriod);
            double totalSales = salesReportService.calculateTotalSales(orders);
            int totalOrders = orders.size();

            String token = UUID.randomUUID().toString();
            session.setAttribute(token, orders);

            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("orders", orders);
            model.addAttribute("totalSales", totalSales);
            model.addAttribute(TOKEN_ATTRIBUTE, token);
            return "sales-report";

        }else {
            model.addAttribute("message","Please select a time period");
            return "redirect:/admin/dashboard";
        }
    }
    @PostMapping("/dashboard/sales/byDate")
    public String orderByDates(@RequestParam(value = "startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
                               @RequestParam(value = "endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate,
                               HttpSession session, Model model){

        if (startDate != null && endDate != null) {
            List<Orders> orders = salesReportService.findByOrderDateBetween(startDate, endDate);
            double totalSales = salesReportService.calculateTotalSales(orders);
            int totalOrders = orders.size();

            String token = UUID.randomUUID().toString();
            session.setAttribute(token, orders);

            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("orders", orders);
            model.addAttribute("totalSales", totalSales);
            model.addAttribute(TOKEN_ATTRIBUTE, token);
            return "sales-report";
        }else {
            model.addAttribute("message","please select dates");
            return "redirect:/admin/dashboard";
        }
    }


    @GetMapping("/dashboard/all-time-sales")
    public ResponseEntity<SalesDto> getAllTimeSales() {
        SalesDto salesDataForOneYear = salesReportService.getSalesForOneYear();
        return ResponseEntity.ok(salesDataForOneYear);
    }
//    @GetMapping("/generate-pdf")
//    public void exportToPdf(HttpServletResponse response, HttpSession session,
//                            HttpServletRequest request){
//
//        String token = request.getParameter(TOKEN_ATTRIBUTE);
//        List<Orders> orders = (List<Orders>) session.getAttribute(token);
//        pdfGeneratorService.generatePdf(orders,response);
//
//    }
//    @GetMapping("/generate-csv")
//    public void exportToCsv(HttpServletResponse response, HttpSession session,
//                            HttpServletRequest request) throws IOException {
//
//        String token = request.getParameter(TOKEN_ATTRIBUTE);
//        List<Orders> orders = (List<Orders>) session.getAttribute(token);
//        pdfGeneratorService.exportToCSV(orders,response);
//
//    }

    @GetMapping("/statusCounts")
    @ResponseBody
    public ResponseEntity<Map<OrderStatus, Long>> getStatusCounts() {
        Map<OrderStatus, Long> statusCounts = salesReportService.getStatusCounts();
        return ResponseEntity.ok(statusCounts);
    }




}
