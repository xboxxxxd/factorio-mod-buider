package luaFileLoader;

public enum LuaTokenLoaderState {

	COMMENT,
	LONGCOMMAND,
	STRING,
	NUMBER,
	NUMBER_DOT,
	TOKENDEVIDER,
	MODLODERCOMMENT,
	OTHER
}
