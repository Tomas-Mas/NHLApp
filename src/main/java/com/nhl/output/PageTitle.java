package com.nhl.output;

public final class PageTitle {
	
	public static Tag getTitleDiv() {
		return new Tag("div", "id='title'",
				new Tag("p", "class='headline'", "Welcome to the jungle!"));
	}
}