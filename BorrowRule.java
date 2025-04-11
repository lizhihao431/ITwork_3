// BorrowRule.java
package com.library.management;

/**
 * 借阅规则设定模块
 * 功能：管理借阅规则配置
 */
public class BorrowRule {
    private int maxBorrow;      // 最大借阅数量
    private int borrowPeriod;   // 借阅周期（天）
    private double dailyFine;   // 每日逾期罚款
    
    public BorrowRule(int maxBorrow, int borrowPeriod, double dailyFine) {
        setRules(maxBorrow, borrowPeriod, dailyFine);
    }

    // 规则修改方法
    public void setRules(int maxBorrow, int borrowPeriod, double dailyFine) {
        if (maxBorrow <= 0 || borrowPeriod <= 0 || dailyFine < 0) {
            throw new IllegalArgumentException("非法参数值");
        }
        this.maxBorrow = maxBorrow;
        this.borrowPeriod = borrowPeriod;
        this.dailyFine = dailyFine;
    }

    // 规则验证方法
    public boolean validateRules() {
        return maxBorrow > 0 && 
               borrowPeriod > 0 && 
               dailyFine >= 0;
    }

    // 生成规则说明
    public String getRuleDescription() {
        return String.format(
            "当前借阅规则：\n" +
            "- 最大可借数量：%d本\n" +
            "- 借阅周期：%d天\n" +
            "- 逾期罚款：%.2f元/天",
            maxBorrow, borrowPeriod, dailyFine
        );
    }

    // 异常处理类
    public static class RuleException extends Exception {
        public RuleException(String message) {
            super(message);
        }
    }

    // Getter/Setter 方法
    public int getMaxBorrow() { return maxBorrow; }
    public void setMaxBorrow(int maxBorrow) { 
        if (maxBorrow > 0) this.maxBorrow = maxBorrow; 
    }

    public int getBorrowPeriod() { return borrowPeriod; }
    public void setBorrowPeriod(int borrowPeriod) { 
        if (borrowPeriod > 0) this.borrowPeriod = borrowPeriod; 
    }

    public double getDailyFine() { return dailyFine; }
    public void setDailyFine(double dailyFine) { 
        if (dailyFine >= 0) this.dailyFine = dailyFine; 
    }

    // 示例配置
    public static void main(String[] args) {
        BorrowRule rule = new BorrowRule(5, 30, 0.5);
        System.out.println(rule.getRuleDescription());
        
        try {
            rule.setRules(3, -10, 0.2);  // 触发异常
        } catch (IllegalArgumentException e) {
            System.out.println("规则修改失败：" + e.getMessage());
        }
    }
}
