package banbro.util;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

/**
 * ���Ԃ������鏈�����v���O���X�o�[���o���Ȃ�����s����X���b�h
 */
public abstract class ProgressThread extends Thread {

	public class ProgressData {
		private int _max;
		private boolean _cancel;
		private int _now;
		private String _text;
		private ProgressData(int max) {
			_max = max;
			_now = 0;
			_cancel = false;
		}
		/**
		 * @return �L�����Z���{�^���������ꂽ�Ȃ�true
		 */
		public boolean isCancel() {
			return _cancel;
		}
		/**
		 * @return �o�[�̍ő�l
		 */
		public int getProgressMax() {
			return _max;
		}
		/**
		 * �o�[���w��̒l�ɂ���
		 * @param now
		 */
		public void setProgress(int now) {
			_now = trim(now);
		}
		/**
		 * �o�[��1�����i�߂�
		 */
		public void addProgress() {
			if (_now<_max) {
				_now++;
			}
		}
		/**
		 * �o�[���w��̒l�����i�߂�
		 * @param v
		 */
		public void addProgress(int v) {
			_now = trim(_now+v);
		}
		private int trim(int v) {
			v = Math.max(v, 0);
			v = Math.min(v, _max);
			return v;
		}
		/**
		 * @param text �o�[�ɕ\�����镶����
		 */
		public void setText(String text) {
			_text = text;
		}
	}
	
	private ProgressMonitor monitor;
	private Component _parent;
	private Object _message;
	private int _max;
	private ProgressData _data;

	public ProgressThread(Component parent, Object message, int max) {
		_parent = parent;
		_message = message;
		if (max<=0) {
			throw new IllegalArgumentException();
		}
		_max = max;
	}

	@Override
	public void run() {
		_data = new ProgressData(_max);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					startThread(_parent);
					monitor = new ProgressMonitor(_parent, _message, "", 0, _max);
				}
			});
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						while (monitor!=null) {
							updateProgress();
							sleep(100);
						}
					} catch (InterruptedException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
			doThread(_data);
		} catch (InterruptedException | InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						endThread(_parent);
						if (monitor!=null) {
							synchronized (monitor) {
								monitor.close();
								monitor = null;
							}
						}
					}
				});
			} catch (InterruptedException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �X���b�h�J�n
	 * @param parent
	 */
	protected void startThread(Component parent) {
		if (_parent!=null) {
			_parent.setEnabled(false);
		}
	}
	
	/**
	 * ���Ԃ������鏈��
	 * @param data �v���O���X�o�[�̃f�[�^ ���\�b�h���g���ăv���O���X�o�[�̏�Ԃ�������ύX�����肷��
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	protected abstract void doThread(ProgressData data) throws InterruptedException, InvocationTargetException;
	
	/**
	 * �X���b�h�I��
	 * @param parent
	 */
	protected void endThread(Component parent) {
		if (_parent!=null) {
			_parent.setEnabled(true);
		}
	}

	/**
	 * �v���O���X�o�[�̓��e���X�V����
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	protected final void updateProgress() throws InterruptedException, InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				if (monitor!=null) {
					synchronized (monitor) {
						_data._cancel = monitor.isCanceled();
						monitor.setProgress(_data._now);
						if (_data._text!=null) {
							monitor.setNote(_data._text);
						}
					}
				}
			}
		});
	}

}
