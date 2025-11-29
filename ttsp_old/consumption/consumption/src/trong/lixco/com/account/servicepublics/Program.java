/**
 * Program.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package trong.lixco.com.account.servicepublics;

public class Program  extends trong.lixco.com.account.servicepublics.AbstractEntity  implements java.io.Serializable {
    private java.lang.String background;

    private java.lang.String cusCSS;

    private java.lang.String customField1;

    private java.lang.String customField2;

    private java.lang.String description;

    private java.lang.String fullname;

    private java.lang.String icon;

    private short indexProgram;

    private short indexSort;

    private byte[] logo;

    private java.lang.String name;

    private trong.lixco.com.account.servicepublics.PhanHeChuongTrinh phanHeChuongTrinh;

    private java.lang.String serveraddress;

    private java.lang.String serveraddressPublic;

    private java.lang.String uRL;

    public Program() {
    }

    public Program(
           java.util.Calendar createdDate,
           java.lang.String createdUser,
           boolean disable,
           java.lang.Long id,
           java.util.Calendar modifiedDate,
           java.lang.String note,
           boolean select,
           java.lang.String background,
           java.lang.String cusCSS,
           java.lang.String customField1,
           java.lang.String customField2,
           java.lang.String description,
           java.lang.String fullname,
           java.lang.String icon,
           short indexProgram,
           short indexSort,
           byte[] logo,
           java.lang.String name,
           trong.lixco.com.account.servicepublics.PhanHeChuongTrinh phanHeChuongTrinh,
           java.lang.String serveraddress,
           java.lang.String serveraddressPublic,
           java.lang.String uRL) {
        super(
            createdDate,
            createdUser,
            disable,
            id,
            modifiedDate,
            note,
            select);
        this.background = background;
        this.cusCSS = cusCSS;
        this.customField1 = customField1;
        this.customField2 = customField2;
        this.description = description;
        this.fullname = fullname;
        this.icon = icon;
        this.indexProgram = indexProgram;
        this.indexSort = indexSort;
        this.logo = logo;
        this.name = name;
        this.phanHeChuongTrinh = phanHeChuongTrinh;
        this.serveraddress = serveraddress;
        this.serveraddressPublic = serveraddressPublic;
        this.uRL = uRL;
    }


    /**
     * Gets the background value for this Program.
     * 
     * @return background
     */
    public java.lang.String getBackground() {
        return background;
    }


    /**
     * Sets the background value for this Program.
     * 
     * @param background
     */
    public void setBackground(java.lang.String background) {
        this.background = background;
    }


    /**
     * Gets the cusCSS value for this Program.
     * 
     * @return cusCSS
     */
    public java.lang.String getCusCSS() {
        return cusCSS;
    }


    /**
     * Sets the cusCSS value for this Program.
     * 
     * @param cusCSS
     */
    public void setCusCSS(java.lang.String cusCSS) {
        this.cusCSS = cusCSS;
    }


    /**
     * Gets the customField1 value for this Program.
     * 
     * @return customField1
     */
    public java.lang.String getCustomField1() {
        return customField1;
    }


    /**
     * Sets the customField1 value for this Program.
     * 
     * @param customField1
     */
    public void setCustomField1(java.lang.String customField1) {
        this.customField1 = customField1;
    }


    /**
     * Gets the customField2 value for this Program.
     * 
     * @return customField2
     */
    public java.lang.String getCustomField2() {
        return customField2;
    }


    /**
     * Sets the customField2 value for this Program.
     * 
     * @param customField2
     */
    public void setCustomField2(java.lang.String customField2) {
        this.customField2 = customField2;
    }


    /**
     * Gets the description value for this Program.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Program.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the fullname value for this Program.
     * 
     * @return fullname
     */
    public java.lang.String getFullname() {
        return fullname;
    }


    /**
     * Sets the fullname value for this Program.
     * 
     * @param fullname
     */
    public void setFullname(java.lang.String fullname) {
        this.fullname = fullname;
    }


    /**
     * Gets the icon value for this Program.
     * 
     * @return icon
     */
    public java.lang.String getIcon() {
        return icon;
    }


    /**
     * Sets the icon value for this Program.
     * 
     * @param icon
     */
    public void setIcon(java.lang.String icon) {
        this.icon = icon;
    }


    /**
     * Gets the indexProgram value for this Program.
     * 
     * @return indexProgram
     */
    public short getIndexProgram() {
        return indexProgram;
    }


    /**
     * Sets the indexProgram value for this Program.
     * 
     * @param indexProgram
     */
    public void setIndexProgram(short indexProgram) {
        this.indexProgram = indexProgram;
    }


    /**
     * Gets the indexSort value for this Program.
     * 
     * @return indexSort
     */
    public short getIndexSort() {
        return indexSort;
    }


    /**
     * Sets the indexSort value for this Program.
     * 
     * @param indexSort
     */
    public void setIndexSort(short indexSort) {
        this.indexSort = indexSort;
    }


    /**
     * Gets the logo value for this Program.
     * 
     * @return logo
     */
    public byte[] getLogo() {
        return logo;
    }


    /**
     * Sets the logo value for this Program.
     * 
     * @param logo
     */
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }


    /**
     * Gets the name value for this Program.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Program.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the phanHeChuongTrinh value for this Program.
     * 
     * @return phanHeChuongTrinh
     */
    public trong.lixco.com.account.servicepublics.PhanHeChuongTrinh getPhanHeChuongTrinh() {
        return phanHeChuongTrinh;
    }


    /**
     * Sets the phanHeChuongTrinh value for this Program.
     * 
     * @param phanHeChuongTrinh
     */
    public void setPhanHeChuongTrinh(trong.lixco.com.account.servicepublics.PhanHeChuongTrinh phanHeChuongTrinh) {
        this.phanHeChuongTrinh = phanHeChuongTrinh;
    }


    /**
     * Gets the serveraddress value for this Program.
     * 
     * @return serveraddress
     */
    public java.lang.String getServeraddress() {
        return serveraddress;
    }


    /**
     * Sets the serveraddress value for this Program.
     * 
     * @param serveraddress
     */
    public void setServeraddress(java.lang.String serveraddress) {
        this.serveraddress = serveraddress;
    }


    /**
     * Gets the serveraddressPublic value for this Program.
     * 
     * @return serveraddressPublic
     */
    public java.lang.String getServeraddressPublic() {
        return serveraddressPublic;
    }


    /**
     * Sets the serveraddressPublic value for this Program.
     * 
     * @param serveraddressPublic
     */
    public void setServeraddressPublic(java.lang.String serveraddressPublic) {
        this.serveraddressPublic = serveraddressPublic;
    }


    /**
     * Gets the uRL value for this Program.
     * 
     * @return uRL
     */
    public java.lang.String getURL() {
        return uRL;
    }


    /**
     * Sets the uRL value for this Program.
     * 
     * @param uRL
     */
    public void setURL(java.lang.String uRL) {
        this.uRL = uRL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Program)) return false;
        Program other = (Program) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.background==null && other.getBackground()==null) || 
             (this.background!=null &&
              this.background.equals(other.getBackground()))) &&
            ((this.cusCSS==null && other.getCusCSS()==null) || 
             (this.cusCSS!=null &&
              this.cusCSS.equals(other.getCusCSS()))) &&
            ((this.customField1==null && other.getCustomField1()==null) || 
             (this.customField1!=null &&
              this.customField1.equals(other.getCustomField1()))) &&
            ((this.customField2==null && other.getCustomField2()==null) || 
             (this.customField2!=null &&
              this.customField2.equals(other.getCustomField2()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.fullname==null && other.getFullname()==null) || 
             (this.fullname!=null &&
              this.fullname.equals(other.getFullname()))) &&
            ((this.icon==null && other.getIcon()==null) || 
             (this.icon!=null &&
              this.icon.equals(other.getIcon()))) &&
            this.indexProgram == other.getIndexProgram() &&
            this.indexSort == other.getIndexSort() &&
            ((this.logo==null && other.getLogo()==null) || 
             (this.logo!=null &&
              java.util.Arrays.equals(this.logo, other.getLogo()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.phanHeChuongTrinh==null && other.getPhanHeChuongTrinh()==null) || 
             (this.phanHeChuongTrinh!=null &&
              this.phanHeChuongTrinh.equals(other.getPhanHeChuongTrinh()))) &&
            ((this.serveraddress==null && other.getServeraddress()==null) || 
             (this.serveraddress!=null &&
              this.serveraddress.equals(other.getServeraddress()))) &&
            ((this.serveraddressPublic==null && other.getServeraddressPublic()==null) || 
             (this.serveraddressPublic!=null &&
              this.serveraddressPublic.equals(other.getServeraddressPublic()))) &&
            ((this.uRL==null && other.getURL()==null) || 
             (this.uRL!=null &&
              this.uRL.equals(other.getURL())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getBackground() != null) {
            _hashCode += getBackground().hashCode();
        }
        if (getCusCSS() != null) {
            _hashCode += getCusCSS().hashCode();
        }
        if (getCustomField1() != null) {
            _hashCode += getCustomField1().hashCode();
        }
        if (getCustomField2() != null) {
            _hashCode += getCustomField2().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getFullname() != null) {
            _hashCode += getFullname().hashCode();
        }
        if (getIcon() != null) {
            _hashCode += getIcon().hashCode();
        }
        _hashCode += getIndexProgram();
        _hashCode += getIndexSort();
        if (getLogo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLogo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLogo(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPhanHeChuongTrinh() != null) {
            _hashCode += getPhanHeChuongTrinh().hashCode();
        }
        if (getServeraddress() != null) {
            _hashCode += getServeraddress().hashCode();
        }
        if (getServeraddressPublic() != null) {
            _hashCode += getServeraddressPublic().hashCode();
        }
        if (getURL() != null) {
            _hashCode += getURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Program.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://servicepublics.account.com.lixco.trong/", "program"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("background");
        elemField.setXmlName(new javax.xml.namespace.QName("", "background"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cusCSS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cusCSS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customField1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "customField1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customField2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "customField2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fullname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("icon");
        elemField.setXmlName(new javax.xml.namespace.QName("", "icon"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indexProgram");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indexProgram"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indexSort");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indexSort"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "logo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
        elemField.setFieldName("phanHeChuongTrinh");
        elemField.setXmlName(new javax.xml.namespace.QName("", "phanHeChuongTrinh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://servicepublics.account.com.lixco.trong/", "phanHeChuongTrinh"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serveraddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serveraddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serveraddressPublic");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serveraddressPublic"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("URL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uRL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
