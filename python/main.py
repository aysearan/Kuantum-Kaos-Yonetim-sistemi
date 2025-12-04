from abc import ABC, abstractmethod

class KuantumNesnesi(ABC):   # abstract class
    def __init__(self):
        self._ID = None
        self._Stabilite = 0.0
        self._TehlikeSeviyesi = 0

    @abstractmethod          # abstracct metot
    def AnalizEt(self):
        pass

    def DurumBilgisi(self):
        return f"ID: {self.id} , Stabilite: {self.stabilite}"

    @property
    def id(self):
        return self._ID

    @id.setter
    def id(self, value):
        self._ID = value
    @property
    def stabilite(self):
        return self._Stabilite

    @stabilite.setter
    def stabilite(self, value):
        if value <= 0:
            self._Stabilite = 0
        elif value > 100:
            self._Stabilite = 100
        else:
            self._Stabilite = value

    @property
    def tehlikeSeviyesi(self):
        return self._TehlikeSeviyesi

    @tehlikeSeviyesi.setter
    def tehlikeSeviyesi(self, value):
        if value < 0:
            self._TehlikeSeviyesi = 0
        elif value > 10:
            self._TehlikeSeviyesi = 10
        else:
            self._TehlikeSeviyesi = value


class IKritik(ABC):               #interface yerine bu
    @abstractmethod
    def AcilDurumSogutmasi(self):
        pass


class KuantumCokusuException(Exception):        # hata sınıfı
    def __init__(self, id):
        super().__init__(f"Kuantum çöküşü gerçekleşti! Patlayan nesne ID: {id}")


class VeriPaketi(KuantumNesnesi):             #yavru sınıf (veri paketi)
    def AnalizEt(self):
        self.stabilite -= 5
        print("Veri içeriği okundu.")
        if self.stabilite <= 0:
            raise KuantumCokusuException(self.id)


class KaranlikMadde(KuantumNesnesi, IKritik):        #yavru sınıf(Karanlık madde)
    def AnalizEt(self):
        self.stabilite -= 15
        print("Karanlık madde analiz edildi.")
        if self.stabilite <= 0:
            raise KuantumCokusuException(self.id)

    def AcilDurumSogutmasi(self):
        self.stabilite += 50
        if self.stabilite > 100:
            self.stabilite = 100
        print("##Acil soğutma uygulandı")


class AntiMadde(KuantumNesnesi, IKritik):          # yavru sınıf (anti madde)
    def AnalizEt(self):
        self.stabilite -= 25
        print("\tEvrenin dokusu titriyor...")
        if self.stabilite <= 0:
            raise KuantumCokusuException(self.id)

    def AcilDurumSogutmasi(self):
        self.stabilite += 50
        if self.stabilite > 100:
            self.stabilite = 100
        print("##Acil soğutma uygulandı")


class KuantumCokusuException(Exception):        # hata sınıfı
    def __init__(self, id):
        super().__init__(f"Kuantum çöküşü gerçekleşti! Patlayan nesne ID: {id}")


import random

def RastgeleNesneOlustur():        # random nesne oluşturma metodu
    r = random.Random()

    # Rastgele tür seç (0=VeriPaketi, 1=KaranlikMadde, 2=AntiMadde)
    tur = r.randint(0, 2)

    if tur == 0:
        n = VeriPaketi()
    elif tur == 1:
        n = KaranlikMadde()
    elif tur == 2:
        n = AntiMadde()
    else:
        n = VeriPaketi()

    # Rastgele ID
    n.id = f"RND-{r.randint(1,21)}"

    # Rastgele stabilite (10 ile 100 arası olsun, sıfır gelirse direkt patlar)
    n.stabilite = r.randint(10, 100)

    # Rastgele tehlike seviyesi (1-10)
    n.tehlikeSeviyesi = r.randint(1, 10)

    print()
    print(f"Rastgele nesne oluşturuldu  Tür: {type(n).__name__}, ID: {n.id}")

    return n





    
    # --- Main program ---
def main():
    envanter = []
    menu = True

    while menu:
        try:
            print("\n                                      KUANTUM AMBARI KONTROL PANELİ             \n")
            print("1. Yeni Nesne Ekle \n2. Tüm Envanteri Listele \n3. Nesneyi Analiz Et \n4. Acil Durum Soğutması Yap (Sadece IKritik olanlar için!)\n5. Çıkış\n")
            secim = input("Seçiminiz: ")
            secim = int(secim)
            print()

            if secim == 1:
                print("Hangi nesneyi oluşturmak istersiniz?  1)VeriPaketi 2)KaranlıkMadde 3)AntiMadde  4)Rastgele Veri")
                turSecim = int(input("Seçiminiz: "))

                n = None
                if turSecim == 1:
                    n = VeriPaketi()
                elif turSecim == 2:
                    n = KaranlikMadde()
                elif turSecim == 3:
                    n = AntiMadde()
                elif turSecim == 4:
                    n = RastgeleNesneOlustur()

                if n is None:
                    print("Geçersiz seçim.")
                    continue

                if turSecim in [1, 2, 3]:
                    n.id = input("ID girin: ")
                    n.stabilite = float(input("Stabilite (0-100) girin: "))
                    n.tehlikeSeviyesi = int(input("Tehlike Seviyesi (1-10) girin: "))
                    print(f"\n----{n.id} ID'li nesne oluşturuldu.---- \n ")

                envanter.append(n)
                print("...........................................................................................................")

            elif secim == 2:  # Envanteri listeleme
                print("---Envanter Listesi---")
                if len(envanter) == 0:
                    print("Envanter boş.")
                else:
                    for x in envanter:
                        print(x.DurumBilgisi())
                print("----------------------")
                print("...........................................................................................................")

            elif secim == 3:  # Nesneyi analiz et
                id_ = input("Analiz edilecek ID: ")
                print()
                nesne = next((x for x in envanter if x.id == id_), None)
                if nesne is None:
                    print("Nesne bulunamadı!")
                    continue

                nesne.AnalizEt()

                if nesne.stabilite <= 0:
                    raise KuantumCokusuException(id_)

                print("...........................................................................................................")

            elif secim == 4:  # Acil durum soğutması yap
                id_ = input("Soğutma yapılacak ID: ")
                print()
                nesne = next((x for x in envanter if x.id == id_), None)
                if nesne is None:
                    print("Nesne Bulunamadı!")
                    continue

                if isinstance(nesne, IKritik):
                    nesne.AcilDurumSogutmasi()
                else:
                    print(" ! Bu nesne soğutulamaz.")

                print("...........................................................................................................")

            elif secim == 5:
                return

        except KuantumCokusuException as ex:
            
            print("\n")
            print("\n")
            print("\n                 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            print("                         SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...")
            print(f"                        HATA NEDENİ: {ex}")
            print("                   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            print("\n")
            print("\n")

            menu = False  # Döngüyü kırar ve programı bitirir

        except ValueError:
            print("Geçersiz giriş. Lütfen sayı girin.")
        except Exception as ex:
            print(f"Hata oluştu: {ex}")



if __name__ == "__main__":
    main()

