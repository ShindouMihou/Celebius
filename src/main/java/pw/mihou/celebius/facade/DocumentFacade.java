package pw.mihou.celebius.facade;

public interface DocumentFacade {

    DocumentFacade setData(String key, Object value);
    DocumentFacade setMessage(String message);
    void send();

}
