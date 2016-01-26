package test.endtoend.auctionsniper;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

import static org.junit.Assert.assertThat;

public class AuctionLogDriver {
    public static final String LOG_FILE_NAME = "auction-sniper.log";

    private final File logFile = new File(LOG_FILE_NAME);

    public void hasEntry(Matcher<String> matcher) throws IOException {
        assertThat(FileUtils.readFileToString(logFile), matcher);
    }

    public void clearLog() {
        logFile.delete();
        LogManager.getLogManager().reset();
    }
}
