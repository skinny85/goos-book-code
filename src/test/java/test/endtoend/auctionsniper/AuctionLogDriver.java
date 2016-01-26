package test.endtoend.auctionsniper;

import auctionsniper.xmpp.XMPPAuctionHouse;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

import static org.junit.Assert.assertThat;

public class AuctionLogDriver {
    private final File logFile = new File(XMPPAuctionHouse.LOG_FILE_NAME);

    public void hasEntry(Matcher<String> matcher) throws IOException {
        assertThat(FileUtils.readFileToString(logFile), matcher);
    }

    public void clearLog() {
        logFile.delete();
        LogManager.getLogManager().reset();
    }
}
