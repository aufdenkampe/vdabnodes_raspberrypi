# Raspberry Pi Nodes
### Overview
This repository includes the source and libraries needed to build the libraries that work
with the raspberry Pi. 

| | |
|  --- |  :---: |
| Application Page    | [Raspberry Pi Apps](https://vdabtec.com/vdab/app-guides/raspberry-pi) |
| Demo Web Link   | [pi-demo.vdabsoft.com:31156](http://pi-demo.vdabsoft.com:31156/vdab) |

### Features
<ul>
<li>PI Camera and Video nodes can be included in flows.
<li>Digital input and output nodes support building an automation hub.
<li>Nodes to support other Wiring Pi and PI4J capabilities.
<li>Provides direct Android control of your PI projects.
</ul>

### Loading the the Package
The current or standard version can be loaded directly using the VDAB Android Client following the directions
for [Adding Packages](https://vdabtec.com/vdab/docs/VDABGUIDE_AddingPackages.pdf) and selecting the <i>PiNodes</i> package.
 
A custom version can be built using Gradle following the direction below.

* Clone or Download this project from Github.
* Open a command windows from the <i>PiNodes</i> directory.
* Build using Gradle: <pre>      gradle vdabPackage</pre>

This builds a package zip file which contains the components that need to be deployed. These can be deployed by 
manually unzipping these files as detailed in the [Server Updates](https://vdabtec.com/vdab/docs/VDABGUIDE_ServerUpdates.pdf) 
 documentation.

### Known Issues as of 24 Oct  2018

* The Container must be restarted whenever the <i>DigitalInSource</i> is edited and restarted.


