package ge.taxistgela.websocket;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created by Alex on 6/6/2015.
 */
public class Configurator extends ServerEndpointConfig.Configurator {
    private static final Server server = new Server();

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        if (Server.class.equals(endpointClass)) {
            return (T) server;
        }

        throw new InstantiationException();
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        System.out.println("handshake passed");
        HttpSession session = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(ServletContext.class.getName(), session.getServletContext());

    }
}