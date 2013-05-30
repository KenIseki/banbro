//------------------------------------------------------------------------------ Header.
//_バンブラDXピアノコード用ヘッダ ver1.1
/*
  ※bdxchord.hを読み込み、SetChordType(BBS_Piano) と記述すると
    GT関数にコード名を指定するだけで使用できます。
*/
/*
  更新履歴
    ver    日付        履歴
    1.0    20110228    公開
    1.1    20110603    バンブラDXで入力できない基本コードとNCを追加
*/
//------------------------------------------------------------------------------
Int VoicingTopNote = 70;
Int VoicingNotes = 3;
Int VoicingSpread = 0;

/* 
 * TOPNOTE:最高音(48以上)
 * NOTES:音数(3 or 4)
 * SPREAD:広がり(0:密 1:中 2:粗)
 * ex) SetVoicing(NoteNo(o5a*+),3,0) // デフォルトの値
 */
Function SetVoicing(Int TOPNOTE=70, Int NOTES=3, Int SPREAD=0){
	VoicingTopNote = TOPNOTE;
	VoicingNotes = NOTES;
	VoicingSpread = SPREAD;
}

/* ピアノコードを鳴らす
 * ROOT:ルート音(Cが0で半音上がるにつれて+1,0～11の範囲)
 * N1,N2,N3:ルート音を0とした、音数3の構成音
 * N4:ルート音を0とした、音数4の4番目の構成音(0なら音数3とオクターブ違いの音)
 */
