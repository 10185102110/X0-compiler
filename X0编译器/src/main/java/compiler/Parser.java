package compiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
/**
 * �﷨������
 * @author AveBai
 *
 */
public class Parser {

	private LexAnalyse lexAnalyse ; //�ʷ�������
	ArrayList<Word> wordList = new ArrayList<Word>(); //���ʱ�
	Stack<AnalyseNode> analyseStack = new Stack<AnalyseNode>(); //����ջ
	Stack<String> semanticStack = new Stack<String>(); //����ջ
	ArrayList<FourElement> fourElemList = new ArrayList<FourElement>(); //��Ԫʽ�б�
	ArrayList<Error> errorList = new ArrayList<Error>(); //������Ϣ�б�
	StringBuffer bf; //����ջ������
	int errorCount=0; //ͳ�ƴ������
	boolean graErrorFlag=false; //�﷨���������־
	int tempCount=0; //����������ʱ����
	int fourElemCount=0; //ͳ����Ԫʽ����
	AnalyseNode S,B,A,C,X,Y,R,Z,Z1,U,U1,E,E1,H,H1,G,D,L,L1,T,T1,F,O,P,Q;
	AnalyseNode AS,DMM,ADD,SUB,DIV,MUL,MOD,ASS_F,ASS_P,ASS_R,ASS_Q,ASS_H,TRAN_LF;
	AnalyseNode SINGLE,SINGLE_OP,EQ,CP,CP_OP,IF_FJ,IF_B_FJ,IF_RJ,IF_B_RJ,WRITE,READ;
	AnalyseNode WHILE_J,WHILE_FJ,WHILE_RJ,WHILE_B,FOR_FJ,FOR_RJ,FOR_B;
	AnalyseNode top; //��ǰջ��Ԫ��
	Word firstWord; //����������
	String OP=null;
	String ARG1,ARG2,RES; // ��Ԫʽ��������1��������2�����
	Error error;
	Stack<Integer>if_fj,if_rj,while_fj,while_rj,for_fj,for_rj,while_j; //if while for ��ת��ַջ
	Stack<String>for_op=new Stack<String>();   //for��ѭ������
	sign[] table = new sign[100];   //���ű�
	int addr = 3;   //���ű���ʼ��ַ

	public Parser(){

	}

	public Parser(LexAnalyse lexAnalyse){
		this.lexAnalyse=lexAnalyse;
		this.wordList=lexAnalyse.wordList;
		init();
	}

	private String newTemp(){
		tempCount++;
		return "T"+tempCount;
	}

