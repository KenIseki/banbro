package banbro.model.arrange;

import java.util.Arrays;

public class PerformanceStyle {
	public static final PerformanceStyle ORIGINAL = new PerformanceStyle("�I���W�i��", Original.class);
	public static final PerformanceStyle JAZZ = new PerformanceStyle("�W���Y", Jazz.class);
	public static final PerformanceStyle JAPANESE = new PerformanceStyle("�a��", Japanese.class);
	public static final PerformanceStyle LULLABY = new PerformanceStyle("����", Lullaby.class);	
	public static final PerformanceStyle PIANO = new PerformanceStyle("�s�A�m", Piano.class);
	public static final PerformanceStyle NES = new PerformanceStyle("�t�@�~�R��", NES.class);
	public static final PerformanceStyle CHURCH_MUSIC = new PerformanceStyle("����", ChurchMusic.class);
	public static final PerformanceStyle TROPICAL = new PerformanceStyle("�썑", Tropical.class);
	public static final PerformanceStyle HARD_ROCK = new PerformanceStyle("�n�[�h���b�N", HardRock.class);
	public static final PerformanceStyle ORCHESTRA = new PerformanceStyle("�I�[�P�X�g��", Orchestra.class);
	public static final PerformanceStyle HORROR = new PerformanceStyle("�z���[", Horror.class);
	public static final PerformanceStyle OKINAWA = new PerformanceStyle("����", Okinawa.class);
	public static final PerformanceStyle NEW_AGE = new PerformanceStyle("New Age", NewAge.class);
	public static final PerformanceStyle ARABIAN = new PerformanceStyle("�A���r�A", Arabian.class);
	public static final PerformanceStyle NOBLE = new PerformanceStyle("�M��", Noble.class);
	public static final PerformanceStyle MUSIC_BOX = new PerformanceStyle("�I���S�[��", MusicBox.class);
	public static final PerformanceStyle REVERSE = new PerformanceStyle("�t�Đ�", Reverse.class);
	public static final PerformanceStyle SHUFFLE = new PerformanceStyle("�V���b�t��", Shuffle.class);
	public static final PerformanceStyle BLUES = new PerformanceStyle("�u���[�X", Blues.class);
	public static final PerformanceStyle YONANUKI = new PerformanceStyle("���i����", YonaNuki.class);
	public static final PerformanceStyle POWER_SAVING = new PerformanceStyle("�ȓd��", PowerSaving.class);
	public static final PerformanceStyle KYOUNORAIBU = new PerformanceStyle("�����̃��C�u", Kyounoraibu.class);
	public static final PerformanceStyle TODAYS_GIG = new PerformanceStyle("Today's Gig", TodaysGig.class);
	private static final PerformanceStyle[] STYLES = {
		ORIGINAL,
		JAZZ,
		JAPANESE,
		LULLABY,
		PIANO,
		NES,
		CHURCH_MUSIC,
		TROPICAL,
		HARD_ROCK,
		ORCHESTRA,
		HORROR,
		OKINAWA,
		NEW_AGE,
		ARABIAN,
		NOBLE,
		MUSIC_BOX,
		REVERSE,
		SHUFFLE,
		BLUES,
		YONANUKI,
		POWER_SAVING,
		KYOUNORAIBU,
		TODAYS_GIG,
	};

	public static PerformanceStyle[] getStyles() {
		return Arrays.copyOf(STYLES, STYLES.length);
	}

	public static PerformanceStyle getInstance(String name) {
		for (PerformanceStyle style : STYLES) {
			if (style.getName().equals(name)) {
				return style;
			}
		}
		return null;
	}

	private String _name;
	private Class<? extends AbstractArrange> _styleClass;

	/**
	 * �V�������t�C���[�W���쐬����
	 * @param name ���t�C���[�W��
	 * @param style �A�����W�N���X
	 */
	public PerformanceStyle(String name, Class<? extends AbstractArrange> style) {
		_name = name;
		_styleClass = style;
	}

	public String getName() {
		return _name;
	}
	public Class<? extends AbstractArrange> getStyleClass() {
		return _styleClass;
	}

	@Override
	public String toString() {
		return getName();
	}

}
