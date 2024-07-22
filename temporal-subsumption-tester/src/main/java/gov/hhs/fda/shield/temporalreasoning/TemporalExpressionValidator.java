package gov.hhs.fda.shield.temporalreasoning;

import java.util.Iterator;
import java.util.List;

public class TemporalExpressionValidator {

	public TemporalExpressionValidator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String temporalRelationshipExprSuper = "{ BB[0dy,0dy] & EE(0dy,0dy] } | { BB[0dy,0dy] & EE[0dy,0dy] } | { EB(-10wk,-12mo) }  ";
		String temporalRelationshipExprSuper = "{ BB[0sc,+INF) & EE(-INF,-0sc] }";
//		String temporalRelationshipExprSuper = "{ BB(-1yr,+1yr] } | { BB(+3mo,+2.5yr] }";
//		String temporalRelationshipExprSub = "{ BB(-INF,+1mo) } | { BB(+6mo, +2yr) } } ";
//		String temporalRelationshipExprSuper = "{ BE[0dy,+INF] & EB[-10dy,0dy] }";
//		String temporalRelationshipExprSuper = "{ BB[0dy,0dy] & EE[0dy,0dy] }";
//		String temporalRelationshipExprSuper = "{ EE(-INF,-0.0dy) & EB[-3.5wk,+2mo) & BB(-1wk,+INF] }";
//		String temporalRelationshipExprSuper = "{ BE(-INF,+10wk) & BE[-3mo,+INF) & EB(-INF, 0yr) & EE(-3wk, +3wk) & EE(-2.5wk, +1.5wk) & EE(-2.75wk, +2dy) & BB(+1hr, +INF) } ";
//		String temporalRelationshipExprSuper = "{ EE(-2.75wk, +2dy) & BE(-INF,+10wk) & EE(-3wk, +3wk) & EB(-INF, 0yr) & BE[-3mo,+INF) & EE(-2.5wk, +1.5wk) & BB(+1hr, +INF) } ";
//		String temporalRelationshipExprSuper = "{ BB(-3wk,+2.5mo) }"; 
//		String temporalRelationshipExprSuper = "{ BB[-77wk, +INF) }";
//		String temporalRelationshipExprSub = "{ EB(-INF,+10wk) }";
//		String temporalRelationshipExprTemp = "{ EE(-INF,1.9dy) }";
//		String temporalRelationshipExprSub = "{ EB(-INF,1.9dy] }";

		TemporalExpressionParser parser = new TemporalExpressionParser();
		
		RtipDisjunction superRtipStructure = parser.parse(temporalRelationshipExprSuper);
		
//		TemporalExpressionSubsumptionTester subsumptionTester = new TemporalExpressionSubsumptionTester();
//		TemporalExpressionNormalizer normalizer = new TemporalExpressionNormalizer(subsumptionTester);
		
//		RtipConjunction rtipConjunction = superRtipStructure.getRtipConjunctions().iterator().next();
//		Rtip rtip = rtipConjunction.getRtips().iterator().next();
		
//		System.out.println("Is " + rtip.getOrigRtipText() + " satisfiable? => " + isSatisfiable(rtip));
		
/***  DEBUGGING ONLY
  		System.out.print("Is this rtipConjunction satisfiable?: ");
 
		Printers.printOrigText(rtipConjunction, 0); 
		
		rtipConjunction = normalizer.expandRtipConjunction(rtipConjunction);
		rtipConjunction = normalizer.sortRtipConjunction(rtipConjunction);
		rtipConjunction = normalizer.consolidateRtipConjunction(rtipConjunction);
		System.out.print("Consolidated Sorted Expanded Conjunction: ");
		Printers.printGeneratedText(rtipConjunction, 0);

		System.out.println("Answer: " + isSatisfiable(rtipConjunction));
***/
		
		System.out.println("Is the rtipDisjunction satisfiable?");
		System.out.println("Answer: " + isSatisfiable(superRtipStructure));


	}
	
	// Basic validation to ensure that the RTIP expression is satisfiable, i.e., that it is possible for the temporal
	// relationship to be satisfied by two events.  An example of an unsatisfiable RTIP expression would be:
	//  "event 1 starts after event 2 ends AND event 1 ends before event 2 starts"
	// Note:  If such an unsatisfiable conjunction of 2 RTIPs is specified, the expansion and consolidation steps of the
	// normalization process (which precedes the satisfiability test) result in a consolidated RTIP of at least one type
	// that, itself, cannot be satisfied by any two events.
	public static boolean isSatisfiable(RtipDisjunction rtipDisjunction) {
		TemporalExpressionSubsumptionTester subsumptionTester = new TemporalExpressionSubsumptionTester();
		TemporalExpressionNormalizer normalizer = new TemporalExpressionNormalizer(subsumptionTester);

		List<RtipConjunction> rtipConjunctionList = rtipDisjunction.getRtipConjunctions();
		
		for (Iterator<RtipConjunction> iterator = rtipConjunctionList.iterator(); iterator.hasNext();) {
			RtipConjunction rtipConjunction = (RtipConjunction) iterator.next();
			rtipConjunction = normalizer.expandRtipConjunction(rtipConjunction);
			rtipConjunction = normalizer.sortRtipConjunction(rtipConjunction);
			rtipConjunction = normalizer.consolidateRtipConjunction(rtipConjunction);
			if (isSatisfiable(rtipConjunction))
				return true;  // Note:  Only one of the disjoined ("Or'd") rtipConjuctions needs to be satisfiable for the
			                  //        entire disjunction to be satisfiable.
		}
		return false;
	}

	
	public static boolean isSatisfiable(RtipConjunction rtipConjunction) {
		List<Rtip> rtipList = rtipConjunction.getRtips();
		for (Iterator<Rtip> iterator = rtipList.iterator(); iterator.hasNext();) {
			Rtip rtip = (Rtip) iterator.next();
			if (!isSatisfiable(rtip))
				return false;
		}
		return true;
	}
	
	// A consolidated RTIP is not satisfiable if the lower bound on the duration between two time points is greater than the 
	// upper bound on the same two time points, because no value can satisfy such an RTIP.  E.g., if an RTIP specifies that
	// the duration between two time points must be both > 10 days and < 5 days, that constraint can never be satisfied.
	public static boolean isSatisfiable(Rtip rtip) {
		if (!rtip.isFullySpecified())
			return false;
		if (rtip.isLowerValueInfinite()) {
			return true;
		}
		if (rtip.isUpperValueInfinite())
			return true;
		if (Float.compare(rtip.getLowerValueSeconds(), rtip.getUpperValueSeconds()) < 0)
			return true;  // lower value will be less, regardless of open/closed intervals
		if (Float.compare(rtip.getLowerValueSeconds(), rtip.getUpperValueSeconds()) > 0)
			return false;  // lower value will be more, regardless of open/closed intervals
		if (Float.compare(rtip.getLowerValueSeconds(), rtip.getUpperValueSeconds()) == 0) {
			if (!rtip.isLowerIntervalOpen() && !rtip.isUpperIntervalOpen()) // if upper and lower magnitudes the same, must be a closed interval, i.e., "[" and "]"
				return true;
		}
		return false;
	}
	

	
}
