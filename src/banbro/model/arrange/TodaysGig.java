package banbro.model.arrange;

import static banbro.model.arrange.PerformanceStyle.ARABIAN;
import static banbro.model.arrange.PerformanceStyle.CHURCH_MUSIC;
import static banbro.model.arrange.PerformanceStyle.HARD_ROCK;
import static banbro.model.arrange.PerformanceStyle.HORROR;
import static banbro.model.arrange.PerformanceStyle.JAZZ;
import static banbro.model.arrange.PerformanceStyle.MUSIC_BOX;
import static banbro.model.arrange.PerformanceStyle.NES;
import static banbro.model.arrange.PerformanceStyle.NEW_AGE;
import static banbro.model.arrange.PerformanceStyle.NOBLE;
import static banbro.model.arrange.PerformanceStyle.ORCHESTRA;
import static banbro.model.arrange.PerformanceStyle.PIANO;
import static banbro.model.arrange.PerformanceStyle.TROPICAL;

import java.util.Calendar;

/**
 * Jam with the Band の「Today's Gig」で選ばれる演奏イメージでアレンジする
 * 住んでいる国、言語によって変わるかもしれない？
 */
public class TodaysGig extends Kyounoraibu {

	public TodaysGig() {
		super();
	}

	@Override
	protected PerformanceStyle getStyleWithCalendar(Calendar calendar) {
		// 日本版との差分のみ
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		switch (month) {
		case 1:
			switch (day) {
			case  1: return ORCHESTRA;
			case  4: return JAZZ;
			case  6: return MUSIC_BOX;
			case 15: return ARABIAN;
			case 28: return NEW_AGE;
			case 29: return HARD_ROCK;
			case 31: return PIANO;
			default: break;
			}
			break;
		case 2:
			switch (day) {
			case  6: return TROPICAL;
			case  7: return NEW_AGE;
			case 11: return HARD_ROCK;
			case 14: return MUSIC_BOX;
			case 20: return NEW_AGE;
			case 27: return JAZZ;
			default: break;
			}
			break;
		case 3:
			switch (day) {
			case  9: return ARABIAN;
			case 26: return NOBLE;
			case 28: return MUSIC_BOX;
			case 30: return NOBLE;
			default: break;
			}
			break;
		case 4:
			switch (day) {
			case  3: return ARABIAN;
			case 13: return NEW_AGE;
			case 18: return NOBLE;
			case 21: return JAZZ;
			case 22: return NEW_AGE;
			case 25: return ORCHESTRA;
			default: break;
			}
			break;
		case 5:
			switch (day) {
			case  9: return ORCHESTRA;
			case 14: return NEW_AGE;
			case 22: return ORCHESTRA;
			case 29: return PIANO;
			default: break;
			}
			break;
		case 6:
			switch (day) {
			case  5: return NEW_AGE;
			case  8: return PIANO;
			case 12: return NOBLE;
			case 17: return NEW_AGE;
			case 21: return HARD_ROCK;
			default: break;
			}
			break;
		case 7:
			switch (day) {
			case  3: return HORROR;
			case  4: return JAZZ;
			case  5: return NEW_AGE;
			case 13: return NEW_AGE;
			case 14: return HARD_ROCK;
			case 17: return NES;
			case 21: return NEW_AGE;
			case 28: return NEW_AGE;
			default: break;
			}
			break;
		case 8:
			switch (day) {
			case  4: return JAZZ;
			case  5: return ARABIAN;
			case  6: return NES;
			case 10: return PIANO;
			case 13: return NES;
			case 15: return HARD_ROCK;
			case 16: return ARABIAN;
			case 22: return PIANO;
			case 26: return NEW_AGE;
			case 27: return PIANO;
			case 30: return NEW_AGE;
			default: break;
			}
			break;
		case 9:
			switch (day) {
			case  7: return HARD_ROCK;
			case  9: return NOBLE;
			case 15: return JAZZ;
			case 16: return NES;
			case 21: return NEW_AGE;
			case 26: return NEW_AGE;
			default: break;
			}
			break;
		case 10:
			switch (day) {
			case  5: return ARABIAN;
			case  6: return JAZZ;
			case 22: return NEW_AGE;
			case 31: return HORROR;
			default: break;
			}
			break;
		case 11:
			switch (day) {
			case  3: return NEW_AGE;
			case 16: return NES;
			case 24: return NEW_AGE;
			case 26: return JAZZ;
			case 27: return HARD_ROCK;
			default: break;
			}
			break;
		case 12:
			switch (day) {
			case  8: return JAZZ;
			case  9: return HARD_ROCK;
			case 14: return HORROR;
			case 19: return ARABIAN;
			case 25: return ORCHESTRA;
			case 26: return CHURCH_MUSIC;
			default: break;
			}
			break;
		default:
			break;
		}
		return super.getStyleWithCalendar(calendar);
	}

}
