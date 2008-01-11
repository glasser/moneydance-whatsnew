package com.moneydance.modules.features.whatsnew;

import com.moneydance.apps.md.model.*;
import com.moneydance.apps.md.view.HomePageView;
import com.moneydance.apps.md.view.gui.MoneydanceLAF;
import com.moneydance.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.*;

public class WhatsNewView implements HomePageView
{
    private class ViewPanel extends JPanel
        implements TransactionListener, JLinkListener {

        private final RootAccount rootAccount;
        private final JPanel listPanel;
        private final JUnderlineLabel headerLabel;
        private final UnsafeAccessor ua
            = new UnsafeAccessor(main.getUnprotectedContext());

        ViewPanel(RootAccount rootAccount) {
            this.rootAccount = rootAccount;

            GridBagLayout gridbag = new GridBagLayout();
            this.setLayout(gridbag);
            this.setBorder(MoneydanceLAF.homePageBorder);
            this.setOpaque(false);

            Font font = getFont();
            font = new Font(font.getName(), 1, font.getSize() + 2);
            
            JPanel headerPanel = new JPanel(new GridBagLayout());
            headerPanel.setOpaque(false);
            headerLabel = new JUnderlineLabel(" ", SwingConstants.LEFT);
            headerLabel.setFont(font);
            //             headerLabel.addLinkListener(this);

            this.listPanel = new JPanel(gridbag);
            listPanel.setOpaque(false);
            
            this.add(headerPanel,
                     AwtUtil.getConstraints(0, 0, 0.0F, 0.0F,
                                            1, 1, true, true));
            headerPanel.add(headerLabel,
                            AwtUtil.getConstraints(0, 0, 0.0F, 0.0F,
                                                   1, 1, true, true,
                                                   AwtUtil.DEFAULT_X_INSET,
                                                   AwtUtil.DEFAULT_X_INSET,
                                                   AwtUtil.DEFAULT_X_INSET,
                                                   AwtUtil.DEFAULT_INSET));
            headerPanel.add(Box.createHorizontalStrut(2),
                            AwtUtil.getConstraints(1, 0, 1.0F, 0.0F,
                                                   1, 1, true, true,
                                                   AwtUtil.DEFAULT_X_INSET,
                                                   AwtUtil.DEFAULT_INSET,
                                                   AwtUtil.DEFAULT_X_INSET,
                                                   AwtUtil.DEFAULT_INSET));
            this.add(listPanel,
                     AwtUtil.getConstraints(0, 1, 1.0F, 0.0F,
                                            1, 1, true, true));
            this.add(Box.createVerticalStrut(2),
                     AwtUtil.getConstraints(0, 2, 0.0F, 1.0F,
                                            1, 1, false, false));
            if (thisView.active) {
                this.activate();
            }
        }

        void activate() {
            this.rootAccount.getTransactionSet().addTransactionListener(this);
            refresh();
        }

        void deactivate() {
            this.rootAccount.getTransactionSet().removeTransactionListener(this);
        }

        void refresh()
        {
            this.listPanel.removeAll();
            this.headerLabel.setText(thisView.toString());

            List<AbstractTxn> txns
                = NewestTransactions.getNewestTransactions(this.rootAccount, 12);

            char dec = ua.getDecimalChar();

            int currentRow = 1;
            for (AbstractTxn txn : txns) {
                JLinkLabel nameLabel = new JLinkLabel(txn.getDescription(),
                                                      txn,
                                                      SwingConstants.LEFT);
                JLinkLabel accountLabel = new JLinkLabel(txn.getAccount().getAccountName(),
                                                         txn,
                                                         SwingConstants.LEFT);
                JLinkLabel rateLabel = new JLinkLabel(txn.getAccount()
                                                      .getCurrencyType()
                                                      .formatFancy(txn.getParentTxn().getValue(),
                                                                   dec),
                                                      txn,
                                                      SwingConstants.RIGHT);
                
                listPanel.add(nameLabel,
                              GridC.getc(0, currentRow)
                              .fillx()
                              .padx(5)
                              .pady(2));
                listPanel.add(accountLabel,
                              GridC.getc(1, currentRow)
                              .wx(1.0f)
                              .fillx()
                              .pady(2));
                listPanel.add(rateLabel,
                              GridC.getc(2, currentRow)
                              .fillx()
                              .pady(2));

                for (JLinkLabel l : new JLinkLabel[] {nameLabel, accountLabel,
                                                      rateLabel }) {
                    l.setDrawUnderline(false);
                    l.addLinkListener(this);
                    stripe(l, currentRow);
                }

                currentRow++;
            }

            if (txns.size() > 0) {
                setVisible(true);
                validate();
            } else {
                setVisible(false);
            }
        }

        private void stripe(JLabel l, int currentRow) {
            if (currentRow % 2 == 0) {
                return;
            }
            l.setOpaque(true);
            l.setBackground(stripeColor());
        }
        
        private Color stripeColor() {
            return ua.stripeColor();
        }
        
        public void linkActivated(Object link, InputEvent evt) {
            assert link instanceof AbstractTxn;
            AbstractTxn txn = (AbstractTxn) link;
            ua.showTxn(txn);
        }

        public void transactionAdded(AbstractTxn t) {
            refresh();
        }
        
        public void transactionModified(AbstractTxn t) {
            refresh();
        }
        
        public void transactionRemoved(AbstractTxn t) {
            refresh();
        }

    }

    private final Main main;
    private boolean active = false;
    private ViewPanel view;
    private final WhatsNewView thisView;

    public WhatsNewView(Main main) {
        this.main = main;
        this.view = null;
        this.thisView = this;
    }

    public String getID() {
        return "net.davidglasser.whatsnew";
    }

    public String toString() {
        return "What's New?";
    }

    public JComponent getGUIView (RootAccount rootAccount) {
        if (this.view == null) {
            this.view = new ViewPanel(rootAccount);
        }
        return this.view;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (this.view != null) {
            if (active) {
                view.activate();
            } else {
                view.deactivate();
            }
        }
    }

    public void refresh() {
        if (this.view != null) {
            this.view.refresh();
        }
    }

    public synchronized void reset() {
        setActive(false);
        this.view = null;
    }



}
