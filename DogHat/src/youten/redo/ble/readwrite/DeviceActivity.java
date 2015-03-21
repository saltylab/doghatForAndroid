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
package youten.redo.ble.readwrite;

import java.util.UUID;

import youten.redo.ble.util.BleUtil;
import youten.redo.ble.util.BleUuid;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import saltyLab.otosan.*;

/**
 * BLE繝�繝舌う繧ｹ縺ｸ縺ｮconnect繝ｻService
 * Discovery繧貞ｮ滓命縺励�，haracteristics縺ｮread/write繧偵ワ繝ｳ繝峨Μ繝ｳ繧ｰ縺吶ｋActivity
 */

public class DeviceActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "BLEDevice";

	static final int SOUND_VOLUME_THREASH = 5;

	public static final String EXTRA_BLUETOOTH_DEVICE = "BT_DEVICE";
	private BluetoothAdapter mBTAdapter;
	private BluetoothDevice mDevice;
	private BluetoothGatt mConnGatt;
	private int mStatus;
	private Visualizer.OnDataCaptureListener listener;

	private ButtonAction[] actions;
	private Button[] buttons;
	private View selectedView;
	private int selectedID;
	public int previousMagnitude;
	public static int counter;
	public static volatile byte rgb[];

	private final BluetoothGattCallback mGattcallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mStatus = newState;
				mConnGatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				mStatus = newState;
				runOnUiThread(new Runnable() {
					public void run() {
						for (int actionID = 0; actionID < ActionID.ACTION_NUM
								.getID(); ++actionID) {
							/* 全ボタンを隠す */
							buttons[actionID].setEnabled(false);
							/*
							 * mReadManufacturerNameButton.setEnabled(false);
							 * mReadSerialNumberButton.setEnabled(false);
							 * mWriteAlertLevelButton.setEnabled(false);
							 */
						}
					};
				});
			}
		};

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			for (BluetoothGattService service : gatt.getServices()) {
				if ((service == null) || (service.getUuid() == null)) {
					continue;
				}
				if (BleUuid.SERVICE_DEVICE_INFORMATION.equalsIgnoreCase(service
						.getUuid().toString())) {
					runOnUiThread(new Runnable() {
						public void run() {
						};
					});
				}
				if (BleUuid.SERVICE_IMMEDIATE_ALERT.equalsIgnoreCase(service
						.getUuid().toString())) {
					runOnUiThread(new Runnable() {
						public void run() {
						};
					});

				}

				if (BleUuid.HELLOW_SERVICE.equalsIgnoreCase(service.getUuid()
						.toString())) {
					runOnUiThread(new Runnable() {
						public void run() {
							for (int actionID = 0; actionID < ActionID.ACTION_NUM
									.getID(); ++actionID) {
								/* 全ボタンを有効化 */
								buttons[actionID].setEnabled(true);
							}

						};
					});

					/* とりあえず全てのボタンをタグ付け */
					for (int buttonID = 0; buttonID < ActionID.ACTION_NUM
							.getID(); ++buttonID) {
						buttons[buttonID]
								.setTag(service.getCharacteristic(UUID
										.fromString(BleUuid.UUID_HELLO_CHARACTERISTIC_CONFIG)));
					}
				}

				if (BleUuid.SERVICE_IMMEDIATE_ALERT.equalsIgnoreCase(service
						.getUuid().toString())) {
					runOnUiThread(new Runnable() {
						public void run() {
						};
					});
				}
			}

			runOnUiThread(new Runnable() {
				public void run() {
					setProgressBarIndeterminateVisibility(false);
				};
			});
		};

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				if (BleUuid.CHAR_MANUFACTURER_NAME_STRING
						.equalsIgnoreCase(characteristic.getUuid().toString())) {
					runOnUiThread(new Runnable() {
						public void run() {
							setProgressBarIndeterminateVisibility(false);
						};
					});
				} else if (BleUuid.CHAR_SERIAL_NUMBEAR_STRING
						.equalsIgnoreCase(characteristic.getUuid().toString())) {
					/*
					 * final String name = characteristic.getStringValue(0);
					 */
					runOnUiThread(new Runnable() {
						public void run() {
							/*
							 * mReadSerialNumberButton.setText(name);
							 */
							setProgressBarIndeterminateVisibility(false);
						};
					});
				}

			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {

			runOnUiThread(new Runnable() {
				public void run() {
					setProgressBarIndeterminateVisibility(false);
				};
			});
		};
	};

	void connectButtonAction(int actionID, int musicResID, int buttonID) {
		actions[actionID] = new ButtonAction(this, buttonID, musicResID);
		buttons[actionID] = (Button) findViewById(buttonID);
		buttons[actionID].setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_device);
		// state
		mStatus = BluetoothProfile.STATE_DISCONNECTED;
		actions = new ButtonAction[ActionID.ACTION_NUM.getID()];
		buttons = new Button[ActionID.ACTION_NUM.getID()];
		rgb = new byte[18];

		listener = new Visualizer.OnDataCaptureListener() {
			@Override
			// Wave形式のキャプチャーデータ
			public void onWaveFormDataCapture(Visualizer visualizer,
					byte[] bytes, int samplingRate) {
				// visualizer.updateVisualizer(bytes);
			}

			@Override
			// 高速フーリエ変換のキャプチャーデータ
			public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
					int samplingRate) {
				// bytesには実部と虚部の両方のデータが入る
				int magnitude;
				int real, imaginary;
				int maxMagnitude = 0;
				for (int i = 0; i < bytes.length / 2; i++) {
					real = bytes[2 * i];
					// imaginary = bytes[2*i+1];

					magnitude = real; /* NOT consider imaginary data. */
					// magnitude = real * real + imaginary * imaginary;
					if (magnitude > maxMagnitude) {
						maxMagnitude = magnitude;
					}
				}

				if ((Math.abs(maxMagnitude - previousMagnitude) > SOUND_VOLUME_THREASH)
						&& (maxMagnitude > previousMagnitude)) {
					/* 色を変化させる */
					counter++;
					int [] hsvRGB = new int[3];
					
					if (counter >= 8) counter = 0;
					hsvRGB = HSVtoRGB(((counter+1) * 255/8), 255, 255);	/* hsvからの変換 */
					
					for (int i = 0; i < 18 / 3; ++i) {
						rgb[0 + 3 * i] = (byte) hsvRGB[0];
						rgb[1 + 3 * i] = (byte) hsvRGB[1];
						rgb[2 + 3 * i] = (byte) hsvRGB[2];
					}
				} else {
				}
				BluetoothGattCharacteristic ch = (BluetoothGattCharacteristic) selectedView
						.getTag();
				ch.setValue(rgb);
				if (mConnGatt.writeCharacteristic(ch)) {
				}
				previousMagnitude = maxMagnitude;
			}
		};

		selectedView = null;
		// for (int i = 0; i < ActionID.ACTION_NUM.getID(); ++i)
		initButtonActions();
	}
	
	@Override
	protected void onPause()
	{	
		super.onPause();
		/* stop sound */
		for (int id = 0; id < ActionID.ACTION_NUM.getID(); ++id) {
		/* 音も止める */
			actions[id].onPause();
		}		
	}

	
	@Override
	protected void onResume() {
		super.onResume();

		init();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mConnGatt != null) {
			if ((mStatus != BluetoothProfile.STATE_DISCONNECTING)
					&& (mStatus != BluetoothProfile.STATE_DISCONNECTED)) {
				mConnGatt.disconnect();
			}
			mConnGatt.close();
			mConnGatt = null;
		}

		/* stop sound */
		for (int id = 0; id < ActionID.ACTION_NUM.getID(); ++id) {
			/* 音も止める */
			actions[id].onDestroy();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		for (int id = 0; id < ActionID.ACTION_NUM.getID(); ++id) {
			/* stop all action */
			actions[id].StopSound();

			/* set next action */
			if (actions[id].isEqual(v.getId())) {
				if ((v.getTag() != null)
						&& (v.getTag() instanceof BluetoothGattCharacteristic)) {
					/* 音をならす */
					selectedView = v;
					selectedID = id;
					actions[selectedID].setListner(listener);
					actions[selectedID].onClick();
				}
			}
		}
	}

	private void init() {
		// BLE check
		if (!BleUtil.isBLESupported(this)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		// BT check
		BluetoothManager manager = BleUtil.getManager(this);
		if (manager != null) {
			mBTAdapter = manager.getAdapter();
		}
		if (mBTAdapter == null) {
			Toast.makeText(this, R.string.bt_unavailable, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		// check BluetoothDevice
		if (mDevice == null) {
			mDevice = getBTDeviceExtra();
			if (mDevice == null) {
				finish();
				return;
			}
		}

		// button disable
		initButtonActions();
		for (int actionID = 0; actionID < ActionID.ACTION_NUM.getID(); ++actionID) {
			/* 全ボタンを隠す */
			buttons[actionID].setEnabled(false);
		}
		for (int actionID = 0; actionID < ActionID.ACTION_NUM.getID(); ++actionID) {
			/* 音止める */
			actions[actionID].StopSound();
		}

		// connect to Gatt
		if ((mConnGatt == null)
				&& (mStatus == BluetoothProfile.STATE_DISCONNECTED)) {
			// try to connect
			mConnGatt = mDevice.connectGatt(this, false, mGattcallback);
			mStatus = BluetoothProfile.STATE_CONNECTING;
		} else {
			if (mConnGatt != null) {
				// re-connect and re-discover Services
				mConnGatt.connect();
				mConnGatt.discoverServices();
			} else {
				Log.e(TAG, "state error");
				finish();
				return;
			}
		}
		setProgressBarIndeterminateVisibility(true);
	}

	private BluetoothDevice getBTDeviceExtra() {
		Intent intent = getIntent();
		if (intent == null) {
			return null;
		}

		Bundle extras = intent.getExtras();
		if (extras == null) {
			return null;
		}

		return extras.getParcelable(EXTRA_BLUETOOTH_DEVICE);
	}

	private void initButtonActions() {
		connectButtonAction(ActionID.BEKKEN.getID(), R.raw.m01, R.id.bekken);
		connectButtonAction(ActionID.AMBITIOUS.getID(), R.raw.m02,
				R.id.ambicious);

		connectButtonAction(ActionID.KATSU.getID(), R.raw.m03, R.id.katsu);
		connectButtonAction(ActionID.KITA.getID(), R.raw.m04, R.id.kita);

		connectButtonAction(ActionID.MITEIRUKA.getID(), R.raw.m05,
				R.id.miteiruka);
		connectButtonAction(ActionID.MADAHAYAI.getID(), R.raw.m06,
				R.id.madahayai);

		connectButtonAction(ActionID.RIYUHAARU.getID(), R.raw.m07,
				R.id.riyuhaaru);
		connectButtonAction(ActionID.SUMIMASEN.getID(), R.raw.m08,
				R.id.sumimasen);

		connectButtonAction(ActionID.YABAIYABAI.getID(), R.raw.m09,
				R.id.yabaiyabai);
		connectButtonAction(ActionID.KIMINOSHIRANAIMONOGATARI.getID(),
				R.raw.kiminoshiranai, R.id.kiminoshiranaimonogatari);

		connectButtonAction(ActionID.SECRETBASE.getID(), R.raw.secret_base,
				R.id.SecretBase);
		connectButtonAction(ActionID.HACKIINGTOTHEGATE.getID(), R.raw.h2g,
				R.id.HackingToTheGate);
	}
	
	
	
	private int[] HSVtoRGB(int h, int s, int v) {
		float f;
		int i, p, q, t;
		int[] rgb = new int[3];

		i = (int) Math.floor(h / 60.0f) % 6;
		f = (float) (h / 60.0f) - (float) Math.floor(h / 60.0f);
		p = (int) Math.round(v * (1.0f - (s / 255.0f)));
		q = (int) Math.round(v * (1.0f - (s / 255.0f) * f));
		t = (int) Math.round(v * (1.0f - (s / 255.0f) * (1.0f - f)));

		switch (i) {
		case 0:
			rgb[0] = v;
			rgb[1] = t;
			rgb[2] = p;
			break;
		case 1:
			rgb[0] = q;
			rgb[1] = v;
			rgb[2] = p;
			break;
		case 2:
			rgb[0] = p;
			rgb[1] = v;
			rgb[2] = t;
			break;
		case 3:
			rgb[0] = p;
			rgb[1] = q;
			rgb[2] = v;
			break;
		case 4:
			rgb[0] = t;
			rgb[1] = p;
			rgb[2] = v;
			break;
		case 5:
			rgb[0] = v;
			rgb[1] = p;
			rgb[2] = q;
			break;
		}

		return rgb;
	}
	
	
}
