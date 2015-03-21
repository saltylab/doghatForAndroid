package saltyLab.otosan;

import android.view.View;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.content.Context;

public class ButtonAction extends View implements
		MediaPlayer.OnCompletionListener {
	private MediaPlayer player;
	private Visualizer visualizer;
	private int musicResID;
	private int buttonID;
	private Visualizer.OnDataCaptureListener listner;

	public void setListner(Visualizer.OnDataCaptureListener _listner)
	{
		listner = _listner;
	}
	
	public ButtonAction(Context context, int _buttonID, int _musicResID) {
		super(context);
		buttonID = _buttonID;
		musicResID = _musicResID;
		player = null;
		visualizer = null;
	}

	public void onClick() {
		/* play music */
		player = MediaPlayer.create(getContext(), musicResID);
		player.seekTo(0);
		player.start();
		player.setOnCompletionListener(this);
		
		if (visualizer != null )
		{
			visualizer.release();	/** ��������J������*/
		}
		
		visualizer = new Visualizer(player.getAudioSessionId());
		visualizer.setEnabled(false);
		
		int captureSize = Visualizer.getCaptureSizeRange()[1];
		visualizer.setCaptureSize(captureSize);
		
		visualizer.setDataCaptureListener(
					listner
					, Visualizer.getMaxCaptureRate() / 2, // �L���v�`���[�f�[�^�̎擾���[�g�i�~���w���c�j
				false,// ���ꂪTrue����onWaveFormDataCapture�ɂƂ�ł���
				true);// ���ꂪTrue����onFftDataCapture�ɂƂ�ł���

		visualizer.setEnabled(true);

	}

	public void StopSound() {
		if (player == null) {
			return;
		}

		player.stop();
		player.setOnCompletionListener(null);
		player.release();
		player = null;
	}

	public void onDestroy()
	{
		if (player != null) {
			player.stop();
			player.setOnCompletionListener(null);
			player.release();
		}
		
		if (visualizer != null)
		{
			visualizer.setEnabled(false);
			visualizer.release();
		}
		
		visualizer = null;

		player = null;		
	}
	public void onPause()
	{
		/* �����Ƃ߂āAvisualizer���~�߂�*/
		if (player != null)	player.stop();
		if (visualizer !=null) visualizer.setEnabled(false);
	}
	
	public void onResume() {
		if (player != null)	player.start();		
		if (visualizer !=null) visualizer.setEnabled(true);
	}

	public boolean isEqual(int id) {
		if (buttonID == id) {
			return true;
		} else {
			return false;
		}
	}

	public int getButtonID() {
		return buttonID;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		StopSound();
		if (visualizer != null)	visualizer.setEnabled(false);
	}


}
