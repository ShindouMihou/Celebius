package pw.mihou.celebius;

import pw.mihou.celebius.core.CelebiusCore;
import pw.mihou.celebius.enums.CelebiusLevel;
import pw.mihou.celebius.facade.GlassBoxFacade;

import java.net.URISyntaxException;
import java.util.function.Consumer;

public interface Celebius {

    static Celebius createInstance(String uri, String token) throws URISyntaxException {
        return new CelebiusCore(uri, token);
    }


    Celebius setLogger(Consumer<String> message);
    Celebius setLevel(CelebiusLevel level);
    GlassBoxFacade getGlassbox(String name);

}
