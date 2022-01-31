package pw.mihou.celebius.facade;

import pw.mihou.celebius.Celebius;

public interface GlassBoxFacade {

    DocumentFacade prepare(String event);
    String getName();
    Celebius getCelebius();

}
