package com.example;

import com.util.Util;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!d" );
        byte[] bytes = Util.hexToBytes("FEF0");
        System.out.println(bytes);
        String str = Util.bytesToHexString(bytes);
        System.out.println(str);
    }
}
