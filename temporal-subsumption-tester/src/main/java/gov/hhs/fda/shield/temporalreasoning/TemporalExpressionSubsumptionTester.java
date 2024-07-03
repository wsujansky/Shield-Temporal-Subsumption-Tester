package gov.hhs.fda.shield.temporalreasoning;

import java.util.List;

import gov.hhs.fda.shield.temporalreasoning.utils.Printers;

public class TemporalExpressionSubsumptionTester {
	TemporalExpressionParser parser = new TemporalExpressionParser();
	TemporalExpressionNormalizer normalizer = new TemporalExpressionNormalizer(this);  // Normalizer uses certain methods from subsumption tester
	TemporalExpressionValidator validator = new TemporalExpressionValidator();
private static boolean DEBUG = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String temporalRelationshipExprSuper = "{ BB[0sc,+INF) & EE(-INF,-0sc] } | { BE(+0sc, +INF) }  "; // During or After
//		String temporalRelationshipExprSub = "{ BE(+1yr,+INF] } | { BE(+30sc,+2.5wk] & EE(-10sc,+20sc)}";
//		String temporalRelationshipExprSub = "{ BE(+30sc,+2.5wk] & EE(-10sc,+20sc)}";
//		String temporalRelationshipExprSuper = "{ EB(-INF,0sc) } | { BE(0sc,+INF] }";  // Before or After
//		String temporalRelationshipExprSub = "{ BE(+0dy,+INF) }  ";  // After
//		String temporalRelationshipExprSuper = " { BB(0sc, +INF) & EE(-INF,0sc) } |  { BE(0sc,+INF] }";  // During or within 4 hours after
//		String temporalRelationshipExprSub = " { BE[+30mn,+60mn] }  ";  // 30 - 60 min after
//		String temporalRelationshipExprSuper = " { BB(0sc, +INF) & EE(-INF,0sc) } ";  // During 
//		String temporalRelationshipExprSub = " { BE(+0hr,+30mn] }  ";  // within 30 min after
//		String temporalRelationshipExprSuper = " { BB[0sc, +INF) & EE(-INF,0sc] } ";  // During 
//		String temporalRelationshipExprSub = " { BB[0wk,+13wk] & EE[0sc,0sc] } | { BB[27wk, +INF) & EE[0sc,0sc] }";  // During 1st or 3rd trimester
//		String temporalRelationshipExprSuper = " { BB[0sc, +INF) & EE(-INF,0sc] } ";  // Started During 
//		String temporalRelationshipExprSub = " { BB[0wk,+13wk] & EE[0sc,0sc] } | { BB[27wk, +INF) & EE[0sc,0sc] }";  // Occurred during 1st or 3rd trimester
		String temporalRelationshipExprSuper = "{ BE(0sc,+1wk] }";
		String temporalRelationshipExprSub = "{ BE(0sc,+1mo] }";		
//		String temporalRelationshipExprSuper = "{ EE(-INF,-0.0dy) & EB[-3.5wk,+2mo) & BB(-1wk,+INF] }";
//		String temporalRelationshipExprSub = "{ EB(+4wk,+2mo) & BB(0dy,+INF) & EE(-INF, 0yr)  } ";
//		String temporalRelationshipExprSuper = "{ EB(0wk,+2mo) }";
//		String temporalRelationshipExprSub = "{ EB(-0wk,+2mo) }";
//		String temporalRelationshipExprSuper = "{ EB(-INF,1.9dy) }";
//		String temporalRelationshipExprSub = "{ EB(-INF,1.9dy] }";

//		Float zero1 = Float.valueOf("0");
//		Float zero2 = Float.valueOf("+0");
//		System.out.println("Float comparison: " + Double.compare(zero1,zero2));
		
/**		TemporalExpressionParser parser = new TemporalExpressionParser();
		
		RtipDisjunction superRtipStructure = parser.parse(temporalRelationshipExprSuper);
		
		RtipDisjunction subRtipStructure = parser.parse(temporalRelationshipExprSub);
**/
		
		TemporalExpressionSubsumptionTester tester = new TemporalExpressionSubsumptionTester();
		

