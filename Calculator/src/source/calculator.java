package source;


import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class calculator extends JFrame{
	public static void main(String[] args) {
		new calculator();
	}
	private final String[] KEYS = { "(", ")", "C", "<-", "F", "1/X", "%", "/",
			"7", "8", "9","x", "4", "5", "6", "-","1", "2", "3", "+","��","0",".","="};
	private JButton keys[] = new JButton[KEYS.length];
	private JButton clear,save;
	private JPanel jp,jp_left,jp_output,jp_input,jp_right,jp_button;
	private static JTextField text1;
	private static JTextField text2;
	private static JTextArea history;
	private JLabel hint;
	private boolean flag = false;
	public calculator(){
		//����
		jp = new JPanel();
		jp.setLayout(new FlowLayout(FlowLayout.LEFT));

		//��
		jp_left = new JPanel();
		jp_left.setLayout(new BoxLayout(jp_left, BoxLayout.Y_AXIS));
		jp_output = new JPanel();
		jp_output.setLayout(new BoxLayout(jp_output, BoxLayout.Y_AXIS));
		text1 = new JTextField(10);
		text1.setFont(new Font("΢���ź�",Font.PLAIN,15));
		text1.setEditable(false);
		text2 = new JTextField(10);
		text2.setHorizontalAlignment(JTextField.RIGHT);
		text2.setFont(new Font("΢���ź�",Font.PLAIN,25));
		text2.setEditable(false);
		text2.setText("0");
		jp_output.add(text1);
		jp_output.add(text2);
		clear = new JButton("�����ʷ��¼");
		save = new JButton("�������ļ�");
		jp_input=new JPanel();
		jp_input.setLayout(new GridLayout(6,4,10,10));
		for (int i = 0; i < KEYS.length; i++) {
			keys[i] = new JButton(KEYS[i]);
			jp_input.add(keys[i]);
			keys[i].setForeground(Color.BLACK);
			keys[i].setFont(new Font("΢���ź�",Font.PLAIN,20));
			keys[i].addKeyListener(new KeyMonitor());
			String temp2 = KEYS[i];
			int a = i;
			if(a!=2&&a!=3&&a!=4&&a!=5&&a!=20&&a!=23){
				keys[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String temp = text1.getText().toString()+temp2;
						text1.setText(temp);
					}
				});
			}
		}
		keys[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				text1.setText(null);
				text2.setText("0");
			}
		});
		keys[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String text = text1.getText();
				int i = text.length();
				if (i > 0) {
					// �˸񣬽��ı����һ���ַ�ȥ��
					text = text.substring(0, i - 1);
					if (text.length() == 0) {
						// ����ı�û�������ݣ����ʼ���������ĸ���ֵ
						text1.setText(null);
					} else {
						// ��ʾ�µ��ı�
						text1.setText(text);
					}
				}
			}
		});
		keys[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					jfc.showDialog(new JPanel(), "ѡ��");
					java.io.File file = jfc.getSelectedFile();
					if(file.isFile()){
						if(file.getName().endsWith(".txt")){
							String s = file.getAbsolutePath();
							history.setText(history.getText()+s+"\n");
							try {
								readFile(file.getAbsolutePath());
							} catch (IOException e) {
								// TODO �Զ����ɵ� catch ��
								e.printStackTrace();
							}
						}
						else{
							history.setText(history.getText()+"�ļ���ʽ���ԣ�"+"\n");
						}
					}
					else{
						history.setText(history.getText()+"�ļ�·�����ԣ�"+"\n");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		keys[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String a = text2.getText().toString();
				double b = Double.valueOf(a);
				if(b!=0){
					text2.setText(String.valueOf(1/b));
					String c = history.getText().toString();
					if(c.equals("")){
						//						System.out.println("a");
						history.setText(b+"�ĵ�����"+1/b+"\n");
					}
					else{
						//System.out.println("b");
						history.setText(c+b+"�ĵ�����"+1/b+"\n");
					}
				}
				else{
					history.setText(history.getText().toString()+"\n"+"0û�е�����");
				}
			}
		});
		keys[20].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String a = text2.getText().toString();
				double b = Double.valueOf(a);
				text2.setText(String.valueOf(-b));
				String c = history.getText();
				if(c.equals("")){
					//System.out.println("a");
					history.setText(b+"���෴����"+-b);
				}
				else{
					//System.out.println("b");
					history.setText(c+"\n"+b+"���෴����"+-b);
				}
			}

		});
		keys[23].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String a = text1.getText().toString();
				CalandSet(a);
			}
		});

		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				history.setText("");
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					jfc.showDialog(new JPanel(), "ѡ��");
					java.io.File file = jfc.getSelectedFile();
					if(file.isFile()){
						try {
							System.out.println(history.getText());
							saveFile(file.getAbsolutePath(),history.getText());
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println(file.getAbsolutePath());
					}
					else{
						System.out.println("��ѡ����ȷ·����");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		jp_left.add(jp_output);
		jp_left.add(jp_input);

		//��
		jp_right = new JPanel();
		jp_right.setLayout(new BoxLayout(jp_right, BoxLayout.Y_AXIS));
		hint=new JLabel("��ʷ��¼");
		hint.setFont(new Font("΢���ź�",Font.PLAIN,15));
		history=new JTextArea(16,25);
		history.setEditable(false);
		clear.setFont(new Font("΢���ź�",Font.PLAIN,15));

		jp_button = new JPanel();
		jp_button.setLayout(new BoxLayout(jp_button, BoxLayout.X_AXIS));
		save.setFont(new Font("΢���ź�",Font.PLAIN,15));
		jp_button.add(clear);
		jp_button.add(save);
		jp_right.add(hint);
		jp_right.add(history);
		jp_right.add(jp_button);
		jp.addKeyListener(new KeyMonitor());
		jp_button.addKeyListener(new KeyMonitor());
		jp_input.addKeyListener(new KeyMonitor());
		jp_left.addKeyListener(new KeyMonitor());
		jp_output.addKeyListener(new KeyMonitor());
		jp_right.addKeyListener(new KeyMonitor());
		text1.addKeyListener(new KeyMonitor());
		text2.addKeyListener(new KeyMonitor());
		hint.addKeyListener(new KeyMonitor());
		history.addKeyListener(new KeyMonitor());
		clear.addKeyListener(new KeyMonitor());
		save.addKeyListener(new KeyMonitor());
		jp.add(jp_left);
		jp.add(jp_right);
		this.setTitle("������");
		this.add(jp);
		this.setBounds(500,100,630,400);
		String lookandfeel=UIManager.getSystemLookAndFeelClassName();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{
			UIManager.setLookAndFeel(lookandfeel);
		}catch(Exception e1){
			e1.printStackTrace();
		}
		this.setVisible(true);
	}

	private static char[][] re = {
			/*     +   -   x   /   %   (   )   #    */
			/*+*/{'>','>','<','<','<','<','>','>'},
			/*-*/{'>','>','<','<','<','<','>','>'},
			/*x*/{'>','>','>','>','>','<','>','>'},
			/*/*/{'>','>','>','>','>','<','>','>'},
			/*%*/{'>','>','>','>','>','<','>','>'},
			/*(*/{'<','<','<','<','<','<','=','@'},
			/*)*/{'>','>','>','>','>','@','>','>'},
			/*#*/{'<','<','<','<','<','<','@','='},
	};
	private static char rule[]={
			'+','-','x','/','%','(',')','#',
	};
	//����򵥱��ʽ
	private static double SimpleCal(double num1,char c,double num2){
		double res = 0;
		switch(c) {
		case '+':{
			res = num1 + num2;
		}break;

		case '-':{	
			res = num1 - num2;
		}break;
		case 'x':{

			res = num1 * num2;
		}break;
		case '/':{

			res = num1 / num2;
		}break;
		case '%':{

			res = num1 % num2;
		}break;
		}
		return res;
	}
	//���ȼ��ж�
	private static String serchOp(String a, String b){
		int a1 = 0,b1 = 0;
		//		System.out.println(a);
		//		System.out.println(b);
		for(int i=0;i<rule.length;i++){
			//			System.out.println(rule[i]);
			if(a.charAt(0)==rule[i]){
				a1=i;
				//				System.out.println(a1);

			}
			if(b.charAt(0)==rule[i]){
				b1=i;
				//				System.out.println(b1);
			}
		}
		return re[a1][b1]+"";
	}
	//�ж�������Ƿ�Ϸ�
	private static boolean isOperator(char chb) {
		if(chb=='+'||chb=='-'||chb=='x'||chb=='/'||chb=='('||chb==')'||chb=='%'||chb=='.'){
			return true;
		}
		return false;
	}
	//�ж��Ƿ�Ϊ����
	private static boolean isNum(char ch) {
		if(ch>='0'&&ch<='9'){
			return true;
		}
		return false;
	}
	//��������ʽ�Ƿ���ȷ
	private static boolean CheckeStr(String str) {
		//		System.out.println(str);
		if(str.isEmpty()){
			return false;
		}
		int flag = 0;
		char[] ch = str.toString().toCharArray();
		if(ch[0]==')'||ch[0]=='x'||ch[0]=='/'||ch[0]=='%'||ch[0]=='.'){
			return false;
		}
		//		System.out.println(ch[0]+"aaa"+ch[str.length()-1]);
		for(int i = 0 ; i < str.length(); i++){
			//�ж�����
			//			System.out.print(ch[i]);
			if(ch[i]=='(') flag++;
			if(ch[i]==')') flag--;
			//			System.out.println(flag);
			if((ch[i]<str.length()-1)&&isOperator(ch[i])&&isOperator(ch[i+1])&&ch[i]!='('&&ch[i]!=')'&&ch[i+1]!='('&&ch[i+1]!=')'){
				return false;
			}
			if((ch[i]<str.length()-1)&&ch[i]=='('&&ch[i+1]==')'){
				return false;
			}
		}
		if(flag!=0) {
			//			System.out.println(flag);
			return false;
		}
		return true;
	}
	//�ַ���->�ַ�������
	private static List<String> StrToStrArray(String str){
		List<String> list = new ArrayList<String>();
		String temp = "";
		for(int i = 0 ; i < str.length() ; i++){
			final char ch = str.charAt(i);
			if(isNum(ch)||ch=='.'){
				temp+=str.charAt(i);

			}
			else if(isOperator(ch)){
				if(!temp.equals("")){
					list.add(temp);
					//					System.out.println(temp);
				}
				list.add(""+ch);
				//				System.out.println(ch);
				temp="";
			}
			if(i==str.length()-1){
				list.add(temp);
				//				System.out.println(temp);
			}
		}
		return list;
	}
	//�ж��Ƿ�Ϊ����
	private static Boolean IsDouble(String str){
		if(str==""){
			return false;
		}
		if(str.charAt(0)=='.'||str.charAt(str.length()-1)=='.'){
			return false;
		}
		for(int i = 0 ; i < str.length() ; i++){
			if(!isNum(str.charAt(i))&&str.charAt(i)!='.'){
				return false;
			}
		}
		return true;

	}
	//��׺->��׺
	private static List<String> MidToLast(List<String> list){
		Stack<String> stack = new Stack<String>();
		List<String> plist = new ArrayList<String>();
		for (String str : list){
			if(IsDouble(str)){
				plist.add(str);
				//				System.out.println("����У�"+str);
			}

			if(isOperator(str.charAt(0))&&stack.isEmpty()){
				String a = stack.push(str);
				//				System.out.println("��ջ��"+a);

			}
			else if(isOperator(str.charAt(0))&&!stack.isEmpty()){

				String last = stack.lastElement();
				//				System.out.println(last+serchOp(last, str)+str);
				if((serchOp(str,last).equals(">")||str.equals("("))&&!str.equals(")")){
					String a = stack.push(str);
					if(str.equals(")")){
						//						System.out.println(")");
					}
					//					System.out.println("��ջ��"+a);
				}

				else if((serchOp(str,last).equals("<")||serchOp(str,last).equals("@"))&&!str.equals(")")){

					while(!stack.isEmpty()&&!stack.lastElement().equals("(")){
						String a = stack.pop();
						plist.add(a);
						//						System.out.println("��ջ��"+a);
					}

					String a = stack.push(str);
					//					System.out.println("��ջ��"+a);
				}
				else if(str.equals(")")){

					while(!stack.isEmpty()){
						String pop = stack.pop();
						//						System.out.println("��ջ��"+pop);
						if(!pop.equals("(")){
							plist.add(pop);
							//							System.out.println("�����"+pop);
						}
						if(pop.equals("(")){
							break;
						}
					}
				}
			}
		}
		while(!stack.isEmpty()){
			String a = stack.pop();
			plist.add(a);
			//			System.out.println("�����"+a);
		}
		for(String a:plist){
			//			System.out.println(a+"!");
		}
		return plist;
	}
	//��׺���ʽ��ֵ
	private static double Cal(List<String> list){
		//		System.out.println();
		Stack<String> stack = new Stack<String>();
		for(String str:list){
			if(IsDouble(str)){
				String a = stack.push(str);
				//				System.out.println("��ջ"+a);
			}
			else if(isOperator(str.charAt(0))){
				double n2 = Double.valueOf(stack.pop());
				//				System.out.println("��ջ"+n2);
				double n1 = Double.valueOf(stack.pop());
				//				System.out.println("��ջ"+n1);
				double c = SimpleCal(n1, str.charAt(0), n2);
				stack.push(""+c);
				//				System.out.println("��ջ"+c);
			}
		}
		double b = Double.valueOf(stack.pop());
		//		System.out.println("��ջ"+b);
		return b;
	}
	private static void CalandSet(String a){
		try{
			if(!CheckeStr(a)){
				System.out.println("error");
				history.setText(history.getText()+a+"="+"error"+"\n");
			}
			else{
				double b = Cal(MidToLast(StrToStrArray(a)));
				text2.setText(b+"");
				history.setText(history.getText()+a+"="+b+"\n");
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void readFile(String path) throws IOException{
		try{
			String line;
			BufferedReader in = new BufferedReader(new FileReader(path));
			line = in.readLine();
			CalandSet(line);
			while(line!=null){
//				System.out.println(line);
				line = in.readLine();
//				text1.setText(line);
				CalandSet(line);
			}
			in.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void saveFile(String path,String content) throws IOException{
		FileWriter writer = new FileWriter(path,true);
		char[] ch = content.toString().toCharArray();
		for(int i = 0 ; i < content.length() ; i++){
			if(ch[i]!='\n'){
				writer.write(ch[i]);
			}
			else{
				writer.write(13);
				writer.write(10);
			}
		}
		writer.close();
	}
	class KeyMonitor extends KeyAdapter{
		private int ptr = 0;
		private boolean flag3 = false;
		double t ;
		String res = "";
		public void keyReleased(KeyEvent e){
			int key = e.getKeyCode();
			if(!e.isShiftDown())
			{
				if(key == KeyEvent.VK_BACK_SPACE){
					String text = text1.getText();
					int i = text.length();
					if (i > 0) {
						// �˸񣬽��ı����һ���ַ�ȥ��
						text = text.substring(0, i - 1);
						if (text.length() == 0) {
							// ����ı�û�������ݣ����ʼ���������ĸ���ֵ
							text1.setText(null);
						} else {
							// ��ʾ�µ��ı�
							text1.setText(text);
						}
					}
				}
				if(key == KeyEvent.VK_EQUALS || key == KeyEvent.VK_ENTER){
					String a = text1.getText().toString();
					CalandSet(a);
				}
				if(key == KeyEvent.VK_DIVIDE){
					String J1temp = text1.getText();
					J1temp += (char)key;
					text1.setText(J1temp);
				}
				if(key == KeyEvent.VK_H){
					history.setText("");
				}
				if(key == KeyEvent.VK_B){
					String a = text2.getText().toString();
					double b = Double.valueOf(a);
					if(b!=0){
						text2.setText(String.valueOf(1/b));
						String c = history.getText().toString();
						if(c.trim().equals("")){
							//System.out.println("a");
							history.setText(b+"�ĵ�����"+1/b+"\n");
						}
						else{
							//System.out.println("b");
							history.setText(c+"\n"+b+"�ĵ�����"+1/b+"\n");
						}
					}
					else{
						history.setText(history.getText().toString()+"\n"+"0û�е�����");
					}
				}
				if(key == KeyEvent.VK_O){
					String a = text2.getText().toString();
					double b = Double.valueOf(a);
					text2.setText(String.valueOf(-b));
					String c = history.getText().toString();
					if(c.trim().equals("")){
						//System.out.println("a");
						history.setText(b+"���෴����"+-b);
					}
					else{
						//System.out.println("b");
						history.setText(c+"\n"+b+"���෴����"+-b);
					}
				}
				if( key == KeyEvent.VK_0 ||
					key == KeyEvent.VK_1 ||
					key == KeyEvent.VK_2 ||
					key == KeyEvent.VK_3 ||
					key == KeyEvent.VK_4 ||
					key == KeyEvent.VK_5 ||
					key == KeyEvent.VK_6 ||
					key == KeyEvent.VK_7 ||
					key == KeyEvent.VK_8 ||
					key == KeyEvent.VK_9 ){
							String J1temp = text1.getText();
							J1temp += (char)key;
							text1.setText(J1temp);
				}
				//С����
				if(key == KeyEvent.VK_PERIOD || key == 110){
					String J1temp = text1.getText();
					J1temp += (char)key;
					text1.setText(J1temp);
				}
				//С�����ϵļӼ��˳��Լ�������ϵļ��źͳ���(��������ϼ�)
				if( key == 106 || key == 107 || key == 109 || key == 111 || key == 45 || key == 47 ){															
					String J1temp = text1.getText();
					J1temp += (char)key;
					text1.setText(J1temp);
				}
				//����˰�ť���
				if(key == KeyEvent.VK_DELETE||key == KeyEvent.VK_C){
					text1.setText(null);
					text2.setText("0");
				}
				if(key == KeyEvent.VK_F){
					try {
						JFileChooser jfc = new JFileChooser();
						jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						jfc.showDialog(new JPanel(), "ѡ��");
						java.io.File file = jfc.getSelectedFile();
						if(file.isFile()){
							try {
								readFile(file.getAbsolutePath());
							} catch (IOException e1) {
								// TODO �Զ����ɵ� catch ��
								e1.printStackTrace();
							}
							System.out.println(file.getAbsolutePath());
						}
						else{
							System.out.println("�ļ���ʽ���ԣ�");
						}
					} catch (Exception e1) {
						// TODO: handle exception
					}
				}
				if(key == KeyEvent.VK_S){
					try {
						JFileChooser jfc = new JFileChooser();
						jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						jfc.showDialog(new JPanel(), "ѡ��");
						java.io.File file = jfc.getSelectedFile();
						if(file.isFile()){
							try {
								System.out.println(history.getText());
								saveFile(file.getAbsolutePath(),history.getText());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							System.out.println(file.getAbsolutePath());
						}
						else{
							System.out.println("��ѡ����ȷ·����");
						}
					} catch (Exception e1) {
						// TODO: handle exception
					}
				}
				else{}
			}
			//��ϼ��Ӻ�'shift' + ' '
			else{
				if(key == KeyEvent.VK_EQUALS){ 
					String J1temp = text1.getText();
					J1temp += "+";
					text1.setText(J1temp);
				}
				if(key == KeyEvent.VK_9){
					String J1temp = text1.getText();
					J1temp += "(";
					text1.setText(J1temp);
				}
				if(key == KeyEvent.VK_0){
					String J1temp = text1.getText();
					J1temp += ")";
					text1.setText(J1temp);
				}
				if(key == KeyEvent.VK_8){
					String J1temp = text1.getText();
					J1temp += "x";
					text1.setText(J1temp);
				}
				if(key == KeyEvent.VK_5){
					String J1temp = text1.getText();
					J1temp += "%";
					text1.setText(J1temp);
				}
			}
		}
	}
	//button[i].addKeyListener(new KeyMonitor());

}
