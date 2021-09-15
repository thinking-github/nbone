package org.nbone.io.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.MethodCache;
import org.supercsv.util.Util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thinking
 * @version 1.0
 * @since 2020-06-19
 *
 * @see  org.supercsv.io.CsvBeanWriter
 */
public class CsvBeanExpressionWriter extends AbstractCsvWriter implements ICsvBeanWriter {

    private static final Logger logger = LoggerFactory.getLogger(CsvBeanExpressionWriter.class);

    // temporary storage of bean values
    private final List<Object> beanValues = new ArrayList<Object>();

    // temporary storage of processed columns to be written
    private final List<Object> processedColumns = new ArrayList<Object>();

    // cache of methods for mapping from fields to columns
    private final MethodCache cache = new MethodCache();

    /**
     * Constructs a new <tt>CsvBeanWriter</tt> with the supplied Writer and CSV preferences. Note that the
     * <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
     *
     * @param writer     the writer
     * @param preference the CSV preferences
     * @throws NullPointerException if writer or preference are null
     */
    public CsvBeanExpressionWriter(final Writer writer, final CsvPreference preference) {
        super(writer, preference);
    }

    /**
     * Extracts the bean values, using the supplied name mapping array.
     *
     * @param source      the bean
     * @param nameMapping the name mapping
     * @throws NullPointerException        if source or nameMapping are null
     * @throws SuperCsvReflectionException if there was a reflection exception extracting the bean value
     */
    private void extractBeanValues(final Object source, final String[] nameMapping) {

        if (source == null) {
            throw new NullPointerException("the bean to write should not be null");
        } else if (nameMapping == null) {
            throw new NullPointerException(
                    "the nameMapping array can't be null as it's used to map from fields to columns");
        }

        beanValues.clear();

        StandardEvaluationContext context = new StandardEvaluationContext(source);
        ExpressionParser parser = new SpelExpressionParser();

        for (int i = 0; i < nameMapping.length; i++) {

            final String fieldName = nameMapping[i];

            if (fieldName == null) {
                beanValues.add(null); // assume they always want a blank column

            } else {
                try {
                    Expression exp = parser.parseExpression(fieldName);
                    Object value = exp.getValue(context);
                    beanValues.add(value);
                }catch (SpelEvaluationException elex){
                    //thinking: field name  not exist ignore
                    //thinking:  assume they always want a blank column
                    beanValues.add(null);
                    if(elex.getMessageCode() == SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE){
                        logger.warn(elex.getMessage() +" Exception:" + elex.getClass().getName());
                    }else {
                        logger.warn(elex.getMessage() +" Exception:" + elex.getClass().getName());
                    }
                }
                catch (final Exception e) {
                    throw new SuperCsvReflectionException(String.format("error extracting bean value for field %s",
                            fieldName), e);
                }
            }

        }

    }

    /**
     * {@inheritDoc}
     */
    public void write(final Object source, final String... nameMapping) throws IOException {

        // update the current row/line numbers
        super.incrementRowAndLineNo();

        // extract the bean values
        extractBeanValues(source, nameMapping);

        // write the list
        super.writeRow(beanValues);
    }

    /**
     * {@inheritDoc}
     */
    public void write(final Object source, final String[] nameMapping, final CellProcessor[] processors)
            throws IOException {

        // update the current row/line numbers
        super.incrementRowAndLineNo();

        // extract the bean values
        extractBeanValues(source, nameMapping);

        // execute the processors for each column
        Util.executeCellProcessors(processedColumns, beanValues, processors, getLineNumber(), getRowNumber());

        // write the list
        super.writeRow(processedColumns);
    }
}
