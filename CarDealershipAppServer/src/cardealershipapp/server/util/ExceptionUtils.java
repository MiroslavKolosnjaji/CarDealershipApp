package cardealershipapp.server.util;

public class ExceptionUtils {
    public static final String GENERIC_ERROR_MESSAGE = "Došlo je do greške prilikom obrade vašeg zahteva. Molimo proverite svoje podatke i pokušajte ponovo. Ako problem i dalje postoji, kontaktirajte podršku.";

    public static final String DATABASE_SQL_QUERY_EXECUTION_ERROR_MESSAGE = "Greska prilikom izvrsavanja SQL upita: ";
    public static final String DATABASE_CONFIRM_TRANSACTION_ERROR_MESSAGE = "Greška prilikom potvrđivanja transakcije: ";
    public static final String DATABASE_CANCEL_TRANSACTION_ERROR_MESSAGE = "Greška prilikom poništavanja transakcije: ";
    public static final String UNSUPPORTED_OPERATION_MESSAGE = "Ova vrsta obrade nije implementirana!";

}
