package com.global;

import com.htmlOutput.Tag;

public final class TeamIcon {

	public static Tag getIconDiv(int teamId, String teamName) {
		Tag div = new Tag("div", "class='teamPic'");
		div.addTag(new Tag("img", "src='sources/images/team-icons/" + teamId + ".png' title='" + teamName + "' alt='" + teamName + "'"));
		return div;
	}
	
	public static Tag getIconDiv(int teamId, String teamName, String divClass) {
		Tag div = new Tag("div", "class='" +  divClass + "'");
		div.addTag(new Tag("img", "src='sources/images/team-icons/" + teamId + ".png' title='" + teamName + "' alt='" + teamName + "'"));
		return div;
	}
	
	public static Tag getTBDIconDiv() {
		Tag div = new Tag("div", "class='teamPic'");
		div.addTag(new Tag("img", "src='sources/images/team-icons/tbd.png' title='TBD' alt='TBD'"));
		return div;
	}
}
