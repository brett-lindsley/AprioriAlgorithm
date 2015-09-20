package com;

import java.util.List;

public class AssociationRule {
	
	private List<String> condition;
	private List<String> association;
	private double confidence;
	private double lift;
	
	public AssociationRule(List<String> condition, List<String> association, double confidence, double lift) {
		this.condition = condition;
		this.association = association;
		this.confidence = confidence;
		this.lift = lift;
	}
	
	public List<String> getCondition() {
		return condition;
	}
	
	public List<String> getAssociation() {
		return association;
	}
	
	public double getConfidence() {
		return confidence;
	}
	
	public double getLift() {
		return lift;
	}

}
