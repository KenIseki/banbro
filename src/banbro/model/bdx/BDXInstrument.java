package banbro.model.bdx;

public enum BDXInstrument {
	// �s�A�m�n
	PIANO(0x01, "�s�A�m"),
	E_PIANO(0x02, "�d�E�s�A�m"),
	HARPSICH_(0x83, "�`�F���o��"),
	R_ORGAN(0x03, "���b�N�I���K��"),
	P_ORGAN(0x06, "�p�C�v�I���K��"),
	ACCORDION(0x84, "�A�R�[�f�B�I��"),
	HARMONICA(0x1E, "�n�[���j�J"),
	VIBRAPH_(0x21, "�r�u���t�H��"),
	MARIMBA(0x22, "�}�����o"),
	MUSIC_BOX(0x86, "�I���S�[��"),
	// �M�^�[���x�[�X
	S_GUITAR(0x89, "�N���V�b�N�M�^�["), 
	F_GUITAR(0x07, "�t�H�[�N�M�^�["),
	E_GUITAR(0x08, "�d�E�M�^�["),
	C_GUITAR(0x8A, "�N���[���M�^�["),
	O_GUITAR(0x8B, "�n�c�E�M�^�["),
	D_GUITAR(0x09, "�c�E�M�^�["),
	R_GUITAR(0x0A, "���b�N�M�^�["),
	PICK_BASS(0x0B, "�s�b�N�x�[�X"),
	SLAP_BASS(0x8C, "�X���b�v�x�[�X"),
	SYN_BASS(0x0C, "�V���Z�x�[�X"),
	ACO_BASS(0x0D, "�`�E�x�[�X"),
	// �V���Z�T�C�U�[
	SYN_LEAD(0x04, "�V���Z���[�h"),
	SQ_LEAD(0x88, "�X�N�G�A���[�h"),
	SYN_BELL(0x05, "�V���Z�x��"),
	NES(0x29, "�t�@�~�R��"),
	// �X�g�����O�X�n
	VIOLIN(0x0F, "�o�C�I����"),
	DBL_BASS(0x10, "�R���g���o�X"),
	STRINGS(0x0E, "�X�g�����O�X"),
	P_VIOLIN(0x12, "�s�`�J�[�g"),
	CHORUS(0x25, "�R�[���X"),
	HARP(0x11, "�n�[�v"),
	TIMPANI(0x23, "�e�B���p�j�["),
	// ���Ǌy��
	TRUMPET(0x1A, "�g�����y�b�g"),
	TROMBONE(0x1B, "�g�����{�[��"),
	HORN(0x1C, "�z����"),
	TUBA(0x1D, "�`���[�o"),
	BRASS(0x19, "�u���X"),
	M_TRUMPET(0x85, "�l���g�����y�b�g"),
	// �؊Ǌy��
	PICCOLO(0x13, "�s�b�R��"),
	FLUTE(0x14, "�t���[�g"),
	SOPR_SAX(0x17, "�\�v���m�T�b�N�X"),
	ALTO_SAX(0x18, "�A���g�T�b�N�X"),
	CLARINET(0x15, "�N�����l�b�g"),
	OBOE(0x16, "�I�[�{�G"),
	// �����y��
	PAN_FLUTE(0x1F, "�p���t���[�g"),
	OCARINA(0x20, "�I�J���i"),
	SHAKUHA_(0x28, "�V���N�n�`"),
	SHAMISEN(0x26, "�V���~�Z��"),
	KOTO(0x27, "�R�g"),
	BANJO(0x87, "�o���W���["),
	STEEL_DRM(0x24, "�X�`�[���h����"),
	// �h����
	R_DRUMS(0x2A, true, "���b�N�h����"),
	E_DRUMS(0x2B, true, "�d�E�h����"),
	S_DRUMS(0x2C, true, "�V���Z�h����"),
	PERC_SET(0x2D, true, "�K�N�_���Z�b�g"),
	BONGO_SET(0x2E, true, "�{���S�Z�b�g"),
	CONGA_SET(0x2F, true, "�R���K�Z�b�g"),
	JPN_PERC_(0x30, true, "���_�C�R�Z�b�g"),
	// �y��Ȃ�
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
