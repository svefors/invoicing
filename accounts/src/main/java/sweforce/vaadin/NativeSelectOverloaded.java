package sweforce.vaadin;

import com.vaadin.data.util.converter.Converter;

/**
 * Created by IntelliJ IDEA.
 * User: sveffa
 * Date: 8/16/12
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class NativeSelectOverloaded extends com.vaadin.ui.NativeSelect {

    public void setConverterObject(Converter<Object, ?> objectConverter) {
        super.setConverter(objectConverter);    //To change body of overridden methods use File | Settings | File Templates.
    }


    public void setConvertToType(Class<?> datamodelType) {
        super.setConverter(datamodelType);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
