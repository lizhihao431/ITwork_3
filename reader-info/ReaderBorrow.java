package com.library.management;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 读者借阅管理模块
 * 功能：处理读者视角的借阅操作
 */
public class ReaderBorrow {
    private final BorrowManagement borrowSystem;
    private final ReaderInfo readerSystem;

    public ReaderBorrow(BorrowManagement borrowSystem, ReaderInfo readerSystem) {
        this.borrowSystem = borrowSystem;
        this.readerSystem = readerSystem;
    }

    // 读者借书请求
    public boolean requestBorrow(int readerId, int bookId) {
        if (!validateReader(readerId)) {
            System.out.println("借阅失败：读者信息无效");
            return false;
        }
        return borrowSystem.borrowBook(readerId, bookId, new java.util.Date());
    }

    // 读者还书请求
    public boolean requestReturn(int recordId) {
        return borrowSystem.returnBook(recordId, new java.util.Date());
    }

    // 获取读者所有借阅记录
    public List<BorrowManagement.BorrowRecord> getBorrowHistory(int readerId) {
        return borrowSystem.getReaderRecords(readerId);
    }

    // 获取当前借阅中的书籍
    public List<BorrowManagement.BorrowRecord> getCurrentBorrows(int readerId) {
        return borrowSystem.getReaderRecords(readerId).stream()
            .filter(record -> !record.isReturned())
            .collect(Collectors.toList());
    }

    // 验证读者有效性
    private boolean validateReader(int readerId) {
        return readerSystem.getReader(readerId) != null;
    }

    // 生成借阅报告
    public String generateBorrowReport(int readerId) {
        StringBuilder report = new StringBuilder();
        ReaderInfo.Reader reader = readerSystem.getReader(readerId);
        if (reader == null) return "读者不存在";

        report.append("读者借阅报告\n")
              .append(reader.toString()).append("\n\n")
              .append("当前借阅中：\n");
        
        getCurrentBorrows(readerId).forEach(record -> 
            report.append("- 图书ID：").append(record.getBookId())
                 .append(" | 应还日期：").append(record.getDueDate())
                 .append("\n"));

        report.append("\n历史记录：\n");
        getBorrowHistory(readerId).forEach(record -> 
            report.append("- 图书ID：").append(record.getBookId())
                 .append(" | 状态：").append(record.isReturned() ? "已还" : "未还")
                 .append("\n"));

        return report.toString();
    }

    // 示例使用
    public static void main(String[] args) {
        BorrowRule rule = new BorrowRule(5, 30, 0.5);
        BorrowManagement borrowMgmt = new BorrowManagement(rule);
        ReaderInfo readerInfo = new ReaderInfo();
        ReaderBorrow service = new ReaderBorrow(borrowMgmt, readerInfo);

        readerInfo.addReader("王五", "wangwu@lib.com");
        service.requestBorrow(1000, 5001);
        System.out.println(service.generateBorrowReport(1000));
    }
}
