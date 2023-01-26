package com.tsp.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    public static final int KEY_SIZE = 1024;
    public static PrivateKey privatekey;
    private static Logger logger;

    public static String makeSHA256(final String param) {
        final String plainText = nullStrToStr(param);
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes());
            final byte[] byteData = md.digest();
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; ++i) {
                sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
            }
            final StringBuffer hexString = new StringBuffer();
            for (int j = 0; j < byteData.length; ++j) {
                final String hex = Integer.toHexString(0xFF & byteData[j]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String nullStrToStr(final String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static Map<String, Object> makeRSA() throws Exception {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        final KeyPair keyPair = generator.genKeyPair();
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PublicKey publicKey = keyPair.getPublic();
        final PrivateKey privateKey = keyPair.getPrivate();
        final RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        final RSAPrivateKeySpec privateSpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        final String publicKeyModulus = publicSpec.getModulus().toString(16);
        final String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
        final String privateKeyModulus = privateSpec.getModulus().toString(16);
        final String privateKeyExponent = privateSpec.getPrivateExponent().toString(16);
        StringUtils.logger.debug("publicKeyModulus : {}", (Object)publicKeyModulus);
        StringUtils.logger.debug("publicKeyExponent : {}", (Object)publicKeyExponent);
        StringUtils.logger.debug("privateKeyModulus : {}", (Object)privateKeyModulus);
        StringUtils.logger.debug("privateKeyExponent : {}", (Object)privateKeyExponent);
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("publicKeyModulus", publicKeyModulus);
        map.put("publicKeyExponent", publicKeyExponent);
        map.put("privateKey", privateKey);
        return map;
    }

    public static String decryptRSA(final PrivateKey privateKey, final String encValue) throws Exception {
        StringUtils.logger.debug("will decrypt : {}", (Object)encValue);
        final Cipher cipher = Cipher.getInstance("RSA");
        final byte[] encryptedBytes = hexToByteArray(encValue);
        cipher.init(2, privateKey);
        final byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        final String decryptedValue = new String(decryptedBytes, "utf-8");
        return decryptedValue;
    }

    public static byte[] hexToByteArray(final String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[0];
        }
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            final byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int)Math.floor(i / 2)] = value;
        }
        return bytes;
    }

    public static String[] parseStringByBytes(final String raw, final int len, final String encoding) {
        if (raw == null) {
            return null;
        }
        String[] ary = null;
        try {
            final byte[] rawBytes = raw.getBytes(encoding);
            final int rawLength = rawBytes.length;
            int index = 0;
            int minus_byte_num = 0;
            int offset = 0;
            final int kr_byte_num = "UTF-8".equals(encoding) ? 3 : 2;
            if (rawLength > len) {
                final int aryLength = rawLength / len + ((rawLength % len != 0) ? 1 : 0);
                ary = new String[aryLength];
                for (int i = 0; i < aryLength; ++i) {
                    minus_byte_num = 0;
                    offset = len;
                    if (index + offset > rawBytes.length) {
                        offset = rawBytes.length - index;
                    }
                    for (int j = 0; j < offset; ++j) {
                        if ((rawBytes[index + j] & 0x80) != 0x0) {
                            ++minus_byte_num;
                        }
                    }
                    if (minus_byte_num % kr_byte_num != 0) {
                        offset -= minus_byte_num % kr_byte_num;
                    }
                    ary[i] = new String(rawBytes, index, offset, encoding);
                    index += offset;
                }
            }
            else {
                ary = new String[] { raw };
            }
        }
        catch (Exception ex) {}
        return ary;
    }

    public static int getBytesLength(final String str) {
        int strLength = 0;
        final char[] tmpChar = new char[str.length()];
        for (int i = 0; i < tmpChar.length; ++i) {
            tmpChar[i] = str.charAt(i);
            if (tmpChar[i] < '\u0080') {
                ++strLength;
            }
            else {
                strLength += 2;
            }
        }
        return strLength;
    }

    static {
        StringUtils.logger = LoggerFactory.getLogger((Class)StringUtils.class);
    }
}
