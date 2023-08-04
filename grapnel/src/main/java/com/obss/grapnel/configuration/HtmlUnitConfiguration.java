package com.obss.grapnel.configuration;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SilentCssErrorHandler;
import org.htmlunit.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HtmlUnitConfiguration {
    public WebClient newWebClient() {
        final WebClient webClient = new WebClient(BrowserVersion.EDGE);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setTimeout(2000);
        webClient.getOptions().setUseInsecureSSL(true);
        // overcome problems in JavaScript
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.setCssErrorHandler(new SilentCssErrorHandler());

        return webClient;
    }
}
