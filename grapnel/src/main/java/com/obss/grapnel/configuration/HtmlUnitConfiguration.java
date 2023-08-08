package com.obss.grapnel.configuration;

import java.net.MalformedURLException;
import java.net.URL;
import org.htmlunit.*;
import org.htmlunit.cssparser.parser.CSSErrorHandler;
import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.cssparser.parser.CSSParseException;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.parser.HTMLParserListener;
import org.htmlunit.javascript.JavaScriptErrorListener;
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
    webClient.setIncorrectnessListener((arg0, arg1) -> {});
    webClient.setCssErrorHandler(new CSSErrorHandler() {
      @Override
      public void warning(CSSParseException exception) throws CSSException {

      }

      @Override
      public void error(CSSParseException exception) throws CSSException {

      }

      @Override
      public void fatalError(CSSParseException exception) throws CSSException {

      }
    });
    webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
      @Override
      public void scriptException(HtmlPage page, ScriptException scriptException) {

      }

      @Override
      public void timeoutError(HtmlPage page, long allowedTime, long executionTime) {

      }

      @Override
      public void malformedScriptURL(HtmlPage page, String url, MalformedURLException malformedURLException) {

      }

      @Override
      public void loadScriptError(HtmlPage page, URL scriptUrl, Exception exception) {

      }

      @Override
      public void warn(String message, String sourceName, int line, String lineSource, int lineOffset) {

      }
    });
    webClient.setHTMLParserListener(new HTMLParserListener() {
      @Override
      public void error(String message, URL url, String html, int line, int column, String key) {

      }

      @Override
      public void warning(String message, URL url, String html, int line, int column, String key) {

      }
    });

    return webClient;
  }
}
