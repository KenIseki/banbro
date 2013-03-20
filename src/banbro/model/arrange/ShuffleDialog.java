package banbro.model.arrange;

import java.awt.Component;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import banbro.util.OptionDialog;

public class ShuffleDialog extends OptionDialog {
	private static final long serialVersionUID = 1L;

	public interface ShuffleOption {
		int getShuffleMaxTempo();
		void setShuffleMaxTempo(int tempo);
		int[] getShuffleValues();
		int getShuffleValue(int noteFlag);
		void setShuffleValue(int noteFlag, int value);
	}

	/** 音符のパターンをこの順番で表示する */
	private static int[] NOTE_LIST = new int[] {
		0b1000, 0b0100, 0b0010, 0b0001,  // 音符1個
		0b1100, 0b1010, 0b1001, 0b0110, 0b0101, 0b0011,  // 音符2個
		0b1110, 0b1101, 0b1011, 0b0111,  // 音符3個
		0b1111  // 音符4個
	};

	private List<JSlider> _valuesSliders;
	private ShuffleOption _option;

	private JSpinner _maxTempoSpinner;

	public ShuffleDialog(Window owner, ShuffleOption option) {
		super(owner, "シャッフル設定", true);
		_option = option;
		setShuffleValue(option.getShuffleMaxTempo(), option.getShuffleValues());
	}

	@Override
	protected Component createMainComponent() {
		JPanel mainPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		List<JLabel> labelList = new ArrayList<JLabel>();
		List<JComponent> valueList = new ArrayList<JComponent>();
		_valuesSliders = new ArrayList<JSlider>();
		
		labelList.add(createLabel("最大テンポ", "半速判定の対象となるテンポ(" +Shuffle.MIN_MAX_TEMPO + "～" + Shuffle.MAX_MAX_TEMPO + ")"));
		SpinnerNumberModel tmodel = new SpinnerNumberModel(Shuffle.DEFAULT_MAX_TEMPO, Shuffle.MIN_MAX_TEMPO, Shuffle.MAX_MAX_TEMPO, 1);
		_maxTempoSpinner = new JSpinner(tmodel);
		valueList.add(_maxTempoSpinner);
		labelList.add(createLabel("半速閾値", "値が大きいほど半速とされやすくなる"));
		JSlider slider = createSlider(0, 100, 0);
		valueList.add(slider);
		_valuesSliders.add(slider);
		for (int i=0; i<NOTE_LIST.length; i++) {
			String text = "";
			for (int j=0; j<4; j++) {
				if ((NOTE_LIST[i]&(0b1000>>j))!=0) {
					text += "♪";
				} else {
					text += "－";
				}
			}
			labelList.add(createLabel(text, "1拍の音符パターン毎の半速閾値"));
			slider = createSlider(0, 100, 0);
			valueList.add(slider);
			_valuesSliders.add(slider);
		}
		GroupLayout groupLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(groupLayout);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
		GroupLayout.ParallelGroup labelGroup = groupLayout.createParallelGroup();
		for (JLabel label : labelList) {
			labelGroup.addComponent(label);
		}
		GroupLayout.ParallelGroup valueGroup = groupLayout.createParallelGroup();
		for (Component component : valueList) {
			valueGroup.addComponent(component);
		}
		hGroup.addGroup(labelGroup);
		hGroup.addGroup(valueGroup);
		groupLayout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();
		assert(labelList.size()==valueList.size());
		for (int i=0; i<labelList.size(); i++) {
			GroupLayout.ParallelGroup group = groupLayout.createParallelGroup();
			group.addComponent(labelList.get(i));
			group.addComponent(valueList.get(i));
			vGroup.addGroup(group);
		}
		groupLayout.setVerticalGroup(vGroup);
		return scrollPane;
	}

	@Override
	protected void initSize() {
		setSize(600, 600);
		setResizable(false);
	}

	private JLabel createLabel(String text, String toolTip) {
		JLabel label = new JLabel(text);
		label.setToolTipText(toolTip);
		return label;
	}

	private JSlider createSlider(int min, int max, int value) {
		JSlider slider = new JSlider(min,max,value);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		return slider;
	}

	@Override
	protected void pushDefaultButton() {
		setShuffleValue(Shuffle.DEFAULT_MAX_TEMPO, Shuffle.getDefaultShuffleValues());
	}

	private void setShuffleValue(int maxTempo, int[] values) {
		assert(values.length==_valuesSliders.size());
		_maxTempoSpinner.setValue(maxTempo);
		_valuesSliders.get(0).setValue(values[0]);
		for (int i=0; i<NOTE_LIST.length; i++) {
			int index = NOTE_LIST[i];
			_valuesSliders.get(i+1).setValue(values[index]);
		}
	}

	@Override
	protected void pushOkButton() {
		_option.setShuffleMaxTempo((int) _maxTempoSpinner.getValue());
		_option.setShuffleValue(0b0000, _valuesSliders.get(0).getValue());
		for (int i=0; i<NOTE_LIST.length; i++) {
			int index = NOTE_LIST[i];
			_option.setShuffleValue(index, _valuesSliders.get(i+1).getValue());
		}
	}

}
