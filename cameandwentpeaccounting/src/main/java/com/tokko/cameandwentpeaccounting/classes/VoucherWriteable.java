package com.tokko.cameandwentpeaccounting.classes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="voucher-writeable">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="serie"/>
 *     &lt;xs:element type="xs:string" name="description"/>
 *     &lt;xs:element type="xs:string" name="comments"/>
 *     &lt;xs:element type="xs:date" name="date"/>
 *     &lt;xs:element name="transactions">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element name="transaction" minOccurs="0" maxOccurs="unbounded">
 *             &lt;!-- Reference to inner class Transaction -->
 *           &lt;/xs:element>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *     &lt;xs:element name="files">
 *       &lt;xs:complexType>
 *         &lt;xs:sequence>
 *           &lt;xs:element name="file" minOccurs="0" maxOccurs="unbounded">
 *             &lt;!-- Reference to inner class File -->
 *           &lt;/xs:element>
 *         &lt;/xs:sequence>
 *       &lt;/xs:complexType>
 *     &lt;/xs:element>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class VoucherWriteable{
    private String serie;
    private String description;
    private String comments;
    private Date date;
    private List<Transaction> transactionList = new ArrayList<Transaction>();
    private List<File> fileList = new ArrayList<File>();

    /**
     * Get the 'serie' element value.
     *
     * @return value
     */
    public String getSerie(){
        return serie;
    }

    /**
     * Set the 'serie' element value.
     *
     * @param serie
     */
    public void setSerie(String serie){
        this.serie = serie;
    }

    /**
     * Get the 'description' element value.
     *
     * @return value
     */
    public String getDescription(){
        return description;
    }

    /**
     * Set the 'description' element value.
     *
     * @param description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Get the 'comments' element value.
     *
     * @return value
     */
    public String getComments(){
        return comments;
    }

    /**
     * Set the 'comments' element value.
     *
     * @param comments
     */
    public void setComments(String comments){
        this.comments = comments;
    }

    /**
     * Get the 'date' element value.
     *
     * @return value
     */
    public Date getDate(){
        return date;
    }

    /**
     * Set the 'date' element value.
     *
     * @param date
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Get the list of 'transaction' element items.
     *
     * @return list
     */
    public List<Transaction> getTransactionList(){
        return transactionList;
    }

    /**
     * Set the list of 'transaction' element items.
     *
     * @param list
     */
    public void setTransactionList(List<Transaction> list){
        transactionList = list;
    }

    /**
     * Get the list of 'file' element items.
     *
     * @return list
     */
    public List<File> getFileList(){
        return fileList;
    }

    /**
     * Set the list of 'file' element items.
     *
     * @param list
     */
    public void setFileList(List<File> list){
        fileList = list;
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="transaction"
     * minOccurs="0" maxOccurs="unbounded">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:int" name="account-nr"/>
     *       &lt;xs:element type="xs:long" name="amount"/>
     *       &lt;xs:element type="project-reference" name="project" minOccurs="0"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class Transaction{
        private Integer accountNr;
        private Long amount;
        private ProjectReference project;

        /**
         * Get the 'account-nr' element value.
         *
         * @return value
         */
        public Integer getAccountNr(){
            return accountNr;
        }

        /**
         * Set the 'account-nr' element value.
         *
         * @param accountNr
         */
        public void setAccountNr(Integer accountNr){
            this.accountNr = accountNr;
        }

        /**
         * Get the 'amount' element value.
         *
         * @return value
         */
        public Long getAmount(){
            return amount;
        }

        /**
         * Set the 'amount' element value.
         *
         * @param amount
         */
        public void setAmount(Long amount){
            this.amount = amount;
        }

        /**
         * Get the 'project' element value.
         *
         * @return value
         */
        public ProjectReference getProject(){
            return project;
        }

        /**
         * Set the 'project' element value.
         *
         * @param project
         */
        public void setProject(ProjectReference project){
            this.project = project;
        }
    }

    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="file" minOccurs="0"
     * maxOccurs="unbounded">
     *   &lt;xs:complexType>
     *     &lt;xs:sequence>
     *       &lt;xs:element type="xs:string" name="filename"/>
     *       &lt;xs:element type="xs:hexBinary" name="data"/>
     *     &lt;/xs:sequence>
     *   &lt;/xs:complexType>
     * &lt;/xs:element>
     * </pre>
     */
    public static class File{
        private String filename;
        private byte[] data;

        /**
         * Get the 'filename' element value.
         *
         * @return value
         */
        public String getFilename(){
            return filename;
        }

        /**
         * Set the 'filename' element value.
         *
         * @param filename
         */
        public void setFilename(String filename){
            this.filename = filename;
        }

        /**
         * Get the 'data' element value.
         *
         * @return value
         */
        public byte[] getData(){
            return data;
        }

        /**
         * Set the 'data' element value.
         *
         * @param data
         */
        public void setData(byte[] data){
            this.data = data;
        }
    }
}
