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

import com.lcrc.af.AnalysisData;
import com.lcrc.af.AnalysisDataDef;
import com.lcrc.af.AnalysisEvent;
import com.lcrc.af.AnalysisTarget;
import com.lcrc.af.util.StringUtility;

public class SerialTarget extends AnalysisTarget {
	private String c_SerialDevice;
	private Boolean c_IncludeLabels = Boolean.FALSE;
	private SerialConnection c_SerialConnection = SerialConnection.getSampleConnection();

	public SerialTarget(){
		c_SerialConnection.addTarget(this);
	}
	public Boolean get_IncludeLabels(){
		return  c_IncludeLabels;
	}
	public void set_IncludeLabels(Boolean include){
		 c_IncludeLabels = include;
	}
	public String get_SerialDevice(){
		return c_SerialDevice;
	}
	public void set_SerialDevice(String device){
		c_SerialDevice = device;
		SerialConnection nextConnection = SerialConnection.getSerialConnection(c_SerialDevice);
		if (nextConnection != c_SerialConnection){
			if (this.isTraceLogging()){
				logTrace(">>>> Resetting Serial Connection to "+c_SerialDevice);
			}
			c_SerialConnection = nextConnection;
			c_SerialConnection.addTarget(this);
		}
	
	}
	public AnalysisDataDef def_SerialDevice(AnalysisDataDef theDataDef){
		ArrayList<String> l = new ArrayList<String>();
		
		for (String defDevice: SerialConnection.DEFAULT_DEVICES){
			if (!SerialConnection.hasAvailableDevice(defDevice)){
				if (!defDevice.equals(c_SerialDevice))
					l.add(defDevice);
			}
		}
		
		for (String con: SerialConnection.getAllSerialDevices()){
			if (!con.equals(c_SerialDevice))
				l.add(con);
		}

		theDataDef.setAllPickValues(l.toArray(new String[l.size()]));
		return theDataDef;
	}
	public String get_AllSerialDevices(){
		return StringUtility.getDelimitedStrings(SerialConnection.getAllSerialDevices(),",");	
	}
	public void _start() {
		super._start();	
		try {
			c_SerialConnection.init();
			c_SerialConnection.open();
		}
		catch (Exception e){
			setError("Unable to start serial conneciton e>"+e);
			_disable();
		}
		catch (Throwable e){
			setError("SERIOUS THROWABLE: Unable to start serial connecton e>"+e);
			_disable();
		}

		
	}
	public void _stop() {
		
		if (c_SerialConnection != null){
			c_SerialConnection.removeTarget(this);		
			if (c_SerialConnection.isUnattached())
				c_SerialConnection.close();
		}

		super._stop();
		
	}
	public synchronized void processEvent(AnalysisEvent ev){

		// Not sure how to handle a trigger.
		if (ev.isTriggerEvent())
			return;
		
		try {
			AnalysisData ad = getSelectedData(ev.getAnalysisData());
			if (ad.isSimple()){
				c_SerialConnection.writeln(this, buildData(ad));
			}
			else {
				AnalysisData[] ads = ad.getAllSimpleSubData();
				for (AnalysisData ad0: ads){
					c_SerialConnection.writeln(this, buildData(ad0));
				}
			}
		}
		catch (Exception e){
			setError("Failed writing to the Serial port e>"+e);
			_disable();
		}
	}
	private String buildData(AnalysisData ad){
		if (c_IncludeLabels.booleanValue()){
			StringBuilder sb = new StringBuilder();
			sb.append(ad.getLabel());
			sb.append(" : ");
			sb.append(ad.getDataAsString());
			return sb.toString();
		}
		return ad.getDataAsString();
	}

}
