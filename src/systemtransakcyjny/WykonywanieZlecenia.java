package systemtransakcyjny;

import java.util.PriorityQueue;

public interface WykonywanieZlecenia {

    void wykonajUInwestora(Inwestor inwestor, IdSpółki idAkcji, int ileAkcji, int cena);

    void cofnijWykonanieUInwestora(Inwestor inwestor, IdSpółki idAkcji, int ileAkcji, int cena);

    void dodajNaMojąKolejkę(Zlecenie zlecenie, PriorityQueue<Zlecenie> zleceniaSprzedaży,
                            PriorityQueue<Zlecenie> zleceniaKupna);

    void dodajNaNieMojąKolejkę(Zlecenie zlecenie, PriorityQueue<Zlecenie> zleceniaSprzedaży,
                               PriorityQueue<Zlecenie> zleceniaKupna);

    boolean nieMojaKolejkaPusta(PriorityQueue<Zlecenie> zleceniaSprzedaży,
                                PriorityQueue<Zlecenie> zleceniaKupna);

    Zlecenie zdejmijZNieMojejKolejki(PriorityQueue<Zlecenie> zleceniaSprzedaży,
                                     PriorityQueue<Zlecenie> zleceniaKupna);
}