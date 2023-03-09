package py.com.fermar.exception;

/**
 * 
 * Excepción definida para casos particulares en los que sólo se desea mostrar
 * el mensaje que causó la excepción y no instanciar RuntimeException
 * 
 * @author fgimenez
 *
 */
public class SIFENGuardarException extends Exception {

    private static final long serialVersionUID = 1L;

    public SIFENGuardarException() {
        super();
    }

    public SIFENGuardarException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public SIFENGuardarException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public SIFENGuardarException(String arg0) {
        super(arg0);
    }

    public SIFENGuardarException(Throwable arg0) {
        super(arg0);
    }

}
