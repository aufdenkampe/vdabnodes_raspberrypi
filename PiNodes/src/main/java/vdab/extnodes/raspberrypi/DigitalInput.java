package vdab.extnodes.raspberrypi;

import java.util.ArrayList;
import java.util.HashMap;

import com.lcrc.af.AnalysisCompoundData;
import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.AnalysisObject;
import com.lcrc.af.constants.EventGroupType;
import com.lcrc.af.constants.SpecialText;
import com.lcrc.af.input.AnalysisInput;
import com.lcrc.af.util.ControlDataBuffer;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class DigitalInput extends AnalysisInput {
	static {
		EventGroupType.getEnum();	// Forces instatioation
	}
	
	ControlDataBuffer c_cdb_InputPins = new ControlDataBuffer("PiInputPins");
	private GpioController c_Gpio;
	private Pin[] c_Pins;
	private String c_PinPrefix = "IN_";
	private String c_GroupName = "Inputs";
	private Integer c_EventGrouping = EventGroupType.GROUPALL;
	private GpioPinDigitalInput[] c_InputPins;
	private GpioPinListenerDigital c_PinListener;
	private HashMap<String, Object> c_LastVal_map = new HashMap<String, Object>();
	public String get_PinPrefix(){
		return  c_PinPrefix;
	}
	public void set_PinPrefix(String label){
		 c_PinPrefix = label;
	}
	public String get_InputPins(){
		if(c_cdb_InputPins.isEmpty())
			return null;
		return c_cdb_InputPins.getAllSet(","); 
	}	
	public void set_InputPins(String ports){
		// Multitple attributes, probably read from xml config
		if (ports.contains(",")){
			 c_cdb_InputPins.setAll(ports,","); 	
		} 
		// Clear command from option picker
		else if (ports.equals(SpecialText.CLEAR)){
			c_cdb_InputPins.clear();
		}
		// One value to add.
		else {
			c_cdb_InputPins.set(ports);
		}
	}
	public AnalysisDataDef def_InputPins(AnalysisDataDef theDataDef){
		String[] portLabels = GPIOPins.getEnum().getAllLabels();

		ArrayList<String> l = new ArrayList<String>();
		if (!c_cdb_InputPins.isEmpty())
			l.add(SpecialText.CLEAR);
		for (String label: portLabels){
			if  (!c_cdb_InputPins.isSet(label))
				l.add(label);
		}
		theDataDef.setAllPickValues(l.toArray(new String[l.size()]));
		return theDataDef;
		}
	public Integer get_EventGrouping(){
		return c_EventGrouping;
	}
	public void set_EventGrouping(Integer grouping){
		c_EventGrouping = grouping;
	}
	public String get_GroupName(){
		return c_GroupName;
	}
	public void set_GroupName(String name){
		c_GroupName = name;
	}
	public void _start(){
		super._start();
		_enable();
		try {
			c_Gpio  = GpioFactory.getInstance();	
			// Create a pin listener
			c_PinListener = (new GpioPinListenerDigital() {
	            @Override
	            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
	            	GpioPin pin = event.getPin();
	            	int no = pin.getPin().getAddress();
	            	Boolean status = Boolean.valueOf(event.getState().isHigh());
	            	AnalysisObject.logInfo("DigitalInput.handler()","PIN="+no+" went STATE="+status);
	            	switch (c_EventGrouping.intValue()){
	            	case EventGroupType.INDIVIDUAL:
	            		publishIndividualEvent(no, status);
	            		break;

	            	case EventGroupType.GROUPALL:
	            		publishGroupEvent(no, status);
	            		break;
	            	}
	            }
	        });
			c_Pins = GPIOUtility.createPinArray(c_cdb_InputPins.getAllSet());
			if (c_Pins.length <= 0){
				setError("No digital inputs selected set at least one input pin");
				_disable();
				return;
			}
			c_InputPins = new GpioPinDigitalInput[c_Pins.length];
			for (int n = 0; n < c_InputPins.length;  n++){
				c_InputPins[n] = c_Gpio.provisionDigitalInputPin(c_Pins[n], PinPullResistance.PULL_DOWN);
				c_InputPins[n].addListener(c_PinListener);
				c_InputPins[n].setShutdownOptions(true);
			}
			logInfo("DIGITAL INPUT 2");
		}
		catch (Exception e) {
			setError("Unable to initialize Digital inputs e>"+e);
			e.printStackTrace();
			_disable();
		}	
	}
	public void _stop(){
		logInfo("Preparing to shutdown the GPIO");
		c_Gpio.shutdown();
	}
	private  String getPinLabel (int no){
		return c_PinPrefix+no;
	}
	private  synchronized void publishIndividualEvent(int pinNo, Boolean status){
        publish(new AnalysisEvent(this, new AnalysisData(getPinLabel(pinNo),status)));    
	}
	private  synchronized void publishGroupEvent(int pinNo, Boolean status){
		String pinLabel = getPinLabel(pinNo);
		c_LastVal_map.put(pinLabel, status);
		publish(new AnalysisEvent(this, new AnalysisCompoundData(c_GroupName, c_LastVal_map)));    
	}
}
