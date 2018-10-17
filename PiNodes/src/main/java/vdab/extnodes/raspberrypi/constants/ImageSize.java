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
package vdab.extnodes.raspberrypi.constants;

import com.lcrc.af.datatypes.AFEnum;

public class ImageSize {
	public static final int XSMALL = 0;
	public static final int SMALL = 1;
	public static final int MEDIUM = 2;
	public static final int LARGE = 3;
	public static final int XLARGE = 4;

	private static AFEnum s_ImageSizeEnum = new AFEnum("ImageSize")
	.addEntry(XSMALL, "XSmall 216x162")
	.addEntry(SMALL, "Small 432x324")
	.addEntry(MEDIUM, "Medium 864x648")
	.addEntry(LARGE, "Large 1296x972")
	.addEntry(XLARGE,"Maximum");
	public static AFEnum getEnum(){
		return  s_ImageSizeEnum  ;
	}
}
