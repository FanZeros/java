import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Copy;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/*
制作by 零殇_Fan
--------------------------------
开发新功能中：涂色

2021/2/3
--------------------------------
更新了不少内容：
1.描绘两次的功能
2.功能可以在界面上直接调整
3.描线防锯齿
4.增加参数，使生成更多样化

2021/2/2
---------------------------------
图片现在支持jpg
参数调试板块还未完成，请先自行调下方的参数吧。
视频记得给个三连让更多人看到，关注up获得最新制作情报

最近期末周up有点忙，可能忙不过来。
反正视频播放越多更新越快咯

2020/12/10
---------------------------------
 */

public class color2 extends JFrame {

    //-------------------page1-----------------------------------------------------
    int abs = 27, //黑边缘
            delta = 15;//色    小的更精密
    int removeBlackPixel = 100;//移除少于此数量的单独的黑点
    int removeBlackPixel2 = 60;//移除黑色块的强度

    int dontProcessColorIfNumLessThan = 5;//移除少于此数量的单独的色块

    double speed = 60;//描绘速度
    boolean paintNotPause = false;//上色不暂停


    boolean colorful = false;//自行随机涂颜色
    boolean black = true;//画黑线

    boolean blackTwice = true;//画两次黑线

    boolean removeBlackLast = true;
    boolean removeForNormal = false;//true用自带颜色替换、false用左边颜色替换 (替换小色块、 黑色块（如果上面是true）)

    private JComboBox<String> jEdgeFont = new JComboBox<String>(new String[]{"字幕不描边", "字幕描边"});

    private JComboBox<String> jRemove = new JComboBox<String>(new String[]{"不消除黑边", "自带颜色替换", "左边颜色替换"});
    private JComboBox<String> jColorful = new JComboBox<String>(new String[]{"普通模式【不处理】", "魔幻色泽"});
    private JComboBox<String> jBlackTwice = new JComboBox<String>(new String[]{"不勾黑边", "勾一次黑边", "勾两次黑边"});

    private JComboBox<String> jQuick = new JComboBox<String>(new String[]{"自行输入参数", "功能1：标准仅去近似色", "功能2：标准去字幕", "功能3：。。。"});


    JSlider jSpeed = new JSlider();
    JLabel jSpeedText2 = new JLabel("x99");

    //-------------------page2-----------------------------------------------------

    private static int mouseX = 0, mouseY = 0;
    private static int imgX = 0, imgY = 0;
    static boolean changeColor = false;

    JColorChooser colorChooser = new JColorChooser(Color.black);

    static int deltaColoring = 15;//涂色的色彩rgb接受差值
    JSlider jDeltaColor = new JSlider();
    JLabel jDeltaColorText = new JLabel("15");

    static int deltaColorHue = 73;//原点色相差值
    JSlider jDeltaColorHue = new JSlider();
    JLabel jDeltaColorTextHue = new JLabel("73");


    JRadioButton jColorCanChange = new JRadioButton("【总开关】点击直接处理图片", false);

    static int operationCnt = 0;
    //------------------------------------------------------------------------

    boolean process = false;
    boolean pro = false;//generation mode
    int time11 = 0;
    String url = "";
    int times = 1;
    int cntColor;
    private JPanel panel = null;
    private JPanel panel1 = null;
    private BufferedImage img = null;
    private BufferedImage img1 = null;
    private BufferedImage imgCopy = null;

    JTextArea jDelta = new JTextArea();
    JTextArea jAbs = new JTextArea();
    JTextArea jRemoveBlackPixel = new JTextArea();
    JTextArea jRemoveBlackPixel2 = new JTextArea();
    JTextArea jDontProcessColorIfNumLessThan = new JTextArea();


    JButton Cal = new JButton("生成");
    JButton Cal1 = new JButton("预览");
    JButton load = new JButton("读取图片");
    JButton change = new JButton("转换图片");
    JButton change1 = new JButton("重新加载");
    JButton saveButtom = new JButton("保存");

    JButton ctrlZ = new JButton("撤销");

    private final JTabbedPane tabbedPane = new JTabbedPane();
    String lfp;

