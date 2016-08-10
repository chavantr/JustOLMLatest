package com.mywings.justolm.Utilities;

import java.util.Random;

/**
 * Created by Tatyabhau Chavan on 5/19/2016.
 */
public class Halted {

    private static Halted ourInstance;

    public static synchronized Halted getInstance() {
        if (null == ourInstance) {
            ourInstance = new Halted();
        }
        return ourInstance;
    }

    public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
