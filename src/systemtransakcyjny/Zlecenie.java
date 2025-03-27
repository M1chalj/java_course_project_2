package systemtransakcyjny;

import java.util.PriorityQueue;

public abstract class Zlecenie implements Comparable<Zlecenie> {

    private static int licznikZleceń = 0;

    private final int numer;
    private final TypZlecenia typ;
    private final IdSpółki idAkcji;
    private final int cena;
    private final Inwestor inwestor;
    private final SystemTransakcyjny giełda;
    private int liczbaAkcji;
    private final WykonywanieZlecenia wykonywanieZlecenia;

    public Zlecenie(SystemTransakcyjny giełda, TypZlecenia typ, IdSpółki idAkcji, int liczbaAkcji,
                    int cena, Inwestor inwestor) {
        this.numer = licznikZleceń++;
        this.typ = typ;
        this.idAkcji = idAkcji;
        this.cena = cena;
        this.inwestor = inwestor;
        this.giełda = giełda;
        this.liczbaAkcji = liczbaAkcji;
        if (typ == TypZlecenia.KUPNO) {
            wykonywanieZlecenia = new WykonywanieZleceniaKupna();
        } else {
            wykonywanieZlecenia = new WykonywanieZleceniaSprzedaży();
        }
    }

    protected int liczbaAkcji() {
        return liczbaAkcji;
    }

    public boolean pasujeZ(Zlecenie z) {
        if (z.typ == typ || !(z.idAkcji.equals(idAkcji))) {
            return false;
        } else if (typ == TypZlecenia.SPRZEDAŻ) {
            return cena <= z.cena;
        } else {
            return cena >= z.cena;
        }
    }

    public IdSpółki idAkcji() {
        return idAkcji;
    }

    public TypZlecenia typ() {
        return typ;
    }

    public boolean realizowalne() {
        Portfel portfel = inwestor.portfel();
        if (typ == TypZlecenia.KUPNO) {
            return portfel.maGotówkę(liczbaAkcji * cena);
        } else {
            return portfel.maAkcje(idAkcji, liczbaAkcji);
        }
    }

    public boolean cenaWPrzedziale(int minimum, int maksimum) {
        return minimum <= cena && cena <= maksimum;
    }

    public boolean spróbujWykonać(PriorityQueue<Zlecenie> zleceniaSprzedaży,
                                  PriorityQueue<Zlecenie> zleceniaKupna) {
        if (!realizowalne() || wykonywanieZlecenia.nieMojaKolejkaPusta(zleceniaSprzedaży, zleceniaKupna)) {
            return false;
        }
        Zlecenie zlecenie;
        zlecenie = wykonywanieZlecenia.zdejmijZNieMojejKolejki(zleceniaSprzedaży, zleceniaKupna);

        if (!zlecenie.realizowalne() || !pasujeZ(zlecenie)) {
            wykonywanieZlecenia.dodajNaNieMojąKolejkę(zlecenie, zleceniaSprzedaży, zleceniaKupna);
            return false;
        }

        int ileAkcji = Math.min(zlecenie.liczbaAkcji(), liczbaAkcji());
        int cena;
        if (compareTo(zlecenie) < 0) {
            cena = this.cena;
        } else {
            cena = zlecenie.cena;
        }

        int ostatniaCena = giełda.spółka(idAkcji).ostatniaCena();

        giełda.spółka(idAkcji).ostatniaCena(cena);
        wykonaj(ileAkcji, cena);
        zlecenie.wykonaj(ileAkcji, cena);

        if (!wykonajDalej(zleceniaSprzedaży, zleceniaKupna)
                || !zlecenie.wykonajDalej(zleceniaSprzedaży, zleceniaKupna)) {
            cofnijWykonanie(ileAkcji, cena);
            zlecenie.cofnijWykonanie(ileAkcji, cena);

            giełda.spółka(idAkcji).ostatniaCena(ostatniaCena);
            wykonywanieZlecenia.dodajNaNieMojąKolejkę(zlecenie, zleceniaSprzedaży, zleceniaKupna);
            return false;
        }
        return true;
    }

    private void cofnijWykonanie(int ileAkcji, int cena) {
        wykonywanieZlecenia.cofnijWykonanieUInwestora(inwestor, idAkcji, ileAkcji, cena);
        liczbaAkcji += ileAkcji;
    }

    private void wykonaj(int ileAkcji, int cena) {
        wykonywanieZlecenia.wykonajUInwestora(inwestor, idAkcji, ileAkcji, cena);
        liczbaAkcji -= ileAkcji;
    }

    protected WykonywanieZlecenia wykonywanieZlecenia() {
        return wykonywanieZlecenia;
    }

    protected abstract boolean wykonajDalej(PriorityQueue<Zlecenie> zleceniaSprzedaży, PriorityQueue<Zlecenie> zleceniaKupna);

    public abstract boolean ostatniaTuraWażności(int tura);


    @Override
    public int compareTo(Zlecenie z) {
        if (typ == TypZlecenia.KUPNO && z.typ == TypZlecenia.KUPNO) {
            if (cena != z.cena) {
                return Integer.compare(z.cena, cena);
            }
        } else if (typ == TypZlecenia.SPRZEDAŻ && z.typ == TypZlecenia.SPRZEDAŻ) {
            if (cena != z.cena) {
                return Integer.compare(cena, z.cena);
            }
        }
        return Integer.compare(numer, z.numer);
    }

    @Override
    public String toString() {
        return "Zlecenie nr " + numer + " zgłoszone przez " + inwestor +
                " typ: " + typ + " akcje: " + idAkcji + " " + liczbaAkcji + " limit ceny: " + cena;
    }
}
