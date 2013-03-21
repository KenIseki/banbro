package banbro.model.bdx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import banbro.model.Accidental;
import banbro.model.Pitch;

/**
 * バイナリの操作に関する便利メソッド集
 * 数値とゲーム上の値の相互変換など
 */
public class BinaryUtil {

	public static final char CHAR_NULL = '\u0000';
	public static final char CHAR_TRADE = '\u2122';
	public static final char CHAR_REG = '\u00ae';
	public static final char CHAR_COPY = '\u00a9';

	public static final Map<Integer, Integer> PanMap = new HashMap<Integer, Integer>();
	static {
		PanMap.put(0xC1, -10);
		PanMap.put(0xC7, -9);
		PanMap.put(0xCD, -8);
		PanMap.put(0xD3, -7);
		PanMap.put(0xDA, -6);
		PanMap.put(0xE0, -5);
		PanMap.put(0xE6, -4);
		PanMap.put(0xED, -3);
		PanMap.put(0xF3, -2);
		PanMap.put(0xF9, -1);
		PanMap.put(0x00, 0);
		PanMap.put(0x07, 1);
		PanMap.put(0x0D, 2);
		PanMap.put(0x13, 3);
		PanMap.put(0x1A, 4);
		PanMap.put(0x20, 5);
		PanMap.put(0x26, 6);
		PanMap.put(0x2D, 7);
		PanMap.put(0x33, 8);
		PanMap.put(0x39, 9);
		PanMap.put(0x3F, 10);
	}

	public static final int[] AttackValue = {
		0x0A, 0x14, 0x1E, 0x28, 0x32, 0x3C, 0x46, 0x4B, 0x50, 0x53,
		0x56, 0x59, 0x5B, 0x5D, 0x5E, 0x5F, 0x60, 0x61, 0x62, 0x63
	};

	public static final int[] DecayValue = {
		0x01, 0x02, 0x03, 0x04, 0x05, 0x07, 0x09, 0x0C, 0x0F, 0x12,
		0x16, 0x1A, 0x1E, 0x23, 0x28, 0x32, 0x3C, 0x46, 0x50, 0x63
	};

	public static final int[] SustainValue = {
		0x00, 0x05, 0x08, 0x0B, 0x0E, 0x12, 0x16, 0x1A, 0x1E, 0x23,
		0x28, 0x2D, 0x32, 0x37, 0x3C, 0x41, 0x46, 0x50, 0x5A, 0x63
	};

	public static final int[] ReleaseValue = {
		0x0A, 0x14, 0x19, 0x1E, 0x23, 0x26, 0x29, 0x2C, 0x2F, 0x32,
		0x37, 0x3C, 0x41, 0x46, 0x4B, 0x50, 0x55, 0x5A, 0x5F, 0x63
	};

	public static final int[] HoldValue = {
		0x00, 0x05, 0x0A, 0x0E, 0x11, 0x13, 0x15, 0x17, 0x19, 0x1B,
		0x1D, 0x20, 0x23, 0x28, 0x2D, 0x32, 0x37, 0x3C, 0x46, 0x63
	};

	public static final int[] DelayValue = {
		0x00, 0x02, 0x04, 0x06, 0x08, 0x0A, 0x0C, 0x0E, 0x10, 0x12,
		0x14, 0x17, 0x1A, 0x1E, 0x23, 0x28, 0x32, 0x3C, 0x46, 0x63
	};

	public static final int[] SpeedValue = {
		0x0A, 0x14, 0x1E, 0x22, 0x25, 0x28, 0x2A, 0x2C, 0x2E, 0x30,
		0x32, 0x34, 0x36, 0x38, 0x3A, 0x3C, 0x41, 0x46, 0x50, 0x63
	};

	public static final int[] DepthValue = {
		0x00, 0x05, 0x08, 0x0A, 0x0C, 0x0E, 0x0F, 0x10, 0x11, 0x12,
		0x13, 0x14, 0x16, 0x18, 0x1B, 0x1E, 0x28, 0x32, 0x46, 0x63
	};

	public static final int[] VolumeValue = {
		0x00, 0x02, 0x03, 0x04, 0x06, 0x07, 0x08, 0x09, 0x0B, 0x0C,
		0x0D, 0x0F, 0x10, 0x11, 0x12, 0x14, 0x15, 0x16, 0x18, 0x19,
		0x1A, 0x1B, 0x1D, 0x1E, 0x1F, 0x20, 0x22, 0x23, 0x24, 0x26,
		0x27, 0x28, 0x29, 0x2B, 0x2C, 0x2D, 0x2F, 0x30, 0x31, 0x32,
		0x34, 0x35, 0x36, 0x38, 0x39, 0x3A, 0x3B, 0x3D, 0x3E, 0x3F,
		0x40, 0x42, 0x43, 0x44, 0x46, 0x47, 0x48, 0x49, 0x4B, 0x4C,
		0x4D, 0x4F, 0x50, 0x51, 0x52, 0x54, 0x55, 0x56, 0x58, 0x59,
		0x5A, 0x5B, 0x5D, 0x5E, 0x5F, 0x60, 0x62, 0x63, 0x64, 0x66,
		0x67, 0x68, 0x69, 0x6B, 0x6C, 0x6D, 0x6F, 0x70, 0x71, 0x72,
		0x74, 0x75, 0x76, 0x78, 0x79, 0x7A, 0x7B, 0x7D, 0x7E, 0x7F
	};

	public static String byteToString(List<Integer> list) {
		return byteToString(list, true);
	}

	public static String byteToString(List<Integer> list, boolean nulEnd) {
		if (list.isEmpty()) {
			return null;
		}
		String st = "";
		int b = 0x00;
		int next = list.get(0);
		if (next==0x00 && nulEnd) {
			return null;
		}
		for (int i=0; i<list.size(); i++) {
			b = list.get(i);
			next = ((i+1<list.size()) ? list.get(i+1) : 0x00);
			if (b==0x00 && nulEnd) {
				break;
			}
			st += byteToString(b, next);
			if (b==0x80) {
				i++;
			}
		}
		return st;
	}

