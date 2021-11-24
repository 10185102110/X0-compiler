package compiler;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.util.Stack;

public class Pcode extends JFrame{

    private static String fileName;
    private static TextArea text = new TextArea();
    private sign[] table = new sign[100];
    private Stack<Integer> fj = new Stack<>();
    private Stack<Integer> fj_b = new Stack<>();
    private Stack<Integer> rj = new Stack<>();
    private String[] t = new String[200];

    public String Pcode(String fileName){
        this.fileName=fileName;
        try {
            for(int i=0;i<100;i++){
                table[i] = new sign(" "," ",0,i,0);
            }
            String s[] =readFile("./output/table.txt").split("\n");
            for(int i = 2; i < s.length; i++) {
                String temp[] = s[i].split("\t");
                table[Integer.parseInt(temp[3])] = new sign(temp[0],temp[1],Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]));
            }
            String str[] =readFile(fileName).split("\n");

            String temp1 = "0" + "\tint " + "0" + "," + (1+s.length) + "\n";
            t[0] = temp1;
            temp1 = "1" + "\tjmp " + "0" + "," + "2" + "\n";
            t[1] = temp1;
            int line = 2;
            for(int i = 2; i < str.length; i++) {
                String temp[] = str[i].split(",");
                if(temp[0].charAt(temp[0].length() - 1) == '=' && temp[0].charAt(temp[0].length() - 2) == '='){   //==
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "8" + "\n";
                    t[line-1] = temp1;
                }else if (temp[0].charAt(temp[0].length() - 1) == '=' && temp[0].charAt(temp[0].length() - 2) != '<'
                        && temp[0].charAt(temp[0].length() - 2) != '!' && temp[0].charAt(temp[0].length() - 2) != '>') {           // =
                    if(Character.isDigit(temp[1].charAt(0))){
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                        int pos = position(temp[3].substring(0, temp[3].length() - 1));
                        temp1 = line++ + "\tsto " + "0" + "," + pos + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        int pos1 = position(temp[1]);
                        temp1 = line++ + "\tlod " + "0" + "," + pos1 + "\n";
                        t[line-1] = temp1;
                        //System.out.println(temp[3].substring(0, temp[3].length() - 1));
                        int pos2 = position(temp[3].substring(0, temp[3].length() - 1));
                        //System.out.println(pos2);
                        temp1 = line++ + "\tsto " + "0" + "," + pos2 + "\n";
                        t[line-1] = temp1;
                    }else{
                        int pos2 = position(temp[3].substring(0, temp[3].length() - 1));
                        temp1 = line++ + "\tsto " + "0" + "," + pos2 + "\n";
                        t[line-1] = temp1;
                    }
                } else if (temp[0].charAt(temp[0].length() - 1) == '+' && temp[0].charAt(temp[0].length() - 2) == '+') {  //++
                    temp1 = line++ + "\tlit " + "0" + "," + "1" + "\n";
                    t[line-1] = temp1;
                    temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                    t[line-1] = temp1;
                    temp1 = line++ + "\topr " + "0" + "," + "6" + "\n";
                    t[line-1] = temp1;
                    temp1 = line++ + "\tsto " + "0" + "," + position(temp[1]) + "\n";
                    t[line-1] = temp1;
                } else if (temp[0].charAt(temp[0].length() - 1) == '+') {       //+
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "2" + "\n";
                    t[line-1] = temp1;
                } else if (temp[0].charAt(temp[0].length() - 1) == '-' && temp[0].charAt(temp[0].length() - 2) == '-') {  //++
                    temp1 = line++ + "\tlit " + "0" + "," + "1" + "\n";
                    t[line-1] = temp1;
                    temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                    t[line-1] = temp1;
                    temp1 = line++ + "\topr " + "0" + "," + "7" + "\n";
                    t[line-1] = temp1;
                    temp1 = line++ + "\tsto " + "0" + "," + position(temp[1]) + "\n";
                    t[line-1] = temp1;
                }else if (temp[0].charAt(temp[0].length() - 1) == '-') {        //-
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "3" + "\n";
                    t[line-1] = temp1;
                } else if (temp[0].charAt(temp[0].length() - 1) == '*') {       //*
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "4" + "\n";
                    t[line-1] = temp1;
                } else if (temp[0].charAt(temp[0].length() - 1) == '/') {     // /
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "5" + "\n";
                    t[line-1] = temp1;
                } else if (temp[0].charAt(temp[0].length() - 1) == '%') {       // %
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "14" + "\n";
                    t[line-1] = temp1;
                }else if(temp[0].charAt(temp[0].length() - 1) == '=' && temp[0].charAt(temp[0].length() - 2) == '!'){ //!=
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "9" + "\n";
                    t[line-1] = temp1;
                }else if(temp[0].charAt(temp[0].length() - 2) == '<' && temp[0].charAt(temp[0].length() - 1) == '='){
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "11" + "\n";
                    t[line-1] = temp1;
                }else if(temp[0].charAt(temp[0].length() - 1) == '<'){
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "10" + "\n";
                    t[line-1] = temp1;
                }else if(temp[0].charAt(temp[0].length() - 2) == '>' && temp[0].charAt(temp[0].length() - 1) == '='){
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "13" + "\n";
                    t[line-1] = temp1;
                }else if(temp[0].charAt(temp[0].length() - 1) == '>'){
                    if(Character.isDigit(temp[1].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[1] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[1].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line-1] = temp1;
                    }
                    if(Character.isDigit(temp[2].charAt(0))) {
                        temp1 = line++ + "\tlit " + "0" + "," + temp[2] + "\n";
                        t[line-1] = temp1;
                    }else if(temp[2].charAt(0)!='T'){
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[2]) + "\n";
                        t[line-1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "12" + "\n";
                    t[line-1] = temp1;
                } else if(temp[0].charAt(temp[0].length() - 1) == 'w'){
                    if(temp[1].charAt(0)!='T') {
                        temp1 = line++ + "\tlod " + "0" + "," + position(temp[1]) + "\n";
                        t[line - 1] = temp1;
                    }
                    temp1 = line++ + "\topr " + "0" + "," + "15" + "\n";
                    t[line - 1] = temp1;
                }else if(temp[0].charAt(temp[0].length() - 1) == 'r'){
                    if(temp[1].charAt(0)!='T') {
                        String message = "请输入" + temp[1];
                        String info = JOptionPane.showInputDialog(null,message,"输入",JOptionPane.PLAIN_MESSAGE);	//输入对话框
                        if(Character.isDigit(info.charAt(0))) {
                            temp1 = line++ + "\tlit " + "0" + "," + info + "\n";
                            t[line - 1] = temp1;
                            temp1 = line++ + "\tsto " + "0" + "," + position(temp[1]) + "\n";
                            t[line - 1] = temp1;
                        }else{
                            JOptionPane.showMessageDialog(null,"应输入 int 类型","非法输入",JOptionPane.ERROR_MESSAGE);	//输入对话框
                        }
                    }
                }else if (temp[0].charAt(temp[0].length() - 1) == 'J' && temp[0].charAt(temp[0].length() - 2) == 'R') {
                    if(temp[3].charAt(0)=='/'){
                        int fj_index = fj.pop();
                        fj_b.pop();
                        t[fj_index] = fj_index + "\tjpc " + "0" + "," + line + "\n";
                    }else{
                        int fj_index = fj.pop();
                        int p = fj_b.pop();
                        int count = 1;
                        int sum = 3;   //应该回跳的步数
                        //System.out.println(fj_index);
                        //System.out.println(str[p]);
                        temp = str[p-count++].split(",");
                        while(temp[1].charAt(0)=='T' || temp[2].charAt(0)=='T'){
                            temp = str[p-count++].split(",");
                            sum+=2;
                        }
                        temp1 = line++ + "\tjmp " + "0" + "," + (fj_index-sum) + "\n";
                        //System.out.println(temp[3]);
                        t[fj_index] = fj_index + "\tjpc " + "0" + "," + line + "\n";
                        t[line-1] = temp1;
                    }
                } else if (temp[0].charAt(temp[0].length() - 1) == 'J' && temp[0].charAt(temp[0].length() - 2) == 'F') {
                    temp1 = line++ + "\tjpc " + "0" + "," + "0" + "\n";
                    fj_b.push(i);
                    fj.push(line-1);
                    t[line-1] = temp1;
                }
            }
            temp1 = line++ + "\topr " + "0" + "," + "0" + "\n";
            t[line-1] = temp1;
        } catch (IOException e) {

            e.printStackTrace();
        }
        for(String s:t){
            if(s!=null) text.append(s);
        }
        return text.getText();
    }

    private int position(String n){
        for(int i = 0;i<table.length;i++){
            if(table[i].name.equals(n)) return table[i].addr;
        }
        return 0;
    }

    private String readFile(String filename)
            throws IOException{
        StringBuilder sbr = new StringBuilder();
        String str;
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        InputStreamReader isr = new InputStreamReader(bis, "utf-8");
        BufferedReader in=new BufferedReader(isr);
        while((str=in.readLine())!=null){
            sbr.append(str).append('\n');
        }
        in.close();
        return sbr.toString();
    }

    public String outputPcode() throws IOException {
        File file=new File("./output/");
        if(!file.exists()){
            file.mkdirs();
            file.createNewFile();//如果这个文件不存在就创建它
        }
        String path=file.getAbsolutePath();
        FileOutputStream fos=new FileOutputStream(path+"/Pcode.txt");
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
        PrintWriter pw1=new PrintWriter(osw1);
        for(String s : t){
            if(s!=null) pw1.print(s);
        }
        pw1.close();
        return path+"/Pcode.txt";
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        Pcode.fileName = fileName;
    }

    public static TextArea getText() {
        return text;
    }

    public static void setText(TextArea jText) {
        Pcode.text = jText;
    }

}
