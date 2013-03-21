package banbro.model.bdx;

import java.util.ArrayList;
import java.util.List;

public class Guitar {

	private List<GuitarChord> _originalChords;

	public Guitar() {
		_originalChords = new ArrayList<GuitarChord>();
	}

	public List<GuitarChord> getOriginalChords() {
		return _originalChords;
	}

	/**
	 * @param n 0-15
	 * @param c
	 */
	public void setOriginalChord(int n, GuitarChord c) {
		assert(0<=n && n<_originalChords.size());
		if (c.getChordName()==null) {
			OriginalChord chord = new OriginalChord(n);
			c.setChordName(chord.toString());
		}
		_originalChords.set(n, c);
	}

	public void addOriginalChord(GuitarChord c) {
		if (c.getChordName()==null) {
			OriginalChord chord = new OriginalChord(_originalChords.size());
			c.setChordName(chord.toString());
		}
		_originalChords.add(c);
	}

	public Guitar clone() {
		Guitar copy = new Guitar();
		for (int i=0; i<_originalChords.size(); i++) {
			copy.addOriginalChord(_originalChords.get(i).clone());
		}
		return copy;
	}

}
