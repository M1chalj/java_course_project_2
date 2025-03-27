package systemtransakcyjny;

import java.util.PriorityQueue;

public abstract class ZlecenieNiepodzielne extends Zlecenie {
    public ZlecenieNiepodzielne(SystemTransakcyjny giełda, TypZlecenia typ, IdSpółki idAkcji, int liczbaAkcji, int cena, Inwestor inwestor) {
        super(giełda, typ, idAkcji, liczbaAkcji, cena, inwestor);
    }

    @Override
    protected boolean wykonajDalej(PriorityQueue<Zlecenie> zleceniaSprzedaży, PriorityQueue<Zlecenie> zleceniaKupna) {
        if (liczbaAkcji() == 0) {
            return true;
        }
        return spróbujWykonać(zleceniaSprzedaży, zleceniaKupna);
    }
}
