package uk.co.naeem.tasktracker.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyRunner {
    private static final Boolean REITH_PROXY_ENABLED = Boolean.TRUE;

    private static final String CONTEXT_PATH = "/tasktracker";
    private static final String IP = "0.0.0.0";
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        setProxySystemProperties();
        Server server = createServer();
        server.start();
        System.out.println("Jetty started on port " + PORT);
        server.join();
    }

    public static Server createServer() {
        WebAppContext webApp = createWebapp();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { webApp });
        Server server = new Server();
        server.addConnector(createConnector());
        server.setHandler(contexts);
        return server;
    }

    private static WebAppContext createWebapp() {
        String webAppDir = "src/main/webapp/";
        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath(CONTEXT_PATH);
        webApp.setResourceBase(webAppDir);
        webApp.setParentLoaderPriority(false);
        webApp.setCopyWebDir(true);
        return webApp;
    }

    private static Connector createConnector() {
        Connector connector = new SelectChannelConnector();
        connector.setPort(PORT);
        connector.setHost(IP);
        return connector;
    }

    private static void setProxySystemProperties() {
        if (REITH_PROXY_ENABLED) {
            System.out.println("Setting proxy properties");
            System.setProperty("kl.usingJetty", "true");
            System.setProperty("socksProxyHost", "");
            System.setProperty("https.proxyHost", "www-cache.reith.bbc.co.uk");
            System.setProperty("https.proxyPort", "80");
            System.setProperty("https.nonProxyHosts", "localhost|*.sandbox.dev.bbc.uk");
        }
    }
}
