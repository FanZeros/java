
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 
 *
 *
 *
 *
 * 2020/12/10
 *
 *  */

public class color extends JFrame {
    String url = "";
    int time = 0;
    int times = 1;
    double fillFrame = 1;
    int pixelValue = 1;
    int colorMin = 10, colorMax = 50;
    private JPanel panel = null;
    private JPanel panel1 = null;
    private JSlider slider = null;
    private BufferedImage img = null;
    private BufferedImage img1 = null;
    private BufferedImage img2 = null;
    private BufferedImage imgLast = null;

    JLabel lab1 = new JLabel("");
    JLabel lab2 = new JLabel("");
    JTextArea r1 = new JTextArea();
    JTextArea g1 = new JTextArea();
    JTextArea b1 = new JTextArea();
    JTextArea r2 = new JTextArea();
    JTextArea g2 = new JTextArea();
    JTextArea b2 = new JTextArea();
    JTextArea rg1 = new JTextArea();
    JTextArea gb1 = new JTextArea();
    JTextArea rg2 = new JTextArea();
    JTextArea gb2 = new JTextArea();
    JColorChooser op = new JColorChooser(Color.black);

    JTextArea num1 = new JTextArea();
    JTextArea num2 = new JTextArea();
    JTextArea num3 = new JTextArea();
    JTextArea num4 = new JTextArea();
    JTextArea num5 = new JTextArea();
    JTextArea num6 = new JTextArea();
    JTextArea num7 = new JTextArea();
    JTextArea num9 = new JTextArea();
    JTextArea num10 = new JTextArea();
    JTextArea num11 = new JTextArea();
    JTextArea num12 = new JTextArea();
    JTextArea num13 = new JTextArea();
    JTextArea num14 = new JTextArea();
    JTextArea numMin = new JTextArea();
    JTextArea numMax = new JTextArea();
    JTextArea value1 = new JTextArea();
    JRadioButton auto1, auto2, removeBlackWhite, removeBilibili, onlyBlackAndWhite, blackFirst, removeForeground, twoDimensionCode, lightsPoly2D, similar, contract, colorLimit, colorChange;
    private double value = 0;
    JButton Cal = new JButton("生成");
    JButton Cal1 = new JButton("预览");
    JButton load = new JButton("读取图片");
    JButton change = new JButton("转换图片");

    JComboBox<String> Tb = new JComboBox<String>(new String[]{"左上", "最近的", "不消除"});
    JComboBox<String> edgeTestMethod = new JComboBox<String>(new String[]{"1.仅最近的", "2.根据旁边8格的", "1的反转", "2的反转"});
    JComboBox<String> ColorChoose = new JComboBox<String>(new String[]{"限制到一半颜色", "限制到1/3颜色", "限制到1/5颜色", "限制到1/7颜色", "限制到1/10颜色"});
    JComboBox<String> ColorChoose1 = new JComboBox<String>(new String[]{"剔除相似大于100的颜色", "剔除相似大于50的颜色", "剔除相似大于30的颜色", "剔除相似大于20的颜色", "剔除相似大于10的颜色"});

    final JTabbedPane tabbedPane = new JTabbedPane();


    private double[] CalculateTotal = {0, 0, 0};
    private int width, height;
    private double r, g, b;
    private boolean stop = false;
    int numOfPoly2D = 30;
    int smooth = 0;


    public color() {
        initComponent();
        this.setVisible(true);
    }


    private void clickTry() {

        begin(true);

    }

    private void click() {
        begin(false);
    }

    private void begin(boolean try1) {
        int n1 = 0, n2 = 0, n3 = 0, n4 = 1, n5 = 0, n6 = 0, n7 = 0, n8 = 0, n9 = 0, n10 = 1, n11 = 1;
        try {
            smooth = Integer.parseInt(num12.getText());
        } catch (Exception e) {
        }
        try {
            colorMin = Integer.parseInt(numMin.getText());
        } catch (Exception e) {
        }
        try {
            colorMax = Integer.parseInt(numMax.getText());
        } catch (Exception e) {
        }
        try {
            n1 = Integer.parseInt(num1.getText());
        } catch (Exception e) {
        }
        try {
            n2 = Integer.parseInt(num2.getText());
        } catch (Exception e) {
        }
        try {
            n3 = Integer.parseInt(num3.getText());
        } catch (Exception e) {
        }
        try {
            n4 = Integer.parseInt(num4.getText());
        } catch (Exception e) {
        }
        try {
            n5 = Integer.parseInt(num5.getText());
        } catch (Exception e) {
        }
        try {
            n6 = Integer.parseInt(num6.getText());
        } catch (Exception e) {
        }
        try {
            n8 = Tb.getSelectedIndex();
        } catch (Exception e) {
        }
        try {
            n7 = Integer.parseInt(num7.getText());
        } catch (Exception e) {
        }
        try {
            n9 = Integer.parseInt(num9.getText());
        } catch (Exception e) {
        }
        try {
            n10 = Integer.parseInt(num10.getText());
        } catch (Exception e) {
        }
        try {
            n11 = Integer.parseInt(num11.getText());
        } catch (Exception e) {
        }
        try {
            pixelValue = Integer.parseInt(num14.getText());
            if (fillFrame < 1) fillFrame = 1;
        } catch (Exception e) {
        }
        try {
            fillFrame = Double.parseDouble(num13.getText());
            if (fillFrame < 1) fillFrame = 1;
        } catch (Exception e) {
        }
        if (auto1.isSelected()) {
            n1 = -1;
        }
        if (auto2.isSelected()) {
            n7 = -1;
        }

        if (try1) {
            processImg(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11);
        } else {
            for (times = 1; times < 10000; times++) {
                if (time < (int) (times / fillFrame + 1)) {
                    time = (int) (times / fillFrame + 1);
                    initImg(new File(url + time + ").jpg"));
                }
                processImg(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11);
                if (stop) break;
            }
        }

    }

    private void Load() {
        String path = System.getProperty("user.dir");
        JFileChooser choose = new JFileChooser(path);
        choose.setDialogTitle("Please Choose Picture");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "picture", "jpg");
        choose.setFileFilter(filter);
        int returnVal = choose.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = choose.getSelectedFile().getPath();
            initImg(new File(filepath));
            url = filepath.substring(0, filepath.lastIndexOf("(") + 1);

