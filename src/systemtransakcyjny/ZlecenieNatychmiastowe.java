package systemtransakcyjny;

public class ZlecenieNatychmiastowe extends ZlecenieZTerminemWażności {

    public ZlecenieNatychmiastowe(SystemTransakcyjny giełda, TypZlecenia typ, IdSpółki idAkcji, int liczbaAkcji, int cena, Inwestor inwestor, int aktualnaTura) {
        super(giełda, typ, idAkcji, liczbaAkcji, cena, inwestor, aktualnaTura);
    }
}
