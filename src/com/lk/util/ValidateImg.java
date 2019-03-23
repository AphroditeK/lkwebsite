package com.lk.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

public class ValidateImg {
	/**
	 * @return map(key=code,value=code)map(key=image,value=imageInputStream)
	 */
	public Map<String, Object> createImg(){
		//定义图片流
		BufferedImage image = 
				new BufferedImage(120, 30, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		//设置背景颜色
		setBackGround(g);
		//设置边框
		setBorder(g);
		//画随机干扰线
		drawRandomLine(g);
		//画随机字符
		String code = drawRandomString(g);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("image", new ByteArrayInputStream(os.toByteArray()));
		return map;
	}
	
	private void setBackGround(Graphics2D g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 120, 30);
	}
	
	private void setBorder(Graphics2D g){
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(1, 1, 118, 28);
	}
	
	private void drawRandomLine(Graphics2D g){
		for(int i=0;i<10;i++){
			int x1 = new Random().nextInt(120);
			int y1 = new Random().nextInt(30);
			int x2 = new Random().nextInt(120);
			int y2 = new Random().nextInt(30);
			g.setColor(Color.GREEN);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	private String drawRandomString(Graphics2D g){
		//g.setColor(Color.RED);
		Random r = new Random();
		g.setFont(new Font("微软雅黑", Font.BOLD, 20));
		
		//65-90 A-Z
		String code = "";
		for(int i=0;i<4;i++){
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			char c = (char) (r.nextInt(26)+65);
			int degree = new Random().nextInt(5);
			g.rotate(degree*Math.PI/180);
			g.drawString(c+"",i*30+10,23);
			g.rotate(-degree*Math.PI/180);
			code += c;
		}
		return code;
	}
	
}