            try {
                times = Integer.parseInt(filepath.substring(filepath.lastIndexOf("(") + 1, filepath.lastIndexOf(")")));
            } catch (Exception e) {
            }
            System.out.println(url);
            System.out.println(times);
            panel1.repaint();
        }
    }

    private void initComponent() {

        lab1.setBounds(380, 60, 300, 70);
        lab1.setFont(new Font("", Font.BOLD, 15));
        lab2.setBounds(380, 260, 300, 70);
        lab2.setFont(new Font("", Font.BOLD, 15));

        this.setTitle("生成奇奇怪怪的图片吧 by 零殇");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1640, 1000);
        setLayout(null);

        auto1 = new JRadioButton("使用自动生成的数据", false);
        auto1.setBounds(112, 60, 200, 60);
        auto2 = new JRadioButton("使用自动生成的数据", false);
        auto2.setBounds(112, 260, 200, 60);
        removeBlackWhite = new JRadioButton("移除灰色斑", false);
        removeBlackWhite.setBounds(52, 500, 360, 40);
        removeBilibili = new JRadioButton("移除右上角'Bilibili'", false);
        removeBilibili.setBounds(52, 460, 260, 40);
        onlyBlackAndWhite = new JRadioButton("使图片以黑白展示", false);
        onlyBlackAndWhite.setBounds(82, 480, 330, 40);
        blackFirst = new JRadioButton("暗色边缘优先去除", false);
        blackFirst.setBounds(52, 405, 200, 40);
        removeForeground = new JRadioButton("移除前景色", false);
        removeForeground.setBounds(52, 530, 360, 40);
        similar = new JRadioButton("仅对近似色模糊", true);
        similar.setBounds(432, 510, 360, 40);
        twoDimensionCode = new JRadioButton("加入 n*79*79 二维码（仅黑白模式使用）", false);
        twoDimensionCode.setBounds(20, 20, 360, 40);

        contract = new JRadioButton("生成对比图", true);
        contract.setBounds(20, 350, 360, 40);

        colorLimit = new JRadioButton("限制图片颜色数量", false);
        colorLimit.setBounds(320, 20, 360, 40);

        lightsPoly2D = new JRadioButton("光的波粒二象性（仅非黑白模式使用）    每一帧移动速度（-100~100）", false);
        lightsPoly2D.setBounds(20, 80, 430, 40);

        JLabel fill = new JLabel("   补帧率（预期帧数/原本帧数）(可输入小数)");
        fill.setBounds(20, 180, 630, 40);
        fill.setFont(new Font("", Font.BOLD, 20));

        JLabel pixel = new JLabel("   像素化图片（n*n格合并为一格）（1-10）");
        pixel.setBounds(20, 280, 630, 40);
        pixel.setFont(new Font("", Font.BOLD, 20));


        JPanel panel4th = new JPanel();
        panel4th.setBounds(0, 0, 600, 600);
        panel4th.setLayout(null);
        panel4th.add(twoDimensionCode);
        panel4th.add(contract);
        panel4th.add(lightsPoly2D);

        JPanel panel3rd = new JPanel();
        panel3rd.setBounds(0, 0, 600, 600);
        panel3rd.setLayout(null);

        JPanel panel1st = new JPanel();
        panel1st.setBounds(0, 0, 600, 600);
        panel1st.setLayout(null);
        panel1st.add(auto1);
        panel1st.add(auto2);
        panel1st.add(colorLimit);
        panel1st.add(removeBilibili);
        panel1st.add(removeBlackWhite);
        panel1st.add(lab1);
        panel1st.add(lab2);
        panel1st.add(blackFirst);
        panel1st.add(removeForeground);
        panel1st.add(similar);


        JPanel panel2nd = new JPanel();
        panel2nd.setBounds(0, 0, 600, 600);
        panel2nd.setLayout(null);
        panel2nd.add(onlyBlackAndWhite);

        tabbedPane.addTab("边缘", null, panel1st, "");
        tabbedPane.addTab("基础", null, panel2nd, "");
        tabbedPane.addTab("色彩提取器", null, panel3rd, "");
        tabbedPane.addTab("其他", null, panel4th, "");
        tabbedPane.setBounds(1000, 200, 600, 600);


        JLabel rgb1 = new JLabel("  (R-G)范围                (G-B)范围");
        rgb1.setBounds(400, 10, 200, 50);
        rg1.setText("0");
        rg1.setBounds(400, 50, 70, 50);
        rg1.setFont(new Font("", Font.BOLD, 40));
        rg1.setForeground(Color.RED);
        rg2.setText("0");
        rg2.setBounds(400, 120, 70, 50);
        rg2.setFont(new Font("", Font.BOLD, 40));
        rg2.setForeground(Color.GREEN);
        gb1.setText("0");
        gb1.setBounds(500, 50, 70, 50);
        gb1.setFont(new Font("", Font.BOLD, 40));
        gb1.setForeground(Color.GREEN);
        gb2.setText("0");
        gb2.setBounds(500, 120, 70, 50);
        gb2.setFont(new Font("", Font.BOLD, 40));
        gb2.setForeground(Color.blue);

        r1.setText("0");
        r1.setBounds(50, 50, 70, 50);
        r1.setFont(new Font("", Font.BOLD, 40));
        r1.setForeground(Color.RED);
        r2.setText("0");
        r2.setBounds(50, 120, 70, 50);
        r2.setFont(new Font("", Font.BOLD, 40));
        r2.setForeground(Color.RED);
        g1.setText("0");
        g1.setBounds(150, 50, 70, 50);
        g1.setFont(new Font("", Font.BOLD, 40));
        g1.setForeground(Color.GREEN);
        g2.setText("0");
        g2.setBounds(150, 120, 70, 50);
        g2.setFont(new Font("", Font.BOLD, 40));
        g2.setForeground(Color.GREEN);
        b1.setText("0");
        b1.setBounds(250, 50, 70, 50);
        b1.setFont(new Font("", Font.BOLD, 40));
        b1.setForeground(Color.BLUE);
        b2.setText("0");
        b2.setBounds(250, 120, 70, 50);
        b2.setFont(new Font("", Font.BOLD, 40));
        b2.setForeground(Color.BLUE);
        colorChange = new JRadioButton("范围内颜色替换成调色板颜色（上面起始值，下面终值（颜色对应RGB）（0-255））", false);
        colorChange.setBounds(20, 180, 600, 40);
        op.setBounds(20, 250, 560, 310);
        panel3rd.add(op);
        panel3rd.add(r1);
        panel3rd.add(r2);
        panel3rd.add(g1);
        panel3rd.add(g2);
        panel3rd.add(b1);
        panel3rd.add(b2);
        panel3rd.add(gb1);
        panel3rd.add(gb2);
        panel3rd.add(rg1);
        panel3rd.add(rg2);
        panel3rd.add(rgb1);
        panel3rd.add(colorChange);

        Tb.setBounds(140, 360, 150, 40);
        Tb.setSelectedIndex(1);
        panel1st.add(Tb);

        ColorChoose.setBounds(340, 140, 150, 40);
        ColorChoose.setSelectedIndex(1);
        panel1st.add(ColorChoose);

        ColorChoose1.setBounds(340, 190, 150, 40);
        ColorChoose1.setSelectedIndex(0);
        panel1st.add(ColorChoose1);

        edgeTestMethod.setBounds(30, 160, 150, 40);
        edgeTestMethod.setSelectedIndex(0);
        panel1st.add(edgeTestMethod);

        panel1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(img1, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        panel1.setBounds(80, 580, 580, 320);
        panel1.setBackground(new Color(224, 224, 255));

        this.add(panel1);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        panel.setBounds(20, 20, 960, 540);
        panel.setBackground(new Color(224, 255, 224));
        this.add(panel);

        load.setBounds(1100, 70, 120, 60);
        load.setFont(new Font("", Font.BOLD, 20));
        load.addActionListener((e) -> Load());
        change.setBounds(750, 650, 200, 60);
        change.setFont(new Font("", Font.BOLD, 20));
        change.addActionListener((e) -> Change());
        this.add(change);
        Cal.setBounds(1250, 70, 130, 60);
        Cal.setFont(new Font("", Font.BOLD, 20));
        Cal.addActionListener((e) -> click());
        Cal1.setBounds(1400, 70, 150, 60);
        Cal1.setFont(new Font("", Font.BOLD, 20));
        Cal1.addActionListener((e) -> clickTry());

        slider = new JSlider();
        slider.setValue(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(100);
        slider.setMinorTickSpacing(5);
        Dictionary<Integer, Component> labels = new Hashtable<Integer, Component>();
        for (int i = 0; i <= 100; i += 5) {
            labels.put(i, new JLabel("" + i));
        }
        slider.setLabelTable(labels);
        slider.setBounds(0, 10, 1550, 40);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (slider.getValueIsAdjusting()) {
                    int changeValue = slider.getValue();
                    num1.setText(String.valueOf(changeValue));
                    value = slider.getValue();
                }
            }
        });


        value1.setText("");
        value1.setBounds(20, 10, 100, 40);
        value1.setFont(new Font("", Font.BOLD, 20));
        value1.setForeground(Color.BLACK);


        num14.setText("1");
        num14.setBounds(450, 270, 70, 50);
        num14.setFont(new Font("", Font.BOLD, 40));
        num14.setForeground(Color.RED);

        num13.setText("1");
        num13.setBounds(450, 170, 70, 50);
        num13.setFont(new Font("", Font.BOLD, 40));
        num13.setForeground(Color.RED);

        num12.setText("0");
        num12.setBounds(450, 460, 70, 50);
        num12.setFont(new Font("", Font.BOLD, 40));
        num12.setForeground(Color.RED);


        num11.setText("1");
        num11.setBounds(450, 70, 70, 50);
        num11.setFont(new Font("", Font.BOLD, 40));
        num10.setText("3");
        num10.setBounds(450, 360, 70, 50);
        num10.setFont(new Font("", Font.BOLD, 40));
        num10.setForeground(Color.RED);
        num9.setText("0");
        num9.setBounds(350, 360, 70, 50);
        num9.setFont(new Font("", Font.BOLD, 40));
        num9.setForeground(Color.RED);

        num7.setText("0");
        num7.setBounds(30, 260, 70, 50);
        num7.setFont(new Font("", Font.BOLD, 40));
        num7.setForeground(Color.RED);
        num6.setText("");
        num6.setBounds(230, 410, 70, 50);
        num6.setFont(new Font("", Font.BOLD, 40));
        num6.setForeground(Color.RED);
        num5.setText("");
        num5.setBounds(230, 310, 70, 50);
        num5.setFont(new Font("", Font.BOLD, 40));
        num5.setForeground(Color.YELLOW);
        num4.setText("");
        num4.setBounds(230, 210, 70, 50);
        num4.setFont(new Font("", Font.BOLD, 40));
        num4.setForeground(Color.BLACK);
        num3.setText("");
        num3.setBounds(230, 110, 70, 50);
        num3.setFont(new Font("", Font.BOLD, 40));
        num3.setForeground(Color.cyan);
        num2.setText("");
        num2.setBounds(230, 10, 70, 50);
        num2.setFont(new Font("", Font.BOLD, 40));
        num2.setForeground(Color.orange);
        num1.setText("0");
        num1.setBounds(30, 60, 70, 50);
        num1.setFont(new Font("", Font.BOLD, 40));
        num1.setForeground(Color.BLUE);

        numMin.setText("10");
        numMin.setBounds(330, 60, 70, 50);
        numMin.setFont(new Font("", Font.BOLD, 40));
        numMin.setForeground(Color.BLUE);
        numMax.setText("50");
        numMax.setBounds(430, 60, 70, 50);
        numMax.setFont(new Font("", Font.BOLD, 40));
        numMax.setForeground(Color.BLUE);

        JLabel label1 = new JLabel("(相似化)");
        label1.setToolTipText("不建议使用，因为需要极大内存");
        JLabel label2 = new JLabel("像素移动:");
        JLabel label2x = new JLabel("0 to 100");
        JLabel label3 = new JLabel("亮度:");
        JLabel label3x = new JLabel("0 to 100");
        JLabel label4 = new JLabel("色彩取整:");
        JLabel label4x = new JLabel("1 to 100");
        JLabel label5 = new JLabel("色调:");
        JLabel label5x = new JLabel("-100 to 100");
        JLabel label6 = new JLabel("随机加色:");
        JLabel label6x = new JLabel("0 to 100");
        JLabel label7 = new JLabel("(边缘)");
        label7.setToolTipText(" Use calculated edge to present the picture,it is useful when compared to (ColorDelta)");
        JLabel label8 = new JLabel("边缘模式:");
        JLabel label9 = new JLabel("修复次数");
        JLabel label10 = new JLabel("边缘大小");
        JLabel label12 = new JLabel("模糊");

        label2x.setBounds(330, 10, 150, 50);
        label2x.setFont(new Font("", Font.BOLD, 30));
        label3x.setBounds(330, 110, 150, 50);
        label3x.setFont(new Font("", Font.BOLD, 30));
        label4x.setBounds(330, 210, 150, 50);
        label4x.setFont(new Font("", Font.BOLD, 30));
        label5x.setBounds(310, 310, 190, 50);
        label5x.setFont(new Font("", Font.BOLD, 30));
        label6x.setBounds(330, 410, 150, 50);
        label6x.setFont(new Font("", Font.BOLD, 30));


        label1.setBounds(30, 10, 490, 50);
        label1.setFont(new Font("", Font.BOLD, 20));
        label2.setBounds(30, 10, 200, 50);
        label2.setFont(new Font("", Font.BOLD, 35));
        label3.setBounds(30, 110, 200, 50);
        label3.setFont(new Font("", Font.BOLD, 33));
        label4.setBounds(30, 210, 200, 50);
        label4.setFont(new Font("", Font.BOLD, 25));
        label5.setBounds(45, 310, 200, 50);
        label5.setFont(new Font("", Font.BOLD, 40));
        label6.setBounds(30, 410, 200, 50);
        label6.setFont(new Font("", Font.BOLD, 21));
        label7.setBounds(30, 210, 190, 50);
        label7.setFont(new Font("", Font.BOLD, 20));
        label8.setBounds(20, 350, 230, 50);
        label8.setFont(new Font("", Font.BOLD, 22));
        label9.setBounds(350, 320, 90, 50);
        label9.setFont(new Font("", Font.BOLD, 15));
        label10.setBounds(450, 320, 90, 50);
        label10.setFont(new Font("", Font.BOLD, 15));
        label12.setBounds(450, 420, 90, 50);
        label12.setFont(new Font("", Font.BOLD, 17));

        panel4th.add(num11);
        panel4th.add(num13);
        panel4th.add(num14);
        panel4th.add(fill);
        panel4th.add(pixel);

        panel1st.add(num12);
        panel1st.add(label12);

        this.add(tabbedPane);
        panel1st.add(label1);

        panel2nd.add(label2);
        panel2nd.add(label3);
        panel2nd.add(label4);
        panel2nd.add(label5);
        panel2nd.add(label6);

        panel2nd.add(label2x);
        panel2nd.add(label3x);
        panel2nd.add(label4x);
        panel2nd.add(label5x);
        panel2nd.add(label6x);

        panel1st.add(label7);
        panel1st.add(label8);
        panel1st.add(label9);
        panel1st.add(label10);
        panel1st.add(numMax);
        panel1st.add(numMin);
        panel1st.add(num1);
        panel2nd.add(num2);
        panel2nd.add(num3);
        panel2nd.add(num4);
        panel2nd.add(num5);
        panel2nd.add(num6);
        panel1st.add(num7);
        panel1st.add(num9);
        panel1st.add(num10);
        this.add(Cal);
        this.add(Cal1);
        this.add(load);
    }

    private void Change() {
        BufferedImage img3 = img1;
        img1 = img;
        img = img3;
        panel.repaint();
        panel1.repaint();
    }

    private void initImg(File file) {
        try {
            img2 = ImageIO.read(file);
            img = ImageIO.read(file);
            if (times <= fillFrame) {
                img1 = ImageIO.read(file);
                imgLast = img1;
            } else {
                imgLast = img1;
                img1 = ImageIO.read(file);
            }
        } catch (IOException e) {
            if (times <= 1)
                JOptionPane.showMessageDialog(null, "图片丢失");
            else
                JOptionPane.showMessageDialog(null, "成功生成");
            stop = true;
        }
    }

    /**
     * -----------------模糊算法-------------------------------------------------------------------------
     */
    private void calculate(int x, int y, double a[][][], int changeValue) {

        r = a[x][y][1];
        g = a[x][y][2];
        b = a[x][y][3];
        try {
            if (x - 1 > 0)
                calculate2(x - 1, y, a, changeValue);
            if (y - 1 > 0)
                calculate2(x, y - 1, a, changeValue);
            if (x + 1 < width)
                calculate2(x + 1, y, a, changeValue);
            if (y + 1 < height)
                calculate2(x, y + 1, a, changeValue);

        } catch (Exception e) {
        }
        a[x][y][0] = 2;
        a[x][y][0] = 3;
        a[x][y][1] = CalculateTotal[0];
        a[x][y][2] = CalculateTotal[1];
        a[x][y][3] = CalculateTotal[2];
    }

    private void calculate2(int x, int y, double a[][][], int changeValue) {

        if (a[x][y][0] <= 0) {
            double delta = Math.abs(a[x][y][1] - r) + Math.abs(a[x][y][2] - g) + Math.abs(a[x][y][3] - b);
            if (delta < changeValue) {
                a[x][y][0] = 1;
                try {
                    calculate(x, y, a, changeValue);
                } catch (Exception e) {
                }
            } else {
                a[x][y][0] = -1;
            }
        }

    }

    /**
     * ----------------------边缘算法--------------------------------------------------------------------
     */


    private void calculate11(int x, int y, double a[][][], int changeValue) {


        r = a[x][y][1];
        g = a[x][y][2];
        b = a[x][y][3];
        a[x][y][0] = 2;
        a[x][y][1] = CalculateTotal[0];
        a[x][y][2] = CalculateTotal[1];
        a[x][y][3] = CalculateTotal[2];
        try {
            if (x - 1 > 0)
                calculate12(x - 1, y, a, changeValue);
            if (y - 1 > 0)
                calculate12(x, y - 1, a, changeValue);
            if (x + 1 < width)
                calculate12(x + 1, y, a, changeValue);
            if (y + 1 < height)
                calculate12(x, y + 1, a, changeValue);

        } catch (Exception e) {
        }

    }

    private void calculate12(int x, int y, double a[][][], int changeValue) {

        if (a[x][y][0] == 0) {
            calculate11(x, y, a, changeValue);
        }

    }

    /**
     * ------------------------------------------------------------------------------------------
     */

    private double findEdgePercent(int valueOfChange, double xxx[][][]) {
        double a[][][] = xxx;
        double value11;
        if (valueOfChange > 0) {
            value11 = 0;
            for (int x = 1; x < img1.getWidth() - 1; x++) {
                for (int y = 1; y < img1.getHeight() - 1; y++) {

                    double delta = Math.abs(a[x][y][1] - a[x + 1][y][1]) + Math.abs(a[x][y][2] - a[x + 1][y][2]) + Math.abs(a[x][y][3] - a[x + 1][y][3]);
                    if (delta > valueOfChange) {
                        value11++;
                        continue;
                    }
                    delta = Math.abs(a[x][y][1] - a[x - 1][y][1]) + Math.abs(a[x][y][2] - a[x - 1][y][2]) + Math.abs(a[x][y][3] - a[x - 1][y][3]);
                    if (delta > valueOfChange) {
                        value11++;
                        continue;
                    }
                    delta = Math.abs(a[x][y][1] - a[x][y + 1][1]) + Math.abs(a[x][y][2] - a[x][y + 1][2]) + Math.abs(a[x][y][3] - a[x][y + 1][3]);
                    if (delta > valueOfChange) {
                        value11++;
                        continue;
                    }
                    delta = Math.abs(a[x][y][1] - a[x][y - 1][1]) + Math.abs(a[x][y][2] - a[x][y - 1][2]) + Math.abs(a[x][y][3] - a[x][y - 1][3]);
                    if (delta > valueOfChange) {
                        value11++;
                        continue;
                    }
                }
            }
            value11 = value11 * 100 / img1.getWidth() / img1.getHeight();
            return value11;
        }
        return 0;
    }

    /**
     * ------------------------------------------------------------------------------------------
     */
    private int findPerfectValue(double a[][][]) {
        int change = 0;


        if (edgeTestMethod.getSelectedIndex() % 2 == 1) {
            int in = 5;
            while (in < 400) {
                if (findEdgePercent(in, a) < 80) {
                    change = in;
                    break;
                }
                in = (int) (in * 1.2);
            }
        }


        if (edgeTestMethod.getSelectedIndex() % 2 == 0) {
            while (true) {
                if (findEdgePercent(1, a) < 60) {
                    change = 1;
                    break;
                }
                double i2 = findEdgePercent(2, a);
                double i3 = findEdgePercent(3, a);
                if (i2 < 66) {
                    change = 2;
                    break;
                }
                if (i2 - i3 > 30) {
                    change = 2;
                    break;
                }
                if (i2 - i3 > 25 && i2 > 80) {
                    change = 2;
                    break;
                }
                if (findEdgePercent(3, a) < 66) {
                    change = 3;
                    break;
                }
                if (findEdgePercent(4, a) < 66) {
                    change = 4;

                    break;
                }
                if (findEdgePercent(5, a) < 66) {
                    change = 5;
                    break;
                }
                if (findEdgePercent(6, a) < 70) {
                    change = 6;
                    break;
                }
                int in = 7;
                while (in < 100) {
                    if (findEdgePercent(in, a) < 80) {
                        change = in;
                        break;
                    }
                    in = (int) (in * 1.2);
                }
                break;
            }
        }
        value = findEdgePercent(change, a);
        return change;
    }


    public static void heapSort2(int[][] a) {
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            adjustHeap2(a, i, a.length);
        }
        for (int j = a.length - 1; j > 0; j--) {
            swap2(a, 0, j);
            adjustHeap2(a, 0, j);
        }

    }

    public static void adjustHeap2(int[][] a, int i, int length) {
        int temp[] = a[i].clone();
        for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
            if (k + 1 < length && a[k][1] < a[k + 1][1]) {
                k++;
            }
            if (a[k][1] > temp[1]) {
                a[i][0] = a[k][0];
                a[i][1] = a[k][1];
                a[i][2] = a[k][2];
                a[i][3] = a[k][3];
                a[i][4] = a[k][4];
                i = k;
            } else {
                break;
            }
        }
        a[i] = temp.clone();
    }

    public static void swap2(int[][] a, int a1, int a2) {
        int temp[] = new int[5];
        temp[1] = a[a1][1];
        a[a1][1] = a[a2][1];
        a[a2][1] = temp[1];

        temp[0] = a[a1][0];
        a[a1][0] = a[a2][0];
        a[a2][0] = temp[0];

        temp[2] = a[a1][2];
        a[a1][2] = a[a2][2];
        a[a2][2] = temp[2];

        temp[3] = a[a1][3];
        a[a1][3] = a[a2][3];
        a[a2][3] = temp[3];

        temp[4] = a[a1][4];
        a[a1][4] = a[a2][4];
        a[a2][4] = temp[4];
    }

    /**
     * 处理图片
     */
    private void processImg(int changeValue, int moveValue, int light, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11) {


        double a[][][] = new double[img1.getWidth()][img1.getHeight()][4];//0---value;1----red;2---green;3--blue
        double a1[][][] = new double[img1.getWidth()][img1.getHeight()][4];//0---value;1----red;2---green;3--blue

        width = img1.getWidth();
        height = img1.getHeight();

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                // find rgb
                int rgb = img1.getRGB(x, y);
                int rgbLast = imgLast.getRGB(x, y);
                Color color = new Color(rgb);
                Color colorLast = new Color(rgbLast);

                if (fillFrame == 1) {
                    a[x][y][1] = color.getRed();
                    a[x][y][2] = color.getGreen();
                    a[x][y][3] = color.getBlue();
                } else {
                    double percent = times / fillFrame - (int) (times / fillFrame);

                    a[x][y][1] = color.getRed() * percent + colorLast.getRed() * (1 - percent);
                    a[x][y][2] = color.getGreen() * percent + colorLast.getGreen() * (1 - percent);
                    a[x][y][3] = color.getBlue() * percent + colorLast.getBlue() * (1 - percent);

                }


                if (removeBlackWhite.isSelected()) {
                    if (color.getBlue() + color.getGreen() + color.getRed() < 600 && color.getBlue() + color.getGreen() + color.getRed() > 450 && Math.abs(color.getGreen() - color.getRed()) + color.getBlue() + color.getGreen() < 30) {// 765 white
                        a[x][y][0] = 4;
                    }
                }
                if (removeBilibili.isSelected())
                    if (x * 100 / width >= 87 && x * 100 / width <= 96 && y * 100 / height <= 11 && y * 100 / height >= 5 && color.getBlue() + color.getGreen() + color.getRed() > 200) {// 765 white
                        a[x][y][0] = 4;
                    } // remove " bilibili "
            }
        }
        /**
         * -----------------find proper changeValue------------------------------------------------------------------------
         */
        if (auto1.isSelected()) {
            changeValue = findPerfectValue(a);
            lab1.setText("值:" + changeValue + "  百分比:" + value);
        }
        if (auto2.isSelected()) {
            n7 = findPerfectValue(a);
            lab2.setText("值:" + n7 + "  百分比:" + value);
        }
        /**
         * -----------------模糊算法-------------------------------------------------------------------------
         */
        if (changeValue > 0) {
            value = 0;
            /**
             * 边缘算法 ~~
             */
            for (int x = 1; x < img1.getWidth() - 1; x++) {
                for (int y = 1; y < img1.getHeight() - 1; y++) {
                    if (edgeTestMethod.getSelectedIndex() == 0) {
                        double delta = Math.abs(a[x][y][1] - a[x + 1][y][1]) + Math.abs(a[x][y][2] - a[x + 1][y][2]) + Math.abs(a[x][y][3] - a[x + 1][y][3]);
                        if (delta > changeValue)
                            a[x][y][0] = 4;
                        delta = Math.abs(a[x][y][1] - a[x - 1][y][1]) + Math.abs(a[x][y][2] - a[x - 1][y][2]) + Math.abs(a[x][y][3] - a[x - 1][y][3]);
                        if (delta > changeValue)
                            a[x][y][0] = 4;
                        delta = Math.abs(a[x][y][1] - a[x][y + 1][1]) + Math.abs(a[x][y][2] - a[x][y + 1][2]) + Math.abs(a[x][y][3] - a[x][y + 1][3]);
                        if (delta > changeValue)
                            a[x][y][0] = 4;
                        delta = Math.abs(a[x][y][1] - a[x][y - 1][1]) + Math.abs(a[x][y][2] - a[x][y - 1][2]) + Math.abs(a[x][y][3] - a[x][y - 1][3]);
                        if (delta > changeValue)
                            a[x][y][0] = 4;
                    }

                    if (edgeTestMethod.getSelectedIndex() == 1) {
                        double dx = 0, dy = 0;
                        for (int i = 1; i < 4; i++) {
                            dx += Math.abs(a[x - 1][y][i] + a[x - 1][y - 1][i] + a[x - 1][y + 1][i] - a[x + 1][y][i] - a[x + 1][y - 1][i] - a[x + 1][y + 1][i]);
                            dy += Math.abs(a[x][y - 1][i] + a[x - 1][y - 1][i] + a[x + 1][y - 1][i] - a[x][y + 1][i] - a[x - 1][y + 1][i] - a[x + 1][y + 1][i]);
                        }
                        double delta = Math.sqrt(dx * dx + dy * dy) / 10;
                        if (delta > changeValue)
                            a[x][y][0] = 4;
                    }

                    if (a[x][y][0] == 4) {
                        value++;
                    }
                }
            }
            value = value * 100 / img1.getWidth() / img1.getHeight();
            if (!auto1.isSelected()) lab1.setText("  Percent:" + value);
            /**
             *
             */
            for (int x = 1; x < img1.getWidth() - 1; x++) {
                for (int y = 1; y < img1.getHeight() - 1; y++) {
                    if (a[x][y][0] == 4) {
                        continue;
                    }
                    if (a[x][y][0] == -1) a[x][y][0] = 0;
                    if (a[x][y][0] != 0) continue;
                    if (a[x][y][0] == 0) {
                        CalculateTotal = new double[]{a[x][y][1], a[x][y][2], a[x][y][3]};
                        calculate11(x, y, a, changeValue);
                    }

                }
            }
        }
        /**
         * --------------------------------n色绘制-----------------------------------------------------------------
         */
        if (colorLimit.isSelected()) {
            int numOfColor = 0;
            int color10[][] = new int[27 * 27 * 27][5];//0 index,1=number,2-4 rgb
            for (int i = 0; i < 27 * 27 * 27; i++) {
                color10[i][0] = i;
            }

            for (int x1 = 0; x1 < img1.getWidth(); x1++) {
                for (int y1 = 0; y1 < img1.getHeight(); y1++) {

                    int a00 = (int) (a[x1][y1][1] / 10) + 27 * (int) (a[x1][y1][2] / 10) + 27 * 27 * (int) (a[x1][y1][3] / 10);
                    color10[a00][1]++;
                    color10[a00][2] += a[x1][y1][1];
                    color10[a00][3] += a[x1][y1][2];
                    color10[a00][4] += a[x1][y1][3];
                }
            }
            int dot = width * height;
            int recent = 0;
            heapSort2(color10);

            for (int i = 27 * 27 * 27 - 1; i > 0; i--) {
                recent += color10[i][1];
                numOfColor++;
                if (color10[i][1] > 0) {
                    color10[i][2] = color10[i][2] / color10[i][1];
                    color10[i][3] = color10[i][3] / color10[i][1];
                    color10[i][4] = color10[i][4] / color10[i][1];
                }
                int aax = 2;
                if (ColorChoose.getSelectedIndex() == 1) aax = 3;
                if (ColorChoose.getSelectedIndex() == 2) aax = 5;
                if (ColorChoose.getSelectedIndex() == 3) aax = 7;
                if (ColorChoose.getSelectedIndex() == 4) aax = 10;
                if ((numOfColor > colorMin && recent > dot / aax) || numOfColor > colorMax) break;
            }


            for (int x1 = 0; x1 < img1.getWidth(); x1++) {
                for (int y1 = 0; y1 < img1.getHeight(); y1++) {
                    double max = 99999;
                    int colors[] = new int[4];
                    for (int i = 27 * 27 * 27 - 1; i > 27 * 27 * 27 - 1 - numOfColor; i--) {
                        double delta = Math.abs(a[x1][y1][1] - color10[i][2]) + Math.abs(a[x1][y1][2] - color10[i][3]) + Math.abs(a[x1][y1][3] - color10[i][4]);
                        if (delta < max) {
                            colors[1] = color10[i][2];
                            colors[2] = color10[i][3];
                            colors[3] = color10[i][4];
                            max = delta;
                        }
                    }

                    int aac = 100;
                    if (ColorChoose1.getSelectedIndex() == 1) aac = 50;
                    if (ColorChoose1.getSelectedIndex() == 2) aac = 30;
                    if (ColorChoose1.getSelectedIndex() == 3) aac = 20;
                    if (ColorChoose1.getSelectedIndex() == 4) aac = 10;

                    if (max > aac) a[x1][y1][0] = 4;
                    a[x1][y1][1] = colors[1];
                    a[x1][y1][2] = colors[2];
                    a[x1][y1][3] = colors[3];

                }
            }
        }
        /**
         * ---------------边缘----------------------------------------------------------------------------------
         */
        value = 0;
        if (n7 > 0) {
            for (int x = 1; x < img1.getWidth() - 1; x++) {
                for (int y = 1; y < img1.getHeight() - 1; y++) {
                    double n77 = n7;
                    if (blackFirst.isSelected()) {
                        n77 = n7 * ((a[x][y][1] + a[x][y][2] + a[x][y][3]) / 255 / 3 * 1 * 2 + 0);
                    }

                    if (edgeTestMethod.getSelectedIndex() == 2) {
                        a[x][y][0] = 4;
                        double delta = Math.abs(a[x][y][1] - a[x + 1][y][1]) + Math.abs(a[x][y][2] - a[x + 1][y][2]) + Math.abs(a[x][y][3] - a[x + 1][y][3]);
                        if (delta > n77) {
                            delta = Math.abs(a[x][y][1] - a[x - 1][y][1]) + Math.abs(a[x][y][2] - a[x - 1][y][2]) + Math.abs(a[x][y][3] - a[x - 1][y][3]);
                            if (delta > n77) {
                                delta = Math.abs(a[x][y][1] - a[x][y + 1][1]) + Math.abs(a[x][y][2] - a[x][y + 1][2]) + Math.abs(a[x][y][3] - a[x][y + 1][3]);
                                if (delta > n77) {
                                    delta = Math.abs(a[x][y][1] - a[x][y - 1][1]) + Math.abs(a[x][y][2] - a[x][y - 1][2]) + Math.abs(a[x][y][3] - a[x][y - 1][3]);
                                    if (delta > n77)
                                        a[x][y][0] = 0;
                                }
                            }
                        }
                    }
                    if (edgeTestMethod.getSelectedIndex() == 0) {
                        double delta = Math.abs(a[x][y][1] - a[x + 1][y][1]) + Math.abs(a[x][y][2] - a[x + 1][y][2]) + Math.abs(a[x][y][3] - a[x + 1][y][3]);
                        if (delta > n77)
                            a[x][y][0] = 4;
                        delta = Math.abs(a[x][y][1] - a[x][y + 1][1]) + Math.abs(a[x][y][2] - a[x][y + 1][2]) + Math.abs(a[x][y][3] - a[x][y + 1][3]);
                        if (delta > n77)
                            a[x][y][0] = 4;
                    }
                    if (edgeTestMethod.getSelectedIndex() == 1) {
                        double dx = 0, dy = 0;
                        for (int i = 1; i < 4; i++) {
                            dx += Math.abs(a[x - 1][y][i] + a[x - 1][y - 1][i] + a[x - 1][y + 1][i] - a[x + 1][y][i] - a[x + 1][y - 1][i] - a[x + 1][y + 1][i]);
                            dy += Math.abs(a[x][y - 1][i] + a[x - 1][y - 1][i] + a[x + 1][y - 1][i] - a[x][y + 1][i] - a[x - 1][y + 1][i] - a[x + 1][y + 1][i]);
                        }
                        double delta = Math.sqrt(dx * dx + dy * dy) / 10;
                        if (delta > n77)
                            a[x][y][0] = 4;
                    }
                    if (edgeTestMethod.getSelectedIndex() == 3) {
                        double dx = 0, dy = 0;
                        for (int i = 1; i < 4; i++) {
                            dx += Math.abs(a[x - 1][y][i] + a[x - 1][y - 1][i] + a[x - 1][y + 1][i] - a[x + 1][y][i] - a[x + 1][y - 1][i] - a[x + 1][y + 1][i]);
                            dy += Math.abs(a[x][y - 1][i] + a[x - 1][y - 1][i] + a[x + 1][y - 1][i] - a[x][y + 1][i] - a[x - 1][y + 1][i] - a[x + 1][y + 1][i]);
                        }
                        double delta = Math.sqrt(dx * dx + dy * dy) / 10;
                        if (delta <= n77)
                            a[x][y][0] = 4;
                    }
                }
            }

            for (int x = 1; x < img1.getWidth() - 1; x++) {
                for (int y = 1; y < img1.getHeight() - 1; y++) {

                    if (removeBlackWhite.isSelected() && Math.abs(a[x][y][1] - a[x][y][2]) + Math.abs(a[x][y][2] - a[x][y][3]) < 20) {
                        int ax = 0;
                        for (int i = -1; i <= 1; i++)
                            for (int j = -1; j <= 1; j++)
                                if (a[x + i][y + j][0] == 4) ax++;
                        if (ax >= 7)
                            a[x][y][0] = 0.1;
                    }

                    if (a[x][y][0] == 4 || a[x][y][0] == 0.1) {
                        a[x][y][0] = 4;
                        a[x][y][1] = op.getColor().getRed();
                        a[x][y][2] = op.getColor().getGreen();
                        a[x][y][3] = op.getColor().getBlue();
                        value++;
                    } else {
                        if (removeForeground.isSelected()) {
                            a[x][y][1] = 255;
                            a[x][y][2] = 255;
                            a[x][y][3] = 255;
                        }
                    }
                }
            }
            value = value * 100 / img1.getWidth() / img1.getHeight();
            if (!auto2.isSelected()) lab2.setText("  百分比:" + value);
        }


        /**
         * -------------------------------------------------------------------------------------------------
         */
        int n;
        for (int x = 1; x < img1.getWidth() - 1; x++) {
            for (int y = 1; y < img1.getHeight() - 1; y++) {
                n = Math.min(Math.min(Math.min(x, y), Math.min(img1.getWidth() - x, img1.getHeight() - y)), 40);//n max==20
                if (a[x][y][0] == 4) {
                    /**
                     *edge method   0==left Up  1==Search nearest    2==No (former)
                     */
                    if (n8 == 0) {
                        if ((x + y) % 2 == 0) {
                            a[x][y][1] = a[x - 1][y][1];
                            a[x][y][2] = a[x - 1][y][2];
                            a[x][y][3] = a[x - 1][y][3];
                        } else {
                            a[x][y][1] = a[x][y - 1][1];
                            a[x][y][2] = a[x][y - 1][2];
                            a[x][y][3] = a[x][y - 1][3];
                        }
                    }
                    if (n8 == 1) {
                        if (edgeTestMethod.getSelectedIndex() < 2) {
                            if ((x + y) % 2 == 0) {
                                a[x][y][1] = a[x - 1][y][1];
                                a[x][y][2] = a[x - 1][y][2];
                                a[x][y][3] = a[x - 1][y][3];
                            } else {
                                a[x][y][1] = a[x][y - 1][1];
                                a[x][y][2] = a[x][y - 1][2];
                                a[x][y][3] = a[x][y - 1][3];
                            }
                        } else {
                            a[x][y][1] = 255;
                            a[x][y][2] = 255;
                            a[x][y][3] = 255;
                        }
                        a[x][y][1] = 255;
                        a[x][y][2] = 255;
                        a[x][y][3] = 255;
                        boolean con = false;
                        int nn = 3;
                        for (int m1 = 1; m1 < n; m1++) {
                            for (int m2 = 0; m2 <= m1; m2++) {
                                double ans = 0;
                                ans = a[x + m1 - m2][y + m2][1];
                                if (a[x + m1 - m2][y + m2][0] < 3 && ans != 0 && ans < 230 && (Math.abs(ans - a[x + m1 - m2][y + m2][2]) > nn)) {
                                    a[x][y][1] = a[x + m1 - m2][y + m2][1];
                                    a[x][y][2] = a[x + m1 - m2][y + m2][2];
                                    a[x][y][3] = a[x + m1 - m2][y + m2][3];
                                    con = true;
                                    break;
                                }
                                ans = a[x - m1 + m2][y + m2][1];
                                if (a[x - m1 + m2][y + m2][0] < 3 && ans != 0 && ans < 230 && Math.abs(ans - a[x - m1 + m2][y + m2][2]) > nn) {
                                    a[x][y][1] = a[x - m1 + m2][y + m2][1];
                                    a[x][y][2] = a[x - m1 + m2][y + m2][2];
                                    a[x][y][3] = a[x - m1 + m2][y + m2][3];
                                    con = true;
                                    break;
                                }
                                ans = a[x + m1 - m2][y - m2][1];
                                if (a[x + m1 - m2][y - m2][0] < 3 && ans != 0 && ans < 230 && Math.abs(ans - a[x + m1 - m2][y - m2][2]) > nn) {
                                    a[x][y][1] = a[x + m1 - m2][y - m2][1];
                                    a[x][y][2] = a[x + m1 - m2][y - m2][2];
                                    a[x][y][3] = a[x + m1 - m2][y - m2][3];
                                    con = true;
                                    break;
                                }
                                ans = a[x - m1 + m2][y - m2][1];
                                if (a[x - m1 + m2][y - m2][0] < 3 && ans != 0 && ans < 230 && Math.abs(ans - a[x - m1 + m2][y - m2][2]) > nn) {
                                    a[x][y][1] = a[x - m1 + m2][y - m2][1];
                                    a[x][y][2] = a[x - m1 + m2][y - m2][2];
                                    a[x][y][3] = a[x - m1 + m2][y - m2][3];
                                    con = true;
                                    break;
                                }
                            }
                            if (con) break;
                        }
                    }
                    a[x][y][0] = 3;
                }
            }
        }


        /**
         * -----make edge smoothly--------------------------------------------------------------------------------------------
         */
        for (int nn9 = 0; nn9 < n9; nn9++) {
            double c[][][] = new double[img1.getWidth()][img1.getHeight()][4];

            if (n8 == 2) {    // line edge
                for (int x = 1; x < img1.getWidth() - 1; x++) {
                    for (int y = 1; y < img1.getHeight() - 1; y++) {
                        int q = 0;
                        q += (a[x][y][1] == a[x - 1][y][1]) ? 1 : 0;
                        q += (a[x][y][1] == a[x + 1][y][1]) ? 1 : 0;
                        q += (a[x][y][1] == a[x][y + 1][1]) ? 1 : 0;
                        q += (a[x][y][1] == a[x][y - 1][1]) ? 1 : 0;
                        q += (a[x][y][1] == a[x - 1][y - 1][1]) ? 1 : 0;
                        q += (a[x][y][1] == a[x + 1][y - 1][1]) ? 1 : 0;
                        q += (a[x][y][1] == a[x - 1][y + 1][1]) ? 1 : 0;
                        q += (a[x][y][1] == a[x + 1][y + 1][1]) ? 1 : 0;
                        c[x][y][1] = a[x][y][1];
                        c[x][y][2] = a[x][y][2];
                        c[x][y][3] = a[x][y][3];

                        if (q <= 3) {
                            c[x][y][1] = 255 - a[x][y][1];
                            c[x][y][2] = 255 - a[x][y][2];
                            c[x][y][3] = 255 - a[x][y][3];
                        }
                    }
                }
                for (int x = 1; x < img1.getWidth() - 1; x++) {
                    for (int y = 1; y < img1.getHeight() - 1; y++) {
                        a[x][y][1] = c[x][y][1];
                        a[x][y][2] = c[x][y][2];
                        a[x][y][3] = c[x][y][3];
                    }
                }
            } else {
                for (int x = n10; x < img1.getWidth() - n10; x++) {
                    for (int y = n10; y < img1.getHeight() - n10; y++) {
                        int q1 = 0, q2 = 0, q3 = 0, q4 = 0, q5 = 0;
                        for (int x1 = -n10; x1 <= n10; x1++) {
                            for (int y1 = -n10; y1 <= n10; y1++) {
                                q1 += (a[x + x1][y + y1][1] == a[x - 1][y][1]) ? 1 : 0;
                                q2 += (a[x + x1][y + y1][1] == a[x + 1][y][1]) ? 1 : 0;
                                q3 += (a[x + x1][y + y1][1] == a[x][y + 1][1]) ? 1 : 0;
                                q4 += (a[x + x1][y + y1][1] == a[x][y - 1][1]) ? 1 : 0;
                                q5 += (a[x + x1][y + y1][1] == a[x][y][1]) ? 1 : 0;
                            }
                        }
                        int q = Math.max(Math.max(Math.max(q1, q2), Math.max(q4, q3)), q5);
                        if (q5 == q || q < 2) {
                            System.arraycopy(a[x][y], 1, c[x][y], 1, 3);
                            continue;
                        }
                        if (q1 == q) {
                            System.arraycopy(a[x - 1][y], 1, c[x][y], 1, 3);
                            continue;
                        }
                        if (q2 == q) {
                            System.arraycopy(a[x + 1][y], 1, c[x][y], 1, 3);
                            continue;
                        }
                        if (q3 == q) {
                            System.arraycopy(a[x][y + 1], 1, c[x][y], 1, 3);
                            continue;
                        }
                        if (q4 == q) {
                            System.arraycopy(a[x][y - 1], 1, c[x][y], 1, 3);
                            continue;
                        }

                    }
                }
                for (int x = n10; x < img1.getWidth() - n10; x++) {
                    for (int y = n10; y < img1.getHeight() - n10; y++) {
                        System.arraycopy(c[x][y], 1, a[x][y], 1, 3);
                    }
                }
            }
        }
        /**
         * -----渐变 smoothly--------------------------------------------------------------------------------------------
         */
        if (smooth > 0)
            if (smooth > 10) smooth = 10;
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                int t = 0;
                int total[] = {0, 0, 0, 0};
                for (int x1 = x - smooth; x1 <= x + smooth; x1++) {
                    if (x1 < 0 || x1 >= img1.getWidth()) continue;
                    for (int y1 = y - smooth; y1 <= y + smooth; y1++) {
                        if (y1 < 0 || y1 >= img1.getHeight()) continue;

                        int value = (similar.isSelected()) ? Math.max(changeValue, n7) + 10 : 999;

                        if (Math.abs(a[x1][y1][1] - a[x][y][1]) + Math.abs(a[x1][y1][1] - a[x][y][1]) + Math.abs(a[x1][y1][1] - a[x][y][1]) < value) {
                            t++;
                            total[1] += a[x1][y1][1];
                            total[2] += a[x1][y1][2];
                            total[3] += a[x1][y1][3];
                        }
                    }
                }
                if (t > 0) {
                    a[x][y][1] = total[1] / t;
                    a[x][y][2] = total[2] / t;
                    a[x][y][3] = total[3] / t;
                }
            }
        }

