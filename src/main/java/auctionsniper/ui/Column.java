package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

public enum Column {
    ITEM_IDENTIFIER,
    LAST_PRICE,
    LAST_BID,
    SNIPER_STATE;

    public static Column at(int offset) {
        return values()[offset];
    }

    public Object valueIn(SniperSnapshot snapshot) {
        throw new UnsupportedOperationException();
    }
}
