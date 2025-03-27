package testy;

import org.junit.jupiter.api.Test;
import tradingsystem.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class tradingSystemTesty {

    @Test
    public void danePrzykładowe() {
        int ileRandom = 4;
        int ileSMA = 2;
        int liczbaTur = 100_000;
        List<Company> spółki = new ArrayList<>();
        spółki.add(new Company(new CompanyId("APL"), 145));
        spółki.add(new Company(new CompanyId("MSFT"), 300));
        spółki.add(new Company(new CompanyId("GOOGL"), 2700));

        Wallet walletPoczątkowy = new Wallet(100_000);
        walletPoczątkowy.addShares(new CompanyId("APL"), 5);
        walletPoczątkowy.addShares(new CompanyId("MSFT"), 15);
        walletPoczątkowy.addShares(new CompanyId("GOOGL"), 3);

        int sumaGotówki = 600_000;
        Map<CompanyId, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new CompanyId("APL"), 30);
        liczbaAkcjiSpółki.put(new CompanyId("MSFT"), 90);
        liczbaAkcjiSpółki.put(new CompanyId("GOOGL"), 18);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, walletPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }

    @Test
    public void inwestorzyRandom() {
        int ileRandom = 2;
        int ileSMA = 0;
        int liczbaTur = 1000;
        List<Company> spółki = new ArrayList<>();
        spółki.add(new Company(new CompanyId("A"), 100));
        spółki.add(new Company(new CompanyId("B"), 500));

        Wallet walletPoczątkowy = new Wallet(10_000);
        walletPoczątkowy.addShares(new CompanyId("A"), 1);
        walletPoczątkowy.addShares(new CompanyId("B"), 400);

        int sumaGotówki = 20_000;
        Map<CompanyId, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new CompanyId("A"), 2);
        liczbaAkcjiSpółki.put(new CompanyId("B"), 800);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, walletPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }

    @Test
    public void inwestorzySMA() {
        int ileRandom = 0;
        int ileSMA = 2;
        int liczbaTur = 1000;
        List<Company> spółki = new ArrayList<>();
        spółki.add(new Company(new CompanyId("A"), 100));
        spółki.add(new Company(new CompanyId("B"), 500));

        Wallet walletPoczątkowy = new Wallet(10_000);
        walletPoczątkowy.addShares(new CompanyId("A"), 1);
        walletPoczątkowy.addShares(new CompanyId("B"), 400);

        int sumaGotówki = 20_000;
        Map<CompanyId, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new CompanyId("A"), 2);
        liczbaAkcjiSpółki.put(new CompanyId("B"), 800);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, walletPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }

    @Test
    public void większeDane() {
        int ileRandom = 10;
        int ileSMA = 20;
        int liczbaTur = 20000;
        List<Company> spółki = new ArrayList<>();
        spółki.add(new Company(new CompanyId("A"), 100));
        spółki.add(new Company(new CompanyId("B"), 500));
        spółki.add(new Company(new CompanyId("C"), 1700));
        spółki.add(new Company(new CompanyId("D"), 5000));

        Wallet walletPoczątkowy = new Wallet(10_000);
        walletPoczątkowy.addShares(new CompanyId("A"), 1);
        walletPoczątkowy.addShares(new CompanyId("B"), 400);
        walletPoczątkowy.addShares(new CompanyId("C"), 5);
        walletPoczątkowy.addShares(new CompanyId("D"), 30);

        int sumaGotówki = 300_000;
        Map<CompanyId, Integer> liczbaAkcjiSpółki = new HashMap<>();
        liczbaAkcjiSpółki.put(new CompanyId("A"), 30);
        liczbaAkcjiSpółki.put(new CompanyId("B"), 12000);
        liczbaAkcjiSpółki.put(new CompanyId("C"), 150);
        liczbaAkcjiSpółki.put(new CompanyId("D"), 900);

        testuj(ileRandom, ileSMA, liczbaTur, spółki, walletPoczątkowy, liczbaAkcjiSpółki, sumaGotówki);
    }


    private void testuj(int ileRandom, int ileSMA, int liczbaTur, List<Company> spółki, Wallet walletPoczątkowy,
                        Map<CompanyId, Integer> liczbaAkcjiSpółki, int sumaGotówki) {
        tradingSystem tradingSystem = new tradingSystem();
        for (Company company : spółki) {
            tradingSystem.addCompany(company);
        }

        List<Investor> inwestorzy = new ArrayList<>();

        for (int i = 0; i < ileSMA; i++) {
            inwestorzy.add(new InvestorSMA(tradingSystem, new Wallet(walletPoczątkowy)));
        }
        for (int i = 0; i < ileRandom; i++) {
            inwestorzy.add(new InvestorRandom(tradingSystem, new Wallet(walletPoczątkowy)));
        }

        for (Investor investor : inwestorzy) {
            tradingSystem.addInvestor(investor);
        }

        tradingSystem.symuluj(liczbaTur);
        List<Wallet> portfele = tradingSystem.portfele();

        for (Company company : spółki) {
            int sumaAkcji = 0;
            for (Wallet wallet : portfele) {
                sumaAkcji += wallet.shares(company.id());
            }

            assertEquals(liczbaAkcjiSpółki.get(company.id()), sumaAkcji);
        }

        int wynikowaSumaGotówki = 0;
        for (Wallet wallet : portfele) {
            wynikowaSumaGotówki += wallet.money();
        }

        assertEquals(wynikowaSumaGotówki, sumaGotówki);
    }
}
