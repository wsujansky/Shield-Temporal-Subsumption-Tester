package gov.hhs.fda.shield.temporalreasoning;

import gov.hhs.fda.shield.temporalreasoning.TemporalRelationshipGrammarParser.Rtip_typeContext;

public class Rtip  {
	private String origRtipText;
	private RtipType rtipType;
	private Boolean lowerIntervalOpen;
	private Boolean upperIntervalOpen;
	private Boolean lowerValueInfinite = false;  // must initialize to false for listener to work
	private Boolean upperValueInfinite = false;  // must initialize to false for listener to work
	private Float lowerValueSeconds;
	private Float upperValueSeconds;
	
	// Constructors
	public Rtip() {  // Instantiates a default, unspecified Rtip
	}

	public Rtip(String text) {
		super();
		this.origRtipText = text;
	}
	
	public Rtip(RtipType type) {
		super();
		this.rtipType = type;
	}
	//


	public boolean isFullySpecified() {
		if (rtipType == null)
			return false;
		if (lowerIntervalOpen == null)
			return false;
		if (upperIntervalOpen == null)
			return false;
		if (lowerValueInfinite == null)
			return false;
		if (lowerValueInfinite == false && lowerValueSeconds == null)
			return false;
		if (lowerValueInfinite == true && lowerValueSeconds != null)
			return false;
		if (upperValueInfinite == null)
			return false;
		if (upperValueInfinite == false && upperValueSeconds == null)
			return false;
		if (upperValueInfinite == true && upperValueSeconds != null)
			return false;
		if (origRtipText == null)
			return false;
		return true;
	}

	
	public String getOrigRtipText() {
		return origRtipText;
	}

	public void setOrigRtipText(String origRtipText) {
		this.origRtipText = origRtipText;
	}

	public RtipType getRtipType() {
		return rtipType;
	}

	public void setRtipType(RtipType rtipType) {
		this.rtipType = rtipType;
	}

	public boolean isLowerIntervalOpen() {
		return lowerIntervalOpen;
	}

	public void setLowerIntervalOpen(boolean lowerIntervalOpen) {
		this.lowerIntervalOpen = lowerIntervalOpen;
	}

	public boolean isUpperIntervalOpen() {
		return upperIntervalOpen;
	}

	public void setUpperIntervalOpen(boolean upperIntervalOpen) {
		this.upperIntervalOpen = upperIntervalOpen;
	}

	public boolean isLowerValueInfinite() {
		return lowerValueInfinite;
	}

	public void setLowerValueInfinite(boolean lowerValueInfinite) {
		this.lowerValueInfinite = lowerValueInfinite;
	}

	public boolean isUpperValueInfinite() {
		return upperValueInfinite;
	}

	public void setUpperValueInfinite(boolean upperValueInfinite) {
		this.upperValueInfinite = upperValueInfinite;
	}

	public Float getLowerValueSeconds() {
		return lowerValueSeconds;
	}

	public void setLowerValueSeconds(Float lowerValueSeconds) {
		this.lowerValueSeconds = lowerValueSeconds;
	}

	public Float getUpperValueSeconds() {
		return upperValueSeconds;
	}

	public void setUpperValueSeconds(Float upperValueSeconds) {
		this.upperValueSeconds = upperValueSeconds;
	}
	
/**  These methods were removed -- they required default values for newly instantiated Rtips, which messed up the parsing/listening process
	public void setLowerValueFromRtip(Rtip sourceRtip) {
		this.setLowerIntervalOpen(sourceRtip.isLowerIntervalOpen());
		this.setLowerValueInfinite(sourceRtip.isLowerValueInfinite());
		this.setLowerValueSeconds(sourceRtip.getLowerValueSeconds());
		this.setOrigRtipText(this.fromValuesToText());
	}
	
	public void setLowerValueNegInf() {
		this.setLowerIntervalOpen(true);
		this.setLowerValueInfinite(true);
		this.setLowerValueSeconds(null);
		this.setOrigRtipText(this.fromValuesToText());
	}
	
	public void setUpperValueFromRtip(Rtip sourceRtip) {
		this.setUpperIntervalOpen(sourceRtip.isLowerIntervalOpen());
		this.setUpperValueInfinite(sourceRtip.isLowerValueInfinite());
		this.setUpperValueSeconds(sourceRtip.getLowerValueSeconds());
		this.setOrigRtipText(this.fromValuesToText());
	}
	
	public void setUpperValuePosInf() {
		this.setUpperIntervalOpen(true);
		this.setUpperValueInfinite(true);
		this.setUpperValueSeconds(null);
		this.setOrigRtipText(this.fromValuesToText());
	}
***/


	
	public String toString () {
		return new String("I'm an Rtip: " + origRtipText);
	}
	
	public String toString (int level) {
		return new String("I'm an Rtip: " + origRtipText);
	}
	
	public String fromValuesToText() {
		String rtipTypeFragment = null;
		String lowerDelimiterFragment = null;
		String lowerValueFragment = null;
		String upperValueFragment = null;
		String upperDelimiterFragment = null;

		switch (this.rtipType) {
		case BEG_BEG:
			rtipTypeFragment = new String("BB");
			break;
		case BEG_END:
			rtipTypeFragment = new String("BE");
			break;
		case END_BEG:
			rtipTypeFragment = new String("EB");
			break;
		case END_END:
			rtipTypeFragment = new String("EE");
			break;
		default:
		}
		if (this.isLowerIntervalOpen()) {
			lowerDelimiterFragment = new String("(");
		} else
			lowerDelimiterFragment = new String("[");
		if (this.isUpperIntervalOpen()) {
			upperDelimiterFragment = new String(")");
		} else
			upperDelimiterFragment = new String("]");
		if (this.isLowerValueInfinite()) {
			lowerValueFragment = new String("-INF");
		} else
			lowerValueFragment = lowerValueSeconds.toString() + "sc";
		if (this.isUpperValueInfinite()) {
			upperValueFragment = new String("+INF");
		} else
			upperValueFragment = upperValueSeconds.toString() + "sc";
		return rtipTypeFragment + lowerDelimiterFragment + lowerValueFragment + ", " + upperValueFragment
				+ upperDelimiterFragment;
	}
}

