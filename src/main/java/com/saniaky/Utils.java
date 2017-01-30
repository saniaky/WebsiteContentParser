package com.saniaky;

/**
 * @author Alexander Kohonovsky
 * @since 30.01.2017
 */
public final class Utils {

    public static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

}
