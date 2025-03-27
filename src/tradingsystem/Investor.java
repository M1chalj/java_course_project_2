package tradingsystem;

import java.util.Optional;

public abstract class Investor {
    protected static final int MAX_PRICE_DIFFERENCE = 10;
    protected static final int MAX_VALID_PER_OFFER = 100;

    private static int InvestorCnt = 0;

    private final int number;
    private final tradingSystem market;
    private final Wallet wallet;

    public Investor(tradingSystem market, Wallet wallet) {
        number = InvestorCnt++;
        this.market = market;
        this.wallet = wallet;
    }

    protected tradingSystem market() {
        return market;
    }

    public Wallet wallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return "Investor " + number;
    }

    public abstract Optional<Offer> makeDecision();
}
