import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class Book {
    private String title;
    private String author;
    private String isbn;
    private double price;

    public Book(String title, String author, String isbn, double price) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                '}';
    }
}

public class BookPurchasePlan {
    private static List<Book> availableBooks = new ArrayList<>();

    // 初始化一些图书数据
    static {
        availableBooks.add(new Book("Java编程思想", "Bruce Eckel", "978-7-111-21382-6", 99.0));
        availableBooks.add(new Book("Effective Java", "Joshua Bloch", "978-7-111-55983-8", 89.0));
        availableBooks.add(new Book("算法导论", "Thomas H. Cormen", "978-7-121-15535-5", 129.0));
        availableBooks.add(new Book("深入理解计算机系统", "Randal E. Bryant", "978-7-121-15536-2", 119.0));
        availableBooks.add(new Book("设计模式", "Erich Gamma", "978-7-111-10867-9", 79.0));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("欢迎使用图书采购计划系统");
        System.out.print("请输入您的预算金额: ");
        double budget = scanner.nextDouble();
        scanner.nextLine(); // 清除缓冲区

        System.out.print("请输入您感兴趣的图书类别（如：编程、算法、计算机系统等，直接回车跳过）: ");
        String category = scanner.nextLine();

        // 筛选符合条件的图书
        List<Book> recommendedBooks = recommendBooks(budget, category);

        if (recommendedBooks.isEmpty()) {
            System.out.println("没有找到符合您需求的图书。");
        } else {
            System.out.println("\n根据您的预算和需求，推荐以下图书:");
            for (Book book : recommendedBooks) {
                System.out.println(book);
            }

            // 按价格排序
            recommendedBooks.sort(Comparator.comparingDouble(Book::getPrice));

            double totalCost = 0.0;
            List<Book> selectedBooks = new ArrayList<>();
            System.out.println("\n开始选择图书（输入序号，多个选择用逗号分隔，如：1,3,5）。输入0结束选择：");

            while (true) {
                System.out.print("请选择图书编号: ");
                String input = scanner.nextLine();
                if ("0".equals(input)) {
                    break;
                }

                try {
                    String[] indices = input.split(",");
                    for (String index : indices) {
                        int idx = Integer.parseInt(index.trim()) - 1;
                        if (idx >= 0 && idx < recommendedBooks.size()) {
                            Book selected = recommendedBooks.get(idx);
                            if (totalCost + selected.getPrice() <= budget) {
                                selectedBooks.add(selected);
                                totalCost += selected.getPrice();
                                System.out.println("已添加: " + selected.getTitle());
                            } else {
                                System.out.println("超出预算，无法添加: " + selected.getTitle());
                            }
                        } else {
                            System.out.println("无效的编号: " + (idx + 1));
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("输入格式错误，请重新输入。");
                }
            }

            System.out.println("\n您最终选择的图书如下:");
            for (Book book : selectedBooks) {
                System.out.println(book);
            }
            System.out.println("总花费: " + totalCost + "元");
        }

        scanner.close();
    }

    /​**​
     * 根据预算和类别推荐图书
     *
     * @param budget  用户的预算金额
     * @param category 用户感兴趣的图书类别
     * @return 推荐的图书列表
     */
    private static List<Book> recommendBooks(double budget, String category) {
        List<Book> recommended = new ArrayList<>();
        for (Book book : availableBooks) {
            boolean matchesCategory = category.isEmpty() || 
                book.getTitle().toLowerCase().contains(category.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(category.toLowerCase());
            if (matchesCategory && book.getPrice() <= budget) {
                recommended.add(book);
            }
        }
        return recommended;
    }
}
