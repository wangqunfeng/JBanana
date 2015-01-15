package jom;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class JZipCom {
    private static String msg = new String();

    /** 解压缩
     * @param zipFilePath 已压缩文件名
     * @param destDirectory 目标解压路径
     * @param passwd 解压密码 如果有密码，要求密码长度不少于6位
     * @param delSrcZipFile 解压成功后是否删除源文件
     * @return true/false 解压成功/不成功
     * @throws Exception 7zip解压时可能会抛出异常
     */
    public static boolean unzip(String zipFilePath, String destDirectory, String passwd, boolean delSrcZipFile) {
        setMsg("Begin to upzip " + zipFilePath + " to " + destDirectory + "\n\r");
        String cmd = "D:\\Program Files\\7-Zip\\7z.exe";

        // 检查参数
        // 如果有密码，要求密码长度不少于6位
        if ((destDirectory == null || destDirectory.isEmpty()) || (zipFilePath == null || zipFilePath.isEmpty()) ||
            (passwd!=null && !passwd.isEmpty() && passwd.length()<6)) {
            setMsg(getMsg() + "Invalid arguments for 7zip!\n\r");
            return false;
        }

        // 确保目录的路径最后面有"\"符号
        if (destDirectory.charAt(destDirectory.length() - 1) != '\\')
            destDirectory = destDirectory + "\\";
        
        // 检查已压缩文件名的后缀，'.'存在，且在文件名中间位置
        if (0 >= zipFilePath.lastIndexOf('.') || zipFilePath.lastIndexOf('.') == (zipFilePath.length() - 1)) {
            setMsg(getMsg() + "Wrong zip file type(invalid suffix)!\n\r");
            return false;
        }
        String newZipFilePath = zipFilePath.substring(0, zipFilePath.lastIndexOf('.')) + ".7z";
        // 尝试执行解压、读取控制台输出信息、解析7zip进程返回值、 捕获异常
        try {
           // 检查已压缩文件和目的解压路径是否都存在
           if (!new File(zipFilePath).isFile() || !new File(destDirectory).isDirectory()) {
                throw new Exception("Zipped file or destination directory does not exists!");
           }
           
           // 将已压缩文件的文件名后缀修改为.7z，7zip解压时依据压缩文件名后缀识别压缩格式
           if (new File(zipFilePath).renameTo(new File(newZipFilePath)) != true) {
                throw new Exception("Rename zipped file fail!");
            }
            setMsg(getMsg() + "Move " + zipFilePath + " to " + newZipFilePath + "\n\r");

            if (passwd != null && !passwd.isEmpty())
                cmd = String.format("%s x \"%s\" -p%s -o\"%s\"", cmd, newZipFilePath, passwd, destDirectory);
            else
                cmd = String.format("%s x \"%s\" -o\"%s\"", cmd, zipFilePath, destDirectory);

            setMsg(getMsg() + cmd + "\n\r");

            // 执行可执行程序7zip进行压缩
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);
            
            // 将程序执行时的输入输出重定向到当前控制台
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                setMsg(getMsg() + lineStr + "\n\r");
            inBr.close();
            in.close();

            // 检查程序返回值，参见7zip帮助手册
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
            // 如果选择了成功后删除源文件，此处删除源压缩文件
            if (delSrcZipFile == true) {
                if (true != new File(newZipFilePath).delete()) {
                    throw new Exception("Delete source zipped file fail!");
                }
            } else {
            // 如果未选择成功后删除源文件，此处将把带.7z后缀的源压缩文件后缀名恢复成为初始压缩文件名
                if (new File(newZipFilePath).renameTo(new File(zipFilePath)) != true) {
                    throw new Exception("Rename zipped file back fail!");
                }
            }
            // 执行到这里已经确定操作成功了
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 压缩文件
     * @param sourceFilePath 源文件路径/名字
     * @param zipFilePath 指定压缩包路径（包含文件名）
     * @param passwd 解压密码设置，如果设置密码，要求密码长度不少于6位
     * @return true/false 成功/失败
     * @throws 7zip执行时可能抛出异常
     */
    public static boolean zip(String sourceFilePath, String zipFilePath, String passwd) {
        setMsg("Begin to zip " + sourceFilePath + " to " + zipFilePath + ".7z\n\r");
        String cmd = "D:\\Program Files\\7-Zip\\7z.exe";
        String suffix = new String("");

        // 提取文件扩展名与基本名
        suffix = zipFilePath.substring(zipFilePath.lastIndexOf('.') > 0 ? zipFilePath.lastIndexOf('.') : 0,
                zipFilePath.length());
        zipFilePath = zipFilePath.substring(0, zipFilePath.lastIndexOf('.') > 0 ? zipFilePath.lastIndexOf('.') : 0);

        // 检查语法
        if (sourceFilePath == null || zipFilePath == null || suffix == null || sourceFilePath.isEmpty()
                || zipFilePath.isEmpty() || suffix.length() < 2 || zipFilePath.lastIndexOf('\\') < 0
                || suffix.indexOf('.') != 0 || (passwd!=null && !passwd.isEmpty() && passwd.length()<6)) {
            setMsg(getMsg() + "Invalid arguments for Main::zip()" + "\n\r");
            return false;
        }

        // 检查语义，源路径是否存在
        File fsrc = new File(sourceFilePath);
        if (!fsrc.exists()) {
            setMsg(getMsg() + sourceFilePath + " is not a valid source path to zip" + "\n\r");
            return false;
        }
        // 检查语义，目的路径是否存在
        if (zipFilePath.lastIndexOf('\\') > 0) {
            if (!(new File(zipFilePath.substring(0, zipFilePath.lastIndexOf('\\')))).isDirectory()) {
                setMsg(getMsg() + zipFilePath + " is not a valid dest path for zip" + "\n\r");
                return false;
            }
        }

        // 尝试执行压缩、读取控制台输出信息、解析7zip进程返回值、 捕获异常
        try {
            if (passwd != null && !passwd.isEmpty())
                cmd = String.format("%s a \"%s.7z\" -p%s -mhe \"%s\"", cmd, zipFilePath, passwd, sourceFilePath);
            else
                cmd = String.format("%s a \"%s.7z\" \"%s\"", cmd, zipFilePath, sourceFilePath);

            setMsg(getMsg() + cmd + "\n\r");

            // 执行可执行程序进行压缩
            Runtime run = Runtime.getRuntime();
            Process p = run.exec(cmd);

            // 将程序执行时的输入输出重定向到当前控制台
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = inBr.readLine()) != null)
                setMsg(getMsg() + lineStr + "\n\r");
            inBr.close();
            in.close();

            // 检查程序返回值，参见7zip帮助手册
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

        // 修改压缩文件扩展名
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
