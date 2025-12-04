import java.util.Scanner;
import java.util.ArrayList; 
import java.util.List;
import java.util.Random;

public class App {

    abstract static class KuantumNesnesi{     //----------abstarct klasss-----

        private String ID;
        private Double Stabilite;
        private int TehlikeSeviyesi;

        public abstract void AnalizEt();

        public String DurumBilgisi(){
            return("ID: "+ID+" , Stabilite: "+Stabilite);
        }

        public String getid(){
            return ID;
        }
        public void setid(String ID){
            this.ID=ID;
        }

        public double getstabilite(){
            return Stabilite;
        }
        public void setstabilite(Double Stabilite){
            if(Stabilite<=0){
                this.Stabilite=0.0;
            }
            else if(Stabilite>100){
                this.Stabilite=100.0;
            }
            else {this.Stabilite=Stabilite;}
        }

        public int gettehlikeSeviyesi(){
            return TehlikeSeviyesi;
        }
        public void settehlikeSeviyesi(int TehlikeSeviyesi){
            if(TehlikeSeviyesi<0){this.TehlikeSeviyesi=0;}
            else if(TehlikeSeviyesi>10){this.TehlikeSeviyesi=10;}
            else{this.TehlikeSeviyesi=TehlikeSeviyesi;}
        }

    }
// -----kritik interfacesi----------
    public interface IKritik {
    
        void AcilDurumSogutması();
    }

    public static class KuantumCokusuException extends  RuntimeException  {
        public KuantumCokusuException(String getid) {super("Kuantum çöküşü gerçekleşti! Patlayan nesne ID: "+getid); } 
        {
            
        }
    }




// -------veripaketi----karanlık maddde------ anti madde

    public static class VeriPaketi extends KuantumNesnesi{

        @Override
        public void AnalizEt(){

            setstabilite(getstabilite()-5);
            System.out.println("Veri içeriği okundu.");

            if(getstabilite()<0){
                throw new KuantumCokusuException(getid() );
            }
        }
    }

    public static class KaranlikMadde extends KuantumNesnesi implements IKritik{

        @Override
        public void AnalizEt(){

            setstabilite(getstabilite()-15);
            System.out.println("Karanlık madde analiz edildi.");

            if(getstabilite()<0){
                throw new KuantumCokusuException(getid() );
            }
        }

        @Override
        public void AcilDurumSogutması(){

            setstabilite(getstabilite()+50);
            if(getstabilite()>100){setstabilite(100.00);}
            System.out.println("##Acil soğutma uygulandı");
        }
    }

    public static class AntiMadde extends KuantumNesnesi implements IKritik{

        @Override
        public void AnalizEt(){
            setstabilite(getstabilite()-25);
            System.out.println("Evrenin dokusu titriyor...");

            if(getstabilite()<0){
                throw new KuantumCokusuException(getid() );
            }
        }

        @Override
        public void AcilDurumSogutması(){
            setstabilite(getstabilite()+50);
            if(getstabilite()>100){setstabilite(100.00);}
            System.out.println("##Acil soğutma uygulandı");
        }
    }


//---------------------- MAİN ------------------------------------------------

