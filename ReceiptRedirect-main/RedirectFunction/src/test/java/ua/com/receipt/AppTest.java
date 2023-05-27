package ua.com.receipt;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AppTest {

  @Test
  public void crashTest() {
    App app = new App();

    APIGatewayProxyResponseEvent result = app.handleRequest(null, null);
    assertEquals(400, result.getStatusCode().intValue());
  }

    @Test
    public void onlineDocument() {
      App app = new App();

      APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
      Map<String, String> parameters = new HashMap<String, String>();
      parameters.put("proxy", "4000066879/35485502");
      requestEvent.setPathParameters(parameters);

      APIGatewayProxyResponseEvent result = app.handleRequest(requestEvent, null);
      assertEquals(301, result.getStatusCode().intValue());
      assertNotNull(result.getHeaders().get("Location"));
      assertEquals("https://cabinet.tax.gov.ua/cashregs/check?id=35485502&fn=4000066879", result.getHeaders().get("Location"));
    }

  @Test
  public void offlineDocument() {
    App app = new App();

    APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("proxy", "4000066879/154139.2.8291");
    requestEvent.setPathParameters(parameters);

    APIGatewayProxyResponseEvent result = app.handleRequest(requestEvent, null);
    assertEquals(301, result.getStatusCode().intValue());
    assertNotNull(result.getHeaders().get("Location"));
    assertEquals("https://cabinet.tax.gov.ua/cashregs/check?id=154139.2.8291&fn=4000066879", result.getHeaders().get("Location"));
  }

  @Test
  public void notValidPath() {
    App app = new App();

    APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("proxy", "4000066879/154139.2.82911");
    requestEvent.setPathParameters(parameters);

    APIGatewayProxyResponseEvent result = app.handleRequest(requestEvent, null);
    assertEquals(400, result.getStatusCode().intValue());
  }
}
