package tradingsystem;

import java.util.PriorityQueue;

public abstract class IndivisibleOffer extends Offer {
    public IndivisibleOffer(tradingSystem market, OfferType type, CompanyId companyId, int sharesNumber, int price, Investor investor) {
        super(market, type, companyId, sharesNumber, price, investor);
    }

    @Override
    protected boolean executeNext(PriorityQueue<Offer> sellOffers, PriorityQueue<Offer> buyOffers) {
        if (sharesNumber() == 0) {
            return true;
        }
        return tryExecute(sellOffers, buyOffers);
    }
}
