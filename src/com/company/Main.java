package com.company;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.html.ObjectView;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static int fileLevel = 0;
    public static DefaultMutableTreeNode root;
    public static JTextField jTextField = new JTextField();

    public static String createPrintStr(String name, int level) {
        // 输出的前缀
        String printStr = "";
        // 按层次进行缩进
        for (int i = 0; i < level; i++) {
            printStr = printStr + " ";
        }
        printStr = printStr + "- " + name;
        return printStr;
    }

    public static void createTree(String dirPath, DefaultMutableTreeNode root) {
        File file = new File(dirPath);
        File[] list = file.listFiles();
        if (list == null) return;
        for (int i = 0; i < list.length; i++) {
            File item = list[i];
            if (item.isDirectory()) {
                //System.out.println(createPrintStr(item.getName(), fileLevel));
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(item.getName());
                root.add(node);
                fileLevel++;
                // 递归子目录
                createTree(item.getPath(), node);
                fileLevel--;
            } else {
                //System.out.println(createPrintStr(item.getName(), fileLevel));
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(item.getName());
                root.add(node);
            }
        }
    }

    public static void main(String[] args) {
        String dirPath = "C:\\Users\\Public";
        File rootFile = new File(dirPath);
        root = new DefaultMutableTreeNode(rootFile.getPath());
        createTree(dirPath, root);

        JTree tree = new JTree(root);
        initTree(tree);
    }

    private static void initTree(JTree tree) {
        JFrame frame = new JFrame("FileChecker");
        GridBagLayout layout = new GridBagLayout();
        frame.setLayout(layout);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(tree);

        frame.add(jScrollPane);
        frame.add(jTextField);

        GridBagConstraints s = new GridBagConstraints();//定义一个GridBagConstraints，
        //是用来控制添加进的组件的显示位置
        s.fill = GridBagConstraints.BOTH;
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
        s.gridwidth = 0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 1;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty = 1;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        layout.setConstraints(jScrollPane, s);//设置组件

        s.gridwidth = 0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 1;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty = 0.1;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        layout.setConstraints(jTextField, s);//设置组件

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        // 添加选择事件
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();
                if (node == null)
                    return;
//                File object = (File) node.getUserObject();
                Object object = node.getUserObject();
                if (node.isLeaf()) {
                    String filepath = getFilePath(node);
                    System.out.println("你选择了：" + filepath);
                    File curFile = new File(filepath);
                    Format simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d = new Date(curFile.lastModified());
                    String dateString = simpleFormat.format(d);
                    jTextField.setText("绝对路径:" + curFile.getAbsolutePath() + " 文件大小:" + curFile.length() / 1024+ "kb" + " 修改时间:" + dateString);

                }
            }
        });
    }

    public static String getFilePath(DefaultMutableTreeNode treeObj) {
        if (treeObj == null) return "";
        String filename = treeObj.getUserObject().toString();
        DefaultMutableTreeNode pNode = (DefaultMutableTreeNode)treeObj.getParent();
        if (pNode != null) {
            filename = getFilePath(pNode) + "\\" + filename;
        }
        return filename;
    }
}
