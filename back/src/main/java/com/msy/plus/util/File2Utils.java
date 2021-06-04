package com.msy.plus.util;


import com.msy.plus.core.constant.ConfigConstants;
import com.msy.plus.core.key.GeneratorId;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;

public class File2Utils {
    private static final Logger logger = LogManager.getLogger(File2Utils.class.getName());

    private static String uploadPath;

    @Value("${file.local-path}")
    public void setUploadPath(String uploadPath) {
        File2Utils.uploadPath = uploadPath;
    }

    /**
     * 获取文件上传根路径
     *
     * @return
     * @throws Exception
     */
    private static String getRootDir() throws Exception {
        String rootDir = uploadPath;
        if (StringUtils.isBlank(rootDir)) {
            logger.debug("文件系统路径初始化失败");
            throw new Exception("文件系统路径初始化失败");
        }
        return rootDir;
    }

    /**
     * 获取禁用文件后缀列表
     *
     * @return
     * @throws Exception
     */
    private static String[] getDisabledFileExtensions() throws Exception {
        String disabledFileExtensions = Config.get(ConfigConstants.DISABLED_FILE_EXTENSIONS);
        if (StringUtils.isBlank(disabledFileExtensions)) {
            logger.debug("禁用文件后缀列表初始化失败");
            throw new Exception("禁用文件后缀列表初始化失败");
        }
        return disabledFileExtensions.split(",");
    }

    /**
     * 清空目录
     *
     * @param filePath
     */
    public static void clearDir(String filePath) {
        File deletefile = new File(filePath);
        if (deletefile.exists()) {
            if (deletefile.isDirectory()) {
                File[] files = deletefile.listFiles();
                for (File file : files) {
                    clearDir(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        file.delete();
                    }
                }
            } else {
                deletefile.delete();
            }
        }
    }

    /**
     * 将内容写入文件
     *
     * @param filePath
     * @param bytes
     * @throws Exception
     */
    public static void writeByteArrayToFile(String filePath, byte[] bytes) throws Exception {
        logger.info("写文件[" + filePath + "]");
        if (createFile(filePath)) {
            FileOutputStream fileWriter = null;
            try {
                fileWriter = new FileOutputStream(filePath);
                fileWriter.write(bytes);
            } catch (Exception e) {
                throw e;
            } finally {
                fileWriter.close();
            }
        } else {
            logger.error("生成失败，文件已存在！");
        }
    }

    /**
     * 将内容写入文件
     *
     * @param content
     * @param filePath
     * @throws Exception
     */
    public static void writeFile(String content, String filePath) throws Exception {
        logger.info("写文件[" + filePath + "]");
        if (createFile(filePath)) {
            FileWriterWithEncoding fileWriter = null;
            BufferedWriter bufferedWriter = null;
            try {
                fileWriter = new FileWriterWithEncoding(filePath, "utf-8", false);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(content);
            } catch (Exception e) {
                throw e;
            } finally {
                bufferedWriter.close();
                fileWriter.close();
            }
        } else {
            logger.info("生成失败，文件已存在！");
        }
    }

