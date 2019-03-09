/*
* This file is part of ResselChain.
* Copyright Center for Secure Energy Informatics 2018
* Fabian Knirsch, Andreas Unterweger, Clemens Brunner
* This code is licensed under a modified 3-Clause BSD License. See LICENSE file for details.
*/

// TODO: DISCUSS

package at.entrust.resselchain.statetable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import at.entrust.resselchain.chain.Transaction;
import at.entrust.resselchain.logging.Logger;
import at.entrust.resselchain.state.ChainState;

public class AssetStateTable {

	// holds a MySQL database for this table

	private String assetName;
	private ArrayList<String> participantNames = new ArrayList<>();
	private HashMap<String, Integer> participantNameShares = new HashMap<>();

	private Connection connection = null;


	public AssetStateTable(String assetName, ArrayList<String> participantNames, HashMap<String, Integer> participantNameShares) {
		this.assetName = assetName;
		this.participantNames = participantNames;
		this.participantNameShares = participantNameShares;

		Logger.FULL.log(assetName);
		participantNames.forEach((name -> Logger.FULL.log(name)));
		participantNameShares.forEach((name, share) -> Logger.FULL.log(name + " " + share));
	}


	public boolean revertTransaction(Transaction tx) {
		final String sender = tx.getSender();
		final String receiver = tx.getReceiver();
		final int amount = tx.getAmount();

		// check if sender and receiver is known
		if (!this.areParcitipansValid(tx)) {
			return false;
		}

		// check if receiver has enough amount of eMundi
		if (participantNameShares.get(receiver) < amount) {
			Logger.FULL.log("Receiver has not enough eMundi");
			return false;
		}

		participantNameShares.put(sender, participantNameShares.get(sender) + amount);
		participantNameShares.put(receiver, participantNameShares.get(receiver) - amount);
		return true;
	}

	/**
	 * process a transaction
	 * @param tx
	 * @return if the transaction is valid it returns true, if not returns false
	 */
	public boolean processTransaction(Transaction tx) {
		final String sender = tx.getSender();
		final String receiver = tx.getReceiver();
		final int amount = tx.getAmount();

		// check if sender and receiver is known
		if (!this.areParcitipansValid(tx)) {
			return false;
		}

		// check if sender has enough amount of eMundi
		if (participantNameShares.get(sender) < Math.abs(amount)) {
			Logger.FULL.log("Sender has not enough eMundi");
			return false;
		}

		participantNameShares.put(sender, participantNameShares.get(sender) - amount);
		participantNameShares.put(receiver, participantNameShares.get(receiver) + amount);
		return true;
	}

	private boolean areParcitipansValid(Transaction tx) {
		final String sender = tx.getSender();
		final String receiver = tx.getReceiver();
		if (!participantNameShares.containsKey(sender) || !participantNameShares.containsKey(receiver)) {
			Logger.FULL.log("Sender or Receiver unknown");
			return false;
		}
		return true;
	}

	public static void restoreBackup() {
		//TODO: implement your own backup restore
	}

	public static HashMap<String, AssetStateTable> createStateFromStorage() {
		//TODO: implement your own backup restore
		// The hashmap consists of the asset name and an assetstatetable where the distribution is stored
		return new HashMap<String, AssetStateTable>();
	}

	public static void backupTable() {
		//TODO: implement a backup mechanism to backup the current state
	}

	/**
	 *
	 * @param date
	 * @return the asset distribution at a given timestamp
	 */
    public HashMap<String, Integer> getAssets(long date) {
		return this.participantNameShares;
	}


	public ArrayList<Transaction> processMultipleTransactions(ArrayList<Transaction> txs) throws AtomicTransactionException {

		ArrayList<Transaction> invalidTx = new ArrayList<>();
		int i = 0;
		while (i < txs.size() && processTransaction(txs.get(i)))
			i++;
		if (i >= txs.size())
			return invalidTx; // returns empty list if all Tx are valid
		else
		{
			Logger.FULL.log("Failed trying to process " + txs.size() + " Tx, failed at index " + i);
			invalidTx.add(txs.get(i)); // returns the first Tx that is invalid

			i--; // do not revert last transaction since it failed
			while (i >= 0 && revertTransaction(txs.get(i)))
				i--;
			if (i != -1) throw new AtomicTransactionException(); //This should never happen

			return invalidTx;
		}
	}

	public static HashMap<String,AssetStateTable> createAssetStateTablesFromMeta() {
		return new HashMap<>();
	}
}