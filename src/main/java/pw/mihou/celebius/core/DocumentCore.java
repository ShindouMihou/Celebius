package pw.mihou.celebius.core;

import pw.mihou.celebius.facade.DocumentFacade;
import pw.mihou.celebius.facade.GlassBoxFacade;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentCore implements DocumentFacade {

    private final Map<String, Object> store = new ConcurrentHashMap<>();
    private final GlassBoxFacade glassBox;

    public DocumentCore(String event, GlassBoxFacade glassBox) {
        store.put("_event", event);
        store.put("_callback", UUID.randomUUID().toString());
        store.put("_glassbox", glassBox.getName());
        this.glassBox = glassBox;
    }

    @Override
    public DocumentFacade setData(String key, Object value) {
        store.put(key, value);
        return this;
    }

    @Override
    public DocumentFacade setMessage(String message) {
        store.put("_message", message);
        return this;
    }

    public Map<String, Object> getStore() {
        return store;
    }

    @Override
    public void send() {
        ((CelebiusCore) glassBox.getCelebius()).send(this);
    }
}
