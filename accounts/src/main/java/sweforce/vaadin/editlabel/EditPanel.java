package sweforce.vaadin.editlabel;


import com.vaadin.data.Buffered;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.gwt.client.AbstractFieldState;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Panel;

import java.awt.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 7/20/12
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditPanel<T> extends Panel implements Field<T>{

    private Field<T> delegate;

    private Label label;


    public void showEdit(){

    }

    public void showView(){

    }

    /**
     * Is this field required.
     *
     * Required fields must filled by the user.
     *
     * @return <code>true</code> if the field is required,otherwise
     *         <code>false</code>.
     * @since 3.1
     */
    @Override
    public boolean isRequired(){
        return delegate.isRequired();
    }

    /**
     * Sets the field required. Required fields must filled by the user.
     *
     * @param required
     *            Is the field required.
     * @since 3.1
     */
    @Override
    public void setRequired(boolean required){
        delegate.setRequired(required);
    }

    /**
     * Sets the error message to be displayed if a required field is empty.
     *
     * @param requiredMessage
     *            Error message.
     * @since 5.2.6
     */
    @Override
    public void setRequiredError(String requiredMessage){
        delegate.setRequiredError(requiredMessage);
    }

    /**
     * Gets the error message that is to be displayed if a required field is
     * empty.
     *
     * @return Error message.
     * @since 5.2.6
     */
    @Override
    public String getRequiredError(){
        return delegate.getRequiredError();
    }

//    public String getConversionError() {
//        return delegate.getConversionError();
//    }
//
//    public void setConversionError(String valueConversionError) {
//        delegate.setConversionError(valueConversionError);
//    }
//
//    public boolean isValidationVisible() {
//        return delegate.isValidationVisible();
//    }
//
//    public void setValidationVisible(boolean validateAutomatically) {
//        delegate.setValidationVisible(validateAutomatically);
//    }
//
//    public void setCurrentBufferedSourceException(SourceException currentBufferedSourceException) {
//        delegate.setCurrentBufferedSourceException(currentBufferedSourceException);
//    }
//
//    public Converter<T, Object> getConverter() {
//        return delegate.getConverter();
//    }
//
//    public void setConverter(Converter<T, ?> tConverter) {
//        delegate.setConverter(tConverter);
//    }

//    @Override
//    public AbstractFieldState getState() {
//        return delegate.getState();
//    }

    @Override
    public void updateState() {
        delegate.updateState();
    }

    @Override
    public Class<? extends T> getType() {
        return delegate.getType();
    }

    @Override
    public boolean isReadOnly() {
        return delegate.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        delegate.setReadOnly(readOnly);
    }

    @Override
    public boolean isInvalidCommitted() {
        return delegate.isInvalidCommitted();
    }

    @Override
    public void setInvalidCommitted(boolean isCommitted) {
        delegate.setInvalidCommitted(isCommitted);
    }

    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        delegate.commit();
    }

    @Override
    public void discard() throws SourceException {
        delegate.discard();
    }

    @Override
    public boolean isModified() {
        return delegate.isModified();
    }

    @Override
    public boolean isWriteThrough() {
        return delegate.isWriteThrough();
    }

    @Override
    @Deprecated
    public void setWriteThrough(boolean writeThrough) throws SourceException, Validator.InvalidValueException {
        delegate.setWriteThrough(writeThrough);
    }

    @Override
    public boolean isReadThrough() {
        return delegate.isReadThrough();
    }

    @Override
    @Deprecated
    public void setReadThrough(boolean readThrough) throws SourceException {
        delegate.setReadThrough(readThrough);
    }

    @Override
    public void setBuffered(boolean buffered) {
        delegate.setBuffered(buffered);
    }

    @Override
    public boolean isBuffered() {
        return delegate.isBuffered();
    }

    @Override
    @Deprecated
    public String toString() {
        return delegate.toString();
    }

    @Override
    public T getValue() {
        return delegate.getValue();
    }

    @Override
    public void setValue(Object newFieldValue) throws ReadOnlyException, Converter.ConversionException {
        delegate.setValue(newFieldValue);
    }

    @Override
    public Property getPropertyDataSource() {
        return delegate.getPropertyDataSource();
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        delegate.setPropertyDataSource(newDataSource);
    }

//    public void setConverter(Class<?> datamodelType) {
//        delegate.setConverter(datamodelType);
//    }
//
//    public Object getConvertedValue() {
//        return delegate.getConvertedValue();
//    }
//
//    public void setConvertedValue(Object value) {
//        delegate.setConvertedValue(value);
//    }

    @Override
    public void addValidator(Validator validator) {
        delegate.addValidator(validator);
    }

    @Override
    public Collection<Validator> getValidators() {
        return delegate.getValidators();
    }

    @Override
    public void removeValidator(Validator validator) {
        delegate.removeValidator(validator);
    }

//    public void removeAllValidators() {
//        delegate.removeAllValidators();
//    }

    @Override
    public boolean isValid() {
        return delegate.isValid();
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        delegate.validate();
    }

    @Override
    public boolean isInvalidAllowed() {
        return delegate.isInvalidAllowed();
    }

    @Override
    public void setInvalidAllowed(boolean invalidAllowed) throws UnsupportedOperationException {
        delegate.setInvalidAllowed(invalidAllowed);
    }

//    @Override
//    public ErrorMessage getErrorMessage() {
//        return delegate.getErrorMessage();
//    }

    @Override
    public void addListener(ValueChangeListener listener) {
        delegate.addListener(listener);
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
        delegate.removeListener(listener);
    }

//    public void readOnlyStatusChange(ReadOnlyStatusChangeEvent event) {
//        delegate.readOnlyStatusChange(event);
//    }
//
//    public void addListener(ReadOnlyStatusChangeListener listener) {
//        delegate.addListener(listener);
//    }
//
//    public void removeListener(ReadOnlyStatusChangeListener listener) {
//        delegate.removeListener(listener);
//    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        delegate.valueChange(event);
    }

    @Override
    public void focus() {
        delegate.focus();
    }

    @Override
    public int getTabIndex() {
        return delegate.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        delegate.setTabIndex(tabIndex);
    }
}
