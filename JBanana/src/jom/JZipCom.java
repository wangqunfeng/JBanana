package jom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class JZipCom {
	private static String msg=new String();
	
	public static boolean unzip(String zipFilePath, String destDirectory, String passwd, boolean delSrcZipFile) {
        
		setMsg("Begin to upzip " + zipFilePath + " to " + destDirectory + "\n\r");
		
		String cmd = "D:\\Program Files\\7-Zip\\7z.exe";
        
        //ȷ��Ŀ¼��·���������"\"����
        if(destDirectory.charAt(destDirectory.length()-1) !='\\')
            destDirectory = destDirectory + "\\";

        if (destDirectory == null || zipFilePath == null) {
            setMsg(getMsg() + "Invalid arguments for Main::zip()" + "\n\r");
            return false;
        }
        try {
            if(!new File(zipFilePath).isFile() || !new File(destDirectory).isDirectory())
            {
                throw new Exception("Zipped file or destination directory does not exists!");
            }
            if(0>=zipFilePath.lastIndexOf('.'))
            {
            	throw new Exception("Wrong zipped file name type!");
            }
            String newZipFilePath = zipFilePath.substring(0, zipFilePath.lastIndexOf('.')) + ".7z";
            if(new File(zipFilePath).renameTo(new File(newZipFilePath)) != true)
            {
                throw new Exception("Rename zipped file fail!");
            }
            setMsg(getMsg() + "Move "+zipFilePath +" to "+ newZipFilePath +"\n\r");
            
            if (passwd != null)
                // ��.7z��׺����ѹ
                cmd = String.format("%s x \"%s\" -p%s -o\"%s\"", cmd, newZipFilePath, passwd, destDirectory);
            else
                cmd = String.format("%s x \"%s\" -o\"%s\"", cmd, zipFilePath, destDirectory);

            setMsg(getMsg() + cmd + "\n\r");

            //ִ�п�ִ�г������ѹ��
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            //������ִ��ʱ����������ض��򵽵�ǰ����̨
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                setMsg(getMsg() + lineStr + "\n\r");
            inBr.close();
            in.close();

            //�����򷵻�ֵ
            if (p.waitFor() != 0) {
                if (p.exitValue() != 0){//p.exitValue()==0��ʾ����������1������������
                	if(p.exitValue() == 1){
                		setMsg(getMsg() + "There is some warning\n\r");
                	}
                	else{
                		switch(p.exitValue())
                		{
                		case 2:
                			setMsg(getMsg() + "Fatal error\n\r");
                			break;
                		case 7:
                			setMsg(getMsg() + "Command line error\n\r");
                			break;
                		case 8:
                			setMsg(getMsg() + "Not enough memory for operation\n\r");
                			break;
                		case 255:
                			setMsg(getMsg() + "User stopped the process\n\r");
                			break;
                		default:
                			setMsg(getMsg() + "Failed to execute 7zip"+ "\n\r");
                		}
                		return false;
                	}
                }
            }
            if(delSrcZipFile == true)
            {
            	if(true != new File(newZipFilePath).delete())
            	{
            		throw new Exception("Delete source zipped file fail!");
            	}
            }else{
        		if(new File(newZipFilePath).renameTo(new File(zipFilePath)) != true){
        			throw new Exception("Rename zipped file back fail!");
        		}
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**ѹ���ļ�
     * @param sourceFilePath Դ�ļ�·��/����
     * @param zipFilePath ָ��ѹ����·��/���֣���������չ��
     * @param passwd ��ѹ��������
     * @param suffix Ҫʹ�õ���չ������"."��ʼ
     * @return true/false �ɹ�/ʧ��
     */
    public static boolean zip(String sourceFilePath, String zipFilePath, String passwd, String suffix) {
		setMsg("Begin to zip " + sourceFilePath + " to " + zipFilePath + ".7z\n\r");
    	String cmd = "D:\\Program Files\\7-Zip\\7z.exe";
        if (sourceFilePath == null || zipFilePath == null) {
            setMsg(getMsg() + "Invalid arguments for Main::zip()" + "\n\r");
            return false;
        }
        try {

            if (passwd != null)
                cmd = String.format("%s a \"%s.7z\" -p%s -mhe \"%s\"", cmd, zipFilePath, passwd, sourceFilePath);
            else
                cmd = String.format("%s a \"%s.7z\" \"%s\"", cmd, zipFilePath, sourceFilePath);

            setMsg(getMsg() + cmd + "\n\r");

            //ִ�п�ִ�г������ѹ��
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            //������ִ��ʱ����������ض��򵽵�ǰ����̨
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                setMsg(getMsg() + lineStr + "\n\r");
            inBr.close();
            in.close();
            
            //�����򷵻�ֵ
            if (p.waitFor() != 0) {
                if(p.exitValue() == 1){
            		setMsg(getMsg() + "There is some warning\n\r");
            	}
            	else{
            		switch(p.exitValue())
            		{
            		case 2:
            			setMsg(getMsg() + "Fatal error\n\r");
            			break;
            		case 7:
            			setMsg(getMsg() + "Command line error\n\r");
            			break;
            		case 8:
            			setMsg(getMsg() + "Not enough memory for operation\n\r");
            			break;
            		case 255:
            			setMsg(getMsg() + "User stopped the process\n\r");
            			break;
            		default:
            			setMsg(getMsg() + "Failed to execute 7zip"+ "\n\r");
            		}
            		return false;
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //�޸�ѹ���ļ���չ��
        File f1 = new File(zipFilePath+".7z");
        if(!f1.renameTo(new File(zipFilePath + suffix)))
        {
        	setMsg(getMsg() + "Failed to rename zip file:"+zipFilePath + "\n\r");
            return false;
        }
        setMsg(getMsg() + "Move " + zipFilePath + ".7z to " + zipFilePath + suffix+" OK!\n\r");
        setMsg(getMsg() + "Zip file(s) finished!\n\r");
        return true;
    }

	public static String getMsg() {
		return msg;
	}

	private static void setMsg(String msg) {
		JZipCom.msg = msg;
	}

}
