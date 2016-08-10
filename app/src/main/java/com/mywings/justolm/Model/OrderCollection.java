package com.mywings.justolm.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/25/2016.
 */
public class OrderCollection {

    public static List<Order> getORDERS() {
        return ORDERS;
    }
    public static List<Order> ORDERS;

    static {
        ORDERS = new ArrayList<Order>();
       // ORDERS.add(new Order("2016-02-05 05:00 PM", "123456789", "Weekly", false, false));
       // ORDERS.add(new Order("2016-02-06 05:00 PM", "123456780", "Weekly", false, false));
       // ORDERS.add(new Order("2016-02-07 05:00 PM", "123456781", "Monthly", false, false));
       // ORDERS.add(new Order("2016-02-08 05:00 PM", "123456782", "Weekly", false, false));
       // ORDERS.add(new Order("2016-02-09 05:00 PM", "123456783", "Weekly", false, false));
    }
}
