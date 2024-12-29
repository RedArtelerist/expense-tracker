import SockJS from 'sockjs-client';
import Stomp from "webstomp-client";
import store from "@/store";

class WebSocketService {
    constructor() {
        this.stompClient = null;
    }

    connect(onConnected, onError) {
        const token = store.state.token;

        // Replace '/ws' with your WebSocket endpoint
        const socket = new SockJS(`http://localhost:8072/ws`);
        this.stompClient = Stomp.over(socket);

        // Configure connection headers
        const headers = {
            Authorization: `Bearer ${token}`
        };

        this.stompClient.connect(headers, frame => {
            console.log('Connected: ' + frame);
            onConnected();
        }, error => {
            console.log('Error: ' + error);
            if (onError) onError(error);
        });
    }

    subscribe(destination, callback) {
        if (this.stompClient) {
            this.stompClient.subscribe(destination, message => {
                callback(JSON.parse(message.body));
            });
        }
    }

    disconnect() {
        if (this.stompClient) {
            this.stompClient.disconnect(() => {
                console.log('Disconnected');
            });
        }
    }

    send(destination, payload) {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.send(destination, {}, JSON.stringify(payload));
        }
    }
}

export default new WebSocketService();