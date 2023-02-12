function menuClicked(event) {
	if(event.target.className === 'menulinks') {
		var button = event.target.textContent;
		if(button.includes('Home')) {
			window.location = 'index.jsp';
		}
		else if(button.includes('Statistics')) {
			window.location = 'statistics.jsp';
		}
	}
}