		System.out.println("Does SUPER subsume SUB? => " + tester.subsumes(temporalRelationshipExprSuper, temporalRelationshipExprSub));
		System.out.println();

		System.out.println("Are SUPER and SUB equivalent? => " + tester.isEquivalent(temporalRelationshipExprSuper, temporalRelationshipExprSub));

//		System.out.println("Does SUPER subsume SUB? => " + tester.subsumes(superRtipStructure, subRtipStructure));

	}

	public TemporalExpressionSubsumptionTester() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean subsumes(String predicateExpression, String candidateExpression) {
		RtipDisjunction predicateDisjunction = parser.parse(predicateExpression);
		RtipDisjunction candidateDisjunction = parser.parse(candidateExpression);
		return subsumes(predicateDisjunction, candidateDisjunction);
	}
	
	public boolean isEquivalent(String predicateExpression, String candidateExpression) {
		RtipDisjunction predicateDisjunction = parser.parse(predicateExpression);
		RtipDisjunction candidateDisjunction = parser.parse(candidateExpression);
		return subsumes(predicateDisjunction, candidateDisjunction) && subsumes(candidateDisjunction, predicateDisjunction);
	}
	
	public boolean subsumes(RtipDisjunction superDisjunction, RtipDisjunction subDisjunction) {
		
		if (DEBUG) {
System.out.println("ORIGINAL PREDICATE DISJUNCTION: ");
Printers.printOrigText(superDisjunction,1);
}
		RtipDisjunction normalizedSuperDisjunction = normalizer.normalizeRtipDisjunction(superDisjunction);
		if (!validator.isSatisfiable(normalizedSuperDisjunction)) {
			System.out.println("The predicate temporal expression is not satisfiable: ");
			Printers.printOrigText(superDisjunction, 1);
			return false;
		}
if (DEBUG) {
System.out.println("NORMALIZED PREDICATE DISJUNCTION: ");
Printers.printOrigText(normalizedSuperDisjunction,1);
System.out.println();
}

if (DEBUG) {
System.out.println("ORIGINAL CANDIDATE DISJUNCTION: ");
Printers.printOrigText(subDisjunction,1);
}
		RtipDisjunction normalizedSubDisjunction = normalizer.normalizeRtipDisjunction(subDisjunction);
		if (!validator.isSatisfiable(normalizedSubDisjunction)) {
			System.out.println("The candidate temporal expression is not satisfiable: ");
			Printers.printOrigText(subDisjunction, 1);
			return false;
		}
if (DEBUG) {
System.out.println("NORMALIZED CANDIDATE DISJUNCTION: ");
Printers.printOrigText(normalizedSubDisjunction,1);
}

		if (subDisjunction.getRtipConjunctions().stream().allMatch(subConjunction ->
		       superDisjunction.getRtipConjunctions().stream().anyMatch(superConjunction ->
		           subsumes(superConjunction, subConjunction))) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean subsumes(RtipConjunction normalizedSuperConjunction, RtipConjunction normalizedSubConjunction) {
		try {
//			RtipConjunction normalizedSuperConjunction = this.normalizer.sortRtipConjunction(superConjunction);
//			RtipConjunction normalizedSubConjunction = this.normalizer.sortRtipConjunction(subConjunction);
			List<Rtip> superRtips = normalizedSuperConjunction.getRtips();
			List<Rtip> subRtips = normalizedSubConjunction.getRtips();
			if (superRtips.size() != subRtips.size()) {
				throw new Exception("Error in subsumes() test for RtipConjunctions => Lists not same size");
			}
			for (int i = 0; i < superRtips.size(); i++) {
				if (!subsumes(superRtips.get(i),subRtips.get(i)))
						return false;
			}
			return true;
//			Rtip superPredicate = superConjunction.getRtips().iterator().next();
//			Rtip subPredicate = subConjunction.getRtips().iterator().next();
//			if (subsumes(superPredicate, subPredicate)) {
//				return true;
//			} else {
//				return false;
//			}
		} catch (Exception e) {
			System.out.println("ERROR message: " + e.getMessage());
			return false;
		}

	}


	public boolean subsumes(Rtip superPredicate, Rtip subPredicate) {
//		System.out.println("superPredicate: " + superPredicate.toOriginalText());
//		Printers.print(superPredicate, 1);
//		System.out.println("subPredicate: " + subPredicate.toOriginalText());
//		Printers.print(subPredicate, 1);

		try {
			if (subPredicate.getRtipType() != superPredicate.getRtipType()) {
				throw new Exception("Error in subsumes() test for " + superPredicate.getOrigRtipText() + " and "
						+ subPredicate.getOrigRtipText() + " => Not same Rtip types");

			}
			if (lowerValueSubsumes(superPredicate, subPredicate) && upperValueSubsumes(superPredicate, subPredicate)) {
				return true;
			} else {
//System.out.println("FALSE: " + superPredicate.getOrigRtipText() + " does not subsume " + subPredicate.getOrigRtipText());
				return false;
			}
		} catch (Exception e) {
			System.out.println("ERROR message: " + e.getMessage());
			return false;
		}
	}
	
	public boolean lowerValueSubsumes(Rtip superPredicate, Rtip subPredicate) {
		try {
			if (subPredicate.isLowerValueInfinite() && superPredicate.isLowerValueInfinite())
				return true;
			else if (!subPredicate.isLowerValueInfinite() && superPredicate.isLowerValueInfinite())
				return true;
			else if (subPredicate.isLowerValueInfinite() && !superPredicate.isLowerValueInfinite())
				return false;
			else if (!subPredicate.isLowerValueInfinite() && !superPredicate.isLowerValueInfinite()) {
				if (Float.compare(subPredicate.getLowerValueSeconds(), superPredicate.getLowerValueSeconds()) > 0)
					return true;
				else if (Float.compare(subPredicate.getLowerValueSeconds(), superPredicate.getLowerValueSeconds()) < 0)
					return false;
				else if (Float.compare(subPredicate.getLowerValueSeconds(), superPredicate.getLowerValueSeconds()) == 0) {
					if (subPredicate.isLowerIntervalOpen() == superPredicate.isLowerIntervalOpen())
						return true;
					else if (subPredicate.isLowerIntervalOpen() && !superPredicate.isLowerIntervalOpen())
						return true;
					else if (!subPredicate.isLowerIntervalOpen() && superPredicate.isLowerIntervalOpen())
						return false;
				}
			}
			throw new Exception("Error in lowerValueSubsumes() test for " + superPredicate.getOrigRtipText() + " and "
					+ subPredicate.getOrigRtipText() + " => case not covered");
		} catch (Exception e) {
			System.out.println("ERROR message: " + e.getMessage());
			return false;
		}
	}

	public boolean upperValueSubsumes(Rtip superPredicate, Rtip subPredicate) {
		try {
			if (subPredicate.isUpperValueInfinite() && superPredicate.isUpperValueInfinite())
				return true;
			else if (!subPredicate.isUpperValueInfinite() && superPredicate.isUpperValueInfinite())
				return true;
			else if (subPredicate.isUpperValueInfinite() && !superPredicate.isUpperValueInfinite())
				return false;
			else if (!subPredicate.isUpperValueInfinite() && !superPredicate.isUpperValueInfinite()) {
				if (Float.compare(subPredicate.getUpperValueSeconds(), superPredicate.getUpperValueSeconds()) < 0)
					return true;
				else if (Float.compare(subPredicate.getUpperValueSeconds(), superPredicate.getUpperValueSeconds()) > 0)
					return false;
				else if (Float.compare(subPredicate.getUpperValueSeconds(), superPredicate.getUpperValueSeconds()) == 0) {
					if (subPredicate.isUpperIntervalOpen() == superPredicate.isUpperIntervalOpen())
						return true;
					else if (subPredicate.isUpperIntervalOpen() && !superPredicate.isUpperIntervalOpen())
						return true;
					else if (!subPredicate.isUpperIntervalOpen() && superPredicate.isUpperIntervalOpen())
						return false;
				}
			}
			throw new Exception("Error in upperValueSubsumes() test for " + superPredicate.getOrigRtipText() + " and "
					+ subPredicate.getOrigRtipText() + " => case not covered");
		} catch (Exception e) {
			System.out.println("ERROR message: " + e.getMessage());
			return false;
		}
	}

	
}
