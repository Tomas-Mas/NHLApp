var expandedElement = null;

document.onreadystatechange = function() {
	if(document.readyState === 'complete') {
		setClickableEvents();
		setMouseHoverEvents();
	}
}

function setClickableEvents() {
	document.getElementById('mainMenu').onclick = menuClicked;
	
	document.getElementById('playoffContainer').onclick = playoffClicked;
}

function playoffClicked(event) {
	var clickedDiv = event.target.closest('div');
	if(clickedDiv.className === 'match-header') {
		var results = clickedDiv.nextElementSibling;
		if(results.style.display === 'none') {
			unexpandBrackets();
			results.style.display = 'block';
			clickedDiv.parentElement.classList.add('expanded');
			expandedElement = clickedDiv;
		} else {
			results.style.display = 'none';
			clickedDiv.parentElement.classList.remove('expanded');
		}
	} else if(clickedDiv.className === 'match-results') {
		alert(event.target.closest('tr').id);
		//todo go to match page
	} else {
		unexpandBrackets();
	}
}

function unexpandBrackets() {
	if(expandedElement != null) {
		expandedElement.parentElement.classList.remove('expanded');
		expandedElement.nextElementSibling.style.display = 'none';
		expandedElement = null;
	}
}

function setMouseHoverEvents() {
	var els = document.getElementsByClassName('highlightable');
	for(var i = 0; i < els.length; i++) {
		els[i].addEventListener("mouseover", mouseOver);
		els[i].addEventListener("mouseout", mouseOut);
	}
	/*els[0].addEventListener("mouseover", mouseOver);
	els[0].addEventListener("mouseout", mouseOut);*/
}

function mouseOver(event) {
	var row = event.target.closest('tr').className;
	var teamId = row.substring(0, 7);
	var teamElements = document.getElementsByClassName(teamId);
	for(var i = 0; i < teamElements.length; i++) {
		teamElements[i].classList.add("ghost-highlight");
	}
}

function mouseOut(event) {
	var row = event.target.closest('tr').className;
	var teamId = row.substring(0, 7);
	var teamElements = document.getElementsByClassName(teamId);
	for(var i = 0; i < teamElements.length; i++) {
		teamElements[i].classList.remove("ghost-highlight");
	}
}