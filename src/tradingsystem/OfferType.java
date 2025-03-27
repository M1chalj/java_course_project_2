package tradingsystem;

import main.RandomNumberGenerator;

public enum OfferType {
    BUY,
    SELL;

    public static OfferType rand() {
        return values()[RandomNumberGenerator.randInt(0, values().length - 1)];
    }
}