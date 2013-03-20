package banbro.model.bdx;

public interface ChordNote {

	/**
	 * ボタン番号を取得。
	 * 実際の音を取得するには、パートに設定されているコードセットからコードを取り出す。
	 * @return ボタン番号
	 */
	int getButtonNum();

}
