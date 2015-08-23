package com;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


// Uses example from:
// http://www2.cs.uregina.ca/~dbd/cs831/notes/itemsets/itemset_apriori.html

public class Database {
	
	private List<Set<String>> transactions = new LinkedList<>();
	
	public int getTotalNumberOfTransactions() {
		return transactions.size();
	}
	
	@SuppressWarnings("serial")
	public Database() {
				
		Set<String> t1 = new HashSet<String>()
				{{ add("A"); add("B"); add("C"); }};
		transactions.add(t1);
		
		Set<String> t2 = new HashSet<String>()
				{{ add("A"); add("B"); add("C"); add("D"); add("E"); }};
		transactions.add(t2);
		
		Set<String> t3 = new HashSet<String>()
				{{ add("A"); add("C"); add("D"); }};
		transactions.add(t3);

		Set<String> t4 = new HashSet<String>()
				{{ add("A"); add("C"); add("D"); add("E"); }};
		transactions.add(t4);
		
		Set<String> t5 = new HashSet<String>()
				{{ add("A"); add("B"); add("C"); add("D"); }};
		transactions.add(t5);

	}
	
	public int getNumberOfRecordsContainingAttributes(Set<String> attributes) {
		
		int n = 0;
		
		// For each transaction.
		for (Set<String> transaction : transactions) {
			
			// See if this transaction contains all attributes.
			boolean containsAllAttributes = true;
			for (String attribute : attributes) {
				if (transaction.contains(attribute) == false) {
					containsAllAttributes = false;
					break;
				}
			}
			
			// If the transaction contained all attributes, inc record count.
			if (containsAllAttributes) n++;
		}
		
		return n;
	}

}
