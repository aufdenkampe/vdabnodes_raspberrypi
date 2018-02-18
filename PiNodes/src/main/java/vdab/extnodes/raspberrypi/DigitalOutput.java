package vdab.extnodes.raspberrypi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.AnalysisObject;
import com.lcrc.af.constants.SpecialText;
import com.lcrc.af.AnalysisTarget;
import com.lcrc.af.util.ControlDataBuffer;
import com.lcrc.af.util.StringUtility;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class DigitalOutput extends AnalysisTarget {
	ControlDataBuffer c_cdb_OutputPins = new ControlDataBuffer("PiOutputPins");
	static { // Forces the initialization of the enum used here
		GpioOutputType.getEnum();
	}
	private Pin[] c_Pins;
	private GpioController c_Gpio;

	private HashMap<Integer, GpioPinDigitalOutput> c_OutputPin_map = new HashMap<Integer, GpioPinDigitalOutput>();
	private String c_PinPrefix = "IN_";
	private Integer c_OutputType = GpioOutputType.SETSTATE;
	
	public DigitalOutput() {
		super();
	
	}
	public Integer get_OutputType(){
		return c_OutputType;
	}
	public void set_OutputType(Integer type){
		c_OutputType = type;
	}
	public String get_PinPrefix(){
		return  c_PinPrefix;
	}
	public void set_PinPrefix(String label){
		c_PinPrefix = label;
	}
	public String get_OutputPins(){
		if(c_cdb_OutputPins.isEmpty())
			return null;
		return c_cdb_OutputPins.getAllSet(","); 
	}	
	public void set_OutputPins(String ports){
		// Multitple attributes, probably read from xml config
		if (ports.contains(",")){
			c_cdb_OutputPins.setAll(ports,","); 	
		} 
		// Clear command from option picker
		else if (ports.equals(SpecialText.CLEAR)){
			c_cdb_OutputPins.clear();
		}
		// One value to add.
		else {
			c_cdb_OutputPins.set(ports);
		}
	}
	public AnalysisDataDef def_OutputPins(AnalysisDataDef theDataDef){
		String[] portLabels = GPIOPins.getEnum().getAllLabels();

		ArrayList<String> l = new ArrayList<String>();
		if (!c_cdb_OutputPins.isEmpty())
			l.add(SpecialText.CLEAR);
		for (String label: portLabels){
			if  (!c_cdb_OutputPins.isSet(label))
				l.add(label);
		}
		theDataDef.setAllPickValues(l.toArray(new String[l.size()]));
		return theDataDef;
	}
	public void _start(){
		super._start();
		_enable();
		c_OutputPin_map.clear();
		try {
			c_Gpio  = GpioFactory.getInstance();
			c_Pins = GPIOUtility.createPinArray(c_cdb_OutputPins.getAllSet());
			if (c_Pins.length <= 0){
				setError("No digital output selected set at least one output pin");
				_disable();
				return;
			}

		
			for (int n=0; n < c_Pins.length; n++) {
				int no = c_Pins[n].getAddress();
				GpioPinDigitalOutput doPin = c_Gpio.provisionDigitalOutputPin(c_Pins[n], c_Pins[n].getName(), PinState.LOW);
				c_OutputPin_map.put(Integer.valueOf(no), doPin);
			}
		}
		catch (Exception e) {
			setError("Unable to initialize Gpio Factory e>"+e);
			_disable();
		}		
	}
	public void _stop(){
		Collection <GpioPinDigitalOutput> doPinC = c_OutputPin_map.values();
		
		for (GpioPinDigitalOutput doPin: doPinC)
			doPin.setShutdownOptions(true, PinState.LOW);
		
		logInfo("Preparing to shutdown the GPIO");
		c_Gpio.shutdown();
		super._stop();
	}
	private GpioPinDigitalOutput[] getOutputPins(String label){
		
		// If the input event is labeled with a number and it matches an output
		// number - fire only that one.
		Integer Id = StringUtility.getIntegerFromDigitsOnly(label);
		if (Id != null){
			GpioPinDigitalOutput doPin = c_OutputPin_map.get(Id);
			if (doPin != null) {
				AnalysisObject.logInfo("getOutputPins(","PIN="+doPin);
				return new GpioPinDigitalOutput[] {doPin};
			}
		}

		// If not an exact match do them all
		Collection <GpioPinDigitalOutput> doPinC = c_OutputPin_map.values();
		if (doPinC.size() > 0)
			return doPinC.toArray(new GpioPinDigitalOutput[doPinC.size()]);

		return null;
	}
	public synchronized void processEvent(AnalysisEvent ev){
		AnalysisData ad = ev.getAnalysisData();
		if (get_SelectedElement() != null)
			ad = ad.getElementByPath(get_SelectedElement()) ;
		logInfo("Processing event LABEL="+ad.getLabel()+" DATA="+ad.getDataAsString());
		GpioPinDigitalOutput[] doPins = getOutputPins(ad.getLabel());
		if (doPins == null){
			setError("No pins defined or matched for output");
			return;
		}
		switch (c_OutputType.intValue()){
		// For Boolean values set to that state.
		case GpioOutputType.SETSTATE:
		if (ad.isBoolean()){
			Boolean isOn = ad.getDataAsBoolean();
			if (isOn != null){
				if (isOn.booleanValue()){
					for (GpioPinDigitalOutput doPin: doPins)
						doPin.high();
				}
				else {
					for (GpioPinDigitalOutput doPin: doPins)
						doPin.low();
				}
			}
		}
		break;
		
		case GpioOutputType.TOGGLE:
			for (GpioPinDigitalOutput doPin: doPins)
				doPin.toggle();
		break;
		
		case GpioOutputType.BLINK:
			for (GpioPinDigitalOutput doPin: doPins)
				doPin.blink(100L,800L);	
		break;
		}
	}
}
