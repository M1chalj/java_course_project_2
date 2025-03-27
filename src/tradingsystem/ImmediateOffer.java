package tradingsystem;

public class ImmediateOffer extends ExpirationOffer {

    public ImmediateOffer(tradingSystem giełda, OfferType typ, CompanyId idAkcji, int liczbaAkcji, int cena, Investor investor, int aktualnaTura) {
        super(giełda, typ, idAkcji, liczbaAkcji, cena, investor, aktualnaTura);
    }
}
