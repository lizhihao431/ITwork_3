// BorrowManagement.java
package com.library.management;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 借阅管理模块
 * 功能：处理借书、还书、查询记录、逾期计算等操作
 */
public class BorrowManagement {
    private HashMap<Integer, BorrowRecord> borrowRecords;
    private BorrowRule borrowRule;

    public BorrowManagement(BorrowRule borrowRule) {
        this.borrowRecords = new HashMap<>();
        this.borrowRule = borrowRule;
    }

    // 借书操作
    public boolean borrowBook(int readerId, int bookId, Date borrowDate) {
        if (getCurrentBorrowCount(readerId) >= borrowRule.getMaxBorrow()) {
            System.out.println("借阅失败：超过最大可借数量");
            return false;
        }
        
        BorrowRecord record = new BorrowRecord(readerId, bookId, borrowDate);
        borrowRecords.put(record.getRecordId(), record);
        System.out.println("借阅成功，记录ID：" + record.getRecordId());
        return true;
    }

    // 还书操作
    public boolean returnBook(int recordId, Date returnDate) {
        BorrowRecord record = borrowRecords.get(recordId);
        if (record == null) {
            System.out.println("还书失败：借阅记录不存在");
            return false;
        }
        
        record.setReturnDate(returnDate);
        calculateOverdue(record);
        return true;
    }

    // 计算逾期天数和罚款
    private void calculateOverdue(BorrowRecord record) {
        long diff = record.getReturnDate().getTime() - record.getDueDate().getTime();
        int overdueDays = (int) (diff / (1000 * 60 * 60 * 24));
        
        if (overdueDays > 0) {
            double fine = overdueDays * borrowRule.getDailyFine();
            record.setFineAmount(fine);
            System.out.println("逾期" + overdueDays + "天，需缴纳罚款：" + fine);
        }
    }

    // 获取当前借阅数量
    private int getCurrentBorrowCount(int readerId) {
        int count = 0;
        for (BorrowRecord record : borrowRecords.values()) {
            if (record.getReaderId() == readerId && !record.isReturned()) {
                count++;
            }
        }
        return count;
    }

    // 获取读者所有借阅记录
    public List<BorrowRecord> getReaderRecords(int readerId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord record : borrowRecords.values()) {
            if (record.getReaderId() == readerId) {
                result.add(record);
            }
        }
        return result;
    }

    // 内部借阅记录类
    private class BorrowRecord {
        private static int nextId = 1;
        private final int recordId;
        private final int readerId;
        private final int bookId;
        private final Date borrowDate;
        private Date dueDate;
        private Date returnDate;
        private double fineAmount;

        public BorrowRecord(int readerId, int bookId, Date borrowDate) {
            this.recordId = nextId++;
            this.readerId = readerId;
            this.bookId = bookId;
            this.borrowDate = borrowDate;
            this.dueDate = new Date(borrowDate.getTime() + 
                (long) borrowRule.getBorrowPeriod() * 24 * 60 * 60 * 1000);
        }

        // Getter/Setter 方法
        public int getRecordId() { return recordId; }
        public int getReaderId() { return readerId; }
        public int getBookId() { return bookId; }
        public Date getDueDate() { return dueDate; }
        public Date getReturnDate() { return returnDate; }
        public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
        public double getFineAmount() { return fineAmount; }
        public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }
        public boolean isReturned() { return returnDate != null; }
    }
}