    public color2() {
        initComponent();
        this.setVisible(true);
        Runner1 r1 = new Runner1();
        Thread t = new Thread(r1);
        t.start();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                //System.out.println(e.getX() + " " + e.getY());
                if (jColorCanChange.isSelected() && img != null && mouseX > 29 && mouseX < 990 && mouseY > 59 && mouseY < 596) {

                    imgX = (mouseX - 29) * (img.getWidth()) / (990 - 29);
                    imgY = (mouseY - 59) * (img.getHeight()) / (596 - 59);
                    changeColor = true;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     *
     */
    void Quick() {
        if (jQuick.getSelectedIndex() == 1) {
            jDontProcessColorIfNumLessThan.setText("0");
            jRemove.setSelectedIndex(1);
        }
        if (jQuick.getSelectedIndex() == 2) {
            jDontProcessColorIfNumLessThan.setText("10");
            jRemove.setSelectedIndex(2);
        }
    }

    private void clickTry() {
        initImg();
        pro = false;
        begin();
    }

    private void click() {
        pro = true;
        begin();
    }

    private void begin() {
        try {
            delta = Integer.parseInt(jDelta.getText());
            abs = Integer.parseInt(jAbs.getText());
            removeBlackPixel = Integer.parseInt(jRemoveBlackPixel.getText());
            removeBlackPixel2 = Integer.parseInt(jRemoveBlackPixel2.getText());
            dontProcessColorIfNumLessThan = Integer.parseInt(jDontProcessColorIfNumLessThan.getText());
        } catch (Exception e) {
        }
        if (jColorful.getSelectedIndex() == 0) {
            colorful = false;
        } else {
            colorful = true;
        }

        if (jRemove.getSelectedIndex() == 0) {
            removeBlackLast = false;
        }
        if (jRemove.getSelectedIndex() == 2) {
            removeBlackLast = true;
            removeForNormal = false;
        }
        if (jRemove.getSelectedIndex() == 1) {
            removeBlackLast = true;
            removeForNormal = true;
        }

        if (jBlackTwice.getSelectedIndex() == 0) {
            black = false;
        }
        if (jBlackTwice.getSelectedIndex() == 1) {
            black = true;
            blackTwice = false;
        }
        if (jBlackTwice.getSelectedIndex() == 2) {
            black = true;
            blackTwice = true;
        }


        process = true;


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
            lfp = filepath;
            System.out.println(lfp);
            initImg();

            url = filepath.substring(0, filepath.lastIndexOf("(") + 1);

            try {
                times = Integer.parseInt(filepath.substring(filepath.lastIndexOf("(") + 1, filepath.lastIndexOf(")")));
            } catch (Exception e) {
            }
            panel1.repaint();
        }
    }

    private void initComponent() {

        this.setTitle("自动重绘图片 by 零殇");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1640, 1000);
        setLayout(null);


        JPanel panel1st = new JPanel();
        panel1st.setBounds(0, 0, 600, 650);
        panel1st.setLayout(null);
        JPanel panel2nd = new JPanel();
        panel2nd.setBounds(0, 0, 600, 650);
        panel2nd.setLayout(null);
        tabbedPane.addTab("绘图", null, panel1st, "");
        tabbedPane.addTab("颜色填充", null, panel2nd, "");
        tabbedPane.setBounds(1000, 200, 600, 650);


        jColorCanChange.setBounds(20, 0, 380, 40);
        jColorCanChange.setFont(new Font("", Font.BOLD, 15));
        panel2nd.add(jColorCanChange);

        colorChooser.setBounds(20, 400, 560, 300);
        panel2nd.add(colorChooser);


        jEdgeFont.setBounds(400, 240, 80, 40);
        jEdgeFont.setSelectedIndex(1);
        //panel1st.add(jEdgeFont);

        jColorful.setBounds(400, 500, 150, 40);
        jColorful.setSelectedIndex(0);
        panel1st.add(jColorful);

        jBlackTwice.setBounds(400, 100, 120, 40);
        jBlackTwice.setSelectedIndex(1);
        panel1st.add(jBlackTwice);

        jQuick.addActionListener((e -> Quick()));
        jQuick.setBounds(200, 500, 150, 40);
        jQuick.setSelectedIndex(0);
        panel1st.add(jQuick);


        jRemove.setBounds(400, 400, 150, 40);
        jRemove.setSelectedIndex(1);
        panel1st.add(jRemove);
        JLabel jRemoveText = new JLabel("最后处理黑色描边的模式");
        jRemoveText.setBounds(40, 400, 380, 50);
        jRemoveText.setFont(new Font("", Font.BOLD, 25));
        panel1st.add(jRemoveText);

        jSpeed.addChangeListener((e) -> speedChange());
        jSpeed.setMaximum(75);
        jSpeed.setMinimum(0);
        jSpeed.setBounds(170, 30, 400, 40);
        panel1st.add(jSpeed);
        JLabel jSpeedText = new JLabel("处理速度");
        jSpeedText.setBounds(40, 25, 120, 50);
        jSpeedText.setFont(new Font("", Font.BOLD, 25));
        panel1st.add(jSpeedText);

        jDeltaColorHue.addChangeListener((e) -> colorDeltaChange());
        jDeltaColorHue.setMaximum(200);
        jDeltaColorHue.setMinimum(1);
        jDeltaColorHue.setValue(60);
        jDeltaColorHue.setBounds(230, 230, 340, 40);
        panel2nd.add(jDeltaColorHue);
        JLabel jDeltaColorTextHue2 = new JLabel("原点色相差距");
        jDeltaColorTextHue2.setToolTipText("当前点和原点的(|r-g|+|g-b|)的绝对差值，用于杜绝和原点色差多大的地方，数值越小范围越小");
        jDeltaColorTextHue2.setBounds(40, 225, 180, 50);
        jDeltaColorTextHue2.setFont(new Font("", Font.BOLD, 25));
        panel2nd.add(jDeltaColorTextHue2);
        jDeltaColorTextHue.setBounds(80, 200, 60, 50);
        jDeltaColorTextHue.setFont(new Font("", Font.BOLD, 15));
        panel2nd.add(jDeltaColorTextHue);


        jDeltaColor.addChangeListener((e) -> colorDeltaChange());
        jDeltaColor.setMaximum(75);
        jDeltaColor.setMinimum(1);
        jDeltaColor.setValue(26);
        jDeltaColor.setBounds(230, 330, 340, 40);
        panel2nd.add(jDeltaColor);
        JLabel jDeltaColorText2 = new JLabel("相邻色彩差距");
        jDeltaColorText2.setToolTipText("当前点和周围点的 r g b 的绝对差值之和，用于寻找边缘，数值越小范围越小");
        jDeltaColorText2.setBounds(40, 325, 180, 50);
        jDeltaColorText2.setFont(new Font("", Font.BOLD, 25));
        panel2nd.add(jDeltaColorText2);

        jDeltaColorText.setBounds(80, 300, 60, 50);
        jDeltaColorText.setFont(new Font("", Font.BOLD, 15));
        panel2nd.add(jDeltaColorText);

        jSpeedText2.setBounds(80, 0, 60, 50);
        jSpeedText2.setFont(new Font("", Font.BOLD, 15));
        panel1st.add(jSpeedText2);


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

        change.setBounds(720, 580, 120, 50);
        change.setFont(new Font("", Font.BOLD, 20));
        change.addActionListener((e) -> Change());
        this.add(change);
        change1.setBounds(860, 580, 120, 50);
        change1.setFont(new Font("", Font.BOLD, 20));
        change1.addActionListener((e) -> Change1());
        this.add(change1);


        saveButtom.setBounds(720, 680, 120, 50);
        saveButtom.setFont(new Font("", Font.BOLD, 20));
        saveButtom.addActionListener((e) -> savePic());
        this.add(saveButtom);

        ctrlZ.setBounds(860, 680, 120, 50);
        ctrlZ.setFont(new Font("", Font.BOLD, 20));
        ctrlZ.addActionListener((e) -> CtrlZ());
        this.add(ctrlZ);

        Cal.setBounds(1250, 70, 130, 60);
        Cal.setFont(new Font("", Font.BOLD, 20));
        Cal.addActionListener((e) -> click());
        Cal1.setBounds(1420, 70, 120, 60);
        Cal1.setFont(new Font("", Font.BOLD, 20));
        Cal1.addActionListener((e) -> clickTry());


        jAbs.setText("25");
        jAbs.setBounds(150, 100, 70, 40);
        jAbs.setFont(new Font("", Font.BOLD, 25));
        jAbs.setForeground(Color.BLACK);
        panel1st.add(jAbs);
        JLabel jAbsText = new JLabel("黑边差值");
        jAbsText.setBounds(40, 100, 90, 50);
        jAbsText.setFont(new Font("", Font.BOLD, 20));
        panel1st.add(jAbsText);

        jDelta.setText("7");
        jDelta.setBounds(150, 160, 70, 40);
        jDelta.setFont(new Font("", Font.BOLD, 25));
        jDelta.setForeground(Color.PINK);
        panel1st.add(jDelta);
        JLabel jDeltaText = new JLabel("色泽差值");
        jDeltaText.setBounds(40, 160, 90, 50);
        jDeltaText.setFont(new Font("", Font.BOLD, 20));
        panel1st.add(jDeltaText);

        jDontProcessColorIfNumLessThan.setText("0");
        jDontProcessColorIfNumLessThan.setBounds(250, 240, 70, 40);
        jDontProcessColorIfNumLessThan.setFont(new Font("", Font.BOLD, 25));
        jDontProcessColorIfNumLessThan.setForeground(Color.red);
        panel1st.add(jDontProcessColorIfNumLessThan);
        JLabel jDontProcessColorIfNumLessThanText = new JLabel("不处理少于x像素的色块");
        jDontProcessColorIfNumLessThanText.setBounds(40, 240, 210, 50);
        jDontProcessColorIfNumLessThanText.setFont(new Font("", Font.BOLD, 17));
        panel1st.add(jDontProcessColorIfNumLessThanText);

        jRemoveBlackPixel.setText("300");
        jRemoveBlackPixel.setBounds(200, 320, 70, 40);
        jRemoveBlackPixel.setFont(new Font("", Font.BOLD, 30));
        jRemoveBlackPixel.setForeground(Color.black);
        panel1st.add(jRemoveBlackPixel);
        JLabel jRemoveBlackPixelText = new JLabel("不画少于x像素的黑边");
        jRemoveBlackPixelText.setBounds(10, 320, 210, 50);
        jRemoveBlackPixelText.setFont(new Font("", Font.BOLD, 17));
        panel1st.add(jRemoveBlackPixelText);

        jRemoveBlackPixel2.setText("60");
        jRemoveBlackPixel2.setBounds(490, 320, 70, 40);
        jRemoveBlackPixel2.setFont(new Font("", Font.BOLD, 30));
        jRemoveBlackPixel2.setForeground(Color.black);
        panel1st.add(jRemoveBlackPixel2);
        JLabel jRemoveBlackPixelText2 = new JLabel("移除大个黑色块的强度");
        jRemoveBlackPixelText2.setBounds(320, 320, 160, 50);
        jRemoveBlackPixelText2.setFont(new Font("", Font.BOLD, 15));
        panel1st.add(jRemoveBlackPixelText2);

        this.add(tabbedPane);
        this.add(Cal);
        this.add(Cal1);
        this.add(load);
    }

