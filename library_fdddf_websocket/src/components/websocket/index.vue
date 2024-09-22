<template>
<div :class="$style.root">
  <h3>WebSocket with STOMP</h3>
  <div>
    <button @click="connect" :disabled="isConnected">Connect</button>
    <button @click="disconnect" :disabled="!isConnected">Disconnect</button>
  </div>
  <div v-if="isConnected">
    <h4>Send a Message:</h4>
    <input v-model="message" placeholder="Type your message"/>
    <button @click="sendMessage">Send</button>
  </div>
  <div v-if="receivedMessages.length">
    <h4>Received Messages:</h4>
    <ul>
      <li v-for="(msg, index) in receivedMessages" :key="index">{{ msg }}</li>
    </ul>
  </div>
</div>
</template>
<script>
import { Client } from '@stomp/stompjs';
import { WebSocket } from 'ws';

export default {
  name: 'websocket',
  props: {
    url: {
      type: String,
      required: true,
    }
  },
  data() {
    return {
      stompClient: null,
      isConnected: false,
      message: '',
      receivedMessages: []
    };
  },
  emit : ['connected', 'disconnected', 'error', 'messageReceived', 'messageSent'],
  created () {
    Object.assign(global, { WebSocket });
  },
  methods: {
    connect() {
      console.log(this.url);
      this.stompClient = new Client({
        brokerURL: this.url,
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        onConnect: (iframeConnected) => {
          console.log('Connected to WebSocket');
          this.stompClient.subscribe('/topic/common', message => {
            if (message.body) {
              console.log(`Received: ${message.body}`)
              const parsedMessage = JSON.parse(message.body).content;
              this.receivedMessages.push(parsedMessage);
              this.$emit('messageReceived', parsedMessage);  // Emitting 'messageReceived' event
            }
          });
          this.isConnected = true;
          this.$emit('connected');  // Emitting 'connected' event            
        },
        onDisconnect: () => {
          console.log('Disconnected from WebSocket');
          this.isConnected = false;
          this.$emit('disconnected');  // Emitting 'disconnected' event
        },
        onStompError: (iframeError) => {
          console.log('Error on WebSocket connection', iframeError.body);
          this.isConnected = false;
          this.$emit('stompError', iframeError.body);  // Emitting 'error' event
        },
        onWebSocketError: (event) => {
          console.log('Error on WebSocket connection', event);
          this.isConnected = false;
          this.$emit('webScoketError', event);  // Emitting 'error' event
        }
      });
      this.stompClient.activate()
    },
    sendMessage() {
      if (this.message.trim()) {
        const body = JSON.stringify({ content: this.message });
        this.stompClient.publish({destination: "/app/sendMessage", body: body});
        this.$emit('messageSent', this.message);  // Emitting 'messageSent' event
        this.message = '';
      }
    },
    send(message) {
      const body = JSON.stringify({ content: message });
      this.stompClient.publish({destination: "/app/sendMessage", body: body});
    },
    disconnect() {
      if (this.stompClient !== null) {
        this.stompClient.deactivate();
      }
    }
  },
  beforeDestroy() {
    if (this.isConnected) {
      this.disconnect();
    }
  }
};
</script>
<style module>
.root {
  /* display: flex; */
  /* align-items: center; */
  /* justify-content: center; */
  font-size: 24px;
  width: 600px;
  height: 400px;
  color: #fff;
  font-size: 24px;
  border-radius: 12px;
}
</style>
