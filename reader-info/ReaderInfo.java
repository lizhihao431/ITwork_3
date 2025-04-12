package com.library.management;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 读者信息管理模块
 * 功能：管理读者信息的CRUD操作
 */
public class ReaderInfo {
    private final List<Reader> readers = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1000);

    // 添加读者
    public void addReader(String name, String contact) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("读者姓名不能为空");
        }
        readers.add(new Reader(idGenerator.getAndIncrement(), name, contact));
    }

    // 删除读者
    public boolean removeReader(int readerId) {
        return readers.removeIf(reader -> reader.getId() == readerId);
    }

    // 更新读者信息
    public boolean updateReader(int readerId, String newContact) {
        return readers.stream()
            .filter(reader -> reader.getId() == readerId)
            .findFirst()
            .map(reader -> {
                reader.setContact(newContact);
                return true;
            })
            .orElse(false);
    }

    // 查询读者信息
    public Reader getReader(int readerId) {
        return readers.stream()
            .filter(reader -> reader.getId() == readerId)
            .findFirst()
            .orElse(null);
    }

    // 读者信息类
    public static class Reader {
        private final int id;
        private final String name;
        private String contact;

        public Reader(int id, String name, String contact) {
            this.id = id;
            this.name = name;
            this.contact = contact;
        }

        // Getter/Setter
        public int getId() { return id; }
        public String getName() { return name; }
        public String getContact() { return contact; }
        public void setContact(String contact) { this.contact = contact; }

        @Override
        public String toString() {
            return String.format("读者ID：%d | 姓名：%s | 联系方式：%s", id, name, contact);
        }
    }

    // 示例使用
    public static void main(String[] args) {
        ReaderInfo manager = new ReaderInfo();
        manager.addReader("张三", "13800138000");
        manager.addReader("李四", "zhangsan@example.com");
        
        System.out.println("所有读者：");
        manager.readers.forEach(System.out::println);
    }
}
