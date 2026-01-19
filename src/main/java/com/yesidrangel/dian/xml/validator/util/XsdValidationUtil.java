package com.yesidrangel.dian.xml.validator.util;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.yesidrangel.dian.xml.validator.domain.enums.DianSeverityLevel;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import com.yesidrangel.dian.xml.validator.domain.enums.XmlValidationSeverityEnum;

public class XsdValidationUtil {

	public static final String XSD_NO_ENCONTRADO = "XSD no encontrado: ";

	public static List<String> validate(String xml, String xsdPath) {

		List<String> errors = new ArrayList<>();
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			InputStream xsdStream = XsdValidationUtil.class.getClassLoader().getResourceAsStream(xsdPath);
			if (xsdStream == null) {
				throw new RuntimeException(XSD_NO_ENCONTRADO + xsdPath);
			}
			Schema schema = factory.newSchema(new StreamSource(xsdStream));
			Validator validator = schema.newValidator();
			validator.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(SAXParseException e) {
					errors.add(DianSeverityLevel.WARNING.getLabel() + ": " + e.getMessage());
				}
				@Override
				public void error(SAXParseException e) {
					errors.add(DianSeverityLevel.ERROR.getLabel() + ": " + e.getMessage());
				}
				@Override
				public void fatalError(SAXParseException e) {
					errors.add(DianSeverityLevel.FATAL.getLabel() + ": " + e.getMessage());
				}
			});
			validator.validate(new StreamSource(new StringReader(xml)));
		} catch (Exception e) {
			errors.add(e.getMessage());
		}
		return errors;
	}

}
