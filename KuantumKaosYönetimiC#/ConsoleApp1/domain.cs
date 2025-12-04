using System;
using System.Collections.Generic;
using System.ComponentModel.Design;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KuantumKaosYönetimi
{
    public class Domain
    {
        public abstract class KuantumNesnesi
        {
            private string ID;
            private double Stabilite;
            private int TehlikeSeviyesi;

            public abstract void AnalizEt();
            public string DurumBilgisi()
            {
                return ($"ID: {id} , Stabilite: {stabilite}");
            }

            public string id
            {
                get { return ID; }
                set { ID = value; }
            }

            public double stabilite
            {
                get { return Stabilite; }
                set
                {
                    if (value <= 0) Stabilite = 0;
                    else if (value > 100) Stabilite = 100;
                    else Stabilite = value;
                    

                }
            }

            public int tehlikeSeviyesi
            {
                get { return TehlikeSeviyesi; }
                set
                {
                    if (value < 0) TehlikeSeviyesi = 0;
                    else if (value > 10) TehlikeSeviyesi = 10;
                    else TehlikeSeviyesi = value;
                }
            }
        }

        public interface IKritik
        {
            void AcilDurumSogutmasi();
        }

        public class VeriPaketi : KuantumNesnesi
        {
            public override void AnalizEt()
            {
                stabilite -= 5;
                Console.WriteLine("Veri içeriği okundu.");
                if(stabilite <= 0)
                {
                    throw new KuantumCokusuException(id);
                }
                
            }
        }

        public class KaranlikMadde : KuantumNesnesi, IKritik
        {
            public override void AnalizEt()
            {
                stabilite -= 15;
                Console.WriteLine("Karanlık madde analiz edildi.");
                if (stabilite <= 0)
                {
                    throw new KuantumCokusuException(id);
                }
            }

            public void AcilDurumSogutmasi()
            {
                stabilite += 50;
                if (stabilite > 100) { stabilite = 100; }
                else stabilite = stabilite;
                Console.WriteLine("##Acil soğutma uygulandı");
            }

        }

        public class AntiMadde : KuantumNesnesi, IKritik
        {
            public override void AnalizEt()
            {
                stabilite -= 25;
                Console.WriteLine("\tEvrenin dokusu titriyor...");
                if (stabilite <= 0)
                {
                    throw new KuantumCokusuException(id);
                }
            }

            public void AcilDurumSogutmasi()
            {
                stabilite += 50;
                if (stabilite > 100) { stabilite = 100; }
                else stabilite = stabilite;
                Console.WriteLine("##Acil soğutma uygulandı");
            }
        }

        
    }
}
