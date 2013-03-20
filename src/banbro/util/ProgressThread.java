package banbro.util;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

/**
 * 時間がかかる処理をプログレスバーを出しながら実行するスレッド
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
		 * @return キャンセルボタンが押されたならtrue
		 */
		public boolean isCancel() {
			return _cancel;
		}
		/**
		 * @return バーの最大値
		 */
		public int getProgressMax() {
			return _max;
		}
		/**
		 * バーを指定の値にする
		 * @param now
		 */
		public void setProgress(int now) {
			_now = trim(now);
		}
		/**
		 * バーを1だけ進める
		 */
		public void addProgress() {
			if (_now<_max) {
				_now++;
			}
		}
		/**
		 * バーを指定の値だけ進める
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
		 * @param text バーに表示する文字列
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
	 * スレッド開始
	 * @param parent
	 */
	protected void startThread(Component parent) {
		if (_parent!=null) {
			_parent.setEnabled(false);
		}
	}
	
	/**
	 * 時間がかかる処理
	 * @param data プログレスバーのデータ メソッドを使ってプログレスバーの状態を見たり変更したりする
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	protected abstract void doThread(ProgressData data) throws InterruptedException, InvocationTargetException;
	
	/**
	 * スレッド終了
	 * @param parent
	 */
	protected void endThread(Component parent) {
		if (_parent!=null) {
			_parent.setEnabled(true);
		}
	}

	/**
	 * プログレスバーの内容を更新する
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
