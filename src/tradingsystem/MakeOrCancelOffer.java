package tradingsystem;

public class MakeOrCancelOffer extends IndivisibleOffer {

    public MakeOrCancelOffer(tradingSystem market, OfferType type, CompanyId companyID, int sharesNumber, int price, Investor investor) {
        super(market, type, companyID, sharesNumber, price, investor);
    }

    @Override
    public boolean expirationRound(int round) {
        return true;
    }
}
