//------------------------------------------------------------------------------ Header.
//_バンブラDXギターコード用ヘッダ ver1.4.1
/*
  ※bdxchord.hを読み込み、SetChordType(BBS_Guitar) と記述すると
    GT関数にコード名を指定するだけで使用できます。
*/
/*
  更新履歴
    ver    日付        履歴
    1.0    20110228    公開
    1.1    20110513    ストロークの速さを可変にした
    1.2    20110603    バンブラDXで入力できない基本コードとNCを追加
    1.3    20110828    全ての音階を1オクターブ下げた
    1.4    20120607    同じ音を重ねて鳴らさないようにした
    1.4.1  20120613    重複音を除く処理が足りなかったのを修正
*/
//------------------------------------------------------------------------------
Int DS = 1;  // ダウンストローク
Int US = -1;  // アップストローク

Function SetStroke(Int LEN=1){
	DS = LEN;
	US = -LEN;
}

/* Fn:n弦のフレット番号
 * Mn:n弦のミュート(0:ミュートしない 1:ミュートする)
 * ST:ストローク
 * LEN:長さ
 */
Function GuitarTab(Int F6, Int F5, Int F4, Int F3, Int F2, Int F1,
	Int M6, Int M5, Int M4, Int M3, Int M2, Int M1, Int ST, Str LEN){
	Str _CHORD = {};
	Array _hArray;  // 次の音に重ならないようにゲートタイムを減算
	IF (ST<0) {
		_CHORD = _CHORD + {t.N(-5*ST,-4*ST,-3*ST,-2*ST,-ST,0)};
		_hArray = (-5*ST+1,-4*ST+1,-3*ST+1,-2*ST+1,-ST+1,1);
	} ELSE {
		_CHORD = _CHORD + {t.N(0,ST,2*ST,3*ST,4*ST,5*ST)};
		_hArray = (1,ST+1,2*ST+1,3*ST+1,4*ST+1,5*ST+1);
	}
	// 重複している音を除く
	// ミュートしない方を優先する
	Array _NoteArray = (40+F6, 45+F5, 50+F4, 55+F3, 59+F2, 64+F1);
	IF(_NoteArray(0)==_NoteArray(1)){  IF(M6==0){_NoteArray(1)=-1;}ELSE{_NoteArray(0)=-1;}  }
	IF(_NoteArray(0)==_NoteArray(2)){  IF(M6==0){_NoteArray(2)=-1;}ELSE{_NoteArray(0)=-1;}  }
	IF(_NoteArray(0)==_NoteArray(3)){  IF(M6==0){_NoteArray(3)=-1;}ELSE{_NoteArray(0)=-1;}  }
	IF(_NoteArray(1)==_NoteArray(2)){  IF(M5==0){_NoteArray(2)=-1;}ELSE{_NoteArray(1)=-1;}  }
	IF(_NoteArray(1)==_NoteArray(3)){  IF(M5==0){_NoteArray(3)=-1;}ELSE{_NoteArray(1)=-1;}  }
	IF(_NoteArray(1)==_NoteArray(4)){  IF(M5==0){_NoteArray(4)=-1;}ELSE{_NoteArray(1)=-1;}  }
	IF(_NoteArray(2)==_NoteArray(3)){  IF(M4==0){_NoteArray(3)=-1;}ELSE{_NoteArray(2)=-1;}  }
	IF(_NoteArray(2)==_NoteArray(4)){  IF(M4==0){_NoteArray(4)=-1;}ELSE{_NoteArray(2)=-1;}  }
	IF(_NoteArray(2)==_NoteArray(5)){  IF(M4==0){_NoteArray(5)=-1;}ELSE{_NoteArray(2)=-1;}  }
	IF(_NoteArray(3)==_NoteArray(4)){  IF(M3==0){_NoteArray(4)=-1;}ELSE{_NoteArray(3)=-1;}  }
	IF(_NoteArray(3)==_NoteArray(5)){  IF(M3==0){_NoteArray(5)=-1;}ELSE{_NoteArray(3)=-1;}  }
	IF(_NoteArray(4)==_NoteArray(5)){  IF(M2==0){_NoteArray(5)=-1;}ELSE{_NoteArray(4)=-1;}  }
	Int _V = MML(v);
	Int _Q = MML(q);
	_CHORD = _CHORD + {v.N(_V/(M6+1),_V/(M5+1),_V/(M4+1),_V/(M3+1),_V/(M2+1),_V/(M1+1))};
	_CHORD = _CHORD + {q.N(_Q/(M6+1),_Q/(M5+1),_Q/(M4+1),_Q/(M3+1),_Q/(M2+1),_Q/(M1+1))};
	_CHORD = _CHORD + {'};
	FOR(Int I=0; I<6; I++){
		_CHORD = _CHORD + {h(} + _hArray(I) + {)n(} + _NoteArray(I) +{)};
	}
	_CHORD = _CHORD + {'};
	_CHORD = _CHORD + LEN;
	_CHORD;
	v(_V); q(_Q); t0 h1
}


Function G_NC(Int ST, Str LEN){Str _MML={r}+LEN; _MML;}

//_基本コード
Function G_C(Int ST, Str LEN){GuitarTab(0,3,2,0,1,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Cm(Int ST, Str LEN){GuitarTab(3,3,5,5,4,3, 0,0,0,0,0,0, ST, LEN)}
Function G_C7(Int ST, Str LEN){GuitarTab(0,3,2,3,1,0, 1,0,0,0,0,0, ST, LEN)}
Function G_CM7(Int ST, Str LEN){GuitarTab(0,3,2,0,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Cm7(Int ST, Str LEN){GuitarTab(3,3,5,3,4,3, 0,0,0,0,0,0, ST, LEN)}
Function G_Cdim(Int ST, Str LEN){GuitarTab(0,0,1,2,1,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Cm7b5(Int ST, Str LEN){GuitarTab(0,3,4,3,4,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Caug(Int ST, Str LEN){GuitarTab(0,3,2,1,1,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Csus4(Int ST, Str LEN){GuitarTab(3,3,5,5,6,3, 0,0,0,0,0,0, ST, LEN)}
Function G_C7sus4(Int ST, Str LEN){GuitarTab(3,3,5,3,6,3, 0,0,0,0,0,0, ST, LEN)}
Function G_C6(Int ST, Str LEN){GuitarTab(0,3,5,5,5,5, 1,0,0,0,0,0, ST, LEN)}
Function G_Cadd9(Int ST, Str LEN){GuitarTab(0,3,2,0,3,0, 0,0,0,0,0,0, ST, LEN)}

Function G_Cp(Int ST, Str LEN){GuitarTab(4,4,6,6,6,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Cpm(Int ST, Str LEN){GuitarTab(4,4,6,6,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Cp7(Int ST, Str LEN){GuitarTab(4,4,6,4,6,4, 0,0,0,0,0,0, ST, LEN)}
Function G_CpM7(Int ST, Str LEN){GuitarTab(4,4,6,5,6,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Cpm7(Int ST, Str LEN){GuitarTab(4,4,6,4,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Cpdim(Int ST, Str LEN){GuitarTab(0,0,2,3,2,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Cpm7b5(Int ST, Str LEN){GuitarTab(0,4,5,4,5,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Cpaug(Int ST, Str LEN){GuitarTab(0,4,3,2,2,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Cpsus4(Int ST, Str LEN){GuitarTab(4,4,6,6,7,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Cp7sus4(Int ST, Str LEN){GuitarTab(4,4,6,4,7,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Cp6(Int ST, Str LEN){GuitarTab(0,4,6,6,6,6, 1,0,0,0,0,0, ST, LEN)}
Function G_Cpadd9(Int ST, Str LEN){GuitarTab(4,4,6,6,4,4, 0,0,0,0,0,0, ST, LEN)}

Function G_Db(Int ST, Str LEN){GuitarTab(4,4,6,6,6,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Dbm(Int ST, Str LEN){GuitarTab(4,4,6,6,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Db7(Int ST, Str LEN){GuitarTab(4,4,6,4,6,4, 0,0,0,0,0,0, ST, LEN)}
Function G_DbM7(Int ST, Str LEN){GuitarTab(4,4,6,5,6,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Dbm7(Int ST, Str LEN){GuitarTab(4,4,6,4,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Dbdim(Int ST, Str LEN){GuitarTab(0,0,2,3,2,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Dbm7b5(Int ST, Str LEN){GuitarTab(0,4,5,4,5,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Dbaug(Int ST, Str LEN){GuitarTab(0,4,3,2,2,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Dbsus4(Int ST, Str LEN){GuitarTab(4,4,6,6,7,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Db7sus4(Int ST, Str LEN){GuitarTab(4,4,6,4,7,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Db6(Int ST, Str LEN){GuitarTab(0,4,6,6,6,6, 1,0,0,0,0,0, ST, LEN)}
Function G_Dbadd9(Int ST, Str LEN){GuitarTab(4,4,6,6,4,4, 0,0,0,0,0,0, ST, LEN)}

Function G_D(Int ST, Str LEN){GuitarTab(0,0,0,2,3,2, 1,0,0,0,0,0, ST, LEN)}
Function G_Dm(Int ST, Str LEN){GuitarTab(0,0,0,2,3,1, 1,0,0,0,0,0, ST, LEN)}
Function G_D7(Int ST, Str LEN){GuitarTab(0,0,0,2,1,2, 1,0,0,0,0,0, ST, LEN)}
Function G_DM7(Int ST, Str LEN){GuitarTab(0,0,0,2,2,2, 1,0,0,0,0,0, ST, LEN)}
Function G_Dm7(Int ST, Str LEN){GuitarTab(0,0,0,2,1,1, 1,0,0,0,0,0, ST, LEN)}
Function G_Ddim(Int ST, Str LEN){GuitarTab(0,0,0,1,0,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Dm7b5(Int ST, Str LEN){GuitarTab(0,0,0,1,1,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Daug(Int ST, Str LEN){GuitarTab(0,0,0,3,3,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Dsus4(Int ST, Str LEN){GuitarTab(0,0,0,2,3,3, 1,0,0,0,0,0, ST, LEN)}
Function G_D7sus4(Int ST, Str LEN){GuitarTab(0,0,0,2,1,3, 1,0,0,0,0,0, ST, LEN)}
Function G_D6(Int ST, Str LEN){GuitarTab(0,0,0,2,0,2, 1,0,0,0,0,0, ST, LEN)}
Function G_Dadd9(Int ST, Str LEN){GuitarTab(0,0,0,2,3,0, 1,0,0,0,0,0, ST, LEN)}

Function G_Dp(Int ST, Str LEN){GuitarTab(6,6,8,8,8,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Dpm(Int ST, Str LEN){GuitarTab(6,6,8,8,7,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Dp7(Int ST, Str LEN){GuitarTab(6,6,8,6,8,6, 0,0,0,0,0,0, ST, LEN)}
Function G_DpM7(Int ST, Str LEN){GuitarTab(6,6,8,7,8,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Dpm7(Int ST, Str LEN){GuitarTab(6,6,8,6,7,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Dpdim(Int ST, Str LEN){GuitarTab(0,0,1,2,1,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Dpm7b5(Int ST, Str LEN){GuitarTab(0,0,1,2,2,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Dpaug(Int ST, Str LEN){GuitarTab(0,6,5,4,4,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Dpsus4(Int ST, Str LEN){GuitarTab(6,6,8,8,9,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Dp7sus4(Int ST, Str LEN){GuitarTab(6,6,8,6,9,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Dp6(Int ST, Str LEN){GuitarTab(0,6,8,8,8,8, 1,0,0,0,0,0, ST, LEN)}
Function G_Dpadd9(Int ST, Str LEN){GuitarTab(6,6,8,8,6,6, 0,0,0,0,0,0, ST, LEN)}

Function G_Eb(Int ST, Str LEN){GuitarTab(6,6,8,8,8,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Ebm(Int ST, Str LEN){GuitarTab(6,6,8,8,7,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Eb7(Int ST, Str LEN){GuitarTab(6,6,8,6,8,6, 0,0,0,0,0,0, ST, LEN)}
Function G_EbM7(Int ST, Str LEN){GuitarTab(6,6,8,7,8,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Ebm7(Int ST, Str LEN){GuitarTab(6,6,8,6,7,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Ebdim(Int ST, Str LEN){GuitarTab(0,0,1,2,1,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Ebm7b5(Int ST, Str LEN){GuitarTab(0,0,1,2,2,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Ebaug(Int ST, Str LEN){GuitarTab(0,6,5,4,4,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Ebsus4(Int ST, Str LEN){GuitarTab(6,6,8,8,9,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Eb7sus4(Int ST, Str LEN){GuitarTab(6,6,8,6,9,6, 0,0,0,0,0,0, ST, LEN)}
Function G_Eb6(Int ST, Str LEN){GuitarTab(0,6,8,8,8,8, 1,0,0,0,0,0, ST, LEN)}
Function G_Ebadd9(Int ST, Str LEN){GuitarTab(6,6,8,8,6,6, 0,0,0,0,0,0, ST, LEN)}

Function G_E(Int ST, Str LEN){GuitarTab(0,2,2,1,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Em(Int ST, Str LEN){GuitarTab(0,2,2,0,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_E7(Int ST, Str LEN){GuitarTab(0,2,0,2,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_EM7(Int ST, Str LEN){GuitarTab(0,2,1,1,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Em7(Int ST, Str LEN){GuitarTab(0,2,0,0,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Edim(Int ST, Str LEN){GuitarTab(0,0,2,3,2,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Em7b5(Int ST, Str LEN){GuitarTab(0,0,2,3,3,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Eaug(Int ST, Str LEN){GuitarTab(0,0,2,1,1,0, 1,1,0,0,0,0, ST, LEN)}
Function G_Esus4(Int ST, Str LEN){GuitarTab(0,2,2,2,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_E7sus4(Int ST, Str LEN){GuitarTab(0,2,0,2,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_E6(Int ST, Str LEN){GuitarTab(0,2,2,1,2,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Eadd9(Int ST, Str LEN){GuitarTab(0,2,2,1,0,2, 0,0,0,0,0,0, ST, LEN)}

Function G_F(Int ST, Str LEN){GuitarTab(1,3,3,2,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Fm(Int ST, Str LEN){GuitarTab(1,3,3,1,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_F7(Int ST, Str LEN){GuitarTab(1,3,1,2,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_FM7(Int ST, Str LEN){GuitarTab(0,0,3,2,1,0, 1,1,0,0,0,0, ST, LEN)}
Function G_Fm7(Int ST, Str LEN){GuitarTab(1,3,1,1,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Fdim(Int ST, Str LEN){GuitarTab(0,0,0,1,0,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Fm7b5(Int ST, Str LEN){GuitarTab(0,0,3,4,4,4, 1,1,0,0,0,0, ST, LEN)}
Function G_Faug(Int ST, Str LEN){GuitarTab(0,0,3,2,2,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Fsus4(Int ST, Str LEN){GuitarTab(1,3,3,3,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_F7sus4(Int ST, Str LEN){GuitarTab(1,3,1,3,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_F6(Int ST, Str LEN){GuitarTab(1,3,0,2,3,0, 0,0,0,0,0,1, ST, LEN)}
Function G_Fadd9(Int ST, Str LEN){GuitarTab(0,0,3,2,1,3, 1,1,0,0,0,0, ST, LEN)}

Function G_Fp(Int ST, Str LEN){GuitarTab(2,4,4,3,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Fpm(Int ST, Str LEN){GuitarTab(2,4,4,2,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Fp7(Int ST, Str LEN){GuitarTab(2,4,2,3,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_FpM7(Int ST, Str LEN){GuitarTab(2,4,3,3,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Fpm7(Int ST, Str LEN){GuitarTab(4,4,6,4,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Fpdim(Int ST, Str LEN){GuitarTab(0,0,1,2,1,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Fpm7b5(Int ST, Str LEN){GuitarTab(2,0,2,2,1,0, 0,1,0,0,0,0, ST, LEN)}
Function G_Fpaug(Int ST, Str LEN){GuitarTab(0,0,4,3,3,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Fpsus4(Int ST, Str LEN){GuitarTab(2,4,4,4,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Fp7sus4(Int ST, Str LEN){GuitarTab(2,4,2,4,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Fp6(Int ST, Str LEN){GuitarTab(2,0,1,3,2,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Fpadd9(Int ST, Str LEN){GuitarTab(0,0,4,3,2,4, 1,1,0,0,0,0, ST, LEN)}

Function G_Gb(Int ST, Str LEN){GuitarTab(2,4,4,3,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Gbm(Int ST, Str LEN){GuitarTab(2,4,4,2,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Gb7(Int ST, Str LEN){GuitarTab(2,4,2,3,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_GbM7(Int ST, Str LEN){GuitarTab(2,4,3,3,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Gbm7(Int ST, Str LEN){GuitarTab(4,4,6,4,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gbdim(Int ST, Str LEN){GuitarTab(0,0,1,2,1,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Gbm7b5(Int ST, Str LEN){GuitarTab(2,0,2,2,1,0, 0,1,0,0,0,0, ST, LEN)}
Function G_Gbaug(Int ST, Str LEN){GuitarTab(0,0,4,3,3,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Gbsus4(Int ST, Str LEN){GuitarTab(2,4,4,4,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Gb7sus4(Int ST, Str LEN){GuitarTab(2,4,2,4,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Gb6(Int ST, Str LEN){GuitarTab(2,0,1,3,2,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Gbadd9(Int ST, Str LEN){GuitarTab(0,0,4,3,2,4, 1,1,0,0,0,0, ST, LEN)}

Function G_G(Int ST, Str LEN){GuitarTab(3,2,0,0,0,3, 0,0,0,0,0,0, ST, LEN)}
Function G_Gm(Int ST, Str LEN){GuitarTab(3,5,5,3,3,3, 0,0,0,0,0,0, ST, LEN)}
Function G_G7(Int ST, Str LEN){GuitarTab(3,2,0,0,0,1, 0,0,0,0,0,0, ST, LEN)}
Function G_GM7(Int ST, Str LEN){GuitarTab(3,2,0,0,0,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Gm7(Int ST, Str LEN){GuitarTab(3,5,3,3,3,3, 0,0,0,0,0,0, ST, LEN)}
Function G_Gdim(Int ST, Str LEN){GuitarTab(0,0,2,3,2,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Gm7b5(Int ST, Str LEN){GuitarTab(3,0,3,3,2,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Gaug(Int ST, Str LEN){GuitarTab(0,0,5,4,4,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Gsus4(Int ST, Str LEN){GuitarTab(3,5,5,5,3,3, 0,0,0,0,0,0, ST, LEN)}
Function G_G7sus4(Int ST, Str LEN){GuitarTab(3,5,3,5,3,3, 0,0,0,0,0,0, ST, LEN)}
Function G_G6(Int ST, Str LEN){GuitarTab(3,2,0,0,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Gadd9(Int ST, Str LEN){GuitarTab(0,0,5,4,3,5, 1,1,0,0,0,0, ST, LEN)}

Function G_Gp(Int ST, Str LEN){GuitarTab(4,6,6,5,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gpm(Int ST, Str LEN){GuitarTab(4,6,6,4,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gp7(Int ST, Str LEN){GuitarTab(4,6,4,5,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_GpM7(Int ST, Str LEN){GuitarTab(4,6,5,5,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gpm7(Int ST, Str LEN){GuitarTab(4,6,4,4,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gpdim(Int ST, Str LEN){GuitarTab(0,0,0,1,0,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Gpm7b5(Int ST, Str LEN){GuitarTab(4,0,4,4,3,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Gpaug(Int ST, Str LEN){GuitarTab(0,0,6,5,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gpsus4(Int ST, Str LEN){GuitarTab(4,6,6,6,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gp7sus4(Int ST, Str LEN){GuitarTab(4,6,4,6,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Gp6(Int ST, Str LEN){GuitarTab(4,0,3,5,4,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Gpadd9(Int ST, Str LEN){GuitarTab(0,0,6,5,4,6, 1,1,0,0,0,0, ST, LEN)}

Function G_Ab(Int ST, Str LEN){GuitarTab(4,6,6,5,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Abm(Int ST, Str LEN){GuitarTab(4,6,6,4,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Ab7(Int ST, Str LEN){GuitarTab(4,6,4,5,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_AbM7(Int ST, Str LEN){GuitarTab(4,6,5,5,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Abm7(Int ST, Str LEN){GuitarTab(4,6,4,4,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Abdim(Int ST, Str LEN){GuitarTab(0,0,0,1,0,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Abm7b5(Int ST, Str LEN){GuitarTab(4,0,4,4,3,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Abaug(Int ST, Str LEN){GuitarTab(0,0,6,5,5,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Absus4(Int ST, Str LEN){GuitarTab(4,6,6,6,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Ab7sus4(Int ST, Str LEN){GuitarTab(4,6,4,6,4,4, 0,0,0,0,0,0, ST, LEN)}
Function G_Ab6(Int ST, Str LEN){GuitarTab(4,0,3,5,4,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Abadd9(Int ST, Str LEN){GuitarTab(0,0,6,5,4,6, 1,1,0,0,0,0, ST, LEN)}

Function G_A(Int ST, Str LEN){GuitarTab(0,0,2,2,2,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Am(Int ST, Str LEN){GuitarTab(0,0,2,2,1,0, 0,0,0,0,0,0, ST, LEN)}
Function G_A7(Int ST, Str LEN){GuitarTab(0,0,2,0,2,0, 0,0,0,0,0,0, ST, LEN)}
Function G_AM7(Int ST, Str LEN){GuitarTab(0,0,2,1,2,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Am7(Int ST, Str LEN){GuitarTab(0,0,2,0,1,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Adim(Int ST, Str LEN){GuitarTab(0,0,1,2,1,2, 1,0,0,0,0,0, ST, LEN)}
Function G_Am7b5(Int ST, Str LEN){GuitarTab(5,0,5,5,4,0, 0,1,0,0,0,1, ST, LEN)}
Function G_Aaug(Int ST, Str LEN){GuitarTab(0,0,3,2,2,1, 1,0,0,0,0,0, ST, LEN)}
Function G_Asus4(Int ST, Str LEN){GuitarTab(0,0,2,2,3,0, 0,0,0,0,0,0, ST, LEN)}
Function G_A7sus4(Int ST, Str LEN){GuitarTab(0,0,2,0,3,0, 0,0,0,0,0,0, ST, LEN)}
Function G_A6(Int ST, Str LEN){GuitarTab(0,0,2,2,2,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Aadd9(Int ST, Str LEN){GuitarTab(0,0,2,2,0,0, 0,0,0,0,0,0, ST, LEN)}

Function G_Ap(Int ST, Str LEN){GuitarTab(1,1,3,3,3,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Apm(Int ST, Str LEN){GuitarTab(1,1,3,3,2,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Ap7(Int ST, Str LEN){GuitarTab(1,1,3,1,3,1, 0,0,0,0,0,0, ST, LEN)}
Function G_ApM7(Int ST, Str LEN){GuitarTab(1,1,3,2,3,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Apm7(Int ST, Str LEN){GuitarTab(1,1,3,1,2,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Apdim(Int ST, Str LEN){GuitarTab(0,0,2,3,2,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Apm7b5(Int ST, Str LEN){GuitarTab(0,1,2,1,2,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Apaug(Int ST, Str LEN){GuitarTab(0,0,4,3,3,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Apsus4(Int ST, Str LEN){GuitarTab(1,1,3,3,4,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Ap7sus4(Int ST, Str LEN){GuitarTab(1,1,3,1,4,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Ap6(Int ST, Str LEN){GuitarTab(0,1,3,3,3,3, 1,0,0,0,0,0, ST, LEN)}
Function G_Apadd9(Int ST, Str LEN){GuitarTab(1,1,3,3,1,1, 0,0,0,0,0,0, ST, LEN)}

Function G_Bb(Int ST, Str LEN){GuitarTab(1,1,3,3,3,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Bbm(Int ST, Str LEN){GuitarTab(1,1,3,3,2,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Bb7(Int ST, Str LEN){GuitarTab(1,1,3,1,3,1, 0,0,0,0,0,0, ST, LEN)}
Function G_BbM7(Int ST, Str LEN){GuitarTab(1,1,3,2,3,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Bbm7(Int ST, Str LEN){GuitarTab(1,1,3,1,2,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Bbdim(Int ST, Str LEN){GuitarTab(0,0,2,3,2,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Bbm7b5(Int ST, Str LEN){GuitarTab(0,1,2,1,2,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Bbaug(Int ST, Str LEN){GuitarTab(0,0,4,3,3,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Bbsus4(Int ST, Str LEN){GuitarTab(1,1,3,3,4,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Bb7sus4(Int ST, Str LEN){GuitarTab(1,1,3,1,4,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Bb6(Int ST, Str LEN){GuitarTab(0,1,3,3,3,3, 1,0,0,0,0,0, ST, LEN)}
Function G_Bbadd9(Int ST, Str LEN){GuitarTab(1,1,3,3,1,1, 0,0,0,0,0,0, ST, LEN)}

Function G_B(Int ST, Str LEN){GuitarTab(2,2,4,4,4,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Bm(Int ST, Str LEN){GuitarTab(2,2,4,4,3,2, 0,0,0,0,0,0, ST, LEN)}
Function G_B7(Int ST, Str LEN){GuitarTab(0,2,1,2,0,2, 1,0,0,0,0,0, ST, LEN)}
Function G_BM7(Int ST, Str LEN){GuitarTab(2,2,4,3,4,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Bm7(Int ST, Str LEN){GuitarTab(2,2,4,2,3,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Bdim(Int ST, Str LEN){GuitarTab(0,0,0,1,0,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Bm7b5(Int ST, Str LEN){GuitarTab(0,2,3,2,3,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Baug(Int ST, Str LEN){GuitarTab(0,0,5,4,4,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Bsus4(Int ST, Str LEN){GuitarTab(2,2,4,4,5,2, 0,0,0,0,0,0, ST, LEN)}
Function G_B7sus4(Int ST, Str LEN){GuitarTab(2,2,4,2,5,2, 0,0,0,0,0,0, ST, LEN)}
Function G_B6(Int ST, Str LEN){GuitarTab(0,2,4,4,4,4, 1,0,0,0,0,0, ST, LEN)}
Function G_Badd9(Int ST, Str LEN){GuitarTab(2,2,4,4,2,2, 0,0,0,0,0,0, ST, LEN)}

//_バンブラDXで入力できない基本コード
// Bp
Function G_Bp(Int ST, Str LEN){GuitarTab(0,3,2,0,1,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Bpm(Int ST, Str LEN){GuitarTab(3,3,5,5,4,3, 0,0,0,0,0,0, ST, LEN)}
Function G_Bp7(Int ST, Str LEN){GuitarTab(0,3,2,3,1,0, 1,0,0,0,0,0, ST, LEN)}
Function G_BpM7(Int ST, Str LEN){GuitarTab(0,3,2,0,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Bpm7(Int ST, Str LEN){GuitarTab(3,3,5,3,4,3, 0,0,0,0,0,0, ST, LEN)}
Function G_Bpdim(Int ST, Str LEN){GuitarTab(0,0,1,2,1,2, 1,1,0,0,0,0, ST, LEN)}
Function G_Bpm7b5(Int ST, Str LEN){GuitarTab(0,3,4,3,4,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Bpaug(Int ST, Str LEN){GuitarTab(0,3,2,1,1,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Bpsus4(Int ST, Str LEN){GuitarTab(3,3,5,5,6,3, 0,0,0,0,0,0, ST, LEN)}
Function G_Bp7sus4(Int ST, Str LEN){GuitarTab(3,3,5,3,6,3, 0,0,0,0,0,0, ST, LEN)}
Function G_Bp6(Int ST, Str LEN){GuitarTab(0,3,5,5,5,5, 1,0,0,0,0,0, ST, LEN)}
Function G_Bpadd9(Int ST, Str LEN){GuitarTab(0,3,2,0,3,0, 0,0,0,0,0,0, ST, LEN)}
// Fb
Function G_Fb(Int ST, Str LEN){GuitarTab(0,2,2,1,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Fbm(Int ST, Str LEN){GuitarTab(0,2,2,0,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Fb7(Int ST, Str LEN){GuitarTab(0,2,0,2,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_FbM7(Int ST, Str LEN){GuitarTab(0,2,1,1,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Fbm7(Int ST, Str LEN){GuitarTab(0,2,0,0,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Fbdim(Int ST, Str LEN){GuitarTab(0,0,2,3,2,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Fbm7b5(Int ST, Str LEN){GuitarTab(0,0,2,3,3,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Fbaug(Int ST, Str LEN){GuitarTab(0,0,2,1,1,0, 1,1,0,0,0,0, ST, LEN)}
Function G_Fbsus4(Int ST, Str LEN){GuitarTab(0,2,2,2,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Fb7sus4(Int ST, Str LEN){GuitarTab(0,2,0,2,0,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Fb6(Int ST, Str LEN){GuitarTab(0,2,2,1,2,0, 0,0,0,0,0,0, ST, LEN)}
Function G_Fbadd9(Int ST, Str LEN){GuitarTab(0,2,2,1,0,2, 0,0,0,0,0,0, ST, LEN)}
// Ep
Function G_Ep(Int ST, Str LEN){GuitarTab(1,3,3,2,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Epm(Int ST, Str LEN){GuitarTab(1,3,3,1,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Ep7(Int ST, Str LEN){GuitarTab(1,3,1,2,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_EpM7(Int ST, Str LEN){GuitarTab(0,0,3,2,1,0, 1,1,0,0,0,0, ST, LEN)}
Function G_Epm7(Int ST, Str LEN){GuitarTab(1,3,1,1,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Epdim(Int ST, Str LEN){GuitarTab(0,0,0,1,0,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Epm7b5(Int ST, Str LEN){GuitarTab(0,0,3,4,4,4, 1,1,0,0,0,0, ST, LEN)}
Function G_Epaug(Int ST, Str LEN){GuitarTab(0,0,3,2,2,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Epsus4(Int ST, Str LEN){GuitarTab(1,3,3,3,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Ep7sus4(Int ST, Str LEN){GuitarTab(1,3,1,3,1,1, 0,0,0,0,0,0, ST, LEN)}
Function G_Ep6(Int ST, Str LEN){GuitarTab(1,3,0,2,3,0, 0,0,0,0,0,1, ST, LEN)}
Function G_Epadd9(Int ST, Str LEN){GuitarTab(0,0,3,2,1,3, 1,1,0,0,0,0, ST, LEN)}
// Cb
Function G_Cb(Int ST, Str LEN){GuitarTab(2,2,4,4,4,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Cbm(Int ST, Str LEN){GuitarTab(2,2,4,4,3,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Cb7(Int ST, Str LEN){GuitarTab(0,2,1,2,0,2, 1,0,0,0,0,0, ST, LEN)}
Function G_CbM7(Int ST, Str LEN){GuitarTab(2,2,4,3,4,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Cbm7(Int ST, Str LEN){GuitarTab(2,2,4,2,3,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Cbdim(Int ST, Str LEN){GuitarTab(0,0,0,1,0,1, 1,1,0,0,0,0, ST, LEN)}
Function G_Cbm7b5(Int ST, Str LEN){GuitarTab(0,2,3,2,3,0, 1,0,0,0,0,1, ST, LEN)}
Function G_Cbaug(Int ST, Str LEN){GuitarTab(0,0,5,4,4,3, 1,1,0,0,0,0, ST, LEN)}
Function G_Cbsus4(Int ST, Str LEN){GuitarTab(2,2,4,4,5,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Cb7sus4(Int ST, Str LEN){GuitarTab(2,2,4,2,5,2, 0,0,0,0,0,0, ST, LEN)}
Function G_Cb6(Int ST, Str LEN){GuitarTab(0,2,4,4,4,4, 1,0,0,0,0,0, ST, LEN)}
Function G_Cbadd9(Int ST, Str LEN){GuitarTab(2,2,4,4,2,2, 0,0,0,0,0,0, ST, LEN)}

//------------------------------------------------------------------------------
