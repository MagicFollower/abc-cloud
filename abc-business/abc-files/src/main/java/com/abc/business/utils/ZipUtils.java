package com.abc.business.utils;

import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.*;
import java.util.List;

/**
 * Zip压缩工具类
 *
 * @since 1.9
 * @author trivis
 */
public class ZipUtils {
    private ZipUtils() {
    }

    private ZipUtils(ZipParameters zp, char[] p) {
        zipParameters = zp;
        password = p;
    }

    private ZipParameters zipParameters;
    private char[] password;

    public static ZipUtils init() {
        return initStore();
    }

    public static ZipUtils init(String password){
        return initFull(CompressionMethod.STORE, true,
                EncryptionMethod.ZIP_STANDARD, null, password);
    }

    public static ZipUtils initStore() {
        return initFull(CompressionMethod.STORE, false,
                null, null, null);
    }

    public static ZipUtils initDeflate() {
        return initFull(CompressionMethod.DEFLATE, false,
                null, null, null);
    }

    public static ZipUtils initFull(CompressionMethod compressionMethod, boolean encrypt,
                                    EncryptionMethod encryptionMethod, AesKeyStrength aesKeyStrength, String password) {
        if (encrypt && password == null) {
            throw new IllegalArgumentException("encrypt is true, but you not set password!");
        }
        return new ZipUtils(buildZipParameters(compressionMethod, encrypt, encryptionMethod, aesKeyStrength),
                password != null ? password.toCharArray() : null);
    }

    public void to(OutputStream outputStream, List<File> fileList) throws IOException {
        try (ZipOutputStream zos = initializeZipOutputStream(outputStream, zipParameters.isEncryptFiles(), password)) {
            for (File fileToAdd : fileList) {

                // Entry size has to be set if you want to add entries of STORE compression method (no compression)
                // This is not required for deflate compression
                if (zipParameters.getCompressionMethod() == CompressionMethod.STORE) {
                    zipParameters.setEntrySize(fileToAdd.length());
                }

                zipParameters.setFileNameInZip(fileToAdd.getName());
                zos.putNextEntry(zipParameters);

                try (InputStream inputStream = new FileInputStream(fileToAdd)) {
                    inputStream.transferTo(zos);
                }
                zos.closeEntry();
            }
        }
    }

    public long to(File outputZipFile, List<File> fileList) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputZipFile)) {
            to(fos, fileList);
        }
        return outputZipFile.length();
    }

    /**
     * 使用OutputStream初始化ZipOutputStream
     *
     * @param os
     * @param encrypt
     * @param password
     * @return
     * @throws IOException
     */
    private static ZipOutputStream initializeZipOutputStream(OutputStream os, boolean encrypt, char[] password)
            throws IOException {
        if (encrypt) {
            return new ZipOutputStream(os, password);
        }
        return new ZipOutputStream(os);
    }

    /**
     * 构建zip参数
     * 1. AesKeyStrength在EncryptionMethod.AES时生效，EncryptionMethod为其他（ZIP_STANDARD）时，该参数设置为null即可
     *
     * @param compressionMethod
     * @param encrypt
     * @param encryptionMethod
     * @param aesKeyStrength
     * @return
     */
    private static ZipParameters buildZipParameters(CompressionMethod compressionMethod, boolean encrypt,
                                                    EncryptionMethod encryptionMethod, AesKeyStrength aesKeyStrength) {
        compressionMethod = compressionMethod != null ? compressionMethod : CompressionMethod.STORE;
        // encrypt=true时，EncryptionMethod.NONE将自动替换为EncryptionMethod.ZIP_STANDARD
        if (encryptionMethod == null || EncryptionMethod.NONE.equals(encryptionMethod)) {
            if (encrypt) {
                encryptionMethod = EncryptionMethod.ZIP_STANDARD;
            } else {
                encryptionMethod = EncryptionMethod.NONE;
            }
        }
        aesKeyStrength = aesKeyStrength != null ? aesKeyStrength : AesKeyStrength.KEY_STRENGTH_128;
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(compressionMethod);
        zipParameters.setEncryptFiles(encrypt);
        zipParameters.setEncryptionMethod(encryptionMethod);
        zipParameters.setAesKeyStrength(aesKeyStrength);
        return zipParameters;
    }
}
