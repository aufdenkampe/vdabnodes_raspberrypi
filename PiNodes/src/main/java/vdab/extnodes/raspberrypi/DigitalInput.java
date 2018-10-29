/*LICENSE*
 * Copyright (C) 2013 - 2018 MJA Technology LLC 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
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
import com.pi4j.io.gpio.PinState;
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
			return;
		}
		else {
		// One value to add.
			c_cdb_InputPins.set(ports);
		}
		// if it is all, reset all the others.
		String allLabel = GPIOPins.getEnum().getLabel(GPIOPins.ALL);
		if (c_cdb_InputPins.isSet(allLabel)){
			c_cdb_InputPins.clear();
			c_cdb_InputPins.set(allLabel);			
		}

	}
	public AnalysisDataDef def_InputPins(AnalysisDataDef theDataDef){
		String[] portLabels = GPIOPins.getEnum().getAllLabels();

		ArrayList<String> l = new ArrayList<String>();
		if (!c_cdb_InputPins.isEmpty())
			l.add(SpecialText.CLEAR);
		
		String allLabel = GPIOPins.getEnum().getLabel(GPIOPins.ALL);
		if (!c_cdb_InputPins.isSet(allLabel)){
			for (String label: portLabels){
				if  (!c_cdb_InputPins.isSet(label))
					l.add(label);
			}
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
	private void initGPIO(){
		try {
			
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
			
			c_Gpio  = GpioFactory.getInstance();
		
		}
		catch (Exception e) {
			setError("Unable to initialize Gpio Factory or Pin Listener e>"+e);
		}	
	}
	public void _start(){
		super._start();
		_enable();
		// Initial GPIO first time.
		if (c_Gpio == null)
			initGPIO();
		
		try {

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
				logInfo("Added Pin Listener PIN="+c_InputPins[n].getPin().getAddress());
			}

		}
		catch (Exception e) {
			setError("Unable to initialize Digital inputs e>"+e);
			e.printStackTrace();
			_disable();
		}	
	}
	public void _stop(){
		resetGPIO();
		super._stop();
	}
	private void resetGPIO(){
		logInfo("Reseting the GPIO"); 
		try {
			for (GpioPinDigitalInput diPin: c_InputPins)
				diPin.setShutdownOptions(true, PinState.LOW);
			c_Gpio.shutdown();
			for (GpioPinDigitalInput diPin: c_InputPins)
				c_Gpio.unprovisionPin(diPin);
			c_LastVal_map.clear();
			c_InputPins = null;
			c_Pins = null;		
		}
		catch (Exception e){
			setError("Unable to reset the GPIO e>"+e); 
		}
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
