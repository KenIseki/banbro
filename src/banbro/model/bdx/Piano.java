package banbro.model.bdx;

import java.util.ArrayList;
import java.util.List;

import banbro.model.bdx.Voicing.Spread;

public class Piano {

	private Voicing _voicing;
	private List<PianoChord> _originalChords;

	public Piano() {
		_voicing = new Voicing();
		_originalChords = new ArrayList<PianoChord>();
	}

	public Voicing getVoicing() {
		return _voicing;
	}

	public int getVoicingTopNote() {
		return _voicing.getTopNote();
	}

	public int getVoicingNotes() {
		return _voicing.getNotes();
	}

	public Spread getVoicingSpread() {
		return _voicing.getSpread();
	}	

	public List<PianoChord> getOriginalChords() {
		return _originalChords;
	}

	public void setVoicing(Voicing v) {
		setVoicing(v.getTopNote(), v.getNotes(), v.getSpread());
	}

	/**
	 * @param top
	 * @param notes
	 * @param spread
	 * @see Voicing#Voicing(int, int, Spread)
	 */
	public void setVoicing(int top, int notes, Spread spread) {
		setVoicingTop(top);
		setVoicingNotes(notes);
		setVoicingSpread(spread);
	}

	public void setVoicingTop(int top) {
		_voicing.setTopNote(top);
	}

	public void setVoicingNotes(int notes) {
		_voicing.setNotes(notes);
	}

	public void setVoicingSpread(Spread spread) {
		_voicing.setSpread(spread);
	}

	/**
	 * @param n 0-15
	 * @param c
	 */
	public void setOriginalChord(int n, PianoChord c) {
		assert(0<=n && n<_originalChords.size());
		if (c.getChordName()==null) {
			OriginalChord chord = new OriginalChord(n);
			c.setChordName(chord.toString());
		}
		_originalChords.set(n, c);
	}

	public void addOriginalChord(PianoChord c) {
		if (c.getChordName()==null) {
			OriginalChord chord = new OriginalChord(_originalChords.size());
			c.setChordName(chord.toString());
		}
		_originalChords.add(c);
	}

	public Piano clone() {
		Piano copy = new Piano();
		copy.setVoicing(getVoicingTopNote(), getVoicingNotes(), getVoicingSpread());
		for (int i=0; i<_originalChords.size(); i++) {
			copy.addOriginalChord(_originalChords.get(i).clone());
		}
		return copy;
	}

}