    private void speedChange() {
        speed = 0.1 * Math.pow(1.1, jSpeed.getValue());
        jSpeedText2.setText("x" + (int) (speed * 10));
        if (speed > 100) {
            jSpeedText2.setText("Max");
        }
    }

    private void colorDeltaChange() {
        deltaColoring = 1 + jDeltaColor.getValue() * jDeltaColor.getValue() / 50;
        jDeltaColorText.setText("" + deltaColoring);

        deltaColorHue = 1 + jDeltaColorHue.getValue() * jDeltaColorHue.getValue() / 50;
        jDeltaColorTextHue.setText("" + deltaColorHue);
    }

    private void Change() {

        BufferedImage img3 = img1;
        img1 = img;
        img = img3;
        panel.repaint();
        panel1.repaint();
    }

    private void Change1() {
        File file = new File(lfp);
        try {
            img = ImageIO.read(file);
            img1 = ImageIO.read(file);
        } catch (IOException ignored) {
        }
        panel.repaint();
        panel1.repaint();
    }

    private void CtrlZ() {
        if(operationCnt==0)return;
        operationCnt--;
        String s = System.getProperty("user.dir") + "/save/data/" + operationCnt + ".jpg";
        File file = new File(s);
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.repaint();
    }

    static boolean stop = false;