	public void init(){
		S=new AnalyseNode(AnalyseNode.NONTERMINAL, "S", null);
		A=new AnalyseNode(AnalyseNode.NONTERMINAL, "A", null);
		B=new AnalyseNode(AnalyseNode.NONTERMINAL, "B", null);
		C=new AnalyseNode(AnalyseNode.NONTERMINAL, "C", null);
		X=new AnalyseNode(AnalyseNode.NONTERMINAL, "X", null);
		Y=new AnalyseNode(AnalyseNode.NONTERMINAL, "Y", null);
		Z=new AnalyseNode(AnalyseNode.NONTERMINAL, "Z", null);
		Z1=new AnalyseNode(AnalyseNode.NONTERMINAL, "Z'", null);
		U=new AnalyseNode(AnalyseNode.NONTERMINAL, "U", null);
		U1=new AnalyseNode(AnalyseNode.NONTERMINAL, "U'", null);
		E=new AnalyseNode(AnalyseNode.NONTERMINAL, "E", null);
		E1=new AnalyseNode(AnalyseNode.NONTERMINAL, "E'", null);
		H=new AnalyseNode(AnalyseNode.NONTERMINAL, "H", null);
		H1=new AnalyseNode(AnalyseNode.NONTERMINAL, "H'", null);
		G=new AnalyseNode(AnalyseNode.NONTERMINAL, "G", null);
		F=new AnalyseNode(AnalyseNode.NONTERMINAL, "F", null);
		D=new AnalyseNode(AnalyseNode.NONTERMINAL, "D", null);
		L=new AnalyseNode(AnalyseNode.NONTERMINAL, "L", null);
		L1=new AnalyseNode(AnalyseNode.NONTERMINAL, "L'", null);
		T=new AnalyseNode(AnalyseNode.NONTERMINAL, "T", null);
		T1=new AnalyseNode(AnalyseNode.NONTERMINAL, "T'", null);
		O=new AnalyseNode(AnalyseNode.NONTERMINAL, "O", null);
		P=new AnalyseNode(AnalyseNode.NONTERMINAL, "P", null);
		Q=new AnalyseNode(AnalyseNode.NONTERMINAL, "Q", null);
		R=new AnalyseNode(AnalyseNode.NONTERMINAL, "R", null);
		AS=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@AS", null);
		ADD=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ADD", null);
		SUB=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SUB", null);
		DMM=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@DMM", null);
		DIV=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@DIV", null);
		MUL=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@MUL", null);
		MOD=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@MOD", null);
		ASS_F=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_F", null);
		ASS_P=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_P", null);
		ASS_R=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_R", null);
		ASS_Q=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_Q", null);
		ASS_H=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_H", null);
		TRAN_LF=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@TRAN_LF", null);
		SINGLE=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE", null);
		SINGLE_OP=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null);
		WRITE=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WRITE", null);
		READ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@READ", null);
		EQ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@EQ", null);
		CP=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@CP", null);
		CP_OP=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@CP_OP", null);
		IF_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_FJ", null);
		IF_B_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_B_FJ", null);
		IF_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_RJ", null);
		IF_B_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@IF_B_RJ", null);
		WHILE_J=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_J", null);
		WHILE_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_FJ", null);
		WHILE_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_RJ", null);
		WHILE_B=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WHILE_B", null);
		FOR_FJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_FJ", null);
		FOR_RJ=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_RJ", null);
		FOR_B=new AnalyseNode(AnalyseNode.ACTIONSIGN, "@FOR_B", null);

		if_fj=new Stack<Integer>();
		if_rj=new Stack<Integer>();
		while_j=new Stack<Integer>();
		while_fj=new Stack<Integer>();
		while_rj=new Stack<Integer>();
		for_fj=new Stack<Integer>();
		for_rj=new Stack<Integer>();

