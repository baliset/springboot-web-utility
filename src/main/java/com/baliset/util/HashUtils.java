package com.baliset.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashUtils
{

    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
        }
        return sb.toString();
    }

    public static String strToMD5HashStr(String cleartext)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(cleartext.getBytes());
            byte[] md5AsBytes = md.digest();
            return bytesToHex(md5AsBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot perform MD5 hash", e);
        }
    }
}