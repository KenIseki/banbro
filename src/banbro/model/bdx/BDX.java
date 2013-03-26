package banbro.model.bdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BDX {
	/** 1拍のStep数 */
	public static final int TIME_BASE = 12;
	/** 小節数の最大 */
	public static final int MAX_BARS = 120;

	private List<String> _title;
	private String _submitter;
	private String _comment;
	private BDXDate _createDate;
	private BDXDate _saveDate;
	private int _time;  // 3 or 4
	private int _bars;
	private int _melodyPartNum;
	private boolean _contributed;
	private boolean _received;
	private int _masterVolume;
	private boolean _lyrics;
	private long _serialId;
	private List<String> _lyric;
	private List<StepValue<String>> _lyricTiming;
	private List<Part> _parts;
	private List<StepValue<Integer>> _tempo;
	private List<StepValue<Integer>> _key;
	private Guitar _guitar;
	private Piano _piano;
	private List<StepValue<Chord>> _chordTiming;

	private List<StepValue<LyricLine>> _lyricLines;
	private final int _mejVer;

	public BDX() {
		this(2);  // 現在のバージョン番号
	}
	/**
	 * BDXMLを読み込む場合、このコンストラクタを使う。
	 * @param version メジャーバージョン番号
	 */
	public BDX(int version) {
		_mejVer = version;
		_title = new ArrayList<String>();
		_createDate = new BDXDate();
		_saveDate = new BDXDate();
		_time = 4;
		_bars = 1;
		_melodyPartNum = -1;
		_contributed = false;
		_received = false;
		_masterVolume = 100;
		_serialId = 0;
		_lyrics = false;
		_lyric = new ArrayList<String>();
		_lyricTiming = new ArrayList<StepValue<String>>();
		_parts = new ArrayList<Part>();
		_tempo = new ArrayList<StepValue<Integer>>();
		_key = new ArrayList<StepValue<Integer>>();
		_chordTiming = new ArrayList<StepValue<Chord>>();
	}

	public int getMejVer() {
		return _mejVer;
	}

	/**
	 * 曲名を追加する
	 * CDラベルに表示するため、1行ずつ追加する
	 * @param title 1行の文字列 nullも可
	 */
	public void addTitle(String title) {
		_title.add(title);
	}

	public void setSubmitter(String submitter) {
		_submitter = submitter;
	}

	public void setComment(String comment) {
		_comment = comment;
	}

	public void setCreateDate(BDXDate d) {
		_createDate = d;
	}

	public void setSaveDate(BDXDate d) {
		_saveDate = d;
	}

	/**
	 * @param t 3 or 4
	 */
	public void setTime(int t) {
		if (_time!=t) {
			_time = t;
		}
	}

	public void setBars(int n) {
		if (_bars!=n) {
			_bars = n;
		}
	}

	/**
	 * メロディパートの設定
	 * @param num パート番号
	 */
	public void setMelodyPartNum(int num) {
		_melodyPartNum = num;
	}

	public void setContributed(boolean contributed) {
		_contributed = contributed;
	}
	
	public void setReceived(boolean received) {
		_received = received;
	}

	public void setMasterVolume(int volume) {
		volume = Math.max(50, volume);
		volume = Math.min(150, volume);
		_masterVolume = volume;
	}

	/**
	 * @param lyrics 歌詞ありならtrue
	 */
	public void setLyrics(boolean lyrics) {
		_lyrics = lyrics;
	}

	public void setSerialId(long id) {
		_serialId = id;
	}

	/**
	 * 歌詞を一度に全部設定する
	 * @param lyric
	 */
	public void setLyric(List<String> lyric) {
		_lyric = lyric;
	}

	/**
	 * 歌詞を1行ずつ追加する
	 * @param lyric
	 */
	public void addLyric(String lyric) {
		_lyric.add(lyric);
	}

	public void setLyricTiming(List<StepValue<String>> timing) {
		_lyricTiming = timing;
	}

	public void addLyricTiming(int step, String str) {
		_lyricTiming.add(new StepValue<String>(step, str));
	}

	public void clearLyricTiming() {
		_lyricTiming.clear();
	}

	/**
	 * パート番号を指定してパートを置き換える
	 * 存在しないパート番号の場合は{@link Part#NULL_PART}を探してそのパート番号に置き換える
	 * それもない場合は最後に追加する
	 * @param num パート番号
	 * @param p
	 * @return 実際に置き換えられたパート番号
	 */
	public int setPart(int num, Part p) {
		if (0<=num && num<_parts.size()) {
			_parts.set(num, p);
			return num;
		}
		for (int i=0; i<_parts.size(); i++) {
			if (_parts.get(i).getType()==InstrumentType.NONE) {
				_parts.set(i, p);
				return i;
			}
		}
		num = _parts.size();
		addPart(p);
		return num;
	}

	public void addPart(Part p) {
		_parts.add(p);
	}

	public void setTempo(List<StepValue<Integer>> tempo) {
		_tempo = tempo;
	}

	public void addTempo(int step, int tempo) {
		_tempo.add(new StepValue<Integer>(step, tempo));
	}

	public void setKey(List<StepValue<Integer>> key) {
		_key = key;
	}

	public void addKey(int step, int key) {
		_key.add(new StepValue<Integer>(step, key));
	}

	public void setGuitar(Guitar guitar) {
		_guitar = guitar;
	}

	public void setPiano(Piano piano) {
		_piano = piano;
	}

	public void addChordTiming(int step, Chord chord) {
		_chordTiming.add(new StepValue<Chord>(step, chord));
	}

	public void clearChordTiming() {
		_chordTiming.clear();
	}

	/**
	 * 歌詞表示用
	 * @param lyricLines
	 */
	public void setLyricLines(List<StepValue<LyricLine>> lyricLines) {
		_lyricLines = lyricLines;
	}

	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		for (String st : _title) {
			if (st==null) {
				continue;
			}
			sb.append(st);
		}
		return sb.toString();
	}

	/**
	 * @return CDラベル用の曲名 nullを含む
	 */
	public List<String> getTitleLabels() {
		return _title;
	}

	public String getSubmitter() {
		return _submitter;
	}

	public String getComment() {
		return _comment;
	}

	public BDXDate getCreateDate() {
		return _createDate;
	}

	public BDXDate getSaveDate() {
		return _saveDate;
	}

	public int getTime() {
		return _time;
	}

	public int getTimeNum() {
		return _time*_bars;
	}

	public int getBars() {
		return _bars;
	}

	public int getMelodyPartNum() {
		return _melodyPartNum;
	}

	public boolean isContributed() {
		return _contributed;
	}
	
	public boolean isReceived() {
		return _received;
	}

	public int getMasterVolume() {
		return _masterVolume;
	}

	public boolean isLyrics() {
		return _lyrics;
	}

	public long getSerialId() {
		return _serialId;
	}

	public List<String> getLyric() {
		return _lyric;
	}

	public List<StepValue<String>> getLyricTiming() {
		return _lyricTiming;
	}

	/**
	 * 空白改行を削除して歌詞を抜き出す
	 * @param beginStep
	 * @param endStep
	 * @return
	 * @see #getLyric(int, int, boolean)
	 */
	public String getLyric(int beginStep, int endStep) {
		return getLyric(beginStep, endStep, true);
	}

	/**
	 * 歌詞を抜き出す
	 * @param beginStep 開始位置（含む）
	 * @param endStep 終了位置（含まない）
	 * @param cut 空白改行を削除するならtrue
	 * @return 抜き出した歌詞 無ければnull
	 */
	public String getLyric(int beginStep, int endStep, boolean cut) {
		if (beginStep>=endStep || _lyricTiming.isEmpty()) {
			return null;
		}
		if (_lyricTiming.get(0).getStep()>=endStep ||
				_lyricTiming.get(_lyricTiming.size()-1).getStep()<beginStep) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int from = BDXUtil.searchIndexWithStep(_lyricTiming, beginStep, false);
		if (from<0) {
			from = 0;
		}
		for (int i=from; i<_lyricTiming.size(); i++) {
			StepValue<String> sv = _lyricTiming.get(i);
			int step = sv.getStep();
			if (beginStep<=step) {
				if (step<endStep) {
					if (cut) {
						for (char c : sv.getValue().toCharArray()) {
							if (c=='\u0000'||c=='\n'||c==' '||c=='　') {
								continue;
							}
							sb.append(c);
						}
					} else {
						sb.append(sv.getValue());
					}
				} else {
					break;
				}
			}
		}
		if (sb.length()==0) {
			return null;
		}
		return sb.toString();
	}

	/**
	 * @param i パート番号
	 * @return パート 存在しないパート番号だった場合は{@link Part#NULL_PART}
	 */
	public Part getPart(int i) {
		if (0<=i && i<_parts.size()) {
			return _parts.get(i);
		}
		return Part.NULL_PART;
	}

	/**
	 * @return パートの数（{@link Part#NULL_PART}の数を含む）
	 */
	public int getPartNum() {
		return _parts.size();
	}

	public List<StepValue<Integer>> getTempo() {
		return _tempo;
	}

	public List<StepValue<Integer>> getKey() {
		return _key;
	}

	public int getKey(int step) {
		return BDXUtil.getValueWithStep(_key, step, true);
	}

	public Guitar getGuitar() {
		return _guitar;
	}

	public Piano getPiano() {
		return _piano;
	}

	public List<StepValue<Chord>> getChordTiming() {
		return _chordTiming;
	}

	/**
	 * 歌詞表示用
	 * @return
	 */
	public List<StepValue<LyricLine>> getLyricLines() {
		return _lyricLines;
	}

	/**
	 * 無駄な情報を削除する
	 */
	public void trim() {
		int maxStep = TIME_BASE * getTimeNum();
		for (int i=_tempo.size()-1; i>=0; i--) {
			StepValue<Integer> sv = _tempo.get(i);
			if (sv.getValue()<=0) {
				_tempo.remove(i);
			}
		}
		if (_tempo.isEmpty()) {
			_tempo.add(new StepValue<Integer>(0, 100));
		} else {
			Collections.sort(_tempo, StepValue.STEP_COMPARATOR);
			StepValue<Integer> sv = _tempo.get(0);
			sv.setStep(0);
			for (int i=_tempo.size()-1; i>0; i--) {
				StepValue<Integer> step = _tempo.get(i);
				if (step.getStep()>maxStep) {
					_tempo.remove(i);
				}
			}
		}
		if (_key.isEmpty()) {
			_key.add(new StepValue<Integer>(0, 0));
		} else {
			Collections.sort(_key, StepValue.STEP_COMPARATOR);
			StepValue<Integer> sv = _key.get(0);
			sv.setStep(0);
			for (int i=_key.size()-1; i>0; i--) {
				StepValue<Integer> step = _key.get(i);
				if (step.getStep()>maxStep) {
					_key.remove(i);
				} else {
					// 同じ値が連続しているなら削除
					StepValue<Integer> step2 = _key.get(i-1);
					if (step.getValue()==step2.getValue()) {
						_key.remove(i);
					}
				}
			}
		}
		if (!_lyricTiming.isEmpty()) {
			Collections.sort(_lyricTiming, StepValue.STEP_COMPARATOR);
			for (int i=_lyricTiming.size()-1; i>0; i--) {
				StepValue<String> step = _lyricTiming.get(i);
				if (step.getStep()>maxStep) {
					_lyricTiming.remove(i);
				}
			}
		}
		for (int i=0; i<_parts.size(); i++) {
			_parts.get(i).trim(maxStep);
		}
		calcCloneNum();
	}

	/**
	 * 同一楽器の番号を再計算する
	 */
	private void calcCloneNum() {
		int[] cloneNum = new int[getPartNum()];
		for (int i=0; i<cloneNum.length-1; i++) {
			if (cloneNum[i]!=0) {
				continue;
			}
			BDXInstrument inst = getPart(i).getInstrument();
			if (inst==BDXInstrument.NONE) {
				continue;
			}
			int num = 1;
			for (int j=i+1; j<getPartNum(); j++) {
				BDXInstrument inst2 = getPart(j).getInstrument();
				if (inst==inst2) {
					num++;
					cloneNum[j] = num;
				}
			}
			if (num!=1) {
				cloneNum[i] = 1;
			}
		}
		for (int i=0; i<cloneNum.length; i++) {
			Part p = getPart(i);
			if (p.getType()==InstrumentType.NONE) {
				continue;
			}
			p.setCloneNum(cloneNum[i]);
		}
	}

	public BDX clone() {
		BDX copy = new BDX(_mejVer);
		// title
		for (String st : _title) {
			copy.addTitle(st);
		}
		copy.setSubmitter(_submitter);
		copy.setComment(_comment);
		copy.setCreateDate(_createDate.clone());
		copy.setSaveDate(_saveDate.clone());
		copy.setTime(_time);
		copy.setBars(_bars);
		copy.setMelodyPartNum(_melodyPartNum);
		copy.setContributed(_contributed);
		copy.setReceived(_received);
		copy.setMasterVolume(_masterVolume);
		copy.setSerialId(_serialId);
		// lyric
		copy.setLyrics(_lyrics);
		List<String> lyric = new ArrayList<String>();
		for (String st : getLyric()) {
			lyric.add(st);
		}
		copy.setLyric(lyric);
		List<StepValue<String>> timing = new ArrayList<StepValue<String>>();
		for (StepValue<String> sv : getLyricTiming()) {
			timing.add(sv.clone());
		}
		copy.setLyricTiming(timing);
		// part
		for (int i=0; i<_parts.size(); i++) {
			copy.addPart(_parts.get(i).clone());
		}
		// tempo
		List<StepValue<Integer>> tempo = new ArrayList<StepValue<Integer>>();
		for (StepValue<Integer> sv : _tempo) {
			tempo.add(sv.clone());
		}
		copy.setTempo(tempo);
		// key
		List<StepValue<Integer>> key = new ArrayList<StepValue<Integer>>();
		for (StepValue<Integer> sv : _key) {
			key.add(sv.clone());
		}
		copy.setKey(key);
		// guitar
		if (_guitar!=null) {
			copy.setGuitar(_guitar.clone());
		}
		// piano
		if (_piano!=null) {
			copy.setPiano(_piano.clone());
		}
		// chord timing
		for (StepValue<Chord> sv : _chordTiming) {
			copy.addChordTiming(sv.getStep(), sv.getValue());
		}
		// lyric line
		if (_lyricLines!=null) {
			List<StepValue<LyricLine>> lines = new ArrayList<StepValue<LyricLine>>();
			for (StepValue<LyricLine> sv : _lyricLines) {
				lines.add(new StepValue<LyricLine>(sv.getStep(), sv.getValue().clone()));
			}
			copy.setLyricLines(lines);
		}
		return copy;
	}

	@Override
	public String toString() {
		return getTitle();
	}


}
