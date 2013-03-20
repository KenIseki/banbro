package banbro.model.bdx;

public interface Tone {

	int getToneAttack();
	int getToneDecay();
	int getToneSustain();
	int getToneRelease();
	VibratoShape getVibratoShape();
	int getVibratoHold();
	int getVibratoDelay();
	int getVibratoDepth();
	int getVibratoSpeed();
	EffectType getToneEffectType();
	int getToneEffectValue();

	void setToneAttack(int attack);
	void setToneDecay(int decay);
	void setToneSustain(int sustain);
	void setToneRelease(int release);
	void setVibratoShape(VibratoShape shape);
	void setVibratoHold(int hold);
	void setVibratoDelay(int delay);
	void setVibratoDepth(int depth);
	void setVibratoSpeed(int speed);
	void setToneEffectType(EffectType type);
	void setToneEffectValue(int value);

}