	public static String byteToString(int b, int next) {
		switch (b) {
		case 0x00: return "\u0000";  // 歌詞改ページ
		case 0x01: return "";
		case 0x02: return "";
		case 0x03: return "";
		case 0x04: return "";
		case 0x05: return "";
		case 0x06: return "";
		case 0x07: return "";
		case 0x08: return "";
		case 0x09: return "";
		case 0x0A: return "\n";  // (LF) 改行
		case 0x0B: return "";
		case 0x0C: return "";
		case 0x0D: return "";
		case 0x0E: return "";
		case 0x0F: return "";
		case 0x10: return "";  // CDラベル空欄
		case 0x11: return "";
		case 0x12: return "";
		case 0x13: return "";
		case 0x14: return "";
		case 0x15: return "";
		case 0x16: return "";
		case 0x17: return "";
		case 0x18: return "";
		case 0x19: return "";
		case 0x1A: return "";
		case 0x1B: return "";
		case 0x1C: return "";
		case 0x1D: return "";
		case 0x1E: return "";
		case 0x1F: return "";
		case 0x20: return " ";  // (SPC) スペース &nbsp;
		case 0x21: return "!";
		case 0x22: return "\"";  // 入力不可&quot;
		case 0x23: return "#";
		case 0x24: return "$";
		case 0x25: return "%";
		case 0x26: return "&";  // &amp;
		case 0x27: return "'";  //&apos;  &rsquo;
		case 0x28: return "(";
		case 0x29: return ")";
		case 0x2A: return "*";
		case 0x2B: return "+";
		case 0x2C: return ",";
		case 0x2D: return "-";
		case 0x2E: return ".";
		case 0x2F: return "/";
		case 0x30: return "0";
		case 0x31: return "1";
		case 0x32: return "2";
		case 0x33: return "3";
		case 0x34: return "4";
		case 0x35: return "5";
		case 0x36: return "6";
		case 0x37: return "7";
		case 0x38: return "8";
		case 0x39: return "9";
		case 0x3A: return ":";
		case 0x3B: return ";";
		case 0x3C: return "<";  // &lt;
		case 0x3D: return "=";
		case 0x3E: return ">";  // &gt;
		case 0x3F: return "?";
		case 0x40: return "@";
		case 0x41: return "A";
		case 0x42: return "B";
		case 0x43: return "C";
		case 0x44: return "D";
		case 0x45: return "E";
		case 0x46: return "F";
		case 0x47: return "G";
		case 0x48: return "H";
		case 0x49: return "I";
		case 0x4A: return "J";
		case 0x4B: return "K";
		case 0x4C: return "L";
		case 0x4D: return "M";
		case 0x4E: return "N";
		case 0x4F: return "O";
		case 0x50: return "P";
		case 0x51: return "Q";
		case 0x52: return "R";
		case 0x53: return "S";
		case 0x54: return "T";
		case 0x55: return "U";
		case 0x56: return "V";
		case 0x57: return "W";
		case 0x58: return "X";
		case 0x59: return "Y";
		case 0x5A: return "Z";
		case 0x5B: return "[";
		case 0x5C: return "￥";
		case 0x5D: return "]";
		case 0x5E: return "^";
		case 0x5F: return "_";
		case 0x60: return "`";  // 入力不可&lsquo;
		case 0x61: return "a";
		case 0x62: return "b";
		case 0x63: return "c";
		case 0x64: return "d";
		case 0x65: return "e";
		case 0x66: return "f";
		case 0x67: return "g";
		case 0x68: return "h";
		case 0x69: return "i";
		case 0x6A: return "j";
		case 0x6B: return "k";
		case 0x6C: return "l";
		case 0x6D: return "m";
		case 0x6E: return "n";
		case 0x6F: return "o";
		case 0x70: return "p";
		case 0x71: return "q";
		case 0x72: return "r";
		case 0x73: return "s";
		case 0x74: return "t";
		case 0x75: return "u";
		case 0x76: return "v";
		case 0x77: return "w";
		case 0x78: return "x";
		case 0x79: return "y";
		case 0x7A: return "z";
		case 0x7B: return "{";
		case 0x7C: return "|";  // 入力不可
		case 0x7D: return "}";
		case 0x7E: return "~";
		case 0x7F: return "";
		case 0x80: // 記号フラグ
			switch (next) {
			case 0x40: return "×";
			case 0x41: return "÷";
			case 0x42: return "≠";
			case 0x43: return "→";
			case 0x44: return "↓";
			case 0x45: return "←";
			case 0x46: return "↑";
			case 0x47: return "※";
			case 0x48: return "〒";
			case 0x49: return "♭";
			case 0x4A: return "♪";
			case 0x4B: return "±";
			case 0x4C: return "℃";
			case 0x4D: return "○";
			case 0x4E: return "●";
			case 0x4F: return "◎";
			case 0x50: return "△";
			case 0x51: return "▲";
			case 0x52: return "▽";
			case 0x53: return "▼";
			case 0x54: return "□";
			case 0x55: return "■";
			case 0x56: return "◇";
			case 0x57: return "◆";
			case 0x58: return "☆";
			case 0x59: return "";  //
			case 0x5A: return "°";
			case 0x5B: return "∞";
			case 0x5C: return "∴";
			case 0x5D: return "…";
			case 0x5E: return "\u2122";  // (TM) &trade;
			case 0x5F: return "\u00ae";  // (R) &reg;
			case 0x60: return "♂";
			case 0x61: return "♀";
			case 0x62: return "α";
			case 0x63: return "β";
			case 0x64: return "γ";
			case 0x65: return "π";
			case 0x66: return "Σ";
			case 0x67: return "√";
			case 0x68: return "ゞ";
			default: return "";
			}
		case 0x81: return "";
		case 0x82: return "";
		case 0x83: return "";
		case 0x84: return "";
		case 0x85: return "";
		case 0x86: return "を";
		case 0x87: return "ぁ";
		case 0x88: return "ぃ";
		case 0x89: return "ぅ";
		case 0x8A: return "ぇ";
		case 0x8B: return "ぉ";
		case 0x8C: return "ゃ";
		case 0x8D: return "ゅ";
		case 0x8E: return "ょ";
		case 0x8F: return "っ";
		case 0x90: return "～";
		case 0x91: return "あ";
		case 0x92: return "い";
		case 0x93: return "う";
		case 0x94: return "え";
		case 0x95: return "お";
		case 0x96: return ((next==0xDE) ? "が" : "か");
		case 0x97: return ((next==0xDE) ? "ぎ" : "き");
		case 0x98: return ((next==0xDE) ? "ぐ" : "く");
		case 0x99: return ((next==0xDE) ? "げ" : "け");
		case 0x9A: return ((next==0xDE) ? "ご" : "こ");
		case 0x9B: return ((next==0xDE) ? "ざ" : "さ");
		case 0x9C: return ((next==0xDE) ? "じ" : "し");
		case 0x9D: return ((next==0xDE) ? "ず" : "す");
		case 0x9E: return ((next==0xDE) ? "ぜ" : "せ");
		case 0x9F: return ((next==0xDE) ? "ぞ" : "そ");
		case 0xA0: return "";
		case 0xA1: return "。";
		case 0xA2: return "「";
		case 0xA3: return "」";
		case 0xA4: return "、";
		case 0xA5: return "・";
		case 0xA6: return "ヲ";
		case 0xA7: return "ァ";
		case 0xA8: return "ィ";
		case 0xA9: return "ゥ";
		case 0xAA: return "ェ";
		case 0xAB: return "ォ";
		case 0xAC: return "ャ";
		case 0xAD: return "ュ";
		case 0xAE: return "ョ";
		case 0xAF: return "ッ";
		case 0xB0: return "ー";
		case 0xB1: return "ア";
		case 0xB2: return "イ";
		case 0xB3: return ((next==0xDE) ? "ヴ" : "ウ");
		case 0xB4: return "エ";
		case 0xB5: return "オ";
		case 0xB6: return ((next==0xDE) ? "ガ" : "カ");
		case 0xB7: return ((next==0xDE) ? "ギ" : "キ");
		case 0xB8: return ((next==0xDE) ? "グ" : "ク");
		case 0xB9: return ((next==0xDE) ? "ゲ" : "ケ");
		case 0xBA: return ((next==0xDE) ? "ゴ" : "コ");
		case 0xBB: return ((next==0xDE) ? "ザ" : "サ");
		case 0xBC: return ((next==0xDE) ? "ジ" : "シ");
		case 0xBD: return ((next==0xDE) ? "ズ" : "ス");
		case 0xBE: return ((next==0xDE) ? "ゼ" : "セ");
		case 0xBF: return ((next==0xDE) ? "ゾ" : "ソ");
		case 0xC0: return ((next==0xDE) ? "ダ" : "タ");
		case 0xC1: return ((next==0xDE) ? "ヂ" : "チ");
		case 0xC2: return ((next==0xDE) ? "ヅ" : "ツ");
		case 0xC3: return ((next==0xDE) ? "デ" : "テ");
		case 0xC4: return ((next==0xDE) ? "ド" : "ト");
		case 0xC5: return "ナ";
		case 0xC6: return "ニ";
		case 0xC7: return "ヌ";
		case 0xC8: return "ネ";
		case 0xC9: return "ノ";
		case 0xCA:
			if (next==0xDE) return "バ";
			else if (next==0xDF) return "パ";
			else return "ハ";
		case 0xCB:
			if (next==0xDE) return "ビ";
			else if (next==0xDF) return "ピ";
			else return "ヒ";
		case 0xCC:
			if (next==0xDE) return "ブ";
			else if (next==0xDF) return "プ";
			else return "フ";
		case 0xCD:
			if (next==0xDE) return "ベ";
			else if (next==0xDF) return "ペ";
			else return "ヘ";
		case 0xCE:
			if (next==0xDE) return "ボ";
			else if (next==0xDF) return "ポ";
			else return "ホ";
		case 0xCF: return "マ";
		case 0xD0: return "ミ";
		case 0xD1: return "ム";
		case 0xD2: return "メ";
		case 0xD3: return "モ";
		case 0xD4: return "ヤ";
		case 0xD5: return "ユ";
		case 0xD6: return "ヨ";
		case 0xD7: return "ラ";
		case 0xD8: return "リ";
		case 0xD9: return "ル";
		case 0xDA: return "レ";
		case 0xDB: return "ロ";
		case 0xDC: return "ワ";
		case 0xDD: return "ン";
		case 0xDE: return "";  // 濁点
		case 0xDF: return "";  // 半濁点
		case 0xE0: return ((next==0xDE) ? "だ" : "た");
		case 0xE1: return ((next==0xDE) ? "ぢ" : "ち");
		case 0xE2: return ((next==0xDE) ? "づ" : "つ");
		case 0xE3: return ((next==0xDE) ? "で" : "て");
		case 0xE4: return ((next==0xDE) ? "ど" : "と");
		case 0xE5: return "な";
		case 0xE6: return "に";
		case 0xE7: return "ぬ";
		case 0xE8: return "ね";
		case 0xE9: return "の";
		case 0xEA:
			if (next==0xDE) return "ば";
			else if (next==0xDF) return "ぱ";
			else return "は";
		case 0xEB:
			if (next==0xDE) return "び";
			else if (next==0xDF) return "ぴ";
			else return "ひ";
		case 0xEC:
			if (next==0xDE) return "ぶ";
			else if (next==0xDF) return "ぷ";
			else return "ふ";
		case 0xED:
			if (next==0xDE) return "べ";
			else if (next==0xDF) return "ぺ";
			else return "へ";
		case 0xEE:
			if (next==0xDE) return "ぼ";
			else if (next==0xDF) return "ぽ";
			else return "ほ";
		case 0xEF: return "ま";
		case 0xF0: return "み";
		case 0xF1: return "む";
		case 0xF2: return "め";
		case 0xF3: return "も";
		case 0xF4: return "や";
		case 0xF5: return "ゆ";
		case 0xF6: return "よ";
		case 0xF7: return "ら";
		case 0xF8: return "り";
		case 0xF9: return "る";
		case 0xFA: return "れ";
		case 0xFB: return "ろ";
		case 0xFC: return "わ";
		case 0xFD: return "ん";
		case 0xFE: return "★";
		case 0xFF: return "\u00a9";  // (C) &copy;
		default: return "";
		}
	}

