package tradingsystem;

public class ExpirationOffer extends DivisibleOffer {
    private final int expirationRound;

    public ExpirationOffer(tradingSystem market, OfferType type, CompanyId companyId, int StocksNumber, int price, Investor investor, int expirationRound) {
        super(market, type, companyId, StocksNumber, price, investor);
        this.expirationRound = expirationRound;
    }

    @Override
    public boolean expirationRound(int round) {
        return round == expirationRound;
    }
}
