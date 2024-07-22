package gov.hhs.fda.shield.temporalreasoning;

import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import gov.hhs.fda.shield.temporalreasoning.utils.Printers;

/*  This class implements the normalization process for Relative Temporal Interval Relationships (RTIPs) as described in the paper
 *  https://medrxiv.org/cgi/content/short/2023.11.17.23298715v1
 */
public class TemporalExpressionNormalizer {
	
	TemporalExpressionSubsumptionTester subsumptionTester;
	
	public static Rtip rtipStructureTemp;  // for debugging
	private static boolean DEBUG = true;

	// Main method for debugging only
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String temporalRelationshipExpr = "{ BB[0sc,+INF) & EE(-INF,-0sc] } | { BE(+0sc, +INF) }  ";
//		String temporalRelationshipExprSuper = "{ BB(-1yr,+1yr] } | { BB(+3mo,+2.5yr] }";
//		String temporalRelationshipExprSub = "{ BB(-INF,+1mo) } | { BB(+6mo, +2yr) } } ";
		String temporalRelationshipExprSuper = "{ BB[0sc,+INF) & EE(-INF,-0sc] }";
//		String temporalRelationshipExprSuper = "{ EE(-INF,-0.0dy) & EB[-3.5wk,+2mo) & BB(-1wk,+INF] }";
//		String temporalRelationshipExprSuper = "{ BE(-INF,+10wk) & BE[-3mo,+INF) & EB(-INF, 0yr) & EE(-3wk, +3wk) & EE(-2.5wk, +1.5wk) & EE(-2.75wk, +2dy) & BB(+1hr, +INF) } ";

//		String temporalRelationshipExprSuper = "{ EE(-2.75wk, +2dy) & BE(-INF,+10wk) & EE(-3wk, +3wk) & EB(-INF, 0yr) & BE[-3mo,+INF) & EE(-2.5wk, +1.5wk) & BB(+1hr, +INF) } ";
//		String temporalRelationshipExprSuper = "{ BB(-3wk,+2.5mo) }"; 
//		String temporalRelationshipExprSuper = "{ EB[-INF,+INF) }";
//		String temporalRelationshipExprSub = "{ EB(-INF,+10wk) }";
//		String temporalRelationshipExprTemp = "{ EE(-INF,1.9dy) }";
//		String temporalRelationshipExprSub = "{ EB(-INF,1.9dy] }";

		TemporalExpressionParser parser = new TemporalExpressionParser();
		
		RtipDisjunction superRtipStructure = parser.parse(temporalRelationshipExprSuper);
//		System.out.println("SUPER TEMPORAL RELATIONSHIP");
//		Printers.print(superRtipStructure, 0);
//		System.out.println();
//		Rtip superPredicate = superRtipStructure.getRtipConjunctions().iterator().next().getRtips().iterator().next();
//		System.out.println("ORIG PREDICATE 1: " + superPredicate.getOrigRtipText() + " { " + 
//		                                          superPredicate.fromValuesToText() + " }");
		
//		RtipDisjunction subRtipStructure = parser.parse(temporalRelationshipExprSub);
//		System.out.println("SUB TEMPORAL RELATIONSHIP");
//		Printers.print(subRtipStructure, 0);
//		System.out.println();
//		Rtip subPredicate = subRtipStructure.getRtipConjunctions().iterator().next().getRtips().iterator().next();
//		System.out.println("ORIG PREDICATE 2: " + subPredicate.getOrigRtipText() + " { " + 
//                								  subPredicate.fromValuesToText() + " }");
		