/**
 * -----------------------------pixel像素化处理--------------------------------------------------------------------
 */

        if (pixelValue > 1)
            for (int x1 = 0; x1 < img1.getWidth() / pixelValue; x1++) {
                for (int y1 = 0; y1 < img1.getHeight() / pixelValue; y1++) {
                    int all[] = new int[27 * 27 * 27];
                    for (int x = 0; x < pixelValue; x++) {
                        for (int y = 0; y < pixelValue; y++) {
                            all[(int) (a[x1 * pixelValue + x][y1 * pixelValue + y][1] / 10) + 27 * (int) (a[x1 * pixelValue + x][y1 * pixelValue + y][2] / 10) + 27 * 27 * (int) (a[x1 * pixelValue + x][y1 * pixelValue + y][3] / 10)]++;
                        }
                    }


                    int rTotal = 0, gTotal = 0, bTotal = 0;
                    int max = 0;
                    for (int i = 0; i < 27 * 27 * 27; i++) {
                        if (all[i] > max) {
                            max = all[i];
                            rTotal = i % 27 * 10;
                            gTotal = i / 27 % 27 * 10;
                            bTotal = i / 27 / 27 % 27 * 10;
                        }
                    }

                    /*
                    for (int x = 0; x < pixelValue; x++) {
                        for (int y = 0; y < pixelValue; y++) {
                            rTotal += a[x1 * pixelValue + x][y1 * pixelValue + y][1];
                            gTotal += a[x1 * pixelValue + x][y1 * pixelValue + y][2];
                            bTotal += a[x1 * pixelValue + x][y1 * pixelValue + y][3];
                        }
                    }
                    rTotal = rTotal / pixelValue / pixelValue;
                    gTotal = gTotal / pixelValue / pixelValue;
                    bTotal = bTotal / pixelValue / pixelValue;
                    */

                    for (int x = 0; x < pixelValue; x++) {
                        for (int y = 0; y < pixelValue; y++) {
                            a[x1 * pixelValue + x][y1 * pixelValue + y][1] = rTotal;
                            a[x1 * pixelValue + x][y1 * pixelValue + y][2] = gTotal;
                            a[x1 * pixelValue + x][y1 * pixelValue + y][3] = bTotal;
                        }
                    }
                }
            }
        /**
         * -----------------------------颜色改变--------------------------------------------------------------------
         *
         * */
        if (colorChange.isSelected()) {
            int rr1 = Integer.parseInt(r1.getText()), rr2 = Integer.parseInt(r2.getText());
            int gg1 = Integer.parseInt(g1.getText()), gg2 = Integer.parseInt(g2.getText());
            int bb1 = Integer.parseInt(b1.getText()), bb2 = Integer.parseInt(b2.getText());
            int rg11 = Integer.parseInt(rg1.getText()), rg12 = Integer.parseInt(rg2.getText());
            int gb11 = Integer.parseInt(gb1.getText()), gb12 = Integer.parseInt(gb2.getText());
            int R = op.getColor().getRed() - (rr1 + rr2) / 2;
            int G = op.getColor().getGreen() - (gg1 + gg2) / 2;
            int B = op.getColor().getBlue() - (bb1 + bb2) / 2;
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (a[x][y][1] <= rr2 && a[x][y][1] >= rr1 && a[x][y][2] <= gg2 && a[x][y][2] >= gg1 && a[x][y][3] <= bb2 && a[x][y][3] >= bb1) {
                        if ((a[x][y][1] - a[x][y][2]) <= rg12 && (a[x][y][1] - a[x][y][2]) >= rg11 && (a[x][y][2] - a[x][y][3]) <= gb12 && (a[x][y][2] - a[x][y][3]) >= gb11) {
                            a[x][y][1] += R;
                            a[x][y][2] += G;
                            a[x][y][3] += B;
                        }
                    }
                }
            }
        }
