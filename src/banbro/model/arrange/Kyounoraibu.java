package banbro.model.arrange;

import static banbro.model.arrange.PerformanceStyle.ARABIAN;
import static banbro.model.arrange.PerformanceStyle.CHURCH_MUSIC;
import static banbro.model.arrange.PerformanceStyle.HARD_ROCK;
import static banbro.model.arrange.PerformanceStyle.HORROR;
import static banbro.model.arrange.PerformanceStyle.JAPANESE;
import static banbro.model.arrange.PerformanceStyle.JAZZ;
import static banbro.model.arrange.PerformanceStyle.MUSIC_BOX;
import static banbro.model.arrange.PerformanceStyle.NES;
import static banbro.model.arrange.PerformanceStyle.NOBLE;
import static banbro.model.arrange.PerformanceStyle.OKINAWA;
import static banbro.model.arrange.PerformanceStyle.ORCHESTRA;
import static banbro.model.arrange.PerformanceStyle.ORIGINAL;
import static banbro.model.arrange.PerformanceStyle.PIANO;
import static banbro.model.arrange.PerformanceStyle.TROPICAL;

import java.util.Calendar;

/**
 * ÉoÉìÉuÉâDXÇÃÅuç°ì˙ÇÃÉâÉCÉuÅvÇ≈ëIÇŒÇÍÇÈââëtÉCÉÅÅ[ÉWÇ≈ÉAÉåÉìÉWÇ∑ÇÈ
 */
public class Kyounoraibu extends ArrangeWithCalendar {

	public Kyounoraibu() {
		super(false, true);  // ÉeÉìÉ|ïœçXÇ»ÇµÅïBDXÇÃîÕàÕì‡
	}
	/** ïœçXïsâ¬ */
	@Override
	public synchronized final void setIsArrangeTempo(boolean isArrangeTempo) {}
	/** ïœçXïsâ¬ */
	@Override
	public synchronized final void setIsBDX(boolean isBDX) {}

