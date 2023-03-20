package test.number;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import otc.result.Result;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;

public class py  extends JFrame {

    public static void main(String[] args) {
        // 创建窗口
        JFrame frame = new JFrame("收银台");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建页面布局
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        // 添加收银台信息
        panel.add(new JLabel("商品名称:"));
        panel.add(new JTextField());
        panel.add(new JLabel("单价:"));
        panel.add(new JTextField());
        panel.add(new JLabel("数量:"));
        panel.add(new JTextField());
        panel.add(new JLabel("总价:"));
        panel.add(new JTextField());

        // 添加结账按钮
        panel.add(new JButton("结账"));

        // 将页面布局添加到窗口
        frame.add(panel);

        // 显示窗口
        frame.setVisible(true);




    }
    }
