package tradingsystem;

import java.util.PriorityQueue;

public interface OfferExecutor {

    void executeInvestor(Investor investor, CompanyId companyID, int numberOfShares, int price);

    void undoExecutionInvestor(Investor investor, CompanyId companyID, int numberOfShares, int price);

    void addToMyQueue(Offer offer, PriorityQueue<Offer> sellOffers,
                      PriorityQueue<Offer> buyOffers);

    void addToOtherQueue(Offer offer, PriorityQueue<Offer> sellOffers,
                         PriorityQueue<Offer> buyOffers);

    boolean otherQueueEmpty(PriorityQueue<Offer> sellOffers,
                            PriorityQueue<Offer> buyOffers);

    Offer getFromOtherQueue(PriorityQueue<Offer> sellOffers,
                            PriorityQueue<Offer> buyOffers);
}