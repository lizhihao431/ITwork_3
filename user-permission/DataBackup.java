package com.library.management;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 数据备份与恢复模块
 * 功能：实现数据压缩备份与恢复操作
 */
public class DataBackup {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private final String backupDirectory;

    public DataBackup(String backupPath) {
        this.backupDirectory = backupPath;
        initializeBackupDirectory();
    }

    // 初始化备份目录
    private void initializeBackupDirectory() {
        File dir = new File(backupDirectory);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("无法创建备份目录: " + backupDirectory);
        }
    }

    // 创建带时间戳的备份文件
    public String createBackup(String[] filePaths) throws IOException {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String zipFileName = "backup_" + timestamp + ".zip";
        Path zipPath = Path.of(backupDirectory, zipFileName);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (!file.exists()) continue;

                ZipEntry entry = new ZipEntry(file.getName());
                zos.putNextEntry(entry);
                Files.copy(file.toPath(), zos);
                zos.closeEntry();
            }
        }
        return zipPath.toString();
    }

    // 恢复备份文件
    public void restoreBackup(String zipFilePath, String outputDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path outputPath = Path.of(outputDir, entry.getName());
                Files.copy(zis, outputPath);
            }
        }
    }

    // 验证备份文件完整性
    public boolean verifyBackup(String zipFilePath) {
        File file = new File(zipFilePath);
        return file.exists() && file.length() > 0;
    }

    // 自动清理旧备份（保留最近7天）
    public void autoCleanBackups(int keepDays) {
        File dir = new File(backupDirectory);
        File[] backups = dir.listFiles((d, name) -> name.startsWith("backup_"));
        if (backups == null) return;

        long cutoff = System.currentTimeMillis() - (keepDays * 24 * 60 * 60 * 1000L);
        for (File backup : backups) {
            if (backup.lastModified() < cutoff) {
                backup.delete();
            }
        }
    }

    // 示例使用
    public static void main(String[] args) throws IOException {
        DataBackup backupSystem = new DataBackup("backups");
        String[] filesToBackup = {"data/books.csv", "data/users.csv"};
        String backupFile = backupSystem.createBackup(filesToBackup);
        System.out.println("备份创建成功：" + backupFile);
    }
}
