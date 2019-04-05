if (window.console) {
    console.log("Welcome to your Play application's JavaScript!");

    var webSocket = new WebSocket('ws://localhost:9000/websocket1');

    webSocket.onmessage = function (event) {
        var binary = event.data;
        var reader = new FileReader();

// This fires after the blob has been read/loaded.
        reader.addEventListener('loadend', function (e) {
            var text = e.srcElement.result;
            console.log(text);
        });

// Start reading the blob as text.
        reader.readAsText(binary);
    };
}
