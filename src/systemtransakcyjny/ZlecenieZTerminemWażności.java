package systemtransakcyjny;

public class ZlecenieZTerminemWażności extends ZleceniePodzielne {
    private final int turaWażności;

    public ZlecenieZTerminemWażności(SystemTransakcyjny giełda, TypZlecenia typ, IdSpółki idAkcji, int liczbaAkcji, int cena, Inwestor inwestor, int turaWażności) {
        super(giełda, typ, idAkcji, liczbaAkcji, cena, inwestor);
        this.turaWażności = turaWażności;
    }

    @Override
    public boolean ostatniaTuraWażności(int tura) {
        return tura == turaWażności;
    }
}