	public static String to2ByteString(String st) {
		if (st==null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : st.toCharArray()) {
			switch (c) {
			case ' ': sb.append('　');
			break;
			case '!': sb.append('！');
			break;
			case '"': sb.append('”');  // 入力不可&quot;
			break;
			case '#': sb.append('＃');
			break;
			case '$': sb.append('＄');
			break;
			case '%': sb.append('％');
			break;
			case '&': sb.append('＆');  // &amp;
			break;
			case '\'': sb.append('’');  //&apos;  &rsquo;
			break;
			case '(': sb.append('（');
			break;
			case ')': sb.append('）');
			break;
			case '*': sb.append('＊');
			break;
			case '+': sb.append('＋');
			break;
			case ',': sb.append('，');
			break;
			case '-': sb.append('－');
			break;
			case '.': sb.append('．');
			break;
			case '/': sb.append('／');
			break;
			case '0': sb.append('０');
			break;
			case '1': sb.append('１');
			break;
			case '2': sb.append('２');
			break;
			case '3': sb.append('３');
			break;
			case '4': sb.append('４');
			break;
			case '5': sb.append('５');
			break;
			case '6': sb.append('６');
			break;
			case '7': sb.append('７');
			break;
			case '8': sb.append('８');
			break;
			case '9': sb.append('９');
			break;
			case ':': sb.append('：');
			break;
			case ';': sb.append('；');
			break;
			case '<': sb.append('＜');  // &lt;
			break;
			case '=': sb.append('＝');
			break;
			case '>': sb.append('＞');  // &gt;
			break;
			case '?': sb.append('？');
			break;
			case '@': sb.append('＠');
			break;
			case 'A': sb.append('Ａ');
			break;
			case 'B': sb.append('Ｂ');
			break;
			case 'C': sb.append('Ｃ');
			break;
			case 'D': sb.append('Ｄ');
			break;
			case 'E': sb.append('Ｅ');
			break;
			case 'F': sb.append('Ｆ');
			break;
			case 'G': sb.append('Ｇ');
			break;
			case 'H': sb.append('Ｈ');
			break;
			case 'I': sb.append('Ｉ');
			break;
			case 'J': sb.append('Ｊ');
			break;
			case 'K': sb.append('Ｋ');
			break;
			case 'L': sb.append('Ｌ');
			break;
			case 'M': sb.append('Ｍ');
			break;
			case 'N': sb.append('Ｎ');
			break;
			case 'O': sb.append('Ｏ');
			break;
			case 'P': sb.append('Ｐ');
			break;
			case 'Q': sb.append('Ｑ');
			break;
			case 'R': sb.append('Ｒ');
			break;
			case 'S': sb.append('Ｓ');
			break;
			case 'T': sb.append('Ｔ');
			break;
			case 'U': sb.append('Ｕ');
			break;
			case 'V': sb.append('Ｖ');
			break;
			case 'W': sb.append('Ｗ');
			break;
			case 'X': sb.append('Ｘ');
			break;
			case 'Y': sb.append('Ｙ');
			break;
			case 'Z': sb.append('Ｚ');
			break;
			case '[': sb.append('［');
			break;
			case '\\': sb.append('￥');
			break;
			case ']': sb.append('］');
			break;
			case '^': sb.append('＾');
			break;
			case '_': sb.append('＿');
			break;
			case '`': sb.append('‘');  // 入力不可&lsquo;
			break;
			case 'a': sb.append('ａ');
			break;
			case 'b': sb.append('ｂ');
			break;
			case 'c': sb.append('ｃ');
			break;
			case 'd': sb.append('ｄ');
			break;
			case 'e': sb.append('ｅ');
			break;
			case 'f': sb.append('ｆ');
			break;
			case 'g': sb.append('ｇ');
			break;
			case 'h': sb.append('ｈ');
			break;
			case 'i': sb.append('ｉ');
			break;
			case 'j': sb.append('ｊ');
			break;
			case 'k': sb.append('ｋ');
			break;
			case 'l': sb.append('ｌ');
			break;
			case 'm': sb.append('ｍ');
			break;
			case 'n': sb.append('ｎ');
			break;
			case 'o': sb.append('ｏ');
			break;
			case 'p': sb.append('ｐ');
			break;
			case 'q': sb.append('ｑ');
			break;
			case 'r': sb.append('ｒ');
			break;
			case 's': sb.append('ｓ');
			break;
			case 't': sb.append('ｔ');
			break;
			case 'u': sb.append('ｕ');
			break;
			case 'v': sb.append('ｖ');
			break;
			case 'w': sb.append('ｗ');
			break;
			case 'x': sb.append('ｘ');
			break;
			case 'y': sb.append('ｙ');
			break;
			case 'z': sb.append('ｚ');
			break;
			case '{': sb.append('｛');
			break;
			case '|': sb.append('｜');  // 入力不可
			break;
			case '}': sb.append('｝');
			break;
			default: sb.append(c);
			break;
			}
		}
		return sb.toString();
	}

