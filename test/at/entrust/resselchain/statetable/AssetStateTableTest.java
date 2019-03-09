package at.entrust.resselchain.statetable;

import at.entrust.resselchain.chain.Transaction;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class AssetStateTableTest {

    private AssetStateTable assetStageTable;

    @Before
    public void before() {
        String assetName = "eMundi";
        ArrayList<String> participantNames = new ArrayList<>();
        participantNames.add("node1");
        participantNames.add("node2");
        participantNames.add("node3");
        HashMap<String, Integer> participantNameShares = new HashMap<>();
        participantNameShares.put("node1", 1000);
        participantNameShares.put("node2", 5000);
        participantNameShares.put("node3", 10);
        assetStageTable = new AssetStateTable(assetName, participantNames, participantNameShares);
    }

    @Test
    public void testRevertTransactionValidTransaction() {
        String sender = "node1";
        String receiver = "node2";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.revertTransaction(tx);

        HashMap<String, Integer> assets = assetStageTable.getAssets(0);
        assertEquals(isProcessed, true);
        assertEquals(1100, (long) assets.get("node1"));
        assertEquals(4900, (long) assets.get("node2"));
    }

    @Test
    public void testRevertTransactionInvalidSender() {
        String sender = "node";
        String receiver = "node2";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.revertTransaction(tx);
        assertEquals(false, isProcessed);
    }

    @Test
    public void testRevertTransactionInvalidReceiver() {
        String sender = "node1";
        String receiver = "node";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.revertTransaction(tx);
        assertEquals(false, isProcessed);
    }

    @Test
    public void testRevertTransactionInvalidAmount() {
        String sender = "node1";
        String receiver = "node2";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 5001;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.revertTransaction(tx);
        assertEquals(false, isProcessed);
    }

    @Test
    public void testProcessTransactionValidTransaction() {
        String sender = "node1";
        String receiver = "node2";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.processTransaction(tx);
        HashMap<String, Integer> assets = assetStageTable.getAssets(0);
        assertEquals(true, isProcessed);
        assertEquals(900, (long) assets.get("node1"));
        assertEquals(5100, (long) assets.get("node2"));
    }

    @Test
    public void testProcessTransactionInvalidSender() {
        String sender = "node";
        String receiver = "node2";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.processTransaction(tx);
        assertEquals(false, isProcessed);
    }

    @Test
    public void testProcessTransactionInvalidReceiver() {
        String sender = "node1";
        String receiver = "node";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.processTransaction(tx);
        assertEquals(false, isProcessed);
    }

    @Test
    public void testProcessTransactionInvalidAmount() {
        String sender = "node1";
        String receiver = "node2";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 1001;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        boolean isProcessed = assetStageTable.processTransaction(tx);
        assertEquals(false, isProcessed);
    }

    @Test
    public void restoreBackup() {
    }

    @Test
    public void createStateFromStorage() {
    }

    @Test
    public void backupTable() {
    }

    @Test
    public void testAssetsShouldReturnValues() {
        HashMap<String, Integer> assets = assetStageTable.getAssets(0);
        assertEquals(1000, (long) assets.get("node1"));
        assertEquals(5000, (long) assets.get("node2"));
        assertEquals(10L, (long) assets.get("node3"));
    }

    @Test
    public void processMultipleTransactions() {
    }

    @Test
    public void createAssetStateTablesFromMeta() {
    }
}