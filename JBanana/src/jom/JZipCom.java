package jom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class JZipCom {
    private static String msg = new String();

    /** ��ѹ��
     * @param zipFilePath ��ѹ���ļ���
     * @param destDirectory Ŀ���ѹ·��
     * @param passwd ��ѹ���� ��������룬Ҫ�����볤�Ȳ�����6λ
     * @param delSrcZipFile ��ѹ�ɹ����Ƿ�ɾ��Դ�ļ�
     * @return true/false ��ѹ�ɹ�/���ɹ�
     * @throws Exception 7zip��ѹʱ���ܻ��׳��쳣
     */
    public static boolean unzip(String zipFilePath, String destDirectory, String passwd, boolean delSrcZipFile) {
        setMsg("Begin to upzip " + zipFilePath + " to " + destDirectory + "\n\r");
        String cmd = "D:\\Program Files\\7-Zip\\7z.exe";

        // ������
        // ��������룬Ҫ�����볤�Ȳ�����6λ
        if ((destDirectory == null || destDirectory.isEmpty()) || (zipFilePath == null || zipFilePath.isEmpty()) ||
            (passwd!=null && !passwd.isEmpty() && passwd.length()<6)) {
            setMsg(getMsg() + "Invalid arguments for 7zip!\n\r");
            return false;
        }

        // ȷ��Ŀ¼��·���������"\"����
        if (destDirectory.charAt(destDirectory.length() - 1) != '\\')
            destDirectory = destDirectory + "\\";
        
        // �����ѹ���ļ����ĺ�׺��'.'���ڣ������ļ����м�λ��
        if (0 >= zipFilePath.lastIndexOf('.') || zipFilePath.lastIndexOf('.') == (zipFilePath.length() - 1)) {
            setMsg(getMsg() + "Wrong zip file type(invalid suffix)!\n\r");
            return false;
        }
        String newZipFilePath = zipFilePath.substring(0, zipFilePath.lastIndexOf('.')) + ".7z";
        // ����ִ�н�ѹ����ȡ����̨�����Ϣ������7zip���̷���ֵ�� �����쳣
        try {
           // �����ѹ���ļ���Ŀ�Ľ�ѹ·���Ƿ񶼴���
           if (!new File(zipFilePath).isFile() || !new File(destDirectory).isDirectory()) {
                throw new Exception("Zipped file or destination directory does not exists!");
           }
           
           // ����ѹ���ļ����ļ�����׺�޸�Ϊ.7z��7zip��ѹʱ����ѹ���ļ�����׺ʶ��ѹ����ʽ
           if (new File(zipFilePath).renameTo(new File(newZipFilePath)) != true) {
                throw new Exception("Rename zipped file fail!");
            }
            setMsg(getMsg() + "Move " + zipFilePath + " to " + newZipFilePath + "\n\r");

            if (passwd != null && !passwd.isEmpty())
                cmd = String.format("%s x \"%s\" -p%s -o\"%s\"", cmd, newZipFilePath, passwd, destDirectory);
            else
                cmd = String.format("%s x \"%s\" -o\"%s\"", cmd, zipFilePath, destDirectory);

            setMsg(getMsg() + cmd + "\n\r");

            // ִ�п�ִ�г���7zip����ѹ��
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            
            // ������ִ��ʱ����������ض��򵽵�ǰ����̨
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                setMsg(getMsg() + lineStr + "\n\r");
            inBr.close();
            in.close();

            // �����򷵻�ֵ���μ�7zip�����ֲ�
            if (p.waitFor() != 0) {
                if (p.exitValue() != 0) {
                    if (p.exitValue() == 1) {
                        setMsg(getMsg() + "There is some warning\n\r");
                    } else {
                        switch (p.exitValue()) {
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
                            setMsg(getMsg() + "Failed to execute 7zip" + "\n\r");
                        }
                        return false;
                    }
                }
            }
            // ���ѡ���˳ɹ���ɾ��Դ�ļ����˴�ɾ��Դѹ���ļ�
            if (delSrcZipFile == true) {
                if (true != new File(newZipFilePath).delete()) {
                    throw new Exception("Delete source zipped file fail!");
                }
            } else {
            // ���δѡ��ɹ���ɾ��Դ�ļ����˴����Ѵ�.7z��׺��Դѹ���ļ���׺���ָ���Ϊ��ʼѹ���ļ���
                if (new File(newZipFilePath).renameTo(new File(zipFilePath)) != true) {
                    throw new Exception("Rename zipped file back fail!");
                }
            }
            // ִ�е������Ѿ�ȷ�������ɹ���
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ѹ���ļ�
     * @param sourceFilePath Դ�ļ�·��/����
     * @param zipFilePath ָ��ѹ����·���������ļ�����
     * @param passwd ��ѹ�������ã�����������룬Ҫ�����볤�Ȳ�����6λ
     * @return true/false �ɹ�/ʧ��
     * @throws 7zipִ��ʱ�����׳��쳣
     */
    public static boolean zip(String sourceFilePath, String zipFilePath, String passwd) {
        setMsg("Begin to zip " + sourceFilePath + " to " + zipFilePath + ".7z\n\r");
        String cmd = "D:\\Program Files\\7-Zip\\7z.exe";
        String suffix = new String("");

        // ��ȡ�ļ���չ���������
        suffix = zipFilePath.substring(zipFilePath.lastIndexOf('.') > 0 ? zipFilePath.lastIndexOf('.') : 0,
                zipFilePath.length());
        zipFilePath = zipFilePath.substring(0, zipFilePath.lastIndexOf('.') > 0 ? zipFilePath.lastIndexOf('.') : 0);

        // ����﷨
        if (sourceFilePath == null || zipFilePath == null || suffix == null || sourceFilePath.isEmpty()
                || zipFilePath.isEmpty() || suffix.length() < 2 || zipFilePath.lastIndexOf('\\') < 0
                || suffix.indexOf('.') != 0 || (passwd!=null && !passwd.isEmpty() && passwd.length()<6)) {
            setMsg(getMsg() + "Invalid arguments for Main::zip()" + "\n\r");
            return false;
        }

        // ������壬Դ·���Ƿ����
        File fsrc = new File(sourceFilePath);
        if (!fsrc.exists()) {
            setMsg(getMsg() + sourceFilePath + " is not a valid source path to zip" + "\n\r");
            return false;
        }
        // ������壬Ŀ��·���Ƿ����
        if (zipFilePath.lastIndexOf('\\') > 0) {
            if (!(new File(zipFilePath.substring(0, zipFilePath.lastIndexOf('\\')))).isDirectory()) {
                setMsg(getMsg() + zipFilePath + " is not a valid dest path for zip" + "\n\r");
                return false;
            }
        }

        // ����ִ��ѹ������ȡ����̨�����Ϣ������7zip���̷���ֵ�� �����쳣
        try {
            if (passwd != null && !passwd.isEmpty())
                cmd = String.format("%s a \"%s.7z\" -p%s -mhe \"%s\"", cmd, zipFilePath, passwd, sourceFilePath);
            else
                cmd = String.format("%s a \"%s.7z\" \"%s\"", cmd, zipFilePath, sourceFilePath);

            setMsg(getMsg() + cmd + "\n\r");

            // ִ�п�ִ�г������ѹ��
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);

            // ������ִ��ʱ����������ض��򵽵�ǰ����̨
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                setMsg(getMsg() + lineStr + "\n\r");
            inBr.close();
            in.close();

            // �����򷵻�ֵ���μ�7zip�����ֲ�
            if (p.waitFor() != 0) {
                if (p.exitValue() == 0) {

                } else if (p.exitValue() == 1) {
                    setMsg(getMsg() + "There is some warning\n\r");
                } else {
                    switch (p.exitValue()) {
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
                        setMsg(getMsg() + "Failed to execute 7zip" + "\n\r");
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // �޸�ѹ���ļ���չ��
        File f1 = new File(zipFilePath + ".7z");
        if (!f1.renameTo(new File(zipFilePath + suffix))) {
            setMsg(getMsg() + "Failed to rename zip file:" + zipFilePath + "\n\r");
            return false;
        }
        setMsg(getMsg() + "Move " + zipFilePath + ".7z to " + zipFilePath + suffix + " OK!\n\r");
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
