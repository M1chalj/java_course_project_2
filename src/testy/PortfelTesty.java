package testy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import systemtransakcyjny.IdSpółki;
import systemtransakcyjny.Portfel;
import static org.junit.jupiter.api.Assertions.*;

public class PortfelTesty {

    private static final IdSpółki spółkaA = new IdSpółki("A");
    private static final IdSpółki spółkaB = new IdSpółki("B");
    private static final IdSpółki spółkaC = new IdSpółki("C");
    private static Portfel portfel;

    @BeforeAll
    public static void init() {
        portfel = new Portfel(0);
        portfel.dodajAkcje(spółkaA, 5);
        portfel.dodajAkcje(spółkaB, 10);
        portfel.dodajAkcje(spółkaC, 15);
    }

    @Test
    public void dodawanieIUsuwanieZasobów() {
        int gotówka = portfel.ileGotówki();

        portfel.dodajGotówkę(10);
        assertEquals(portfel.ileGotówki(), gotówka + 10);

        portfel.odejmijGotówkę(10);
        assertEquals(portfel.ileGotówki(), gotówka);

        int liczbaAkcji = portfel.ileAkcji(spółkaA);

        portfel.dodajAkcje(spółkaA, 5);
        assertEquals(portfel.ileAkcji(spółkaA), liczbaAkcji + 5);

        portfel.odejmijAkcje(spółkaA, 5);
        assertEquals(portfel.ileAkcji(spółkaA), liczbaAkcji);
    }

    @Test
    public void UsuwanieNieistniejącychZasobów() {
        int gotówka = portfel.ileGotówki();
        assertThrows(IllegalArgumentException.class, () -> portfel.odejmijGotówkę(gotówka + 10));

    }

}
