package systemtransakcyjny;

import main.Losowanie;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Optional;

public class SystemTransakcyjny {
    private static final int MAKSYMALNA_RÓŻNICA_CENY = 10;

    private final Map<IdSpółki, Spółka> spółki;
    private final Set<Inwestor> inwestorzy;
    private int numerTury;

    public SystemTransakcyjny() {
        numerTury = 0;
        spółki = new HashMap<>();
        inwestorzy = new HashSet<>();
    }

    public int numerTury() {
        return numerTury;
    }

    public void dodajInwestora(Inwestor inwestor) {
        inwestorzy.add(inwestor);
    }

    public void dodajSpółkę(Spółka spółka) {
        spółki.put(spółka.id(), spółka);
    }

    public Set<IdSpółki> idSpółek() {
        return spółki.keySet();
    }

    public Spółka spółka(IdSpółki id) {
        return spółki.get(id);
    }

    public void kolejnaTura() {
        List<Inwestor> kolejność = new LinkedList<>(inwestorzy);
        Collections.shuffle(kolejność, Losowanie.generator());

        for (Inwestor inwestor : kolejność) {
            Optional<Zlecenie> decyzja = inwestor.podejmijDecyzję();
            if (decyzja.isPresent()) {
                Zlecenie zlecenie = decyzja.get();
                if (poprawne(zlecenie)) {
                    spółki.get(zlecenie.idAkcji()).dodajZlecenie(zlecenie);
                }
            }
        }

        for (Spółka spółka : spółki.values()) {
            spółka.wykonajZlecenia();
            spółka.wyrzućNieważneZlecenia(numerTury);
        }

        numerTury++;
    }

    public void symuluj(int liczbaTur) {
        while (liczbaTur > 0) {
            kolejnaTura();
            liczbaTur--;
        }
    }

    public List<Portfel> portfele() {
        List<Portfel> lista = new ArrayList<>();
        for (Inwestor inwestor : inwestorzy) {
            lista.add(inwestor.portfel());
        }
        return lista;
    }

    private boolean poprawne(Zlecenie zlecenie) {
        int ostatniaCena = spółki.get(zlecenie.idAkcji()).ostatniaCena();
        return zlecenie.realizowalne() &&
                zlecenie.cenaWPrzedziale(ostatniaCena - MAKSYMALNA_RÓŻNICA_CENY,
                        ostatniaCena + MAKSYMALNA_RÓŻNICA_CENY);
    }
}
