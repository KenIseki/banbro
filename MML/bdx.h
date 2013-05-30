//------------------------------------------------------------------------------ Header.
//_バンブラDX用ヘッダ ver1.6
/*
  更新履歴
    ver    日付        履歴
    1.0    20110228    公開
    1.1    20110309    パンと音量の値をバンブラDXと同じ値に変更
                       ロックギターを2和音に変更
    1.2    20110424    演奏速度とテンポ変化を追加
                       ロックギターの楽器指定を削除
    1.3    20110502    音色調整を追加
    1.4    20110529    エンベロープの値のミスを修正
    1.5    20120607    CROSSエフェクトを削除
    1.6    20120731    マスターボリュームを追加
*/
//------------------------------------------------------------------------------
Int BBS_PlaySpeed = 100;  // 演奏速度(%) テンポが0以下にならないよう注意

Array BBS_PAN_MAP =
	(-63,-57,-51,-45,-38,-32,-26,-19,-13,-7,
	 0,7,13,19,26,32,38,45,51,57,63
	);
Array BBS_VOLUME_MAP = 
	(  0,  2,  3,  4,  6,  7,  8,  9, 11, 12,
	  13, 15, 16, 17, 18, 20, 21, 22, 24, 25,
	  26, 27, 29, 30, 31, 32, 34, 35, 36, 38,
	  39, 40, 41, 43, 44, 45, 47, 48, 49, 50,
	  52, 53, 54, 56, 57, 58, 59, 61, 62, 63,
	  64, 66, 67, 68, 70, 71, 72, 73, 75, 76,
	  77, 79, 80, 81, 82, 84, 85, 86, 88, 89,
	  90, 91, 93, 94, 95, 96, 98, 99,100,102,
	 103,104,105,107,108,109,111,112,113,114,
	 116,117,118,120,121,122,123,125,126,127
	);

#BBS_PAN = {IF(-10<=0#?1&&0#?1<=10){P(64+BBS_PAN_MAP(#?1+10))}};  // パンの設定(-10～10)
#BBS_PART_V = {IF(0<=0#?1&&0#?1<=99){V(BBS_VOLUME_MAP(#?1))}};  // パート音量(0～99)
#BBS_V = {IF(0<=0#?1&&0#?1<=99){EP(BBS_VOLUME_MAP(#?1))}};  // 音量変化(0～99)
#BBS_TEMPO = {Tempo=(#?1*BBS_PlaySpeed/100)};  // テンポ変化(30～300推奨)

//音色調整(音源によっては変わらない場合あり)
Array BBS_ATTACK_MAP = 
	( 10, 20, 30, 40, 50, 60, 70, 75, 80, 83,
	  86, 89, 91, 93, 94, 95, 96, 97, 98, 99
	);
Array BBS_DECAY_MAP = 
	(  1,  2,  3,  4,  5,  7,  9, 12, 15, 18,
	  20, 26, 30, 35, 40, 50, 60, 70, 80, 99
	);
Array BBS_SUSTAIN_MAP = 
	(  0,  5,  8, 11, 14, 18, 22, 26, 30, 35,
	  40, 45, 50, 55, 60, 65, 70, 80, 90, 99
	);
Array BBS_RELEASE_MAP = 
	( 10, 20, 25, 30, 35, 38, 41, 44, 47, 50,
	  55, 60, 65, 70, 75, 80, 85, 90, 95, 99
	);
Array BBS_HOLD_MAP = 
	(  0,  5, 10, 14, 17, 19, 21, 23, 25, 27,
	  29, 32, 35, 40, 45, 50, 55, 60, 70, 99
	);
Array BBS_DELAY_MAP = 
	(  0,  2,  4,  6,  8, 10, 12, 14, 16, 18,
	  20, 23, 26, 30, 35, 40, 50, 60, 70, 99
	);
Array BBS_SPEED_MAP = 
	( 10, 20, 30, 34, 37, 40, 42, 44, 46, 48,
	  50, 52, 54, 56, 58, 60, 65, 70, 80, 99
	);
Array BBS_DEPTH_MAP = 
	(  0,  5,  8, 10, 12, 14, 15, 16, 17, 18,
	  19, 20, 22, 24, 27, 30, 40, 50, 70, 99
	);
Int EFFECT_CROSS = 2;
Int EFFECT_ECHO = 3;
Int EFFECT_CHORUS = 4;

Function ClearTone() {
	y121,127
	h1
};

Function SetEnvelope(Int _A, Int _D, Int _S, Int _R) {
	Str _MML = {};
	_MML = _MML + {EGAttack(} + (127 - (127 * BBS_ATTACK_MAP(_A) / 99)) + {);};
	_MML = _MML + {EGDecay(} + (127 - (127 * BBS_DECAY_MAP(_D) / 99)) + {);};
	// sustainなし
	_MML = _MML + {EGRelease(} + (127 - (127 * BBS_RELEASE_MAP(_R) / 99)) + {);};
	_MML;
};

Function SetVibrato(Int _Hold, Int _Delay, Int _Depth, Int _Speed, Int _Shape) {
	// 他の波形の再現は大変なので全部正弦波
	IF(_Shape==0){
		M(0);
	}ELSE{
		Str _MML = {};
		_MML = _MML + {M.W(0,127,} + (!1 * BBS_HOLD_MAP(_Hold) / 99) + {);};
		_MML = _MML + {VibratoDelay(} + (127 * BBS_DELAY_MAP(_Delay) / 99) + {);};
		_MML = _MML + {VibratoDepth(} + (127 * BBS_DEPTH_MAP(_Depth) / 99) + {);};
		_MML = _MML + {VibratoRate(} + (127 * BBS_SPEED_MAP(_Speed) / 99) + {);};
		_MML;
	}
};

Function SetEffects(Int EffectType, Int EffectValue) {
	Str _MML = {};
	IF(EffectType==EFFECT_CROSS){
		// CROSS なし
	}
	IF(EffectType==EFFECT_ECHO){
		_MML = {REV(} + (127 * EffectValue / 20) + {);};
	}
	IF(EffectType==EFFECT_CHORUS){
		_MML = {CHO(} + (127 * EffectValue / 20) + {);};
	}
	_MML;
};

Function SetMasterVolume(Int Volume=100) {  // マスターボリューム
	Int _Vol = Volume;
	IF(_Vol<50){_Vol=50;}
	IF(_Vol>150){_Vol=150;}
	_Vol = 127 * _Vol / 150;
	Str _MML = {SysEx=$f0,$7f,$7f,$04,$01,$00,} + _Vol + {,$f7};
	_MML;
};

Function RockGuitar(Str MML_) {  // ロックギターっぽい音色で演奏する
	Int V_ = MML(v);
	// パワーコードで演奏する
	//（途中でヴェロシティ,キーの変更をする場合は現在の値を参照すること）
	Sub{Key(MML(Key)+7); r%2 v(V_*40/100) MML_;}
	Key(MML(Key)-7); v(V_*90/100) MML_;
};

//------------------------------------------------------------------------------
