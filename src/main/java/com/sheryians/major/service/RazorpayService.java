package com.sheryians.major.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.sheryians.major.domain.TransactionDetails;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


@Service
public class RazorpayService {



    private static final String KEY ="rzp_test_ZjDakOScsBJM19";
    private static final String SECRET_KEY = "j8KFK3N29DqoDfhzWshhybVu";
    private static final String CURRENCY = "INR";


    public TransactionDetails createTransaction(Double amount){
        try {
            JSONObject jsonObject = new JSONObject();
            RazorpayClient razorpayClient = new RazorpayClient(KEY,SECRET_KEY);

            jsonObject.put("amount",(amount * 100));
            jsonObject.put("currency",CURRENCY);

            Order order = razorpayClient.orders.create(jsonObject);

            return prepareTransactionDetails(order);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private TransactionDetails prepareTransactionDetails(Order order){
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");


        return new TransactionDetails(orderId,currency,amount,KEY);
    }
}
