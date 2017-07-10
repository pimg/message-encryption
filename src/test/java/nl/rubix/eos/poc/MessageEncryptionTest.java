package nl.rubix.eos.poc;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(CamelCdiRunner.class)
public class MessageEncryptionTest {

  @Inject CamelContext context;

  @Produce(uri = "direct:start")
  private ProducerTemplate start;

  @Inject
  @Uri("mock:decrypted")
  private MockEndpoint mockDecrypted;

  @Test
  public void messageEncryptionTest() throws Exception {
    Exchange exchange = new DefaultExchange(context);

    String message = "This message should be encrypted and decrypted";

    exchange.getIn().setBody(message);

    start.send(exchange);

    mockDecrypted.setExpectedMessageCount(1);

    mockDecrypted.assertIsSatisfied();

    assertEquals("decrypted message should match input", message, mockDecrypted.getExchanges().get(0).getIn().getBody(String.class));

  }
}
