package sweforce.vaadin;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.ComboBox;

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 8/17/12
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComboBoxOverloaded extends ComboBox {

    public void setConverterObject(Converter<Object, ?> objectConverter) {
        super.setConverter(objectConverter);    //To change body of overridden methods use File | Settings | File Templates.
    }


    public void setConvertToType(Class<?> datamodelType) {
        super.setConverter(datamodelType);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