		TemporalExpressionSubsumptionTester subsumptionTester = new TemporalExpressionSubsumptionTester();
		TemporalExpressionNormalizer normalizer = new TemporalExpressionNormalizer(subsumptionTester);
		
//		rtipStructureTemp = new Rtip();
//		System.out.println("Is it fully specified: " + rtipStructureTemp.isFullySpecified());
//		rtipStructureTemp = parser.parse(temporalRelationshipExprTemp).getRtipConjunctions().iterator().next().getRtips().iterator().next();
//		System.out.println("Is it fully specified: " + rtipStructureTemp.isFullySpecified());
//		System.out.println("Temp Rtip: " + rtipStructureTemp.fromValuesToText());
//		Printers.print(rtipStructureTemp, 0);

		
		RtipConjunction rtipConjunction = superRtipStructure.getRtipConjunctions().iterator().next();
if (DEBUG) {
System.out.print("Original Conjunction (Orig): ");
Printers.printOrigText(rtipConjunction, 0);
System.out.print("Original Conjunction (Generated): ");
Printers.printGeneratedText(rtipConjunction, 0);
}
		rtipConjunction = normalizer.expandRtipConjunction(rtipConjunction);
if (DEBUG) {
System.out.print("Expanded Conjunction (Orig): ");
Printers.printOrigText(rtipConjunction, 0);
System.out.print("Expanded Conjunction (Generated): ");
Printers.printGeneratedText(rtipConjunction, 0);
}
		rtipConjunction = normalizer.sortRtipConjunction(rtipConjunction);
if (DEBUG) {
System.out.print("Sorted Expanded Conjunction (Orig): ");
Printers.printOrigText(rtipConjunction, 0);
System.out.print("Sorted Expanded Conjunction (Generated): ");
Printers.printGeneratedText(rtipConjunction, 0);
}
		rtipConjunction = normalizer.consolidateRtipConjunction(rtipConjunction);
if (DEBUG) {
System.out.print("Consolidated Sorted Expanded Conjunction: ");
Printers.printGeneratedText(rtipConjunction, 0);
}

//DEBUG	Rtip intersectedPredicate = normalizer.intersectRtips(superPredicate, subPredicate);
//DEBUG	System.out.println("INTERSECTED PREDICATE => " + intersectedPredicate.getOrigRtipText());
//DEBUG	Printers.print(intersectedPredicate, 1);

	}
	
	// Constructor
	public TemporalExpressionNormalizer(TemporalExpressionSubsumptionTester subsumptionTester) {
		this.subsumptionTester = subsumptionTester;
	}
	
	// Normalization consists of first "expanding" each RTIP conjunction, i.e. adding every *implied* RTIP for each stated RTIP,
	// then sorting the resulting (expanded) conjunction of RTIPs by RTIP type, and finally logically consolidating the RTIPs of each type
	// into a single RTIP of that type, so that the resulting expression is a conjunction of exactly 4 RTIPs, one of each type.
	// For details of the justification and proof of this algorithm, see the paper.
	public RtipDisjunction normalizeRtipDisjunction(RtipDisjunction rtipDisjunction) {
		List<RtipConjunction> rtipConjunctionList = rtipDisjunction.getRtipConjunctions();
		for (Iterator<RtipConjunction> iterator = rtipConjunctionList.iterator(); iterator.hasNext();) {
			RtipConjunction rtipConjunction = (RtipConjunction) iterator.next();
			rtipConjunction = expandRtipConjunction(rtipConjunction); 
			rtipConjunction = sortRtipConjunction(rtipConjunction);
			rtipConjunction = consolidateRtipConjunction(rtipConjunction);
		}
		return rtipDisjunction;
	}

	public RtipConjunction expandRtipConjunction(RtipConjunction rtipConjunction) {
		List<Rtip> rtipList = rtipConjunction.getRtips();
		int origListSize = rtipList.size(); // Initialize
		if (origListSize == 0) {  // empty list - no Rtips
			System.out.println("Empty RtipList - no op");
			return rtipConjunction;  // no-op
		}
		int currentIndex = 0; // Initialize
		while (currentIndex < origListSize) {
			Rtip currentRtip = rtipList.get(currentIndex); 
			rtipList = addImpliedRtips(currentRtip, rtipList);
			currentIndex++;
			}
		rtipConjunction.setRtips(rtipList);
		return rtipConjunction;
	}

	public RtipConjunction consolidateRtipConjunction(RtipConjunction rtipConjunction) {
		List<Rtip> rtipList = rtipConjunction.getRtips();
		int currentListSize = rtipList.size(); // Initialize
		if (currentListSize == 0) {  // empty list - no Rtips
			return rtipConjunction;  // no-op
		}
		int currentIndex = 0; // Initialize
		int nextIndex = 1; // Initialize
		while (nextIndex < rtipList.size()) {
			Rtip currentRtip = rtipList.get(currentIndex); 
			Rtip nextRtip = rtipList.get(nextIndex);
			if (currentRtip.getRtipType() == nextRtip.getRtipType()) { // If same types, then consolidate the pair
																	   // and replace it by the consolidated RTIP
				Rtip intersectedRtip = intersectRtips(currentRtip,nextRtip);
				rtipList.remove(nextIndex);
				rtipList.remove(currentIndex);
				rtipList.add(currentIndex, intersectedRtip);
			}
			else { // otherwise, just advance to next RTIP, since RTIPs of different types cannot be consolidated
			currentIndex++;
			nextIndex++;
			}
		}
		rtipConjunction.setRtips(rtipList);
		return rtipConjunction;
	}
	
	public List<Rtip> addImpliedRtips(Rtip rtip, List<Rtip> rtipList) {
		switch (rtip.getRtipType()) {
		case BEG_BEG: return addImpliedBegBegRtips(rtip, rtipList);
		case BEG_END: return addImpliedBegEndRtips(rtip, rtipList);
		case END_BEG: return addImpliedEndBegRtips(rtip, rtipList);
		case END_END: return addImpliedEndEndRtips(rtip, rtipList);
		default: return rtipList; 
		}
	}
	
	// See Table 2. RTIP Implication Table in the paper - Row "BB"
	public List<Rtip> addImpliedBegBegRtips(Rtip sourceRtip, List<Rtip> rtipList) {
		Rtip impliedBeRtip = new Rtip(RtipType.BEG_END);
		impliedBeRtip = setLowerValueNegInf(impliedBeRtip);
		impliedBeRtip = setUpperValuesFromRtip(impliedBeRtip, sourceRtip);
		impliedBeRtip.setOrigRtipText(impliedBeRtip.fromValuesToText());
		rtipList.add(impliedBeRtip);
		Rtip impliedEbRtip = new Rtip(RtipType.END_BEG);
		impliedEbRtip = setLowerValuesFromRtip(impliedEbRtip, sourceRtip);
		impliedEbRtip = setUpperValuePosInf(impliedEbRtip);
		impliedEbRtip.setOrigRtipText(impliedEbRtip.fromValuesToText());
		rtipList.add(impliedEbRtip);
		Rtip impliedEeRtip = new Rtip(RtipType.END_END);
		impliedEeRtip = setLowerValueNegInf(impliedEeRtip);
		impliedEeRtip = setUpperValuePosInf(impliedEeRtip);
		impliedEeRtip.setOrigRtipText(impliedEeRtip.fromValuesToText());
		rtipList.add(impliedEeRtip);
		return rtipList;
	}
	
	// See Table 2. RTIP Implication Table in the paper - Row "BE"
	public List<Rtip> addImpliedBegEndRtips(Rtip sourceRtip, List<Rtip> rtipList) {
		Rtip impliedBbRtip = new Rtip(RtipType.BEG_BEG);
		impliedBbRtip = setLowerValuesFromRtip(impliedBbRtip, sourceRtip);
		impliedBbRtip = setUpperValuePosInf(impliedBbRtip);
		impliedBbRtip.setOrigRtipText(impliedBbRtip.fromValuesToText());
		rtipList.add(impliedBbRtip);
		Rtip impliedEbRtip = new Rtip(RtipType.END_BEG);
		impliedEbRtip = setLowerValuesFromRtip(impliedEbRtip, sourceRtip);
		impliedEbRtip = setUpperValuePosInf(impliedEbRtip);
		impliedEbRtip.setOrigRtipText(impliedEbRtip.fromValuesToText());
		rtipList.add(impliedEbRtip);
		Rtip impliedEeRtip = new Rtip(RtipType.END_END);
		impliedEeRtip = setLowerValuesFromRtip(impliedEeRtip, sourceRtip);
		impliedEeRtip = setUpperValuePosInf(impliedEeRtip);
		impliedEeRtip.setOrigRtipText(impliedEeRtip.fromValuesToText());
		rtipList.add(impliedEeRtip);
		return rtipList;
	}
	
	// See Table 2. RTIP Implication Table in the paper - Row "EB"
	public List<Rtip> addImpliedEndBegRtips(Rtip sourceRtip, List<Rtip> rtipList) {
		Rtip impliedBbRtip = new Rtip(RtipType.BEG_BEG);
		impliedBbRtip = setLowerValueNegInf(impliedBbRtip);
		impliedBbRtip = setUpperValuesFromRtip(impliedBbRtip, sourceRtip);
		impliedBbRtip.setOrigRtipText(impliedBbRtip.fromValuesToText());
		rtipList.add(impliedBbRtip);
		Rtip impliedBeRtip = new Rtip(RtipType.BEG_END);
		impliedBeRtip = setLowerValueNegInf(impliedBeRtip);
		impliedBeRtip = setUpperValuesFromRtip(impliedBeRtip, sourceRtip);
		impliedBeRtip.setOrigRtipText(impliedBeRtip.fromValuesToText());
		rtipList.add(impliedBeRtip);
		Rtip impliedEeRtip = new Rtip(RtipType.END_END);
		impliedEeRtip = setLowerValueNegInf(impliedEeRtip);
		impliedEeRtip = setUpperValuesFromRtip(impliedEeRtip, sourceRtip);
		impliedEeRtip.setOrigRtipText(impliedEeRtip.fromValuesToText());
		rtipList.add(impliedEeRtip);
		return rtipList;
	}
	
	// See Table 2. RTIP Implication Table in the paper - Row "EE"
	public List<Rtip> addImpliedEndEndRtips(Rtip sourceRtip, List<Rtip> rtipList) {
		Rtip impliedBbRtip = new Rtip(RtipType.BEG_BEG);
		impliedBbRtip = setLowerValueNegInf(impliedBbRtip);
		impliedBbRtip = setUpperValuePosInf(impliedBbRtip);
		impliedBbRtip.setOrigRtipText(impliedBbRtip.fromValuesToText());
		rtipList.add(impliedBbRtip);
		Rtip impliedBeRtip = new Rtip(RtipType.BEG_END);
		impliedBeRtip = setLowerValueNegInf(impliedBeRtip);
		impliedBeRtip = setUpperValuesFromRtip(impliedBeRtip, sourceRtip);
		impliedBeRtip.setOrigRtipText(impliedBeRtip.fromValuesToText());
		rtipList.add(impliedBeRtip);
		Rtip impliedEbRtip = new Rtip(RtipType.END_BEG);
		impliedEbRtip = setLowerValuesFromRtip(impliedEbRtip, sourceRtip);
		impliedEbRtip = setUpperValuePosInf(impliedEbRtip);
		impliedEbRtip.setOrigRtipText(impliedEbRtip.fromValuesToText());
		rtipList.add(impliedEbRtip);
		return rtipList;
	}
	
	public Rtip intersectRtips(Rtip rtip1, Rtip rtip2) {
		Rtip intersectedRtip = new Rtip();
		try {
			if (rtip1.getRtipType() != rtip2.getRtipType()) {
				throw new Exception("Error in intersectRtips() for " + rtip1.getOrigRtipText() + " and "
						+ rtip2.getOrigRtipText() + " => Not same Rtip types");
			}
			// Set the RtipType of the resulting RTIP (does not changes; note that the types of rtip1 and rtip2 are the same)
			intersectedRtip.setRtipType(rtip1.getRtipType()); 
			// Set lower value to be the most restrictive of the two
			if (subsumptionTester.lowerValueSubsumes(rtip1, rtip2)) {
				intersectedRtip = setLowerValuesFromRtip(intersectedRtip, rtip2);
			}
			else {
				intersectedRtip = setLowerValuesFromRtip(intersectedRtip, rtip1);
			} 
			// Set upper value to be the most restrictive of the two
			if (subsumptionTester.upperValueSubsumes(rtip1, rtip2)) {
				intersectedRtip = setUpperValuesFromRtip(intersectedRtip, rtip2);
			}
			else {
				intersectedRtip = setUpperValuesFromRtip(intersectedRtip, rtip1);
			}
			// Reset the text representation of the Rtip (for printing/debuggin)
			intersectedRtip.setOrigRtipText(intersectedRtip.fromValuesToText()); // Regenerate from intersected values 
			return intersectedRtip;
			} catch (Exception e) {
				System.out.println("ERROR message: " + e.getMessage());
				return intersectedRtip;				
				} 
	}
	
	public Rtip setLowerValuesFromRtip(Rtip destinationRtip, Rtip sourceRtip) {
		destinationRtip.setLowerIntervalOpen(sourceRtip.isLowerIntervalOpen());
		destinationRtip.setLowerValueInfinite(sourceRtip.isLowerValueInfinite());
		destinationRtip.setLowerValueSeconds(sourceRtip.getLowerValueSeconds());
		return destinationRtip;
	}
	
	public Rtip setUpperValuesFromRtip(Rtip destinationRtip, Rtip sourceRtip) {
		destinationRtip.setUpperIntervalOpen(sourceRtip.isUpperIntervalOpen());
		destinationRtip.setUpperValueInfinite(sourceRtip.isUpperValueInfinite());
		destinationRtip.setUpperValueSeconds(sourceRtip.getUpperValueSeconds());
		return destinationRtip;
	}
	
	public Rtip setLowerValueNegInf(Rtip destinationRtip) {
		destinationRtip.setLowerIntervalOpen(true);
		destinationRtip.setLowerValueInfinite(true);
		destinationRtip.setLowerValueSeconds(null);
		return destinationRtip;
	}
	
	public Rtip setUpperValuePosInf(Rtip destinationRtip) {
		destinationRtip.setUpperIntervalOpen(true);
		destinationRtip.setUpperValueInfinite(true);
		destinationRtip.setUpperValueSeconds(null);
		return destinationRtip;
	}

	// Sorts a conjunction of RTIPs by the string that denotes the type of each RTIP (e.g., "BB", "BE", "EB", and "EE")
	public RtipConjunction sortRtipConjunction(RtipConjunction rtipConjunction) {
		List<Rtip> list = rtipConjunction.getRtips();

    list.sort( new Comparator<Rtip>() {
    @Override
        public int compare(Rtip rtip1, Rtip rtip2)  {
            return Collator.getInstance().compare(
            		rtip1.getRtipType().toString(),
            		rtip2.getRtipType().toString());
        }
    } );
    return rtipConjunction;
	}


}
