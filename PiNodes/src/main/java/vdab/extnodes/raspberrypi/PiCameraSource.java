package vdab.extnodes.raspberrypi;

import com.lcrc.af.polledsource.PolledServiceSource;

public class PiCameraSource extends PolledServiceSource {

	// CONSTRUCTORS 
	public PiCameraSource(){	
		super(new PiCameraService());
	
	}
	
}
