package tradingsystem;

import main.RandomNumberGenerator;

import java.util.*;

public class InvestorSMA extends Investor {

    private static final int LONGER_AVERAGE_LEN = 10;
    private static final int SHORTER_AVERAGE_LEN = 5;

    private final Map<CompanyId, SMA> longerAverage;
    private final Map<CompanyId, SMA> shorterAverage;

    public InvestorSMA(tradingSystem market, Wallet wallet) {
        super(market, wallet);
        longerAverage = new HashMap<>();
        shorterAverage = new HashMap<>();

        for (CompanyId id : market.CompaniesIDs()) {
            longerAverage.put(id, new SMA(LONGER_AVERAGE_LEN));
            shorterAverage.put(id, new SMA(SHORTER_AVERAGE_LEN));
        }
    }

    @Override
    public Optional<Offer> makeDecision() {

        List<CompanyId> buySignals = new ArrayList<>();
        List<CompanyId> sellSignals = new ArrayList<>();

        for (CompanyId id : market().CompaniesIDs()) {

            double longBefore = 0;
            double shortBefore = 0;
            if (market().roundNumber() >= LONGER_AVERAGE_LEN) {
                longBefore = longerAverage.get(id).average();
                shortBefore = shorterAverage.get(id).average();
            }

            longerAverage.get(id).nextValue(market().company(id).lastPrice());
            shorterAverage.get(id).nextValue(market().company(id).lastPrice());

            double longAfter = 0;
            double shortAfter = 0;
            if (market().roundNumber() >= LONGER_AVERAGE_LEN) {
                longAfter = longerAverage.get(id).average();
                shortAfter = shorterAverage.get(id).average();
            }

            if (longBefore < shortBefore && longAfter >= shortAfter
                    && wallet().hadShares(id, 1)) {
                sellSignals.add(id);
            } else if (longBefore > shortBefore && longAfter <= shortAfter
                    && canBuy(market().company(id))) {
                buySignals.add(id);
            }
        }

        OfferType offerType = OfferType.rand();
        CompanyId companyId;
        if (offerType == OfferType.BUY) {
            if (buySignals.isEmpty()) {
                return Optional.empty();
            }
            companyId = buySignals.get(RandomNumberGenerator.randInt(0, buySignals.size() - 1));
        } else {
            if (sellSignals.isEmpty()) {
                return Optional.empty();
            }
            companyId = sellSignals.get(RandomNumberGenerator.randInt(0, sellSignals.size() - 1));
        }


        int lastPrice = market().company(companyId).lastPrice();
        int price;
        if (offerType == OfferType.BUY) {
            if (wallet().money() == 0) {
                return Optional.empty();
            }
            price = RandomNumberGenerator.randInt(Math.max(1, lastPrice - MAX_PRICE_DIFFERENCE),
                    Math.min(lastPrice + MAX_PRICE_DIFFERENCE, wallet().money()));
        } else {
            price = RandomNumberGenerator.randInt(Math.max(1, lastPrice - MAX_PRICE_DIFFERENCE),
                    lastPrice + MAX_PRICE_DIFFERENCE);
        }

        int sharesNumber;
        if (offerType == OfferType.BUY) {
            sharesNumber = wallet().money() / price;
        } else {
            sharesNumber = wallet().shares(companyId);
        }

        Offer offer = switch (RandomNumberGenerator.randInt(0, 4)) {
            case 1 -> new OpenEndedOffer(market(), offerType, companyId, sharesNumber,
                    price, this);
            case 2 -> new ImmediateOffer(market(), offerType, companyId, sharesNumber,
                    price, this, market().roundNumber());
            case 3 -> new MakeOrCancelOffer(market(), offerType, companyId, sharesNumber,
                    price, this);
            case 4 -> {
                int term = market().roundNumber() + RandomNumberGenerator.randInt(1, MAX_VALID_PER_OFFER);
                yield new ExpirationOffer(market(), offerType, companyId, sharesNumber,
                        price, this, term);
            }
            default -> null;
        };
        return Optional.ofNullable(offer);
    }

    private boolean canBuy(Company company) {
        return wallet().hasMoney(company.lastPrice() - MAX_PRICE_DIFFERENCE);
    }
}
