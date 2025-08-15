import javax.print.attribute.standard.DateTimeAtCompleted;
import java.util.Scanner;

public class MiniATM {
    static String username = "user";
    static String password = "12345";

    static String readUsername;
    static String readPassword;

    static int selectMenu;

    static double amount=0;
    static double totalBalance=0;
    static double komisyon;
    static double discount;

    static double newPillAmount=0;
     static int count = 0 ;
    static int attempt=3;
    static int remain;
    static int billType;



    static int totalDeposit=0;
    static int totalWithDraw=0;
    static int totlalPayBillCount=0;

    static Scanner read = new Scanner(System.in);
    static boolean condition = true;


    public static void main (String [] args){

        while(count < attempt){
            System.out.print("Kullanıcı Adı Giriniz :");
            readUsername=read.nextLine();

            System.out.print("Parola Giriniz :");
            readPassword=read.nextLine();

            if(!username.equals(readUsername) || !readPassword.equals(password)){
                count +=1;
                remain = attempt -count;
                System.out.println("Kullanıcı Adı veya Şifre Hatalı !!");
                System.out.println("Kalan Hak : " + remain);
                continue;
            } else {
                do{
                    System.out.println("Banka Sistemine Hoşgeldiniz");
                    System.out.println("1- Para Yatır ");
                    System.out.println("2- Para Çek ");
                    System.out.println("3- Para  Bakiye Görüntüle");
                    System.out.println("4- Fatura Öde ");
                    System.out.println("5- Çıkış ");

                    System.out.print("Yapacağınız ilgili işlemi seçiniz :");
                    selectMenu=read.nextInt();

                    switch (selectMenu){
                        case 1 :
                            System.out.print("Yatırmak İstediğiniz Tutarı Girin: ");
                            amount=read.nextInt();
                            deposite(amount);
                            break;
                        case 2:
                            System.out.print("Çekmek İstediğiniz Turarı Girin : ");
                            amount=read.nextInt();
                            withdraw(amount);
                            break;
                        case 3:
                            System.out.println("Bakiyeniz " + totalBalance);
                            break;
                        case 4:
                            System.out.println("1- Elektrik");
                            System.out.println("2- Su");
                            System.out.println("3 -İnternet");


                            System.out.print("Ödemek İstediğiniz Fatura Tipini Girin : ");
                            billType = read.nextInt();

                            System.out.print("Fatura Tutarı Girin: ");
                            amount=read.nextInt();
                            payBill(billType , amount);
                            break;

                        case 5:
                            System.out.println("Çıkış Yapılıyor ... ");
                            printSummary();
                            condition=false;
                            break;

                        default:
                            System.out.println("Uygun bir seçenek seçin");
                            break;
                    }

                }while(condition == true);

            }

        }
    }

    private static void printSummary() {
        System.out.println("Toplam Bakiye " + totalBalance);
        System.out.println("Toplam Yatırılan Tutar : " + totalDeposit);
        System.out.println("Toplam Çekilen Tutar: " + totalWithDraw);
        System.out.println("Toplam Ödenen Fatura Sayısı  : " + totlalPayBillCount);
    }

    private static void payBill(int billType, double amount) {
        if(billType <=3  && billType>0){
            if(billType==1){
                discount=0.05;
                System.out.println("Fatura Tutarı : " + amount);
                newPillAmount=amount-amount*discount;
                totalBalance=totalBalance-newPillAmount;
                System.out.println("İndirim Sonrası Fatura Tutarı : " +newPillAmount );
            }
            else if(billType==2){
                discount=0.03;
                System.out.println("Fatura Tutarı : " + amount);
                newPillAmount=amount-amount*discount;
                totalBalance-=totalBalance-newPillAmount;
                System.out.println("İndirim Sonrası Fatura Tutarı : " +newPillAmount );
            }else if (billType==3){
                discount=0.02;
                System.out.println("Fatura Tutarı : " + amount);
                newPillAmount=amount-amount*discount;
                totalBalance-=totalBalance-newPillAmount;
                System.out.println("İndirim Sonrası Fatura Tutarı : " +newPillAmount );
            }
            totlalPayBillCount++;
        }
    }

    public static void withdraw(double amount) {
        if(totalBalance>=amount && amount>0) {
            komisyon = amount> 5000 ? amount*0.02 :0;
            totalBalance = totalBalance - amount - komisyon;
            totalWithDraw+=amount;
            System.out.println("Para Çekilmiştir.");
        }
        else if (amount==0){
            System.out.println("Sıfır Tl Yatırılamaz");
        }
        else {
            System.out.println("Yetersiz Bakiye");
        }

    }

    public static void deposite(double amount){
        if(amount>0){
            totalBalance+=amount;
            System.out.println("Para Yatırılmıştır");
            totalDeposit+=amount;
        }



    }



}
