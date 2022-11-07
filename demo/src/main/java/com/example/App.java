package com.example;

import com.util.Util;
import java.util.Arrays;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )throws ParseException, Exception
    {
        // System.out.println( "Hello World!d" );
        // byte[] bytes1 = Util.hexToBytes("FEF0");
        // // System.out.println(bytes1);
        // System.out.println(Arrays.toString(bytes1));
        // String str = Util.bytesToHexString(bytes1);
        // System.out.println(str);
        // int result []  = new int[]{0,1,2,3,4};

        // byte[] bytes = Util.toByteArray(result, false);
        // System.out.println(Arrays.toString(bytes));
        // int[] result2 = Util.toIntArray(bytes, false);
        // System.out.println(Arrays.toString(result2));

        // 2654435665
        // 2147483647
        // int val3 = 2147483647;
        // System.out.println(val3+1+1);

        // int val  = (7 >>> 5);
        // int val2 = (8 << 4);
        // System.out.println(val);
        // System.out.println(val2);
        // System.out.println("sum-3对应的无符号整数：" + Integer.toUnsignedString(val2));


        // int k [] = new int[]{0,1,2};
        // int v [] = new int[]{5,6,7};
        // int[] result1 = Util.decrypt(v,k);
        // System.out.println("sum-3对应的无符号整数：" + Integer.toUnsignedString(-1640531527));
        // int arr3[] = new int[]{-1640531527,-1,2} ;
        // System.out.println(Arrays.toString(result1));
        // System.out.printf("%s, %s, %s",Integer.toUnsignedString(result1[0]),Integer.toUnsignedString(result1[1]),Integer.toUnsignedString(result1[2]));
        
        JSONObject obj = new JSONObject();
		
		obj.put("name", "Pankaj Kumar");
		obj.put("age", new Integer(32));
        String jsonStr = JSONObject.toJSONString(obj);

        System.out.println(jsonStr);
         
        // String jsonStr = "{\"name\":\"Pankaj Kumar\",\"age\":32}";
        // String jsonStr = "{\"school\":\"商职\",\"sex\":\"男\",\"name\":\"wjw\",\"age\":22}";
        // JSONParser parser = new JSONParser();

		// Object jsonObj = parser.parse(jsonStr);

		// JSONObject jsonObject = (JSONObject) jsonObj;

		// String name = (String) jsonObject.get("name");
		// System.out.println("Name = " + name);

        String str=  Util.encryptStr(jsonStr, "utf-8", "key");
        System.out.println(str);

        // 解密
        String newJsonStr = Util.decryptStr(str, "utf-8", "key");
        System.out.println(newJsonStr);
    }
}
