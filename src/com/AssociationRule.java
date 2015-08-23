package com;

import java.util.List;

public class AssociationRule {
	
	private List<String> condition;
	private List<String> association;
	private double confidence;
	
	public AssociationRule(List<String> condition, List<String> association, double confidence) {
		this.condition = condition;
		this.association = association;
		this.confidence = confidence;
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
}
