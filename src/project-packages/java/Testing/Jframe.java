package Testing;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static Assists.ExtentManager.reportDate;

public class Jframe
{
    static JLabel label1 = new JLabel();
    static JLabel label2 = new JLabel();
    static JLabel label3 = new JLabel();
    static JLabel label4 = new JLabel();
    static JLabel label5 = new JLabel();
    static JLabel label6 = new JLabel();
    static JLabel label7 = new JLabel();
    static JLabel label8 = new JLabel();
    static JLabel label9 = new JLabel();
    static JLabel label10 = new JLabel();
    static JLabel label11 = new JLabel();
    static JLabel label12 = new JLabel();


    public static void main(String[] args)
    {
        String xmlPath = ".\\src\\project-packages\\java\\Testing\\SuperPharm.xml";
        String STDPath = "explorer .\\My SuperPharm Project - STD + STR Report\\STD\\Automation Project - Super-Pharm STD.xlsx";
        String STRPath = "explorer .\\My SuperPharm Project - STD + STR Report\\STR Report\\"+reportDate+"\\exReport.html";
        int btnX =80, btnY=20, btnW=180, btnH=20;
        int lblX=5, lblY=20, lblW = 70, lblH=20;

        JFrame frame = new JFrame("SuperPharm - Automation Proj");
        frame.setTitle("SuperPharm - Automation Proj");
        frame.setSize(300,500);
        frame.setLayout(null);
        frame.setVisible(true);

        JButton btn1 = new JButton("Register/Login");
        btn1.setBounds(btnX,30,btnW, btnH);
        frame.add(btn1);

        JButton btn2 = new JButton("Cart");
        btn2.setBounds(btnX,60,btnW, btnH);
        frame.add(btn2);

        JButton btn3 = new JButton("On-Sale");
        btn3.setBounds(btnX,90,btnW, btnH);
        frame.add(btn3);

        JButton btn4 = new JButton("Optic");
        btn4.setBounds(btnX,120,btnW, btnH);
        frame.add(btn4);

        JButton btn5 = new JButton("Coupons");
        btn5.setBounds(btnX,150,btnW, btnH);
        frame.add(btn5);

        JButton btn6 = new JButton("Links");
        btn6.setBounds(btnX,180,btnW, btnH);
        frame.add(btn6);

        JButton btn7 = new JButton("Item/Price Compare");
        btn7.setBounds(btnX,210,btnW, btnH);
        frame.add(btn7);

        JButton btn8 = new JButton("Logo");
        btn8.setBounds(btnX,240,btnW, btnH);
        frame.add(btn8);

        JButton btn9 = new JButton("Categories");
        btn9.setBounds(btnX,270,btnW, btnH);
        frame.add(btn9);

        JButton btn10 = new JButton("Favorite/No-Inventory");
        btn10.setBounds(btnX,300,btnW, btnH);
        frame.add(btn10);

        JButton btn11 = new JButton("Join LifeStyle");
        btn11.setBounds(btnX,330,btnW, btnH);
        frame.add(btn11);

        JButton btn12 = new JButton("Run-All Tests");
        btn12.setBounds(btnX,360,btnW, btnH);
        frame.add(btn12);

        JButton btn13 = new JButton("STD");
        btn13.setBounds(btnX,390,btnW, btnH);
        frame.add(btn13);

        JButton btn14 = new JButton("STR");
        btn14.setBounds(btnX,420,btnW, btnH);
        frame.add(btn14);

        label1.setBounds(lblX,30 , lblW, lblH);
        frame.add(label1);
        label1.setText("Tests 1-4: ");

        label2.setBounds(lblX,60 , lblW, lblH);
        frame.add(label2);
        label2.setText("Tests 5-9: ");

        label3.setBounds(lblX,90 , lblW, lblH);
        frame.add(label3);
        label3.setText("Tests 10-12: ");

        label4.setBounds(lblX,120 , lblW, lblH);
        frame.add(label4);
        label4.setText("Tests 13-14: ");

        label5.setBounds(lblX,150 , lblW, lblH);
        frame.add(label5);
        label5.setText("Test 15: ");

        label6.setBounds(lblX,180 , lblW, lblH);
        frame.add(label6);
        label6.setText("Tests 16-18: ");

        label7.setBounds(lblX,210 , lblW, lblH);
        frame.add(label7);
        label7.setText("Tests 19-20: ");

        label8.setBounds(lblX,240 , lblW, lblH);
        frame.add(label8);
        label8.setText("Tests 21-22: ");

        label9.setBounds(lblX,270 , lblW, lblH);
        frame.add(label9);
        label9.setText("Tests 23-25: ");

        label10.setBounds(lblX,300 , lblW, lblH);
        frame.add(label10);
        label10.setText("Tests 26-27: ");

        label11.setBounds(lblX,330 , lblW, lblH);
        frame.add(label11);
        label11.setText("Tests 28-30: ");

        label12.setBounds(lblX,360 , lblW, lblH);
        frame.add(label12);
        label12.setText("Tests 1-30: ");

        lisAction(btn1,TestNG_RegisterAndLogin.class);
        lisAction(btn2,TestNG_Cart.class);
        lisAction(btn3,TestNG_On_Sale.class);
        lisAction(btn4,TestNG_Optic.class);
        lisAction(btn5,TestNG_Coupons.class);
        lisAction(btn6,TestNG_Links.class);
        lisAction(btn7,TestNG_Items_and_Price_Compare.class);
        lisAction(btn8,TestNG_Logo.class);
        lisAction(btn9,TestNG_Categories.class);
        lisAction(btn10,TestNG_Favorite_and_No_inventory.class);
        lisAction(btn11,TestNG_Join_lifestyle.class);
        lisActionSuit(btn12,xmlPath);
        openSTDorSTR(btn13,STDPath);
        openSTDorSTR(btn14,STRPath);

    }
    public static void lisAction(JButton jb,Class Cla)
    {
        jb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                try
                {
                    TestListenerAdapter tla = new TestListenerAdapter();
                    TestNG testng = new TestNG();
                    testng.setTestClasses(new Class[] { Cla });
                    testng.addListener(tla);
                    testng.run();
                    System.out.println("test run ok");
                }
                catch (Exception e)
                {
                    System.out.println("test run fail");
                }
            }
        });
    }
    public static void lisActionSuit(JButton jb,String xmlPath)
    {
        jb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                try
                {
                    TestNG testng = new TestNG();
                    testng.setTestSuites(Arrays.asList(xmlPath));
                    testng.run();
                    System.out.println("test run ok");
                }
                catch (Exception e)
                {
                    System.out.println("test run fail");
                }
            }
        });
    }

    private static void openSTDorSTR(JButton jb,String path)
    {
        jb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                try
                {
                    Runtime.getRuntime().exec(path);
                }
                catch (Exception e)
                {
                    System.out.println("test run fail");
                }
            }
        });
    }
}

