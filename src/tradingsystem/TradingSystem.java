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

public class TradingSystem {
    private static final int MAX_PRICE_DIFFERENCE = 10;

    private final Map<CompanyId, Company> companies;
    private final Set<Investor> investors;
    private int roundNumber;

    public TradingSystem() {
        roundNumber = 0;
        companies = new HashMap<>();
        investors = new HashSet<>();
    }

    public int roundNumber() {
        return roundNumber;
    }

    public void addInvestor(Investor investor) {
        investors.add(investor);
    }

    public void addCompany(Company company) {
        companies.put(company.id(), company);
    }

    public Set<CompanyId> companiesIDs() {
        return companies.keySet();
    }

    public Company company(CompanyId id) {
        return companies.get(id);
    }

    public void nextRound() {
        List<Investor> order = new LinkedList<>(investors);
        Collections.shuffle(order, RandomNumberGenerator.generator());

        for (Investor investor : order) {
            Optional<Offer> decision = investor.makeDecision();
            if (decision.isPresent()) {
                Offer offer = decision.get();
                if (valid(offer)) {
                    companies.get(offer.companyID()).addOffer(offer);
                }
            }
        }

        for (Company company : companies.values()) {
            company.executeOffers();
            company.deleteExpiredOffers(roundNumber);
        }

        roundNumber++;
    }

    public void simulate(int numberOfRounds) {
        while (numberOfRounds > 0) {
            nextRound();
            numberOfRounds--;
        }
    }

    public List<Wallet> wallets() {
        List<Wallet> list = new ArrayList<>();
        for (Investor investor : investors) {
            list.add(investor.wallet());
        }
        return list;
    }

    private boolean valid(Offer offer) {
        int lastPrice = companies.get(offer.companyID()).lastPrice();
        return offer.executable() &&
                offer.priceInInterval(lastPrice - MAX_PRICE_DIFFERENCE,
                        lastPrice + MAX_PRICE_DIFFERENCE);
    }
}
