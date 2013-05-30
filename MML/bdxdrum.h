//------------------------------------------------------------------------------ Header.
//_バンブラDXドラムパート用ヘッダ ver1.3
/*
  ※マクロを上書きしているため、ドラムパート以外のパートより後ろで読み込んでください。
*/
/*
  更新履歴
    ver    日付        履歴
    1.0    20110228    公開
    1.1    20110309    初期化処理をPrintDrumInfoから各ドラム関数に変更
    1.2    20110424    CUSTOM_DRUMS を追加
    1.3    20110506    各ボタンのデフォルトの値を変更
*/
//------------------------------------------------------------------------------
Int Combo;  // MAX COMBO を数える変数
Int PART_V;
Int V_CHANGE;
#BBS_PART_V = {PART_V=0#?1; V(127)};  // パート音量(0～99)
#BBS_V = {V_CHANGE=0#?1; EP(127);};  // 音量変化(0～99)
Array BBS_Drum_Button = ();
Int BBS_DRUM_SET = StandardSet;


// 各ボタンの音
// 似てない音もあるけど気にしない

Function R_DRUMS(Int TRACK_NUM=8) {  // ロックドラム
	CUSTOM_DRUMS(TRACK_NUM,35,40,37,54,42,45,41,48,46,57)
}

Function E_DRUMS(Int TRACK_NUM=8) {  // Ｅ・ドラム
	CUSTOM_DRUMS(TRACK_NUM,36,38,51,54,42,47,43,50,46,49)
}

Function S_DRUMS(Int TRACK_NUM=8) {  // シンセドラム
	CUSTOM_DRUMS(TRACK_NUM,32,40,30,54,44,47,43,50,46,57)
}

Function PERC_SET(Int TRACK_NUM=8) {  // ガクダンセット
	CUSTOM_DRUMS(TRACK_NUM,35,38,81,40,52,54,38,54,55,49)
}

Function BONGO_SET(Int TRACK_NUM=8) {  // ボンゴセット
	CUSTOM_DRUMS(TRACK_NUM,61,60,80,81,54,75,39,69,66,65)
}

Function CONGA_SET(Int TRACK_NUM=8) {  // コンガセット
	CUSTOM_DRUMS(TRACK_NUM,64,63,62,58,83,82,85,56,77,76)
}

Function JPN_PERC_(Int TRACK_NUM=8) {  // ワダイコセット
	CUSTOM_DRUMS(TRACK_NUM,54,86,87,52,75,60,67,66,77,76)
}

Function CUSTOM_DRUMS(Int TRACK_NUM=8, Int _B, Int _A, Int _Y, Int _X, Int _U, Int _S, Int _H, Int _M, Int _L, Int _R) {
	Track(TRACK_NUM) Channel(10) Time(1:1:0) l16
	@(BBS_DRUM_SET)
	BBS_Drum_Button = (_B, _A, _Y, _X, _U, _S, _H, _M, _L, _R);
	$B{Combo++; n(BBS_Drum_Button(0)),?,,(127*PART_V/99*V_CHANGE/99)}
	$A{Combo++; n(BBS_Drum_Button(1)),?,,(127*PART_V/99*V_CHANGE/99)}
	$Y{Combo++; n(BBS_Drum_Button(2)),?,,(127*PART_V/99*V_CHANGE/99)}
	$X{Combo++; n(BBS_Drum_Button(3)),?,,(127*PART_V/99*V_CHANGE/99)}
	$U{Combo++; n(BBS_Drum_Button(4)),?,,(127*PART_V/99*V_CHANGE/99)}
	$S{Combo++; n(BBS_Drum_Button(5)),?,,(127*PART_V/99*V_CHANGE/99)}
	$H{Combo++; n(BBS_Drum_Button(6)),?,,(127*PART_V/99*V_CHANGE/99)}
	$M{Combo++; n(BBS_Drum_Button(7)),?,,(127*PART_V/99*V_CHANGE/99)}
	$L{Combo++; n(BBS_Drum_Button(8)),?,,(127*PART_V/99*V_CHANGE/99)}
	$R{Combo++; n(BBS_Drum_Button(9)),?,,(127*PART_V/99*V_CHANGE/99)}
	Combo=0;
}

Function PrintDrumInfo() {  // ドラムの情報表示
	Print({"ドラム:"} + Combo + {"Combo"});
}
//------------------------------------------------------------------------------
