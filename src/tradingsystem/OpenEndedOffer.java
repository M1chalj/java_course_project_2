package tradingsystem;

public class OpenEndedOffer extends DivisibleOffer {
    public OpenEndedOffer(tradingSystem giełda, OfferType typ, CompanyId idAkcji, int liczbaAkcji, int cena, Investor investor) {
        super(giełda, typ, idAkcji, liczbaAkcji, cena, investor);
    }

    @Override
    public boolean expirationRound(int round) {
        return false;
    }
}
