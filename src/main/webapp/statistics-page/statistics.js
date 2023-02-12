document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
	}
}

function setClickableEvents() {
	document.getElementById('mainMenu').onclick = menuClicked;
}