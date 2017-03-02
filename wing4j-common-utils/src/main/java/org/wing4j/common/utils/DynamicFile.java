package org.wing4j.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by wing4j on 2017/3/2.
 * 动态文件对象,构建了一个具有事务特性的文件抽象.
 * 通过开始事务的方式创建临时文件，通过提交和回滚来实现文件变更，通过无阻塞式的方式读取最新副本文件，即使在读取文件时也能无阻塞写入文件
 */
public final class DynamicFile {
    String pathName;
    String fileName;
    int backupSize;
    int commitTimeoutSec;
    /**
     * 默认备份数10
     */
    public static final int DEFAULT_BACKUP_SIZE = 10;
    /**
     * 默认提交超时间秒数10秒
     */
    public static final int DEFAULT_COMMIT_TIMEOUT_SEC = 10;

    /**
     * 不存在需要提交或者回滚的文件异常
     */
    public static class NotExistsCommitOrRollbackFileException extends IOException {
        public NotExistsCommitOrRollbackFileException(String message) {
            super(message);
        }
    }

    public static class TimeoutException extends IOException {
        public TimeoutException(String message) {
            super(message);
        }
    }

    public static class NotSuchFieldException extends IOException {
        public NotSuchFieldException(String message) {
            super(message);
        }
    }


    /**
     * 构建一个指定的文件，保存备份数默认值
     *
     * @param pathname 文件路径
     */
    public DynamicFile(String pathname) {
        this(pathname, DEFAULT_BACKUP_SIZE, DEFAULT_COMMIT_TIMEOUT_SEC);
    }

    /**
     * 构建一个指定的文件，保存备份数
     *
     * @param pathname   文件路径
     * @param backupSize 备份数
     */
    public DynamicFile(String pathname, int backupSize, int commitTimeoutSec) {
        File file = new File(pathname);
        this.pathName = file.getParent();
        this.fileName = file.getName();
        this.backupSize = backupSize;
        this.commitTimeoutSec = commitTimeoutSec;
    }

    /**
     * 构建一个指定的文件，保存备份数默认值
     *
     * @param dir      文件路径
     * @param fileName 文件名
     */
    public DynamicFile(File dir, String fileName) {
        this(dir, fileName, DEFAULT_BACKUP_SIZE, DEFAULT_COMMIT_TIMEOUT_SEC);
    }

    /**
     * 构建一个指定的文件，保存备份数
     *
     * @param dir              文件路径
     * @param fileName         文件名
     * @param backupSize       备份数
     * @param commitTimeoutSec 提交超时时间
     */
    public DynamicFile(File dir, String fileName, int backupSize, int commitTimeoutSec) {
        this.pathName = dir.getAbsolutePath();
        this.fileName = fileName;
        this.backupSize = backupSize;
        this.commitTimeoutSec = commitTimeoutSec;
    }

    /**
     * 检查文件是否存在
     * @return 存在返回真
     * @throws IOException 异常
     */
    public boolean exists() throws IOException {
        File dir = new File(pathName, fileName);
        if (!dir.exists()) {
            return false;
        }
        File real = getRealFile(false);
        if (real == null) {
            return false;
        }
        return true;
    }

