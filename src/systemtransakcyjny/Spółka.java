package systemtransakcyjny;

import java.util.PriorityQueue;

public class Spółka {

    private final IdSpółki id;
    private final PriorityQueue<Zlecenie> zleceniaKupna;
    private final PriorityQueue<Zlecenie> zleceniaSprzedaży;
    private int ostatniaCena;

    public Spółka(IdSpółki id, int ostatniaCena) {
        this.id = id;
        this.ostatniaCena = ostatniaCena;
        zleceniaSprzedaży = new PriorityQueue<>();
        zleceniaKupna = new PriorityQueue<>();
    }

    public void dodajZlecenie(Zlecenie zlecenie) {
        if (zlecenie.typ() == TypZlecenia.KUPNO) {
            zleceniaKupna.add(zlecenie);
        } else {
            zleceniaSprzedaży.add(zlecenie);
        }
    }

    public void wykonajZlecenia() {
        while (!zleceniaKupna.isEmpty() && !zleceniaSprzedaży.isEmpty()
                && zleceniaKupna.peek().pasujeZ(zleceniaSprzedaży.peek())) {
            if (zleceniaSprzedaży.peek().compareTo(zleceniaKupna.peek()) < 0) {
                zleceniaSprzedaży.remove().spróbujWykonać(zleceniaSprzedaży, zleceniaKupna);
            } else {
                zleceniaKupna.remove().spróbujWykonać(zleceniaSprzedaży, zleceniaKupna);
            }
        }
    }

    public void wyrzućNieważneZlecenia(int tura) {
        zleceniaKupna.removeIf(zlecenie -> zlecenie.ostatniaTuraWażności(tura));
    }

    public int ostatniaCena() {
        return ostatniaCena;
    }

    public void ostatniaCena(int cena) {
        ostatniaCena = cena;
    }

    public IdSpółki id() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
