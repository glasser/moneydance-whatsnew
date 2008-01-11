package com.moneydance.modules.features.whatsnew;

import com.moneydance.apps.md.controller.FeatureModule;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.ModuleUtil;
import com.moneydance.apps.md.controller.UserPreferences;
import com.moneydance.apps.md.view.HomePageView;

import com.moneydance.apps.md.model.*;

import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.*;

/** Pluggable module used to give users access to a list of what's new.
 */

public class Main
    extends FeatureModule
{
    public void init() {
        // the first thing we will do is register this module to be invoked
        // via the application toolbar
        FeatureModuleContext context = getContext();
        HomePageView hpv = new WhatsNewView(this);
        try {
            context.registerHomePageView(this, hpv);
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void invoke(String p) {
        // Nothing to invoke!
    }
  
    public String getName() {
        return "What's New?";
    }

    public FeatureModuleContext getUnprotectedContext() {
        return getContext();
    }
}


