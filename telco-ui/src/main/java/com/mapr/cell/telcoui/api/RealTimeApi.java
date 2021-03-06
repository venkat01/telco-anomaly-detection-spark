package com.mapr.cell.telcoui.api;



import com.fasterxml.jackson.databind.JsonNode;
import com.mapr.cell.telcoui.LiveConsumer;
import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RealTimeApi extends EventSourceServlet {

    private LiveConsumer consumer = new LiveConsumer();

    @Override
    protected EventSource newEventSource(final HttpServletRequest req) {
        return new DataSource(consumer);
    }

    protected static class DataSource implements EventSource, LiveConsumer.Listener {

        private LiveConsumer poller;
        private ConcurrentLinkedQueue<JsonNode> initQueue = new ConcurrentLinkedQueue<>();
        private ConcurrentLinkedQueue<JsonNode> moveQueue = new ConcurrentLinkedQueue<>();
        private ConcurrentLinkedQueue<JsonNode> statusQueue = new ConcurrentLinkedQueue<>();
        private ConcurrentLinkedQueue<JsonNode> towerQueue = new ConcurrentLinkedQueue<>();
        private ConcurrentLinkedQueue<JsonNode> eventQueue = new ConcurrentLinkedQueue<>();

        public DataSource(LiveConsumer poller) {
            this.poller = poller;
        }

        @Override
        public void onOpen(final EventSource.Emitter emitter) throws IOException {

            poller.subscribe(this);
            emitter.event("test", "Event source opened");
            System.out.println("opened");
            while (true) {
                emitInit(emitter);
                emitMove(emitter);
                emitStatus(emitter);
                emitTower(emitter);
                emitEvent(emitter);
            }
        }

        private void emitInit(Emitter emitter) throws IOException {
            JsonNode value;
            do {
                value = initQueue.poll();
                if (value != null) {
                    emitter.event("init", value.toString());
                }
            } while (value != null);
        }

        private void emitMove(Emitter emitter) throws IOException {
            JsonNode value;
            do {
                value = moveQueue.poll();
                if (value != null) {
                    emitter.event("move", value.toString());
                }
            } while (value != null);
        }

        private void emitStatus(Emitter emitter) throws IOException {
            JsonNode value;
            do {
                value = statusQueue.poll();
                if (value != null) {
                    emitter.event("status", value.toString());
                }
            } while (value != null);
        }

        private void emitTower(Emitter emitter) throws IOException {
            JsonNode value;
            do {
                value = towerQueue.poll();
                if (value != null) {
                    emitter.event("cdr", value.toString());
                }
            } while (value != null);
        }
        private void emitEvent(Emitter emitter) throws IOException {
            JsonNode value;
            do {
                value = eventQueue.poll();
                if (value != null) {
                    emitter.event("event", value.toString());
                }
            } while (value != null);
        }

        @Override
        public void onClose() {
            poller.unsubscribe(this);
        }

        @Override
        public void onNewInitData(JsonNode data) {
            initQueue.add(data);
        }

        @Override
        public void onNewMoveData(JsonNode data) {
            moveQueue.add(data);
        }

        @Override
        public void onNewStatusData(JsonNode data) {
            statusQueue.add(data);
        }

        @Override
        public void onNewEventData(JsonNode data) {
            eventQueue.add(data);
        }

        @Override
        public void onNewTowerData(JsonNode data) {
            towerQueue.add(data);
        }
    }
}
