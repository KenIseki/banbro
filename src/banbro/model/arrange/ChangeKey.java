package banbro.model.arrange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import banbro.model.bdx.BDX;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;

/**
 * 単音パートの音階を変更するアレンジ
 */
public abstract class ChangeKey extends AbstractArrange {

	protected List<StepValue<Integer>> _keyOffset;
	
	/**
	 * @see AbstractArrange#AbstractArrange()
	 */
	protected ChangeKey() {
		super();
	}
	/**
	 * @param isArrangeTempo
	 * @param isBDX
	 * @see AbstractArrange#AbstractArrange(boolean, boolean)
	 */
	protected ChangeKey(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected BDX doArrange(BDX bdx) {
		_keyOffset = calcKeyOffset(bdx);
		return super.doArrange(bdx);
	}

	private List<StepValue<Integer>> calcKeyOffset(BDX bdx) {
		List<StepValue<Integer>> keyList = new ArrayList<StepValue<Integer>>(bdx.getKey());
		keyList.add(new StepValue<Integer>(BDX.TIME_BASE*bdx.getTimeNum(), 0));
		List<StepValue<Integer>> keyOffset = new ArrayList<StepValue<Integer>>();

		for (int keyI=0; keyI<keyList.size()-1; keyI++) {
			StepValue<Integer> sv1 = keyList.get(keyI);
			StepValue<Integer> sv2 = keyList.get(keyI+1);
			int begin = (sv1.getStep() / BDX.TIME_BASE) * 4;
			int end = (sv2.getStep() / BDX.TIME_BASE) * 4;
			int[] noteMap = new int[12];  // 各音階の出現回数
			for (int partNum=0; partNum<bdx.getPartNum(); partNum++) {
				Part p = bdx.getPart(partNum);
				if (p.getType()!=InstrumentType.SINGLE) {
					continue;
				}
				List<Note> noteList = p.getNoteList();
				for (int j=begin; j<end; j++) {
					if (j>=noteList.size()) {
						break;
					}
					SingleNote n = (SingleNote) noteList.get(j);
					if (n.isNote()) {
						noteMap[n.getSingleNoteNum()%12]++;
					}
				}
			}
			int[] test = new int[12];  // キー毎の臨時記号なしで演奏できる音階の数
			int[] defaultKey = new int[] {0, 2, 4, 5, 7, 9, 11};  // 臨時記号なしの音階
			for (int pitch=0; pitch<12; pitch++) {
				for (int dk : defaultKey) {
					test[pitch] += noteMap[(pitch+dk)%12];
				}
			}
			int offset = 0;
			int max = 0;
			for (int i=0; i<test.length; i++) {
				if (test[i]>max) {
					max = test[i];
					offset = i;
				}
			}
			keyOffset.add(new StepValue<Integer>(sv1.getStep(), offset));
		}
		keyOffset.add(new StepValue<Integer>(BDX.TIME_BASE*bdx.getTimeNum(), 0));
		
		return keyOffset;
	}

	@Override
	protected void arrangeSinglePart(Part p) {
		int lastNoteNum = SingleNote.REST;
		int lastNoteNum2 = SingleNote.REST;
		for (int i=0; i<_keyOffset.size()-1; i++) {
			StepValue<Integer> sv1 = _keyOffset.get(i);
			StepValue<Integer> sv2 = _keyOffset.get(i+1);
			int begin = (sv1.getStep() / BDX.TIME_BASE) * 4;
			int end = (sv2.getStep() / BDX.TIME_BASE) * 4;
			List<Note> noteList = p.getNoteList();
			for (int j=begin; j<end; j++) {
				if (j>=noteList.size()) {
					break;
				}
				SingleNote note = (SingleNote) noteList.get(j);
				if (note.isNote() || note.isExtend()) {
					int noteNum = note.getSingleNoteNum();
					if (note.isExtend() && (noteNum==lastNoteNum)) {
						p.setNote(j, SingleNote.getInstance(lastNoteNum2, true));
						continue;
					}
					lastNoteNum = noteNum;
					int oct = noteNum/12;
					int n = (noteNum%12) - sv1.getValue();
					if (n<0) {
						oct--;
						n += 12;
					}
					n = calcNewNote(n);
					n += sv1.getValue();
					if (n>=12) {
						oct++;
						n -= 12;
					}
					noteNum = oct*12 + n;
					lastNoteNum2 = noteNum;
					p.setNote(j, SingleNote.getInstance(noteNum, note.isExtend()));
				}
			}
		}
		// ボタン割り当て調整
		Set<Integer> stepSet = new HashSet<Integer>();
		for (StepValue<Integer> sv : p.getBass()) {
			stepSet.add(sv.getStep());
		}
		for (StepValue<Integer> sv: p.getButton()) {
			stepSet.add(sv.getStep());
		}
		List<Integer> stepList = new ArrayList<Integer>(stepSet);
		Collections.sort(stepList);
		stepList.add(_keyOffset.get(_keyOffset.size()-1).getStep());
		List<StepValue<Integer>> newBass = new ArrayList<StepValue<Integer>>();
		List<StepValue<Integer>> newButton = new ArrayList<StepValue<Integer>>();
		for (int i=0; i<stepList.size()-1; i++) {
			int step1 = stepList.get(i);
			int step2 = stepList.get(i+1);
			int begin = stepToNoteNum(step1);
			int end = stepToNoteNum(step2);
			int bass = p.getBass(step1);
			int button = p.getButton(step1);
			int maxNoteNum = 0x00;
			int minNoteNum = 0xFF;
			List<Note> noteList = p.getNoteList();
			for (int j=begin; j<end; j++) {
				if (j>=noteList.size()) {
					break;
				}
				SingleNote note = (SingleNote) noteList.get(j);
				if (note.isNote()) {
					int noteNum = note.getNoteNum();
					if (noteNum>maxNoteNum) {
						maxNoteNum = noteNum;
					}
					if (noteNum<minNoteNum) {
						minNoteNum = noteNum;
					}
				}
			}
			if (minNoteNum<=maxNoteNum) {
				if (button>=2) {
					bass++;
				} else if (button<=-6) {
					bass--;
				}
				int offset = 0;
				if ((bass+24)<maxNoteNum) {
					offset = maxNoteNum - (bass+24);
				}
				if (minNoteNum<bass) {
					offset = minNoteNum - bass;
				}
				bass += offset;
				if (button>=2) {
					bass--;
				} else if (button<=-6) {
					bass++;
				}
			}
			newBass.add(new StepValue<Integer>(step1, bass));
			newButton.add(new StepValue<Integer>(step1, button));
		}
		p.clearBass();
		for (StepValue<Integer> sv : newBass) {
			p.addBass(sv.getStep(), sv.getValue());
		}
		p.clearButton();
		for (StepValue<Integer> sv : newButton) {
			p.addButton(sv.getStep(), sv.getValue());
		}
	}

	private int stepToNoteNum(int step) {
		int num = (step / BDX.TIME_BASE) * 4;
		int st = step % BDX.TIME_BASE;
		switch (st) {
		case 0:
			break;
		case 3:
			num += 1;
			break;
		case 6:
			num += 2;
			break;
		case 9:
			num += 3;
			break;
		case 4:
			num += 2;
			break;
		case 8:
			num += 3;
			break;
		default:
			break;
		}
		return num;
	}

	/**
	 * @param n Cが0の相対音階
	 * @return 変更後の相対音階
	 */
	protected abstract int calcNewNote(int n);

}
