package peaccounting;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="product">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="xs:string" name="foreign-id"/>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:string" name="unit"/>
 *     &lt;xs:element type="xs:long" name="price"/>
 *     &lt;xs:element type="xs:long" name="ean"/>
 *     &lt;xs:element name="accounts" minOccurs="0">
 *       &lt;!-- Reference to inner class Accounts -->
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Product{
    private int id;
    private String foreignId;
    private String name;
    private String unit;
    private long price;
    private long ean;
    private Accounts accounts;

    /**
     * Get the 'id' element value.
     *
     * @return value
     */
    public int getId(){
        return id;
    }

    /**
     * Set the 'id' element value.
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Get the 'foreign-id' element value.
     *
     * @return value
     */
    public String getForeignId(){
        return foreignId;
    }

    /**
     * Set the 'foreign-id' element value.
     *
     * @param foreignId
     */
    public void setForeignId(String foreignId){
        this.foreignId = foreignId;
    }

    /**
     * Get the 'name' element value.
     *
     * @return value
     */
    public String getName(){
        return name;
    }

    /**
     * Set the 'name' element value.
     *
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Get the 'unit' element value.
     *
     * @return value
     */
    public String getUnit(){
        return unit;
    }

    /**
     * Set the 'unit' element value.
     *
     * @param unit
     */
    public void setUnit(String unit){
        this.unit = unit;
    }

    /**
     * Get the 'price' element value.
     *
     * @return value
     */
    public long getPrice(){
        return price;
    }

    /**
     * Set the 'price' element value.
     *
     * @param price
     */
    public void setPrice(long price){
        this.price = price;
    }

    /**
     * Get the 'ean' element value.
     *
     * @return value
     */
    public long getEan(){
        return ean;
    }

    /**
     * Set the 'ean' element value.
     *
     * @param ean
     */
    public void setEan(long ean){
        this.ean = ean;
    }

    /**
     * Get the 'accounts' element value.
     *
     * @return value
     */
    public Accounts getAccounts(){
        return accounts;
    }

    /**
     * Set the 'accounts' element value.
     *
     * @param accounts
     */
    public void setAccounts(Accounts accounts){
        this.accounts = accounts;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="accounts" minOccurs="0">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:int" name="se" minOccurs="0"/>
     *       &lt;xs:element type="xs:int" name="eu" minOccurs="0"/>
     *       &lt;xs:element type="xs:int" name="export" minOccurs="0"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Accounts{
        private Integer se;
        private Integer eu;
        private Integer export;

        /**
         * Get the 'se' element value.
         *
         * @return value
         */
        public Integer getSe(){
            return se;
        }

        /**
         * Set the 'se' element value.
         *
         * @param se
         */
        public void setSe(Integer se){
            this.se = se;
        }

        /**
         * Get the 'eu' element value.
         *
         * @return value
         */
        public Integer getEu(){
            return eu;
        }

        /**
         * Set the 'eu' element value.
         *
         * @param eu
         */
        public void setEu(Integer eu){
            this.eu = eu;
        }

        /**
         * Get the 'export' element value.
         *
         * @return value
         */
        public Integer getExport(){
            return export;
        }

        /**
         * Set the 'export' element value.
         *
         * @param export
         */
        public void setExport(Integer export){
            this.export = export;
        }
    }
}