	@Override
	protected PerformanceStyle getStyleWithCalendar(Calendar calendar) {
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		switch (month) {
		case 1:  // ÇPåé
			switch (day) {
			case  1: return JAPANESE;
			case  2: return MUSIC_BOX;
			case  3: return NES;
			case  4: return ORCHESTRA;
			case  5: return TROPICAL;
			case  6: return HARD_ROCK;
			case  7: return ORCHESTRA;
			case  8: return NES;
			case  9: return JAZZ;
			case 10: return HARD_ROCK;
			case 11: return JAPANESE;
			case 12: return TROPICAL;
			case 13: return PIANO;
			case 14: return HARD_ROCK;
			case 15: return JAPANESE;
			case 16: return JAZZ;
			case 17: return PIANO;
			case 18: return NES;
			case 19: return JAPANESE;
			case 20: return CHURCH_MUSIC;
			case 21: return HARD_ROCK;
			case 22: return NOBLE;
			case 23: return TROPICAL;
			case 24: return ORCHESTRA;
			case 25: return HARD_ROCK;
			case 26: return MUSIC_BOX;
			case 27: return ORCHESTRA;
			case 28: return JAPANESE;
			case 29: return MUSIC_BOX;
			case 30: return JAPANESE;
			case 31: return ORCHESTRA;
			default: break;
			}
			break;
		case 2:  // ÇQåé
			switch (day) {
			case  1: return NES;
			case  2: return HARD_ROCK;
			case  3: return JAPANESE;
			case  4: return TROPICAL;
			case  5: return HARD_ROCK;
			case  6: return JAPANESE;
			case  7: return OKINAWA;
			case  8: return HARD_ROCK;
			case  9: return ARABIAN;
			case 10: return NOBLE;
			case 11: return JAPANESE;
			case 12: return TROPICAL;
			case 13: return PIANO;
			case 14: return CHURCH_MUSIC;
			case 15: return HARD_ROCK;
			case 16: return TROPICAL;
			case 17: return ORCHESTRA;
			case 18: return MUSIC_BOX;
			case 19: return NES;
			case 20: return JAPANESE;
			case 21: return HARD_ROCK;
			case 22: return PIANO;
			case 23: return ORCHESTRA;
			case 24: return MUSIC_BOX;
			case 25: return NOBLE;
			case 26: return PIANO;
			case 27: return JAPANESE;
			case 28: return HARD_ROCK;
			case 29: return HORROR;
			default: break;
			}
			break;
		case 3:  // ÇRåé
			switch (day) {
			case  1: return JAZZ;
			case  2: return HARD_ROCK;
			case  3: return JAPANESE;
			case  4: return MUSIC_BOX;
			case  5: return TROPICAL;
			case  6: return CHURCH_MUSIC;
			case  7: return HARD_ROCK;
			case  8: return TROPICAL;
			case  9: return JAPANESE;
			case 10: return ORCHESTRA;
			case 11: return CHURCH_MUSIC;
			case 12: return NES;
			case 13: return NOBLE;
			case 14: return JAZZ;
			case 15: return TROPICAL;
			case 16: return PIANO;
			case 17: return NES;
			case 18: return ORCHESTRA;
			case 19: return HARD_ROCK;
			case 20: return NES;
			case 21: return CHURCH_MUSIC;
			case 22: return JAPANESE;
			case 23: return TROPICAL;
			case 24: return HARD_ROCK;
			case 25: return NES;
			case 26: return CHURCH_MUSIC;
			case 27: return MUSIC_BOX;
			case 28: return JAPANESE;
			case 29: return ORCHESTRA;
			case 30: return TROPICAL;
			case 31: return PIANO;
			default: break;
			}
			break;
		case 4:  // ÇSåé
			switch (day) {
			case  1: return NES;
			case  2: return MUSIC_BOX;
			case  3: return JAPANESE;
			case  4: return CHURCH_MUSIC;
			case  5: return HARD_ROCK;
			case  6: return ORCHESTRA;
			case  7: return PIANO;
			case  8: return JAPANESE;
			case  9: return ORCHESTRA;
			case 10: return HARD_ROCK;
			case 11: return NES;
			case 12: return ORCHESTRA;
			case 13: return JAPANESE;
			case 14: return TROPICAL;
			case 15: return CHURCH_MUSIC;
			case 16: return JAPANESE;
			case 17: return HARD_ROCK;
			case 18: return CHURCH_MUSIC;
			case 19: return PIANO;
			case 20: return NES;
			case 21: return JAPANESE;
			case 22: return OKINAWA;
			case 23: return ORCHESTRA;
			case 24: return HARD_ROCK;
			case 25: return PIANO;
			case 26: return JAZZ;
			case 27: return HARD_ROCK;
			case 28: return MUSIC_BOX;
			case 29: return ORCHESTRA;
			case 30: return PIANO;
			default: break;
			}
			break;
		case 5:  // ÇTåé
			switch (day) {
			case  1: return HARD_ROCK;
			case  2: return NOBLE;
			case  3: return ORCHESTRA;
			case  4: return CHURCH_MUSIC;
			case  5: return NES;
			case  6: return NOBLE;
			case  7: return ORCHESTRA;
			case  8: return MUSIC_BOX;
			case  9: return NES;
			case 10: return TROPICAL;
			case 11: return HARD_ROCK;
			case 12: return NES;
			case 13: return PIANO;
			case 14: return OKINAWA;
			case 15: return CHURCH_MUSIC;
			case 16: return JAZZ;
			case 17: return NES;
			case 18: return ORCHESTRA;
			case 19: return TROPICAL;
			case 20: return NES;
			case 21: return JAPANESE;
			case 22: return NOBLE;
			case 23: return MUSIC_BOX;
			case 24: return PIANO;
			case 25: return JAZZ;
			case 26: return NES;
			case 27: return TROPICAL;
			case 28: return HARD_ROCK;
			case 29: return MUSIC_BOX;
			case 30: return ORCHESTRA;
			case 31: return PIANO;
			default: break;
			}
			break;
		case 6:  // ÇUåé
			switch (day) {
			case  1: return TROPICAL;
			case  2: return NOBLE;
			case  3: return NES;
			case  4: return PIANO;
			case  5: return ORCHESTRA;
			case  6: return JAPANESE;
			case  7: return CHURCH_MUSIC;
			case  8: return ORCHESTRA;
			case  9: return HARD_ROCK;
			case 10: return NES;
			case 11: return MUSIC_BOX;
			case 12: return CHURCH_MUSIC;
			case 13: return HARD_ROCK;
			case 14: return ORCHESTRA;
			case 15: return PIANO;
			case 16: return JAZZ;
			case 17: return JAPANESE;
			case 18: return HARD_ROCK;
			case 19: return ARABIAN;
			case 20: return NES;
			case 21: return TROPICAL;
			case 22: return MUSIC_BOX;
			case 23: return PIANO;
			case 24: return HARD_ROCK;
			case 25: return NOBLE;
			case 26: return ORCHESTRA;
			case 27: return NES;
			case 28: return JAPANESE;
			case 29: return CHURCH_MUSIC;
			case 30: return HARD_ROCK;
			default: break;
			}
			break;
		case 7:  // ÇVåé
			switch (day) {
			case  1: return TROPICAL;
			case  2: return PIANO;
			case  3: return JAZZ;
			case  4: return ORCHESTRA;
			case  5: return OKINAWA;
			case  6: return HARD_ROCK;
			case  7: return MUSIC_BOX;
			case  8: return NES;
			case  9: return CHURCH_MUSIC;
			case 10: return JAPANESE;
			case 11: return ORCHESTRA;
			case 12: return PIANO;
			case 13: return OKINAWA;
			case 14: return MUSIC_BOX;
			case 15: return NES;
			case 16: return NOBLE;
			case 17: return JAPANESE;
			case 18: return TROPICAL;
			case 19: return PIANO;
			case 20: return ORCHESTRA;
			case 21: return OKINAWA;
			case 22: return TROPICAL;
			case 23: return HARD_ROCK;
			case 24: return CHURCH_MUSIC;
			case 25: return NES;
			case 26: return JAZZ;
			case 27: return ORCHESTRA;
			case 28: return OKINAWA;
			case 29: return TROPICAL;
			case 30: return HARD_ROCK;
			case 31: return JAPANESE;
			default: break;
			}
			break;
		case 8:  // ÇWåé
			switch (day) {
			case  1: return JAZZ;
			case  2: return NOBLE;
			case  3: return ORCHESTRA;
			case  4: return PIANO;
			case  5: return NES;
			case  6: return MUSIC_BOX;
			case  7: return JAPANESE;
			case  8: return NES;
			case  9: return MUSIC_BOX;
			case 10: return ORCHESTRA;
			case 11: return NOBLE;
			case 12: return HARD_ROCK;
			case 13: return JAPANESE;
			case 14: return PIANO;
			case 15: return MUSIC_BOX;
			case 16: return JAPANESE;
			case 17: return PIANO;
			case 18: return TROPICAL;
			case 19: return HARD_ROCK;
			case 20: return NES;
			case 21: return JAZZ;
			case 22: return NES;
			case 23: return NOBLE;
			case 24: return JAPANESE;
			case 25: return ORCHESTRA;
			case 26: return OKINAWA;
			case 27: return MUSIC_BOX;
			case 28: return ORCHESTRA;
			case 29: return TROPICAL;
			case 30: return JAPANESE;
			case 31: return HARD_ROCK;
			default: break;
			}
			break;
		case 9:  // ÇXåé
			switch (day) {
			case  1: return PIANO;
			case  2: return NES;
			case  3: return HARD_ROCK;
			case  4: return JAPANESE;
			case  5: return MUSIC_BOX;
			case  6: return ORCHESTRA;
			case  7: return NOBLE;
			case  8: return PIANO;
			case  9: return JAPANESE;
			case 10: return NES;
			case 11: return HARD_ROCK;
			case 12: return ORCHESTRA;
			case 13: return NOBLE;
			case 14: return MUSIC_BOX;
			case 15: return JAPANESE;
			case 16: return ORCHESTRA;
			case 17: return HARD_ROCK;
			case 18: return JAPANESE;
			case 19: return NOBLE;
			case 20: return TROPICAL;
			case 21: return NES;
			case 22: return ORCHESTRA;
			case 23: return HARD_ROCK;
			case 24: return TROPICAL;
			case 25: return JAZZ;
			case 26: return JAPANESE;
			case 27: return HARD_ROCK;
			case 28: return NOBLE;
			case 29: return CHURCH_MUSIC;
			case 30: return PIANO;
			default: break;
			}
			break;
		case 10:  // ÇPÇOåé
			switch (day) {
			case  1: return MUSIC_BOX;
			case  2: return JAZZ;
			case  3: return ORCHESTRA;
			case  4: return PIANO;
			case  5: return JAPANESE;
			case  6: return ORCHESTRA;
			case  7: return NES;
			case  8: return MUSIC_BOX;
			case  9: return PIANO;
			case 10: return HARD_ROCK;
			case 11: return JAZZ;
			case 12: return ORCHESTRA;
			case 13: return NOBLE;
			case 14: return NES;
			case 15: return CHURCH_MUSIC;
			case 16: return JAPANESE;
			case 17: return MUSIC_BOX;
			case 18: return NES;
			case 19: return HARD_ROCK;
			case 20: return PIANO;
			case 21: return JAZZ;
			case 22: return JAPANESE;
			case 23: return NES;
			case 24: return NOBLE;
			case 25: return MUSIC_BOX;
			case 26: return HARD_ROCK;
			case 27: return JAZZ;
			case 28: return NES;
			case 29: return PIANO;
			case 30: return CHURCH_MUSIC;
			case 31: return HARD_ROCK;
			default: break;
			}
			break;
		case 11:  // ÇPÇPåé
			switch (day) {
			case  1: return TROPICAL;
			case  2: return MUSIC_BOX;
			case  3: return PIANO;
			case  4: return ORCHESTRA;
			case  5: return JAZZ;
			case  6: return NOBLE;
			case  7: return ORCHESTRA;
			case  8: return NES;
			case  9: return CHURCH_MUSIC;
			case 10: return HARD_ROCK;
			case 11: return MUSIC_BOX;
			case 12: return PIANO;
			case 13: return JAZZ;
			case 14: return MUSIC_BOX;
			case 15: return JAPANESE;
			case 16: return ORCHESTRA;
			case 17: return JAPANESE;
			case 18: return JAZZ;
			case 19: return MUSIC_BOX;
			case 20: return TROPICAL;
			case 21: return NES;
			case 22: return PIANO;
			case 23: return MUSIC_BOX;
			case 24: return OKINAWA;
			case 25: return HARD_ROCK;
			case 26: return JAPANESE;
			case 27: return ORCHESTRA;
			case 28: return NES;
			case 29: return NOBLE;
			case 30: return PIANO;
			default: break;
			}
			break;
		case 12:  // ÇPÇQåé
			switch (day) {
			case  1: return JAZZ;
			case  2: return ORCHESTRA;
			case  3: return PIANO;
			case  4: return MUSIC_BOX;
			case  5: return HARD_ROCK;
			case  6: return NOBLE;
			case  7: return NES;
			case  8: return HARD_ROCK;
			case  9: return JAZZ;
			case 10: return ORCHESTRA;
			case 11: return JAPANESE;
			case 12: return HARD_ROCK;
			case 13: return MUSIC_BOX;
			case 14: return NOBLE;
			case 15: return PIANO;
			case 16: return ORCHESTRA;
			case 17: return NOBLE;
			case 18: return JAZZ;
			case 19: return JAPANESE;
			case 20: return HARD_ROCK;
			case 21: return JAPANESE;
			case 22: return ORCHESTRA;
			case 23: return NES;
			case 24: return CHURCH_MUSIC;
			case 25: return CHURCH_MUSIC;
			case 26: return HARD_ROCK;
			case 27: return PIANO;
			case 28: return MUSIC_BOX;
			case 29: return TROPICAL;
			case 30: return NES;
			case 31: return ORCHESTRA;
			default: break;
			}
			break;
		default:
			break;
		}
		return ORIGINAL;
	}

}
