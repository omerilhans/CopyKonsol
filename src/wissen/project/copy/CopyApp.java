package wissen.project.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Date 25.04.2016 <br/>
 *
 * @author Ömer İlhanlı
 */
public class CopyApp {

    /**
     * Fields <br/><br/>
     *
     * Girilen Dosya yolundaki, içiçe olan tüm klasor yollarının listesi. <br/>
     * <br/>
     * <b> klasorYolList = Klasorlerin yollarının listesi</b>
     *
     */
    List<String> klasorYolList = new ArrayList<>();

    /**
     * Fields <br/><br/>
     *
     * Girilen Dosya yolundaki, tüm dosyaların dosya yollarının listesi. <br/>
     * <br/>
     * <b>dosyaYolList = Dosyaların yollarının listesi </b>
     *
     */
    List<String> dosyaYolList = new ArrayList<>();

    /**
     * Fields <br/><br/>
     *
     * Statik olarak konsoldan alınan yol değişkenleri. <br/>
     * <br/>
     * <b>
     * klasorYol = Girilen Dosyanın Yolu <br/><br/>
     *
     * klasorAd = Girilen Dosyanın Adı <br/><br/>
     *
     * hedefYol = Kaydedilecek Dosya Yolu <br/><br/>
     *
     * guncelHedefYol = hedefYol'un Güncellenmiş hali
     * </b>
     */
    String klasorYol, klasorAd, hedefYol, guncelHedefYol;

    /**
     * Fields <br/><br/>
     *
     * Kopyalanacak dosya yolundan oluşturulan Klasor dosyası. <br/>
     * <br/>
     */
    File klasor;

    /**
     * Fields <br/><br/>
     *
     * Kopyalama Bitince copy_paste Methodunda ekrana bilgi düşürür. <br/>
     * <br/>
     * <b>
     * isCopyFile = Tüm dosyalar kopyalandı mı? <br/><br/>
     *
     * isCopyFolder = Tüm klasorler kopyalandı mı? <br/><br/>
     *
     * isFile = Verilen yolda bir dosya mı var? <br/><br/>
     *
     * isFolder = Verilen yolda bir klasor mu var?
     * </b>
     */
    boolean isCopyFile, isCopyFolder, isFile, isFolder;

    /**
     * Fields <br/><br/>
     *
     * I/O İşlemleri için gerekli Java Api Sınıfları. <br/><br/>
     * <br/>
     * <b>
     * oku = dosyayı bir byte array içine okur. <br/><br/>
     *
     * yaz = okunan dosyayı bir byte arrayden alıp yeni bir dosyaya yazar.
     * </b>
     */
    InputStream oku;
    OutputStream yaz;

    /**
     * Methods <br/><br/>
     *
     * Copy-Paste işlemleri için Klasor Yolu verir. <br/>
     * <br/>
     *
     * @return String
     *
     */
    public String giveWay() {
        Scanner velis = new Scanner(System.in);
        return velis.nextLine().trim();
    }

    /**
     * Methods <br/><br/>
     *
     * Dosya ve Klasorler için, recursive olarak Yol Listelerini oluşturur.<br/>
     * <br/>
     *
     * @param file
     *
     */
    public void getFiFoList(File file) {

        //------------------------------------------   Eğer gelen dosya klasorse, klasorYolList listesine eklenir.          
        if (file.isDirectory()) {

            klasorYolList.add(file.getAbsolutePath());

            //------  Alt dosyalar oluşturulur.
            File[] altDosyalar = file.listFiles();

            if (altDosyalar != null) {

                // Bilgi
                if (altDosyalar.length != 0) {
                    if (!klasor.getName().equals(file.getName())) {
                        System.out.println(klasor.getName() + " içindeki " + file.getName() + " Klasorü Dolu.\n");
                    } else {
                        System.out.println(file.getName() + " Klasorü Dolu.\n");
                    }
                } else if (!klasor.getName().equals(file.getName())) {
                    System.out.println(klasor.getName() + " içindeki " + file.getName() + " Klasorü Boş.\n");
                } else {
                    System.out.println(file.getName() + " Klasorü Boş.\n");
                }

                //------  Tüm alt dosyalardaki dosya adlarını dosyaYolList listesine alıyoruz
                for (File altDosya : altDosyalar) {
                    if (altDosya.isDirectory()) {
                        //----- Klasor olan dosya, tekrar getFiFoList'e gönderilerek Recursive işleme sokulur. 
                        getFiFoList(altDosya);
                    } else {
                        //----  Tüm dosya adlarını dosyaYolList listesine eklenir.  //
                        dosyaYolList.add(altDosya.getAbsolutePath());
                    }
                }
            }

        } //---------------------------------- Eğer dosya klasor değilse, dosyaYolList listesine eklenir.
        else {
            dosyaYolList.add(file.getAbsolutePath());
        }
    }

