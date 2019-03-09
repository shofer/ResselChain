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
        HashMap<String, Integer> participantNameShares = new HashMap<>();
        assetStageTable = new AssetStateTable(assetName, participantNames, participantNameShares);
    }

    @Test
    public void revertTransaction() {
        String sender = "sender";
        String receiver = "receiver";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        Boolean result = assetStageTable.revertTransaction(tx);
        assertEquals(result, true);
    }

    @Test
    public void processTransaction() {
        String sender = "sender";
        String receiver = "receiver";
        String assetName = "eMundi";
        long timestamp = 0L;
        int amount = 100;
        byte[] signature = null;
        String tag = "tag";
        boolean isExternal = true;

        Transaction tx = new Transaction(sender, receiver, timestamp, amount, signature, tag, assetName);
        Boolean result = assetStageTable.processTransaction(tx);
        assertEquals(result, true);
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
    public void getAssets() {
    }

    @Test
    public void processMultipleTransactions() {
    }

    @Test
    public void createAssetStateTablesFromMeta() {
    }
}