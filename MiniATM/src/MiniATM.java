import java.util.Scanner;

public class MiniATM {
    // --- Kullanıcı bilgileri  ---
    private final String username = "user";
    private final String password = "12345";

    // --- I/O ---
    private final Scanner read = new Scanner(System.in);

    // --- Uygulama durumu ---
    private double totalBalance = 0.0;
    private double totalDeposit = 0.0;      // Toplam yatırılan tutar
    private double totalWithDraw = 0.0;     // Toplam çekilen tutar
    private int    totalPayBillCount = 0;   // Ödenen fatura adedi

    // --- Giriş/deneme ---
    private final int attempt = 3;

    public static void main(String[] args) {
        new MiniATM().run();
    }

    //  kimlik doğrulamayı yap başarılıysa menüyü göster
    public void run() {
        if (authenticate()) {
            menuLoop();
        }
    }

    // kimlik doğrulama ve deneme hakkı yönetimi
    private boolean authenticate() {
        int count = 0;
        while (count < attempt) {
            System.out.print("Kullanıcı Adı Giriniz : ");
            String readUsername = read.nextLine();

            System.out.print("Parola Giriniz : ");
            String readPassword = read.nextLine();

            if (username.equals(readUsername) && password.equals(readPassword)) {
                return true;
            } else {
                count++;
                int remain = attempt - count;
                System.out.println("Kullanıcı Adı veya Şifre Hatalı !!");
                if (remain > 0) System.out.println("Kalan Hak : " + remain);
            }
        }
        System.out.println("Giriş hakkı bitti. Program sonlandırılıyor.");
        return false;
    }

    // menü döngüsü ve yönlendirme
    private void menuLoop() {
        boolean running = true;
        while (running) {
            System.out.println("\nBanka Sistemine Hoşgeldiniz");
            System.out.println("1- Para Yatır");
            System.out.println("2- Para Çek");
            System.out.println("3- Bakiye Görüntüle");
            System.out.println("4- Fatura Öde");
            System.out.println("5- Çıkış");

            System.out.print("Yapacağınız işlemi seçiniz : ");
            Integer selectMenu = safeReadInt();
            if (selectMenu == null) continue;

            switch (selectMenu) {
                case 1 -> {
                    System.out.print("Yatırmak İstediğiniz Tutarı Girin: ");
                    Double amount = safeReadDouble();
                    if (amount != null) deposit(amount);
                }
                case 2 -> {
                    System.out.print("Çekmek İstediğiniz Tutarı Girin : ");
                    Double amount = safeReadDouble();
                    if (amount != null) withdraw(amount);
                }
                case 3 -> System.out.println("Bakiyeniz: " + totalBalance);
                case 4 -> {
                    System.out.println("1- Elektrik ( %5 indirim )");
                    System.out.println("2- Su       ( %3 indirim )");
                    System.out.println("3- İnternet ( %2 indirim )");
                    System.out.print("Ödemek İstediğiniz Fatura Tipini Girin : ");
                    Integer billType = safeReadInt();
                    if (billType == null) break;
                    System.out.print("Fatura Tutarı Girin: ");
                    Double amount = safeReadDouble();
                    if (amount != null) payBill(billType, amount);
                }
                case 5 -> {
                    System.out.println("Çıkış Yapılıyor ... ");
                    printSummary();
                    running = false;
                }
                default -> System.out.println("Uygun bir seçenek seçin");
            }
        }
    }

    // ---- Menü seçiminde Girilecek olan integer sayının gerçekten girilip girilmediğini , integer olup olmadığını kontrol eder ----
    private Integer safeReadInt() {
        if (!read.hasNextInt()) {
            System.out.println("Geçersiz giriş! Lütfen tam sayı girin.");
            read.nextLine(); // hatalı girdiyi temizle
            return null;
        }
        int valueInteger = read.nextInt();
        read.nextLine();
        return valueInteger;
    }

    // Para Yatırma , Para Çekme ve Fatura turarının girilmesi kısmında double olan tutarın gerçekten girilip girilmediğinin yada double olup olmadığını kontrol eder.
    private Double safeReadDouble() {
        if (!read.hasNextDouble()) {
            System.out.println("Geçersiz tutar! Sayısal bir değer girin.");
            read.nextLine(); // hatalı girdiyi temizle
            return null;
        }
        double valueDouble = read.nextDouble();
        read.nextLine();
        return valueDouble;
    }

    // ---- Özet ----
    private void printSummary() {
        System.out.println("\n--- Özet ---");
        System.out.println("Toplam Bakiye: " + totalBalance);
        System.out.println("Toplam Yatırılan Tutar: " + totalDeposit);
        System.out.println("Toplam Çekilen Tutar: " + totalWithDraw);
        System.out.println("Toplam Ödenen Fatura Sayısı: " + totalPayBillCount);
    }

    // ---- İş kuralları  ----
    private void deposit(double amount) {
        if (amount <= 0) { // sıfır/negatif kontrolü
            System.out.println("Sıfır veya negatif tutar yatırılamaz.");
            return;
        }
        totalBalance += amount;
        totalDeposit += amount;
        System.out.println("Para yatırılmıştır.");
    }

    private void withdraw(double amount) {
        if (amount <= 0) { // sıfır/negatif kontrolü
            System.out.println("Sıfır veya negatif tutar çekilemez.");
            return;
        }
        double komisyon = (amount > 5000) ? amount * 0.02 : 0.0;
        double toplamCekim = amount + komisyon;

        if (totalBalance < toplamCekim) { // bakiye aşımı kontrolü
            System.out.println("Yetersiz bakiye! (Gerekli: " + toplamCekim + ", Bakiye: " + totalBalance + ")");
            return;
        }

        totalBalance -= toplamCekim;
        totalWithDraw += amount;
        System.out.println("Para çekilmiştir. (Komisyon: " + komisyon + ")");
    }

    private void payBill(int billType, double amount) {
        if (amount <= 0) { // sıfır/negatif kontrolü
            System.out.println("Sıfır veya negatif fatura tutarı ödenemez.");
            return;
        }

        if (billType < 1 || billType > 3) {
            System.out.println("Geçersiz fatura tipi!");
            return;
        }

        double discount = (billType == 1) ? 0.05 : (billType == 2) ? 0.03 : 0.02;
        double discounted = amount - amount * discount;

        if (totalBalance < discounted) { // bakiye aşımı kontrolü
            System.out.println("Yetersiz bakiye! (Gerekli: " + discounted + ", Bakiye: " + totalBalance + ")");
            return;
        }

        totalBalance -= discounted;
        totalPayBillCount++;
        System.out.println("Fatura Tutarı : " + amount);
        System.out.println("İndirim Oranı : " + (discount * 100) + "%");
        System.out.println("Ödenen Tutar  : " + discounted);
    }
}
