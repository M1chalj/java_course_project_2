package tradingsystem;

public class ExecuteOrCancelOffer extends IndivisibleOffer {

    public ExecuteOrCancelOffer(TradingSystem market, OfferType type, CompanyId companyID, int sharesNumber, int price, Investor investor) {
        super(market, type, companyID, sharesNumber, price, investor);
    }

    @Override
    public boolean expirationRound(int round) {
        return true;
    }
}
