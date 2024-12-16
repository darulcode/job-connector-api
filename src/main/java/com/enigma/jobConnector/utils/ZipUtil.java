package com.enigma.jobConnector.utils;

import java.io.*;
import java.util.Map;
import java.util.zip.*;

public class ZipUtil {

    /**
     * Membuat file ZIP di memori.
     *
     * @param zipEntries Daftar pasangan nama file dan konten file (dalam byte[]).
     * @return Byte array dari file ZIP yang dibuat.
     * @throws IOException Jika ada error saat membuat ZIP.
     */
    public static byte[] createZip(Map<String, byte[]> zipEntries) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (Map.Entry<String, byte[]> entry : zipEntries.entrySet()) {
                String fileName = entry.getKey();
                byte[] fileContent = entry.getValue();

                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                zos.write(fileContent);
                zos.closeEntry();
            }

            zos.finish();
            return baos.toByteArray();
        }
    }
}

