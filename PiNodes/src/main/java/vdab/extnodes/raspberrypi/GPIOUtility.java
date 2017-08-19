package vdab.extnodes.raspberrypi;

import java.util.ArrayList;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class GPIOUtility {
	public static Pin[] createPinArray(String[] pinLabels){
		ArrayList<Pin> l = new ArrayList<Pin>();
		 for (String pinLabel: pinLabels){
			int code = GPIOPins.getEnum().getCode(pinLabel);
			switch (code) {
			case GPIOPins.GPIO0:
				l.add(RaspiPin.GPIO_00);
				break;
			case GPIOPins.GPIO1:
				l.add(RaspiPin.GPIO_01);
				break;
			case GPIOPins.GPIO2:
				l.add(RaspiPin.GPIO_02);
				break;
			case GPIOPins.GPIO3:
				l.add(RaspiPin.GPIO_03);
				break;
			case GPIOPins.GPIO4:
				l.add(RaspiPin.GPIO_04);
				break;
			case GPIOPins.GPIO5:
				l.add(RaspiPin.GPIO_05);
				break;
			case GPIOPins.GPIO6:
				l.add( RaspiPin.GPIO_06);
				break;
			case GPIOPins.GPIO7:
				l.add( RaspiPin.GPIO_07);
				break;
			}		 
		 }
		return l.toArray(new Pin[l.size()]);
	}
}
