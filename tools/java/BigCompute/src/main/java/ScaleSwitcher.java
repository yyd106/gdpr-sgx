import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ScaleSwitcher {
    private JPanel mainPanel;
    private JFormattedTextField orgScale;
    private JButton decimal2hex;
    private JButton hex2decimal;
    private JFormattedTextField desScale;
    private JButton addInput;
    private JPanel orgPanel;
    private JPanel desPanel;
    private JButton delInput;
    private JButton fromFileButton;
    ArrayList<JFormattedTextField> orgTextFieldArry = new ArrayList<JFormattedTextField>();
    ArrayList<JFormattedTextField> desTextFieldArry = new ArrayList<JFormattedTextField>();

    String scaleFile;
    
    private void addInputField(String fileName){
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int inputFAcc = 0;
            while((line=br.readLine()) != null) {
                if(orgTextFieldArry.size() <= inputFAcc) {
                    JFormattedTextField tmpField = new JFormattedTextField();
                    tmpField.setText(line);
                    orgTextFieldArry.add(tmpField);
                    orgPanel.add(tmpField);
                    orgPanel.updateUI();
                    // add des input field
                    JFormattedTextField tmpField_des = new JFormattedTextField();
                    desTextFieldArry.add(tmpField_des);
                    desPanel.add(tmpField_des);
                    desPanel.updateUI();
                } else {
                    orgTextFieldArry.get(inputFAcc).setText(line);
                }
                inputFAcc++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public ScaleSwitcher() {
        orgPanel.setLayout(new BoxLayout(orgPanel,BoxLayout.PAGE_AXIS));
        desPanel.setLayout(new BoxLayout(desPanel,BoxLayout.PAGE_AXIS));
        orgTextFieldArry.add(orgScale);
        desTextFieldArry.add(desScale);

        decimal2hex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int inputAcc = orgTextFieldArry.size();
                for(int i=0;i<inputAcc;i++) {
                    String num_str = orgTextFieldArry.get(i).getText();
                    if(num_str == null || num_str.length() == 0) {
//                        JOptionPane.showMessageDialog(null,
//                                "Please input number",
//                                "Error",
//                                JOptionPane.ERROR_MESSAGE);
                        desTextFieldArry.get(i).setText("");
                        continue;
                    }
                    String pattern = "[0-9]+";
                    boolean flag = Pattern.matches(pattern,num_str);
                    if(flag) {
                        desTextFieldArry.get(i).setText(new BigInteger(num_str, 10).toString(16));
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Invalid decimal string!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        hex2decimal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(orgTextFieldArry.size() != desTextFieldArry.size()) {
                    JOptionPane.showMessageDialog(null,
                            "input field doesn't equal output field!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                int inputAcc = orgTextFieldArry.size();
                for(int i=0;i<inputAcc;i++) {
                    String num_str = orgTextFieldArry.get(i).getText();
                    if (num_str == null || num_str.length() == 0) {
//                        JOptionPane.showMessageDialog(null,
//                                "Please input number",
//                                "Error",
//                                JOptionPane.ERROR_MESSAGE);
                        desTextFieldArry.get(i).setText("");
                        continue;
                    }
                    String pattern = "[0-9a-zA-Z]+";
                    boolean flag = Pattern.matches(pattern, num_str);
                    if (flag) {
                        desTextFieldArry.get(i).setText(new BigInteger(num_str, 16).toString(10));
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Invalid hex string!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        addInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFormattedTextField orgTmp = new JFormattedTextField();
                orgTextFieldArry.add(orgTmp);
                orgPanel.add(orgTmp);
                orgPanel.updateUI();

                JFormattedTextField desTmp = new JFormattedTextField();
                desTextFieldArry.add(desTmp);
                desPanel.add(desTmp);
                desPanel.updateUI();
            }
        });
        delInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int orgCompNum = orgPanel.getComponentCount();
                if(orgCompNum > 1) {
                    orgPanel.remove(orgCompNum-1);
                    orgPanel.updateUI();
                    orgTextFieldArry.remove(orgCompNum-1);
                }
                int desComNum = desPanel.getComponentCount();
                if(desComNum > 1) {
                    desPanel.remove(desComNum-1);
                    desPanel.updateUI();
                    desTextFieldArry.remove(desComNum-1);
                }
            }
        });
        fromFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file == null || file.isDirectory()) {
                    JOptionPane.showMessageDialog(null,
                            "请选择一个文件",
                            "",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                scaleFile = file.getAbsolutePath();
                addInputField(scaleFile);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ScaleSwitcher");
        frame.setPreferredSize(new Dimension(850,450));
        frame.setLocation(500,300);
        frame.setContentPane(new ScaleSwitcher().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
