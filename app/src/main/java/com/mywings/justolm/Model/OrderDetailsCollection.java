package com.mywings.justolm.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/27/2016.
 */
public class OrderDetailsCollection {
    public static List<OrderDetails> ORDERDETAILS;
    public static List<OrderDetails> getORDERDETAILS() {
        return ORDERDETAILS;
    }
    static {
        ORDERDETAILS = new ArrayList<OrderDetails>();
        ORDERDETAILS.add(new OrderDetails("1", "Zen Plus", "4", "Weekly", ""));
    }
}
