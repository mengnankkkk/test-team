package com.mengnnakk.utility;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;

public class QRCodeUtils {

    public static byte[] generateQRCode(String content, int width, int height) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
        return stream.toByteArray();
    }
}
