package com.showtime;

import java.awt.Container;
//import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ShowTime {
	public static void main(String[] args) {
        final JFrame frame = new JFrame("ShowTime");
        final Container content = frame.getContentPane();
        //content.setLayout(new GridBagLayout());
        content.setLayout(null);
        
        final JButton button = new JButton("请选择时间戳文件...");
        content.add(button);
        button.setBounds(30, 30, 180, 30);
        
        final JTextField intext = new JTextField("所选文件路径");
        content.add(intext);
        intext.setBounds(30, 80, 350, 30);
        
        //final JLabel label = new JLabel("执行结果：");
        final JTextArea ta = new JTextArea("执行结果：");
        ta.setLineWrap(true);
        content.add(ta);
        ta.setBounds(30, 130, 350, 40);
       
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                	File file = fileChooser.getSelectedFile();
                	String input = file.getAbsolutePath();
                	intext.setText(input);
                	parseFile(file, ta);
                }
            }
        });
       
        frame.setSize(450,250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
	
	@SuppressWarnings("resource")
	private static void parseFile(File file, JTextArea ta) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String filename = file.getAbsolutePath();
			int index = filename.lastIndexOf(".");
			String outFile;
			if (index >= 0) {
				outFile = filename.substring(0, index) + "_out.txt";
			} else {
				outFile = filename + "_out.txt";
			}
			ta.setText("解析成功，输出文件路径：" + outFile);
			bw = new BufferedWriter(new FileWriter(outFile));
			int lineIndex = 1;
			long lastTime = 0;
			long curTime = 0;
			long interval = 0;
			String lineStr;
			while((lineStr = br.readLine()) != null) {
				bw.write(lineStr + "     ");
				curTime = Long.parseLong(lineStr);
				bw.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(curTime)) + ":" + curTime % 1000 + "     ");
				if (lineIndex <= 1) {
					lastTime = curTime;
				}
				interval = curTime - lastTime;
				bw.write("" + interval / 3600000 + " h " + interval % 3600000 / 60000 + " m " + (float)(interval % 60000) / 1000 + " s" + "\n");
				lastTime = curTime;
				lineIndex++;
			}
			bw.write("There are " + lineIndex + " items in all.\n");
		} catch(Exception e) {
			ta.setText("文件解析异常，请确认文件内容！");
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
