package banbro.io.midi;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import banbro.model.bdx.BDXInstrument;
import banbro.util.ComponentUtil;
import banbro.util.OptionDialog;

public class MidiOptionDialog extends OptionDialog {
	private static final long serialVersionUID = 1L;

	private JSpinner _nesNumSpinner;
	private JSpinner _rguitarNumSpinner;
	private JCheckBox _rockGuitarChordCheckBox;
	private JCheckBox _musicBoxOctaveUpCheckBox;
	private JCheckBox _envelopeCheckBox;
	private JCheckBox _vibratoCheckBox;
	private JCheckBox _effectsCheckBox;
	private JSpinner _drumNumSpinner;
	private List<JSpinner> _rDrumSpinners;
	private List<JSpinner> _eDrumSpinners;
	private List<JSpinner> _sDrumSpinners;
	private List<JSpinner> _percSetSpinners;
	private List<JSpinner> _bongoSetSpinners;
	private List<JSpinner> _congaSetSpinners;
	private List<JSpinner> _jpnPercSpinners;

	private MidiOption _option;

	public MidiOptionDialog(Frame owner, MidiOption midiOption) {
		super(owner, "MIDI設定", true);
		_option = midiOption;
		setOption(midiOption);
	}

	@Override
	protected Component createMainComponent() {
		JPanel mainPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		GridBagLayout layout = new GridBagLayout();
		mainPanel.setLayout(layout);
		int y = 0;
		// 横13

		// ロックギター
		mainPanel.add(new JLabel("ロックギター"), getDefaultConstraints(0, y, 1, 1));
		SpinnerNumberModel rguitarNumModel = new SpinnerNumberModel(1, 1, 128, 1);
		_rguitarNumSpinner = new JSpinner(rguitarNumModel);
		((JSpinner.NumberEditor)_rguitarNumSpinner.getEditor()).getTextField().setEditable(false);
		mainPanel.add(_rguitarNumSpinner, getDefaultConstraints(1, y, 1, 1));
		// ロックギターのパワーコード
		_rockGuitarChordCheckBox = new JCheckBox("単音ロックギターをパワーコードにする");
		mainPanel.add(_rockGuitarChordCheckBox, getDefaultConstraints(3, y, 5, 1));
		// エンベロープ
		_envelopeCheckBox = new JCheckBox("エンベロープを出力する");
		mainPanel.add(_envelopeCheckBox, getDefaultConstraints(8, y, 5, 1));
		y++;

		// ファミコン
		mainPanel.add(new JLabel("ファミコン"), getDefaultConstraints(0, y, 1, 1));
		SpinnerNumberModel nesNumModel = new SpinnerNumberModel(1, 1, 128, 1);
		_nesNumSpinner = new JSpinner(nesNumModel);
		((JSpinner.NumberEditor)_nesNumSpinner.getEditor()).getTextField().setEditable(false);
		mainPanel.add(_nesNumSpinner, getDefaultConstraints(1, y, 1, 1));
		// オルゴールオクターブ上げ
		_musicBoxOctaveUpCheckBox = new JCheckBox("オルゴールの音を1オクターブ上げる");
		mainPanel.add(_musicBoxOctaveUpCheckBox, getDefaultConstraints(3, y, 5, 1));
		// ビブラート
		_vibratoCheckBox = new JCheckBox("ビブラートを出力する");
		mainPanel.add(_vibratoCheckBox, getDefaultConstraints(8, y, 5, 1));
		y++;

		// ドラム
		mainPanel.add(new JLabel("ドラム"), getDefaultConstraints(0, y, 1, 1));
		SpinnerNumberModel drumNumModel = new SpinnerNumberModel(1, 1, 128, 1);
		_drumNumSpinner = new JSpinner(drumNumModel);
		((JSpinner.NumberEditor)_drumNumSpinner.getEditor()).getTextField().setEditable(false);
		mainPanel.add(_drumNumSpinner, getDefaultConstraints(1, y, 1, 1));
		// エフェクト
		_effectsCheckBox = new JCheckBox("エフェクトを出力する");
		mainPanel.add(_effectsCheckBox, getDefaultConstraints(8, y, 5, 1));
		y++;

		// ドラムのボタン
		String[] buttonNames = {"Ｂ", "Ａ", "Ｙ", "Ｘ", "↑", "↓", "←", "→", "Ｌ", "Ｒ"};
		for (int i=0; i<buttonNames.length; i++) {
			GridBagConstraints constraints = getDefaultConstraints(i+1, y, 1, 1);
			constraints.anchor = GridBagConstraints.CENTER;
			mainPanel.add(new JLabel(buttonNames[i]), constraints);
		}
		y++;

		// 各ドラムの音
		_rDrumSpinners = createDrumOption(mainPanel, 0, y, buttonNames, BDXInstrument.R_DRUMS.getName());
		y++;
		_eDrumSpinners = createDrumOption(mainPanel, 0, y, buttonNames, BDXInstrument.E_DRUMS.getName());
		y++;
		_sDrumSpinners = createDrumOption(mainPanel, 0, y, buttonNames, BDXInstrument.S_DRUMS.getName());
		y++;
		_percSetSpinners = createDrumOption(mainPanel, 0, y, buttonNames, BDXInstrument.PERC_SET.getName());
		y++;
		_bongoSetSpinners = createDrumOption(mainPanel, 0, y, buttonNames, BDXInstrument.BONGO_SET.getName());
		y++;
		_congaSetSpinners = createDrumOption(mainPanel, 0, y, buttonNames, BDXInstrument.CONGA_SET.getName());
		y++;
		_jpnPercSpinners = createDrumOption(mainPanel, 0, y, buttonNames, BDXInstrument.JPN_PERC_.getName());
		y++;
		
		return scrollPane;
	}

