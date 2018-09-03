package com.cloudSeckill.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ValidateImageUtils {
    //设置字母的大小,大小
    private Font mFont = new Font("Times New Roman", Font.PLAIN, 17);

    private String validateCode = "";

    //生成随机颜色
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    //生成随即字符
    private String getRandChar() {
        int rand = (int) Math.round(Math.random() * 2);
        long itmp = 0;
        char ctmp = '\u0000';
        //根据rand的值来决定是生成一个大写字母、小写字母还是数字
        switch (rand) {
            //生成大写字母
            case 1:
                itmp = Math.round(Math.random() * 25 + 65);
                ctmp = (char) itmp;
                return String.valueOf(ctmp);
            //生成小写字母
            case 2:
                itmp = Math.round(Math.random() * 25 + 97);
                ctmp = (char) itmp;
                return String.valueOf(ctmp);
            //生成数字
            default:
                itmp = Math.round(Math.random() * 9);
                ctmp = (char) itmp;
                return String.valueOf(itmp);
        }
    }

    public BufferedImage getValidateImage() {
        //指定验证码图片的大小
        int width = 80, height = 18;
        //生成一张新图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //在图片中绘制内容
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200, 250));
        g.fillRect(1, 1, width - 1, height - 1);
        g.setColor(new Color(102, 102, 102));
        g.drawRect(0, 0, width - 1, height - 1);
        g.setFont(mFont);
        //随即生成线条，让图片看起来更加杂乱
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g.drawLine(x, y, x + xl, y + yl);
        }
        //从另一方向画随机线
        for (int i = 0; i < 70; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int xl = random.nextInt(12) + 1;
            int yl = random.nextInt(6) + 1;
            g.drawLine(x, y, x - xl, y - yl);
        }
        //生成随机数,并将随机数字转换为字母
        String sRand = "";
        for (int i = 0; i < 4; i++) {
            //取得一个随即字符
            String tmp = getRandChar();
            sRand += tmp;
            //将系统生成的随即字符添加到图形验证码图片上
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(String.valueOf(tmp), 15 * i + 10, 16);
        }
        /*//取得用户session
        HttpSession session = request.getSession(true);
        //将验证码保存在session中
        session.setAttribute("ValidateCode",sRand);*/

        validateCode = sRand;

        g.dispose();

        return image;
    }

    public String getValidateCode() {
        return validateCode;
    }
}
