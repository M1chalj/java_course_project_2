package tradingsystem;

public class ExpirationOffer extends DivisibleOffer {
    private final int turaWażności;

    public ExpirationOffer(tradingSystem giełda, OfferType typ, CompanyId idAkcji, int liczbaAkcji, int cena, Investor investor, int turaWażności) {
        super(giełda, typ, idAkcji, liczbaAkcji, cena, investor);
        this.turaWażności = turaWażności;
    }

    @Override
    public boolean expirationRound(int round) {
        return round == turaWażności;
    }
}
