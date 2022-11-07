package com.util;

import java.util.Arrays;

public class Util {
    // Encrypt data with key.
    public static byte[] encrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false);
    }

    // Decrypt data with key.
    public static byte[] decrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(decrypt(toIntArray(data, false), toIntArray(key, false)), true);
    }

    // Encrypt data with key.
    public static int[] encrypt(int[] v, int[] k) {
        int n = v.length - 1;
        if (n < 1) {
            return v;
        }
        if (k.length < 4) {
            int[] key = new int[4];
            System.arraycopy(k, 0, key, 0, k.length);
            k = key;
        }
        int z = v[n], y = v[0], delta = 0x9E3779B9, sum = 0, e;
        int p, q = 6 + 52 / (n + 1);
        // System.out.println(p);
        // System.out.println(Arrays.toString(v));

        while (q-- > 0) {
            sum = sum + delta;
            e = sum >>> 2 & 3;
            // System.out.printf("sum:%s, e:%s, k:%s \n",Integer.toUnsignedString(sum),Integer.toUnsignedString(e),Arrays.toString(k));
            for (p = 0; p < n; p++) {

                // int temp=  (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
                // v[p] += temp;
                // System.out.printf("temp:%s, v[p]:%s\n",Integer.toUnsignedString(temp),Integer.toUnsignedString(v[p]));
                // System.out.printf("v[p]:%s\n",Integer.toUnsignedString(v[p]));
                // int temp = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
                // v[p] += temp;
                // System.out.printf("v[p]:%s, v[p+1]:%s, y:%s, z: %s \n",Integer.toUnsignedString(v[p]),Integer.toUnsignedString(v[p+1]),Integer.toUnsignedString(y),Integer.toUnsignedString(z));
                y = v[p + 1];
                // int temp1 = (z >>> 5 ^ y << 2);
                // int temp2_1 = y >>> 3;
                // int temp2_2 = z << 4;
                // int temp2_0 = temp2_1 ^ temp2_2;
                // System.out.printf("temp2_0:%d, y:%d, z:%d, temp2_1:%d, temp2_2: %d  \n",temp2_0,y,z,temp2_1,temp2_2);
                
                // int temp2 = (y >>> 3 ^ z << 4) ;
                // int temp3 = (sum ^ y);
                // int temp4 = (k[p & 3 ^ e] ^ z);
                // int temp0 = temp1 + temp2 ^ temp3 + temp4;
                // System.out.printf("temp0:%d, temp1:%d, temp2:%d, temp3:%d, temp4: %d \n",temp0,temp1,temp2,temp3,temp4);
                

                // int temp101 = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ;
                // int temp102 = (sum ^ y) + (k[p & 3 ^ e] ^ z);
                // int temp100 = temp101 ^ temp102;
                
                int temp = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
                // System.out.printf("temp:%d,temp0:%d, temp100: %d \n",temp,temp0,temp100);

                // System.out.printf("v[p]:%d, temp: %d \n",v[p],temp);
                v[p] = v[p] + temp;
                z = v[p];
                // System.out.printf("q:%s,p: %s, sum:%s, e:%s, y:%s, z:%s, v[p]: %s \n", Integer.toUnsignedString(q),Integer.toUnsignedString(p), Integer.toUnsignedString(sum), Integer.toUnsignedString(e), Integer.toUnsignedString(y), Integer.toUnsignedString(z),Integer.toUnsignedString(v[p]));
                // if (q < 23) {
                //     break;
                // }
            }
            // System.out.printf("[内部] q:%s,p: %s, sum:%s, e:%s, y:%s, z:%s, v:%s\n", Integer.toUnsignedString(q),Integer.toUnsignedString(p), Integer.toUnsignedString(sum), Integer.toUnsignedString(e), Integer.toUnsignedString(y), Integer.toUnsignedString(z),Arrays.toString(v));

            y = v[0];
            z = v[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
        }
        // System.out.printf("[外部] q:%d, sum:%d,  y:%d, z:%d, v:%s\n", q, sum, y, z,Arrays.toString(v));
        return v;
    }

    // Decrypt data with key.
    public static int[] decrypt(int[] v, int[] k) {
        int n = v.length - 1;
        if (n < 1) {
            return v;
        }
        if (k.length < 4) {
            int[] key = new int[4];
            System.arraycopy(k, 0, key, 0, k.length);
            k = key;
        }
        int z = v[n], y = v[0], delta = 0x9E3779B9, sum, e;
        int p, q = 6 + 52 / (n + 1);
        sum = q * delta;
        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = v[p - 1];
                y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            }
            z = v[n];
            y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
            sum = sum - delta;
        }
        return v;
    }

    // Convert byte array to int array.
    public static int[] toIntArray(byte[] data, boolean includeLength) {
        int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
        int[] result;
        if (includeLength) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }
        n = data.length;
        for (int i = 0; i < n; i++) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }
        return result;
    }

    // Convert int array to byte array
    public static byte[] toByteArray(int[] data, boolean includeLength) {
        int n;
        if (includeLength) {
            n = data[data.length - 1];
        } else {
            n = data.length << 2;
        }

        byte[] result = new byte[n];
        for (int i = 0; i < n; i++) {
            result[i] = (byte) (data[i >>> 2] >>> ((i & 3) << 3));
        }
        return result;
    }

    // 字节数组转换为hex字符串
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);

        for (int i = 0; i < bArray.length; ++i) {
            String sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    // hex字符串转换为字节数组
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        }

        char[] hex = str.toCharArray();

        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; ++i) {
            int high = Character.digit(hex[(i * 2)], 16);
            int low = Character.digit(hex[(i * 2 + 1)], 16);
            int value = high << 4 | low;
            if (value > 127)
                value -= 256;
            raw[i] = (byte) value;
        }
        return raw;
    }

    // 使用XXTea 算法加密字符串
    public static String encryptStr(String plain, String charset, String key) throws Exception {
        if (plain == null || charset == null || key == null) {
            return null;
        }
        byte[] bytes = encrypt(plain.getBytes(charset), key.getBytes(charset));

        return bytesToHexString(bytes);
    }

    // 使用XXTea 算法解密字符串
    public static String decryptStr(String hexStr, String charset, String key) throws Exception {
        if (hexStr == null || charset == null || key == null) {
            return null;
        }
        byte[] bytes = decrypt(hexToBytes(hexStr), key.getBytes(charset));

        return new String(bytes, charset);
    }
}
