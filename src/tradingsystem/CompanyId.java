package tradingsystem;

import java.security.InvalidParameterException;

public class CompanyId {
    private final String id;

    public CompanyId(String s) {
        if (s.length() > 5 || s.isEmpty()) {
            throw new InvalidParameterException("Invalid company ID");
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 'A' || s.charAt(i) > 'Z') {
                throw new InvalidParameterException("Invalid company ID");
            }
        }
        id = s;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CompanyId)) {
            return false;
        }
        CompanyId companyId = (CompanyId) obj;
        return id.equals(companyId.id);
    }

    @Override   
    public int hashCode() {
        return id.hashCode();
    }
}
