package gov.hhs.fda.shield.temporalreasoning;

import java.util.List;

import gov.hhs.fda.shield.temporalreasoning.utils.Printers;


/** This class implements the subsumption-testing algorithm for relative temporal relationships that is described in the paper
 *  https://medrxiv.org/cgi/content/short/2023.11.17.23298715v1
 *    
 *  The inputs to the algorithm are two strings, each consisting of a valid expressions defined by the context-free
 *  grammar TemporalRelationshipGrammar.g4  
 * 
 */
public class TemporalExpressionSubsumptionTester {
	TemporalExpressionParser parser = new TemporalExpressionParser();
	TemporalExpressionNormalizer normalizer = new TemporalExpressionNormalizer(this);  // Normalizer uses certain methods from subsumption tester
																					
	private static boolean DEBUG = false;
	

	// Main method for debugging only
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String temporalRelationshipExprSuper = "{ BB[0sc,+INF) & EE(-INF,-0sc] } | { BE(+0sc, +INF) }  "; // During or After
//		String temporalRelationshipExprSub = "{ BE(+1yr,+INF] } | { BE(+30sc,+2.5wk] & EE(-10sc,+20sc)}";
//		String temporalRelationshipExprSub = "{ BE(+30sc,+2.5wk] & EE(-10sc,+20sc)}";
//		String temporalRelationshipExprSuper = "{ EB(-INF,0sc) } | { BE(0sc,+INF] }";  // Before or After
//		String temporalRelationshipExprSub = "{ BE(+0dy,+INF) }  ";  // After
		String temporalRelationshipExprSuper = " { BB(0sc, +INF) & EE(-INF,0sc) } |  { BE(0sc,+10mn] }";  // During or within 4 hours after
		String temporalRelationshipExprSub = " { BE[+30mn,+60mn] }  ";  // 30 - 60 min after
//		String temporalRelationshipExprSuper = " { BB(0sc, +INF) & EE(-INF,0sc) } ";  // During 
//		String temporalRelationshipExprSub = " { BE(+0hr,+30mn] }  ";  // within 30 min after
//		String temporalRelationshipExprSuper = " { BB[0sc, +INF) & EE(-INF,0sc] } ";  // During 
//		String temporalRelationshipExprSub = " { BB[0wk,+13wk] & EE[0sc,0sc] } | { BB[27wk, +INF) & EE[0sc,0sc] }";  // During 1st or 3rd trimester
//		String temporalRelationshipExprSuper = " { BB[0sc, +INF) & EE(-INF,0sc] } ";  // Started During 
//		String temporalRelationshipExprSub = " { BB[0wk,+13wk] & EE[0sc,0sc] } | { BB[27wk, +INF) & EE[0sc,0sc] }";  // Occurred during 1st or 3rd trimester
//		String temporalRelationshipExprSuper = "{ BE(0sc,+1wk] }";
//		String temporalRelationshipExprSub = "{ BE(0sc,+1mo] }";		
//		String temporalRelationshipExprSuper = "{ EE(-INF,-0.0dy) & EB[-3.5wk,+2mo) & BB(-1wk,+INF] }";
//		String temporalRelationshipExprSub = "{ EB(+4wk,+2mo) & BB(0dy,+INF) & EE(-INF, 0yr)  } ";
//		String temporalRelationshipExprSuper = "{ EB(0wk,+2mo) }";
//		String temporalRelationshipExprSub = "{ EB(-0wk,+2mo) }";
//		String temporalRelationshipExprSuper = "{ EB(-INF,1.9dy) }";
//		String temporalRelationshipExprSub = "{ EB(-INF,1.9dy] }";

		
/**		TemporalExpressionParser parser = new TemporalExpressionParser();
		
		RtipDisjunction superRtipStructure = parser.parse(temporalRelationshipExprSuper);
		
		RtipDisjunction subRtipStructure = parser.parse(temporalRelationshipExprSub);
**/
		
