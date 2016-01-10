package test.endtoend.auctionsniper;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(format(ITEM_ID_AS_LOGIN, itemId),
                AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        currentChat = chat;
                        chat.addMessageListener(messageListener);
                    }
                });
    }

    public String getItemId() {
        return itemId;
    }

    public void reportPrice(int price, int increment, String bidder) {
        throw new UnsupportedOperationException();
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage();
    }

    public void hasReceivedBid(int bid, String sniperId) {
        throw new UnsupportedOperationException();
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage(new Message());
    }

    public void stop() {
        connection.disconnect();
    }

    public class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages =
                new ArrayBlockingQueue<Message>(1);

        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage() throws InterruptedException {
            assertThat("Message", messages.poll(5, SECONDS), is(notNullValue()));
        }
    }
}
