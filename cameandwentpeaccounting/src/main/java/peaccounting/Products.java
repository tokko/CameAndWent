package peaccounting;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="products">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="product" name="product" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Products{
    private List<Product> productList = new ArrayList<Product>();

    /**
     * Get the list of 'product' element items.
     *
     * @return list
     */
    public List<Product> getProductList(){
        return productList;
    }

    /**
     * Set the list of 'product' element items.
     *
     * @param list
     */
    public void setProductList(List<Product> list){
        productList = list;
    }
}
