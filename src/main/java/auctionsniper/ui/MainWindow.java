package auctionsniper.ui;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import auctionsniper.util.Defect;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static auctionsniper.SniperState.JOINING;

public class MainWindow extends JFrame {
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String JOIN_BUTTON_NAME = "join button";

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) {
        super("Auction Sniper");
        this.snipers = snipers;
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);
        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        controls.add(joinAuctionButton);
        return controls;
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    public static class SnipersTableModel extends AbstractTableModel implements SniperListener {
        private static String[] STATUS_TEXT = {"Joining", "Bidding", "Winning", "Lost", "Won"};
        private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, JOINING);

        private List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

        public void addSniper(SniperSnapshot snapshot) {
            int row = snapshots.size();
            snapshots.add(snapshot);
            fireTableRowsInserted(row, row);
        }

        @Override
        public void sniperStateChanged(SniperSnapshot newSniperSnapshot) {
            int row = rowMatching(newSniperSnapshot);
            snapshots.set(row, newSniperSnapshot);
            fireTableRowsUpdated(row, row);
        }

        public int getColumnCount() {
            return Column.values().length;
        }

        public int getRowCount() {
            return snapshots.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
        }

        @Override
        public String getColumnName(int column) {
            return Column.at(column).name;
        }

        public static String textFor(SniperState state) {
            return STATUS_TEXT[state.ordinal()];
        }

        private int rowMatching(SniperSnapshot newSnapshot) {
            for (int i = 0; i < snapshots.size(); i++) {
                if (newSnapshot.isForSameItemAs(snapshots.get(i))) {
                    return i;
                }
            }
            throw new Defect("Cannot find match for " + newSnapshot);
        }
    }
}
