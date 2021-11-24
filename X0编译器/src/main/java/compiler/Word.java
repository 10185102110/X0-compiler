package compiler;

import java.util.ArrayList;

/**
 * 单词类
 * @author AveBai
 */
public class Word {
	public final static String KEY = "关键字";
	public final static String OPERATOR = "运算符";
	public final static String INT = "int常量";
	public final static String CHAR = "char常量";
	public final static String BOOL = "bool常量";
	public final static String IDENTIFIER = "标识符";
	public final static String BOUNDARYSIGN = "界符";
	public final static String END = "结束符";
	public final static String UNIDEF = "未知类型";
	public static ArrayList<String> key = new ArrayList<String>();// 关键字集合
	public static ArrayList<String> boundarySign = new ArrayList<String>();// 界符集合
	public static ArrayList<String> operator = new ArrayList<String>();// 运算符集合
	static {
		Word.operator.add("+");
		Word.operator.add("-");
		Word.operator.add("++");
		Word.operator.add("--");
		Word.operator.add("*");
		Word.operator.add("/");
		Word.operator.add(">");
		Word.operator.add("<");
		Word.operator.add(">=");
		Word.operator.add("<=");
		Word.operator.add("==");
		Word.operator.add("!=");
		Word.operator.add("=");
		Word.operator.add("&&");
		Word.operator.add("||");
		Word.operator.add("!");
		Word.operator.add("%");
		Word.boundarySign.add("(");
		Word.boundarySign.add(")");
		Word.boundarySign.add("{");
		Word.boundarySign.add("}");
		Word.boundarySign.add(";");
		Word.boundarySign.add(",");
		Word.boundarySign.add("[");
		Word.boundarySign.add("]");
		Word.key.add("main");
		Word.key.add("int");
		Word.key.add("char");
		Word.key.add("if");
		Word.key.add("while");
		Word.key.add("for");
		Word.key.add("write");
		Word.key.add("read");
	}
	int id; //单词序号
	String value; //单词的值
	String type; //单词类型
	int line; //单词所在行
	boolean flag = true; //单词是否合法

	public Word() {

	}

	public Word(int id, String value, String type, int line) {
		this.id = id;
		this.value = value;
		this.type = type;
		this.line = line;
	}

	//判断是否为关键字
	public static boolean isKey(String word) {
		return key.contains(word);
	}

	//判断是否为运算符
	public static boolean isOperator(String word) {
		return operator.contains(word);
	}

	//判断是否为界符
	public static boolean isBoundarySign(String word) {
		return boundarySign.contains(word);
	}

	//判断单词是否为算术运算符
	public static boolean isArOP(String word) {
		if (word.equals("+") || word.equals("-") || word.equals("*") || word.equals("/") || word.equals("%"))
			return true;
		else
			return false;
	}

	//判断单词是否为布尔运算符
	public static boolean isBoolOP(String word) {
		if ((word.equals(">") || word.equals("<") || word.equals(">=") || word.equals("<=")|| word.equals("==")
				|| word.equals("!=") || word.equals("!") || word.equals("&&") || word.equals("||")))
			return true;
		else
			return false;
	}
}
