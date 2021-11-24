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

	String soursePath;// Դ�ļ�·��
	String LL1Path;
	String wordListPath;
	String fourElementPath;
	LexAnalyse lexAnalyse;
	
	Parser parser;
	public MainFrame() throws IOException{
		this.init();
	}

	//���ڳ�ʼ��
	public void init() throws IOException{
		Toolkit toolkit = Toolkit.getDefaultToolkit(); //��ȡToolkit����
		Dimension screen = toolkit.getScreenSize(); //��ȡ��ʾ����Ļ��С
		setTitle("X0���Ա����� @AveBai 2021/11");
		setSize(1250, 800);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);  //���ڴ�С���ɸı�
		setLocation(screen.width / 2 - this.getWidth() / 2, screen.height / 2 - this.getHeight() / 2);  //���ô������Ͻ����꣬ʹ����Ļ����
		setContentPane(createContentPane()); //�������
	}

	//�������
	private JSplitPane createContentPane() throws IOException {
		JSplitPane p = new JSplitPane();
		JPanel panel1 = new JPanel(null);
		JPanel panel2 = new JPanel(null);

		/*
		  �����ļ�ѡ��ť
		 */
		JLabel label1 = new JLabel("ѡ���ļ�:");
		label1.setBounds(20,20,100,50);
		panel1.add(label1);

		//�ļ�����ʾ����
		JTextArea fileText = new JTextArea();
		fileText.setLineWrap(true);    // �Զ�����
		fileText.setBounds(120,20,400,50);
		panel1.add(fileText);

		JButton chooseButton = new JButton("���");
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

		JButton button1 = new JButton("ȷ��");
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
		  ���������ʾ����
		 */
		JLabel label2 = new JLabel("Դ��������:");
		//label2.setForeground(new Color(169,183,198));
		label2.setBounds(20,130,200,50);
		panel1.add(label2);

		//����Ĭ�ϴ���
		try {
			textArea1.setText(readFile("sushu.txt"));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		textArea1.setLineWrap(true);    // �Զ�����
		//textArea1.setFont(new Font("MONACO", Font.PLAIN, 20));
		textArea1.setBackground(new Color(43,43,43));


		// �����������
		JScrollPane scrollPane1 = new JScrollPane(
				textArea1,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
		);
		scrollPane1.setBounds(20,180,400,550);
		panel1.add(scrollPane1);

		//�����ʾ����
		JTextArea textArea2 = new JTextArea();
		textArea2.setBackground(new Color(43,43,43));
		//textArea2.setForeground(new Color(169,183,198));
		//textArea2.setLineWrap(true);    // �Զ�����
		//textArea2.setFont(new Font("����", Font.PLAIN, 20));
		// �����������
		JScrollPane scrollPane2 = new JScrollPane(
				textArea2,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		scrollPane2.setBounds(0,0,620,750);
		panel2.add(scrollPane2);
		/*
		  ���ܰ�ť
		 */
		JButton bt1 = new JButton("�ʷ�����");
		bt1.setBounds(450,250,120,50);
		//bt1.setFont(new Font("����", Font.BOLD, 20));
		//bt1.setForeground(Color.blue);
		bt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					lexAnalyse=new LexAnalyse(textArea1.getText());
					wordListPath = lexAnalyse.outputWordList(); //�ʷ�����
					textArea2.setText(readFile(wordListPath));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		panel1.add(bt1);

		JButton bt2 = new JButton("�﷨����");
		bt2.setBounds(450,350,120,50);
		//bt2.setFont(new Font("����", Font.BOLD, 20));
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

		JButton bt3 = new JButton("�м����");
		bt3.setBounds(450,450,120,50);
		//bt3.setFont(new Font("����", Font.BOLD, 20));
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

		JButton bt4 = new JButton("���н��");
		bt4.setBounds(450,650,120,50);
		//bt4.setFont(new Font("����", Font.BOLD, 20));
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

		JButton bt5 = new JButton("Ŀ�����");
		bt5.setBounds(450,550,120,50);
		//bt4.setFont(new Font("����", Font.BOLD, 20));
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

		JButton bt6 = new JButton("���ű�");
		bt6.setBounds(450,150,120,50);
		//bt4.setFont(new Font("����", Font.BOLD, 20));
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

		p.setOneTouchExpandable(true); // �ָ�������ʾ���� �۵�/չ�� ���������С��ť
		p.setContinuousLayout(true);  // �϶��ָ���ʱ�����ػ����
		p.setDividerLocation(600);  // ���÷ָ����ĳ�ʼλ��
		p.setLeftComponent(panel1);
		p.setRightComponent(panel2);
		return p;
	}



	//��ȡ�ļ�
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


		//UI���
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

