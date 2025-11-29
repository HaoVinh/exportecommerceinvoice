/**
 * PhanHeChuongTrinh.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package trong.lixco.com.account.servicepublics;

public class PhanHeChuongTrinh  implements java.io.Serializable {
    private java.lang.Long id;

    private java.lang.String name;

    private trong.lixco.com.account.servicepublics.Program[] programs;

    private int sapxep;

    public PhanHeChuongTrinh() {
    }

    public PhanHeChuongTrinh(
           java.lang.Long id,
           java.lang.String name,
           trong.lixco.com.account.servicepublics.Program[] programs,
           int sapxep) {
           this.id = id;
           this.name = name;
           this.programs = programs;
           this.sapxep = sapxep;
    }


    /**
     * Gets the id value for this PhanHeChuongTrinh.
     * 
     * @return id
     */
    public java.lang.Long getId() {
        return id;
    }


    /**
     * Sets the id value for this PhanHeChuongTrinh.
     * 
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }


    /**
     * Gets the name value for this PhanHeChuongTrinh.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this PhanHeChuongTrinh.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the programs value for this PhanHeChuongTrinh.
     * 
     * @return programs
     */
    public trong.lixco.com.account.servicepublics.Program[] getPrograms() {
        return programs;
    }


    /**
     * Sets the programs value for this PhanHeChuongTrinh.
     * 
     * @param programs
     */
    public void setPrograms(trong.lixco.com.account.servicepublics.Program[] programs) {
        this.programs = programs;
    }

    public trong.lixco.com.account.servicepublics.Program getPrograms(int i) {
        return this.programs[i];
    }

    public void setPrograms(int i, trong.lixco.com.account.servicepublics.Program _value) {
        this.programs[i] = _value;
    }


    /**
     * Gets the sapxep value for this PhanHeChuongTrinh.
     * 
     * @return sapxep
     */
    public int getSapxep() {
        return sapxep;
    }


    /**
     * Sets the sapxep value for this PhanHeChuongTrinh.
     * 
     * @param sapxep
     */
    public void setSapxep(int sapxep) {
        this.sapxep = sapxep;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PhanHeChuongTrinh)) return false;
        PhanHeChuongTrinh other = (PhanHeChuongTrinh) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.programs==null && other.getPrograms()==null) || 
             (this.programs!=null &&
              java.util.Arrays.equals(this.programs, other.getPrograms()))) &&
            this.sapxep == other.getSapxep();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPrograms() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPrograms());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPrograms(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getSapxep();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PhanHeChuongTrinh.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://servicepublics.account.com.lixco.trong/", "phanHeChuongTrinh"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("programs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "programs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://servicepublics.account.com.lixco.trong/", "program"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sapxep");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sapxep"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
