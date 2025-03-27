package systemtransakcyjny;

public class ZlecenieBezterminowe extends ZleceniePodzielne {
    public ZlecenieBezterminowe(SystemTransakcyjny giełda, TypZlecenia typ, IdSpółki idAkcji, int liczbaAkcji, int cena, Inwestor inwestor) {
        super(giełda, typ, idAkcji, liczbaAkcji, cena, inwestor);
    }

    @Override
    public boolean ostatniaTuraWażności(int tura) {
        return false;
    }
}
