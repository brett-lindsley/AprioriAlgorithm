package com;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Apriori {

	// Uses example from:
	// http://www2.cs.uregina.ca/~dbd/cs831/notes/itemsets/itemset_apriori.html

	DecimalFormat df = new DecimalFormat("0.00");

	private int supportPercentage = 40;
	private int confidencePercentage = 60;

	private Database database = new Database();

	private List<ItemSet> itemSets = new LinkedList<>();
	private List<ItemSet> candidates = new LinkedList<>();

	private void application() {
		System.out.println("Application starting");

		initializeItemSet();

		int pass = 1;

		while (true) {

			System.out.println("Item list size: " + itemSets.size());

			printItemset("Item set:", itemSets);

			createCandidates(pass);

			calculateCandidateSupport();

			System.out.println("candidate size before prune: " + candidates.size());
			printItemset("Candidate set:", candidates);

			pruneCandidates();

			System.out.println("candidate size after  prune: " + candidates.size());
			printItemset("Candidate set:", candidates);

			if (candidates.size() == 0)
				break;

			copyCandidatesToItemList();

			pass++;
		}

		System.out.println("*** Done ***");
		printItemset("Item set:", itemSets);

		System.out.println();
		System.out.println("*** Association rules");
		List<AssociationRule> associationRules = generateAssociationRules(itemSets);
		printFinalAssociationRules(associationRules);
	}

	private void copyCandidatesToItemList() {
		itemSets.clear();
		itemSets.addAll(candidates);
	}

	@SuppressWarnings("serial")
	private void initializeItemSet() {
		itemSets.clear();

		itemSets.add(new ItemSet(new HashSet<String>() {
			{
				add("A");
			}
		}));
		itemSets.add(new ItemSet(new HashSet<String>() {
			{
				add("B");
			}
		}));
		itemSets.add(new ItemSet(new HashSet<String>() {
			{
				add("C");
			}
		}));
		itemSets.add(new ItemSet(new HashSet<String>() {
			{
				add("D");
			}
		}));
		itemSets.add(new ItemSet(new HashSet<String>() {
			{
				add("E");
			}
		}));
	}

	private void calculateCandidateSupport() {
		for (ItemSet item : candidates) {
			int s = database.getNumberOfRecordsContainingAttributes(item.getItems());
			item.setSupportCount(s);
		}
	}

	private void printItemset(String title, List<ItemSet> sets) {
		System.out.println(title);
		for (ItemSet item : sets) {
			System.out.print("   {");
			for (String s : item.getItems())
				System.out.print(s + " ");
			System.out.print("}");
			System.out.print(" --- ");
			System.out.println(item.getSupportCount());
		}
	}

	private void pruneCandidates() {
		for (Iterator<ItemSet> iterator = candidates.iterator(); iterator.hasNext();) {
			ItemSet item = iterator.next();
			if (item.getSupportCount() * 100 < supportPercentage * database.getTotalNumberOfTransactions()) {
				iterator.remove();
			}
		}
	}

	private void createCandidates(int pass) {

		candidates.clear();

		// Create all unique combinations.
		for (int i = 0; i < itemSets.size(); i++) {
			for (int j = i + 1; j < itemSets.size(); j++) {

				Set<String> s1 = itemSets.get(i).getItems();
				Set<String> s2 = itemSets.get(j).getItems();

				// See if intersection.
				boolean isInter = isCandidate(pass, s1, s2);
				if (isInter) {
					// Find union.
					Set<String> union = new HashSet<String>(s1);
					union.addAll(s2);

					// See if already in list.
					if (isSetAlreadyInCandidateList(union))
						continue;

					// Put in candidate list.
					ItemSet newItem = new ItemSet(union);
					candidates.add(newItem);
				}
			}

		}

	}

	private boolean isCandidate(int desiredItemsetSize, Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<String>(s1);
		intersection.retainAll(s2);
		int intersectionSize = intersection.size();
		return intersectionSize == desiredItemsetSize - 1;
	}

	private boolean isSetAlreadyInCandidateList(Set<String> s) {
		
		for (ItemSet item : candidates) {
			// Get items in candidate set.
			Set<String> c = item.getItems();
			if (c.equals(s)) return true;
		}
		return false;
	}
	

	private List<AssociationRule> generateAssociationRules(List<ItemSet> itemSets) {
		System.out.println();
		
		List<AssociationRule> associationRules = new LinkedList<>();
		
		for (ItemSet item : itemSets) {
			System.out.print("Item set   {");
			for (String s : item.getItems())
				System.out.print(s + " ");
			System.out.println("}");
			
			int itemsetTransactionCount = database.getNumberOfRecordsContainingAttributes(item.getItems());

			List<List<String>> powerSet = new LinkedList<List<String>>();
			List<String> itemSet = new LinkedList<>(item.getItems());
			buildPowerSet(powerSet, itemSet);

			Collections.sort(powerSet, new Comparator<List<String>>() {
				@Override
				public int compare(List<String> o1, List<String> o2) {
					return o1.size() - o2.size();
				}
			});

			// delete last item in list since it is the entire set.
			powerSet.remove(powerSet.size() - 1);

			for (int i = 0; i < powerSet.size(); i++) {
				System.out.print(i + ": ");

				System.out.print("{");
				List<String> conditionSet = powerSet.get(i);
				int conditionItemsetTransactionCount = database.getNumberOfRecordsContainingAttributes(new HashSet<String>(conditionSet));
				for (String s : conditionSet)
					System.out.print(s + " ");
				System.out.print("} ==> {");

				List<String> associationSet = new LinkedList<String>(itemSet);
				associationSet.removeAll(conditionSet);
				for (String s : associationSet) {
					System.out.print(s + " ");
				}
				int consequentItemsetTransactionCount = database.getNumberOfRecordsContainingAttributes(new HashSet<String>(associationSet));

				System.out.print("}  C:");
				
				System.out.print(itemsetTransactionCount + "/" + conditionItemsetTransactionCount);
				
				double confidence = (double) itemsetTransactionCount / (double) conditionItemsetTransactionCount;
				System.out.print(" = " + df.format(confidence));
				
				System.out.print(", " + database.getTotalNumberOfTransactions() + "*" + itemsetTransactionCount + "/(" + 
						conditionItemsetTransactionCount + "*" + consequentItemsetTransactionCount + ") = ");
				
				double lift = ((double) itemsetTransactionCount * database.getTotalNumberOfTransactions())/ 
						((double) conditionItemsetTransactionCount * (double) consequentItemsetTransactionCount);
				System.out.println("L:" + df.format(lift));
				
				// If confidence is high enough, put in list.
				if (itemsetTransactionCount * 100 >= confidencePercentage * conditionItemsetTransactionCount) {
					// Sort to make display a little easier to read.
					Collections.sort(conditionSet);
					Collections.sort(associationSet);
					associationRules.add(new AssociationRule(conditionSet, associationSet, confidence, lift));
				}
			}

			System.out.println();
		}
		return associationRules;
	}

	private void buildPowerSet(List<List<String>> powerSet, List<String> list) {
		if (list.size() == 0)
			return;

		if (powerSet.contains(list))
			return;

		powerSet.add(list);

		for (int i = 0; i < list.size(); i++) {
			List<String> temp = new LinkedList<String>(list);
			temp.remove(i);
			buildPowerSet(powerSet, temp);
		}
	}
	
	private void printFinalAssociationRules(List<AssociationRule> associationRules) {
		System.out.println("*** Final association rules with minimum confidence percentage: " + confidencePercentage + " and Lift>1");

		for (AssociationRule ar : associationRules) {
			if (ar.getLift() <= 1.0) continue;
			System.out.print("C:" + df.format(ar.getConfidence()));
			System.out.print("  L:");
			System.out.print(df.format(ar.getLift()));			
			System.out.print("  {");
			for (String s : ar.getCondition()) System.out.print(s + " ");
			System.out.print("} ==> {");
			for (String s : ar.getAssociation()) System.out.print(s + " ");
			System.out.println("}");
		}
	}

	public static void main(String[] args) {

		Apriori a = new Apriori();
		a.application();
	}

}
