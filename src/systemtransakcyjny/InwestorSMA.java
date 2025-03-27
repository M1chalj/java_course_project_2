package systemtransakcyjny;

import main.Losowanie;

import java.util.*;

public class InwestorSMA extends Inwestor {

    private static final int ROZMIAR_DŁUGIEJ_ŚREDNIEJ = 10;
    private static final int ROZMIAR_KRÓTKIEJ_ŚREDNIEJ = 5;

    private final Map<IdSpółki, SMA> długaŚrednia;
    private final Map<IdSpółki, SMA> krótkaŚrednia;

    public InwestorSMA(SystemTransakcyjny giełda, Portfel portfel) {
        super(giełda, portfel);
        długaŚrednia = new HashMap<>();
        krótkaŚrednia = new HashMap<>();

        for (IdSpółki id : giełda.idSpółek()) {
            długaŚrednia.put(id, new SMA(ROZMIAR_DŁUGIEJ_ŚREDNIEJ));
            krótkaŚrednia.put(id, new SMA(ROZMIAR_KRÓTKIEJ_ŚREDNIEJ));
        }
    }

    @Override
    public Optional<Zlecenie> podejmijDecyzję() {

        List<IdSpółki> sygnałyKupna = new ArrayList<>();
        List<IdSpółki> sygnałySprzedaży = new ArrayList<>();

        for (IdSpółki id : giełda().idSpółek()) {

            double długaPrzed = 0;
            double krótkaPrzed = 0;
            if (giełda().numerTury() >= ROZMIAR_DŁUGIEJ_ŚREDNIEJ) {
                długaPrzed = długaŚrednia.get(id).średnia();
                krótkaPrzed = krótkaŚrednia.get(id).średnia();
            }

            długaŚrednia.get(id).następnaWartość(giełda().spółka(id).ostatniaCena());
            krótkaŚrednia.get(id).następnaWartość(giełda().spółka(id).ostatniaCena());

            double długaPo = 0;
            double krótkaPo = 0;
            if (giełda().numerTury() >= ROZMIAR_DŁUGIEJ_ŚREDNIEJ) {
                długaPo = długaŚrednia.get(id).średnia();
                krótkaPo = krótkaŚrednia.get(id).średnia();
            }

            if (długaPrzed < krótkaPrzed && długaPo >= krótkaPo
                    && portfel().maAkcje(id, 1)) {
                sygnałySprzedaży.add(id);
            } else if (długaPrzed > krótkaPrzed && długaPo <= krótkaPo
                    && staćNa(giełda().spółka(id))) {
                sygnałyKupna.add(id);
            }
        }

        TypZlecenia typZlecenia = TypZlecenia.losowy();
        IdSpółki idSpółki;
        if (typZlecenia == TypZlecenia.KUPNO) {
            if (sygnałyKupna.isEmpty()) {
                return Optional.empty();
            }
            idSpółki = sygnałyKupna.get(Losowanie.losuj(0, sygnałyKupna.size() - 1));
        } else {
            if (sygnałySprzedaży.isEmpty()) {
                return Optional.empty();
            }
            idSpółki = sygnałySprzedaży.get(Losowanie.losuj(0, sygnałySprzedaży.size() - 1));
        }


        int ostatniaCena = giełda().spółka(idSpółki).ostatniaCena();
        int cena;
        if (typZlecenia == TypZlecenia.KUPNO) {
            if (portfel().ileGotówki() == 0) {
                return Optional.empty();
            }
            cena = Losowanie.losuj(Math.max(1, ostatniaCena - MAKS_RÓŻNICA_W_CENIE),
                    Math.min(ostatniaCena + MAKS_RÓŻNICA_W_CENIE, portfel().ileGotówki()));
        } else {
            cena = Losowanie.losuj(Math.max(1, ostatniaCena - MAKS_RÓŻNICA_W_CENIE),
                    ostatniaCena + MAKS_RÓŻNICA_W_CENIE);
        }

        int liczbaAkcji;
        if (typZlecenia == TypZlecenia.KUPNO) {
            liczbaAkcji = portfel().ileGotówki() / cena;
        } else {
            liczbaAkcji = portfel().ileAkcji(idSpółki);
        }

        Zlecenie zlecenie = switch (Losowanie.losuj(0, 4)) {
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

    private boolean staćNa(Spółka spółka) {
        return portfel().maGotówkę(spółka.ostatniaCena() - MAKS_RÓŻNICA_W_CENIE);
    }
}
