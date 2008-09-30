package async.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;


import org.apache.log4j.BasicConfigurator;

import async.Command;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.jms.JMSMockObjectFactory;

class AbstractTest {
    
    static {
        BasicConfigurator.configure();
    }
    
    static final String TEST_QUEUE_NAME = "queue/Test";

    /**
     * Sometimes we just need any old command for testing.  A really simple one
     * will do nicely.
     */
    Command<MyCommandResponse> anyOldCommand = new TestCommand();    
    
    Destination commandQueue;
    
    ConnectionFactory connectionFactory;
    
    JMSTestModule jmsTestModule;
    

    AbstractTest() {
        JMSMockObjectFactory jmsFactory = new JMSMockObjectFactory();
        this.jmsTestModule = new JMSTestModule(jmsFactory);
        DestinationManager destinationManager = 
            jmsTestModule.getDestinationManager();
        this.commandQueue = destinationManager.createQueue(TEST_QUEUE_NAME);
        this.connectionFactory = 
            jmsFactory.getMockQueueConnectionFactory();
    }
}
