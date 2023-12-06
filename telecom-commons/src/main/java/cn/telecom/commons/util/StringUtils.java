package cn.telecom.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public interface StringUtils {
    static String compress(String s) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(s.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()));
    }

    static String uncompress(String s) {
        byte[] decode = Base64.getDecoder().decode(s.getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.writeBytes(gzipInputStream.readAllBytes());
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
