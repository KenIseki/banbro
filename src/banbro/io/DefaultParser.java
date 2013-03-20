package banbro.io;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class DefaultParser extends DefaultHandler {

	private Stack<Object> _obj;
	private Object _currentObj;
	private Stack<String> _tag;
	private String _currentTag;
	private String _text;

	public DefaultParser() {
	}

	@Override
	public void startDocument() throws SAXException {
		_obj = new Stack<Object>();
		_currentObj = null;
		_tag = new Stack<String>();
		_currentTag = null;
		_text = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		_currentObj = createCurrentObject(qName, attributes, _currentTag, _currentObj);
		_obj.push(_currentObj);
		_currentTag = qName;
		_tag.push(_currentTag);
		_text = null;
	}

	/**
	 * 開始タグに対応するオブジェクトを作成する。特にない場合は parentObj を返す。
	 * @param tag 開始タグ
	 * @param attributes 開始タグの属性
	 * @param parentTag
	 * @param parentObj
	 * @return
	 */
	protected Object createCurrentObject(String tag, Attributes attributes, String parentTag, Object parentObj) {
		if (parentObj==null) {
			return tag;
		} else {
			return parentObj;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (_currentTag==null) {
			return;
		}
		addText(new String(ch, start, length));
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (_currentTag==null) {
			return;
		}
		if (_currentObj==null) {
			return;
		}
		String oldTag = _currentTag;
		Object oldObj = _currentObj;
		_obj.pop();
		_currentObj = _obj.isEmpty() ? null : _obj.peek();
		_tag.pop();
		_currentTag = _tag.isEmpty() ? null : _tag.peek();
		updateCurrentObject(_currentTag, oldTag, _text, _currentObj, oldObj);
		_text = null;
	}

	/**
	 * 終了タグのオブジェクトを更新する。
	 * @param parentTag
	 * @param tag 終了タグ
	 * @param text
	 * @param parentObj
	 * @param obj
	 */
	protected void updateCurrentObject(String parentTag, String tag, String text, Object parentObj, Object obj) {
	}

	protected final Object getCurrentObj() {
		return _currentObj;
	}
	protected final String getCurrentTag() {
		return _currentTag;
	}
	protected final String getText() {
		return _text;
	}
	protected final void setText(String text) {
		_text = text;
	}
	protected final void addText(String text) {
		_text = _text==null ? text : _text+text;
	}

}
