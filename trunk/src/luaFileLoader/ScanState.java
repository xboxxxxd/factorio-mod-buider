package luaFileLoader;

public enum ScanState {

	COMMENT,
	LONGCOMMAND,
	STRING,
	TOKENDEVIDER,
	MODLODERCOMMENT,
	OTHER
}
