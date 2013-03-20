package banbro.model.bdx;

public class LyricNote {
	private String _lyric;
	private int _length;

	public LyricNote(String lyric) {
		this(lyric,BDX.TIME_BASE);
	}
	public LyricNote(String lyric, int length) {
		setLyric(lyric);
		setLength(length);
	}
	public void setLyric(String lyric) {
		if (lyric==null) {
			throw new IllegalArgumentException();
		}
		_lyric = lyric;
	}
	public void setLength(int length) {
		if (length<0) {
			_length = 0;
		} else {
			_length = length;
		}
	}
	public String getLyric() {
		return _lyric;
	}
	public int getLength() {
		return _length;
	}

	public LyricNote clone() {
		return new LyricNote(_lyric, _length);
	}

	@Override
	public String toString() {
		return "length=" + _length + ", lyric=" + getLyric();
	}

}
