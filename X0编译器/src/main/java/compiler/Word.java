package compiler;

import java.util.ArrayList;

/**
 * ������
 * @author AveBai
 */
public class Word {
	public final static String KEY = "�ؼ���";
	public final static String OPERATOR = "�����";
	public final static String INT = "int����";
	public final static String CHAR = "char����";
	public final static String BOOL = "bool����";
	public final static String IDENTIFIER = "��ʶ��";
	public final static String BOUNDARYSIGN = "���";
	public final static String END = "������";
	public final static String UNIDEF = "δ֪����";
	public static ArrayList<String> key = new ArrayList<String>();// �ؼ��ּ���
	public static ArrayList<String> boundarySign = new ArrayList<String>();// �������
	public static ArrayList<String> operator = new ArrayList<String>();// ���������
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
	int id; //�������
	String value; //���ʵ�ֵ
	String type; //��������
	int line; //����������
	boolean flag = true; //�����Ƿ�Ϸ�

	public Word() {

	}

	public Word(int id, String value, String type, int line) {
		this.id = id;
		this.value = value;
		this.type = type;
		this.line = line;
	}

	//�ж��Ƿ�Ϊ�ؼ���
	public static boolean isKey(String word) {
		return key.contains(word);
	}

	//�ж��Ƿ�Ϊ�����
	public static boolean isOperator(String word) {
		return operator.contains(word);
	}

	//�ж��Ƿ�Ϊ���
	public static boolean isBoundarySign(String word) {
		return boundarySign.contains(word);
	}

	//�жϵ����Ƿ�Ϊ���������
	public static boolean isArOP(String word) {
		if (word.equals("+") || word.equals("-") || word.equals("*") || word.equals("/") || word.equals("%"))
			return true;
		else
			return false;
	}

	//�жϵ����Ƿ�Ϊ���������
	public static boolean isBoolOP(String word) {
		if ((word.equals(">") || word.equals("<") || word.equals(">=") || word.equals("<=")|| word.equals("==")
				|| word.equals("!=") || word.equals("!") || word.equals("&&") || word.equals("||")))
			return true;
		else
			return false;
	}
}
