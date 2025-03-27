package tradingsystem;

import java.util.PriorityQueue;

public abstract class DivisibleOffer extends Offer {

    public DivisibleOffer(tradingSystem market, OfferType type, CompanyId companyId, int sharesNumber, int price, Investor investor) {
        super(market, type, companyId, sharesNumber, price, investor);
    }

    @Override
    protected boolean executeNext(PriorityQueue<Offer> sellOffers, PriorityQueue<Offer> buyOffers) {
        if (sharesNumber() != 0) {
            offerExecutor().addToMyQueue(this, sellOffers, buyOffers);
        }
        return true;
    }
}
