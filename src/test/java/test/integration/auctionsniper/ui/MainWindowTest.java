package test.integration.auctionsniper.ui;

import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import auctionsniper.ui.MainWindow;
import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Test;
import test.endtoend.auctionsniper.AuctionSniperDriver;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<Item> itemProbe =
                new ValueMatcherProbe<Item>(equalTo(new Item("some item-id", 789)), "join request");
        mainWindow.addUserRequestListener(new UserRequestListener() {
                    public void joinAuction(String itemId, Item item) {
                        itemProbe.setReceivedValue(item);
                    }
                });
        driver.startBiddingFor("some item-id", 789);
        driver.check(itemProbe);
    }
}
