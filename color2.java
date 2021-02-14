
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/*
制作by 零殇_Fan
--------------------------------
1.新功能：二次元魔法变天【选择图片特定部分的区块，用颜色或者贴图替换】
可以使用颜色替换和贴图替换（贴图也要jpg）
在计算方式1中，选完参数后直接点击图片上你想要更改的地方就能替换；计算方式2中，点击预览或者生成键自动替换
2.消除了部分bug，增加只描绘黑线的选项
3.图片自适应

2021/2/12
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

    static double percentIMG = 1;
    //-------------------page1-----------------------------------------------------
    int absBlack = 27, //黑边缘
            deltaColor = 15;//色    小的更精密
    int removeBlackPixel = 100;//移除少于此数量的单独的黑点
    int removeBlackPixel2 = 60;//移除黑色块的强度

    int notProcessColorIfNumLessThan = 5;//移除少于此数量的单独的色块

    double speed = 60;//描绘速度
    boolean paintNotPause = false;//上色不暂停


    boolean colorful = false;//自行随机涂颜色
    boolean black = true;//画黑线

    boolean blackTwice = true;//画两次黑线

    boolean removeBlackLast = true;
    boolean removeForNormal = false;//true用自带颜色替换、false用左边颜色替换 (替换小色块、 黑色块（如果上面是true）)

    private JComboBox<String> jRemove = new JComboBox<String>(new String[]{"不消除黑边", "自带颜色替换", "左边颜色替换"});
    private JComboBox<String> jColorful = new JComboBox<String>(new String[]{"普通模式【不处理】", "魔幻色泽", "只勾黑边不上色"});
    private JComboBox<String> jBlackTwice = new JComboBox<String>(new String[]{"不勾黑边", "勾一次黑边", "勾两次黑边"});

    private JComboBox<String> jQuick = new JComboBox<String>(new String[]{"自行输入参数", "功能1：标准仅去近似色", "功能2：标准去字幕", "功能3：。。。"});


    JSlider jSpeed = new JSlider();
    JLabel jSpeedText2 = new JLabel("x99");

    //-------------------page2-----------------------------------------------------
    JTabbedPane tabbedPane2ndDown = new JTabbedPane();
    JTabbedPane tabbedPane2ColorMod = new JTabbedPane();

    private static int mouseX = 0, mouseY = 0;
    private static int imgX = 0, imgY = 0;
    static boolean changeColor = false;
    static boolean changeColor2 = false;
    static boolean selectedColor = false;//在色彩范围栏中选择颜色

    JColorChooser colorChooser = new JColorChooser(Color.black);

    static int deltaColoring = 15;//涂色的色彩rgb接受差值
    JSlider jDeltaColor = new JSlider();
    JLabel jDeltaColorText = new JLabel("15");

    static int deltaColorHue = 73;//原点色相差值
    JSlider jDeltaColorHue = new JSlider();
    JLabel jDeltaColorTextHue = new JLabel("73");

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
    private JComboBox<String> jLimitColorRange = new JComboBox<String>(new String[]{"不限制色域", "使用处理2的限制色域"});
    static int operationCnt = 0;

    private JComboBox<String> jParallaxesChooser = new JComboBox<String>(new String[]{"替换", "叠加", "消除【黑底】", "曝光【白底】","透明【仅png有效】"});
    private JComboBox<String> jParallaxesPositionChooser = new JComboBox<String>(new String[]{"替换贴图：原点", "替换贴图：点击点为中心"});
    //------------------------------------------------------------------------

    static long time = 0;
    boolean process = false;
    boolean pro = false;//generation mode

    String url = "";
    int times = 1;
    int cntColor;
    private JPanel panelMap = null;
    private JPanel panel = null;
    private JPanel panel1 = null;
    private BufferedImage img = null;
    private BufferedImage img1 = null;
    private BufferedImage imgMap = null;

    JTextArea jDelta = new JTextArea();
    JTextArea jAbs = new JTextArea();
    JTextArea jRemoveBlackPixel = new JTextArea();
    JTextArea jRemoveBlackPixel2 = new JTextArea();
    JTextArea jDontProcessColorIfNumLessThan = new JTextArea();

    JButton lastButton = new JButton("<");
    JButton nextButton = new JButton(">");

    JButton help = new JButton("帮助");
    JButton Cal = new JButton("生成");
    JButton Cal1 = new JButton("预览");
    JButton load = new JButton("读取图片");
    JButton loadMap = new JButton("读取贴图");
    JButton change = new JButton("转换图片");
    JButton change1 = new JButton("重新加载");
    JButton saveButton = new JButton("保存【.png】");
    JButton ctrlZ = new JButton("撤销");
    JButton reSetColor = new JButton("重置");

    JLabel nowNumText = new JLabel("1");

    private final JTabbedPane tabbedPane = new JTabbedPane();
    String urlNow;

    public color2() {

        initComponent();
        this.setVisible(true);
        this.setBackground(new Color(0, 0, 100));
        Runner1 r1 = new Runner1();
        Thread t = new Thread(r1);
        t.start();
        setFocusable(true);

        this.addMouseListener(new MouseAdapter() {

            boolean mousePress = false;

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == 1) {//左键
                    mouseX = e.getX();
                    mouseY = e.getY();
                    if (tabbedPane2ColorMod.getSelectedIndex() == 0 && tabbedPane.getSelectedIndex() == 1 && img != null && mouseX > 29 && mouseX < 990 && mouseY > 59 && mouseY < 596) {

                        imgX = (int) (img.getWidth() / 2 + (mouseX - (990 + 29) / 2) / (percentIMG));
                        imgY = (int) (img.getHeight() / 2 + (mouseY - ((596 + 59)) / 2) / (percentIMG));
                        if (imgX > 0 && imgY > 0 && imgX < img.getWidth() && imgY < img.getHeight())
                            changeColor = true;
                    }
                    if (tabbedPane2ColorMod.getSelectedIndex() == 1 && tabbedPane.getSelectedIndex() == 1 && img != null && mouseX > 29 && mouseX < 990 && mouseY > 59 && mouseY < 596) {
                        imgX = (int) (img.getWidth() / 2 + (mouseX - (990 + 29) / 2) / (percentIMG));
                        imgY = (int) (img.getHeight() / 2 + (mouseY - ((596 + 59)) / 2) / (percentIMG));
                        if (imgX > 0 && imgY > 0 && imgX < img.getWidth() && imgY < img.getHeight())
                            selectedColor = true;
                    }
                }
                if (e.getButton() == 3) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    if (tabbedPane2ColorMod.getSelectedIndex() == 0 && tabbedPane.getSelectedIndex() == 1 && img != null && mouseX > 29 && mouseX < 990 && mouseY > 59 && mouseY < 596) {

                        imgX = (int) (img.getWidth() / 2 + (mouseX - (990 + 29) / 2) / (percentIMG));
                        imgY = (int) (img.getHeight() / 2 + (mouseY - ((596 + 59)) / 2) / (percentIMG));
                        if (imgX > 0 && imgY > 0 && imgX < img.getWidth() && imgY < img.getHeight())
                            changeColor2 = true;
                    }
                }//右键
            }


            @Override
            public void mousePressed(MouseEvent e) {
                mousePress = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePress = false;
                changeColor = false;
            }


        });


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                //System.out.println(e.getKeyCode());
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_LEFT) lastPicture();
                if (code == KeyEvent.VK_RIGHT) nextPicture();
            }
        });


    }
    void helpButton(){
        JOptionPane.showMessageDialog(null, "1.《自动描线绘图》功能部分主要参数为“黑边差值（决定黑边的厚度）”和“色泽差值（决定色块的大小，越小越真实）”如果生成出的图片不好看可以改这两个参数\n" +
                "2.《二次元魔幻变天》功能可以实现 批量改发色/天空等等有明确边界的地方，也可以修改图片透明度、加水印、上色等等\n"
        +"3.处理单张图片请按 预览，处理多张图片使用 生成，生成功能在此之前要对图片们统一命名【即全选后点击重命名】\n"
        +"4.这个图片处理器主打处理二次元图片，三次元图片处理会由于模糊问题处理不佳，并且尽量选择“尽量二次元的图片”");
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
            deltaColor = Integer.parseInt(jDelta.getText());
            absBlack = Integer.parseInt(jAbs.getText());
            removeBlackPixel = Integer.parseInt(jRemoveBlackPixel.getText());
            removeBlackPixel2 = Integer.parseInt(jRemoveBlackPixel2.getText());
            notProcessColorIfNumLessThan = Integer.parseInt(jDontProcessColorIfNumLessThan.getText());
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

    private void resetRGB() {//重置选择rgb范围
        r1.setText("255");
        r2.setText("0");
        g1.setText("255");
        g2.setText("0");
        b1.setText("255");
        b2.setText("0");
        rg1.setText("255");
        rg2.setText("-255");
        gb1.setText("255");
        gb2.setText("-255");
    }

    private void LoadMap() {
        String path = System.getProperty("user.dir")+"/parallaxes";
        JFileChooser choose = new JFileChooser(path);
        choose.setDialogTitle("Please Choose Picture");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "贴图", "jpg","png");
        choose.setFileFilter(filter);
        int returnVal = choose.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = choose.getSelectedFile().getPath();
            try {
                imgMap = ImageIO.read(new File(filepath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            panelMap.repaint();


        }
    }

    private void Load() {
        String path = System.getProperty("user.dir");
        JFileChooser choose = new JFileChooser(path);
        choose.setDialogTitle("请选择图片");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "图片", "png","jpg");
        choose.setFileFilter(filter);
        int returnVal = choose.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = choose.getSelectedFile().getPath();
            urlNow = filepath;
            System.out.println(urlNow);
            initImg();
            url = filepath.substring(0, filepath.lastIndexOf("(") + 1);
            try {
                times = Integer.parseInt(filepath.substring(filepath.lastIndexOf("(") + 1, filepath.lastIndexOf(")")));
                nowNumText.setText(String.valueOf(times));
            } catch (Exception e) {
            }
            panel1.repaint();
        }
    }

    private void initComponent() {

        this.setTitle("图片二创软件 by 零殇         [1.软件免费使用，严禁二次售卖  2.使用该软件制作多张图片并发布时，请附上该软件的发布地址【比如b站视频地址】，谢谢]");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1640, 1000);
        setLayout(null);


        JPanel panel1st = new JPanel();
        panel1st.setBounds(0, 0, 600, 700);
        panel1st.setLayout(null);
        JPanel panel2nd = new JPanel();
        panel2nd.setBounds(0, 0, 600, 700);
        panel2nd.setLayout(null);

        tabbedPane.addTab("自动描线绘图", null, panel1st, "");
        tabbedPane.addTab("二次元魔幻变天", null, panel2nd, "");
        tabbedPane.setBounds(1000, 200, 600, 700);

        tabbedPane2ColorMod.setBounds(0, 10, 600, 330);
        panel2nd.add(tabbedPane2ColorMod);
        tabbedPane2ndDown.setBounds(0, 350, 600, 340);
        panel2nd.add(tabbedPane2ndDown);

        //2nd上半部分
        JPanel panel2ndUp1 = new JPanel();
        panel2ndUp1.setBounds(0, 0, 600, 300);
        panel2ndUp1.setLayout(null);
        tabbedPane2ColorMod.addTab("计算方式1：单次替换点击周围区域", null, panel2ndUp1, "");
        JPanel panel2ndUp2 = new JPanel();
        panel2ndUp2.setBounds(0, 0, 600, 300);
        panel2ndUp2.setLayout(null);
        tabbedPane2ColorMod.addTab("计算方式2：一键替换限定颜色区域", null, panel2ndUp2, "");

        JLabel help2Label = new JLabel("点击图片更改选定区域块的颜色，调节参数可以控制区域大小。远景建议第一条拉满，第二条10左右");
        help2Label.setBounds(10, 30, 600, 30);
        panel2ndUp1.add(help2Label);
        JLabel help2Label2 = new JLabel("换（头发）颜色建议第一条拉50上下，第二条拉一半；鼠标悬浮在参数上可以查看参数帮助");
        help2Label2.setBounds(10, 60, 600, 30);
        panel2ndUp1.add(help2Label2);
        JLabel rgb2 = new JLabel("  点击重置最大化颜色范围，左键点击图片点缩小颜色区域，点击预览看效果");
        rgb2.setBounds(50, 210, 600, 50);
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
        panel2ndUp2.add(r1);
        panel2ndUp2.add(r2);
        panel2ndUp2.add(g1);
        panel2ndUp2.add(g2);
        panel2ndUp2.add(b1);
        panel2ndUp2.add(b2);
        panel2ndUp2.add(gb1);
        panel2ndUp2.add(gb2);
        panel2ndUp2.add(rg1);
        panel2ndUp2.add(rg2);
        panel2ndUp2.add(rgb1);
        panel2ndUp2.add(rgb2);
        reSetColor.setBounds(20, 3, 90, 37);
        reSetColor.setFont(new Font("", Font.BOLD, 19));
        reSetColor.addActionListener((e) -> resetRGB());
        panel2ndUp2.add(reSetColor);

        //2nd下半部分
        JPanel panel2nd1 = new JPanel();
        panel2nd1.setBounds(0, 0, 600, 300);
        panel2nd1.setLayout(null);
        tabbedPane2ndDown.addTab("模式1：用颜色替换", null, panel2nd1, "");
        JPanel panel2nd2 = new JPanel();
        panel2nd2.setBounds(0, 0, 600, 300);
        panel2nd2.setLayout(null);
        tabbedPane2ndDown.addTab("模式2：用贴图替换", null, panel2nd2, "");
        loadMap.setBounds(20, 20, 140, 40);
        loadMap.setFont(new Font("", Font.BOLD, 19));
        loadMap.addActionListener((e) -> LoadMap());
        panel2nd2.add(loadMap);


        panelMap = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.drawImage(imgMap, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        panelMap.setBounds(280, 25, 250, 250);
        panelMap.setBackground(new Color(255, 224, 224));
        panel2nd2.add(panelMap);


        colorChooser.setBounds(20, 0, 560, 300);
        panel2nd1.add(colorChooser);


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

        jLimitColorRange.setBounds(20, 240, 150, 40);
        jLimitColorRange.setSelectedIndex(0);
        panel2ndUp1.add(jLimitColorRange);

        jParallaxesChooser.setBounds(20, 100, 150, 40);
        jParallaxesChooser.setSelectedIndex(0);
        panel2nd2.add(jParallaxesChooser);
        jParallaxesPositionChooser.setBounds(20, 200, 150, 40);
        jParallaxesPositionChooser.setSelectedIndex(0);
        panel2nd2.add(jParallaxesPositionChooser);

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
        jDeltaColorHue.setBounds(230, 130, 340, 40);
        panel2ndUp1.add(jDeltaColorHue);
        JLabel jDeltaColorTextHue2 = new JLabel("原点色相差距");
        jDeltaColorTextHue2.setToolTipText("当前点和原点的(|r-g|+|g-b|)的绝对差值，用于杜绝和原点色差多大的地方，数值越小范围越小");
        jDeltaColorTextHue2.setBounds(40, 125, 180, 50);
        jDeltaColorTextHue2.setFont(new Font("", Font.BOLD, 25));
        panel2ndUp1.add(jDeltaColorTextHue2);
        jDeltaColorTextHue.setBounds(80, 100, 60, 50);
        jDeltaColorTextHue.setFont(new Font("", Font.BOLD, 15));
        panel2ndUp1.add(jDeltaColorTextHue);


        jDeltaColor.addChangeListener((e) -> colorDeltaChange());
        jDeltaColor.setMaximum(75);
        jDeltaColor.setMinimum(1);
        jDeltaColor.setValue(26);
        jDeltaColor.setBounds(230, 180, 340, 40);
        panel2ndUp1.add(jDeltaColor);
        JLabel jDeltaColorText2 = new JLabel("相邻色彩差距");
        jDeltaColorText2.setToolTipText("当前点和周围点的 r g b 的绝对差值之和，用于寻找边缘，数值越小范围越小");
        jDeltaColorText2.setBounds(40, 175, 180, 50);
        jDeltaColorText2.setFont(new Font("", Font.BOLD, 25));
        panel2ndUp1.add(jDeltaColorText2);
        jDeltaColorText.setBounds(80, 150, 60, 50);
        jDeltaColorText.setFont(new Font("", Font.BOLD, 15));
        panel2ndUp1.add(jDeltaColorText);


        jSpeedText2.setBounds(80, 0, 60, 50);
        jSpeedText2.setFont(new Font("", Font.BOLD, 15));
        panel1st.add(jSpeedText2);


        panel1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                if (img1 != null) {
                    double percent = Math.min(1.0 * this.getWidth() / img1.getWidth(), 1.0 * this.getHeight() / img1.getHeight());
                    g2.drawImage(img1, (int) (img1.getWidth() * (-percent / 2) + this.getWidth() / 2), (int) (img1.getHeight() * (-percent / 2) + this.getHeight() / 2), (int) (img1.getWidth() * (percent)), (int) (img1.getHeight() * (percent)), null);

                }
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
                if (img != null) {
                    double percent = Math.min(1.0 * this.getWidth() / img.getWidth(), 1.0 * this.getHeight() / img.getHeight());
                    percentIMG = percent;
                    g2.drawImage(img, (int) (img.getWidth() * (-percent / 2) + this.getWidth() / 2), (int) (img.getHeight() * (-percent / 2) + this.getHeight() / 2), (int) (img.getWidth() * (percent)), (int) (img.getHeight() * (percent)), null);
                }
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


        saveButton.setBounds(720, 680, 130, 50);
        saveButton.setFont(new Font("", Font.BOLD, 15));
        saveButton.addActionListener((e) -> savePic());
        this.add(saveButton);

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

        help.setBounds(10, 905, 86, 40);
        help.setFont(new Font("", Font.BOLD, 20));
        help.addActionListener((e) -> helpButton());
        this.add(help);

        nowNumText.setBounds(360, 905, 46, 40);
        nowNumText.setFont(new Font("", Font.BOLD, 20));
        this.add(nowNumText);
        lastButton.setBounds(300, 905, 46, 40);
        lastButton.setFont(new Font("", Font.BOLD, 20));
        lastButton.addActionListener((e) -> lastPicture());
        this.add(lastButton);
        nextButton.setBounds(400, 905, 46, 40);
        nextButton.setFont(new Font("", Font.BOLD, 20));
        nextButton.addActionListener((e) -> nextPicture());
        this.add(nextButton);


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
        File file = new File(urlNow);
        try {
            img = ImageIO.read(file);
            img1 = ImageIO.read(file);
        } catch (IOException ignored) {
        }
        panel.repaint();
        panel1.repaint();
    }

    private void CtrlZ() {
        if (operationCnt == 0) return;
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
        File file = new File(urlNow);
        try {
            img = ImageIO.read(file);
            img1 = ImageIO.read(file);
            panel1.repaint();
            panel.repaint();
        } catch (IOException e) {
            if (times > 1)
                times--;
            JOptionPane.showMessageDialog(null, "已经是最后一张图片了");
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

        long date = currentTime.getYear() * 10000000000L + currentTime.getDayOfMonth() * 1000000 + currentTime.getMonthValue() * 100000000L + currentTime.getHour() * 10000 + currentTime.getMinute() * 100 + currentTime.getSecond();
        try {
            //ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/save/" + (date % 1000000) + ".jpg"));
            ImageIO.write(img, "PNG", new FileOutputStream(System.getProperty("user.dir") + "/save/" + date  + ".png"));
            JOptionPane.showMessageDialog(null, "已经保存至save文件夹");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void lastPicture() {
        if (times <= 1) {
            JOptionPane.showMessageDialog(null, "这已经是第一张图片");
            return;
        }
        times--;
        urlNow = url + times + ").jpg";
        initImg();
        nowNumText.setText(String.valueOf(times));
        operationCnt = 0;
    }

    void nextPicture() {
        if (operationCnt > 0) {
            File file = new File(url.substring(0, url.lastIndexOf("\\")) + "/autoSave");
            if (!file.exists()) {//如果文件夹不存在
                file.mkdir();//创建文件夹
            }
            try {
                //ImageIO.write(img, "JPEG", new FileOutputStream(url.substring(0, url.lastIndexOf("\\")) + "/autoSave/(" + times + ").jpg"));
                ImageIO.write(img, "PNG", new FileOutputStream(url.substring(0, url.lastIndexOf("\\")) + "/autoSave/(" + times + ").png"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        times++;
        urlNow = url + times + ").jpg";
        initImg();
        nowNumText.setText(String.valueOf(times));
        operationCnt = 0;

    }


    /**
     * 处理图片
     */


    public static void main(String[] args) {
        LocalDateTime currentTime = LocalDateTime.now();
        time = currentTime.getYear() * 10000000000L + currentTime.getDayOfMonth() * 1000000 + currentTime.getMonthValue() * 100000000L + currentTime.getHour() * 10000 + currentTime.getMinute() * 100 + currentTime.getSecond();


        File file = new File(System.getProperty("user.dir") + "/output");
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
        file = new File(System.getProperty("user.dir") + "/save");
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
        file = new File(System.getProperty("user.dir") + "/save/data");
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
        new color2();
    }


    class Runner1 implements Runnable {

        int cnt = 0;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (selectedColor) {
                    colorSelected(imgX, imgY);
                    selectedColor = false;
                }
                if (changeColor) {
                    copyPic();
                    processImgChangeColor(imgX, imgY, false);
                    changeColor = false;
                }
                if (changeColor2) {
                    copyPic();
                    processImgChangeColor(imgX, imgY, true);
                    changeColor2 = false;
                }
                if (process) {
                    if (!pro)
                        processImg();
                    else {
                        for (; times < 10000; times++) {
                            nowNumText.setText(String.valueOf(times));
                            urlNow = url + times + ").jpg";
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

        //第二页处理2  选颜色功能
        private void colorSelected(int x, int y) {
            Color color = new Color(img.getRGB(x, y));
            int rMax = Math.min(255, color.getRed() + 5);
            int rMin = Math.max(0, color.getRed() - 5);
            int gMax = Math.min(255, color.getGreen() + 5);
            int gMin = Math.max(0, color.getGreen() - 5);
            int bMax = Math.min(255, color.getBlue() + 5);
            int bMin = Math.max(0, color.getBlue() - 5);
            r1.setText(String.valueOf(Math.min(Integer.parseInt(r1.getText()), rMin)));
            r2.setText(String.valueOf(Math.max(Integer.parseInt(r2.getText()), rMax)));
            g1.setText(String.valueOf(Math.min(Integer.parseInt(g1.getText()), gMin)));
            g2.setText(String.valueOf(Math.max(Integer.parseInt(g2.getText()), gMax)));
            b1.setText(String.valueOf(Math.min(Integer.parseInt(b1.getText()), bMin)));
            b2.setText(String.valueOf(Math.max(Integer.parseInt(b2.getText()), bMax)));
            rg1.setText(String.valueOf(Math.min(Integer.parseInt(rg1.getText()), rMin - gMax)));
            rg2.setText(String.valueOf(Math.max(Integer.parseInt(rg2.getText()), rMax - gMin)));
            gb1.setText(String.valueOf(Math.min(Integer.parseInt(gb1.getText()), gMin - bMax)));
            gb2.setText(String.valueOf(Math.max(Integer.parseInt(gb2.getText()), gMax - bMin)));
        }

        //第二页处理1  改变颜色
        private void processImgChangeColor(int x, int y, boolean Mode) {


            if (!Mode) {
                Queue<Integer> q1 = new LinkedList<>();
                q1.add(x * 10000 + y);
                pro3Color(q1, colorChooser.getColor(), new Color(img.getRGB(x, y)), tabbedPane2ndDown.getSelectedIndex() != 0, false);
            } else {
                Queue<Integer> q1 = new LinkedList<>();
                for (int i = -4; i < 4; i++) {
                    for (int j = -4; j < 4; j++) {
                        q1.add(Math.max(0, (x + i)) * 10000 + Math.max(0, y + j));
                    }
                }

                pro3Color(q1, colorChooser.getColor(), new Color(img.getRGB(x, y)), tabbedPane2ndDown.getSelectedIndex() != 0, true);

            }

            panel.repaint();
        }

        //第一页处理   基础
        private void processImg() {
            if (tabbedPane.getSelectedIndex() == 1 && tabbedPane2ColorMod.getSelectedIndex() == 1) {
                int r = (Integer.parseInt(r1.getText()) + Integer.parseInt(r2.getText())) / 2;
                int g = (Integer.parseInt(g1.getText()) + Integer.parseInt(g2.getText())) / 2;
                int b = (Integer.parseInt(b1.getText()) + Integer.parseInt(b2.getText())) / 2;
                proColorWithColorRange(colorChooser.getColor(), new Color(r, g, b), tabbedPane2ndDown.getSelectedIndex() != 0);
                return;
            }
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


                    if (black)
                        if (Math.abs(red) + Math.abs(green) + Math.abs(blue) > absBlack) data[x][y][0] = 0;//black
                    if (black)
                        if (Math.abs(red) + Math.abs(green) + Math.abs(blue) > 2.5 * absBlack) data[x][y][3] = 1;//gray


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
            if (jColorful.getSelectedIndex() != 2)
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

                            if (cntColor >= notProcessColorIfNumLessThan) {
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
            if (removeBlackLast) if (jColorful.getSelectedIndex() != 2)
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

            File file = new File(System.getProperty("user.dir") + "/output/" + time);
            if (!file.exists()) {//如果文件夹不存在
                file.mkdir();//创建文件夹
            }
            if (!pro) {
                try {
                    ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/" + time + "/" + (int) (Math.random() * 10000) + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ImageIO.write(img, "JPEG", new FileOutputStream(System.getProperty("user.dir") + "/output/" + time + "/pic" + times + ".jpg"));
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
                    int max = deltaColor;
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
                        if (jParallaxesChooser.getSelectedIndex() == 4)
                            img.setRGB(x, y, color4.getRGB() & 0x00ffffff);
                        else img.setRGB(x, y, color4.getRGB() );
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

        Color getTargetColor(Color colorMap, Color targetColor, Color standardColor, Color colorNow, boolean changeColorWithMap) {//为了得到目标的颜色
            int r = Math.max(0, colorNow.getRed() - standardColor.getRed() + targetColor.getRed());
            int g = Math.max(0, colorNow.getGreen() - standardColor.getGreen() + targetColor.getGreen());
            int b = Math.max(0, colorNow.getBlue() - standardColor.getBlue() + targetColor.getBlue());
            Color color4 = new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));//替换的颜色

            if (changeColorWithMap) {//如果是用贴图换
                if (imgMap != null) {
                    //Color colorMap = new Color(imgMap.getRGB(x % imgMap.getWidth(), y % imgMap.getHeight()));//贴图颜色
                    if (jParallaxesChooser.getSelectedIndex() == 0) {
                        color4 = colorMap;
                    }
                    if (jParallaxesChooser.getSelectedIndex() == 1) {
                        color4 = new Color((colorMap.getRed() + colorNow.getRed()) / 2, (colorMap.getGreen() + colorNow.getGreen()) / 2, (colorMap.getBlue() + colorNow.getBlue()) / 2, colorMap.getAlpha());
                    }
                    if (jParallaxesChooser.getSelectedIndex() == 2) {
                        r = (int) Math.max(0, (colorMap.getRed() * colorNow.getRed() / 255.0));
                        g = (int) Math.max(0, (colorMap.getGreen() * colorNow.getGreen() / 255.0));
                        b = (int) Math.max(0, (colorMap.getBlue() * colorNow.getBlue() / 255.0));
                        color4 = new Color(r, g, b);
                    }
                    if (jParallaxesChooser.getSelectedIndex() == 3) {
                        r = 255 - (int) ((255 - colorMap.getRed()) * (255 - colorNow.getRed()) / 255.0);
                        g = 255 - (int) ((255 - colorMap.getGreen()) * (255 - colorNow.getGreen()) / 255.0);
                        b = 255 - (int) ((255 - colorMap.getBlue()) * (255 - colorNow.getBlue()) / 255.0);
                        color4 = new Color(r, g, b);
                    }
                    if (jParallaxesChooser.getSelectedIndex() == 4) {
                        int a=255-colorMap.getRed();
                        color4 = new Color(colorNow.getRed()/2, colorNow.getGreen()/2, colorNow.getBlue()/2,a);
                    }
                } else {
                    //JOptionPane.showMessageDialog(null, "贴图丢失[请先选择贴图]");
                    color4 = new Color(0, 0, 0);
                }
            }
            return color4;
        }//为了得到目标的颜色

        void pro3Color(Queue queue, Color targetColor, Color standardColor, boolean changeColorWithMap, boolean mode) {//对块颜色重绘的处理

            int data[][][] = new int[img.getWidth()][img.getHeight()][2];
            Color colorData[][] = new Color[img.getWidth()][img.getHeight()];
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    colorData[i][j] = new Color(img.getRGB(i, j));
                }
            }
            Queue queueEdge = new LinkedList();
            while (queue.size() > 0) {

                int max = deltaColoring;
                int max2 = deltaColorHue;


                int a = (int) queue.poll();
                int x = a / 10000;
                int y = a % 10000;
                if (x >= img.getWidth() || y >= img.getHeight()) continue;
                if (data[x][y][0] == 1) continue;
                Color colorNow = colorData[x][y];

                if (jLimitColorRange.getSelectedIndex() == 1) {
                    boolean continueBool = true;
                    if (colorNow.getRed() <= Integer.parseInt(r2.getText()) && colorNow.getRed() >= Integer.parseInt(r1.getText()))
                        if (colorNow.getGreen() <= Integer.parseInt(g2.getText()) && colorNow.getGreen() >= Integer.parseInt(g1.getText()))
                            if (colorNow.getBlue() <= Integer.parseInt(b2.getText()) && colorNow.getBlue() >= Integer.parseInt(b1.getText()))
                                if (colorNow.getRed() - colorNow.getGreen() <= Integer.parseInt(rg2.getText()) && colorNow.getRed() - colorNow.getGreen() >= Integer.parseInt(rg1.getText()))
                                    if (colorNow.getGreen() - colorNow.getBlue() <= Integer.parseInt(gb2.getText()) && colorNow.getGreen() - colorNow.getBlue() >= Integer.parseInt(gb1.getText())) {
                                        continueBool = false;
                                    }
                    if (continueBool) continue;
                }


                Color colorMap = new Color(0, 0, 0);
                if (imgMap != null)
                    if (jParallaxesPositionChooser.getSelectedIndex() == 1)
                        colorMap = new Color(imgMap.getRGB(((x - imgX) + imgMap.getWidth() * 1001 / 2) % imgMap.getWidth(), ((y - imgY) + imgMap.getHeight() * 1001 / 2) % imgMap.getHeight()));//贴图颜色
                    else
                        colorMap = new Color(imgMap.getRGB(x % imgMap.getWidth(), y  % imgMap.getHeight()));//贴图颜色

                Color color4 = getTargetColor(colorMap, targetColor, standardColor, colorNow, changeColorWithMap);


                Color color = new Color(img.getRGB(x, y));
                Color color1;
                if (jParallaxesChooser.getSelectedIndex() == 4)
                    img.setRGB(x, y,  0x00ffffff);
                else img.setRGB(x, y, color4.getRGB() );
                data[x][y][0] = 1;
                if (!mode) {
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
                        int ad = (x + 1) * 10000 + y;
                        queueEdge.add(ad);
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
                        int ad = (x) * 10000 + y + 1;
                        queueEdge.add(ad);
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
                        int ad = (x - 1) * 10000 + y;
                        queueEdge.add(ad);
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
                        int ad = (x) * 10000 + y - 1;
                        queueEdge.add(ad);
                    }
                }

                cnt++;
                if (cnt > 8000) {
                    update();
                }

            }

            while (queueEdge.size() > 0) {
                int a = (int) queueEdge.poll();
                int x = a / 10000;
                int y = a % 10000;
                if (data[x][y][0] == 1) continue;
                Color colorNow = colorData[x][y];
                Color colorMap = new Color(0, 0, 0);
                if (imgMap != null)
                    if (jParallaxesPositionChooser.getSelectedIndex() == 1)
                        colorMap = new Color(imgMap.getRGB(((x - imgX) + imgMap.getWidth() * 1001 / 2) % imgMap.getWidth(), ((y - imgY) + imgMap.getHeight() * 1001 / 2) % imgMap.getHeight()));//贴图颜色
                    else
                        colorMap = new Color(imgMap.getRGB(x % imgMap.getWidth(), y  % imgMap.getHeight()));//贴图颜色

                Color color4 = getTargetColor(colorMap, targetColor, standardColor, colorNow, changeColorWithMap);

                if (jParallaxesChooser.getSelectedIndex() == 4)
                    img.setRGB(x, y, color4.getRGB() & 0x00ffffff);
                else img.setRGB(x, y, color4.getRGB() );

                data[x][y][0] = 1;
            }


        }//对块颜色重绘的处理

        void proColorWithColorRange(Color targetColor, Color standardColor, boolean changeColorWithMap) {//对块颜色重绘的处理

            int data[][][] = new int[img.getWidth()][img.getHeight()][2];
            Color colorData[][] = new Color[img.getWidth()][img.getHeight()];
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    colorData[i][j] = new Color(img.getRGB(i, j));
                }
            }
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    if (data[x][y][0] == 1) continue;

                    Color colorNow = colorData[x][y];


                    if (colorNow.getRed() <= Integer.parseInt(r2.getText()) && colorNow.getRed() >= Integer.parseInt(r1.getText()))
                        if (colorNow.getGreen() <= Integer.parseInt(g2.getText()) && colorNow.getGreen() >= Integer.parseInt(g1.getText()))
                            if (colorNow.getBlue() <= Integer.parseInt(b2.getText()) && colorNow.getBlue() >= Integer.parseInt(b1.getText()))
                                if (colorNow.getRed() - colorNow.getGreen() <= Integer.parseInt(rg2.getText()) && colorNow.getRed() - colorNow.getGreen() >= Integer.parseInt(rg1.getText()))
                                    if (colorNow.getGreen() - colorNow.getBlue() <= Integer.parseInt(gb2.getText()) && colorNow.getGreen() - colorNow.getBlue() >= Integer.parseInt(gb1.getText())) {

                                        Color colorMap = new Color(0, 0, 0);
                                        if (imgMap != null)
                                            if (jParallaxesPositionChooser.getSelectedIndex() == 1)
                                                colorMap = new Color(imgMap.getRGB(((x - imgX) + imgMap.getWidth() * 1001 / 2) % imgMap.getWidth(), ((y - imgY) + imgMap.getHeight() * 1001 / 2) % imgMap.getHeight()));//贴图颜色
                                            else
                                                colorMap = new Color(imgMap.getRGB(x % imgMap.getWidth(), y  % imgMap.getHeight()));//贴图颜色
                                        Color color4 = getTargetColor(colorMap, targetColor, standardColor, colorNow, changeColorWithMap);
                                        if (jParallaxesChooser.getSelectedIndex() == 4)
                                            img.setRGB(x, y, color4.getRGB() & 0x00ffffff);
                                        else img.setRGB(x, y, color4.getRGB() );
                                    }
                }
            }
            panel.repaint();


        }//对选定颜色重绘的处理

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