	private List<JSpinner> createDrumOption(JPanel mainPanel, int x, int y, String[] buttonNames, String name) {
		mainPanel.add(new JLabel(name), getDefaultConstraints(x, y, 1, 1));
		List<JSpinner> drumSpinnerList = new ArrayList<JSpinner>();
		assert(buttonNames.length==10);
		for (int i=0; i<buttonNames.length; i++) {
			JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 128, 1));
			((JSpinner.NumberEditor)spinner.getEditor()).getTextField().setEditable(false);
			mainPanel.add(spinner, getDefaultConstraints(i+1, y, 1, 1));
			drumSpinnerList.add(spinner);
		}
		return drumSpinnerList;
	}

	@Override
	protected void pushDefaultButton() {
		setOption(new MidiOption());
	}

	private void setOption(MidiOption option) {
		_nesNumSpinner.setValue(option.getNesNum());
		_rguitarNumSpinner.setValue(option.getRguitarNum());
		_rockGuitarChordCheckBox.setSelected(option.isRockGuitarChord());
		_musicBoxOctaveUpCheckBox.setSelected(option.isMusicBoxOctaveUp());
		_envelopeCheckBox.setSelected(option.isOutputEnvelope());
		_vibratoCheckBox.setSelected(option.isOutputVibrato());
		_effectsCheckBox.setSelected(option.isOutputEffects());
		_drumNumSpinner.setValue(option.getDrumNum());
		List<int[]> drumList = Arrays.asList(option.getRDrum(), option.getEDrum(), option.getSDrum(),
				option.getPercSet(), option.getBongoSet(), option.getCongaSet(), option.getJpnPerc());
		List<List<JSpinner>> spinnersList =Arrays.asList(_rDrumSpinners, _eDrumSpinners, _sDrumSpinners,
				_percSetSpinners, _bongoSetSpinners, _congaSetSpinners, _jpnPercSpinners);
		assert(drumList.size()==spinnersList.size());
		for (int i=0; i<spinnersList.size(); i++) {
			int[] drum = drumList.get(i);
			List<JSpinner> spinners = spinnersList.get(i);
			assert(drum.length==spinners.size());
			for (int j=0; j<spinners.size(); j++) {
				spinners.get(j).setValue(drum[j]);
			}
		}
	}

	@Override
	protected void pushOkButton() {
		Object value;
		value = _rguitarNumSpinner.getValue();
		if (value instanceof Integer) {
			_option.setRguitarNum((Integer)value);
		}
		value = _nesNumSpinner.getValue();
		if (value instanceof Integer) {
			_option.setNesNum((Integer)value);
		}
		_option.setIsRockGuitarChord(_rockGuitarChordCheckBox.isSelected());
		_option.setIsMusicBoxOctaveUp(_musicBoxOctaveUpCheckBox.isSelected());
		_option.setOutputEnvelope(_envelopeCheckBox.isSelected());
		_option.setOutputVibrato(_vibratoCheckBox.isSelected());
		_option.setOutputEffects(_effectsCheckBox.isSelected());
		value = _drumNumSpinner.getValue();
		if (value instanceof Integer) {
			_option.setDrumNum((Integer)value);
		}
		for (int i=0; i<_rDrumSpinners.size(); i++) {
			value = _rDrumSpinners.get(i).getValue();
			if (value instanceof Integer) {
				_option.setRDrum(i, (Integer)value);
			}
		}
		for (int i=0; i<_eDrumSpinners.size(); i++) {
			value = _eDrumSpinners.get(i).getValue();
			if (value instanceof Integer) {
				_option.setEDrum(i, (Integer)value);
			}
		}
		for (int i=0; i<_sDrumSpinners.size(); i++) {
			value = _sDrumSpinners.get(i).getValue();
			if (value instanceof Integer) {
				_option.setSDrum(i, (Integer)value);
			}
		}
		for (int i=0; i<_percSetSpinners.size(); i++) {
			value = _percSetSpinners.get(i).getValue();
			if (value instanceof Integer) {
				_option.setPercSet(i, (Integer)value);
			}
		}
		for (int i=0; i<_bongoSetSpinners.size(); i++) {
			value = _bongoSetSpinners.get(i).getValue();
			if (value instanceof Integer) {
				_option.setBongoSet(i, (Integer)value);
			}
		}
		for (int i=0; i<_congaSetSpinners.size(); i++) {
			value = _congaSetSpinners.get(i).getValue();
			if (value instanceof Integer) {
				_option.setCongaSet(i, (Integer)value);
			}
		}
		for (int i=0; i<_jpnPercSpinners.size(); i++) {
			value = _jpnPercSpinners.get(i).getValue();
			if (value instanceof Integer) {
				_option.setJpnPerc(i, (Integer)value);
			}
		}
	}

	protected GridBagConstraints getDefaultConstraints(int x, int y, int w, int h) {
		GridBagConstraints constraints = ComponentUtil.createConstraints(x, y, w, h);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5,5,5,5);
		return constraints;
	}

}
