/*
 * Copyright (C) 2013 youten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package youten.redo.ble.util;

/** BLE UUID Strings */
public class BleUuid {
	// 180A Device Information
	public static final String SERVICE_DEVICE_INFORMATION = "0000180a-0000-1000-8000-00805f9b34fb";
	public static final String CHAR_MANUFACTURER_NAME_STRING = "00002a29-0000-1000-8000-00805f9b34fb";
	public static final String CHAR_MODEL_NUMBER_STRING = "00002a24-0000-1000-8000-00805f9b34fb";
	public static final String CHAR_SERIAL_NUMBEAR_STRING = "00002a25-0000-1000-8000-00805f9b34fb";

	// 1802 Immediate Alert
	public static final String SERVICE_IMMEDIATE_ALERT = "00001802-0000-1000-8000-00805f9b34fb";
	public static final String CHAR_ALERT_LEVEL = "00002a06-0000-1000-8000-00805f9b34fb";
	// StickNFind縺ｧ縺ｯCHAR_ALERT_LEVEL縺ｫ0x01繧淡rite縺吶ｋ縺ｨ蜈峨ｊ縲�0x02縺ｧ縺ｯ髻ｳ縺碁ｳｴ繧翫��0x03縺ｧ縺ｯ蜈峨▲縺ｦ魑ｴ繧九��

	// 180F Battery Service
	public static final String SERVICE_BATTERY_SERVICE = "0000180F-0000-1000-8000-00805f9b34fb";
	public static final String CHAR_BATTERY_LEVEL = "00002a19-0000-1000-8000-00805f9b34fb";

	// 180F Battery Service
//	public static final String HELLOW_SERVICE = "0000180F-0000-1000-8000-00805f9b34fb";
//	public static final String UUID_HELLO_CHARACTERISTIC_CONFIG = "00002a19-0000-1000-8000-00805f9b34fb";
	public static final String HELLOW_SERVICE = "1b7e8251-2877-41c3-b46e-cf057c562023";
	public static final String UUID_HELLO_CHARACTERISTIC_CONFIG = "5e9bf2a8-f93f-4481-a67e-3b2f4a07891a";	


}
