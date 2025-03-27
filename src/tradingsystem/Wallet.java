package tradingsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Wallet {
    private final Map<CompanyId, Integer> akcje;
    private int gotówka;

    public Wallet(int gotówka) {
        this.gotówka = gotówka;
        akcje = new HashMap<>();
    }

    public Wallet(Wallet wallet) { //konstruktor kopiujący
        this.akcje = new HashMap<>(wallet.akcje);
        this.gotówka = wallet.gotówka;
    }

    public void addMoney(int gotówka) {
        this.gotówka += gotówka;
    }

    public void takeMoney(int gotówka) {
        if (this.gotówka < gotówka) {
            throw new IllegalArgumentException("Usunięcie gotówki, której nie ma w portfelu");
        }
        this.gotówka -= gotówka;
    }

    public void addShares(CompanyId id, int liczba) {
        if (akcje.containsKey(id)) {
            liczba += akcje.get(id);
        }
        akcje.put(id, liczba);
    }

    public void takeShares(CompanyId id, int liczba) {
        if (!akcje.containsKey(id) || akcje.get(id) < liczba) {
            throw new IllegalArgumentException("Usunięcie akcji, których nie ma w portfelu");
        }
        liczba = akcje.get(id) - liczba;
        if (liczba == 0) {
            akcje.remove(id);
        } else {
            akcje.put(id, liczba);
        }
    }

    public Set<CompanyId> CompaniesIDs() {
        return akcje.keySet();
    }

    public int shares(CompanyId companyId) {
        return akcje.getOrDefault(companyId, 0);
    }

    public int money() {
        return gotówka;
    }

    public boolean hasMoney(int ile) {
        return gotówka >= ile;
    }

    public boolean hadShares(CompanyId id, int ile) {
        return akcje.containsKey(id) && akcje.get(id) >= ile;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(gotówka);
        for (Map.Entry<CompanyId, Integer> elem : akcje.entrySet()) {
            stringBuilder.append(" ");
            stringBuilder.append(elem.getKey());
            stringBuilder.append(":");
            stringBuilder.append(elem.getValue());
        }
        return stringBuilder.toString();
    }
}
