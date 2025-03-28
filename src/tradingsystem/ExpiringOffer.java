package tradingsystem;

public class ExpiringOffer extends DivisibleOffer {
    private final int expirationRound;

    public ExpiringOffer(TradingSystem market, OfferType type, CompanyId companyId, int StocksNumber, int price, Investor investor, int expirationRound) {
        super(market, type, companyId, StocksNumber, price, investor);
        this.expirationRound = expirationRound;
    }

    @Override
    public boolean expirationRound(int round) {
        return round == expirationRound;
    }
}
