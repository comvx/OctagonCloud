package com.octagon.clientSide;

public class TimeZoneConverter {
    public String convert(String str) {
        String output = "";

        int h1 = (int)str.charAt(0) - '0';
        int h2 = (int)str.charAt(1)- '0';

        int hh = h1 * 10 + h2;

        hh %= 12;

        // Handle 00 and 12 case separately
        if (hh == 0) {
            output+=12;

            // Printing minutes and seconds
            for (int i = 2; i < 8; ++i) {
                output+=str.charAt(i);
            }
        }
        else {
            output+=hh;
            // Printing minutes and seconds
            for (int i = 2; i < 8; ++i) {
                output+=str.charAt(i);
            }
        }
        return output;
    }
}
