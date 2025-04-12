import java.util.Scanner;

class Book {
    private String title;
    private String author;
    private String isbn;
    private int year;

    public Book(String title, String author, String isbn, int year) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", year=" + year +
                '}';
    }
}

public class BookManagement_Add {
    private static java.util.List<Book> books = new java.util.ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用图书管理系统 - 添加图书功能");

        System.out.print("请输入书名: ");
        String title = scanner.nextLine();

        System.out.print("请输入作者: ");
        String author = scanner.nextLine();

        System.out.print("请输入ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("请输入出版年份: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // 清除缓冲区

        Book newBook = new Book(title, author, isbn, year);
        books.add(newBook);

        System.out.println("图书添加成功！");
        System.out.println("已添加的图书信息: " + newBook);

        // 显示所有图书
        System.out.println("\n当前所有图书列表:");
        for (Book book : books) {
            System.out.println(book);
        }

        scanner.close();
    }
}
