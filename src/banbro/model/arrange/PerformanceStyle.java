package banbro.model.arrange;

import java.util.Arrays;

public class PerformanceStyle {
	public static final PerformanceStyle ORIGINAL = new PerformanceStyle("オリジナル", Original.class);
	public static final PerformanceStyle JAZZ = new PerformanceStyle("ジャズ", Jazz.class);
	public static final PerformanceStyle JAPANESE = new PerformanceStyle("和風", Japanese.class);
	public static final PerformanceStyle LULLABY = new PerformanceStyle("安眠", Lullaby.class);	
	public static final PerformanceStyle PIANO = new PerformanceStyle("ピアノ", Piano.class);
	public static final PerformanceStyle NES = new PerformanceStyle("ファミコン", NES.class);
	public static final PerformanceStyle CHURCH_MUSIC = new PerformanceStyle("教会", ChurchMusic.class);
	public static final PerformanceStyle TROPICAL = new PerformanceStyle("南国", Tropical.class);
	public static final PerformanceStyle HARD_ROCK = new PerformanceStyle("ハードロック", HardRock.class);
	public static final PerformanceStyle ORCHESTRA = new PerformanceStyle("オーケストラ", Orchestra.class);
	public static final PerformanceStyle HORROR = new PerformanceStyle("ホラー", Horror.class);
	public static final PerformanceStyle OKINAWA = new PerformanceStyle("沖縄", Okinawa.class);
	public static final PerformanceStyle NEW_AGE = new PerformanceStyle("New Age", NewAge.class);
	public static final PerformanceStyle ARABIAN = new PerformanceStyle("アラビア", Arabian.class);
	public static final PerformanceStyle NOBLE = new PerformanceStyle("貴族", Noble.class);
	public static final PerformanceStyle MUSIC_BOX = new PerformanceStyle("オルゴール", MusicBox.class);
	public static final PerformanceStyle REVERSE = new PerformanceStyle("逆再生", Reverse.class);
	public static final PerformanceStyle SHUFFLE = new PerformanceStyle("シャッフル", Shuffle.class);
	public static final PerformanceStyle BLUES = new PerformanceStyle("ブルース", Blues.class);
	public static final PerformanceStyle YONANUKI = new PerformanceStyle("ヨナ抜き", YonaNuki.class);
	public static final PerformanceStyle POWER_SAVING = new PerformanceStyle("省電力", PowerSaving.class);
	public static final PerformanceStyle KYOUNORAIBU = new PerformanceStyle("今日のライブ", Kyounoraibu.class);
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
	 * 新しい演奏イメージを作成する
	 * @param name 演奏イメージ名
	 * @param style アレンジクラス
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
