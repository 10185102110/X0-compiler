package compiler;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class interpret extends JFrame {
    ArrayList<Integer> res = new ArrayList<>();
    private static String fileName;
    private sign[] table = new sign[100];
    private Stack<Integer> stack = new Stack<>();

    public void interpret(String fileName){
        this.fileName=fileName;
        try {
            for (int i = 0; i < 100; i++) {
                table[i] = new sign(" ", " ", 0, i, 0);
            }
            String s[] = readFile("./output/table.txt").split("\n");
            for (int i = 2; i < s.length; i++) {
                String temp[] = s[i].split("\t");
                table[Integer.parseInt(temp[3])] = new sign(temp[0], temp[1], Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4]));
            }
            String str[] = readFile(fileName).split("\n");
            for(int i = 0; i < str.length; i++) {
                //System.out.println(str[i]);
                String temp[] = str[i].split("\t");
                String op = temp[1].substring(0,3);
                int p = Integer.parseInt(temp[1].substring(6,temp[1].length()));
                //System.out.println(temp[0]);
                int a,b;
                switch (op){
                    case "int":
                        break;
                    case "jpc":
                        //System.out.println("jpc  "+p);
                        if(stack.pop() == 0){
                            i = p-1;
                        }
                        break;
                    case "jmp":
                        //System.out.println("jmp  "+p);
                        i = p-1;
                        break;
                    case "lit":
                        stack.push(p);
                        break;
                    case "lod":
                        stack.push(table[p].val);
                        break;
                    case "sto":
                        table[p].val = stack.pop();
                        break;
                    case "opr":
                        switch (p){
                            case 1:
                                break;
                            case 2:
                                a = stack.pop();
                                b = stack.pop();
                                stack.push(b+a);
                                break;
                            case 3:
                                a = stack.pop();
                                b = stack.pop();
                                stack.push(b-a);
                                break;
                            case 4:
                                a = stack.pop();
                                b = stack.pop();
                                stack.push(b*a);
                                break;
                            case 5:
                                a = stack.pop();
                                b = stack.pop();
                                stack.push(b/a);
                                break;
                            case 6:
                                stack.push(stack.pop()+stack.pop());
                                break;
                            case 7:
                                stack.push(stack.pop()-stack.pop());
                                break;
                            case 8:
                                if(stack.pop() == stack.pop()){
                                    stack.push(1);
                                }else{
                                    stack.push(0);
                                }
                                break;
                            case 9:
                                if(stack.pop() != stack.pop()){
                                    stack.push(1);
                                }else{
                                    stack.push(0);
                                }
                                break;
                            case 10:
                                a = stack.pop();
                                b = stack.pop();
                                if(b < a){
                                    stack.push(1);
                                    //System.out.println();
                                }else{
                                    stack.push(0);
                                }
                                break;
                            case 11:
                                a = stack.pop();
                                b = stack.pop();
                                if(b <= a){
                                    stack.push(1);
                                    //System.out.println();
                                }else{
                                    stack.push(0);
                                }
                                break;
                            case 12:
                                a = stack.pop();
                                b = stack.pop();
                                if(b > a){
                                    stack.push(1);
                                }else{
                                    stack.push(0);
                                }
                                break;
                            case 13:
                                a = stack.pop();
                                b = stack.pop();
                                if(b >= a){
                                    stack.push(1);
                                }else{
                                    stack.push(0);
                                }
                                break;
                            case 14:
                                a = stack.pop();
                                b = stack.pop();
                                //System.out.println(b%a);
                                stack.push(b%a);
                                break;
                            case 15:
                                res.add(stack.pop());
                                break;
                        }
                        break;
                }
            }
            /*
            for(sign q:table){
                if(!q.name.equals(" ")){
                    System.out.printf("%s\t%s\t%s\t%d\t%d\n",q.type,q.name,q.val,q.addr,q.size);
                }
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public String outputRes() throws IOException {
        File file=new File("./output/");
        if(!file.exists()){
            file.mkdirs();
            file.createNewFile();
        }
        String path=file.getAbsolutePath();
        FileOutputStream fos=new FileOutputStream(path+"/res.txt");
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        OutputStreamWriter osw1=new OutputStreamWriter(bos,"utf-8");
        PrintWriter pw1=new PrintWriter(osw1);
        for(Integer s : res){
            pw1.println(s);
        }
        pw1.close();
        return path+"/res.txt";
    }

}
