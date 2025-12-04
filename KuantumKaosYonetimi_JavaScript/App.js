const prompt = require("prompt-sync")({ sigint: true });

// ---- Exception ----
class KuantumCokusuException extends Error {
    constructor(id) {
        super(`Kuantum çöküşü gerçekleşti! Patlayan nesne ID: ${id}`);
        this.name = "KuantumCokusuException";
    }
}

// ---- Abstract Base Class ----
class KuantumNesnesi {
    constructor() {
        if (new.target === KuantumNesnesi) {
            throw new Error("KuantumNesnesi abstract bir sınıftır.");
        }
        this._id = "";
        this._stabilite = 0;
        this._tehlikeSeviyesi = 0;
    }

    AnalizEt() {
        throw new Error("AnalizEt metodu override edilmelidir.");
    }

    DurumBilgisi() {
        return `ID: ${this._id} , Stabilite: ${this._stabilite}`;
    }

    get id() { return this._id; }
    set id(v) { this._id = v; }

    get stabilite() { return this._stabilite; }
    set stabilite(v) {
        if (v <= 0) this._stabilite = 0;
        else if (v > 100) this._stabilite = 100;
        else this._stabilite = v;
    }

    get tehlikeSeviyesi() { return this._tehlikeSeviyesi; }
    set tehlikeSeviyesi(v) {
        if (v < 0) this._tehlikeSeviyesi = 0;
        else if (v > 10) this._tehlikeSeviyesi = 10;
        else this._tehlikeSeviyesi = v;
    }
}



// ---- VeriPaketi ----
class VeriPaketi extends KuantumNesnesi {
    AnalizEt() {
        this.stabilite -= 5;
        console.log("Veri içeriği okundu.");

        if (this.stabilite <= 0) {
            throw new KuantumCokusuException(this.id);
        }
    }
}

// ---- KaranlikMadde ----
class KaranlikMadde extends KuantumNesnesi {
    AnalizEt() {
        this.stabilite -= 15;
        console.log("Karanlık madde analiz edildi.");

        if (this.stabilite <= 0) {
            throw new KuantumCokusuException(this.id);
        }
    }

    AcilDurumSogutmasi() {
        this.stabilite += 50;
        if (this.stabilite > 100) this.stabilite = 100;

        console.log("##Acil soğutma uygulandı");
    }
}

// ---- AntiMadde ----
class AntiMadde extends KuantumNesnesi {
    AnalizEt() {
        this.stabilite -= 25;
        console.log("\tEvrenin dokusu titriyor...");

        if (this.stabilite <= 0) {
            throw new KuantumCokusuException(this.id);
        }
    }

    AcilDurumSogutmasi() {
        this.stabilite += 50;
        if (this.stabilite > 100) this.stabilite = 100;

        console.log("##Acil soğutma uygulandı");
    }
}

// ---- Rastgele nesne oluşturma ----
function RastgeleNesneOlustur() {
    const r = Math.floor(Math.random() * 3);

    let n =
        r === 0 ? new VeriPaketi() :
        r === 1 ? new KaranlikMadde() :
                  new AntiMadde();

    n.id = "RND-" + (Math.floor(Math.random() * 20) + 1);
    n.stabilite = Math.floor(Math.random() * 91) + 10;
    n.tehlikeSeviyesi = Math.floor(Math.random() * 10) + 1;

    console.log(`\nRastgele nesne oluşturuldu  Tür: ${n.constructor.name}, ID: ${n.id}`);

    return n;
}

// ---- MAIN PROGRAM ----
function main() {
    let envanter = [];
    let menu = true;

    while (menu) {
        try {
            console.log("\n--- KUANTUM AMBARI KONTROL PANELİ ---\n");
            console.log("1. Yeni Nesne Ekle");
            console.log("2. Envanteri Listele");
            console.log("3. Nesneyi Analiz Et");
            console.log("4. Acil Durum Soğutması (IKritik)");
            console.log("5. Çıkış\n");

            let secim = Number(prompt("Seçiminiz: "));
            console.log();

            if (secim === 1) {

                console.log("Hangi nesneyi oluşturmak istersiniz?");
                console.log("1) VeriPaketi");
                console.log("2) KaranlikMadde");
                console.log("3) AntiMadde");
                console.log("4) Rastgele Veri");
                let t = Number(prompt("Seçim: "));

                let n = null;

                if (t === 1) n = new VeriPaketi();
                else if (t === 2) n = new KaranlikMadde();
                else if (t === 3) n = new AntiMadde();
                else if (t === 4) n = RastgeleNesneOlustur();

                if (t !== 4) {
                    n.id = prompt("ID girin: ");
                    n.stabilite = Number(prompt("Stabilite (0-100): "));
                    n.tehlikeSeviyesi = Number(prompt("Tehlike Seviyesi (1-10): "));
                }

                envanter.push(n);
                console.log(`\n---- ${n.id} ID'li nesne oluşturuldu. ----`);
            }

            else if (secim === 2) {
                console.log("\n--- Envanter ---");
                if (envanter.length === 0) console.log("Envanter boş.");
                else envanter.forEach(n => console.log(n.DurumBilgisi()));
            }

            else if (secim === 3) {
                let id = prompt("Analiz edilecek ID: ");
                console.log();

                let nesne = envanter.find(x => x.id === id);
                if (!nesne) {
                    console.log("Nesne bulunamadı!");
                    continue;
                }

                nesne.AnalizEt();

                if (nesne.stabilite <= 0) {
                    throw new KuantumCokusuException(id);
                }
            }

            else if (secim === 4) {
                let id = prompt("Soğutma yapılacak ID: ");
                console.log();

                let nesne = envanter.find(x => x.id === id);
                if (!nesne) {
                    console.log("Nesne bulunamadı!");
                    continue;
                }

                if (typeof nesne.AcilDurumSogutmasi === "function") {
                    nesne.AcilDurumSogutmasi();
                } else {
                    console.log("Bu nesne soğutulamaz!");
                }
            }

            else if (secim === 5) {
                console.log("Çıkılıyor...");
                return;
            }

        } catch (ex) {
            if (ex instanceof KuantumCokusuException) {
                console.clear();
                console.log("\n**************************************************");
                console.log("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                console.log("HATA NEDENİ: " + ex.message);
                console.log("**************************************************");
                menu = false;
            } else {
                console.log("Beklenmeyen Hata: " + ex.message);
            }
        }
    }
}


main();
