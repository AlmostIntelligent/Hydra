package org.gethydrated.hydra.test.core.message;

import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.api.message.Message;
import org.gethydrated.hydra.api.message.MessageHandler;
import org.gethydrated.hydra.api.message.MessageType;
import org.gethydrated.hydra.api.node.Node;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.message.MessageDispatcher;
import org.gethydrated.hydra.core.message.MessageQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MessageDispatcherTest {
    
    private MessageQueue mq;
    private MessageDispatcher md;
    
    @Before
    public void setup() {
        mq = new MessageQueue();
        md = new MessageDispatcher(mq);
        md.start();
    }
    
    @After
    public void teardown() {
        md.stop();
    }
    
    @Test
    public void testDispatcher() throws InterruptedException {
        USID id = new USID((long) 1, new TestNode());
        TestHandler th = new TestHandler();
        md.addHandler(id, th);
        mq.addMessage(new TestMessage(id));
        Thread.sleep(1000);
        assertTrue(th.count == 1);
    }

    public void testDispatcherConcurrent() throws InterruptedException {
        final USID id = new USID((long) 1, new TestNode());
        TestHandler th = new TestHandler();
        md.addHandler(id, th);
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    mq.addMessage(new TestMessage(id));
                }
            } }).start();
        Thread.sleep(1000);
        assertTrue(th.count == 1000);
    }
    
    public class TestHandler implements MessageHandler{

        private int count;

        @Override
        public void processMessage(Message m) {
            count++;
        }
        
    }
    
    public class TestMessage implements Message {

        private USID id;

        public TestMessage(USID id) {
            this.id = id;
        }
        
        @Override
        public MessageType getType() {
            return MessageType.MESSAGE;
        }

        @Override
        public USID getDestination() {
            return id;
        }
        
        @Override
        public USID getSource() {
            return null;
        }
        
    }
    
    public class TestNode implements Node {

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public int getIP() {
            return 0;
        }

        @Override
        public String getName() {
            return "test";
        }

        @Override
        public boolean isLocal() {
            return true;
        }
        
    }
}
