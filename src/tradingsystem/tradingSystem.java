package tradingsystem;

import main.RandomNumberGenerator;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Optional;

public class tradingSystem {
    private static final int MAKSYMALNA_RÓŻNICA_CENY = 10;

    private final Map<CompanyId, Company> spółki;
    private final Set<Investor> inwestorzy;
    private int numerTury;

    public tradingSystem() {
        numerTury = 0;
        spółki = new HashMap<>();
        inwestorzy = new HashSet<>();
    }

    public int roundNumber() {
        return numerTury;
    }

    public void addInvestor(Investor investor) {
        inwestorzy.add(investor);
    }

    public void addCompany(Company company) {
        spółki.put(company.id(), company);
    }

    public Set<CompanyId> CompaniesIDs() {
        return spółki.keySet();
    }

    public Company company(CompanyId id) {
        return spółki.get(id);
    }

    public void kolejnaTura() {
        List<Investor> kolejność = new LinkedList<>(inwestorzy);
        Collections.shuffle(kolejność, RandomNumberGenerator.generator());

        for (Investor investor : kolejność) {
            Optional<Offer> decyzja = investor.makeDecision();
            if (decyzja.isPresent()) {
                Offer offer = decyzja.get();
                if (poprawne(offer)) {
                    spółki.get(offer.companyID()).dodajZlecenie(offer);
                }
            }
        }

        for (Company company : spółki.values()) {
            company.wykonajZlecenia();
            company.wyrzućNieważneZlecenia(numerTury);
        }

        numerTury++;
    }

    public void symuluj(int liczbaTur) {
        while (liczbaTur > 0) {
            kolejnaTura();
            liczbaTur--;
        }
    }

    public List<Wallet> portfele() {
        List<Wallet> lista = new ArrayList<>();
        for (Investor investor : inwestorzy) {
            lista.add(investor.wallet());
        }
        return lista;
    }

    private boolean poprawne(Offer offer) {
        int ostatniaCena = spółki.get(offer.companyID()).lastPrice();
        return offer.executable() &&
                offer.priceInInterval(ostatniaCena - MAKSYMALNA_RÓŻNICA_CENY,
                        ostatniaCena + MAKSYMALNA_RÓŻNICA_CENY);
    }
}
