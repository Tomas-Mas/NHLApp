package com.htmlOutput;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Tag {

	private String name;
	private String attributes;
	private String text;
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	
	public Tag() {
		this.name = "";
	}
	
	public Tag(String name) {
		this.name = name;
	}
	
	public Tag(String name, Tag[] tags) {
		this.name = name;
		for(Tag t : tags) {
			this.tags.add(t);
		}
	}
	
	public Tag(String name, Tag tag) {
		this.name = name;
		this.tags.add(tag);
	}
	
	public Tag(String name, String attributes, Tag[] tags) {
		this.name = name;
		this.attributes = attributes;
		for(Tag t : tags) {
			this.tags.add(t);
		}
	}
	
	public Tag(String name, String attributes, Tag tag) {
		this.name = name;
		this.attributes = attributes;
		this.tags.add(tag);
	}
	
	public Tag(String name, String attributes) {
		this.name = name;
		this.attributes = attributes;
	}
	
	public Tag(String name, String attributes, String text) {
		this.name = name;
		this.attributes = attributes;
		this.text = text;
	}
	
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	
	public void print(PrintWriter out) {
		if(name.isEmpty()) {
			return;
		}
		
		out.print("<" + name);
		if(attributes != null && !attributes.isEmpty()) {
			out.print(" " + attributes);
		}
		out.println(">");
		
		if(!tags.isEmpty()) {
			for(Tag tag : tags) {
				tag.print(out);
			}
		}
		
		if(text != null)
			out.println(text);
		
		if(!name.equals("img") && !name.equals("link")) {
			out.println("</" + name + ">");
		}
	}
}
