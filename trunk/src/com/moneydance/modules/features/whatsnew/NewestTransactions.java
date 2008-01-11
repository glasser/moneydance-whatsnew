package com.moneydance.modules.features.whatsnew;

import com.moneydance.apps.md.model.*;

import java.util.*;

public class NewestTransactions {
    public static List<AbstractTxn> getNewestTransactions(RootAccount root,
                                                          int howMany) {
        if (root == null) {
            return new ArrayList<AbstractTxn>();
        }
        
        SortedSet<AbstractTxn> transactions
            = new TreeSet<AbstractTxn>(new CompareByDateEntered());
        
        Enumeration e = root.getTransactionSet().getAllTransactions();
        while (e.hasMoreElements()) {
            AbstractTxn t = (AbstractTxn) e.nextElement();
            transactions.add(t);
        }

        List<AbstractTxn> l = new ArrayList<AbstractTxn>(howMany);

        for (AbstractTxn t : transactions) {
            if (l.size() >= howMany) {
                break;
            }
            
            l.add(t);
        }
        return l;
    }
        
    private static class CompareByDateEntered
        implements Comparator<AbstractTxn> {
        public int compare(AbstractTxn o1, AbstractTxn o2) {
            Long d1 = o1.getDateEntered();
            Long d2 = o2.getDateEntered();
            // Reverse order!
            return d2.compareTo(d1);
        }
    }
}

