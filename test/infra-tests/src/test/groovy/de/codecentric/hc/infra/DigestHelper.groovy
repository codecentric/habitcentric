package de.codecentric.hc.infra

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.zip.CRC32

public class DigestHelper {

    public static String generateUniqueString(String input) {
        def uuid = UUID.randomUUID()
        uuid.toString();
    }
}
