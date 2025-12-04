using KuantumKaosYönetimi;
using System.Collections.Generic;
using static KuantumKaosYönetimi.Domain;


namespace KuantumKaosYönetimi
{
    internal class Program
    {
        static void Main(string[] args)
        {
            List<KuantumNesnesi> envanter = new List<KuantumNesnesi>();
            bool menu = true;


            while (menu)
            {
                try
                {
                    Console.WriteLine();
                    Console.WriteLine("                                      KUANTUM AMBARI KONTROL PANELİ             \n");
                    Console.WriteLine("1. Yeni Nesne Ekle \r\n2. Tüm Envanteri Listele \r\n3. Nesneyi Analiz Et \r\n4. Acil Durum Soğutması Yap (Sadece IKritik olanlar için!)\r\n5. Çıkış\r\n");
                    Console.Write("Seçiminiz: ");

                    int secim = Convert.ToInt32(Console.ReadLine());
                    Console.WriteLine();



                    if (secim == 1)
                    {
                        Console.WriteLine("Hangi nesneyi oluşturmak istersiniz?  1)VeriPaketi 2)KaranlıkMadde 3)AntiMadde  4)Rastgele Veri");
                        Console.Write("Seçiminiz: ");
                        int turSecim = Convert.ToInt32(Console.ReadLine());


                        KuantumNesnesi n = turSecim switch
                        {
                            1 => new VeriPaketi(),
                            2 => new KaranlikMadde(),
                            3 => new AntiMadde(),
                            4 => RastgeleNesneOlustur(),
                            


                        };


                        if (n == null)
                        {
                            Console.WriteLine("Geçersiz seçim.");
                            continue;
                        }

                        if (turSecim == 1 || turSecim == 2 || turSecim == 3)
                        {
                            Console.Write("ID girin: ");
                            n.id = Console.ReadLine();


                            Console.Write("Stabilite (0-100) girin: ");
                            n.stabilite = double.Parse(Console.ReadLine());


                            Console.Write("Tehlike Seviyesi (1-10) girin: ");
                            n.tehlikeSeviyesi = int.Parse(Console.ReadLine());

                            Console.WriteLine();
                            Console.WriteLine($"----{n.id} ID'li nesne oluşturuldu.---- \n ");

                        }
                        envanter.Add(n);
                        
                        Console.WriteLine("...........................................................................................................");



                    }

                    else if (secim == 2)//envanteri listeleme
                    {
                        Console.WriteLine("---Envanter Listesi---");
                        if (envanter.Count == 0)
                        {
                            Console.WriteLine("Envanter boş.");
                        }
                        else
                        {
                            foreach (var x in envanter)
                                Console.WriteLine(x.DurumBilgisi());
                        }
                        Console.WriteLine("----------------------");
                        Console.WriteLine("...........................................................................................................");
                    }

                    else if (secim == 3) // nesneyi id isteyrerk analiz et
                    {
                        Console.Write("Analiz edilecek ID: ");
                        string id = Console.ReadLine();
                        Console.WriteLine();

                        var nesne = envanter.Find(x => x.id == id);
                        if (nesne == null)
                        {
                            Console.WriteLine("Nesne bulunamadı!");
                            continue;
                        }
                        nesne.AnalizEt();

                        if (nesne.stabilite <= 0)
                        {
                            throw new KuantumCokusuException(id);
                        }
                        Console.WriteLine("...........................................................................................................");

                    }

                    else if (secim == 4)  //acil durum soğutması yap
                    {
                        Console.Write("Soğutma yapılacak ID: ");
                        string id = Console.ReadLine();
                        Console.WriteLine();

                        var nesne = envanter.Find(x => x.id == id);
                        if (nesne == null)
                        {
                            Console.WriteLine("Nesne Bulunamadı!");
                            continue;
                        }
                        if (nesne is IKritik kritikNesne)
                        {
                            kritikNesne.AcilDurumSogutmasi();

                        }
                        else
                        {
                            Console.WriteLine(" ! Bu nesne soğutulamaz.");
                        }
                        Console.WriteLine("...........................................................................................................");
                    }

                    else if (secim == 5)
                    {
                        return;
                    }

                } catch(KuantumCokusuException ex)
                {
                    Console.BackgroundColor = ConsoleColor.Red;
                    Console.ForegroundColor = ConsoleColor.White;
                    Console.Clear();
                    Console.WriteLine("\n**************************************************");
                    Console.WriteLine("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                    Console.WriteLine($"HATA NEDENİ: {ex.Message}");
                    Console.WriteLine("**************************************************");
                    Console.ResetColor();
                    menu = false; // Döngüyü kırar ve programı bitirir
                }
                   
                     
                    
                




            }






        }

        private static KuantumNesnesi RastgeleNesneOlustur()
        {
            Random r = new Random();

            // Rastgele tür seç (0=VeriPaketi, 1=KaranlikMadde, 2=AntiMadde)
            int tur = r.Next(0, 3);

            KuantumNesnesi n = tur switch
            {
                0 => new VeriPaketi(),
                1 => new KaranlikMadde(),
                2 => new AntiMadde(),
                _ => new VeriPaketi()
            };

            // Rastgele ID
            n.id = "RND-" + r.Next(1, 21);

            // Rastgele stabilite (10 ile 100 arası olsun, sıfır gelirse direkt patlar)
            n.stabilite = r.Next(10, 101);

            // Rastgele tehlike seviyesi (1-10)
            n.tehlikeSeviyesi = r.Next(1, 11);
            Console.WriteLine();
            Console.WriteLine($"Rastgele nesne oluşturuldu  Tür: {n.GetType().Name}, ID: {n.id} ");

            return n;
        }
    }
}
