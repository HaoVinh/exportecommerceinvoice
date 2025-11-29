package lixco.com.converter;

import java.io.Serializable;
import java.util.Objects;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "integerConverter")
public class IntegerConverter  implements Converter,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
		try{
		return Integer.parseInt(value);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
		try {
			return Objects.toString(value, "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
