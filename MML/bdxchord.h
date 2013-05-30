//------------------------------------------------------------------------------ Header.
//_バンブラDXコードパート用ヘッダ ver1.2
/*
  ※バンブラDXで用意されているコードを使用するには
    bdxguitar.h または bdxpiano.h を読み込んでください。
*/
/*
  更新履歴
    ver    日付        履歴
    1.0    20110228    公開
    1.1    20110309    初期化処理をPrintChordInfoからSetChordTypeに変更
    1.2    20110503    GTの置換文字を{X}以外に変更可能にした
*/
//------------------------------------------------------------------------------
//_共通
Int BBS_Guitar = 2;  // ギター和音
Int BBS_Piano = 3;  // ピアノ和音
Int ChordType;
#LastChord = {};
#NowChord = {};
Array ChordList = ();
Str ChordFunctionStr = {X};

Function SetChordType(Int Type){  // Type:BBS_Guitar or BBS_Piano
	ChordType = Type;
	#LastChord = {};
	#NowChord = {};
	ChordList = ();
}

/* CHO_:コード名
 * RHY_:リズム
 */
Function GT(Str CHO_, STR RHY_){  // RHY_ 内の ChordFunctionStr を CHO_ に置換し、演奏する
	STR R_=RHY_;
	SWITCH(ChordType){
		CASE(BBS_Guitar){R_.s(ChordFunctionStr,{"G_"}+CHO_);}
		CASE(BBS_Piano){R_.s(ChordFunctionStr,{"P_"}+CHO_);}
		DEFAULT{R_.s(ChordFunctionStr,CHO_);}
	}
	R_;
	// コード進行の記録
	#NowChord = CHO_;
	IF(#NowChord!=#LastChord){
		ChordList = (ChordList, #NowChord);
		#LastChord = #NowChord;
	}
}

Function PrintChordInfo(){  // 和音パートの各種情報を表示
	// 作曲制限の計算開始
	Int NowChordIndex;
	Array ChordSet = ();  // 使用するコードを使用順に格納
	Array ChordNum = ();  // 使用頻度
	Array IndexList = ();  // コードセットの切れ目となるIndexを格納(ギター和音用)
	FOR(Int Index=0; Index<SizeOf(ChordList); Index++){
		FOR(NowChordIndex=0; NowChordIndex<SizeOf(ChordSet); NowChordIndex++){
			IF(ChordSet(NowChordIndex)==ChordList(Index)){
				EXIT;
			}
		}
		IF(NowChordIndex==SizeOf(ChordSet)){  // 新しいコードの時
			IF(ChordType==BBS_Guitar && NowChordIndex>=8){  // ギターコード1セット8種類まで
				IndexList = (IndexList, Index);
				// 使用頻度が多いコードを探す
				Int Max=0;
				FOR(Int I=0; I<SizeOf(ChordNum); I++){
					IF(ChordNum(I)>Max){
						Max = ChordNum(I);
					}
				}
				Array MaxChordArray = ();
				FOR(Int I=0; I<SizeOf(ChordNum); I++){
					IF(ChordNum(I)==Max){
						MaxChordArray = (MaxChordArray, ChordSet(I));
					}
				}
				// 次のコードセットに含まれるコードを探す
				// 使用頻度が最多のコード(同じ数の場合は最後に使用したコードが優先)
				Int MaxIndex=0;
				FOR(Int I=0; I<SizeOf(MaxChordArray); I++){
					FOR(Int I2=SizeOf(ChordList)-1; I2>=0; I2--){
						IF(ChordList(I2)==MaxChordArray(I)){
							IF(I2>MaxIndex){
								MaxIndex = I2;
							}
							EXIT;
						}
					}
				}
				ChordSet = (ChordList(MaxIndex));
				ChordNum = (0);
			}  // ギター和音用のみの計算終了
			ChordSet = (ChordSet, ChordList(Index));
			ChordNum = (ChordNum, 1);
		}ELSE{  // 既出のコードの時
			ChordNum(NowChordIndex) = ChordNum(NowChordIndex) + 1;
		}
	}
	// 作曲制限の計算終了
	Print({"コードチェンジ : "} + SizeOf(ChordList) + {"(255以下)"});
	SWITCH(ChordType){
		CASE(BBS_Guitar){
			IndexList = (IndexList, SizeOf(ChordList));
			Print({"コードセット : "} + SizeOf(IndexList) + {"(31以下)"});
			Int FromIndex = 0;
			Array ChordSetArray;  // 1セット分のコード進行を格納
			FOR(Int I=0; I<SizeOf(IndexList); I++){
				ChordSetArray = ();
				FOR(Int I2=FromIndex; I2<IndexList(I); I2++){
					ChordSetArray = (ChordSetArray, ChordList(I2));
				}
				Print(ChordSetArray);
				FromIndex = IndexList(I);
			}
		}
		CASE(BBS_Piano){
			Print({"コードの種類 : "} + SizeOf(ChordSet) + {"(32以下)"});
			FOR(Int I=0; I<SizeOf(ChordSet); I++){
				Print(ChordSet(I) + {" : "} + ChordNum(I));
			}
		}
		DEFAULT{
			Print({"使用コード : "} + ChordList);
		}
	}
}

//------------------------------------------------------------------------------
