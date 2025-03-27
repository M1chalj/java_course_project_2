package systemtransakcyjny;

import java.util.PriorityQueue;

public class WykonywanieZleceniaSprzedaży implements WykonywanieZlecenia {

    @Override
    public void wykonajUInwestora(Inwestor inwestor, IdSpółki idAkcji, int ileAkcji, int cena) {
        inwestor.portfel().dodajGotówkę(ileAkcji * cena);
        inwestor.portfel().odejmijAkcje(idAkcji, ileAkcji);
    }

    @Override
    public void cofnijWykonanieUInwestora(Inwestor inwestor, IdSpółki idAkcji, int ileAkcji, int cena) {
        inwestor.portfel().odejmijGotówkę(ileAkcji * cena);
        inwestor.portfel().dodajAkcje(idAkcji, ileAkcji);
    }

    @Override
    public void dodajNaMojąKolejkę(Zlecenie zlecenie, PriorityQueue<Zlecenie> zleceniaSprzedaży,
                                   PriorityQueue<Zlecenie> zleceniaKupna) {
        zleceniaSprzedaży.add(zlecenie);
    }

    @Override
    public void dodajNaNieMojąKolejkę(Zlecenie zlecenie, PriorityQueue<Zlecenie> zleceniaSprzedaży,
                                      PriorityQueue<Zlecenie> zleceniaKupna) {
        zleceniaKupna.add(zlecenie);
    }

    @Override
    public boolean nieMojaKolejkaPusta(PriorityQueue<Zlecenie> zleceniaSprzedaży,
                                       PriorityQueue<Zlecenie> zleceniaKupna) {
        return zleceniaKupna.isEmpty();
    }

    @Override
    public Zlecenie zdejmijZNieMojejKolejki(PriorityQueue<Zlecenie> zleceniaSprzedaży,
                                            PriorityQueue<Zlecenie> zleceniaKupna) {
        return zleceniaKupna.remove();
    }
}