		TemporalExpressionSubsumptionTester tester = new TemporalExpressionSubsumptionTester();
		System.out.println("Does SUPER subsume SUB? => " + tester.subsumes(temporalRelationshipExprSuper, temporalRelationshipExprSub));
		System.out.println();
		System.out.println("Are SUPER and SUB equivalent? => " + tester.isEquivalent(temporalRelationshipExprSuper, temporalRelationshipExprSub));



	}

	public TemporalExpressionSubsumptionTester() {
		
	}
	
	// The key method that this package provides to test subsumption between two relative-temporal-relationship expressions
	// Returns TRUE if the predicateExpression subsumes the candidateExpression; else, returns FALSE
	public boolean subsumes(String predicateExpression, String candidateExpression) {
		// Generate parse trees for the predicate and candidate expressions, each consisting of a RtipDisjunction object
		RtipDisjunction predicateDisjunction = parser.parse(predicateExpression);
		RtipDisjunction candidateDisjunction = parser.parse(candidateExpression);
		// Perform subsumption testing on the parse trees
		return subsumes(predicateDisjunction, candidateDisjunction);
	}
	
	// The two expressions are logically equivalent if they both subsume (i.e., are implied by) each other, per the rules of logic
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
		// First, normalizes both the predicate and candidate expressions.  

		RtipDisjunction normalizedSuperDisjunction = normalizer.normalizeRtipDisjunction(superDisjunction);
		// Also perform some basic semantic validation of the predicate expression.
		if (!TemporalExpressionValidator.isSatisfiable(normalizedSuperDisjunction)) {
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
		// Also perform some basic semantic validation of the predicate expression.
		if (!TemporalExpressionValidator.isSatisfiable(normalizedSubDisjunction)) {
			System.out.println("The candidate temporal expression is not satisfiable: ");
			Printers.printOrigText(subDisjunction, 1);
			return false;
		}
if (DEBUG) {
System.out.println("NORMALIZED CANDIDATE DISJUNCTION: ");
Printers.printOrigText(normalizedSubDisjunction,1);
}

		// Tests whether, for every conjunction Cx in the candidate expression, there is at least one conjunction Cy in the predicate expression
		// such that Cy subsumes Cx (see the paper for justification/proof of this method)
		if (subDisjunction.getRtipConjunctions().stream().allMatch(subConjunction ->
		       superDisjunction.getRtipConjunctions().stream().anyMatch(superConjunction ->
		           subsumes(superConjunction, subConjunction))) ) {
			return true;
		}
		else {
			return false;
		}
	}

	// Tests whether a normalized conjunction of RTIPs that appears in the predicateExpression subsumes a normalized conjunction of RTIPs
	// that appears in the candidate expression ((see the paper for justification/proof of this method)
	public boolean subsumes(RtipConjunction normalizedSuperConjunction, RtipConjunction normalizedSubConjunction) {
		try {
			List<Rtip> superRtips = normalizedSuperConjunction.getRtips();
			List<Rtip> subRtips = normalizedSubConjunction.getRtips();
			if (superRtips.size() != subRtips.size()) {
				throw new Exception("Error in subsumes() test for RtipConjunctions => Lists not same size");
			}
			// Iterates through each pair of matching RTIPs in the predicate (super) conjunction and candidate (sub) conjunction,
			// and tests whether the predicate (super) RTIP subsumes the candidate (sub) RTIP.  Only returns true if the subsumption
			// relationship holds TRUE for every such pair.
			// NOTE:  For this algorithm to work correctly, the normalized predicate and candidate conjunctions must have the followring properties:
			//    1.  Each must contain exactly 4 RTIPs, one of each kind BB, BE, EB, and EE
			//    2.  Each must be sorted by the type of RTIP, so that each pair of RTIPs that are subsumption tested are of the same type.
			for (int i = 0; i < superRtips.size(); i++) {
				if (!subsumes(superRtips.get(i),subRtips.get(i)))
						return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("ERROR message: " + e.getMessage());
			return false;
		}

	}

	// The remainder of the methods test whether one RTIP subsumes another RTIP, per the algorithm described and proven
	// in the paper 
	public boolean subsumes(Rtip superPredicate, Rtip subPredicate) {
		try {
			if (subPredicate.getRtipType() != superPredicate.getRtipType()) {
				throw new Exception("Error in subsumes() test for " + superPredicate.getOrigRtipText() + " and "
						+ subPredicate.getOrigRtipText() + " => Not same Rtip types");

			}
			if (lowerValueSubsumes(superPredicate, subPredicate) && upperValueSubsumes(superPredicate, subPredicate)) {
				return true;
			} else {
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
