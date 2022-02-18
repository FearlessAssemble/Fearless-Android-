package net.fearlessplus.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JP16785 on 16/08/05.
 */
public class MD5 {
    public static String getHash(String msg) {
        MessageDigest m = null;
        String hash = msg;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(msg.getBytes(), 0, msg.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }
}
