package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarculaLaf;
import compiler.*;

public class MainFrame extends JFrame {

	String soursePath;// 源文件路径
	String LL1Path;
	String wordListPath;
	String fourElementPath;
	LexAnalyse lexAnalyse;
	
	Parser parser;
	public MainFrame() throws IOException{
		this.init();
	}

	//窗口初始化
	public void init() throws IOException{
		Toolkit toolkit = Toolkit.getDefaultToolkit(); //获取Toolkit对象
		Dimension screen = toolkit.getScreenSize(); //获取显示器屏幕大小
		setTitle("X0语言编译器 @AveBai 2021/11");
		setSize(1250, 800);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);  //窗口大小不可改变
		setLocation(screen.width / 2 - this.getWidth() / 2, screen.height / 2 - this.getHeight() / 2);  //设置窗口左上角坐标，使其屏幕居中
		setContentPane(createContentPane()); //设置面板
	}

	//创建面板
	private JSplitPane createContentPane() throws IOException {
		JSplitPane p = new JSplitPane();
		JPanel panel1 = new JPanel(null);
		JPanel panel2 = new JPanel(null);

		/*
		  创建文件选择按钮
		 */
		JLabel label1 = new JLabel("选择文件:");
		label1.setBounds(20,20,100,50);
		panel1.add(label1);

		//文件名显示区域
		JTextArea fileText = new JTextArea();
		fileText.setLineWrap(true);    // 自动换行
		fileText.setBounds(120,20,400,50);
		panel1.add(fileText);

		JButton chooseButton = new JButton("浏览");
		chooseButton.setBounds(170,90,80,40);
		chooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				int ret = chooser.showOpenDialog(panel1);
				if (ret == JFileChooser.APPROVE_OPTION) {
					fileText.setText(chooser.getSelectedFile().getPath());
				}
			}
		});
		panel1.add(chooseButton);

		JButton button1 = new JButton("确定");
		button1.setBounds(320,90,80,40);
		JTextArea textArea1 = new JTextArea();
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text;
				try {
					soursePath = fileText.getText();
					text = readFile(soursePath);
					textArea1.setText(text);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel1.add(button1);

		/*
		  待测代码显示区域
		 */
		JLabel label2 = new JLabel("源代码如下:");
		//label2.setForeground(new Color(169,183,198));
		label2.setBounds(20,130,200,50);
		panel1.add(label2);

		//设置默认代码
		try {
			textArea1.setText(readFile("sushu.txt"));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		textArea1.setLineWrap(true);    // 自动换行
		//textArea1.setFont(new Font("MONACO", Font.PLAIN, 20));
		textArea1.setBackground(new Color(43,43,43));


		// 创建滚动面板
		JScrollPane scrollPane1 = new JScrollPane(
				textArea1,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
		);
		scrollPane1.setBounds(20,180,400,550);
		panel1.add(scrollPane1);

		//结果显示区域
		JTextArea textArea2 = new JTextArea();
		textArea2.setBackground(new Color(43,43,43));
		//textArea2.setForeground(new Color(169,183,198));
		//textArea2.setLineWrap(true);    // 自动换行
		//textArea2.setFont(new Font("宋体", Font.PLAIN, 20));
		// 创建滚动面板
		JScrollPane scrollPane2 = new JScrollPane(
				textArea2,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		scrollPane2.setBounds(0,0,620,750);
		panel2.add(scrollPane2);
		/*
		  功能按钮
		 */
		JButton bt1 = new JButton("词法分析");
		bt1.setBounds(450,250,120,50);
		//bt1.setFont(new Font("楷体", Font.BOLD, 20));
		//bt1.setForeground(Color.blue);
		bt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(textArea1.getText());
					wordListPath = lexAnalyse.outputWordList(); //词法分析
					textArea2.setText(readFile(wordListPath));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		panel1.add(bt1);

		JButton bt2 = new JButton("语法分析");
		bt2.setBounds(450,350,120,50);
		//bt2.setFont(new Font("楷体", Font.BOLD, 20));
		//bt2.setForeground(Color.BLUE);
		bt2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lexAnalyse=new LexAnalyse(textArea1.getText());
				parser=new Parser(lexAnalyse);
				try {
					parser.grammerAnalyse();
					LL1Path= parser.outputLL1();
					textArea2.setText(readFile(LL1Path));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel1.add(bt2);

		JButton bt3 = new JButton("中间代码");
		bt3.setBounds(450,450,120,50);
		//bt3.setFont(new Font("楷体", Font.BOLD, 20));
		//bt3.setForeground(Color.BLUE);
		bt3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(textArea1.getText());
					parser=new Parser(lexAnalyse);
					parser.grammerAnalyse();
					fourElementPath=parser.outputFourElem();
					textArea2.setText(readFile(fourElementPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel1.add(bt3);

		JButton bt4 = new JButton("运行结果");
		bt4.setBounds(450,650,120,50);
		//bt4.setFont(new Font("楷体", Font.BOLD, 20));
		//bt4.setForeground(Color.BLUE);
		bt4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					/*lexAnalyse=new LexAnalyse(textArea1.getText());
					parser=new Parser(lexAnalyse);
					parser.grammerAnalyse();
					String table =parser.outputTable();
					fourElementPath=parser.outputFourElem();
					Pcode code = new Pcode();
					String ss = code.Pcode(fourElementPath);
					String Pcode= code.outputPcode();
					interpret intepre = new interpret();
					 */
					String Pcode = "./output/Pcode.txt";
					interpret intepre = new interpret();
					intepre.interpret(Pcode);
					String res = intepre.outputRes();
					textArea2.setText(readFile(res));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel1.add(bt4);

		JButton bt5 = new JButton("目标代码");
		bt5.setBounds(450,550,120,50);
		//bt4.setFont(new Font("楷体", Font.BOLD, 20));
		//bt4.setForeground(Color.BLUE);
		bt5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(textArea1.getText());
					parser=new Parser(lexAnalyse);
					parser.grammerAnalyse();
					String table =parser.outputTable();
					fourElementPath=parser.outputFourElem();
					Pcode code = new Pcode();
					String ss = code.Pcode(fourElementPath);
					String Pcode= code.outputPcode();
					textArea2.setText(readFile(Pcode));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel1.add(bt5);

		JButton bt6 = new JButton("符号表");
		bt6.setBounds(450,150,120,50);
		//bt4.setFont(new Font("楷体", Font.BOLD, 20));
		//bt4.setForeground(Color.BLUE);
		bt6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(textArea1.getText());
					parser=new Parser(lexAnalyse);
					parser.grammerAnalyse();
					String table =parser.outputTable();
					textArea2.setText(readFile(table));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel1.add(bt6);

		p.setOneTouchExpandable(true); // 分隔条上显示快速 折叠/展开 两边组件的小按钮
		p.setContinuousLayout(true);  // 拖动分隔条时连续重绘组件
		p.setDividerLocation(600);  // 设置分隔条的初始位置
		p.setLeftComponent(panel1);
		p.setRightComponent(panel2);
		return p;
	}



	//读取文件
	public static String readFile(String fileName) throws IOException {
		StringBuilder sbr = new StringBuilder();
		String str;
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "utf-8");
		BufferedReader in = new BufferedReader(isr);
		while ((str = in.readLine()) != null) {
			sbr.append(str).append('\n');
		}
		in.close();
		return sbr.toString();
	}

	public static void main(String[] args) throws IOException{

		//FlatLightLaf.install();
		FlatDarculaLaf.install();


		//UI风格
		try {
			//UIManager.setLookAndFeel(new FlatDarculaLaf());
			//UIManager.setLookAndFeel(new FlatLightLaf());
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}

		MainFrame mf = new MainFrame();
		//mf.setUndecorated(true);
		mf.setVisible(true);
	}
}

