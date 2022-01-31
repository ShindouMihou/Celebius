package pw.mihou.celebius.core;

import pw.mihou.celebius.Celebius;
import pw.mihou.celebius.facade.DocumentFacade;
import pw.mihou.celebius.facade.GlassBoxFacade;

public class GlassBoxCore implements GlassBoxFacade {

    private final Celebius celebius;
    private final String name;

    public GlassBoxCore(Celebius celebius, String name) {
        this.celebius = celebius;
        this.name = name;
    }

    @Override
    public DocumentFacade prepare(String event) {
        return new DocumentCore(event, this);
    }

    @Override
    public Celebius getCelebius() {
        return celebius;
    }

    @Override
    public String getName() {
        return name;
    }
}
