package compiler;

import java.util.ArrayList;

/**
 * ����ջ�ڵ���
 *	String type;//�ڵ�����
	String name;//�ڵ���
	Object value;//�ڵ�ֵ
 */
public class AnalyseNode {
	public final static String NONTERMINAL="���ս��";
	public final static String TERMINAL="�ս��";
	public final static String ACTIONSIGN="������";
	public final static String END="������";
	static ArrayList<String>nonterminal=new ArrayList<String>();//���ս������
	static ArrayList<String>actionSign=new ArrayList<String>();//����������
	static{
		nonterminal.add("S"); //program
		nonterminal.add("A"); //declaration_list
		nonterminal.add("B"); //statement_list
		nonterminal.add("C"); //declaration_stat
		nonterminal.add("D"); //statement_list
		nonterminal.add("E");
		nonterminal.add("F");
		nonterminal.add("G");
		nonterminal.add("H");
		nonterminal.add("L");
		nonterminal.add("M");
		nonterminal.add("O");
		nonterminal.add("P");
		nonterminal.add("Q");
		nonterminal.add("X");
		nonterminal.add("Y");
		nonterminal.add("Z");
		nonterminal.add("R");
		nonterminal.add("U");
		nonterminal.add("Z'");
		nonterminal.add("U'");
		nonterminal.add("E'");
		nonterminal.add("H'");
		nonterminal.add("L'");
		nonterminal.add("T");
		nonterminal.add("T'");

		actionSign.add("@AS");   //�Ӽ�
		actionSign.add("@ADD");  //��
		actionSign.add("@SUB");  //��
		actionSign.add("@DMM");  //�Ӽ�
		actionSign.add("@DIV");  //��
		actionSign.add("@MUL");  //��
		actionSign.add("@MOD");  //����
		actionSign.add("@SINGLE");  // �Լӡ��Լ�
		actionSign.add("@SINGTLE_OP");  //�Լӡ��Լ�
		actionSign.add("@ASS_R");  //��ֵ����������ջ
		actionSign.add("@ASS_Q");  //for����е�++��--��ֵ
		actionSign.add("@ASS_F");  //����������ĸ�ֵ
		actionSign.add("@ASS_P");  //wirte��ı�ʶ����num��ch
		actionSign.add("@ASS_H");  //read�еĸ�ֵ
		actionSign.add("@WRITE");
		actionSign.add("@READ");
		actionSign.add("@TRAN_LF"); //��¼�����ڱ��ʽ��ֵ
		actionSign.add("@EQ");   //��ֵ�����������洢��������
		actionSign.add("@CP");    //������֮��� <|>|==|!=
		actionSign.add("@CP_OP");  //��������֮��� <|>|==|!=
		actionSign.add("@IF_FJ");  //if�ṹ�ڵ���俪ʼ
		actionSign.add("@IF_B"); //����if���
		actionSign.add("@WHILE_FJ");
		actionSign.add("@WHILE_B");
		actionSign.add("@IF_RJ");
		actionSign.add("@FOR_FJ"); //����ARG1�ж��Ƿ���ת��FOR_RJ��ĵ�һ��ָ��
		actionSign.add("@FOR_RJ"); //��������ת��forѭ��ǰ�ĵ�һ��ָ��
		actionSign.add("@FOR_B");
	}
	
	String type;//�ڵ�����
	String name;//�ڵ���
	String value;//�ڵ�ֵ
	
	public static boolean isNonterm(AnalyseNode node){
		return nonterminal.contains(node.name);
	}

	public static boolean isTerm(AnalyseNode node){
		return Word.isKey(node.name)||Word.isOperator(node.name)||Word.isBoundarySign(node.name)
		||node.name.equals("id")||node.name.equals("num")||node.name.equals("ch");
	}

	public static boolean isActionSign(AnalyseNode node){
		return actionSign.contains(node.name);
	}

	public AnalyseNode(){
		
	}

public AnalyseNode(String type,String name,String value){
		this.type=type;
		this.name=name;
		this.value=value;
	}

}