    private void initImg() {
        File file = new File(lfp);
        try {
            img = ImageIO.read(file);
            img1 = ImageIO.read(file);
            imgCopy = ImageIO.read(file);
            panel1.repaint();
            panel.repaint();
        } catch (IOException e) {
            if (times <= 1)
                JOptionPane.showMessageDialog(null, "图片丢失");
            else
                JOptionPane.showMessageDialog(null, "成功生成");
            stop = true;

        }
    }

    private void copyPic() {

        try {
            ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/save/data/" + operationCnt + ".jpg"));
            ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/save/last" + ".jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        operationCnt++;
    }

    private void savePic() {
        LocalDateTime currentTime = LocalDateTime.now();

        long date=currentTime.getYear()*10000000000L+currentTime.getDayOfMonth()*1000000+currentTime.getMonthValue()*100000000L+currentTime.getHour()*10000+currentTime.getMinute()*100+currentTime.getSecond();
        try {
            ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/save/" + date + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 处理图片
     */


    public static void main(String[] args) {
        new color2();
    }


    class Runner1 implements Runnable {

        int cnt = 0;

        @Override
        public void run() {
            while (time11 < 10000000) {
                time11++;
                try {
                    Thread.sleep(55);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (changeColor) {

                    copyPic();
                    processImgChangeColor(imgX, imgY);
                    changeColor = false;
                }


                if (process) {

                    if (!pro)
                        processImg();
                    else {
                        for (times = 1; times < 10000; times++) {

                            lfp = url + times + ").jpg";
                            initImg();
                            System.out.println(url + times + ").jpg");
                            processImg();
                            if (stop) break;

                        }
                    }


                    stop = false;
                    process = false;
                }
            }
        }

        //第二页处理  改变颜色
        private void processImgChangeColor(int x, int y) {


            Queue<Integer> q1 = new LinkedList<>();
            q1.add(x * 10000 + y);
            pro3Color(q1, colorChooser.getColor(), new Color(img.getRGB(x, y)));


            panel.repaint();
            System.out.println(x + " " + y);
        }

        //第一页处理   基础
        private void processImg() {
            if (img == null) {
                System.out.println("错误！无图片");
                return;
            }
            int data[][][] = new int[img.getWidth()][img.getHeight()][11];
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    data[x][y][0] = 1;
                    data[x][y][10] = img.getRGB(x, y);
                    Color color = new Color(255, 255, 255);
                    img.setRGB(x, y, color.getRGB());
                    if (!pro)
                        panel.repaint();
                }
            }
            for (int x = 1; x < img.getWidth() - 1; x++) {
                for (int y = 1; y < img.getHeight() - 1; y++) {

                    double red = (new Color(data[x - 1][y][10]).getRed() + new Color(data[x + 1][y][10]).getRed() - new Color(data[x][y - 1][10]).getRed()
                            - new Color(data[x][y + 1][10]).getRed() + 0.5 * (new Color(data[x - 1][y - 1][10]).getRed() - new Color(data[x + 1][y + 1][10]).getRed())
                            + 0.5 * (new Color(data[x + 1][y - 1][10]).getRed() - new Color(data[x - 1][y + 1][10]).getRed()));
                    double green = (new Color(data[x - 1][y][10]).getGreen() + new Color(data[x + 1][y][10]).getGreen() - new Color(data[x][y - 1][10]).getGreen()
                            - new Color(data[x][y + 1][10]).getGreen() + 0.5 * (new Color(data[x - 1][y - 1][10]).getGreen() - new Color(data[x + 1][y + 1][10]).getGreen())
                            + 0.5 * (new Color(data[x + 1][y - 1][10]).getGreen() - new Color(data[x - 1][y + 1][10]).getGreen()));
                    double blue = (new Color(data[x - 1][y][10]).getBlue() + new Color(data[x + 1][y][10]).getBlue() - new Color(data[x][y - 1][10]).getBlue()
                            - new Color(data[x][y + 1][10]).getBlue() + 0.5 * (new Color(data[x - 1][y - 1][10]).getBlue() - new Color(data[x + 1][y + 1][10]).getBlue())
                            + 0.5 * (new Color(data[x + 1][y - 1][10]).getBlue() - new Color(data[x - 1][y + 1][10]).getBlue()));

                    double delta1 =
                            (new Color(data[x - 1][y][10]).getBlue() + new Color(data[x + 1][y][10]).getBlue() + new Color(data[x][y - 1][10]).getBlue() + new Color(data[x][y + 1][10]).getBlue()) -
                                    (new Color(data[x - 1][y][10]).getRed() + new Color(data[x + 1][y][10]).getRed() + new Color(data[x][y - 1][10]).getRed() + new Color(data[x][y + 1][10]).getRed());
                    double delta2 =
                            (new Color(data[x - 1][y][10]).getBlue() + new Color(data[x + 1][y][10]).getBlue() + new Color(data[x][y - 1][10]).getBlue() + new Color(data[x][y + 1][10]).getBlue()) -
                                    (new Color(data[x - 1][y][10]).getGreen() + new Color(data[x + 1][y][10]).getGreen() + new Color(data[x][y - 1][10]).getGreen() + new Color(data[x][y + 1][10]).getGreen());


                    if (black) if (Math.abs(red) + Math.abs(green) + Math.abs(blue) > abs) data[x][y][0] = 0;//black
                    if (black)
                        if (Math.abs(red) + Math.abs(green) + Math.abs(blue) > 2.5 * abs) data[x][y][3] = 1;//gray

                    if (jEdgeFont.getSelectedIndex() == 0) {//字幕不画描边
                        if (Math.abs(delta1) + Math.abs(delta2) < 100) data[x][y][0] = 1;
                    }


                }
            }
            // if(blackTwice)
            for (int y = 1; y < img.getHeight() - 1; y++) {
                for (int x = 1; x < img.getWidth() - 1; x++) {
                    if (data[x][y][1] == 0 && data[x][y][0] <= 0) {
                        Queue<Integer> q = new LinkedList<>();
                        q.add(x * 10000 + y);
                        proRemove(x, y, data, q, 0);
                    }
                }
            }
            for (int y = 1; y < img.getHeight() - 1; y++) {
                for (int x = 1; x < img.getWidth() - 1; x++) {
                    if (data[x][y][1] == 0 && data[x][y][0] <= 0) {
                        Queue<Integer> q = new LinkedList<>();
                        q.add(x * 10000 + y);
                        proRemove(x, y, data, q, 1);
                    }
                }
            }
            //消除小黑

            if (black) {//黑色线描绘
                if (blackTwice)
                    for (int y = 1; y < img.getHeight() - 1; y++) {
                        for (int x = 1; x < img.getWidth() - 1; x++) {
                            if (data[x][y][1] <= 0) {
                                Stack<Integer> q = new Stack<>();
                                q.add(x * 10000 + y);
                                pro(data, q, 1);
                            }
                        }
                    }
                for (int y = 1; y < img.getHeight() - 1; y++) {
                    for (int x = 1; x < img.getWidth() - 1; x++) {
                        if (data[x][y][1] <= 0) {
                            Stack<Integer> q = new Stack<>();
                            q.add(x * 10000 + y);
                            pro(data, q, 0);
                        }
                    }
                }
            }
            for (int y = 1; y < img.getHeight() - 1; y++) {
                for (int x = 1; x < img.getWidth() - 1; x++) {
                    if (data[x][y][1] == 0) {
                        Queue<Integer> q = new LinkedList<>();
                        q.add(x * 10000 + y);
                        Color color = new Color(data[x][y][10]);

                        Color color2 = pro2(data, q, color);

                        if (colorful) {
                            int total = color2.getGreen() + color2.getBlue() + color2.getRed();
                            int min, max;
                            min = Math.max(0, total - 255 * 2);
                            max = Math.min(255, 2 * total / 3);
                            int red = (int) (min + (max - min) * Math.random());
                            total -= red;
                            min = Math.max(0, total - 255);
                            max = Math.min(255, total);
                            int green = (int) (min + (max - min) * Math.random());
                            color2 = new Color(red, green, total - green);
                        }

                        if (cntColor >= dontProcessColorIfNumLessThan) {
                            Queue<Integer> q1 = new LinkedList<>();
                            q1.add(x * 10000 + y);
                            pro3(data, q1, color2);
                            if (cnt > speed * 30) {
                                if (!paintNotPause)
                                    update();
                            }
                        }
                    }
                }
            }


            //remove black
            if (removeBlackLast)
                for (int y = 1; y < img.getHeight() - 1; y++) {
                    for (int x = 1; x < img.getWidth() - 1; x++) {
                        if (data[x][y][9] != 0) {
                            img.setRGB(x, y, data[x][y][9]);
                        }

                        if (removeBlackLast && data[x][y][1] == 1 || data[x][y][1] == 4) {
                            Color color;
                            if (data[x][y][1] == 4 || !removeForNormal) {
                                int x1 = x, y1 = y;
                                int w = img.getWidth(), h = img.getHeight();
                                boolean br = false;
                                for (int i = 1; i < 50; i += 2) {
                                    for (int j = 0; j <= i; j++) {
                                        x1++;
                                        if (data[(w + x1) % w][(h + y1) % h][1] == 2) {
                                            br = true;
                                            break;
                                        }
                                    }
                                    if (br) break;
                                    for (int j = 0; j <= i; j++) {
                                        y1++;
                                        if (data[(w + x1) % w][(h + y1) % h][1] == 2) {
                                            br = true;
                                            break;
                                        }
                                    }
                                    if (br) break;
                                    for (int j = 0; j <= i + 1; j++) {
                                        x1--;
                                        if (data[(w + x1) % w][(h + y1) % h][1] == 2) {
                                            br = true;
                                            break;
                                        }
                                    }
                                    if (br) break;
                                    for (int j = 0; j <= i + 1; j++) {
                                        y1--;
                                        if (data[(w + x1) % w][(h + y1) % h][1] == 2) {
                                            br = true;
                                            break;
                                        }
                                    }
                                    if (br) break;
                                }
                                color = new Color(data[(w + x1) % w][(h + y1) % h][10]);
                            } else color = new Color(data[x][y][10]);
                            img.setRGB(x, y, color.getRGB());
                            cnt++;
                            if (cnt > speed * 20) {
                                if (!paintNotPause)
                                    update();
                                cnt -= speed * 20;
                            }
                        }
                    }
                }

            panel.repaint();
            if (!pro) {
                try {
                    ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/out/out" + (int) (Math.random() * 10000) + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/out/Generation" + times + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        void pro(int data[][][], Stack queue, int mode) {//mode 1 第一次灰,0 第二次黑
            while (queue.size() > 0) {

                int a = (int) queue.pop();
                int x = a / 10000;
                int y = a % 10000;

                if ((mode == 0 && data[x][y][0] <= 0 && data[x][y][1] <= 0) || (mode == 1 && data[x][y][0] <= 0 && data[x][y][3] == 1)) {

                    Color color;
                    if (mode == 0)
                        color = new Color(0, 0, 0);
                    else
                        color = new Color(160, 160, 160);

                    img.setRGB(x, y, color.getRGB());

                    if (mode == 0) {
                        data[x][y][1] = 1;
                        data[x][y][0] = 2;
                    }
                    data[x][y][3] = 0;


                    //周围浅描绘
                    if (mode == 0) {
                        int delta = 80, delta1 = 0, max = 40;

                        int x1, y1;
                        x1 = (img.getWidth() + x + 1) % img.getWidth();
                        y1 = (img.getHeight() + y) % img.getHeight();
                        int c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());
                        x1 = (img.getWidth() + x - 1) % img.getWidth();
                        y1 = (img.getHeight() + y) % img.getHeight();
                        c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());
                        x1 = (img.getWidth() + x) % img.getWidth();
                        y1 = (img.getHeight() + y + 1) % img.getHeight();
                        c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());
                        x1 = (img.getWidth() + x) % img.getWidth();
                        y1 = (img.getHeight() + y - 1) % img.getHeight();
                        c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());


                        x1 = (img.getWidth() + x + 2) % img.getWidth();
                        y1 = (img.getHeight() + y) % img.getHeight();
                        c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta1);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());
                        x1 = (img.getWidth() + x - 2) % img.getWidth();
                        y1 = (img.getHeight() + y) % img.getHeight();
                        c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta1);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());
                        x1 = (img.getWidth() + x) % img.getWidth();
                        y1 = (img.getHeight() + y + 2) % img.getHeight();
                        c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta1);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());
                        x1 = (img.getWidth() + x) % img.getWidth();
                        y1 = (img.getHeight() + y - 2) % img.getHeight();
                        c = new Color(img.getRGB(x1, y1)).getRed();
                        c = Math.max(Math.min(max, c), c - delta1);
                        img.setRGB(x1, y1, new Color(c, c, c).getRGB());
                    }

                    //------------------------
                    cnt++;
                    if (cnt > speed * 10) {
                        update();
                    }

                    if (queue.size() < 1000) {
                        if (x < img.getWidth() - 1 && data[x + 1][y][1] <= 0) {
                            int ad = (x + 1) * 10000 + y;
                            queue.add(ad);
                        }
                        if (y < img.getHeight() - 1 && data[x][y + 1][1] <= 0) {
                            int ad = (x) * 10000 + y + 1;
                            queue.add(ad);
                        }
                        if (x > 0 && data[x - 1][y][1] <= 0) {
                            int ad = (x - 1) * 10000 + y;
                            queue.add(ad);
                        }
                        if (y > 0 && data[x][y - 1][1] <= 0) {
                            int ad = (x) * 10000 + y - 1;
                            queue.add(ad);
                        }
                    }
                }
            }
        }//dfs stack<1000 用dfs画黑边

        void pro(int x, int y, int data[][][]) {
            if (data[x][y][1] > 0) return;
            if (data[x][y][0] == 0 && data[x][y][1] <= 0) {
                data[x][y][1] = 1;

                Color color = new Color(0, 0, 0);
                img.setRGB(x, y, color.getRGB());

                cnt++;
                if (cnt > 10) {
                    update();
                }
                if (x < img.getWidth() - 1 && data[x + 1][y][1] <= 0)
                    pro(x + 1, y, data);
                if (y < img.getHeight() - 1 && data[x][y + 1][1] <= 0)
                    pro(x, y + 1, data);
                if (x > 0 && data[x - 1][y][1] <= 0) {
                    pro(x - 1, y, data);
                }
                if (y > 0 && data[x][y - 1][1] <= 0) {
                    pro(x, y - 1, data);
                }
            }
        }//dfs

        void pro(int data[][][], Queue queue) {
            while (queue.size() > 0) {


                int a = (int) queue.poll();
                int x = a / 10000;
                int y = a % 10000;

                if (data[x][y][0] == 0 && data[x][y][1] <= 0) {
                    data[x][y][1] = 1;

                    Color color = new Color(0, 0, 0);
                    img.setRGB(x, y, color.getRGB());
                    cnt++;
                    if (cnt > speed * 10) {
                        update();
                    }

                    if (x < img.getWidth() - 1 && data[x + 1][y][1] <= 0) {
                        int ad = (x + 1) * 10000 + y;
                        queue.add(ad);
                    }
                    if (y < img.getHeight() - 1 && data[x][y + 1][1] <= 0) {
                        int ad = (x) * 10000 + y + 1;
                        queue.add(ad);
                    }
                    if (x > 0 && data[x - 1][y][1] <= 0) {
                        int ad = (x - 1) * 10000 + y;
                        queue.add(ad);
                    }
                    if (y > 0 && data[x][y - 1][1] <= 0) {
                        int ad = (x) * 10000 + y - 1;
                        queue.add(ad);
                    }
                }
            }
        }//bfs

        void proRemove(int x1, int y1, int data[][][], Queue queue, int mode) {//第1次，测定小黑块大小
            int size = 0;
            int maxX = 0, minX = 99999, maxY = 0, minY = 99999, l = 0;
            while (queue.size() > 0) {

                size++;
                int a = (int) queue.poll();
                int x = a / 10000;
                int y = a % 10000;

                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                l = Math.min(maxX - minX + 8, maxY - minY + 8);
                if ((mode == 0 && data[x][y][0] <= 0 && data[x][y][1] == 0) || (mode == 1 && data[x][y][0] <= 0 && data[x][y][1] == 0 && data[x][y][3] == 1)) {
                    data[x][y][1] = -1;

                    if (x < img.getWidth() - 1 && data[x + 1][y][1] == 0) {
                        int ad = (x + 1) * 10000 + y;
                        queue.add(ad);
                    }
                    if (y < img.getHeight() - 1 && data[x][y + 1][1] == 0) {
                        int ad = (x) * 10000 + y + 1;
                        queue.add(ad);
                    }
                    if (x > 0 && data[x - 1][y][1] == 0) {
                        int ad = (x - 1) * 10000 + y;
                        queue.add(ad);
                    }
                    if (y > 0 && data[x][y - 1][1] == 0) {
                        int ad = (x) * 10000 + y - 1;
                        queue.add(ad);
                    }
                }
            }


            if (size <= removeBlackPixel || l * l < size * removeBlackPixel2 / 100 + 1) {
                Queue<Integer> q1 = new LinkedList<>();
                q1.add(x1 * 10000 + y1);
                proRemove2(data, q1, mode);
            }

        }//bfs


        void proRemove2(int data[][][], Queue queue, int mode) {//第二次，小黑块变白

            while (queue.size() > 0) {

                int a = (int) queue.poll();
                int x = a / 10000;
                int y = a % 10000;

                //   Color color = new Color(0, 0, 0);img.setRGB(x, y, color.getRGB());cnt++;if (cnt > speed*100) { update(); }

                if ((mode == 0 && data[x][y][0] <= 0 && data[x][y][1] == -1) || (mode == 1 && data[x][y][0] <= 0 && data[x][y][1] == -1 && data[x][y][3] == 1)) {
                    data[x][y][1] = 0;
                    if (mode == 0) {
                        data[x][y][0] = 1;
                    } else {
                        data[x][y][3] = 0;
                    }

                    if (x < img.getWidth() - 1 && data[x + 1][y][1] < 0) {
                        int ad = (x + 1) * 10000 + y;
                        queue.add(ad);
                    }
                    if (y < img.getHeight() - 1 && data[x][y + 1][1] < 0) {
                        int ad = (x) * 10000 + y + 1;
                        queue.add(ad);
                    }
                    if (x > 0 && data[x - 1][y][1] < 0) {
                        int ad = (x - 1) * 10000 + y;
                        queue.add(ad);
                    }
                    if (y > 0 && data[x][y - 1][1] < 0) {
                        int ad = (x) * 10000 + y - 1;
                        queue.add(ad);
                    }
                }
            }
        }//bfs

        Color pro2(int data[][][], Queue queue, Color color2) {
            long red = 0, blue = 0, green = 0, cnt = 0;
            while (queue.size() > 0) {
                cnt++;
                int a = (int) queue.poll();
                int x = a / 10000;
                int y = a % 10000;
                Color color = new Color(data[x][y][10]);
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();

                if (data[x][y][1] == 0 && data[x][y][0] != 2) {
                    data[x][y][1] = 4;


                    Color color1;
                    int max = delta;
                    if (x < img.getWidth() - 1 && data[x + 1][y][1] == 0) {
                        color1 = new Color(data[x + 1][y][10]);
                        if (Math.abs(color.getRed() - color1.getRed()) + Math.abs(color.getGreen() - color1.getGreen()) + Math.abs(color.getBlue() - color1.getBlue()) < max) {
                            int ad = (x + 1) * 10000 + y;
                            queue.add(ad);
                        }
                    }
                    if (y < img.getHeight() - 1 && data[x][y + 1][1] == 0) {
                        color1 = new Color(data[x][y + 1][10]);
                        if (Math.abs(color.getRed() - color1.getRed()) + Math.abs(color.getGreen() - color1.getGreen()) + Math.abs(color.getBlue() - color1.getBlue()) < max) {
                            int ad = (x) * 10000 + y + 1;
                            queue.add(ad);
                        }
                    }
                    if (x > 0 && data[x - 1][y][1] == 0) {
                        color1 = new Color(data[x - 1][y][10]);
                        if (Math.abs(color.getRed() - color1.getRed()) + Math.abs(color.getGreen() - color1.getGreen()) + Math.abs(color.getBlue() - color1.getBlue()) < max) {
                            int ad = (x - 1) * 10000 + y;
                            queue.add(ad);
                        }
                    }
                    if (y > 0 && data[x][y - 1][1] == 0) {
                        color1 = new Color(data[x][y - 1][10]);
                        if (Math.abs(color.getRed() - color1.getRed()) + Math.abs(color.getGreen() - color1.getGreen()) + Math.abs(color.getBlue() - color1.getBlue()) < max) {
                            int ad = (x) * 10000 + y - 1;
                            queue.add(ad);
                        }
                    }
                }
            }
            cntColor = (int) cnt;
            return new Color((int) (red / cnt), (int) (green / cnt), (int) (blue / cnt));
        }//bfs

        void pro3(int data[][][], Queue queue, Color color3) {
            while (queue.size() > 0) {

                int a = (int) queue.poll();
                int x = a / 10000;
                int y = a % 10000;

                if (data[x][y][1] == 4 && data[x][y][0] != 2) {
                    data[x][y][1] = 2;
                    data[x][y][9] = color3.getRGB();
                    if (new Color(img.getRGB(x, y)).getRed() == 255)
                        img.setRGB(x, y, color3.getRGB());
                    else {
                        Color colorNow = new Color(img.getRGB(x, y));
                        int r = Math.max(0, colorNow.getRed() + color3.getRed() - 255);
                        int g = Math.max(0, colorNow.getGreen() + color3.getGreen() - 255);
                        int b = Math.max(0, colorNow.getBlue() + color3.getBlue() - 255);
                        Color color4 = new Color(r, g, b);
                        img.setRGB(x, y, color4.getRGB());
                    }

                    cnt++;
                    if (cnt > speed * 4500) {
                        if (!paintNotPause)
                            update();
                    }


                    if (x < img.getWidth() - 1 && data[x + 1][y][1] == 4) {
                        int ad = (x + 1) * 10000 + y;
                        queue.add(ad);

                    }
                    if (y < img.getHeight() - 1 && data[x][y + 1][1] == 4) {
                        int ad = (x) * 10000 + y + 1;
                        queue.add(ad);

                    }
                    if (x > 0 && data[x - 1][y][1] == 4) {
                        int ad = (x - 1) * 10000 + y;
                        queue.add(ad);

                    }
                    if (y > 0 && data[x][y - 1][1] == 4) {
                        int ad = (x) * 10000 + y - 1;
                        queue.add(ad);

                    }
                }
            }
        }//bfs

        void pro3Color(Queue queue, Color targetColor, Color standardColor) {

            int data[][][] = new int[img.getWidth()][img.getHeight()][2];
            Color colorData[][] = new Color[img.getWidth()][img.getHeight()];
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    colorData[i][j] = new Color(img.getRGB(i, j));
                }
            }
            LocalDateTime currentTime = LocalDateTime.now();
            int seconds=currentTime.getSecond();
            while (queue.size() > 0) {
                int max = deltaColoring;
                int max2 = deltaColorHue;


                int a = (int) queue.poll();
                int x = a / 10000;
                int y = a % 10000;


                Color colorNow = colorData[x][y];
                int r = Math.max(0, colorNow.getRed() - standardColor.getRed() + targetColor.getRed());
                int g = Math.max(0, colorNow.getGreen() - standardColor.getGreen() + targetColor.getGreen());
                int b = Math.max(0, colorNow.getBlue() - standardColor.getBlue() + targetColor.getBlue());
                Color color4 = new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));


                Color color = new Color(img.getRGB(x, y));
                Color color1;

                if (x < img.getWidth() - 1 && data[x + 1][y][0] == 0) {
                    color1 = new Color(img.getRGB(x + 1, y));

                    int rDelta = color.getRed() - color1.getRed();
                    int gDelta = color.getGreen() - color1.getGreen();
                    int bDelta = color.getBlue() - color1.getBlue();
                    int rDeltaS = standardColor.getRed() - color1.getRed();
                    int gDeltaS = standardColor.getGreen() - color1.getGreen();
                    int bDeltaS = standardColor.getBlue() - color1.getBlue();
                    if (Math.abs(rDeltaS - gDeltaS) + Math.abs(gDeltaS - bDeltaS) < max2)
                        if (Math.abs(rDelta) + Math.abs(gDelta) + Math.abs(bDelta) < max) {
                            int ad = (x + 1) * 10000 + y;
                            queue.add(ad);
                        }
                }
                if (y < img.getHeight() - 1 && data[x][y + 1][0] == 0) {
                    color1 = new Color(img.getRGB(x, y + 1));
                    int rDelta = color.getRed() - color1.getRed();
                    int gDelta = color.getGreen() - color1.getGreen();
                    int bDelta = color.getBlue() - color1.getBlue();
                    int rDeltaS = standardColor.getRed() - color1.getRed();
                    int gDeltaS = standardColor.getGreen() - color1.getGreen();
                    int bDeltaS = standardColor.getBlue() - color1.getBlue();
                    if (Math.abs(rDeltaS - gDeltaS) + Math.abs(gDeltaS - bDeltaS) < max2)
                        if (Math.abs(rDelta) + Math.abs(gDelta) + Math.abs(bDelta) < max) {
                            int ad = (x) * 10000 + y + 1;
                            queue.add(ad);
                        }
                }
                if (x > 0 && data[x - 1][y][0] == 0) {
                    color1 = new Color(img.getRGB(x - 1, y));
                    int rDelta = color.getRed() - color1.getRed();
                    int gDelta = color.getGreen() - color1.getGreen();
                    int bDelta = color.getBlue() - color1.getBlue();
                    int rDeltaS = standardColor.getRed() - color1.getRed();
                    int gDeltaS = standardColor.getGreen() - color1.getGreen();
                    int bDeltaS = standardColor.getBlue() - color1.getBlue();
                    if (Math.abs(rDeltaS - gDeltaS) + Math.abs(gDeltaS - bDeltaS) < max2)
                        if (Math.abs(rDelta) + Math.abs(gDelta) + Math.abs(bDelta) < max) {
                            int ad = (x - 1) * 10000 + y;
                            queue.add(ad);
                        }
                }
                if (y > 0 && data[x][y - 1][0] == 0) {
                    color1 = new Color(img.getRGB(x, y - 1));
                    int rDelta = color.getRed() - color1.getRed();
                    int gDelta = color.getGreen() - color1.getGreen();
                    int bDelta = color.getBlue() - color1.getBlue();
                    int rDeltaS = standardColor.getRed() - color1.getRed();
                    int gDeltaS = standardColor.getGreen() - color1.getGreen();
                    int bDeltaS = standardColor.getBlue() - color1.getBlue();
                    if (Math.abs(rDeltaS - gDeltaS) + Math.abs(gDeltaS - bDeltaS) < max2)
                        if (Math.abs(rDelta) + Math.abs(gDelta) + Math.abs(bDelta) < max) {
                            int ad = (x) * 10000 + y - 1;
                            queue.add(ad);
                        }
                }
                img.setRGB(x, y, color4.getRGB());
                data[x][y][0] = 1;

                cnt++;
                if (cnt > 4000) {
                    update();
                }
                if((currentTime.getSecond()-seconds+60)%60>5){
                    System.out.println(queue.size()+"!");
                    break;
                }
            }
        }//bfs

        void update() {


            cnt = 0;
            if (speed < 100)
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            panel.repaint();
        }
    }
}



