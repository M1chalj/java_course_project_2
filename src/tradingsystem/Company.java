package tradingsystem;

import java.util.PriorityQueue;

public class Company {

    private final CompanyId id;
    private final PriorityQueue<Offer> buyOffers;
    private final PriorityQueue<Offer> sellOffers;
    private int lastPrice;

    public Company(CompanyId id, int lastPrice) {
        this.id = id;
        this.lastPrice = lastPrice;
        sellOffers = new PriorityQueue<>();
        buyOffers = new PriorityQueue<>();
    }

    public void addOffer(Offer offer) {
        if (offer.type() == OfferType.BUY) {
            buyOffers.add(offer);
        } else {
            sellOffers.add(offer);
        }
    }

    public void executeOffers() {
        while (!buyOffers.isEmpty() && !sellOffers.isEmpty()
                && buyOffers.peek().matchWith(sellOffers.peek())) {
            if (sellOffers.peek().compareTo(buyOffers.peek()) < 0) {
                sellOffers.remove().tryExecute(sellOffers, buyOffers);
            } else {
                buyOffers.remove().tryExecute(sellOffers, buyOffers);
            }
        }
    }

    public void deleteExpiredOffers(int round) {
        buyOffers.removeIf(offer -> offer.expirationRound(round));
    }

    public int lastPrice() {
        return lastPrice;
    }

    public void lastPrice(int price) {
        lastPrice = price;
    }

    public CompanyId id() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
