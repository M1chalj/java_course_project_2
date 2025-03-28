package tradingsystem;

public class ImmediateOffer extends ExpiringOffer {

    public ImmediateOffer(TradingSystem market, OfferType type, CompanyId companyId, int stocksNumber, int price, Investor investor, int round) {
        super(market, type, companyId, stocksNumber, price, investor, round);
    }
}