	public static int byteToInt(int b) {
		return b/16*10 + b%16;
	}

	public static int byteToPan(int b) {
		Integer p;
		do {
			p = PanMap.get(b);
			if (b<0x80) {
				b--;
				if (b<0x00) {
					return 0;
				}
			} else {
				b++;
				if (b>0xFF) {
					return 0;
				}
			}
		} while (p==null);
		return p.intValue();
	}

	private static int byteToIndex(int[] array, int b) {
		int index = -1;
		for (int i=0; i<array.length; i++) {
			if (b<=array[i]) {
				index = i;
				break;
			}
		}
		if (index==-1) {
			return array.length - 1;
		}
		if (index!=0) {
			int d1 = array[index] - b;
			int d2 = b - array[index-1];
			if (d1>d2) {
				index--;
			}
		}
		return index;
	}

	public static int byteToAttack(int b) {
		return byteToIndex(AttackValue, b);
	}

	public static int byteToDecay(int b) {
		return byteToIndex(DecayValue, b);
	}

	public static int byteToSustain(int b) {
		return byteToIndex(SustainValue, b);
	}

	public static int byteToRelease(int b) {
		return byteToIndex(ReleaseValue, b);
	}

	public static int byteToHold(int b) {
		return byteToIndex(HoldValue, b);
	}

	public static int byteToDelay(int b) {
		return byteToIndex(DelayValue, b);
	}

	public static int byteToDepth(int b) {
		return byteToIndex(DepthValue, b);
	}

	public static int byteToSpeed(int b) {
		return byteToIndex(SpeedValue, b);
	}

	public static int byteToVolume(int b) {
		int i = Arrays.binarySearch(VolumeValue, b);
		if (i<0) {
			return b * 99 / 127;
		}
		return i;
	}

	public static List<Integer> to2byteBinary(int value) {
		List<Integer> bin = new ArrayList<Integer>();
		bin.add(value&0xFF);
		bin.add((value&0xFF00)>>8);
		return bin;
	}

	public static List<Integer> to4byteBinary(long value) {
		List<Integer> bin = new ArrayList<Integer>();
		bin.add((int)(value&0xFF));
		bin.add((int)((value&0xFF00)>>8));
		bin.add((int)((value&0xFF0000)>>16));
		bin.add((int)((value&0xFF000000l)>>24));
		return bin;
	}

	public static int[] getChordBinary(Chord c) {
		int[] binary = new int[2];
		if (c instanceof BaseChord) {
			binary[0] = ((BaseChord)c).getName().getValue();
			binary[1] = ((BaseChord)c).getAccidental().getValue()*16 + ((BaseChord)c).getRoot().getValue();
		} else if (c instanceof OriginalChord) {
			binary[0] = ((OriginalChord)c).getNum();
			binary[1] = 0x0F;
		} else {
			binary[0] = 0xFF;
			binary[1] = 0xFF;
		}
		return binary;
	}

	public static void arrangeGuitarButtonBinary(List<Integer> binary, Part p) {
		// ギター:ボタン割り当て
//		Guitar guitar = bdx.getGuitar();
//		if (guitar!=null) {
		if (p.getType()==InstrumentType.GUITAR) {
			List<StepValue<List<Chord>>> gButton = p.getChordSet();
			assert(gButton.size()<=32);
			// 0x4B08 ギターボタン割り当て
			//    (位置,FFFF,↓←↑→,FFFF,L↓←↑→,FFFF)の繰り返し(2*12)
			for (int i=0; i<gButton.size(); i++) {
				StepValue<List<Chord>> sv = gButton.get(i);
//				int[] stepBinary = get2ByteBinary(sv.getStep());
				List<Integer> stepBinary = to2byteBinary(sv.getStep());
				binary.set(0x4B08 + 24*i, stepBinary.get(0));
				binary.set(0x4B09 + 24*i, stepBinary.get(1));
				List<Chord> chordList = new ArrayList<Chord>(sv.getValue());
				while (chordList.size()<8) {
					chordList.add(NonChord.getInstance());
				}
				for (int j=0; j<4; j++) {
					int[] chordBinary = getChordBinary(chordList.get(j));
					binary.set(0x4B0C + 24*i + 2*j, chordBinary[0]);
					binary.set(0x4B0D + 24*i + 2*j, chordBinary[1]);
				}
				for (int j=0; j<4; j++) {
					int[] chordBinary = getChordBinary(chordList.get(j+4));
					binary.set(0x4B16 + 24*i + 2*j, chordBinary[0]);
					binary.set(0x4B17 + 24*i + 2*j, chordBinary[1]);
				}
			}
		}
	}

	public static void arrangePianoButtonBinary(List<Integer> binary, Part p) {
		List<Chord> button = p.getChordSet(0);
		assert(button.size()==32);
		for (int bi=0; bi<button.size(); bi++) {
			int[] chordBinary = BinaryUtil.getChordBinary(button.get(bi));
			binary.set(0x6608, chordBinary[0]);
			binary.set(0x6609, chordBinary[1]);
		}
	}

	/**
	 * GBALZSS圧縮されているデータを解凍する
	 * @param binary 圧縮されているデータ
	 * @return 解凍したデータ
	 */
	public static List<Integer> decodeBDXBinary(List<Integer> binary) {
		if (binary==null || binary.size()<4) {
			throw new IllegalArgumentException("データのサイズが小さすぎます");
		}
		Iterator<Integer> ite=binary.iterator();
		int[] header = new int[] {ite.next(), ite.next(), ite.next(), ite.next()};
		if (Arrays.equals(header, new int[] {0x10, 0x60, 0x7D, 0x00}) == false) {
			throw new IllegalArgumentException("ヘッダがありません");
		}
		List<Integer> decodeB = new LinkedList<Integer>();
		int bufPointer = 0;
		int bufSize = 0x1000;
		int[] q = new int[bufSize];
		out: for ( ; ite.hasNext() ;) {  // 1セクション
			int info = ite.next();
			for (int j=0; j<8; j++) {  // 1回の圧縮情報で8回の処理
				if (ite.hasNext()==false) {break out;}
				if ((info & (0x80>>j)) != 0) {  // 圧縮されている？
					int[] info2 = new int[2];
					info2[0] = ite.next();
					if (ite.hasNext()==false) {break out;}
					info2[1] = ite.next();
					int len = ((info2[0] & 0xF0) >> 4) + 3;
					int prev = ((info2[0] & 0x0F) << 8) + info2[1] + 1;
					for (int k=0; k<len; k++) {
						int index = (bufSize + bufPointer - prev) & 0x0FFF;
						int value = q[index];
						decodeB.add(value);
						q[bufPointer++] = value;
						if (bufPointer>=bufSize) {bufPointer=0;}
					}
				} else {
					int value = ite.next();
					decodeB.add(value);
					q[bufPointer++] = value;
					if (bufPointer>=bufSize) {bufPointer=0;}
				}
			}
		}
		int addSize = decodeB.size() - (0x8000-0x01E8);
		for (int i=0; i<addSize; i++) {
			decodeB.add(0x00);
		}
		return decodeB;
	}

