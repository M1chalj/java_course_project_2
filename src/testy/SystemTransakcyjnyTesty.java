package testy;

import org.junit.jupiter.api.Test;
import systemtransakcyjny.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class SystemTransakcyjnyTesty {

    @Test
    public void danePrzykładowe() {
        int ileRandom = 4;
        int ileSMA = 2;
        int liczbaTur = 100_000;
        List<Spółka> spółki = new ArrayList<>();
        spółki.add(new Spółka(new IdSpółki("APL"), 145));
        spółki.add(new Spółka(new IdSpółki("MSFT"), 300));
        spółki.add(new Spółka(new IdSpółki("GOOGL"), 2700));

        Portfel portfelPoczątkowy = new Portfel(100_000);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("APL"), 5);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("MSFT"), 15);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("GOOGL"), 3);

        int sumaGotówki = 600_000;
        Map<IdSpółki, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new IdSpółki("APL"), 30);
        liczbaAkcjiSpółki.put(new IdSpółki("MSFT"), 90);
        liczbaAkcjiSpółki.put(new IdSpółki("GOOGL"), 18);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, portfelPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }

    @Test
    public void inwestorzyRandom() {
        int ileRandom = 2;
        int ileSMA = 0;
        int liczbaTur = 1000;
        List<Spółka> spółki = new ArrayList<>();
        spółki.add(new Spółka(new IdSpółki("A"), 100));
        spółki.add(new Spółka(new IdSpółki("B"), 500));

        Portfel portfelPoczątkowy = new Portfel(10_000);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("A"), 1);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("B"), 400);

        int sumaGotówki = 20_000;
        Map<IdSpółki, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new IdSpółki("A"), 2);
        liczbaAkcjiSpółki.put(new IdSpółki("B"), 800);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, portfelPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }

    @Test
    public void inwestorzySMA() {
        int ileRandom = 0;
        int ileSMA = 2;
        int liczbaTur = 1000;
        List<Spółka> spółki = new ArrayList<>();
        spółki.add(new Spółka(new IdSpółki("A"), 100));
        spółki.add(new Spółka(new IdSpółki("B"), 500));

        Portfel portfelPoczątkowy = new Portfel(10_000);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("A"), 1);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("B"), 400);

        int sumaGotówki = 20_000;
        Map<IdSpółki, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new IdSpółki("A"), 2);
        liczbaAkcjiSpółki.put(new IdSpółki("B"), 800);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, portfelPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }

    @Test
    public void większeDane() {
        int ileRandom = 10;
        int ileSMA = 20;
        int liczbaTur = 20000;
        List<Spółka> spółki = new ArrayList<>();
        spółki.add(new Spółka(new IdSpółki("A"), 100));
        spółki.add(new Spółka(new IdSpółki("B"), 500));
        spółki.add(new Spółka(new IdSpółki("C"), 1700));
        spółki.add(new Spółka(new IdSpółki("D"), 5000));

        Portfel portfelPoczątkowy = new Portfel(10_000);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("A"), 1);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("B"), 400);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("C"), 5);
        portfelPoczątkowy.dodajAkcje(new IdSpółki("D"), 30);

        int sumaGotówki = 300_000;
        Map<IdSpółki, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new IdSpółki("A"), 30);
        liczbaAkcjiSpółki.put(new IdSpółki("B"), 12000);
        liczbaAkcjiSpółki.put(new IdSpółki("C"), 150);
        liczbaAkcjiSpółki.put(new IdSpółki("D"), 900);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, portfelPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }


    private void testuj(int ileRandom, int ileSMA, int liczbaTur, List<Spółka> spółki, Portfel portfelPoczątkowy,
                        Map<IdSpółki, Integer> liczbaAkcjiSpółki, int sumaGotówki) {
        SystemTransakcyjny systemTransakcyjny = new SystemTransakcyjny();
        for (Spółka spółka : spółki) {
            systemTransakcyjny.dodajSpółkę(spółka);
        }

        List<Inwestor> inwestorzy = new ArrayList<>();

        for (int i = 0; i < ileSMA; i++) {
            inwestorzy.add(new InwestorSMA(systemTransakcyjny, new Portfel(portfelPoczątkowy)));
        }
        for (int i = 0; i < ileRandom; i++) {
            inwestorzy.add(new InwestorRandom(systemTransakcyjny, new Portfel(portfelPoczątkowy)));
        }

        for (Inwestor inwestor : inwestorzy) {
            systemTransakcyjny.dodajInwestora(inwestor);
        }

        systemTransakcyjny.symuluj(liczbaTur);
        List<Portfel> portfele = systemTransakcyjny.portfele();

        for (Spółka spółka : spółki) {
            int sumaAkcji = 0;
            for (Portfel portfel : portfele) {
                sumaAkcji += portfel.ileAkcji(spółka.id());
            }

            assertEquals(liczbaAkcjiSpółki.get(spółka.id()), sumaAkcji);
        }

        int wynikowaSumaGotówki = 0;
        for (Portfel portfel : portfele) {
            wynikowaSumaGotówki += portfel.ileGotówki();
        }

        assertEquals(wynikowaSumaGotówki, sumaGotówki);
    }
}
