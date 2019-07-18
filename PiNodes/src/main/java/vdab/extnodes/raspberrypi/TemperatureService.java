package vdab.extnodes.raspberrypi;

import java.util.ArrayList;
import java.util.List;

import vdab.extnodes.raspberrypi.constants.TemperatureUnit;

import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.AnalysisService;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;

public class TemperatureService extends AnalysisService {
	static {
		TemperatureUnit.getEnum();
	}
	// PIN 4 is used by default for 1 Wire communication
	// Must enable 1 Wire with PI configuration
	// Multiple temp sensors can be used.
	// 4.7K pull up
	private static String DEFAULTLABEL = "Temperature";
	private List<TemperatureSensor> c_Sensors;
	private TemperatureSensor c_SelectedSensor;
	private String c_Device;
	private Integer c_TemperatureUnit = TemperatureUnit.C;
	
	public TemperatureService(){
		super();
		set_NewDataLabel(DEFAULTLABEL);
		try {
		W1Master master = new W1Master(); // Read sensors at startup
		c_Sensors = (List<TemperatureSensor>) master.getDevices(TemperatureSensor.class);
		}
		catch(Throwable e){
			setError("Unable to find PI temperature sensors, check sensor configuration e>"+e);
		}
	}	
	public void set_Device(String device){
		c_Device = device;

	}
	public String get_Device(){
		return c_Device;
	}
	
	
	public AnalysisDataDef def_Device(AnalysisDataDef theDataDef){
		ArrayList<String> l = new ArrayList<String>();
		for (TemperatureSensor sensor: c_Sensors)
			l.add(sensor.getName());
		theDataDef.setAllPickValues(l.toArray(new String[l.size()]));
		return theDataDef;
	}
	public Integer get_TemperatureUnit(){
		return c_TemperatureUnit;
	}
	public void set_TemperatureUnit(Integer unit){
		c_TemperatureUnit = unit;
	}
	public void _start(){
		super._start();
		if (c_Sensors == null){
			setError("Did not find Pi temperature sensors, check sensor configuration and restart VDAB");
			_disable();
		}
	}
	public synchronized void processEvent(AnalysisEvent ev){

		if (c_Device == null)
			return;

		if (c_SelectedSensor == null || !(c_SelectedSensor.getName().equals(c_Device))){
			for (TemperatureSensor sensor: c_Sensors){
				if (sensor.getName().equals(c_Device)) {
					c_SelectedSensor = sensor;
					break;
				}
			}
		}

		if (c_SelectedSensor == null)
			return;

		double temp = Double.MIN_VALUE;
		switch (c_TemperatureUnit.intValue()){
		
		case TemperatureUnit.F:
			temp =  c_SelectedSensor.getTemperature(TemperatureScale.FARENHEIT)	;		
			break;
					
		case TemperatureUnit.C:		
		default:
			temp =  c_SelectedSensor.getTemperature()	;		
			break;
		}
		if (temp == Double.MIN_VALUE){
			setError("Unable to return temperature from the selected sensor DEVICE="+c_Device);
			return;
		}
		String label = get_NewDataLabel();
		if (label == null)
			label = c_Device;
		publish(new AnalysisEvent(this, new AnalysisData(label, Double.valueOf(temp))));

	}

/**
	public static void main(String args[]) throws Exception { 
		//W1Master w1Master = new W1Master("C:\\work\\pi4j\\pi4j-device\\target\\test-classes\\w1\\sys\\bus\\w1\\devices"); 
		W1Master w1Master =new W1Master(); 

		System.out.println(w1Master); 

		for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) { 
			System.out.printf("%-20s %3.1f°C %3.1f°F\n", device.getName(), device.getTemperature(), 
					device.getTemperature(TemperatureScale.FARENHEIT)); 
		} 

		System.out.println("Exiting W1TempExample"); 
	} 
**/

}
