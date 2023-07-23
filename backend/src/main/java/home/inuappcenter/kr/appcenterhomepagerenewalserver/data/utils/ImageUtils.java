package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];

        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }

        try {
            outputStream.close();
        } catch (Exception ignored) {

        }

        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputSteam.write(tmp, 0, count);
            }
            outputSteam.close();
        } catch (Exception ignored) {
        }
        return outputSteam.toByteArray();
    }
}
