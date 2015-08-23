package com;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GenPowerSet {
	
	@SuppressWarnings("serial")
	private void gen() {
		List<List<String>> powerSet = new LinkedList<List<String>>();
		List<String> mainList = new LinkedList<String>() {{ 
			add("a");
			add("b");
			add("c");
			add("d");
			add("e");
		}};
		buildPowerSet(powerSet, mainList);
		
		Collections.sort(powerSet, 
				new Comparator<List<String>>() {
					@Override
					public int compare(List<String> o1, List<String> o2) {
						return o1.size() - o2.size();
					}			
		});
		
		for (int i = 0; i < powerSet.size(); i++) {
			System.out.print(i + ": ");
			
			System.out.print("{");
			List<String> subset = powerSet.get(i);
			for (String s : subset) System.out.print(s + " ");
			System.out.println("}");
		}
	}
	
	private void buildPowerSet(List<List<String>> powerSet, List<String> list) {
		if (list.size() == 0) return;
		
		if (powerSet.contains(list)) return;
		
	    powerSet.add(list);

	    for(int i = 0; i < list.size(); i++) {
	        List<String> temp = new LinkedList<String>(list);
	        temp.remove(i);
	        buildPowerSet(powerSet, temp);
	    }
	}

	public static void main(String[] args) {
		GenPowerSet gps = new GenPowerSet();
		gps.gen();
	}

}
