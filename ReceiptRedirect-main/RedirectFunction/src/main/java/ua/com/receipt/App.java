package ua.com.receipt;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {

        Map<String, String> headers = new HashMap<>();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        if(input == null) return response.withStatusCode(400);

        String path = getPath(input);
        boolean valid = isValidPath(path);

        if(!valid) {
            return response.withStatusCode(400);
        }

        String[] registrarIdAndFiscalNumber = getRegistrarIdAndFiscalNumber(path);
        String redirectUrl = createUrlRedirect(registrarIdAndFiscalNumber);
        headers.put("Location", redirectUrl);
        return response.withStatusCode(301);
    }

    private String getPath(APIGatewayProxyRequestEvent input){
        return input.getPathParameters().getOrDefault("proxy", "");
    }

    private boolean isValidPath(String path) {

        Pattern pattern = Pattern.compile("[0-9]{10,12}/([0-9]{1,12}|[0-9]{1,12}\\.[0-9]{1,4}\\.[0-9]{4})");
        return pattern.matcher(path).matches();
    }

    private String[] getRegistrarIdAndFiscalNumber(String path)
    {
        return path.split("/");
    }

    private String createUrlRedirect(String[] registrarIdAndFiscalNumber)
    {
        return String.format("https://cabinet.tax.gov.ua/cashregs/check?id=%s&fn=%s",
                registrarIdAndFiscalNumber[1],  // fiscalNumber
                registrarIdAndFiscalNumber[0]   // registrarId
        );
    }
}
