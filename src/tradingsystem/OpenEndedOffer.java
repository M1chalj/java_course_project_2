package tradingsystem;

public class OpenEndedOffer extends DivisibleOffer {
    public OpenEndedOffer(tradingSystem market, OfferType type, CompanyId companyId, int numberOfShares, int price, Investor investor) {
        super(market, type, companyId, numberOfShares, price, investor);
    }

    @Override
    public boolean expirationRound(int round) {
        return false;
    }
}
