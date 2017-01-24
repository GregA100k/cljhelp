chrome.extension.onMessage.addListener(function(request, sender, sendResponse) {
  if (request.method == "getSelection"){
	  //alert("in getSelection true");
    sendResponse({data: window.getSelection().toString()});
  } else {
	  //alert("in getSelection else");
    sendResponse({data: "Snub"}); // snub them.
  }
});
