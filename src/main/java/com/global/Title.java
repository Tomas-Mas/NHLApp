package com.global;

import com.htmlOutput.Tag;

public class Title {
	
	private Tag titleDiv = null;

	public Title() {
		titleDiv = new Tag("div", "id='title'",
				new Tag("p", "class='headline'", "Welcome to the jungle!"));
	}
	
	public Tag getTitleDiv() {
		return this.titleDiv;
	}
}