Function PianoChord(Int ROOT, Int N1, Int N2, Int N3, Int N4, Str LEN){
	Str _CHORD = {};
	Array NoteList = ();
	Int _Note;
	_Note = VoicingTopNote/12*12 + ROOT + N1;
	WHILE(_Note>VoicingTopNote){
		_Note -= 12;
	}
	NoteList = (NoteList,_Note);
	_Note = VoicingTopNote/12*12 + ROOT + N2;
	WHILE(_Note>VoicingTopNote){
		_Note -= 12;
	}
	NoteList = (NoteList,_Note);
	_Note = VoicingTopNote/12*12 + ROOT + N3;
	WHILE(_Note>VoicingTopNote){
		_Note -= 12;
	}
	NoteList = (NoteList,_Note);
	NoteList = ArraySortNum(NoteList);
	IF(VoicingNotes==3){  // 音数3
		IF(VoicingSpread==1){
			NoteList(1) = NoteList(1) - 12;
		}
		IF(VoicingSpread==2){
			NoteList(0) = NoteList(0) - 12;
			NoteList(1) = NoteList(1) - 24;
		}
	} ELSE {  // 音数4
		IF(N4>0){  // オクターブ違いの音なし
			_Note = VoicingTopNote/12*12 + ROOT + N4;
			WHILE(_Note>VoicingTopNote){
				_Note -= 12;
			}
			NoteList = (NoteList,_Note);
			NoteList = ArraySortNum(NoteList);
			IF(VoicingSpread==1){
				NoteList(2) = NoteList(2) - 12;
			}
			IF(VoicingSpread==2){
				NoteList(0) = NoteList(0) - 12;
				NoteList(2) = NoteList(2) - 12;
			}
		}ELSE{  // オクターブ違いの音あり
			IF(VoicingSpread==0){
				_Note = NoteList(2) - 12;
				NoteList = (NoteList,_Note);
			}
			IF(VoicingSpread==1){
				NoteList(1) = NoteList(1) - 12;
				_Note = NoteList(2) - 12;
				NoteList = (NoteList,_Note);
			}
			IF(VoicingSpread==2){
				NoteList(1) = NoteList(1) - 12;
				_Note = NoteList(2) - 24;
				NoteList = (NoteList,_Note);
			}
		}
	}
	NoteList = ArraySortNum(NoteList);
	_CHORD = _CHORD + {'};
	FOR(Int I=0; I<SizeOf(NoteList); I++){
			_CHORD = _CHORD + {n} + NoteList(I);
	}
	_CHORD = _CHORD + {'};
	_CHORD = _CHORD + LEN;
	_CHORD;
}


Function P_NC(Str LEN){Str _MML={r}+LEN; _MML;}

//_基本コード
Function P_C(Str LEN){PianoChord(0, 0,4,7,0, LEN)}
Function P_Cm(Str LEN){PianoChord(0, 0,3,7,0, LEN)}
Function P_C7(Str LEN){PianoChord(0, 0,4,10,7, LEN)}
Function P_CM7(Str LEN){PianoChord(0, 0,4,11,7, LEN)}
Function P_Cm7(Str LEN){PianoChord(0, 0,3,10,7, LEN)}
Function P_Cdim(Str LEN){PianoChord(0, 3,6,9,12, LEN)}
Function P_Cm7b5(Str LEN){PianoChord(0, 3,6,10,12, LEN)}
Function P_Caug(Str LEN){PianoChord(0, 0,4,8,0, LEN)}
Function P_Csus4(Str LEN){PianoChord(0, 0,5,7,0, LEN)}
Function P_C7sus4(Str LEN){PianoChord(0, 0,5,10,7, LEN)}
Function P_C6(Str LEN){PianoChord(0, 4,7,9,12, LEN)}
Function P_Cadd9(Str LEN){PianoChord(0, 4,7,14,12, LEN)}

Function P_Cp(Str LEN){PianoChord(1, 0,4,7,0, LEN)}
Function P_Cpm(Str LEN){PianoChord(1, 0,3,7,0, LEN)}
Function P_Cp7(Str LEN){PianoChord(1, 0,4,10,7, LEN)}
Function P_CpM7(Str LEN){PianoChord(1, 0,4,11,7, LEN)}
Function P_Cpm7(Str LEN){PianoChord(1, 0,3,10,7, LEN)}
Function P_Cpdim(Str LEN){PianoChord(1, 3,6,9,12, LEN)}
Function P_Cpm7b5(Str LEN){PianoChord(1, 3,6,10,12, LEN)}
Function P_Cpaug(Str LEN){PianoChord(1, 0,4,8,0, LEN)}
Function P_Cpsus4(Str LEN){PianoChord(1, 0,5,7,0, LEN)}
Function P_Cp7sus4(Str LEN){PianoChord(1, 0,5,10,7, LEN)}
Function P_Cp6(Str LEN){PianoChord(1, 4,7,9,12, LEN)}
Function P_Cpadd9(Str LEN){PianoChord(1, 4,7,14,12, LEN)}

Function P_Db(Str LEN){PianoChord(1, 0,4,7,0, LEN)}
Function P_Dbm(Str LEN){PianoChord(1, 0,3,7,0, LEN)}
Function P_Db7(Str LEN){PianoChord(1, 0,4,10,7, LEN)}
Function P_DbM7(Str LEN){PianoChord(1, 0,4,11,7, LEN)}
Function P_Dbm7(Str LEN){PianoChord(1, 0,3,10,7, LEN)}
Function P_Dbdim(Str LEN){PianoChord(1, 3,6,9,12, LEN)}
Function P_Dbm7b5(Str LEN){PianoChord(1, 3,6,10,12, LEN)}
Function P_Dbaug(Str LEN){PianoChord(1, 0,4,8,0, LEN)}
Function P_Dbsus4(Str LEN){PianoChord(1, 0,5,7,0, LEN)}
Function P_Db7sus4(Str LEN){PianoChord(1, 0,5,10,7, LEN)}
Function P_Db6(Str LEN){PianoChord(1, 4,7,9,12, LEN)}
Function P_Dbadd9(Str LEN){PianoChord(1, 4,7,14,12, LEN)}

Function P_D(Str LEN){PianoChord(2, 0,4,7,0, LEN)}
Function P_Dm(Str LEN){PianoChord(2, 0,3,7,0, LEN)}
Function P_D7(Str LEN){PianoChord(2, 0,4,10,7, LEN)}
Function P_DM7(Str LEN){PianoChord(2, 0,4,11,7, LEN)}
Function P_Dm7(Str LEN){PianoChord(2, 0,3,10,7, LEN)}
Function P_Ddim(Str LEN){PianoChord(2, 3,6,9,12, LEN)}
Function P_Dm7b5(Str LEN){PianoChord(2, 3,6,10,12, LEN)}
Function P_Daug(Str LEN){PianoChord(2, 0,4,8,0, LEN)}
Function P_Dsus4(Str LEN){PianoChord(2, 0,5,7,0, LEN)}
Function P_D7sus4(Str LEN){PianoChord(2, 0,5,10,7, LEN)}
Function P_D6(Str LEN){PianoChord(2, 4,7,9,12, LEN)}
Function P_Dadd9(Str LEN){PianoChord(2, 4,7,14,12, LEN)}

Function P_Dp(Str LEN){PianoChord(3, 0,4,7,0, LEN)}
Function P_Dpm(Str LEN){PianoChord(3, 0,3,7,0, LEN)}
Function P_Dp7(Str LEN){PianoChord(3, 0,4,10,7, LEN)}
Function P_DpM7(Str LEN){PianoChord(3, 0,4,11,7, LEN)}
Function P_Dpm7(Str LEN){PianoChord(3, 0,3,10,7, LEN)}
Function P_Dpdim(Str LEN){PianoChord(3, 3,6,9,12, LEN)}
Function P_Dpm7b5(Str LEN){PianoChord(3, 3,6,10,12, LEN)}
Function P_Dpaug(Str LEN){PianoChord(3, 0,4,8,0, LEN)}
Function P_Dpsus4(Str LEN){PianoChord(3, 0,5,7,0, LEN)}
Function P_Dp7sus4(Str LEN){PianoChord(3, 0,5,10,7, LEN)}
Function P_Dp6(Str LEN){PianoChord(3, 4,7,9,12, LEN)}
Function P_Dpadd9(Str LEN){PianoChord(3, 4,7,14,12, LEN)}

Function P_Eb(Str LEN){PianoChord(3, 0,4,7,0, LEN)}
Function P_Ebm(Str LEN){PianoChord(3, 0,3,7,0, LEN)}
Function P_Eb7(Str LEN){PianoChord(3, 0,4,10,7, LEN)}
Function P_EbM7(Str LEN){PianoChord(3, 0,4,11,7, LEN)}
Function P_Ebm7(Str LEN){PianoChord(3, 0,3,10,7, LEN)}
Function P_Ebdim(Str LEN){PianoChord(3, 3,6,9,12, LEN)}
Function P_Ebm7b5(Str LEN){PianoChord(3, 3,6,10,12, LEN)}
Function P_Ebaug(Str LEN){PianoChord(3, 0,4,8,0, LEN)}
Function P_Ebsus4(Str LEN){PianoChord(3, 0,5,7,0, LEN)}
Function P_Eb7sus4(Str LEN){PianoChord(3, 0,5,10,7, LEN)}
Function P_Eb6(Str LEN){PianoChord(3, 4,7,9,12, LEN)}
Function P_Ebadd9(Str LEN){PianoChord(3, 4,7,14,12, LEN)}

Function P_E(Str LEN){PianoChord(4, 0,4,7,0, LEN)}
Function P_Em(Str LEN){PianoChord(4, 0,3,7,0, LEN)}
Function P_E7(Str LEN){PianoChord(4, 0,4,10,7, LEN)}
Function P_EM7(Str LEN){PianoChord(4, 0,4,11,7, LEN)}
Function P_Em7(Str LEN){PianoChord(4, 0,3,10,7, LEN)}
Function P_Edim(Str LEN){PianoChord(4, 3,6,9,12, LEN)}
Function P_Em7b5(Str LEN){PianoChord(4, 3,6,10,12, LEN)}
Function P_Eaug(Str LEN){PianoChord(4, 0,4,8,0, LEN)}
Function P_Esus4(Str LEN){PianoChord(4, 0,5,7,0, LEN)}
Function P_E7sus4(Str LEN){PianoChord(4, 0,5,10,7, LEN)}
Function P_E6(Str LEN){PianoChord(4, 4,7,9,12, LEN)}
Function P_Eadd9(Str LEN){PianoChord(4, 4,7,14,12, LEN)}

Function P_F(Str LEN){PianoChord(5, 0,4,7,0, LEN)}
Function P_Fm(Str LEN){PianoChord(5, 0,3,7,0, LEN)}
Function P_F7(Str LEN){PianoChord(5, 0,4,10,7, LEN)}
Function P_FM7(Str LEN){PianoChord(5, 0,4,11,7, LEN)}
Function P_Fm7(Str LEN){PianoChord(5, 0,3,10,7, LEN)}
Function P_Fdim(Str LEN){PianoChord(5, 3,6,9,12, LEN)}
Function P_Fm7b5(Str LEN){PianoChord(5, 3,6,10,12, LEN)}
Function P_Faug(Str LEN){PianoChord(5, 0,4,8,0, LEN)}
Function P_Fsus4(Str LEN){PianoChord(5, 0,5,7,0, LEN)}
Function P_F7sus4(Str LEN){PianoChord(5, 0,5,10,7, LEN)}
Function P_F6(Str LEN){PianoChord(5, 4,7,9,12, LEN)}
Function P_Fadd9(Str LEN){PianoChord(5, 4,7,14,12, LEN)}

Function P_Fp(Str LEN){PianoChord(6, 0,4,7,0, LEN)}
Function P_Fpm(Str LEN){PianoChord(6, 0,3,7,0, LEN)}
Function P_Fp7(Str LEN){PianoChord(6, 0,4,10,7, LEN)}
Function P_FpM7(Str LEN){PianoChord(6, 0,4,11,7, LEN)}
Function P_Fpm7(Str LEN){PianoChord(6, 0,3,10,7, LEN)}
Function P_Fpdim(Str LEN){PianoChord(6, 3,6,9,12, LEN)}
Function P_Fpm7b5(Str LEN){PianoChord(6, 3,6,10,12, LEN)}
Function P_Fpaug(Str LEN){PianoChord(6, 0,4,8,0, LEN)}
Function P_Fpsus4(Str LEN){PianoChord(6, 0,5,7,0, LEN)}
Function P_Fp7sus4(Str LEN){PianoChord(6, 0,5,10,7, LEN)}
Function P_Fp6(Str LEN){PianoChord(6, 4,7,9,12, LEN)}
Function P_Fpadd9(Str LEN){PianoChord(6, 4,7,14,12, LEN)}

Function P_Gb(Str LEN){PianoChord(6, 0,4,7,0, LEN)}
Function P_Gbm(Str LEN){PianoChord(6, 0,3,7,0, LEN)}
Function P_Gb7(Str LEN){PianoChord(6, 0,4,10,7, LEN)}
Function P_GbM7(Str LEN){PianoChord(6, 0,4,11,7, LEN)}
Function P_Gbm7(Str LEN){PianoChord(6, 0,3,10,7, LEN)}
Function P_Gbdim(Str LEN){PianoChord(6, 3,6,9,12, LEN)}
Function P_Gbm7b5(Str LEN){PianoChord(6, 3,6,10,12, LEN)}
Function P_Gbaug(Str LEN){PianoChord(6, 0,4,8,0, LEN)}
Function P_Gbsus4(Str LEN){PianoChord(6, 0,5,7,0, LEN)}
Function P_Gb7sus4(Str LEN){PianoChord(6, 0,5,10,7, LEN)}
Function P_Gb6(Str LEN){PianoChord(6, 4,7,9,12, LEN)}
Function P_Gbadd9(Str LEN){PianoChord(6, 4,7,14,12, LEN)}

Function P_G(Str LEN){PianoChord(7, 0,4,7,0, LEN)}
Function P_Gm(Str LEN){PianoChord(7, 0,3,7,0, LEN)}
Function P_G7(Str LEN){PianoChord(7, 0,4,10,7, LEN)}
Function P_GM7(Str LEN){PianoChord(7, 0,4,11,7, LEN)}
Function P_Gm7(Str LEN){PianoChord(7, 0,3,10,7, LEN)}
Function P_Gdim(Str LEN){PianoChord(7, 3,6,9,12, LEN)}
Function P_Gm7b5(Str LEN){PianoChord(7, 3,6,10,12, LEN)}
Function P_Gaug(Str LEN){PianoChord(7, 0,4,8,0, LEN)}
Function P_Gsus4(Str LEN){PianoChord(7, 0,5,7,0, LEN)}
Function P_G7sus4(Str LEN){PianoChord(7, 0,5,10,7, LEN)}
Function P_G6(Str LEN){PianoChord(7, 4,7,9,12, LEN)}
Function P_Gadd9(Str LEN){PianoChord(7, 4,7,14,12, LEN)}

Function P_Gp(Str LEN){PianoChord(8, 0,4,7,0, LEN)}
Function P_Gpm(Str LEN){PianoChord(8, 0,3,7,0, LEN)}
Function P_Gp7(Str LEN){PianoChord(8, 0,4,10,7, LEN)}
Function P_GpM7(Str LEN){PianoChord(8, 0,4,11,7, LEN)}
Function P_Gpm7(Str LEN){PianoChord(8, 0,3,10,7, LEN)}
Function P_Gpdim(Str LEN){PianoChord(8, 3,6,9,12, LEN)}
Function P_Gpm7b5(Str LEN){PianoChord(8, 3,6,10,12, LEN)}
Function P_Gpaug(Str LEN){PianoChord(8, 0,4,8,0, LEN)}
Function P_Gpsus4(Str LEN){PianoChord(8, 0,5,7,0, LEN)}
Function P_Gp7sus4(Str LEN){PianoChord(8, 0,5,10,7, LEN)}
Function P_Gp6(Str LEN){PianoChord(8, 4,7,9,12, LEN)}
Function P_Gpadd9(Str LEN){PianoChord(8, 4,7,14,12, LEN)}

Function P_Ab(Str LEN){PianoChord(8, 0,4,7,0, LEN)}
Function P_Abm(Str LEN){PianoChord(8, 0,3,7,0, LEN)}
Function P_Ab7(Str LEN){PianoChord(8, 0,4,10,7, LEN)}
Function P_AbM7(Str LEN){PianoChord(8, 0,4,11,7, LEN)}
Function P_Abm7(Str LEN){PianoChord(8, 0,3,10,7, LEN)}
Function P_Abdim(Str LEN){PianoChord(8, 3,6,9,12, LEN)}
Function P_Abm7b5(Str LEN){PianoChord(8, 3,6,10,12, LEN)}
Function P_Abaug(Str LEN){PianoChord(8, 0,4,8,0, LEN)}
Function P_Absus4(Str LEN){PianoChord(8, 0,5,7,0, LEN)}
Function P_Ab7sus4(Str LEN){PianoChord(8, 0,5,10,7, LEN)}
Function P_Ab6(Str LEN){PianoChord(8, 4,7,9,12, LEN)}
Function P_Abadd9(Str LEN){PianoChord(8, 4,7,14,12, LEN)}

Function P_A(Str LEN){PianoChord(9, 0,4,7,0, LEN)}
Function P_Am(Str LEN){PianoChord(9, 0,3,7,0, LEN)}
Function P_A7(Str LEN){PianoChord(9, 0,4,10,7, LEN)}
Function P_AM7(Str LEN){PianoChord(9, 0,4,11,7, LEN)}
Function P_Am7(Str LEN){PianoChord(9, 0,3,10,7, LEN)}
Function P_Adim(Str LEN){PianoChord(9, 3,6,9,12, LEN)}
Function P_Am7b5(Str LEN){PianoChord(9, 3,6,10,12, LEN)}
Function P_Aaug(Str LEN){PianoChord(9, 0,4,8,0, LEN)}
Function P_Asus4(Str LEN){PianoChord(9, 0,5,7,0, LEN)}
Function P_A7sus4(Str LEN){PianoChord(9, 0,5,10,7, LEN)}
Function P_A6(Str LEN){PianoChord(9, 4,7,9,12, LEN)}
Function P_Aadd9(Str LEN){PianoChord(9, 4,7,14,12, LEN)}

Function P_Ap(Str LEN){PianoChord(10, 0,4,7,0, LEN)}
Function P_Apm(Str LEN){PianoChord(10, 0,3,7,0, LEN)}
Function P_Ap7(Str LEN){PianoChord(10, 0,4,10,7, LEN)}
Function P_ApM7(Str LEN){PianoChord(10, 0,4,11,7, LEN)}
Function P_Apm7(Str LEN){PianoChord(10, 0,3,10,7, LEN)}
Function P_Apdim(Str LEN){PianoChord(10, 3,6,9,12, LEN)}
Function P_Apm7b5(Str LEN){PianoChord(10, 3,6,10,12, LEN)}
Function P_Apaug(Str LEN){PianoChord(10, 0,4,8,0, LEN)}
Function P_Apsus4(Str LEN){PianoChord(10, 0,5,7,0, LEN)}
Function P_Ap7sus4(Str LEN){PianoChord(10, 0,5,10,7, LEN)}
Function P_Ap6(Str LEN){PianoChord(10, 4,7,9,12, LEN)}
Function P_Apadd9(Str LEN){PianoChord(10, 4,7,14,12, LEN)}

Function P_Bb(Str LEN){PianoChord(10, 0,4,7,0, LEN)}
Function P_Bbm(Str LEN){PianoChord(10, 0,3,7,0, LEN)}
Function P_Bb7(Str LEN){PianoChord(10, 0,4,10,7, LEN)}
Function P_BbM7(Str LEN){PianoChord(10, 0,4,11,7, LEN)}
Function P_Bbm7(Str LEN){PianoChord(10, 0,3,10,7, LEN)}
Function P_Bbdim(Str LEN){PianoChord(10, 3,6,9,12, LEN)}
Function P_Bbm7b5(Str LEN){PianoChord(10, 3,6,10,12, LEN)}
Function P_Bbaug(Str LEN){PianoChord(10, 0,4,8,0, LEN)}
Function P_Bbsus4(Str LEN){PianoChord(10, 0,5,7,0, LEN)}
Function P_Bb7sus4(Str LEN){PianoChord(10, 0,5,10,7, LEN)}
Function P_Bb6(Str LEN){PianoChord(10, 4,7,9,12, LEN)}
Function P_Bbadd9(Str LEN){PianoChord(10, 4,7,14,12, LEN)}

Function P_B(Str LEN){PianoChord(11, 0,4,7,0, LEN)}
Function P_Bm(Str LEN){PianoChord(11, 0,3,7,0, LEN)}
Function P_B7(Str LEN){PianoChord(11, 0,4,10,7, LEN)}
Function P_BM7(Str LEN){PianoChord(11, 0,4,11,7, LEN)}
Function P_Bm7(Str LEN){PianoChord(11, 0,3,10,7, LEN)}
Function P_Bdim(Str LEN){PianoChord(11, 3,6,9,12, LEN)}
Function P_Bm7b5(Str LEN){PianoChord(11, 3,6,10,12, LEN)}
Function P_Baug(Str LEN){PianoChord(11, 0,4,8,0, LEN)}
Function P_Bsus4(Str LEN){PianoChord(11, 0,5,7,0, LEN)}
Function P_B7sus4(Str LEN){PianoChord(11, 0,5,10,7, LEN)}
Function P_B6(Str LEN){PianoChord(11, 4,7,9,12, LEN)}
Function P_Badd9(Str LEN){PianoChord(11, 4,7,14,12, LEN)}

//_バンブラDXで入力できない基本コード
// Bp
Function P_Bp(Str LEN){PianoChord(0, 0,4,7,0, LEN)}
Function P_Bpm(Str LEN){PianoChord(0, 0,3,7,0, LEN)}
Function P_Bp7(Str LEN){PianoChord(0, 0,4,10,7, LEN)}
Function P_BpM7(Str LEN){PianoChord(0, 0,4,11,7, LEN)}
Function P_Bpm7(Str LEN){PianoChord(0, 0,3,10,7, LEN)}
Function P_Bpdim(Str LEN){PianoChord(0, 3,6,9,12, LEN)}
Function P_Bpm7b5(Str LEN){PianoChord(0, 3,6,10,12, LEN)}
Function P_Bpaug(Str LEN){PianoChord(0, 0,4,8,0, LEN)}
Function P_Bpsus4(Str LEN){PianoChord(0, 0,5,7,0, LEN)}
Function P_Bp7sus4(Str LEN){PianoChord(0, 0,5,10,7, LEN)}
Function P_Bp6(Str LEN){PianoChord(0, 4,7,9,12, LEN)}
Function P_Bpadd9(Str LEN){PianoChord(0, 4,7,14,12, LEN)}
// Fb
Function P_Fb(Str LEN){PianoChord(4, 0,4,7,0, LEN)}
Function P_Fbm(Str LEN){PianoChord(4, 0,3,7,0, LEN)}
Function P_Fb7(Str LEN){PianoChord(4, 0,4,10,7, LEN)}
Function P_FbM7(Str LEN){PianoChord(4, 0,4,11,7, LEN)}
Function P_Fbm7(Str LEN){PianoChord(4, 0,3,10,7, LEN)}
Function P_Fbdim(Str LEN){PianoChord(4, 3,6,9,12, LEN)}
Function P_Fbm7b5(Str LEN){PianoChord(4, 3,6,10,12, LEN)}
Function P_Fbaug(Str LEN){PianoChord(4, 0,4,8,0, LEN)}
Function P_Fbsus4(Str LEN){PianoChord(4, 0,5,7,0, LEN)}
Function P_Fb7sus4(Str LEN){PianoChord(4, 0,5,10,7, LEN)}
Function P_Fb6(Str LEN){PianoChord(4, 4,7,9,12, LEN)}
Function P_Fbadd9(Str LEN){PianoChord(4, 4,7,14,12, LEN)}
// Ep
Function P_Ep(Str LEN){PianoChord(5, 0,4,7,0, LEN)}
Function P_Epm(Str LEN){PianoChord(5, 0,3,7,0, LEN)}
Function P_Ep7(Str LEN){PianoChord(5, 0,4,10,7, LEN)}
Function P_EpM7(Str LEN){PianoChord(5, 0,4,11,7, LEN)}
Function P_Epm7(Str LEN){PianoChord(5, 0,3,10,7, LEN)}
Function P_Epdim(Str LEN){PianoChord(5, 3,6,9,12, LEN)}
Function P_Epm7b5(Str LEN){PianoChord(5, 3,6,10,12, LEN)}
Function P_Epaug(Str LEN){PianoChord(5, 0,4,8,0, LEN)}
Function P_Epsus4(Str LEN){PianoChord(5, 0,5,7,0, LEN)}
Function P_Ep7sus4(Str LEN){PianoChord(5, 0,5,10,7, LEN)}
Function P_Ep6(Str LEN){PianoChord(5, 4,7,9,12, LEN)}
Function P_Epadd9(Str LEN){PianoChord(5, 4,7,14,12, LEN)}
// Cb
Function P_Cb(Str LEN){PianoChord(11, 0,4,7,0, LEN)}
Function P_Cbm(Str LEN){PianoChord(11, 0,3,7,0, LEN)}
Function P_Cb7(Str LEN){PianoChord(11, 0,4,10,7, LEN)}
Function P_CbM7(Str LEN){PianoChord(11, 0,4,11,7, LEN)}
Function P_Cbm7(Str LEN){PianoChord(11, 0,3,10,7, LEN)}
Function P_Cbdim(Str LEN){PianoChord(11, 3,6,9,12, LEN)}
Function P_Cbm7b5(Str LEN){PianoChord(11, 3,6,10,12, LEN)}
Function P_Cbaug(Str LEN){PianoChord(11, 0,4,8,0, LEN)}
Function P_Cbsus4(Str LEN){PianoChord(11, 0,5,7,0, LEN)}
Function P_Cb7sus4(Str LEN){PianoChord(11, 0,5,10,7, LEN)}
Function P_Cb6(Str LEN){PianoChord(11, 4,7,9,12, LEN)}
Function P_Cbadd9(Str LEN){PianoChord(11, 4,7,14,12, LEN)}

//------------------------------------------------------------------------------
