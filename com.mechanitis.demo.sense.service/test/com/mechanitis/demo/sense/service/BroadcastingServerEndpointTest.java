package com.mechanitis.demo.sense.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class BroadcastingServerEndpointTest {

    @Test
    @DisplayName("should forward messages to all open sessions")
    void shouldForwardToAllSessions() throws IOException {
        // given:
        BroadcastingServerEndpoint endpoint = new BroadcastingServerEndpoint("", 0);
        RemoteEndpoint.Basic remoteEndpoint1 = mock(RemoteEndpoint.Basic.class);
        RemoteEndpoint.Basic remoteEndpoint2 = mock(RemoteEndpoint.Basic.class);
        Session session1 = createMockSession("1", remoteEndpoint1);
        Session session2 = createMockSession("2", remoteEndpoint2);

        endpoint.onOpen(session1, null);
        endpoint.onOpen(session2, null);

        String message = "Some Message";

        // when:
        endpoint.onNext(message);

        // then:
        assertAll(() -> verify(remoteEndpoint1).sendText(message),
                  () -> verify(remoteEndpoint2).sendText(message));

        // finally:
        endpoint.close();
    }

    @Test
    @DisplayName("should not try to forward messages to closed sessions")
    void shouldNotForwardToClosedSessions() {
        // given:
        BroadcastingServerEndpoint endpoint = new BroadcastingServerEndpoint("", 0);
        Session session = createMockSession("session");

        // when:
        endpoint.onNext("Some Tweet");

        // then:
        assertAll(() -> verify(session, never()).getAsyncRemote(),
                  () -> verify(session, never()).getBasicRemote());

        // finally:
        endpoint.close();
    }

    private static Session createMockSession(String id, RemoteEndpoint.Basic remoteEndpoint) {
        Session session = createMockSession(id);
        when(session.getBasicRemote()).thenReturn(remoteEndpoint);
        return session;
    }

    private static Session createMockSession(String id) {
        Session session = mock(Session.class);
        when(session.isOpen()).thenReturn(true);
        when(session.getId()).thenReturn(id);
        return session;
    }

    private static class StubMessage {
        @Override
        public String toString() {
            return "StubMessage{}";
        }
    }
}