    public static void main(String[] args) throws Exception {


        Scanner tarayici = new Scanner(System.in);
        


        List<KuantumNesnesi> envanter = new ArrayList<>();

        boolean menu =true;

        while(menu){


            try {

                System.out.println("");
                System.out.println("              KUANTUM AMBARI KONTROL PANELİ   ");
                System.out.println(" 1. Yeni Nesne Ekle  \n 2. Tüm Envanteri Listele \n 3. Nesneyi Analiz Et \n 4. Acil Durum Soğutması Yap \n 5. Çıkış ");
                System.out.print("Seçiminiz: ");

                int secim= Integer.parseInt(tarayici.nextLine());
                System.out.println();

                if(secim==1){
                    System.out.println("Hangi nesneyi oluşturmak istersiniz?  1)VeriPaketi 2)KaranlıkMadde 3)AntiMadde  4)Rastgele Veri");
                    System.out.print("Seçiminiz: ");
                    int turSecim= Integer.parseInt(tarayici.nextLine());
                    
                    KuantumNesnesi n = switch (turSecim) {
                        case 1 -> new VeriPaketi();
                        case 2 -> new KaranlikMadde();
                        case 3 -> new AntiMadde();
                        case 4 -> rastgeleNesneOlustur(); // Metot harfi küçük başlar
                        default -> null; // Veya throw new ... (Java'da bu satır ZORUNLUDUR)
                    };

                    if(n==null){
                        System.out.println("Geçersiz seçim.");
                        continue;
                    }

                    if(turSecim==1||turSecim==2||turSecim==3){
                        System.out.print("ID girin: ");
                        n.setid(tarayici.nextLine());
                        System.out.print("Stabilite (0-100) girin: ");
                        n.setstabilite(Double.parseDouble(tarayici.nextLine()));
                        System.out.print("Tehlike seviyesi (1-10) girin: ");
                        n.settehlikeSeviyesi(Integer.parseInt(tarayici.nextLine()));
                        System.out.println();
                        System.out.println("----"+n.getid()+" ID'li nesne oluşturuldu.----");
                    }

                    envanter.add(n);
                    System.out.println("..........................................................................................");
                }

                else if(secim==2){
                    System.out.println("---Envanter Listesi---");
                    if(envanter.isEmpty())
                    {
                        System.out.println("Envanter boş.");
                    }
                    else {
                        for (var x : envanter) {
                            System.out.println(x.DurumBilgisi());
                        }
                    }
                    System.out.println("----------------------");
                    System.out.println("....................................................................................................");
                }

                else if(secim==3){
                    System.out.println("Analiz edilecek ID: ");
                    String id=tarayici.nextLine();
                    System.out.println();

                    var nesne = envanter.stream()
                       .filter(x -> x.getid().equals(id)) // id eşleşmesini kontrol et
                       .findFirst()
                       .orElse(null); // bulunamazsa null döner

                    if (nesne == null) {
                    System.out.println("Nesne bulunamadı!");
                    continue; // döngü içinde kullanılıyor
                    }

                    nesne.AnalizEt();
                    if(nesne.getstabilite()<=0){
                        throw new KuantumCokusuException(id);
                    }
                    System.out.println("....................................................................................................");
                }

                else if(secim==4){
                    System.out.print("Soğutma yapılacak ID: ");
                    String id=tarayici.nextLine();
                    System.out.println();


                    var nesne = envanter.stream()
                       .filter(x -> x.getid().equals(id)) // id eşleşmesini kontrol et
                       .findFirst()
                       .orElse(null); // bulunamazsa null döner

                    if (nesne == null) {
                    System.out.println("Nesne bulunamadı!");
                    continue; // döngü içinde kullanılıyor
                    }
                    if(nesne instanceof IKritik ){
                        IKritik kritikNesne = (IKritik) nesne; // cast yap
                        kritikNesne.AcilDurumSogutması();
                    }
                    else{
                        System.out.println(" ! Bu nesne soğutulamaz. ");
                        System.out.println("................................................................................................");
                    }

                }
                else if(secim==5){
                    return;
                }
                
            } catch (KuantumCokusuException ex) {
               // System.out.print(CLEAR); // ekranı temizle
                System.out.flush();
                System.out.println("\n**************************************************");
                System.out.println("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                System.out.println("HATA NEDENİ: " + ex.getMessage());
                System.out.println("**************************************************");
                menu=false;
            }





            
                

            
                
            

            





        }

    }

    
    private static KuantumNesnesi rastgeleNesneOlustur() {
    Random r = new Random();

    
    int tur = r.nextInt(3); 


    KuantumNesnesi n = switch (tur) {
        case 0 -> new VeriPaketi();
        case 1 -> new KaranlikMadde();
        case 2 -> new AntiMadde();
        default -> new VeriPaketi(); // Default zorunludur
    };

    // --- EN ÖNEMLİ KISIM: SETTER KULLANIMI ---
    try {
        // C#: n.id = "RND-" + r.Next(1, 21);
        // Java: n.setId(...) ve r.nextInt(20) + 1
        n.setid("RND-" + (r.nextInt(20) + 1));

        // C#: n.stabilite = r.Next(10, 101);
        // Java: nextInt(91) -> 0..90 üretir, +10 eklersek 10..100 olur
        n.setstabilite((double)r.nextInt(91) + 10);

        // C#: n.tehlikeSeviyesi = r.Next(1, 11);
        n.settehlikeSeviyesi(r.nextInt(10) + 1);
        
    } catch (Exception e) {
        // Setter'larınızda "throws Exception" varsa Java burada try-catch zorunlu kılar
        System.out.println("Hata: " + e.getMessage());
    }

    System.out.println();
    
    // C#: n.GetType().Name
    // Java: n.getClass().getSimpleName()
    System.out.println("Rastgele nesne oluşturuldu Tür: " + n.getClass().getSimpleName() + ", ID: " + n.getid());

    return n;
        
    }
}
