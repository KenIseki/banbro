package banbro.io;

public abstract class DefaultXML {

	protected static final String CLOSE = ">";
	protected static final String END_CLOSE = " />";
	public static final String ATT_MY_XML_version = "version";

	/**
	 * XML�ɕϊ�����
	 * @return
	 */
	public abstract String toXML();

	/**
	 * XML�w�b�_
	 * @return "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;"
	 */
	protected String header() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	}

	/**
	 * �����Ȃ��̊J�n�^�O
	 * @param tag
	 * @return "&lt;tag&gt;"
	 * @see DefaultXML#startTag(String, boolean)
	 */
	protected final String startTag(String tag) {
		return startTag(tag, true);
	}

	/**
	 * �J�n�^�O
	 * @param tag
	 * @param close ">"�ŕ���Ȃ�true (����������ꍇ��false�ɂ���)
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
	 * ����
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
	 * �I���^�O
	 * @param tag
	 * @return "&lt;/tag&gt;"
	 */
	protected final String endTag(String tag) {
		return "</" + tag + ">";
	}

	/**
	 * �����E�q�v�f�������Ȃ��^�O
	 * @param tag
	 * @return "&lt;tag /&gt;"
	 */
	protected final String emptyTag(String tag) {
		return "<" + tag + " />";
	}

	/**
	 * �e�L�X�g�v�f�����^�O
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
	 * �e�L�X�g�v�f
	 * �����s�̏ꍇ�͉��s�����s�^�O�ɒu�������
	 * @param text
	 * @return
	 * @see DefaultXML#textLines(String[])
	 */
	protected final String text(String text) {
		return textLines(text.split("\n", -1));
	}

	/**
	 * �����s�̃e�L�X�g�v�f
	 * @param lines 1�v�f��1�s�̃e�L�X�g
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
	 * @return ���s�^�O(�f�t�H���g��null�ŁA���s�^�O�Ȃ�)
	 * @see DefaultXML#textLines(String, String[])
	 */
	protected String lineFeedTag() {
		return null;
	}

	/**
	 * �R�����g
	 * @param comment
	 * @return &lt;!--comment--&gt;
	 */
	protected final String comment(String comment) {
		return "<!--" + replaceXMLText(comment) + "-->";
	}

	/**
	 * XML�Ŏg�p�s�\�ȕ�����ϊ�����<br>
	 * <table>
	 * <tr><th>������</th><th>�����Q��</th><th>���l�Q��</th><th>�\���e�X�g</th></tr>
	 * <tr><td>�A���p�T���h</td><td>&amp;amp;</td><td>&amp;#38;</td><td>&amp; &#38;</td></tr>
	 * <tr><td>���R������</td><td>&amp;lt;</td><td>&amp;#60;</td><td>&lt; &#60;</td></tr>
	 * <tr><td>�E�R������</td><td>&amp;gt;</td><td>&amp;#62;</td><td>&gt; &#62;</td></tr>
	 * <tr><td>��d���p��</td><td>&amp;quot;</td><td>&amp;#34;</td><td>&quot; &#34;</td></tr>
	 * <tr><td>�A�|�X�g���t�B</td><td>&amp;apos;</td><td>&amp;#39;</td><td>&apos; &#39;</td></tr>
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
	 * XML�Ŏg�p�s�\�ȕ�����ϊ�����(�e�L�X�g�v�f�p)
	 * @param text
	 * @return
	 * @see DefaultXML#replaceXMLText(String)
	 * @see DefaultXML#textLines(String, String[])
	 */
	protected String replaceText(String text) {
		return replaceXMLText(text);
	}

}
