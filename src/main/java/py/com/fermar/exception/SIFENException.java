package py.com.fermar.exception;

/**
 * 
 * Excepción definida para casos particulares en los que sólo se desea mostrar
 * el mensaje que causó la excepción y no instanciar Exception
 * 
 * @author fgimenez
 *
 */
public class SIFENException extends Exception {

    private static final long serialVersionUID = -2248146801714109922L;

    private int id;

    public SIFENException(String message) {
        super(message);
    }

    public SIFENException(String message, int id) {
        super(message);
        this.setId(id);
    }

    public SIFENException() {
        super();
    }

    public SIFENException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SIFENException(Throwable cause) {
        super(cause);
    }

    public SIFENException(Exception ex) {
        super(ex.getMessage(), ex);
    }

    public SIFENException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}