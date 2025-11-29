package trong.lixco.com.util;


import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.thoughtworks.xstream.XStream;

@FacesConverter(value = "abstractConverter")
public class AbstractConverter implements Converter,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
		try{
			XStream stream = new XStream();
			Object backObj = stream.fromXML(value);
			return backObj;
		}catch(Exception  e){
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
		try{
			XStream stream = new XStream();
			String xml = stream.toXML(value);
			return xml;
		}catch(Exception e){
		}
		return null;
	}
}