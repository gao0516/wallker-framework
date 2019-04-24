package com.wallker.framework.tools.image;

import java.io.Serializable;

public class MarkImageTextBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String text;
	private Integer x;
	private Integer y;
	private String fontFamily;
	private Integer fontSize;
	
	public MarkImageTextBean(){}
	
	public MarkImageTextBean(String text, Integer x, Integer y) {
		super();
		this.text = text;
		this.x = x;
		this.y = y;
		this.fontFamily = "宋体";
		this.fontSize = 20;
	}
	
	public MarkImageTextBean(String text, Integer x, Integer y,
			String fontFamily, Integer fontSize) {
		super();
		this.text = text;
		this.x = x;
		this.y = y;
		this.fontFamily = fontFamily;
		this.fontSize = fontSize;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	public Integer getFontSize() {
		return fontSize;
	}
	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}
	@Override
	public String toString() {
		return String
				.format("MarkImageTextBean [text=%s, x=%s, y=%s, fontFamily=%s, fontSize=%s]",
						text, x, y, fontFamily, fontSize);
	}
}