		for(int i=0;i<100;i++){
			table[i] = new sign(" "," ",0,i,0);
		}

	}

	/*
	 *LL1�������������﷨����
	 */
	public void grammerAnalyse(){
		if(lexAnalyse.isFail())javax.swing.JOptionPane.showMessageDialog(null, "�ʷ�����δͨ�������ܽ����﷨����");
		bf=new StringBuffer();
		int gcount=0; //��¼����
		error=null;
		analyseStack.add(0,S);
		analyseStack.add(1,new AnalyseNode(AnalyseNode.END, "#", null));
		semanticStack.add("#");
		while(!analyseStack.empty() && !wordList.isEmpty()){
			bf.append('\n');
			bf.append("����"+gcount+"\t");

			//������������࣬�ж��﷨��������
			if(gcount++>1000){
				graErrorFlag=true;
				break;
			}
			top = analyseStack.get(0); //��ǰջ��Ԫ��
			firstWord = wordList.get(0); //����������
			if(firstWord.value.equals("#") && top.name.equals("#")){ //��������
				bf.append("\n");
				analyseStack.remove(0);
				wordList.remove(0);

			}
			//����ջΪ�գ������д��������ʣ��﷨��������
			else if(top.name.equals("#")){
				analyseStack.remove(0);
				graErrorFlag=true;
				break;

			}
			else if(AnalyseNode.isTerm(top)){  //ջ��Ϊ�ս��ʱ�Ĵ���
				termOP(top.name);
			}
			else if(AnalyseNode.isNonterm(top)){ //ջ��Ϊ���ս��ʱ�Ĵ���
				nonTermOP(top.name);
			}
			else if(top.type.equals(AnalyseNode.ACTIONSIGN)){  //ջ���Ƕ�������ʱ�Ĵ���
				actionSignOP();
			}

			bf.append("��ǰ����ջ:");
			for(int i=0;i<analyseStack.size();i++){
				bf.append(analyseStack.get(i).name);
			}
			bf.append("\t").append("ʣ����ţ�");
			for(int j=0;j<wordList.size();j++){
				bf.append(wordList.get(j).value);
			}
			bf.append("\t").append("����ջ:");
			for(int k=semanticStack.size()-1;k>=0;k--){
				bf.append(semanticStack.get(k));
			}
		}
	}

	/*
	 *ջ��Ϊ�ս��ʱ�Ĵ���
	 */
	private void termOP(String term){
		if(firstWord.type.equals(Word.INT) || firstWord.type.equals(Word.CHAR) || firstWord.type.equals(Word.IDENTIFIER) ||
				term.equals(firstWord.value)){
			analyseStack.remove(0);
			wordList.remove(0);
		}
		else{
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error=new Error(errorCount,"�﷨����ȱ��"+term,firstWord.line,firstWord);
			errorList.add(error);
			graErrorFlag=true;
		}

	}

	/*
	 *ջ��Ϊ���ս��ʱ�Ĵ���
	 */
	private void nonTermOP(String nonTerm){
		if(nonTerm.equals("Z'"))nonTerm="1";
		if(nonTerm.equals("U'"))nonTerm="2";
		if(nonTerm.equals("E'"))nonTerm="3";
		if(nonTerm.equals("H'"))nonTerm="4";
		if(nonTerm.equals("L'"))nonTerm="5";
		if(nonTerm.equals("T'"))nonTerm="6";
		switch(nonTerm.charAt(0)){  //ջ��Ϊ���ս������
			//N:S,B,A,C,,X,R,Z,Z��,U,U��,E,E��,H,H��,G,M,D,L,L��,T,T��,F,O,P,Q
			case 'S':
				if(firstWord.value.equals("main")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "main", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
					analyseStack.add(2,A);
					analyseStack.add(3,U);
					analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
				}else {
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"û��������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'U':
				if(firstWord.value.equals("write") || firstWord.value.equals("read") || firstWord.value.equals("if") ||
						firstWord.value.equals("for") || firstWord.value.equals("while") || firstWord.type.equals(Word.IDENTIFIER)){
					analyseStack.remove(0);
					analyseStack.add(0,U1);
					analyseStack.add(1,U);
				}else{
					analyseStack.remove(0);
				}
				break;
			case '2':
				if(firstWord.value.equals("write") || firstWord.value.equals("read") || firstWord.value.equals("if") ||
						firstWord.value.equals("for") || firstWord.value.equals("while")){
					analyseStack.remove(0);
					analyseStack.add(0,B);
				}else if(firstWord.type.equals(Word.IDENTIFIER)){
					analyseStack.remove(0);
					analyseStack.add(0,R);
				}else{
					analyseStack.remove(0);
				}
				break;
			case 'A':
				if(firstWord.value.equals("int")||firstWord.value.equals("char")
						||firstWord.value.equals("bool")){
					analyseStack.remove(0);
					analyseStack.add(0,X);
					analyseStack.add(1,A);
				}else{
					analyseStack.remove(0);
				}
				break;

			case '4': //read
				if(firstWord.type.equals(Word.IDENTIFIER)) {
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_H", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
					analyseStack.add(2,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@READ", null));
				}else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"�Ƿ���ʶ��������read",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'B':
				if(firstWord.value.equals("write")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "write", null));
					analyseStack.add(1,E1);
					analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				}
				else if(firstWord.value.equals("read")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "read", null));
					analyseStack.add(1,H1);
					analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				}
				else if(firstWord.value.equals("if")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "if", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
					analyseStack.add(2,E);
					analyseStack.add(3,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
					analyseStack.add(4,IF_FJ);
					analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
					analyseStack.add(6,U);
					analyseStack.add(7,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
					analyseStack.add(8,IF_B_FJ);
					analyseStack.add(9,IF_RJ);
				}
				else if(firstWord.value.equals("while")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "while", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
					analyseStack.add(2,WHILE_J);
					analyseStack.add(3,G);
					analyseStack.add(4,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
					analyseStack.add(5,WHILE_FJ);
					analyseStack.add(6,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
					analyseStack.add(7,U);
					analyseStack.add(8,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
					analyseStack.add(9,WHILE_RJ);
					analyseStack.add(10,WHILE_B);
				}
				else if(firstWord.value.equals("for")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "for", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
					analyseStack.add(2,R);
					analyseStack.add(3,G);
					analyseStack.add(4,FOR_FJ);
					analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
					analyseStack.add(6,Q);
					analyseStack.add(7,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
					analyseStack.add(8,new AnalyseNode(AnalyseNode.TERMINAL, "{", null));
					analyseStack.add(9,U);
					analyseStack.add(10,SINGLE);
					analyseStack.add(11,new AnalyseNode(AnalyseNode.TERMINAL, "}", null));
					analyseStack.add(12,FOR_RJ);
					analyseStack.add(13,FOR_B);
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"�Ƿ����",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'X':
				if(firstWord.value.equals("int")||firstWord.value.equals("char")||firstWord.value.equals("bool")){
					analyseStack.remove(0);
					analyseStack.add(0,Y);
					analyseStack.add(1,Z);
					analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
					table[addr].type = firstWord.value;
				}else{
					analyseStack.remove(0);
				}
				break;
			case 'Y':
				if(firstWord.value.equals("int")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "int", null));

				}
				else if(firstWord.value.equals("char")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "char", null));
				}
				else if(firstWord.value.equals("bool")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "bool", null));
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"�Ƿ���������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'Z':
				if(firstWord.type.equals(Word.IDENTIFIER)){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
					analyseStack.add(1,Z1);
					table[addr++].name = firstWord.value;
				}else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"����ȱ�ٱ�ʶ��",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case '1': //Z'
				if(firstWord.value.equals("[")){
					//System.out.println(1);
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "[", null));
					analyseStack.add(1,H);
				}else{
					//System.out.println(2);
					analyseStack.remove(0);
				}
				break;
			case 'H':
				if(firstWord.type.equals(Word.INT)){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "]", null));
					table[addr-1].type = "arr";
					table[addr-1].size = Integer.parseInt(firstWord.value);
				}else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"δ�������鳤��",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'R':
				if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)!=0) {
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_R", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
					analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "=", null));
					analyseStack.add(3,L);
					analyseStack.add(4,EQ);
					analyseStack.add(5,new AnalyseNode(AnalyseNode.TERMINAL, ";", null));
				}else if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)==0) {
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"��ʶ��δ����",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				else{
					analyseStack.remove(0);
				}
				break;
			case '3': //write
				if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)!=0) {
					analyseStack.remove(0);
					analyseStack.add(0,L);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@WRITE", null));
				}else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"�Ƿ���ʶ��������write",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'E':
				if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)!=0){
					analyseStack.remove(0);
					analyseStack.add(0,G);
				}else if(firstWord.type.equals(Word.INT)){
					analyseStack.remove(0);
					analyseStack.add(0,G);
				}else if(firstWord.value.equals("(")){
					analyseStack.remove(0);
					analyseStack.add(0,G);
				}else if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)==0){
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"��ʶ��δ����",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"���ܽ��в����������������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'D':
				if(firstWord.value.equals("==")){
					analyseStack.remove(0);
					analyseStack.add(0,CP_OP);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "==", null));
				}else if(firstWord.value.equals("!=")){
					analyseStack.remove(0);
					analyseStack.add(0,CP_OP);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "!=", null));

				}else if(firstWord.value.equals(">")){
					analyseStack.remove(0);
					analyseStack.add(0,CP_OP);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, ">", null));
				}else if(firstWord.value.equals("<")){
					analyseStack.remove(0);
					analyseStack.add(0,CP_OP);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "<", null));
				}else if(firstWord.value.equals(">=")){
					analyseStack.remove(0);
					analyseStack.add(0,CP_OP);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, ">=", null));
				}else if(firstWord.value.equals("<=")){
					analyseStack.remove(0);
					analyseStack.add(0,CP_OP);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "<=", null));
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"�Ƿ����������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
					analyseStack.add(0,CP_OP);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, firstWord.value, null));
				}
				break;
			case 'G':
				if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)!=0){
					analyseStack.remove(0);
					analyseStack.add(0,L);
					analyseStack.add(1,D);
					analyseStack.add(2,L);
					analyseStack.add(3,CP);
				}else if(firstWord.type.equals(Word.INT)){
					analyseStack.remove(0);
					analyseStack.add(0,L);
					analyseStack.add(1,D);
					analyseStack.add(2,L);
					analyseStack.add(3,CP);
				}
				else if(firstWord.value.equals("(")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
					analyseStack.add(1,E);
					analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
				}
				else if(firstWord.value.equals("!")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "!", null));
					analyseStack.add(1,E);
				}else if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)==0){
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"��ʶ��δ����",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'L':
				if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)!=0){
					analyseStack.remove(0);
					analyseStack.add(0,T);
					analyseStack.add(1,L1);
					analyseStack.add(2,AS);
				}else if(firstWord.type.equals(Word.INT)){
					analyseStack.remove(0);
					analyseStack.add(0,T);
					analyseStack.add(1,L1);
					analyseStack.add(2,AS);
				}else if(firstWord.value.equals("(")){
					analyseStack.remove(0);
					analyseStack.add(0,T);
					analyseStack.add(1,L1);
					analyseStack.add(2,AS);
				}else if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)==0){
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"��ʶ��δ����",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case '5'://l'
				if(firstWord.value.equals("+")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "+", null));
					analyseStack.add(1,L);
					analyseStack.add(2,ADD);
				}
				else if(firstWord.value.equals("-")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "-", null));
					analyseStack.add(1,L);
					analyseStack.add(2,SUB);
				}else {
					analyseStack.remove(0);
				}
				break;

			case 'T':
				if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)!=0){
					analyseStack.remove(0);
					analyseStack.add(0,F);
					analyseStack.add(1,T1);
					analyseStack.add(2,DMM);
				}else if(firstWord.type.equals(Word.INT)){
					analyseStack.remove(0);
					analyseStack.add(0,F);
					analyseStack.add(1,T1);
					analyseStack.add(2,DMM);
				}
				else if(firstWord.value.equals("(")){
					analyseStack.remove(0);
					analyseStack.add(0,F);
					analyseStack.add(1,T1);
					analyseStack.add(2,DMM);
				}else if(firstWord.type.equals(Word.IDENTIFIER) && position(firstWord.value)==0) {
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"��ʶ��δ����",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case '6'://T'
				if(firstWord.value.equals("*")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "*", null));
					analyseStack.add(1,T);
					analyseStack.add(2,MUL);
				}
				else if(firstWord.value.equals("/")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "/", null));
					analyseStack.add(1,T);
					analyseStack.add(2,DIV);
				}else if(firstWord.value.equals("%")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "%", null));
					analyseStack.add(1,T);
					analyseStack.add(2,MOD);
				} else {
					analyseStack.remove(0);
				}
				break;
			case 'F':
				if(firstWord.type.equals(Word.IDENTIFIER)){
					analyseStack.remove(0);
					analyseStack.add(0,ASS_F);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
				}else if(firstWord.type.equals(Word.INT)){
					analyseStack.remove(0);
					analyseStack.add(0,ASS_F);
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "num", null));
				}else if(firstWord.value.equals("(")){
					analyseStack.add(0,new AnalyseNode(AnalyseNode.TERMINAL, "(", null));
					analyseStack.add(1,L);
					analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, ")", null));
					analyseStack.add(3,TRAN_LF);
				}
				else{
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"���ܽ��������������������",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'O':
				if(firstWord.value.equals("++")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "++", null));
				}else if(firstWord.value.equals("--")){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@SINGLE_OP", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "--", null));

				}else {
					errorCount++;
					analyseStack.remove(0);
					wordList.remove(0);
					error=new Error(errorCount,"for��ʽ����,ѭ��������Ҫ++��--",firstWord.line,firstWord);
					errorList.add(error);
					graErrorFlag=true;
				}
				break;
			case 'Q'://Q
				if(firstWord.type.equals(Word.IDENTIFIER)){
					analyseStack.remove(0);
					analyseStack.add(0,new AnalyseNode(AnalyseNode.ACTIONSIGN, "@ASS_Q", null));
					analyseStack.add(1,new AnalyseNode(AnalyseNode.TERMINAL, "id", null));
					analyseStack.add(2,new AnalyseNode(AnalyseNode.TERMINAL, "O", null));
				}else {
					analyseStack.remove(0);
				}
				break;

		}
	}

	/*
	 *������
	 */
	private void actionSignOP(){
		if(top.name.equals("@AS")){
			if(OP!=null && (OP.equals("+") || OP.equals("-"))){
				RES=newTemp();
				ARG2=semanticStack.pop();
				ARG1=semanticStack.pop();
				FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
				fourElemList.add(fourElem);
				L.value=RES;
				semanticStack.push(L.value);
				OP=null;
			}
			analyseStack.remove(0);
		}else if(top.name.equals("@ADD")){
			OP="+";
			analyseStack.remove(0);
		}else if(top.name.equals("@SUB")){
			OP="-";
			analyseStack.remove(0);
		}else if(top.name.equals("@DMM")){
			if(OP != null && (OP.equals("*") || OP.equals("/") || OP.equals("%"))){
				RES=newTemp();
				ARG2=semanticStack.pop();
				ARG1=semanticStack.pop();
				FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
				fourElemList.add(fourElem);
				T.value=RES;
				semanticStack.push(T.value);
				OP=null;
			}
			analyseStack.remove(0);
		} else if(top.name.equals("@DIV")){
			OP="/";
			analyseStack.remove(0);
		} else if(top.name.equals("@MUL")){
			OP="*";
			analyseStack.remove(0);
		} else if(top.name.equals("@MOD")){
			OP="%";
			analyseStack.remove(0);
		} else if(top.name.equals("@TRAN_LF")){
			F.value=L.value;
			//semanticStack.push(F.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@ASS_F")){  //��ʶ��
			F.value=firstWord.value;
			semanticStack.push(F.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@ASS_P")) { //��ʶ��/num/ch
			Z.value=firstWord.value;
			semanticStack.push(Z.value);
			analyseStack.remove(0);
		} else if(top.name.equals("@ASS_R")){  //��ʶ��
			R.value=firstWord.value;
			semanticStack.push(R.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@ASS_Q")){  //��ʶ��
			Q.value=firstWord.value;
			semanticStack.push(Q.value);
			analyseStack.remove(0);
		} else if(top.name.equals("@ASS_H")){  //��ʶ��
			H.value=firstWord.value;
			semanticStack.push(H.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@SINGLE")){
			if(for_op.size()!=0 && for_op.peek()!=null){
				ARG1=semanticStack.pop();
				RES=ARG1;
				FourElement fourElem=new FourElement(++fourElemCount,for_op.pop(),ARG1,"/",RES);
				fourElemList.add(fourElem);
			}
			analyseStack.remove(0);
		} else if(top.name.equals("@SINGLE_OP")){
			for_op.push(firstWord.value);
			analyseStack.remove(0);
		}else if(top.name.equals("@EQ")){
			OP="=";
			ARG1=semanticStack.pop();
			RES=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/",RES);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		} else if(top.name.equals("@WRITE")){
			OP = "w";
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/","/");
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@READ")){
			OP = "r";
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/","/");
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@CP")){
			ARG2=semanticStack.pop();
			OP=semanticStack.pop();
			ARG1=semanticStack.pop();
			RES=newTemp();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,ARG2,RES);
			fourElemList.add(fourElem);
			G.value=RES;
			semanticStack.push(G.value);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@CP_OP")){
			D.value=firstWord.value;
			semanticStack.push(D.value);
			analyseStack.remove(0);
		}
		else if(top.name.equals("@IF_FJ")){
			OP="FJ";
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/",RES);
			if_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@IF_B_FJ")){
			backpatch(if_fj.pop(), fourElemCount+1);
			analyseStack.remove(0);
		}else if(top.name.equals("@IF_RJ")){
			OP="RJ";
			FourElement fourElem=new FourElement(++fourElemCount,OP,"/","/","/");
			if_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@IF_B_RJ")){
			backpatch(if_rj.pop(), fourElemCount+1);
			analyseStack.remove(0);
		}else if(top.name.equals("@WHILE_J")){            //��ת��while�����ĵ�һ��
			while_j.push(fourElemCount);
			analyseStack.remove(0);
		}else if(top.name.equals("@WHILE_FJ")){
			OP="FJ";
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/","/");
			while_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@WHILE_RJ")){
			OP="RJ";
			//RES=(while_fj.peek()-1)+"";
			RES=(while_j.pop()+1)+"";
			FourElement fourElem=new FourElement(++fourElemCount,OP,"/","/",RES);
			for_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@WHILE_B")){
			backpatch(while_fj.pop(), fourElemCount+1);
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_FJ")){
			OP="FJ";
			ARG1=semanticStack.pop();
			FourElement fourElem=new FourElement(++fourElemCount,OP,ARG1,"/","/");
			for_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_RJ")){
			OP="RJ";
			RES=(for_fj.peek()-1)+"";
			FourElement fourElem=new FourElement(++fourElemCount,OP,"/","/",RES);
			for_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP=null;
			analyseStack.remove(0);
		}else if(top.name.equals("@FOR_B")){
			backpatch(for_fj.pop(), fourElemCount+1);
			analyseStack.remove(0);
		}


	}

	/*
	 *���������res�����i����Ԫʽ����ת��ַ��
	 */
	private void backpatch(int i,int res){
		FourElement temp=fourElemList.get(i-1);
		temp.result=res+"";
		fourElemList.set(i-1, temp);
	}

	private int position(String name){
		for(sign s:table){
			if(s.name.equals(name)) return s.addr;
		}
		return 0;
	}

	/*
	 *����﷨����������ļ� output/LL1.txt
	 */
	public String outputLL1() throws IOException{
		//grammerAnalyse();
		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//�������ļ������ھʹ�����
		}
		String path=file.getAbsolutePath();
		FileOutputStream fos=new FileOutputStream(path+"/LL1.txt");
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter pw1=new PrintWriter(osw1);
		pw1.println(bf.toString());
		bf.delete(0, bf.length());
		if(graErrorFlag){
			Error error;
			pw1.println("������Ϣ����:");

			pw1.println("�������\t������Ϣ\t���������� \t���󵥴�");
			for(int i=0;i<errorList.size();i++){
				error=errorList.get(i);
				pw1.println(error.id+"\t"+error.info+"\t"+error.line+"\t"+error.word.value);
			}
		}else {
			pw1.println("�﷨����ͨ����");
		}
		pw1.close();
		return path+"/LL1.txt";
	}

	/*
	 *�����Ԫʽ���ļ� output/FourElement.txt
	 */
	public String outputFourElem() throws IOException{

		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//�������ļ������ھʹ�����
		}
		String path=file.getAbsolutePath();
		FileOutputStream fos=new FileOutputStream(path+"/FourElement.txt");
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter pw1=new PrintWriter(osw1);
		pw1.println("��Ԫʽ����");
		pw1.println("���\t(OP,ARG1,ARG2,RESULT)");
		FourElement temp;
		for(int i=0;i<fourElemList.size();i++){
			temp=fourElemList.get(i);
			pw1.println(temp.id+"\t("+temp.op+","+temp.arg1+","+temp.arg2+","+temp.result+")");
		}
		pw1.close();

		return path+"/FourElement.txt";
	}

	public String outputTable() throws IOException {
		File file=new File("./output/");
		if(!file.exists()){
			file.mkdirs();
			file.createNewFile();//�������ļ������ھʹ�����
		}
		String path=file.getAbsolutePath();
		FileOutputStream fos=new FileOutputStream(path+"/table.txt");
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
		PrintWriter pw1=new PrintWriter(osw1);
		pw1.println("���ű�����");
		//pw1.println("��ţ�OP,ARG1��ARG2��RESULT��");
		sign temp;
		pw1.println("type\tname\tval\taddr\tsize");
		for(sign s : table){
			if(s.type!=" ") pw1.printf("%s\t%s\t%s\t%d\t%d\n",s.type,s.name,s.val,s.addr,s.size);
		}
		pw1.close();
		return path+"/table.txt";
	}
}



//���ű�ṹ
class sign{
	public String type;
	public String name;
	public int val;
	public int addr;
	public int size;
	public sign(){

	}
	public sign(String type,String name,int val,int addr,int size){
		this.type = type;
		this.name = name;
		this.val = val;
		this.addr = addr;
		this.size = size;
	}
}