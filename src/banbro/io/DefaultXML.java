package banbro.io;

public abstract class DefaultXML {

	protected static final String CLOSE = ">";
	protected static final String END_CLOSE = " />";
	public static final String ATT_MY_XML_version = "version";

	/**
	 * XMLに変換する
	 * @return
	 */
	public abstract String toXML();

	/**
	 * XMLヘッダ
	 * @return "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;"
	 */
	protected String header() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	/**
	 * 属性なしの開始タグ
	 * @param tag
	 * @return "&lt;tag&gt;"
	 * @see DefaultXML#startTag(String, boolean)
	 */
	protected final String startTag(String tag) {
		return startTag(tag, true);
	}

	/**
	 * 開始タグ
	 * @param tag
	 * @param close ">"で閉じるならtrue (属性がある場合はfalseにする)
	 * @return
	 */
	protected final String startTag(String tag, boolean close) {
		if (close) {
			return "<" + tag + ">";
		} else {
			return "<" + tag;
		}
	}

	/**
	 * 属性
	 * @param att
	 * @param value
	 * @return " att=\"value\""
	 * @see DefaultXML#startTag(String, boolean)
	 */
	protected final String attribute(String att, Object value) {
		StringBuilder buf = new StringBuilder();
		buf.append(" ");
		buf.append(att);
		buf.append("=\"");
		buf.append(replaceXMLText(value.toString()));
		buf.append("\"");
		return buf.toString();
	}

	/**
	 * 終了タグ
	 * @param tag
	 * @return "&lt;/tag&gt;"
	 */
	protected final String endTag(String tag) {
		return "</" + tag + ">";
	}

	/**
	 * 属性・子要素を持たないタグ
	 * @param tag
	 * @return "&lt;tag /&gt;"
	 */
	protected final String emptyTag(String tag) {
		return "<" + tag + " />";
	}

	/**
	 * テキスト要素を持つタグ
	 * @param tag
	 * @param value
	 * @return &lt;tag&gt;value&lt;/tag&gt;
	 */
	protected final String textTag(String tag, Object value) {
		StringBuilder buf = new StringBuilder();
		buf.append(startTag(tag, true));
		buf.append(text(value.toString()));
		buf.append(endTag(tag));
		return buf.toString();
	}

	/**
	 * テキスト要素
	 * 複数行の場合は改行が改行タグに置き換わる
	 * @param text
	 * @return
	 * @see DefaultXML#textLines(String[])
	 */
	protected final String text(String text) {
		return textLines(text.split("\n", -1));
	}

	/**
	 * 複数行のテキスト要素
	 * @param lines 1要素が1行のテキスト
	 * @return
	 * @see DefaultXML#text(String)
	 * @see DefaultXML#lineFeedTag()
	 */
	private final String textLines(String[] lines) {
		StringBuilder buf = new StringBuilder();
		if (lines.length!=0 && lines[0].length()!=0) {
			buf.append(replaceText(lines[0]));
		}
		String LF = lineFeedTag();
		for (int i=1; i<lines.length; i++) {
			if (LF!=null) {
				buf.append(emptyTag(LF));
			}
			buf.append(replaceText(lines[i]));
		}
		return buf.toString();
	}

	/**
	 * @return 改行タグ(デフォルトはnullで、改行タグなし)
	 * @see DefaultXML#textLines(String, String[])
	 */
	protected String lineFeedTag() {
		return null;
	}

	/**
	 * コメント
	 * @param comment
	 * @return &lt;!--comment--&gt;
	 */
	protected final String comment(String comment) {
		return "<!--" + replaceXMLText(comment) + "-->";
	}

	/**
	 * XMLで使用不可能な文字を変換する<br>
	 * <table>
	 * <tr><th>文字名</th><th>文字参照</th><th>数値参照</th><th>表示テスト</th></tr>
	 * <tr><td>アンパサンド</td><td>&amp;amp;</td><td>&amp;#38;</td><td>&amp; &#38;</td></tr>
	 * <tr><td>左山かっこ</td><td>&amp;lt;</td><td>&amp;#60;</td><td>&lt; &#60;</td></tr>
	 * <tr><td>右山かっこ</td><td>&amp;gt;</td><td>&amp;#62;</td><td>&gt; &#62;</td></tr>
	 * <tr><td>二重引用符</td><td>&amp;quot;</td><td>&amp;#34;</td><td>&quot; &#34;</td></tr>
	 * <tr><td>アポストロフィ</td><td>&amp;apos;</td><td>&amp;#39;</td><td>&apos; &#39;</td></tr>
	 * </table>
	 */
	private final String replaceXMLText(String text) {
		StringBuilder sb = new StringBuilder(text.length()+16);
		for (char c : text.toCharArray()) {
			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&#39;");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * XMLで使用不可能な文字を変換する(テキスト要素用)
	 * @param text
	 * @return
	 * @see DefaultXML#replaceXMLText(String)
	 * @see DefaultXML#textLines(String, String[])
	 */
	protected String replaceText(String text) {
		return replaceXMLText(text);
	}

}
