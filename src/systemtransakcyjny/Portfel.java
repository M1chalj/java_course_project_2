package systemtransakcyjny;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Portfel {
    private final Map<IdSpółki, Integer> akcje;
    private int gotówka;

    public Portfel(int gotówka) {
        this.gotówka = gotówka;
        akcje = new HashMap<>();
    }

    public Portfel(Portfel portfel) { //konstruktor kopiujący
        this.akcje = new HashMap<>(portfel.akcje);
        this.gotówka = portfel.gotówka;
    }

    public void dodajGotówkę(int gotówka) {
        this.gotówka += gotówka;
    }

    public void odejmijGotówkę(int gotówka) {
        if (this.gotówka < gotówka) {
            throw new IllegalArgumentException("Usunięcie gotówki, której nie ma w portfelu");
        }
        this.gotówka -= gotówka;
    }

    public void dodajAkcje(IdSpółki id, int liczba) {
        if (akcje.containsKey(id)) {
            liczba += akcje.get(id);
        }
        akcje.put(id, liczba);
    }

    public void odejmijAkcje(IdSpółki id, int liczba) {
        if (!akcje.containsKey(id) || akcje.get(id) < liczba) {
            throw new IllegalArgumentException("Usunięcie akcji, których nie ma w portfelu");
        }
        liczba = akcje.get(id) - liczba;
        if (liczba == 0) {
            akcje.remove(id);
        } else {
            akcje.put(id, liczba);
        }
    }

    public Set<IdSpółki> idSpółek() {
        return akcje.keySet();
    }

    public int ileAkcji(IdSpółki idSpółki) {
        return akcje.getOrDefault(idSpółki, 0);
    }

    public int ileGotówki() {
        return gotówka;
    }

    public boolean maGotówkę(int ile) {
        return gotówka >= ile;
    }

    public boolean maAkcje(IdSpółki id, int ile) {
        return akcje.containsKey(id) && akcje.get(id) >= ile;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gotówka);
        for (Map.Entry<IdSpółki, Integer> elem : akcje.entrySet()) {
            stringBuilder.append(" ");
            stringBuilder.append(elem.getKey());
            stringBuilder.append(":");
            stringBuilder.append(elem.getValue());
        }
        return stringBuilder.toString();
    }
}