    /**
     * 创建文件目录
     */
    public void mkdirs() {
        File dir = new File(pathName, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 保存文件内容
     *
     * @param data 内容
     * @return 真实文件对象
     * @throws IOException 异常
     */
    public File save(byte[] data) throws IOException {
        File tempFile = begin();
        try {
            FileUtils.writeByteArrayToFile(tempFile, data);
            return commit();
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    /**
     * 打开文件
     *
     * @return 输入流
     * @throws IOException 异常
     */
    public InputStream read() throws IOException {
        File realFile = getRealFile();
        byte[] data = FileUtils.readFileToByteArray(realFile);
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        return is;
    }

    /**
     * 回滚临时区的变更
     *
     * @throws IOException 异常
     */
    public void rollback() throws IOException {
        File dir = new File(pathName, fileName);
        if (!dir.exists()) {
            throw new FileNotFoundException(MessageFormatter.format("not exists dir '{}'", dir));
        }
        File file = new File(dir, fileName + "." + "0");
        if (!file.exists()) {
            throw new NotExistsCommitOrRollbackFileException(MessageFormatter.format("not exists temp file '{}'", file));
        }
        FileUtils.forceDelete(file);
        getRealFile(false);
    }

    /**
     * 提交临时区的文件变更
     *
     * @return 实际文件对象
     * @throws IOException 异常
     */
    public File commit() throws IOException {
        File dir = new File(pathName, fileName);
        if (!dir.exists()) {
            throw new FileNotFoundException(MessageFormatter.format("not exists dir '{}'", dir));
        }
        File tempFile = new File(dir, fileName + "." + "0");
        if (!tempFile.exists()) {
            throw new NotExistsCommitOrRollbackFileException(MessageFormatter.format("not exists temp file '{}'", tempFile));
        }
        File lateFile = new File(dir, fileName + "." + DateUtils.toString(new Date(), DateStyle.FILE_FORMAT2));
        long beginTime = System.currentTimeMillis();
        while (lateFile.exists()) {
            //小于
            if (System.currentTimeMillis() - beginTime < 10 * 1000) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //DO NOTHING
                }
                lateFile = new File(dir, fileName + "." + DateUtils.toString(new Date(), DateStyle.FILE_FORMAT2));
            } else {
                throw new TimeoutException(MessageFormatter.format("commit temp file '{}' happens timeout", tempFile));
            }
        }
        FileUtils.copyFile(tempFile, lateFile);
        FileUtils.forceDelete(tempFile);
        return getRealFile(false);
    }

    /**
     * 开始临时文件
     *
     * @return 临时文件
     * @throws IOException 异常
     */
    public File begin() throws IOException {
        File dir = new File(pathName, fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, fileName + "." + "0");
        if (file.exists()) {
            throw new FileAlreadyExistsException(MessageFormatter.format("temp file '{}' already exists!", file));
        }
        file.createNewFile();
        return file;
    }

    public File getRealFile() throws IOException {
        return getRealFile(true);
    }

    /**
     * 获取实际文件
     *
     * @return 实际文件
     * @throws IOException 异常
     */
    File getRealFile(boolean throwExp) throws IOException {
        File dir = new File(pathName, fileName);
        if (!dir.exists()) {
            if (throwExp) {
                throw new NotDirectoryException(MessageFormatter.format("not exists dir '{}'", dir));
            } else {
                return null;
            }
        }
        if (dir.isFile()) {
            if (throwExp) {
                throw new NotSuchFieldException(MessageFormatter.format("not exists dir '{}'", dir));
            } else {
                return null;
            }
        }
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                int dotIdx = name.lastIndexOf(".");
                String time = name.substring(dotIdx + 1);
                long time0 = 0L;
                try {
                    time0 = Long.parseLong(time);
                } catch (Exception e) {
                    return false;
                }
                if (time0 < 10) {
                    return false;
                }
                return true;
            }
        });
        long[] filesDate = new long[files.length];
        int idx = 0;
        for (File file : files) {
            String fileName0 = file.getName();
            int dotIdx = fileName0.lastIndexOf(".");
            if (dotIdx >= fileName.length()) {
                String time = fileName0.substring(dotIdx + 1);
                long time0 = Long.parseLong(time);
                if (time0 > 10) {
                    filesDate[idx] = time0;
                    idx++;
                } else {
                    //这种情况是临时文件
                }
            } else {
                //不含后缀
            }
        }
        if (idx < 1) {
            if (throwExp) {
                throw new NotSuchFieldException(MessageFormatter.format("not exists such file '{}'", dir));
            } else {
                return null;
            }
        }
        Arrays.sort(filesDate);
        long[] finalDates = Arrays.copyOf(filesDate, idx);
        for (int i = 0; i < finalDates.length; i++) {
            if (i < backupSize) {
            } else {
                File deleteFile = new File(dir, fileName + "." + finalDates[i]);
                FileUtils.forceDelete(deleteFile);
            }
        }
        String lateUpdateFile = fileName + "." + finalDates[0];
        return new File(dir, lateUpdateFile);
    }
}
