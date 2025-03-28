package tradingsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Wallet {
    private final Map<CompanyId, Integer> shares;
    private int money;

    public Wallet(int money) {
        this.money = money;
        shares = new HashMap<>();
    }

    public Wallet(Wallet wallet) {
        this.shares = new HashMap<>(wallet.shares);
        this.money = wallet.money;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public void takeMoney(int money) {
        if (this.money < money) {
            throw new IllegalArgumentException("Deleting money that is not in the wallet");
        }
        this.money -= money;
    }

    public void addShares(CompanyId id, int amount) {
        if (shares.containsKey(id)) {
            amount += shares.get(id);
        }
        shares.put(id, amount);
    }

    public void takeShares(CompanyId id, int amount) {
        if (!shares.containsKey(id) || shares.get(id) < amount) {
            throw new IllegalArgumentException("Deleting shares that are not in the wallet");
        }
        amount = shares.get(id) - amount;
        if (amount == 0) {
            shares.remove(id);
        } else {
            shares.put(id, amount);
        }
    }

    public Set<CompanyId> CompaniesIDs() {
        return shares.keySet();
    }

    public int shares(CompanyId companyId) {
        return shares.getOrDefault(companyId, 0);
    }

    public int money() {
        return money;
    }

    public boolean hasMoney(int amount) {
        return money >= amount;
    }

    public boolean hadShares(CompanyId id, int amount) {
        return shares.containsKey(id) && shares.get(id) >= amount;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(money);
        for (Map.Entry<CompanyId, Integer> elem : shares.entrySet()) {
            stringBuilder.append(" ");
            stringBuilder.append(elem.getKey());
            stringBuilder.append(":");
            stringBuilder.append(elem.getValue());
        }
        return stringBuilder.toString();
    }
}
