package com.moneydance.modules.features.whatsnew;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.model.AbstractTxn;
import java.awt.Color;

// This makes the assumptions about the FeatureModuleContext.

public class UnsafeAccessor {
    private final com.moneydance.apps.md.view.gui.MoneydanceGUI mdg;

    public UnsafeAccessor(FeatureModuleContext fmc) {
        com.moneydance.apps.md.controller.Main main
            = (com.moneydance.apps.md.controller.Main) fmc;
        this.mdg = (com.moneydance.apps.md.view.gui.MoneydanceGUI)
            main.getUI();
    }

    public void showTxn(AbstractTxn txn) {
        mdg.showTxn(txn);
    }

    public Color stripeColor() {
        return mdg.getColors().homePageAltBG;
    }

    public char getDecimalChar() {
        return mdg.getMain().getPreferences().getDecimalChar();
    }
}
