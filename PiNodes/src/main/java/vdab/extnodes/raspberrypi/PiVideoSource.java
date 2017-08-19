package vdab.extnodes.raspberrypi;

import com.lcrc.af.polledsource.PolledServiceSource;

public class PiVideoSource extends PolledServiceSource {

	// CONSTRUCTORS 
	public PiVideoSource(){	
		super(new PiVideoService());
	
	}
	
}