	public static List<Integer> stringToByte(String st) {
		List<Integer> binary = new ArrayList<Integer>();
		char c;
		for (int i=0; i<st.length(); i++) {
			c = st.charAt(i);
			binary.addAll(charToByte(c));
		}
		return binary;
	}

	/**
	 * @param lyric
	 * @return
	 */
	public static List<Integer> charToByte(char c) {
		List<Integer> binary = new ArrayList<Integer>();
			switch (c) {
			case CHAR_NULL:
				binary.add(0x00);
				break;
			case '\n':
				binary.add(0x0A);
				break;
			case ' ':
			case '　':
				binary.add(0x20);
				break;
			case '!':
			case '！':
				binary.add(0x21);
				break;
			case '"':  // 入力不可
			case '”':
				binary.add(0x22);
				break;
			case '#':
			case '＃':
				binary.add(0x23);
				break;
			case '$':
			case '＄':
				binary.add(0x24);
				break;
			case '%':
			case '％':
				binary.add(0x25);
				break;
			case '&':
			case '＆':
				binary.add(0x26);
				break;
			case '\'':
			case '’':
				binary.add(0x27);
				break;
			case '(':
			case '（':
				binary.add(0x28);
				break;
			case ')':
			case '）':
				binary.add(0x29);
				break;
			case '*':
			case '＊':
				binary.add(0x2A);
				break;
			case '+':
			case '＋':
				binary.add(0x2B);
				break;
			case ',':
			case '，':
				binary.add(0x2C);
				break;
			case '-':
			case '－':
				binary.add(0x2D);
				break;
			case '.':
			case '．':
				binary.add(0x2E);
				break;
			case '/':
			case '／':
				binary.add(0x2F);
				break;
			case '0':
			case '０':
				binary.add(0x30);
				break;
			case '1':
			case '１':
				binary.add(0x31);
				break;
			case '2':
			case '２':
				binary.add(0x32);
				break;
			case '3':
			case '３':
				binary.add(0x33);
				break;
			case '4':
			case '４':
				binary.add(0x34);
				break;
			case '5':
			case '５':
				binary.add(0x35);
				break;
			case '6':
			case '６':
				binary.add(0x36);
				break;
			case '7':
			case '７':
				binary.add(0x37);
				break;
			case '8':
			case '８':
				binary.add(0x38);
				break;
			case '9':
			case '９':
				binary.add(0x39);
				break;
			case ':':
			case '：':
				binary.add(0x3A);
				break;
			case ';':
			case '；':
				binary.add(0x3B);
				break;
			case '<':
			case '＜':
				binary.add(0x3C);
				break;
			case '=':
			case '＝':
				binary.add(0x3D);
				break;
			case '>':
			case '＞':
				binary.add(0x3E);
				break;
			case '?':
			case '？':
				binary.add(0x3F);
				break;
			case '@':
			case '＠':
				binary.add(0x40);
				break;
			case 'A':
			case 'Ａ':
				binary.add(0x41);
				break;
			case 'B':
			case 'Ｂ':
				binary.add(0x42);
				break;
			case 'C':
			case 'Ｃ':
				binary.add(0x43);
				break;
			case 'D':
			case 'Ｄ':
				binary.add(0x44);
				break;
			case 'E':
			case 'Ｅ':
				binary.add(0x45);
				break;
			case 'F':
			case 'Ｆ':
				binary.add(0x46);
				break;
			case 'G':
			case 'Ｇ':
				binary.add(0x47);
				break;
			case 'H':
			case 'Ｈ':
				binary.add(0x48);
				break;
			case 'I':
			case 'Ｉ':
				binary.add(0x49);
				break;
			case 'J':
			case 'Ｊ':
				binary.add(0x4A);
				break;
			case 'K':
			case 'Ｋ':
				binary.add(0x4B);
				break;
			case 'L':
			case 'Ｌ':
				binary.add(0x4C);
				break;
			case 'M':
			case 'Ｍ':
				binary.add(0x4D);
				break;
			case 'N':
			case 'Ｎ':
				binary.add(0x4E);
				break;
			case 'O':
			case 'Ｏ':
				binary.add(0x4F);
				break;
			case 'P':
			case 'Ｐ':
				binary.add(0x50);
				break;
			case 'Q':
			case 'Ｑ':
				binary.add(0x51);
				break;
			case 'R':
			case 'Ｒ':
				binary.add(0x52);
				break;
			case 'S':
			case 'Ｓ':
				binary.add(0x53);
				break;
			case 'T':
			case 'Ｔ':
				binary.add(0x54);
				break;
			case 'U':
			case 'Ｕ':
				binary.add(0x55);
				break;
			case 'V':
			case 'Ｖ':
				binary.add(0x56);
				break;
			case 'W':
			case 'Ｗ':
				binary.add(0x57);
				break;
			case 'X':
			case 'Ｘ':
				binary.add(0x58);
				break;
			case 'Y':
			case 'Ｙ':
				binary.add(0x59);
				break;
			case 'Z':
			case 'Ｚ':
				binary.add(0x5A);
				break;
			case '[':
			case '［':
				binary.add(0x5B);
				break;
			case '\\':
			case '￥':
				binary.add(0x5C);
				break;
			case ']':
			case '］':
				binary.add(0x5D);
				break;
			case '^':
			case '＾':
				binary.add(0x5E);
				break;
			case '_':
			case '＿':
				binary.add(0x5F);
				break;
			case '`':
			case '‘':  // 入力不可
				binary.add(0x60);
				break;
			case 'a':
			case 'ａ':
				binary.add(0x61);
				break;
			case 'b':
			case 'ｂ':
				binary.add(0x62);
				break;
			case 'c':
			case 'ｃ':
				binary.add(0x63);
				break;
			case 'd':
			case 'ｄ':
				binary.add(0x64);
				break;
			case 'e':
			case 'ｅ':
				binary.add(0x65);
				break;
			case 'f':
			case 'ｆ':
				binary.add(0x66);
				break;
			case 'g':
			case 'ｇ':
				binary.add(0x67);
				break;
			case 'h':
			case 'ｈ':
				binary.add(0x68);
				break;
			case 'i':
			case 'ｉ':
				binary.add(0x69);
				break;
			case 'j':
			case 'ｊ':
				binary.add(0x6A);
				break;
			case 'k':
			case 'ｋ':
				binary.add(0x6B);
				break;
			case 'l':
			case 'ｌ':
				binary.add(0x6C);
				break;
			case 'm':
			case 'ｍ':
				binary.add(0x6D);
				break;
			case 'n':
			case 'ｎ':
				binary.add(0x6E);
				break;
			case 'o':
			case 'ｏ':
				binary.add(0x6F);
				break;
			case 'p':
			case 'ｐ':
				binary.add(0x70);
				break;
			case 'q':
			case 'ｑ':
				binary.add(0x71);
				break;
			case 'r':
			case 'ｒ':
				binary.add(0x72);
				break;
			case 's':
			case 'ｓ':
				binary.add(0x73);
				break;
			case 't':
			case 'ｔ':
				binary.add(0x74);
				break;
			case 'u':
			case 'ｕ':
				binary.add(0x75);
				break;
			case 'v':
			case 'ｖ':
				binary.add(0x76);
				break;
			case 'w':
			case 'ｗ':
				binary.add(0x77);
				break;
			case 'x':
			case 'ｘ':
				binary.add(0x78);
				break;
			case 'y':
			case 'ｙ':
				binary.add(0x79);
				break;
			case 'z':
			case 'ｚ':
				binary.add(0x7A);
				break;
			case '{':
			case '｛':
				binary.add(0x7B);
				break;
			case '|':
			case '｜':  // 入力不可
				binary.add(0x7C);
				break;
			case '}':
			case '｝':
				binary.add(0x7D);
				break;
			case '~':
				binary.add(0x7E);
				break;
			case 'を':
				binary.add(0x86);
				break;
			case 'ぁ':
				binary.add(0x87);
				break;
			case 'ぃ':
				binary.add(0x88);
				break;
			case 'ぅ':
				binary.add(0x89);
				break;
			case 'ぇ':
				binary.add(0x8A);
				break;
			case 'ぉ':
				binary.add(0x8B);
				break;
			case 'ゃ':
				binary.add(0x8C);
				break;
			case 'ゅ':
				binary.add(0x8D);
				break;
			case 'ょ':
				binary.add(0x8E);
				break;
			case 'っ':
				binary.add(0x8F);
				break;
			case '～':
				binary.add(0x90);
				break;
			case 'あ':
				binary.add(0x91);
				break;
			case 'い':
				binary.add(0x92);
				break;
			case 'う':
				binary.add(0x93);
				break;
			case 'え':
				binary.add(0x94);
				break;
			case 'お':
				binary.add(0x95);
				break;
			case 'か':
				binary.add(0x96);
				break;
			case 'が':
				binary.add(0x96);
				binary.add(0xDE);
				break;
			case 'き':
				binary.add(0x97);
				break;
			case 'ぎ':
				binary.add(0x97);
				binary.add(0xDE);
				break;
			case 'く':
				binary.add(0x98);
				break;
			case 'ぐ':
				binary.add(0x98);
				binary.add(0xDE);
				break;
			case 'け':
				binary.add(0x99);
				break;
			case 'げ':
				binary.add(0x99);
				binary.add(0xDE);
				break;
			case 'こ':
				binary.add(0x9A);
				break;
			case 'ご':
				binary.add(0x9A);
				binary.add(0xDE);
				break;
			case 'さ':
				binary.add(0x9B);
				break;
			case 'ざ':
				binary.add(0x9B);
				binary.add(0xDE);
				break;
			case 'し':
				binary.add(0x9C);
				break;
			case 'じ':
				binary.add(0x9C);
				binary.add(0xDE);
				break;
			case 'す':
				binary.add(0x9D);
				break;
			case 'ず':
				binary.add(0x9D);
				binary.add(0xDE);
				break;
			case 'せ':
				binary.add(0x9E);
				break;
			case 'ぜ':
				binary.add(0x9E);
				binary.add(0xDE);
				break;
			case 'そ':
				binary.add(0x9F);
				break;
			case 'ぞ':
				binary.add(0x9F);
				binary.add(0xDE);
				break;
			case '。':
				binary.add(0xA1);
				break;
			case '「':
				binary.add(0xA2);
				break;
			case '」':
				binary.add(0xA3);
				break;
			case '、':
				binary.add(0xA4);
				break;
			case '・':
				binary.add(0xA5);
				break;
			case 'ヲ':
				binary.add(0xA6);
				break;
			case 'ァ':
				binary.add(0xA7);
				break;
			case 'ィ':
				binary.add(0xA8);
				break;
			case 'ゥ':
				binary.add(0xA9);
				break;
			case 'ェ':
				binary.add(0xAA);
				break;
			case 'ォ':
				binary.add(0xAB);
				break;
			case 'ャ':
				binary.add(0xAC);
				break;
			case 'ュ':
				binary.add(0xAD);
				break;
			case 'ョ':
				binary.add(0xAE);
				break;
			case 'ッ':
				binary.add(0xAF);
				break;
			case 'ー':
				binary.add(0xB0);
				break;
			case 'ア':
				binary.add(0xB1);
				break;
			case 'イ':
				binary.add(0xB2);
				break;
			case 'ウ':
				binary.add(0xB3);
				break;
			case 'ヴ':
				binary.add(0xB3);
				binary.add(0xDE);
				break;
			case 'エ':
				binary.add(0xB4);
				break;
			case 'オ':
				binary.add(0xB5);
				break;
			case 'カ':
				binary.add(0xB6);
				break;
			case 'ガ':
				binary.add(0xB6);
				binary.add(0xDE);
				break;
			case 'キ':
				binary.add(0xB7);
				break;
			case 'ギ':
				binary.add(0xB7);
				binary.add(0xDE);
				break;
			case 'ク':
				binary.add(0xB8);
				break;
			case 'グ':
				binary.add(0xB8);
				binary.add(0xDE);
				break;
			case 'ケ':
				binary.add(0xB9);
				break;
			case 'ゲ':
				binary.add(0xB9);
				binary.add(0xDE);
				break;
			case 'コ':
				binary.add(0xBA);
				break;
			case 'ゴ':
				binary.add(0xBA);
				binary.add(0xDE);
				break;
			case 'サ':
				binary.add(0xBB);
				break;
			case 'ザ':
				binary.add(0xBB);
				binary.add(0xDE);
				break;
			case 'シ':
				binary.add(0xBC);
				break;
			case 'ジ':
				binary.add(0xBC);
				binary.add(0xDE);
				break;
			case 'ス':
				binary.add(0xBD);
				break;
			case 'ズ':
				binary.add(0xBD);
				binary.add(0xDE);
				break;
			case 'セ':
				binary.add(0xBE);
				break;
			case 'ゼ':
				binary.add(0xBE);
				binary.add(0xDE);
				break;
			case 'ソ':
				binary.add(0xBF);
				break;
			case 'ゾ':
				binary.add(0xBF);
				binary.add(0xDE);
				break;
			case 'タ':
				binary.add(0xC0);
				break;
			case 'ダ':
				binary.add(0xC0);
				binary.add(0xDE);
				break;
			case 'チ':
				binary.add(0xC1);
				break;
			case 'ヂ':
				binary.add(0xC1);
				binary.add(0xDE);
				break;
			case 'ツ':
				binary.add(0xC2);
				break;
			case 'ヅ':
				binary.add(0xC2);
				binary.add(0xDE);
				break;
			case 'テ':
				binary.add(0xC3);
				break;
			case 'デ':
				binary.add(0xC3);
				binary.add(0xDE);
				break;
			case 'ト':
				binary.add(0xC4);
				break;
			case 'ド':
				binary.add(0xC4);
				binary.add(0xDE);
				break;
			case 'ナ':
				binary.add(0xC5);
				break;
			case 'ニ':
				binary.add(0xC6);
				break;
			case 'ヌ':
				binary.add(0xC7);
				break;
			case 'ネ':
				binary.add(0xC8);
				break;
			case 'ノ':
				binary.add(0xC9);
				break;
			case 'ハ':
				binary.add(0xCA);
				break;
			case 'バ':
				binary.add(0xCA);
				binary.add(0xDE);
				break;
			case 'パ':
				binary.add(0xCA);
				binary.add(0xDF);
				break;
			case 'ヒ':
				binary.add(0xCB);
				break;
			case 'ビ':
				binary.add(0xCB);
				binary.add(0xDE);
				break;
			case 'ピ':
				binary.add(0xCB);
				binary.add(0xDF);
				break;
			case 'フ':
				binary.add(0xCC);
				break;
			case 'ブ':
				binary.add(0xCC);
				binary.add(0xDE);
				break;
			case 'プ':
				binary.add(0xCC);
				binary.add(0xDF);
				break;
			case 'ヘ':
				binary.add(0xCD);
				break;
			case 'ベ':
				binary.add(0xCD);
				binary.add(0xDE);
				break;
			case 'ペ':
				binary.add(0xCD);
				binary.add(0xDF);
				break;
			case 'ホ':
				binary.add(0xCE);
				break;
			case 'ボ':
				binary.add(0xCE);
				binary.add(0xDE);
				break;
			case 'ポ':
				binary.add(0xCE);
				binary.add(0xDF);
				break;
			case 'マ':
				binary.add(0xCF);
				break;
			case 'ミ':
				binary.add(0xD0);
				break;
			case 'ム':
				binary.add(0xD1);
				break;
			case 'メ':
				binary.add(0xD2);
				break;
			case 'モ':
				binary.add(0xD3);
				break;
			case 'ヤ':
				binary.add(0xD4);
				break;
			case 'ユ':
				binary.add(0xD5);
				break;
			case 'ヨ':
				binary.add(0xD6);
				break;
			case 'ラ':
				binary.add(0xD7);
				break;
			case 'リ':
				binary.add(0xD8);
				break;
			case 'ル':
				binary.add(0xD9);
				break;
			case 'レ':
				binary.add(0xDA);
				break;
			case 'ロ':
				binary.add(0xDB);
				break;
			case 'ワ':
				binary.add(0xDC);
				break;
			case 'ン':
				binary.add(0xDD);
				break;
			case 'た':
				binary.add(0xE0);
				break;
			case 'だ':
				binary.add(0xE0);
				binary.add(0xDE);
				break;
			case 'ち':
				binary.add(0xE1);
				break;
			case 'ぢ':
				binary.add(0xE1);
				binary.add(0xDE);
				break;
			case 'つ':
				binary.add(0xE2);
				break;
			case 'づ':
				binary.add(0xE2);
				binary.add(0xDE);
				break;
			case 'て':
				binary.add(0xE3);
				break;
			case 'で':
				binary.add(0xE3);
				binary.add(0xDE);
				break;
			case 'と':
				binary.add(0xE4);
				break;
			case 'ど':
				binary.add(0xE4);
				binary.add(0xDE);
				break;
			case 'な':
				binary.add(0xE5);
				break;
			case 'に':
				binary.add(0xE6);
				break;
			case 'ぬ':
				binary.add(0xE7);
				break;
			case 'ね':
				binary.add(0xE8);
				break;
			case 'の':
				binary.add(0xE9);
				break;
			case 'は':
				binary.add(0xEA);
				break;
			case 'ば':
				binary.add(0xEA);
				binary.add(0xDE);
				break;
			case 'ぱ':
				binary.add(0xEA);
				binary.add(0xDF);
				break;
			case 'ひ':
				binary.add(0xEB);
				break;
			case 'び':
				binary.add(0xEB);
				binary.add(0xDE);
				break;
			case 'ぴ':
				binary.add(0xEB);
				binary.add(0xDF);
				break;
			case 'ふ':
				binary.add(0xEC);
				break;
			case 'ぶ':
				binary.add(0xEC);
				binary.add(0xDE);
				break;
			case 'ぷ':
				binary.add(0xEC);
				binary.add(0xDF);
				break;
			case 'へ':
				binary.add(0xED);
				break;
			case 'べ':
				binary.add(0xED);
				binary.add(0xDE);
				break;
			case 'ぺ':
				binary.add(0xED);
				binary.add(0xDF);
				break;
			case 'ほ':
				binary.add(0xEE);
				break;
			case 'ぼ':
				binary.add(0xEE);
				binary.add(0xDE);
				break;
			case 'ぽ':
				binary.add(0xEE);
				binary.add(0xDF);
				break;
			case 'ま':
				binary.add(0xEF);
				break;
			case 'み':
				binary.add(0xF0);
				break;
			case 'む':
				binary.add(0xF1);
				break;
			case 'め':
				binary.add(0xF2);
				break;
			case 'も':
				binary.add(0xF3);
				break;
			case 'や':
				binary.add(0xF4);
				break;
			case 'ゆ':
				binary.add(0xF5);
				break;
			case 'よ':
				binary.add(0xF6);
				break;
			case 'ら':
				binary.add(0xF7);
				break;
			case 'り':
				binary.add(0xF8);
				break;
			case 'る':
				binary.add(0xF9);
				break;
			case 'れ':
				binary.add(0xFA);
				break;
			case 'ろ':
				binary.add(0xFB);
				break;
			case 'わ':
				binary.add(0xFC);
				break;
			case 'ん':
				binary.add(0xFD);
				break;
			case '★':
				binary.add(0xFE);
				break;
			case CHAR_COPY:
				binary.add(0xFF);
				break;
			case '×':
				binary.add(0x80);
				binary.add(0x40);
				break;
			case '÷':
				binary.add(0x80);
				binary.add(0x41);
				break;
			case '≠':
				binary.add(0x80);
				binary.add(0x42);
				break;
			case '→':
				binary.add(0x80);
				binary.add(0x43);
				break;
			case '↓':
				binary.add(0x80);
				binary.add(0x44);
				break;
			case '←':
				binary.add(0x80);
				binary.add(0x45);
				break;
			case '↑':
				binary.add(0x80);
				binary.add(0x46);
				break;
			case '※':
				binary.add(0x80);
				binary.add(0x47);
				break;
			case '〒':
				binary.add(0x80);
				binary.add(0x48);
				break;
			case '♭':
				binary.add(0x80);
				binary.add(0x49);
				break;
			case '♪':
				binary.add(0x80);
				binary.add(0x4A);
				break;
			case '±':
				binary.add(0x80);
				binary.add(0x4B);
				break;
			case '℃':
				binary.add(0x80);
				binary.add(0x4C);
				break;
			case '○':
				binary.add(0x80);
				binary.add(0x4D);
				break;
			case '●':
				binary.add(0x80);
				binary.add(0x4E);
				break;
			case '◎':
				binary.add(0x80);
				binary.add(0x4F);
				break;
			case '△':
				binary.add(0x80);
				binary.add(0x50);
				break;
			case '▲':
				binary.add(0x80);
				binary.add(0x51);
				break;
			case '▽':
				binary.add(0x80);
				binary.add(0x52);
				break;
			case '▼':
				binary.add(0x80);
				binary.add(0x53);
				break;
			case '□':
				binary.add(0x80);
				binary.add(0x54);
				break;
			case '■':
				binary.add(0x80);
				binary.add(0x55);
				break;
			case '◇':
				binary.add(0x80);
				binary.add(0x56);
				break;
			case '◆':
				binary.add(0x80);
				binary.add(0x57);
				break;
			case '☆':
				binary.add(0x80);
				binary.add(0x58);
				break;
			case '°':
				binary.add(0x80);
				binary.add(0x5A);
				break;
			case '∞':
				binary.add(0x80);
				binary.add(0x5B);
				break;
			case '∴':
				binary.add(0x80);
				binary.add(0x5C);
				break;
			case '…':
				binary.add(0x80);
				binary.add(0x5D);
				break;
			case CHAR_TRADE:
				binary.add(0x80);
				binary.add(0x5E);
				break;
			case CHAR_REG:
				binary.add(0x80);
				binary.add(0x5F);
				break;
			case '♂':
				binary.add(0x80);
				binary.add(0x60);
				break;
			case '♀':
				binary.add(0x80);
				binary.add(0x61);
				break;
			case 'α':
				binary.add(0x80);
				binary.add(0x62);
				break;
			case 'β':
				binary.add(0x80);
				binary.add(0x63);
				break;
			case 'γ':
				binary.add(0x80);
				binary.add(0x64);
				break;
			case 'π':
				binary.add(0x80);
				binary.add(0x65);
				break;
			case 'Σ':
				binary.add(0x80);
				binary.add(0x66);
				break;
			case '√':
				binary.add(0x80);
				binary.add(0x67);
				break;
			case 'ゞ':
				binary.add(0x80);
				binary.add(0x68);
				break;
			default:
				binary.add(0x20);
				break;
			}
		return binary;
	}

