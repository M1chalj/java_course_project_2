package tradingsystem;

import java.util.PriorityQueue;

public abstract class Offer implements Comparable<Offer> {

    private static int offersCnt = 0;

    private final int number;
    private final OfferType type;
    private final CompanyId companyID;
    private final int price;
    private final Investor investor;
    private final tradingSystem market;
    private int sharesNumber;
    private final OfferExecutor offerExecutor;

    public Offer(tradingSystem market, OfferType type, CompanyId companyID, int sharesNumber,
                 int price, Investor investor) {
        this.number = offersCnt++;
        this.type = type;
        this.companyID = companyID;
        this.price = price;
        this.investor = investor;
        this.market = market;
        this.sharesNumber = sharesNumber;
        if (type == OfferType.BUY) {
            offerExecutor = new OfferExecutorBuy();
        } else {
            offerExecutor = new OfferExecutorSell();
        }
    }

    protected int sharesNumber() {
        return sharesNumber;
    }

    public boolean matchWith(Offer z) {
        if (z.type == type || !(z.companyID.equals(companyID))) {
            return false;
        } else if (type == OfferType.SELL) {
            return price <= z.price;
        } else {
            return price >= z.price;
        }
    }

    public CompanyId companyID() {
        return companyID;
    }

    public OfferType type() {
        return type;
    }

    public boolean executable() {
        Wallet wallet = investor.wallet();
        if (type == OfferType.BUY) {
            return wallet.hasMoney(sharesNumber * price);
        } else {
            return wallet.hadShares(companyID, sharesNumber);
        }
    }

    public boolean priceInInterval(int minimum, int maksimum) {
        return minimum <= price && price <= maksimum;
    }

    public boolean tryExecute(PriorityQueue<Offer> sellOffers,
                              PriorityQueue<Offer> buyOffers) {
        if (!executable() || offerExecutor.otherQueueEmpty(sellOffers, buyOffers)) {
            return false;
        }
        Offer offer;
        offer = offerExecutor.getFromOtherQueue(sellOffers, buyOffers);

        if (!offer.executable() || !matchWith(offer)) {
            offerExecutor.addToOtherQueue(offer, sellOffers, buyOffers);
            return false;
        }

        int numberOfShares = Math.min(offer.sharesNumber(), sharesNumber());
        int price;
        if (compareTo(offer) < 0) {
            price = this.price;
        } else {
            price = offer.price;
        }

        int lastPrice = market.company(companyID).lastPrice();

        market.company(companyID).lastPrice(price);
        execute(numberOfShares, price);
        offer.execute(numberOfShares, price);

        if (!executeNext(sellOffers, buyOffers)
                || !offer.executeNext(sellOffers, buyOffers)) {
            undoExecution(numberOfShares, price);
            offer.undoExecution(numberOfShares, price);

            market.company(companyID).lastPrice(lastPrice);
            offerExecutor.addToOtherQueue(offer, sellOffers, buyOffers);
            return false;
        }
        return true;
    }

    private void undoExecution(int numberOfShares, int price) {
        offerExecutor.undoExecutionInvestor(investor, companyID, numberOfShares, price);
        sharesNumber += numberOfShares;
    }

    private void execute(int numberOfShares, int price) {
        offerExecutor.executeInvestor(investor, companyID, numberOfShares, price);
        sharesNumber -= numberOfShares;
    }

    protected OfferExecutor offerExecutor() {
        return offerExecutor;
    }

    protected abstract boolean executeNext(PriorityQueue<Offer> sellOffers, PriorityQueue<Offer> buyOffers);

    public abstract boolean expirationRound(int round);


    @Override
    public int compareTo(Offer z) {
        if (type == OfferType.BUY && z.type == OfferType.BUY) {
            if (price != z.price) {
                return Integer.compare(z.price, price);
            }
        } else if (type == OfferType.SELL && z.type == OfferType.SELL) {
            if (price != z.price) {
                return Integer.compare(price, z.price);
            }
        }
        return Integer.compare(number, z.number);
    }

    @Override
    public String toString() {
        return "Offer nr " + number + " submitted by " + investor +
                " type: " + type + " shares: " + companyID + " " + sharesNumber + " price limit: " + price;
    }
}
