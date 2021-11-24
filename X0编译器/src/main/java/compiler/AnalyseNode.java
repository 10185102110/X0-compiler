package compiler;

import java.util.ArrayList;

/**
 * 分析栈节点类
 *	String type;//节点类型
	String name;//节点名
	Object value;//节点值
 */
public class AnalyseNode {
	public final static String NONTERMINAL="非终结符";
	public final static String TERMINAL="终结符";
	public final static String ACTIONSIGN="动作符";
	public final static String END="结束符";
	static ArrayList<String>nonterminal=new ArrayList<String>();//非终结符集合
	static ArrayList<String>actionSign=new ArrayList<String>();//动作符集合
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

		actionSign.add("@AS");   //加减
		actionSign.add("@ADD");  //加
		actionSign.add("@SUB");  //减
		actionSign.add("@DMM");  //加减
		actionSign.add("@DIV");  //除
		actionSign.add("@MUL");  //乘
		actionSign.add("@MOD");  //求余
		actionSign.add("@SINGLE");  // 自加、自减
		actionSign.add("@SINGTLE_OP");  //自加、自减
		actionSign.add("@ASS_R");  //赋值，变量名入栈
		actionSign.add("@ASS_Q");  //for语句中的++或--赋值
		actionSign.add("@ASS_F");  //算术运算里的赋值
		actionSign.add("@ASS_P");  //wirte里的标识符或num或ch
		actionSign.add("@ASS_H");  //read中的赋值
		actionSign.add("@WRITE");
		actionSign.add("@READ");
		actionSign.add("@TRAN_LF"); //记录括号内表达式的值
		actionSign.add("@EQ");   //赋值，将计算结果存储到变量名
		actionSign.add("@CP");    //两个项之间的 <|>|==|!=
		actionSign.add("@CP_OP");  //两个因子之间的 <|>|==|!=
		actionSign.add("@IF_FJ");  //if结构内的语句开始
		actionSign.add("@IF_B"); //回填if语句
		actionSign.add("@WHILE_FJ");
		actionSign.add("@WHILE_B");
		actionSign.add("@IF_RJ");
		actionSign.add("@FOR_FJ"); //利用ARG1判断是否跳转到FOR_RJ后的第一个指令
		actionSign.add("@FOR_RJ"); //无条件跳转到for循环前的第一个指令
		actionSign.add("@FOR_B");
	}
	
	String type;//节点类型
	String name;//节点名
	String value;//节点值
	
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
