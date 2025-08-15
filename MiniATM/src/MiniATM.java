import java.util.Scanner;

public class MiniATM {
    // --- Kullanıcı bilgileri Örnek Statik Değerler Ödev içerisinde verilmiştir ---
    private String username = "user";
    private String password = "12345";

    // --- Oturum/girdi alanları Kullanıcıdan değer alıp sabit olan username ve password ile kontrol etmek amacı ile eklenmiştir ---
    private String readUsername;
    private String readPassword;
    private final Scanner read = new Scanner(System.in);

    // --- Uygulama içerisindeeki iş akışı için gerekli değişkenler eklenmiştir. ---
    private double totalBalance = 0.0;
    private double totalDeposit = 0;      // Toplam yatırılan
    private double totalWithDraw = 0;     // Toplam çekilen
    private double  totalPayBillCount = 0; // Ödenen fatura adedi


    private boolean condition = true;
    private int count = 0;             // Hatalı giriş sayacı
    private final int attempt = 3;     // Giriş hakkı


    public static void main(String[] args) {
        MiniATM atm = new MiniATM();
        atm.run();
    }

    // --- Ana akışı tek bir metod içerisinde toplayıp daha okunabilir bir kod görüntüsü elde etmek istedim ---
    public void run() {
        while (count < attempt) {
            System.out.print("Kullanıcı Adı Giriniz : ");
            readUsername = read.nextLine();

            System.out.print("Parola Giriniz : ");
            readPassword = read.nextLine();

            if (!username.equals(readUsername) || !password.equals(readPassword)) {
                count++;
                int remain = attempt - count;
                System.out.println("Kullanıcı Adı veya Şifre Hatalı !!");
                System.out.println("Kalan Hak : " + remain);
                continue;
            } else {
                condition = true;
                do {
                    System.out.println("\nBanka Sistemine Hoşgeldiniz");
                    System.out.println("1- Para Yatır");
                    System.out.println("2- Para Çek");
                    System.out.println("3- Bakiye Görüntüle");
                    System.out.println("4- Fatura Öde");
                    System.out.println("5- Çıkış");

                    System.out.print("Yapacağınız işlemi seçiniz : ");
                    if (!read.hasNextInt()) {
                        System.out.println("Geçersiz giriş! Lütfen sayı girin.");
                        read.nextLine(); // hatalı girdiyi temizle
                        continue;
                    }
                    int selectMenu = read.nextInt();

                    switch (selectMenu) {
                        case 1 -> {
                            System.out.print("Yatırmak İstediğiniz Tutarı Girin: ");
                            Double amount = safeReadPositiveDouble();
                            if (amount != null) deposit(amount);
                        }
                        case 2 -> {
                            System.out.print("Çekmek İstediğiniz Tutarı Girin : ");
                            Double amount = safeReadPositiveDouble();
                            if (amount != null) withdraw(amount);
                        }
                        case 3 -> System.out.println("Bakiyeniz: " + totalBalance);
                        case 4 -> {
                            System.out.println("1- Elektrik ( %5 indirim )");
                            System.out.println("2- Su       ( %3 indirim )");
                            System.out.println("3- İnternet ( %2 indirim )");
                            System.out.print("Ödemek İstediğiniz Fatura Tipini Girin : ");
                            if (!read.hasNextInt()) {
                                System.out.println("Geçersiz fatura tipi!");
                                read.nextLine();
                                continue;
                            }
                            int billType = read.nextInt();
                            System.out.print("Fatura Tutarı Girin: ");
                            Double amount = safeReadPositiveDouble();
                            if (amount != null) payBill(billType, amount);
                        }
                        case 5 -> {
                            System.out.println("Çıkış Yapılıyor ... ");
                            printSummary();
                            condition = false; // menüden çık
                        }
                        default -> System.out.println("Uygun bir seçenek seçin");
                    }

                } while (condition);

                // Menüyü kapattıysan programı bitir
                break;
            }
        }
    }

    // --- Para Giriş ve Çıkışı için yardımcı bir metot oluşturulmuş olunup bu metot para yatırma / para çekme ve fatura ödemede kullanılmıştır---
    private Double safeReadPositiveDouble() {
        if (!read.hasNextDouble()) {
            System.out.println("Geçersiz tutar! Sayısal bir değer girin.");
            read.nextLine(); // hatalı girdiyi temizle
            return null;
        }
        double val = read.nextDouble();
        if (val <= 0) {
            System.out.println("Sıfır veya negatif tutar geçersizdir.");
            return null;
        }
        return val;
    }

    // --- Özet ---
    private void printSummary() {
        System.out.println("\n--- Özet ---");
        System.out.println("Toplam Bakiye: " + totalBalance);
        System.out.println("Toplam Yatırılan Tutar: " + totalDeposit);
        System.out.println("Toplam Çekilen Tutar: " + totalWithDraw);
        System.out.println("Toplam Ödenen Fatura Sayısı: " + totalPayBillCount);
    }

    // --- Fatura Ödeme ---
    private void payBill(int billType, double amount) {
        if (billType < 1 || billType > 3) {
            System.out.println("Geçersiz fatura tipi!");
            return;
        }

        double discount;
        if (billType == 1)
            discount = 0.05; // Elektrik
        else if (billType == 2)
            discount = 0.03; // Su
        else
            discount = 0.02; // İnternet

        double discounted = amount - amount * discount;

        if (totalBalance < discounted) {
            System.out.println("Yetersiz bakiye! (Gerekli: " + discounted + ", Bakiye: " + totalBalance + ")");
            return;
        }

        totalBalance -= discounted;
        totalPayBillCount++;
        System.out.println("Fatura Tutarı : " + amount);
        System.out.println("İndirim Oranı : " + (discount * 100) + "%");
        System.out.println("Ödenen Tutar  : " + discounted);
        System.out.println("Yeni Bakiye   : " + totalBalance);
    }

    // --- Para Çekme ---
    private void withdraw(double amount) {
        // Komisyon: 5000 TL üzeri %2
        double komisyon = (amount > 5000) ? amount * 0.02 : 0.0;
        double toplamCekim = amount + komisyon;

        if (totalBalance < toplamCekim) {
            System.out.println("Yetersiz bakiye! (Gerekli: " + toplamCekim + ", Bakiye: " + totalBalance + ")");
            return;
        }

        totalBalance -= toplamCekim;
        totalWithDraw +=  amount;
        System.out.println("Para çekildi. (Komisyon: " + komisyon + ")");
        System.out.println("Yeni Bakiye : " + totalBalance);
    }

    // --- Para Yatırma ---
    private void deposit(double amount) {
        totalBalance += amount;
        totalDeposit += amount;
        System.out.println("Para yatırılmıştır. Yeni Bakiye: " + totalBalance);
    }
}
