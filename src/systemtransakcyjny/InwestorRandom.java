package systemtransakcyjny;

import main.Losowanie;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InwestorRandom extends Inwestor {

    public InwestorRandom(SystemTransakcyjny giełda, Portfel portfel) {
        super(giełda, portfel);
    }

    @Override
    public Optional<Zlecenie> podejmijDecyzję() {

        TypZlecenia typZlecenia = TypZlecenia.losowy();
        List<IdSpółki> możliweSpółki;

        if (typZlecenia == TypZlecenia.KUPNO) {
            możliweSpółki = new ArrayList<>(giełda().idSpółek());
        } else {
            możliweSpółki = new ArrayList<>(portfel().idSpółek());
        }
        if (możliweSpółki.isEmpty()) {
            return Optional.empty();
        }

        IdSpółki idSpółki = możliweSpółki.get(Losowanie.losuj(0, możliweSpółki.size() - 1));
        int ostatniaCena = giełda().spółka(idSpółki).ostatniaCena();
        int cena = Losowanie.losuj(Math.max(ostatniaCena - MAKS_RÓŻNICA_W_CENIE, 1),
                ostatniaCena + MAKS_RÓŻNICA_W_CENIE);

        int liczbaAkcji;
        if (typZlecenia == TypZlecenia.SPRZEDAŻ) {
            liczbaAkcji = Losowanie.losuj(1, portfel().ileAkcji(idSpółki));
        } else {
            if (portfel().ileGotówki() < cena) {
                return Optional.empty();
            }
            liczbaAkcji = Losowanie.losuj(1, portfel().ileGotówki() / cena);
        }

        Zlecenie zlecenie = switch (Losowanie.losuj(1, 4)) {
            case 1 -> new ZlecenieBezterminowe(giełda(), typZlecenia, idSpółki, liczbaAkcji,
                    cena, this);
            case 2 -> new ZlecenieNatychmiastowe(giełda(), typZlecenia, idSpółki, liczbaAkcji,
                    cena, this, giełda().numerTury());
            case 3 -> new ZlecenieWykonajLubAnuluj(giełda(), typZlecenia, idSpółki, liczbaAkcji,
                    cena, this);
            case 4 -> {
                int termin = giełda().numerTury() + Losowanie.losuj(1, MAKS_DŁ_WAŻNOŚCI_ZGŁOSZENIA);
                yield new ZlecenieZTerminemWażności(giełda(), typZlecenia, idSpółki, liczbaAkcji,
                        cena, this, termin);
            }
            default -> null;
        };
        return Optional.ofNullable(zlecenie);
    }
}
