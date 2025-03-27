package tradingsystem;

import java.util.PriorityQueue;

public class OfferExecutorBuy implements OfferExecutor {

    @Override
    public void executeInvestor(Investor investor, CompanyId companyID, int numberOfShares, int price) {
        investor.wallet().takeMoney(numberOfShares * price);
        investor.wallet().addShares(companyID, numberOfShares);
    }

    @Override
    public void undoExecutionInvestor(Investor investor, CompanyId companyID, int numberOfShares, int price) {
        investor.wallet().addMoney(numberOfShares * price);
        investor.wallet().takeShares(companyID, numberOfShares);
    }

    @Override
    public void addToMyQueue(Offer offer, PriorityQueue<Offer> sellOffers,
                             PriorityQueue<Offer> buyOffers) {
        buyOffers.add(offer);
    }

    @Override
    public void addToOtherQueue(Offer offer, PriorityQueue<Offer> sellOffers,
                                PriorityQueue<Offer> buyOffers) {
        sellOffers.add(offer);
    }

    @Override
    public boolean otherQueueEmpty(PriorityQueue<Offer> sellOffers,
                                   PriorityQueue<Offer> buyOffers) {
        return sellOffers.isEmpty();
    }

    @Override
    public Offer getFromOtherQueue(PriorityQueue<Offer> sellOffers,
                                   PriorityQueue<Offer> buyOffers) {
        return sellOffers.remove();
    }
}