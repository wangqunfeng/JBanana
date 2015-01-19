package jom;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


@SuppressWarnings("serial")
public class JBanana extends JDialog {

	private final JPanel panelZip = new JPanel();
	private final JPanel panelUnZip = new JPanel();
	private JTextField txtZipSrcPath;
	private JTextField txtZipDestDir;
	private JPasswordField passwdZipPasswd;
	private JPasswordField passwdUnZipPasswd;
	private JComboBox<String> combZipFileSuffix;
	private JTextField txtUnZipSrcFile;
	private JTextField txtUnZipDestPath;
	private JTextArea txtZipProcessLog;
	private JTextArea txtUnZipProcessLog;
	private JCheckBox ckUnZipDelSrcAfterSuccess;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JBanana dialog = new JBanana();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public JBanana() {
	    setResizable(false);
		setBounds(100, 100, 610, 401);
		setTitle("С�㽶");
		getContentPane().setLayout(null);

		getContentPane().add(panelZip, BorderLayout.SOUTH);
		panelZip.setBounds(10, 10, 582, 176);
		panelZip.setLayout(null);
		panelZip.setBorder(BorderFactory.createTitledBorder("ѹ����"));
		
		txtZipSrcPath = new JTextField();
		txtZipSrcPath.setBounds(96, 29, 209, 23);
		panelZip.add(txtZipSrcPath);
		txtZipSrcPath.setColumns(10);
		
		JButton btZipSrcPath = new JButton("ѡ���ļ�");
		btZipSrcPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
				jfc.showDialog(new JLabel(), "\u9009\u62E9");
				File path =jfc.getSelectedFile();
				if(path.exists())
					txtZipSrcPath.setText(path.getAbsolutePath());
			}
		});
		btZipSrcPath.setBounds(315, 29, 93, 23);
		panelZip.add(btZipSrcPath);
		
		txtZipDestDir = new JTextField();
		txtZipDestDir.setBounds(96, 57, 209, 23);
		panelZip.add(txtZipDestDir);
		txtZipDestDir.setColumns(10);
		
		JButton btZipDestDir = new JButton("ѡ��Ŀ¼");
		btZipDestDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
				jfc.showDialog(new JLabel(), "\u9009\u62E9");
				File file=jfc.getSelectedFile();
				if(file.exists() && file.isDirectory())
				{
					// s1�Զ����ɵ��ļ���
					String s1 = new String();
					s1=(new SimpleDateFormat("yyyyMMdd_HHmmssSSS")).format(new Date());
					String path = file.getAbsolutePath();
					if(path.lastIndexOf('\\') != path.length() -1 )
						path = path + "\\";
					txtZipDestDir.setText(path +s1);
					
					//�����ѡ���ļ���׺����������ļ���׺��
					if(combZipFileSuffix.getSelectedIndex()!=-1)
					{
						txtZipDestDir.setText(txtZipDestDir.getText() + (String)combZipFileSuffix.getSelectedItem());
					}
				}
			}
		});
		btZipDestDir.setBounds(315, 57, 93, 23);
		panelZip.add(btZipDestDir);
		
		JLabel lbZipDestDir = new JLabel("ָ��ѹ��·��");
		lbZipDestDir.setBounds(10, 57, 87, 22);
		panelZip.add(lbZipDestDir);
		
		JLabel lbZipSrcPath = new JLabel("ѡ���ѹ�ļ�");
		lbZipSrcPath.setBounds(10, 29, 87, 23);
		panelZip.add(lbZipSrcPath);
		
		combZipFileSuffix = new JComboBox<String>();
		combZipFileSuffix.setEditable(true);
		//�Զ����ļ���׺���¼��������µĺ�׺������<Enter>
		combZipFileSuffix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isaddItem=true;
				int index = 0;
			    String tmp=(String)combZipFileSuffix.getSelectedItem();
			    //�ж��û����������Ŀ�Ƿ����ظ��������ظ������ӵ�JComboBox�С�
			    for(;index<combZipFileSuffix.getItemCount();index++){
				   if (((String)combZipFileSuffix.getItemAt(index)).equals(tmp)){
					   isaddItem=false;
					   break;
				   }
				}
			    //�������
			    if(isaddItem)
			    {
			    	combZipFileSuffix.insertItemAt(tmp,0);
			    	combZipFileSuffix.setSelectedIndex(0);
			    }
			    //ѡ��һ��
			    else if(index <combZipFileSuffix.getItemCount())
			    {
			    	combZipFileSuffix.setSelectedIndex(index);
			    }
			}
		});
		//����ѹ���ļ���׺��ʱ�Զ�����ѹ���ļ���
		combZipFileSuffix.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED && !txtZipDestDir.getText().isEmpty())
				{
					//���ѹ���ļ��к�׺������º�׺��
					//��һ����Ӻ�׺���϶���ѡ���ļ�����ģ��������ڱ��¼������
					String s1 = txtZipDestDir.getText();
					if(s1.lastIndexOf('.')!=-1)
					{
						s1 = s1.substring(0, s1.lastIndexOf('.')) + (String)combZipFileSuffix.getSelectedItem();
						txtZipDestDir.setText(s1);
					}
				}
			}
		});
		combZipFileSuffix.setModel(new DefaultComboBoxModel<String>(new String[] {".log", ".txt", ".xml"}));
		combZipFileSuffix.setSelectedIndex(0);
		combZipFileSuffix.setToolTipText("");
		combZipFileSuffix.setBounds(495, 29, 77, 23);
		panelZip.add(combZipFileSuffix);
		
		passwdZipPasswd = new JPasswordField();
		passwdZipPasswd.setBounds(495, 57, 77, 23);
		panelZip.add(passwdZipPasswd);
		
		JLabel lbZipFileSuffix = new JLabel("�ļ���׺");
		lbZipFileSuffix.setBounds(418, 29, 77, 23);
		panelZip.add(lbZipFileSuffix);
		
		JLabel label = new JLabel("ѹ������");
		label.setBounds(418, 57, 77, 23);
		panelZip.add(label);
		
		JButton btZipDoZip = new JButton("ѹ��");
		btZipDoZip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String alertStr = new String();
				String stZipDestPath = txtZipDestDir.getText();
				String stZipSrcPath = txtZipSrcPath.getText();
                char []arrpasswd = new char[64];
                Arrays.fill(arrpasswd, (char)0);
                arrpasswd = passwdZipPasswd.getPassword();
                String stPasswd = new String(arrpasswd);
                
                if((stZipSrcPath == null || stZipSrcPath.isEmpty()) || (stPasswd == null || stPasswd.isEmpty()) ||
				   (stZipDestPath == null || stZipDestPath.isEmpty() || 
				   (stZipDestPath.lastIndexOf('.') <= 0 || stZipDestPath.lastIndexOf('.') >= stZipDestPath.length()-1)))
				{
					alertStr = "����������룬������ִ��ѹ�����";
				}
				else
				{
					try{
						if(false ==JZipCom.zip(stZipSrcPath, stZipDestPath, stPasswd))
						{
							alertStr = "ѹ��ʧ�ܣ�������or���Գ���";
						}else{
							alertStr = "�ɹ�ѹ��Ϊ" + stZipDestPath + "!";
						}
					}catch(Exception e1){
						txtZipProcessLog.setText(e1.getMessage());
					}finally{
						txtZipProcessLog.setText(txtZipProcessLog.getText() + JZipCom.getMsg());
					}					
				}
				if(!alertStr.isEmpty())
					JOptionPane.showMessageDialog(null, alertStr);
			}
		});
		btZipDoZip.setBounds(418, 89, 67, 67);
		panelZip.add(btZipDoZip);
		
		JButton btZipReset = new JButton("����");
		btZipReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtZipSrcPath.setText("");
				txtZipDestDir.setText("");
				passwdZipPasswd.setText("");
				combZipFileSuffix.setSelectedIndex(0);
				txtZipProcessLog.setText("");
			}
		});
		btZipReset.setBounds(505, 89, 67, 67);
		panelZip.add(btZipReset);
		
		txtZipProcessLog = new JTextArea();
		txtZipProcessLog.setBounds(10, 89, 398, 67);
		txtZipProcessLog.setWrapStyleWord(true);
		txtZipProcessLog.setLineWrap(true);
		JScrollPane sc = new JScrollPane(txtZipProcessLog);
		sc.setBounds(10, 89, 398, 67);
		sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelZip.add(sc);

		getContentPane().add(panelUnZip, BorderLayout.SOUTH);
		panelUnZip.setBounds(10, 196, 582, 166);
		panelUnZip.setLayout(null);
		panelUnZip.setBorder(BorderFactory.createTitledBorder("��ѹ����"));
		
		txtUnZipSrcFile = new JTextField();
		txtUnZipSrcFile.setColumns(10);
		txtUnZipSrcFile.setBounds(96, 23, 209, 23);
		panelUnZip.add(txtUnZipSrcFile);
		
		JLabel label_1 = new JLabel("ѡ��ѹ���ļ�");
		label_1.setBounds(10, 23, 87, 23);
		panelUnZip.add(label_1);
		
		JButton btUnZipSrcFile = new JButton("ѡ���ļ�");
		btUnZipSrcFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
				jfc.showDialog(new JLabel(), "\u9009\u62E9");
				File file=jfc.getSelectedFile();
				if(file.exists() && file.isFile())
				{
					String path = file.getAbsolutePath();
					txtUnZipSrcFile.setText(path);
				}
			}
		});
		btUnZipSrcFile.setBounds(315, 23, 93, 23);
		panelUnZip.add(btUnZipSrcFile);
		
		JButton btUnZipDestPath = new JButton("ѡ��Ŀ¼");
		btUnZipDestPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
				jfc.showDialog(new JLabel(), "\u9009\u62E9");
				File file=jfc.getSelectedFile();
				if(file.exists() && file.isDirectory())
				{
					String path = file.getAbsolutePath();					
					if(path.lastIndexOf('\\') != path.length() -1 )
						path = path + "\\";
					txtUnZipDestPath.setText(path);
				}				
			}
		});
		btUnZipDestPath.setBounds(315, 51, 93, 23);
		panelUnZip.add(btUnZipDestPath);
		
		txtUnZipDestPath = new JTextField();
		txtUnZipDestPath.setColumns(10);
		txtUnZipDestPath.setBounds(96, 51, 209, 23);
		panelUnZip.add(txtUnZipDestPath);
		
		JLabel label_2 = new JLabel("ָ����ѹĿ¼");
		label_2.setBounds(10, 51, 87, 22);
		panelUnZip.add(label_2);
		
		JLabel label_3 = new JLabel("��ѹ����");
		label_3.setBounds(418, 51, 77, 23);
		panelUnZip.add(label_3);
		
		passwdUnZipPasswd = new JPasswordField();
		passwdUnZipPasswd.setBounds(495, 51, 77, 23);
		panelUnZip.add(passwdUnZipPasswd);
		
		txtUnZipProcessLog = new JTextArea();
		txtUnZipProcessLog.setBounds(10, 84, 398, 67);
		txtUnZipProcessLog.setWrapStyleWord(true);
		txtUnZipProcessLog.setLineWrap(true);
		JScrollPane sc1 = new JScrollPane(txtUnZipProcessLog);
		sc1.setBounds(10, 84, 398, 67);
		sc1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sc1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelUnZip.add(sc1);
		
		JButton btUnZipDoUnZip = new JButton("��ѹ");
		btUnZipDoUnZip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    JOptionPane.showMessageDialog(null, passwdUnZipPasswd.getPassword().toString());
				String alertStr=new String("");
				String stUnZipSrcFile = txtUnZipSrcFile.getText();
				String stUnZipDestPath = txtUnZipDestPath.getText();
                char []arrpasswd = new char[64];
                Arrays.fill(arrpasswd, (char)0);
                arrpasswd = passwdUnZipPasswd.getPassword();
                String stPasswd = new String(arrpasswd);
                boolean bDeleteSrcAfterSuccess = ckUnZipDelSrcAfterSuccess.isSelected();
                if((stUnZipDestPath == null || stUnZipDestPath.isEmpty()) || (stPasswd == null || stPasswd.isEmpty()) ||
                   (stUnZipSrcFile == null || stUnZipSrcFile.isEmpty() || 
                   (stUnZipSrcFile.lastIndexOf('.') <= 0 || stUnZipSrcFile.lastIndexOf('.') >= stUnZipSrcFile.length()-1)))
				{
					alertStr = "����������룬������ִ�н�ѹ���";
				}
				else
				{
					try{
						if(false == JZipCom.unzip(stUnZipSrcFile, stUnZipDestPath, stPasswd, bDeleteSrcAfterSuccess)) {
							alertStr = "��ѹʧ�ܣ�������or���Գ���";
						}else{
							alertStr = "�ɹ���ѹ��" + stUnZipDestPath + "��";
						}
					}catch(Exception e1){
						txtUnZipProcessLog.setText(e1.getMessage());
					}finally{
						txtUnZipProcessLog.setText(txtUnZipProcessLog.getText() + JZipCom.getMsg());
					}
				}
				if(!alertStr.isEmpty())
					JOptionPane.showMessageDialog(null, alertStr);
			}
		});
		btUnZipDoUnZip.setBounds(418, 84, 67, 67);
		panelUnZip.add(btUnZipDoUnZip);
		
		JButton btUnZipReset = new JButton("����");
		btUnZipReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUnZipSrcFile.setText("");
				txtUnZipDestPath.setText("");
				passwdUnZipPasswd.setText("");
				txtUnZipProcessLog.setText("");
			}
		});
		btUnZipReset.setBounds(505, 84, 67, 67);
		panelUnZip.add(btUnZipReset);
		
		ckUnZipDelSrcAfterSuccess = new JCheckBox("��ѹ�ɹ���ɾ��Դ�ļ�");
		ckUnZipDelSrcAfterSuccess.setSelected(true);
		ckUnZipDelSrcAfterSuccess.setBounds(418, 23, 154, 23);
		panelUnZip.add(ckUnZipDelSrcAfterSuccess);
	}
}