	/**
	 * @param b
	 * @return 特殊文字か未定義の文字ならtrue
	 */
	public static boolean isSpecialChar(int b) {
		if (b<=0x20) {
			return true;
		}
		if (b>=0x7F && b<=0x85) {
			return true;
		}
		if (b==0xA0 || b==0xDE || b==0xDF) {
			return true;
		}
		return false;
	}

	/**
	 * 単音パートのデフォルトの音色調整を取得する
	 * @param instrumant
	 * @return Attack, Decay, Sustain, Release, Shape, Hold, Delay, Speed, Depth, EffectType, EffectValue
	 */
	public static int[] getDefaultSinglePartTone(BDXInstrument instrumant) {
		switch (instrumant) {
		case PIANO:
			return new int[] {99,21,8,40,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),10};
		case E_PIANO:
			return new int[] {99,11,20,40,VibratoShape.SIN.getValue(),15,15,55,8,EffectType.CHORUS.getTypeValue(),16};
		case R_ORGAN:
			return new int[] {99,4,62,50,VibratoShape.SIN.getValue(),0,0,50,14,EffectType.CHORUS.getTypeValue(),14};
		case SYN_LEAD:
			return new int[] {97,3,40,50,VibratoShape.SIN.getValue(),20,20,66,13,EffectType.ECHO.getTypeValue(),18};
		case SYN_BELL:
			return new int[] {99,2,28,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),12};
		case P_ORGAN:
			return new int[] {99,1,28,50,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),14};
		case F_GUITAR:
			return new int[] {99,5,0,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),10};
		case E_GUITAR:
			return new int[] {99,2,0,50,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),13};
		case D_GUITAR:
			return new int[] {97,3,8,50,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),14};
		case R_GUITAR:
			return new int[] {99,3,11,60,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),14};
		case PICK_BASS:
			return new int[] {96,3,0,50,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),12};
		case SYN_BASS:
			return new int[] {99,3,80,70,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),13};
		case ACO_BASS:
			return new int[] {99,5,0,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),12};
		case STRINGS:
			return new int[] {95,5,55,25,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CROSS.getTypeValue(),16};
		case VIOLIN:
			return new int[] {98,2,40,40,VibratoShape.SIN.getValue(),10,15,48,13,EffectType.ECHO.getTypeValue(),13};
		case DBL_BASS:
			return new int[] {94,2,50,25,VibratoShape.SIN.getValue(),10,27,40,14,EffectType.CHORUS.getTypeValue(),9};
		case HARP:
			return new int[] {99,13,0,20,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),9};
		case P_VIOLIN:
			return new int[] {96,30,0,60,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),13};
		case PICCOLO:
			return new int[] {99,4,40,70,VibratoShape.SIN.getValue(),15,15,48,12,EffectType.ECHO.getTypeValue(),9};
		case FLUTE:
			return new int[] {95,7,45,50,VibratoShape.SIN.getValue(),5,10,44,12,EffectType.ECHO.getTypeValue(),10};
		case CLARINET:
			return new int[] {99,10,35,35,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),10};
		case OBOE:
			return new int[] {97,5,50,60,VibratoShape.SIN.getValue(),15,20,48,10,EffectType.ECHO.getTypeValue(),10};
		case SOPR_SAX:
			return new int[] {99,6,45,50,VibratoShape.SIN.getValue(),20,3,46,20,EffectType.ECHO.getTypeValue(),13};
		case ALTO_SAX:
			return new int[] {97,6,50,30,VibratoShape.SIN.getValue(),20,7,46,20,EffectType.ECHO.getTypeValue(),13};
		case BRASS:
			return new int[] {99,4,30,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),13};
		case TRUMPET:
			return new int[] {99,6,40,60,VibratoShape.SIN.getValue(),20,15,50,12,EffectType.ECHO.getTypeValue(),12};
		case TROMBONE:
			return new int[] {99,5,15,30,VibratoShape.SIN.getValue(),25,6,48,18,EffectType.ECHO.getTypeValue(),13};
		case HORN:
			return new int[] {99,5,35,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CROSS.getTypeValue(),14};
		case TUBA:
			return new int[] {96,5,25,40,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),14};
		case HARMONICA:
			return new int[] {99,6,50,40,VibratoShape.SIN.getValue(),22,0,50,18,EffectType.ECHO.getTypeValue(),12};
		case PAN_FLUTE:
			return new int[] {99,4,50,35,VibratoShape.SIN.getValue(),18,10,44,15,EffectType.ECHO.getTypeValue(),15};
		case OCARINA:
			return new int[] {99,2,38,70,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),10};
		case VIBRAPH_:
			return new int[] {99,4,0,20,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CROSS.getTypeValue(),20};
		case MARIMBA:
			return new int[] {99,0,99,60,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CROSS.getTypeValue(),10};
		case TIMPANI:
			return new int[] {99,0,99,20,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),10};
		case STEEL_DRM:
			return new int[] {99,0,99,40,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CROSS.getTypeValue(),10};
		case CHORUS:
			return new int[] {99,2,30,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),13};
		case SHAMISEN:
			return new int[] {99,11,0,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),14};
		case KOTO:
			return new int[] {99,15,0,30,VibratoShape.RANDOM.getValue(),5,10,45,15,EffectType.CHORUS.getTypeValue(),19};
		case SHAKUHA_:
			return new int[] {99,2,30,30,VibratoShape.SIN.getValue(),39,0,51,40,EffectType.CHORUS.getTypeValue(),16};
		case NES:
			return new int[] {99,3,40,85,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),13};
		case HARPSICH_:
			return new int[] {99,11,0,80,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),12};
		case ACCORDION:
			return new int[] {97,10,55,50,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.ECHO.getTypeValue(),13};
		case M_TRUMPET:
			return new int[] {99,35,15,85,VibratoShape.SIN.getValue(),20,3,48,15,EffectType.ECHO.getTypeValue(),8};
		case MUSIC_BOX:
			return new int[] {99,11,0,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CROSS.getTypeValue(),20};
		case BANJO:
			return new int[] {99,20,5,30,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),15};
		case SQ_LEAD:
			return new int[] {97,20,60,40,VibratoShape.SIN.getValue(),15,20,50,20,EffectType.ECHO.getTypeValue(),18};
		case S_GUITAR:
			return new int[] {99,7,0,35,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),5};
		case C_GUITAR:
			return new int[] {98,12,0,50,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),8};
		case O_GUITAR:
			return new int[] {99,2,30,70,VibratoShape.SIN.getValue(),24,26,44,12,EffectType.ECHO.getTypeValue(),14};
		case SLAP_BASS:
			return new int[] {99,2,0,55,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.CHORUS.getTypeValue(),5};
		default:
			return new int[] {99,13,99,0,VibratoShape.NONE.getValue(),15,15,55,15,EffectType.NONE.getTypeValue(),0};
		}
	}

	/**
	 * @param root
	 * @param flag
	 * @param name
	 * @return
	 */
	public static Chord createChord(int root, int flag, int name) {
		Chord chord = NullChord.getInstance();
		Pitch pitch = Pitch.valueOf(root);
		if (pitch==null) {
			if (name!=0xFF) {
				chord = new OriginalChord(name);
			}
		} else {
			chord = new BaseChord(pitch, Accidental.valueOf(flag), BaseChord.Name.valueOf(name));
		}
		return chord;
	}

}
