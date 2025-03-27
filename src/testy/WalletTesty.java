package testy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tradingsystem.CompanyId;
import tradingsystem.Wallet;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTesty {

    private static final CompanyId spółkaA = new CompanyId("A");
    private static final CompanyId spółkaB = new CompanyId("B");
    private static final CompanyId spółkaC = new CompanyId("C");
    private static Wallet wallet;

    @BeforeAll
    public static void init() {
        wallet = new Wallet(0);
        wallet.addShares(spółkaA, 5);
        wallet.addShares(spółkaB, 10);
        wallet.addShares(spółkaC, 15);
    }

    @Test
    public void dodawanieIUsuwanieZasobów() {
        int gotówka = wallet.money();

        wallet.addMoney(10);
        assertEquals(wallet.money(), gotówka + 10);

        wallet.takeMoney(10);
        assertEquals(wallet.money(), gotówka);

        int liczbaAkcji = wallet.shares(spółkaA);

        wallet.addShares(spółkaA, 5);
        assertEquals(wallet.shares(spółkaA), liczbaAkcji + 5);

        wallet.takeShares(spółkaA, 5);
        assertEquals(wallet.shares(spółkaA), liczbaAkcji);
    }

    @Test
    public void UsuwanieNieistniejącychZasobów() {
        int gotówka = wallet.money();
        assertThrows(IllegalArgumentException.class, () -> wallet.takeMoney(gotówka + 10));

    }

}
