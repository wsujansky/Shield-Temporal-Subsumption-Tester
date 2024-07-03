package gov.hhs.fda.shield.temporalreasoning.utils;

import java.util.Iterator;
import java.util.List;

import gov.hhs.fda.shield.temporalreasoning.Rtip;
import gov.hhs.fda.shield.temporalreasoning.RtipConjunction;
import gov.hhs.fda.shield.temporalreasoning.RtipDisjunction;

public class Printers {

	public Printers() {
		// TODO Auto-generated constructor stub
	}
	
	public static void print (RtipDisjunction rtipDisjunction, int level) {
		System.out.println(indentSpaces(level) + "DISJUNCTION OF (");
		List<RtipConjunction> listOfRtipConjunctions = rtipDisjunction.getRtipConjunctions();
		for  (RtipConjunction rtipConjunction : listOfRtipConjunctions) {
			print(rtipConjunction, level+1);
		}
		System.out.println(indentSpaces(level) + ") END DISJUNCTION");		
	}

	public static void print (RtipConjunction rtipConjunction, int level) {
			System.out.println(indentSpaces(level) + "CONJUNCTION OF (");
			List<Rtip> listOfRtips = rtipConjunction.getRtips();
			for  (Rtip rtip : listOfRtips) {
				print(rtip, level+1);
			}
			System.out.println(indentSpaces(level) + ") END CONJUNCTION");		
	}

	public static void print (Rtip rtip, int level) {
		System.out.println(indentSpaces(level) + "RTIP: " + rtip.getOrigRtipText());
		System.out.println(indentSpaces(level) + " rtipType: " + rtip.getRtipType());
		System.out.println(indentSpaces(level) + " lowerIntervalOpen: " + rtip.isLowerIntervalOpen());
		System.out.println(indentSpaces(level) + " upperIntervalOpen: " + rtip.isUpperIntervalOpen());
		System.out.println(indentSpaces(level) + " lowerIntervalInfinite: " + rtip.isLowerValueInfinite());
		System.out.println(indentSpaces(level) + " upperIntervalInfinite: " + rtip.isUpperValueInfinite());
		if (!rtip.isLowerValueInfinite()) {
			System.out.println(indentSpaces(level) + " lowerSecondsValue: " + rtip.getLowerValueSeconds());
		}
		else {
			System.out.println(indentSpaces(level) + " lowerSecondsValue: N/A");

		}
		if (!rtip.isUpperValueInfinite()) {
			System.out.println(indentSpaces(level) + " upperSecondsValue: " + rtip.getUpperValueSeconds());
		}
		else {
			System.out.println(indentSpaces(level) + " upperSecondsValue: N/A");

		}
	}
	
	public static void printOrigText (RtipDisjunction rtipDisjunction, int level) {
		System.out.println(indentSpaces(level) + "DISJUNCTION OF (");
		List<RtipConjunction> listOfRtipConjunctions = rtipDisjunction.getRtipConjunctions();
		for  (RtipConjunction rtipConjunction : listOfRtipConjunctions) {
			printOrigText(rtipConjunction, level+1);
		}
		System.out.println(indentSpaces(level) + ") END DISJUNCTION");		
	}

	public static void printOrigText (RtipConjunction rtipConjunction, int level) {
		System.out.print(indentSpaces(level) + "{ ");
		List<Rtip> listOfRtips = rtipConjunction.getRtips();
		for  (Rtip rtip : listOfRtips) {
			System.out.print(rtip.getOrigRtipText() + " ");
		}
		System.out.println("}");		
}
	
	public static void printGeneratedText (RtipDisjunction rtipDisjunction, int level) {
		System.out.println(indentSpaces(level) + "DISJUNCTION OF (");
		List<RtipConjunction> listOfRtipConjunctions = rtipDisjunction.getRtipConjunctions();
		for  (RtipConjunction rtipConjunction : listOfRtipConjunctions) {
			printGeneratedText(rtipConjunction, level+1);
		}
		System.out.println(indentSpaces(level) + ") END DISJUNCTION");		
	}

	public static void printGeneratedText (RtipConjunction rtipConjunction, int level) {
		System.out.print(indentSpaces(level) + "{ ");
		List<Rtip> listOfRtips = rtipConjunction.getRtips();
		for  (Rtip rtip : listOfRtips) {
			System.out.print(rtip.fromValuesToText() + " ");
		}
		System.out.println("}");		
}



	public static String indentSpaces(int level) {
		String indentString = "";
		for (int i = 0; i < level*2; i++) 
		   {indentString = indentString + " ";}
		return indentString;
	}

}
