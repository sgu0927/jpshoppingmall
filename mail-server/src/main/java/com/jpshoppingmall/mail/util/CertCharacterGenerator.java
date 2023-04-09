package com.jpshoppingmall.mail.util;

import java.util.Random;

public class CertCharacterGenerator {
    private static int certCharLength = 8;
    private static final char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

    public static String generate() {
        Random random = new Random(System.currentTimeMillis());
        int characterTableLength = characterTable.length;
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < certCharLength; i++) {
            buf.append(characterTable[random.nextInt(characterTableLength)]);
        }

        return buf.toString();
    }

    public int getCertCharLength() {
        return certCharLength;
    }

    public void setCertCharLength(int certCharLength) {
        CertCharacterGenerator.certCharLength = certCharLength;
    }
}
