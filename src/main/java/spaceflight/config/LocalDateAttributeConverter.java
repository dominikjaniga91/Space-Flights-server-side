package spaceflight.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;

/**
 * This class is required to appropriate save LocalDate into MySQL database
 */

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        return localDate == null ? null : localDate.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String sqlDate) {
        return sqlDate == null ? null : LocalDate.parse(sqlDate);
    }
}