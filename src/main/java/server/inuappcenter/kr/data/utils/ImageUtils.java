package server.inuappcenter.kr.data.utils;

import org.xerial.snappy.Snappy;
import java.io.IOException;


public class ImageUtils {

    public static byte[] compressImage(byte[] data) throws IOException {
        return Snappy.compress(data);
    }

    public static byte[] decompressImage(byte[] data) {
        try {
            return Snappy.uncompress(data);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