    /**
     * 将内容读取出来
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String readFileToString(String filePath) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));

            String tmp = "";
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    logger.debug("资源释放异常");
                }
            }
        }
        return sb.toString();
    }

    /**
     * 创建单个文件
     *
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            // file.delete();
            // logger.debug("文件 " + descFileName + " 已存在!");
            return true;
        }
        if (descFileName.endsWith("/")) {
            logger.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                logger.debug("创建文件所在的目录失败!");
                return false;
            }
        }

        // 创建文件
        try {
            if (file.createNewFile()) {
                // logger.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                logger.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            logger.debug(descFileName + " 文件创建失败!");
            return false;
        }

    }

    /**
     * 拷贝文件
     *
     * @param src
     * @param des
     * @throws Exception
     */
    public static void copyFile(File src, File des) throws Exception {
        int bytesum = 0;
        int byteread = 0;
        if (src.exists()) {
            // 文件存在时
            InputStream is = null;
            FileOutputStream os = null;
            try {
                is = new FileInputStream(src);
                // 读入原文件
                os = new FileOutputStream(des);
                byte[] buffer = new byte[1024];
                while ((byteread = is.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println("读取进度：" + bytesum / src.length() + "%");
                    os.write(buffer, 0, byteread);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcPath
     * @param desPath
     * @throws Exception
     */
    public static void copyFile(String srcPath, String desPath) throws Exception {
        File src = new File(srcPath);
        File des = new File(desPath);
        copyFile(src, des);
    }

    /**
     * 检测文件是否为禁传文件 true表示是禁传文件
     *
     * @param fileName 文件名或文件的路径名
     * @return
     * @throws Exception
     */
    private static boolean isDisabledFile(String fileName) throws Exception {
        // 健壮性判断
        if (StringUtils.isBlank(fileName)) {
            logger.debug("文件名或文件路径名为空");
            throw new Exception("文件名或文件路径名为空");
        }
        for (String disabledExtension : getDisabledFileExtensions()) {
            if (fileName.endsWith(disabledExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化相对路径 请不要使用该方法处理绝对路径
     *
     * @param path 相对路径
     * @return 返回不以"/"开头结尾的路径
     * @throws Exception
     */
    private static String formatPath(String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            logger.debug("传入的相对路径为空");
            throw new Exception("传入的相对路径为空");
        }
        while (path.trim().startsWith("/") && path.trim().length() > 1) {
            path = path.trim().substring(1);
        }
        while (path.trim().endsWith("/") && path.trim().length() > 1) {
            path = path.trim().substring(0, path.trim().length() - 1);
        }
        return path;
    }

    /**
     * 发号器生成字符串
     *
     * @return
     */
    private static String generate() {
        return GeneratorId.getInstance().nextId() + "";
    }

    /**
     * 判断file为文件
     *
     * @param file
     * @return true表示是文件
     */
    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 判断multipartFile是否为空
     *
     * @param file
     * @return
     */
    public static boolean isEmpty(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    /**
     * 判断multipartFile是否不为空
     *
     * @param file
     * @return
     */
    public static boolean isNotEmpty(MultipartFile file) {
        return !isEmpty(file);
    }

    /**
     * 判断file不为文件，可能file并不存在
     *
     * @param file
     * @return true表示不为文件
     */
    public static boolean isNotFile(File file) {
        return !isFile(file);
    }

    /**
     * 判断file为目录
     *
     * @param file
     * @return true表示是目录
     */
    public static boolean isDirectory(File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 判断file不为目录，可能file并不存在
     *
     * @param file
     * @return true表示不是目录
     */
    public static boolean isNotDirectory(File file) {
        return !isDirectory(file);
    }

    /**
     * 上传文件
     *
     * @param source 文件
     * @param path   相对路径
     * @return 落地文件的文件名
     * @throws Exception
     */
    public static String upload(File source, String path) throws Exception {
        // 健壮性判断
        if (isFile(source)) {
            logger.debug("待传文件异常");
            throw new Exception("待传文件异常");
        }
        // 格式化相对路径
        path = formatPath(path);
        // 获取文件名
        String fileName = source.getName();
        if (isDisabledFile(fileName)) {
            logger.debug("禁止上传的文件类型");
            throw new Exception("禁止上传的文件类型");
        }
        // 转为输入流
        InputStream in = null;
        try {
            in = new FileInputStream(source);
        } catch (FileNotFoundException e) {
            logger.debug("文件转输入流失败");
            throw new Exception("文件转输入流失败");
        }
        return upload(in, fileName, path);
    }

    /**
     * 上传文件
     *
     * @param source   文件
     * @param fileName 文件名
     * @param path     相对路径
     * @return
     * @throws Exception
     */
    public static String upload(InputStream source, String fileName, String path) throws Exception {
        // 健壮性判断
        if (source == null) {
            logger.debug("输入流为空");
            throw new Exception("输入流为空");
        }
        if (StringUtils.isBlank(fileName)) {
            logger.debug("文件名为空");
            throw new Exception("文件名为空");
        }
        // 格式化相对路径
        path = formatPath(path);
        if (isDisabledFile(fileName)) {
            logger.debug("禁止上传的文件类型");
            throw new Exception("禁止上传的文件类型");
        }
        // 获取文件后缀名（包括点）
        String extensions = "";
        try {
            extensions = fileName.substring(fileName.lastIndexOf("."));
        } catch (Exception e) {
            // 无后缀名
            logger.debug("上传文件无后缀名");
        }
        // 发号器生成随机文件名
        String generatePath = generate();
        // 落地文件名
        String finalFileName = generatePath + extensions;
        File file = new File(getRootDir() + path + "/" + finalFileName);

        // 创建落地文件--如果创建失败
        if (!createFile(file.getAbsolutePath())) {
            logger.debug("文件创建失败");
            throw new Exception("文件创建失败");
        }
        // 文件拷贝
        try {
            IOUtils.copy(source, new FileOutputStream(file));
        } catch (Exception e) {
            logger.debug("文件拷贝异常");
            throw new Exception("文件拷贝异常");
        }

        return finalFileName;
    }

    /**
     * 文件下载
     *
     * @param path     相对路径
     * @param fileName 文件名
     * @return
     * @throws Exception
     */
    public static OutputStream download(String path, String fileName) throws Exception {
        // 健壮性判断
        // 格式化相对路径
        path = formatPath(path);
        if (StringUtils.isBlank(fileName)) {
            logger.debug("文件名为空");
            throw new Exception("文件名为空");
        }
        return download(path.trim() + "/" + fileName);
    }

    /**
     * 文件下载
     *
     * @param relativePath 相对路径，带文件名
     * @return
     * @throws Exception
     */
    public static OutputStream download(String relativePath) throws Exception {
        // 相对路径判断
        // 格式化相对路径
        relativePath = formatPath(relativePath);
        File source = new File(getRootDir() + relativePath);
        // 健壮性判断
        if (isNotFile(source)) {
            logger.debug("文件不存在");
            throw new Exception("文件不存在");
        }
        // 将文件转为流返回
        try {
            return new FileOutputStream(source);
        } catch (FileNotFoundException e) {
            logger.debug("文件转为流失败");
            throw new Exception("文件转为流失败");
        }
    }

    /**
     * File base64 encode
     *
     * @param file
     * @return
     */
    public static String base64Encode(File file) {
        if (file == null || !file.isFile() || file.length() == 0) {
            return null;
        }
        FileInputStream fileStream = null;
        byte[] buffer = new byte[(int) file.length()];
        try {
            fileStream = new FileInputStream(file);
            fileStream.read(buffer);
        } catch (IOException e) {
            logger.error("file base64Encode error" + e.getMessage());
        }
        IOUtils.closeQuietly(fileStream);
        return new sun.misc.BASE64Encoder().encode(buffer);
    }

    /**
     * MultipartFile base64 encode
     *
     * @param file
     * @return
     */
    public static String base64Encode(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        byte[] buffer = null;
        try {
            buffer = file.getBytes();
        } catch (IOException e) {
            logger.error("multipartFile base64Encode error" + e.getMessage());
        }
        return buffer == null ? null : new sun.misc.BASE64Encoder().encode(buffer);
    }


    /**
     * File base64 encode
     *
     * @param file
     * @return
     */
    public static byte[] file2Byte(File file) {
        if (file == null || !file.isFile() || file.length() == 0) {
            return null;
        }
        FileInputStream fileStream = null;
        byte[] buffer = new byte[(int) file.length()];
        try {
            fileStream = new FileInputStream(file);
            fileStream.read(buffer);
        } catch (IOException e) {
            logger.error("file base64Encode error" + e.getMessage());
        }
        IOUtils.closeQuietly(fileStream);
        return buffer;
    }

    /**
     * base64Decode
     *
     * @param base64
     * @return
     */
    public static byte[] base64Decode(String base64) {
        if (org.apache.commons.lang3.StringUtils.isBlank(base64)) {
            return null;
        }
        try {
            return new sun.misc.BASE64Decoder().decodeBuffer(base64);
        } catch (IOException e) {
            logger.error("base64Decode error" + e.getMessage());
        }
        return null;
    }

    private static byte[] createChecksum(String fileFullName) throws Exception {
        InputStream fis =  new FileInputStream(fileFullName);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");// "SHA1"或"MD5"
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    public static String getMD5Checksum(String fileFullName) throws Exception {
        if (null == fileFullName){
            return null;
        }
        byte[] b = createChecksum(fileFullName);
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
}
