package tradingsystem;

import java.util.PriorityQueue;

public class Company {

    private final CompanyId id;
    private final PriorityQueue<Offer> zleceniaKupna;
    private final PriorityQueue<Offer> zleceniaSprzedaży;
    private int ostatniaCena;

    public Company(CompanyId id, int ostatniaCena) {
        this.id = id;
        this.ostatniaCena = ostatniaCena;
        zleceniaSprzedaży = new PriorityQueue<>();
        zleceniaKupna = new PriorityQueue<>();
    }

    public void dodajZlecenie(Offer offer) {
        if (offer.type() == OfferType.BUY) {
            zleceniaKupna.add(offer);
        } else {
            zleceniaSprzedaży.add(offer);
        }
    }

    public void wykonajZlecenia() {
        while (!zleceniaKupna.isEmpty() && !zleceniaSprzedaży.isEmpty()
                && zleceniaKupna.peek().matchWith(zleceniaSprzedaży.peek())) {
            if (zleceniaSprzedaży.peek().compareTo(zleceniaKupna.peek()) < 0) {
                zleceniaSprzedaży.remove().tryExecute(zleceniaSprzedaży, zleceniaKupna);
            } else {
                zleceniaKupna.remove().tryExecute(zleceniaSprzedaży, zleceniaKupna);
            }
        }
    }

    public void wyrzućNieważneZlecenia(int tura) {
        zleceniaKupna.removeIf(zlecenie -> zlecenie.expirationRound(tura));
    }

    public int lastPrice() {
        return ostatniaCena;
    }

    public void lastPrice(int cena) {
        ostatniaCena = cena;
    }

    public CompanyId id() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
