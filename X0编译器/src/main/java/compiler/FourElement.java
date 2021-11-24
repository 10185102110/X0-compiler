package compiler;

public class FourElement {
	int id;//四元式序号
	String op;//操作符
	String arg1;//第一个操作数
	String arg2;//第二个操作数
	String result;
	public FourElement(){

	}
	public FourElement(int id,String op,String arg1,String arg2,String r){
		this.id=id;
		this.op=op;
		this.arg1=arg1;
		this.arg2=arg2;
		this.result = r;
	}
}
