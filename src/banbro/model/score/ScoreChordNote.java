package banbro.model.score;

import banbro.model.Accidental;
import banbro.model.Clef;
import banbro.model.Pitch;
import banbro.model.Stroke;
import banbro.model.bdx.BDX;

/**
 * �R�[�h�p�̉���
 * ��Ɉ��̍����ɕ\�����A�R�[�h���ŉ����w�肷��
 */
public class ScoreChordNote extends ScorePitchNote {

	private Stroke _stroke;

	public ScoreChordNote() {
		super();
		_stroke = Stroke.NONE;
	}

	@Override
	public Clef getClef() {
		return Clef.G2;
	}
	@Override
	public Pitch getStep() {
		return Pitch.F;
	}
	@Override
	public int getAlter() {
		return 0;
	}
	@Override
	public int getOctave() {
		return 4;
	}
	@Override
	public Accidental getAccidental() {
		return Accidental.NONE;
	}
	@Override
	public int getSlope() {
		return 0;
	}

	@Override
	public void updateStem() {
		setStem(ScoreUtil.calcStem(this));
		// �R�[�h�\���ʒu�v�Z
		if (getLyric()!=null) {
			int lyricLine = 8;
			int step = getLocalCounter() % BDX.TIME_BASE;
			switch (step) {
			case 9:
				lyricLine += 3;
			case 6:
				lyricLine += 3;
			case 3:
				lyricLine += 3;
				break;
			case 8:
				lyricLine += 4;
			case 4:
				lyricLine += 4;
				break;
			case 0:
			default:
				break;
			}
			setLyricLine(lyricLine);
		}
	}

	/**
	 * �M�^�[�̂�
	 * @return
	 */
	public Stroke getStroke() {
		return _stroke;
	}

	/**
	 * �M�^�[�̂�
	 * @param stroke
	 */
	public void setStroke(Stroke stroke) {
		_stroke = stroke;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		String lyric = getLyric();
		if (lyric!=null) {
			sb.append("Chord=" + lyric + ", ");
		}
		if (_stroke!=Stroke.NONE) {
			sb.append("Stroke=" + _stroke + ", ");
		}
		sb.append("Duration=" + getDuration());
		sb.append("]");
		return sb.toString();
	}

}
