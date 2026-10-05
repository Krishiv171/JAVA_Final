import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class ValidationUtils {
    private ValidationUtils(){}
    public static LocalDate parseDate(String value) throws ValidationException {
        try {
            LocalDate d=LocalDate.parse(value);
            if(d.isAfter(LocalDate.now()))throw new ValidationException("Date cannot be in the future.");
            return d;
        } catch(DateTimeParseException e) {
            throw new ValidationException("Date must use YYYY-MM-DD format.");
        }
    }
    public static void validateAssetInput(String id,String name,double purchasePrice,double currentValue,
                                           double quantity,LocalDate date) throws ValidationException {
        if(id==null||id.isBlank())throw new ValidationException("Asset ID is required.");
        if(name==null||name.isBlank())throw new ValidationException("Asset name is required.");
        if(purchasePrice<=0)throw new ValidationException("Purchase price must be greater than 0.");
        if(currentValue<0)throw new ValidationException("Current value cannot be negative.");
        if(quantity<=0)throw new ValidationException("Quantity must be greater than 0.");
        if(date==null)throw new ValidationException("Purchase date is required.");
    }
}
