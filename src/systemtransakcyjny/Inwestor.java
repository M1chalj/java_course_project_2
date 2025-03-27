package systemtransakcyjny;

import java.util.Optional;

public abstract class Inwestor {
    protected static final int MAKS_RÓŻNICA_W_CENIE = 10;
    protected static final int MAKS_DŁ_WAŻNOŚCI_ZGŁOSZENIA = 100;

    private static int licznikInwestorów = 0;

    private final int numer;
    private final SystemTransakcyjny giełda;
    private final Portfel portfel;

    public Inwestor(SystemTransakcyjny giełda, Portfel portfel) {
        numer = licznikInwestorów++;
        this.giełda = giełda;
        this.portfel = portfel;
    }

    protected SystemTransakcyjny giełda() {
        return giełda;
    }

    public Portfel portfel() {
        return portfel;
    }

    @Override
    public String toString() {
        return "Inwestor " + numer;
    }

    public abstract Optional<Zlecenie> podejmijDecyzję();
}
