//------------------------------------------------------------------------------ Header.
//_�o���u��DX�R�[�h�p�[�g�p�w�b�_ ver1.2
/*
  ���o���u��DX�ŗp�ӂ���Ă���R�[�h���g�p����ɂ�
    bdxguitar.h �܂��� bdxpiano.h ��ǂݍ���ł��������B
*/
/*
  �X�V����
    ver    ���t        ����
    1.0    20110228    ���J
    1.1    20110309    ������������PrintChordInfo����SetChordType�ɕύX
    1.2    20110503    GT�̒u��������{X}�ȊO�ɕύX�\�ɂ���
*/
//------------------------------------------------------------------------------
//_����
Int BBS_Guitar = 2;  // �M�^�[�a��
Int BBS_Piano = 3;  // �s�A�m�a��
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

/* CHO_:�R�[�h��
 * RHY_:���Y��
 */
Function GT(Str CHO_, STR RHY_){  // RHY_ ���� ChordFunctionStr �� CHO_ �ɒu�����A���t����
	STR R_=RHY_;
	SWITCH(ChordType){
		CASE(BBS_Guitar){R_.s(ChordFunctionStr,{"G_"}+CHO_);}
		CASE(BBS_Piano){R_.s(ChordFunctionStr,{"P_"}+CHO_);}
		DEFAULT{R_.s(ChordFunctionStr,CHO_);}
	}
	R_;
	// �R�[�h�i�s�̋L�^
	#NowChord = CHO_;
	IF(#NowChord!=#LastChord){
		ChordList = (ChordList, #NowChord);
		#LastChord = #NowChord;
	}
}

Function PrintChordInfo(){  // �a���p�[�g�̊e�����\��
	// ��Ȑ����̌v�Z�J�n
	Int NowChordIndex;
	Array ChordSet = ();  // �g�p����R�[�h���g�p���Ɋi�[
	Array ChordNum = ();  // �g�p�p�x
	Array IndexList = ();  // �R�[�h�Z�b�g�̐؂�ڂƂȂ�Index���i�[(�M�^�[�a���p)
	FOR(Int Index=0; Index<SizeOf(ChordList); Index++){
		FOR(NowChordIndex=0; NowChordIndex<SizeOf(ChordSet); NowChordIndex++){
			IF(ChordSet(NowChordIndex)==ChordList(Index)){
				EXIT;
			}
		}
		IF(NowChordIndex==SizeOf(ChordSet)){  // �V�����R�[�h�̎�
			IF(ChordType==BBS_Guitar && NowChordIndex>=8){  // �M�^�[�R�[�h1�Z�b�g8��ނ܂�
				IndexList = (IndexList, Index);
				// �g�p�p�x�������R�[�h��T��
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
				// ���̃R�[�h�Z�b�g�Ɋ܂܂��R�[�h��T��
				// �g�p�p�x���ő��̃R�[�h(�������̏ꍇ�͍Ō�Ɏg�p�����R�[�h���D��)
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
			}  // �M�^�[�a���p�݂̂̌v�Z�I��
			ChordSet = (ChordSet, ChordList(Index));
			ChordNum = (ChordNum, 1);
		}ELSE{  // ���o�̃R�[�h�̎�
			ChordNum(NowChordIndex) = ChordNum(NowChordIndex) + 1;
		}
	}
	// ��Ȑ����̌v�Z�I��
	Print({"�R�[�h�`�F���W : "} + SizeOf(ChordList) + {"(255�ȉ�)"});
	SWITCH(ChordType){
		CASE(BBS_Guitar){
			IndexList = (IndexList, SizeOf(ChordList));
			Print({"�R�[�h�Z�b�g : "} + SizeOf(IndexList) + {"(31�ȉ�)"});
			Int FromIndex = 0;
			Array ChordSetArray;  // 1�Z�b�g���̃R�[�h�i�s���i�[
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
			Print({"�R�[�h�̎�� : "} + SizeOf(ChordSet) + {"(32�ȉ�)"});
			FOR(Int I=0; I<SizeOf(ChordSet); I++){
				Print(ChordSet(I) + {" : "} + ChordNum(I));
			}
		}
		DEFAULT{
			Print({"�g�p�R�[�h : "} + ChordList);
		}
	}
}

//------------------------------------------------------------------------------
