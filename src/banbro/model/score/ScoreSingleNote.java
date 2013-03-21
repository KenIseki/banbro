package banbro.model.score;

import java.util.ArrayList;
import java.util.List;

import banbro.model.Clef;

/**
 * ����1��
 */
public abstract class ScoreSingleNote implements Score, IScoreNote {

	private NoteType _type;
	private boolean _dot;  // �t�_
	private int _localCounter;
	private int _duration;
	private Clef _clef;  // �����L�� �\�����鍂���̌v�Z�Ɏg�p����

	// MusiXTeX�p�@�\�����鍂��
	private int _beamLine;
	private int _tupletLine;
	private int _lyricLine;
	private int _chordLine;

	private Tuplet _tuplet;  // �O�A��
	private String _lyric;  // �̎��i�����f�B�p�[�g�̂݁j
	private String _chord;  // �R�[�h�i�s�i�����f�B�p�[�g�̂݁j
	private List<Beam> _beams;  // �t���Ă���A�t
	private int _slope;  // �A�t�̌X��

	public ScoreSingleNote() {
		_beams = new ArrayList<Beam>();
	}

	public int getDuration() {
		return _duration;
	}

	public void setDuration(int duration) {
		_duration = duration;
	}

	public NoteType getType() {
		return _type;
	}

	public void setType(NoteType type) {
		_type = type;
	}

	public Tuplet getTuplet() {
		return _tuplet;
	}

	public void setTuplet(Tuplet tuplet) {
		_tuplet = tuplet;
	}

	public boolean isDot() {
		return _dot;
	}

	public void setDot(boolean dot) {
		_dot = dot;
	}

	public String getLyric() {
		return _lyric;
	}

	public void setLyric(String lyric) {
		_lyric = lyric;
	}

	public String getChord() {
		return _chord;
	}

	public void setChord(String chord) {
		_chord = chord;
	}

	public Clef getClef() {
		return _clef;
	}

	public void setClef(Clef clef) {
		_clef = clef;
	}

	public int getLocalCounter() {
		return _localCounter;
	}

	public void setLocalCounter(int counter) {
		_localCounter = counter;
	}

	public void setBeamLine(int line) {
		_beamLine = line;
	}
	public int getBeamLine() {
		return _beamLine;
	}
	public void setTupletLine(int line) {
		_tupletLine = line;
	}
	public int getTupletLine() {
		return _tupletLine;
	}
	public void setLyricLine(int line) {
		_lyricLine = line;
	}
	public int getLyricLine() {
		return _lyricLine;
	}
	public int getChordLine() {
		return _chordLine;
	}
	public void setChordLine(int line) {
		_chordLine = line;
	}
	public List<Beam> getBeam() {
		return _beams;
	}
	public void setBeams(List<Beam> beams) {
		_beams = beams;
	}
	public int getSlope() {
		return _slope;
	}
	public void setSlope(int slope) {
		_slope = slope;
	}

	/**
	 * @return �m�[�g�ԍ�
	 */
	public abstract int getNoteNum();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		String chord = getChord();
		if (chord!=null) {
			sb.append("Chord=" + chord + ", ");
		}
		String lyric = getLyric();
		if (lyric!=null) {
			sb.append("Lyric=" + lyric + ", ");
		}
		sb.append("NoteNum=" + getNoteNum() + ", ");
		sb.append("Duration=" + getDuration());
		sb.append("]");
		return sb.toString();
	}

}
