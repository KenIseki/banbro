package banbro.model.bdx;

public enum BDXInstrument {
	// ピアノ系
	PIANO(0x01, "ピアノ"),
	E_PIANO(0x02, "Ｅ・ピアノ"),
	HARPSICH_(0x83, "チェンバロ"),
	R_ORGAN(0x03, "ロックオルガン"),
	P_ORGAN(0x06, "パイプオルガン"),
	ACCORDION(0x84, "アコーディオン"),
	HARMONICA(0x1E, "ハーモニカ"),
	VIBRAPH_(0x21, "ビブラフォン"),
	MARIMBA(0x22, "マリンバ"),
	MUSIC_BOX(0x86, "オルゴール"),
	// ギター＆ベース
	S_GUITAR(0x89, "クラシックギター"), 
	F_GUITAR(0x07, "フォークギター"),
	E_GUITAR(0x08, "Ｅ・ギター"),
	C_GUITAR(0x8A, "クリーンギター"),
	O_GUITAR(0x8B, "ＯＤ・ギター"),
	D_GUITAR(0x09, "Ｄ・ギター"),
	R_GUITAR(0x0A, "ロックギター"),
	PICK_BASS(0x0B, "ピックベース"),
	SLAP_BASS(0x8C, "スラップベース"),
	SYN_BASS(0x0C, "シンセベース"),
	ACO_BASS(0x0D, "Ａ・ベース"),
	// シンセサイザー
	SYN_LEAD(0x04, "シンセリード"),
	SQ_LEAD(0x88, "スクエアリード"),
	SYN_BELL(0x05, "シンセベル"),
	NES(0x29, "ファミコン"),
	// ストリングス系
	VIOLIN(0x0F, "バイオリン"),
	DBL_BASS(0x10, "コントラバス"),
	STRINGS(0x0E, "ストリングス"),
	P_VIOLIN(0x12, "ピチカート"),
	CHORUS(0x25, "コーラス"),
	HARP(0x11, "ハープ"),
	TIMPANI(0x23, "ティンパニー"),
	// 金管楽器
	TRUMPET(0x1A, "トランペット"),
	TROMBONE(0x1B, "トロンボーン"),
	HORN(0x1C, "ホルン"),
	TUBA(0x1D, "チューバ"),
	BRASS(0x19, "ブラス"),
	M_TRUMPET(0x85, "Ｍｔトランペット"),
	// 木管楽器
	PICCOLO(0x13, "ピッコロ"),
	FLUTE(0x14, "フルート"),
	SOPR_SAX(0x17, "ソプラノサックス"),
	ALTO_SAX(0x18, "アルトサックス"),
	CLARINET(0x15, "クラリネット"),
	OBOE(0x16, "オーボエ"),
	// 民族楽器
	PAN_FLUTE(0x1F, "パンフルート"),
	OCARINA(0x20, "オカリナ"),
	SHAKUHA_(0x28, "シャクハチ"),
	SHAMISEN(0x26, "シャミセン"),
	KOTO(0x27, "コト"),
	BANJO(0x87, "バンジョー"),
	STEEL_DRM(0x24, "スチールドラム"),
	// ドラム
	R_DRUMS(0x2A, true, "ロックドラム"),
	E_DRUMS(0x2B, true, "Ｅ・ドラム"),
	S_DRUMS(0x2C, true, "シンセドラム"),
	PERC_SET(0x2D, true, "ガクダンセット"),
	BONGO_SET(0x2E, true, "ボンゴセット"),
	CONGA_SET(0x2F, true, "コンガセット"),
	JPN_PERC_(0x30, true, "ワダイコセット"),
	// 楽器なし
	NONE(0x00, "None");

	private int _num;
	private boolean _drums;
	private String _name;
	private BDXInstrument(int num, String name) {
		this(num, false, name);
	}
	private BDXInstrument(int num, boolean drums, String name) {
		_num = num;
		_drums = drums;
		_name = name;
	}
	public int getNum() {
		return _num;
	}
	public boolean isDrums() {
		return _drums;
	}
	public String getName() {
		return _name;
	}
	public static BDXInstrument valueOf(int value) {
		for (BDXInstrument inst : values()) {
			if (inst.getNum()==value) {
				return inst;
			}
		}
		return NONE;
	}

}