    /**
     * Methods <br/><br/>
     *
     * Tüm İçiçe olan klasorler oluşturur. <br/><br/>
     *
     * (klasor yollarını kullanır)
     *
     */
    public void createFolders() {
        int isAll = 0;

        for (String kYol : klasorYolList) {
            /* ----------------------------------------------------------------------------------------
            UPDATE-WAY : dosyaAdliGuncelHedefYol ile -hedef klasor yol- üzerine kopyalanması gereken tüm klasorlerin
            bulunduğu yolu ve adi ile yeni klasor yolu oluşturulur.
            
            örn: /Users/omer/Document/KlasorA/klasor1; klasoru için yeni yol;
            /Users/omer/Desktop/KlasorB/KlasorA/klasor1; olur. s
            ---------------------------------------------------------------------------------------- */

            File tmpF = new File(kYol);

            if (tmpF.isDirectory()) { //------------------------------------------------------
                System.out.println("klasorYol : " + klasorYol);
                System.out.println("klasorAd : " + klasorAd);
                int ndx = klasorYol.lastIndexOf(klasorAd) + klasorAd.length();
                String dosyaAdliGuncelHedefYol = guncelHedefYol + kYol.substring(ndx);
                // klasor yoksa oluşturulur.
                File klas = new File(dosyaAdliGuncelHedefYol);
                if (!klas.exists()) {
                    klas.mkdir();
                    // hedef klasorun adını alır ekranda bilgi veririz.
                    File hdfKlas = new File(hedefYol);
                    System.out.println(klas.getName() + " klasoru, \n\t" + klas.getAbsolutePath()
                            + " \tYolunda \n\t**" + hdfKlas.getName() + "** \tiçinde \n\t# Oluşturuldu.\n");
                }
                isAll++;
                if (isAll == klasorYolList.size()) {
                    System.out.println("\t\tTüm Klasorler Oluşturuldu...");
                    System.out.println("\t\t----------------------------\n\n");
                    isCopyFolder = true;
                }
            } //------------------------------------------------------
        }
    }

    /**
     * Methods <br/><br/>
     *
     * Klasorler dışındaki tüm dosyaları oluşturur. <br/>
     *
     * (dosya yollarını kullanır) <br/>
     * <br/>
     *
     * @throws FileNotFoundException <br/>
     * @throws IOException
     *
     */
    public void createFiles() throws FileNotFoundException, IOException {
        //------------------------------------------------------ Dosya okunur - yazılır.

        int isAll = 0; //---------------- Bütün dosyaları bitince, tüm dosyalar yazıldı bilgisi.
        for (String dYol : dosyaYolList) {

            File readFile = new File(dYol);
            oku = new FileInputStream(readFile);
            byte[] buffer = new byte[4096];
            int miktar = 0;
            /* ------------------------------------------------------
                UPDATE-WAY : aynı şekilde dosyalar için, dosya adı eklenmiş yeni klasor yolu oluşturulur.
             */

            int ndx = klasorYol.lastIndexOf(klasorAd) + klasorAd.length();
            String dosyaAdliGuncelHedefYol = guncelHedefYol + dYol.substring(ndx);
            File writeFile = null;
            if (isFile) {
                writeFile = new File(hedefYol);
            } else {
                writeFile = new File(dosyaAdliGuncelHedefYol);
            }

            yaz = new FileOutputStream(writeFile);
            while ((miktar = oku.read(buffer)) != -1) {
                yaz.write(buffer);
            }

            if (writeFile.exists()) {
                System.out.println(readFile.getName() + " Dosyası, \n\t"
                        + "** " + writeFile.getAbsolutePath() + " ** \tYolunda      \n\t->> Yazıldı.\n");
            } else {
                System.out.println(writeFile.getName() + " Dosyası, ->>     \n\tYazılamadı !!!\n");
            }

            isAll++;
            if (isAll == dosyaYolList.size()) {
                System.out.println("\nTüm Dosyalar Yazıldı...");
                System.out.println("\\----------------------\\\n\n");
                isCopyFile = true;
            }
        }
    }

    /**
     * Methods <br/><br/>
     *
     * Kopyalanacak klasor yolu ve hedef klasor yolu alır, <br/><br/>
     *
     * Tüm klasor ve dosyaları hedef klasore kopyalar, <br/><br/>
     *
     * Kopyalanmanın tamamlanma bilgisini verir. <br/><br/>
     * <br/>
     *
     *
     * @param klasorYol <br/>
     * @param hedefYol <br/>
     * @throws Exception
     *
     */
    public void copy_paste(String klasorYol, String hedefYol) throws Exception {
        while (true) {
            //------------------------------------------------------ klasor oluşur
            klasor = new File(klasorYol);
            if (klasor.isFile()) {
                isFile = true;
            } else {
                isFolder = true;
            }

            //------------------------------------------------------ klasor ve dosya yol listeleri oluşturulur.
            getFiFoList(klasor);
            guncelHedefYol = hedefYol + "/" + klasorAd;

            createFolders();
            createFiles();

            //------------------------------------------------------ Kopyalanma  Bilgisi      verilir.
            if (isCopyFile && isCopyFolder) {
                System.out.println("\t\tTüm Dosyalar Kopyalandı. √\n\n");
            } else if (isCopyFolder || isCopyFile) {
                if (isCopyFolder) {
                    System.out.println("Klasorler Kopyalandı.");
                }
                if (isCopyFile) {
                    System.out.println("Dosyalar Kopyalandı.");
                }
            } else { // ------------------------------------------------------ İşlemin Tamamlanıp Tamamlanmadığı Bilgisi
                System.out.println("Hiçbirşey Kopyalanmadı. !!!");
            }

            if (isFile || isFolder) {
                System.out.println("\n\t\t Kopyalama Tamamlandı. √√\n\n");
                break;
            } else {
                System.out.println("\t\t Verilen Yol'da, dosya veya klasor bulunamadı. Yol'u yeniden girin edin! \n");
            }
        }
    }

    // Main
    public static void main(String[] args) throws Exception {
        CopyApp fc = new CopyApp();

        System.out.print("Kopyalanıcak klasor yolu : ");
        fc.klasorYol = fc.giveWay(); //------------------------------------------------------ Klasor Yolu
        fc.klasor = new File(fc.klasorYol);
        fc.klasorAd = fc.klasor.getName();

        System.out.print("Hedef yol : ");
        fc.hedefYol = fc.giveWay(); //------------------------------------------------------ Hedef Klasor Yolu
        System.out.println("\n");

        fc.copy_paste(fc.klasorYol, fc.hedefYol);

    }
}
