package tradingsystem;

import java.util.PriorityQueue;

public abstract class Offer implements Comparable<Offer> {

    private static int offersCnt = 0;

    private final int number;
    private final OfferType type;
    private final CompanyId companyId;
    private final int price;
    private final Investor investor;
    private final TradingSystem market;
    private int sharesNumber;
    private final OfferExecutor offerExecutor;

    public Offer(TradingSystem market, OfferType type, CompanyId companyId, int sharesNumber,
                 int price, Investor investor) {
        this.number = offersCnt++;
        this.type = type;
        this.companyId = companyId;
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
        if (z.type == type || !(z.companyId.equals(companyId))) {
            return false;
        } else if (type == OfferType.SELL) {
            return price <= z.price;
        } else {
            return price >= z.price;
        }
    }

    public CompanyId companyID() {
        return companyId;
    }

    public OfferType type() {
        return type;
    }

    public boolean executable() {
        Wallet wallet = investor.wallet();
        if (type == OfferType.BUY) {
            return wallet.hasMoney(sharesNumber * price);
        } else {
            return wallet.hadShares(companyId, sharesNumber);
        }
    }

    public boolean priceInInterval(int min, int max) {
        return min <= price && price <= max;
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

        int lastPrice = market.company(companyId).lastPrice();

        market.company(companyId).lastPrice(price);
        execute(numberOfShares, price);
        offer.execute(numberOfShares, price);

        if (!executeNext(sellOffers, buyOffers)
                || !offer.executeNext(sellOffers, buyOffers)) {
            undoExecution(numberOfShares, price);
            offer.undoExecution(numberOfShares, price);

            market.company(companyId).lastPrice(lastPrice);
            offerExecutor.addToOtherQueue(offer, sellOffers, buyOffers);
            return false;
        }
        return true;
    }

    private void undoExecution(int numberOfShares, int price) {
        offerExecutor.undoExecutionInvestor(investor, companyId, numberOfShares, price);
        sharesNumber += numberOfShares;
    }

    private void execute(int numberOfShares, int price) {
        offerExecutor.executeInvestor(investor, companyId, numberOfShares, price);
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
                " type: " + type + " shares: " + companyId + " " + sharesNumber + " price limit: " + price;
    }
}