/**
 * -----------------------------基本处理--------------------------------------------------------------------
 */
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {


                //像素移动+光波粒二象性
                int x1, y1;
                //  if (lightsPoly2D.isSelected()) {
                //      double random = Math.random();
                //      int moveValue2 = (int) ((((int) (Math.random() * 2)) * 2 - 1) * ((random * random) * (width / numOfPoly2D / 2)));

                //     x1 = (int) (x + Math.random() * (moveValue) * 2 - (moveValue));
                //      x1 += moveValue2;
                //  } else {
                x1 = (int) (x + Math.random() * (moveValue) * 2 - (moveValue));
                //  }
                y1 = (int) (y + Math.random() * moveValue * 2 - moveValue);

                if (x1 > img1.getWidth() - 1) {
                    x1 = img1.getWidth() - 1;
                } else if (x1 < 0) {
                    x1 = 0;
                }
                if (y1 > img1.getHeight() - 1) {
                    y1 = img1.getHeight() - 1;
                } else if (y1 < 0) {
                    y1 = 0;
                }
                int r = ((int) (a[x1][y1][1] + Math.random() * n6 * 2 - n6 + light) / n4) * n4;
                int g = ((int) (a[x1][y1][2] + Math.random() * n6 * 2 - n6 + light) / n4) * n4;
                int b = ((int) (a[x1][y1][3] + Math.random() * n6 * 2 - n6 + light) / n4) * n4;
                int average = (r + g + b) / 3;
                r = r + (r - average) * (n5) / 100;
                g = g + (g - average) * (n5) / 100;
                b = b + (b - average) * (n5) / 100;

                //光的波粒二象性
                int poly = 10;
                if (lightsPoly2D.isSelected()) {
                    double xx = x;
                    r += poly * Math.sin((xx + n11 * times) * numOfPoly2D * 3.1415 / width);
                    g += poly * Math.sin((xx + n11 * times) * numOfPoly2D * 3.1415 / width);
                    b += poly * Math.sin((xx + n11 * times) * numOfPoly2D * 3.1415 / width);
                }


                if (twoDimensionCode.isSelected()) {
                    //二维码
                    x1 = x1 % 80 / 2;
                    y1 = y1 % 80 / 2;
                    if (x1 <= 8 && y1 <= 8) {
                        r = 255;
                        g = 255;
                        b = 255;
                        if (y1 < 8 && (x1 == 1 || x1 == 7)) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (x1 < 8 && (y1 == 1 || y1 == 7)) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (x1 <= 5 && x1 >= 3 && y1 <= 5 && y1 >= 3) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                    }
                    if (x1 >= 32 && y1 <= 8) {
                        x1 = x1 - 32;
                        r = 255;
                        g = 255;
                        b = 255;
                        if (y1 < 8 && (x1 == 1 || x1 == 7)) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (x1 < 8 && (y1 == 1 || y1 == 7)) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (x1 <= 5 && x1 >= 3 && y1 <= 5 && y1 >= 3) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                    }
                    if (y1 >= 32 && x1 <= 8) {
                        y1 = y1 - 32;
                        r = 255;
                        g = 255;
                        b = 255;
                        if (y1 < 8 && (x1 == 1 || x1 == 7)) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (x1 < 8 && (y1 == 1 || y1 == 7)) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (x1 <= 5 && x1 >= 3 && y1 <= 5 && y1 >= 3) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                    }
                    if (y1 >= 31 && y1 <= 35 && x1 >= 31 && x1 <= 35) {
                        r = 255;
                        g = 255;
                        b = 255;
                        if (x1 == 31 || x1 == 35) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (y1 == 31 || y1 == 35) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                        if (x1 == 33 && y1 == 33) {
                            r = 0;
                            g = 0;
                            b = 0;
                        }
                    }
                    if (x1 == 0 || y1 == 0) {
                        r = 255;
                        g = 255;
                        b = 255;
                    }
                }
                //-----------------------------------------
                if (r > 255) {
                    r = 255;
                } else if (r < 0) {
                    r = 0;
                }
                if (g > 255) {
                    g = 255;
                } else if (g < 0) {
                    g = 0;
                }
                if (b > 255) {
                    b = 255;
                } else if (b < 0) {
                    b = 0;
                }


                if (onlyBlackAndWhite.isSelected()) {//黑白模式
                    int total = r + g + b;


                    if (total > 382) {
                        r = 255;
                        g = 255;
                        b = 255;
                    } else {
                        r = 0;
                        g = 0;
                        b = 0;
                    }
                }

                Color color = new Color(r, g, b);
                img.setRGB(x, y, color.getRGB());

            }
        }

        if (contract.isSelected()) {
            for (int x = 0; x < img1.getWidth() / 2; x++) {
                for (int y = 0; y < img1.getHeight() / 2; y++) {
                    img2.setRGB(x, y, img1.getRGB(2 * x, 2 * y));
                }
            }
            for (int x = img1.getWidth() / 2; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight() / 2; y++) {
                    img2.setRGB(x, y, new Color(0, 0, 0).getRGB());
                }
            }
            for (int x = 0; x < img1.getWidth() / 2; x++) {
                for (int y = img1.getHeight() / 2; y < img1.getHeight(); y++) {
                    img2.setRGB(x, y, new Color(0, 0, 0).getRGB());
                }
            }
            for (int x = (img1.getWidth() + 1) / 2; x < img1.getWidth(); x++) {
                for (int y = (img1.getHeight() + 1) / 2; y < img1.getHeight(); y++) {
                    img2.setRGB(x, y, img.getRGB((x - (width + 1) / 2) * 2, (y - (height + 1) / 2) * 2));
                }
            }
            try {
                if (times < 10)
                    ImageIO.write(img2, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/x000" + times + ".jpg"));
                else if (times < 100)
                    ImageIO.write(img2, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/x00" + times + ".jpg"));
                else if (times < 1000)
                    ImageIO.write(img2, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/x0" + times + ".jpg"));
                else
                    ImageIO.write(img2, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/x" + times + ".jpg"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //------------------------------------------------------------------
        try {
            if (times < 10)
                ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/000" + times + ".jpg"));
            else if (times < 100)
                ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/00" + times + ".jpg"));
            else if (times < 1000)
                ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/0" + times + ".jpg"));
            else
                ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/" + times + ".jpg"));
            System.out.println("图片" + times + "转化完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }


        panel.repaint();
    }

    public static void main(String[] args) {
        new color();

    }

}


