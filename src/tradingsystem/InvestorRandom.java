package tradingsystem;

import main.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvestorRandom extends Investor {

    public InvestorRandom(tradingSystem market, Wallet wallet) {
        super(market, wallet);
    }

    @Override
    public Optional<Offer> makeDecision() {

        OfferType offerType = OfferType.rand();
        List<CompanyId> possibleCompanies;

        if (offerType == OfferType.BUY) {
            possibleCompanies = new ArrayList<>(market().companiesIDs());
        } else {
            possibleCompanies = new ArrayList<>(wallet().CompaniesIDs());
        }
        if (possibleCompanies.isEmpty()) {
            return Optional.empty();
        }

        CompanyId companyId = possibleCompanies.get(RandomNumberGenerator.randInt(0, possibleCompanies.size() - 1));
        int lastPrice = market().company(companyId).lastPrice();
        int price = RandomNumberGenerator.randInt(Math.max(lastPrice - MAX_PRICE_DIFFERENCE, 1),
                lastPrice + MAX_PRICE_DIFFERENCE);

        int sharesNumber;
        if (offerType == OfferType.SELL) {
            sharesNumber = RandomNumberGenerator.randInt(1, wallet().shares(companyId));
        } else {
            if (wallet().money() < price) {
                return Optional.empty();
            }
            sharesNumber = RandomNumberGenerator.randInt(1, wallet().money() / price);
        }

        Offer offer = switch (RandomNumberGenerator.randInt(1, 4)) {
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
}
