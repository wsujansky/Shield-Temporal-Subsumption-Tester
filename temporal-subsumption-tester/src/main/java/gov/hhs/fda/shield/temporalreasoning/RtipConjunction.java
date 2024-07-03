package gov.hhs.fda.shield.temporalreasoning;

import java.util.ArrayList;
import java.util.List;

public class RtipConjunction  {
	public List<Rtip> rtips = new ArrayList<Rtip>();
	
	public List<Rtip> getRtips() {
		return rtips;
	}
	public void setRtips(List<Rtip> rtips) {
		this.rtips = rtips;
	}
	
	public void addRtip(Rtip rtip) {
		this.rtips.add(rtip);
	}

	public RtipConjunction() {
		// TODO Auto-generated constructor stub
	}
	
	public String toString () {
		return new String("I'm an RtipConjunction!");
	}
}
