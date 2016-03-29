package org.henry.jmockit;
import java.math.*;
import javax.persistence.*;

@Entity
public class EntityX
{
   private String someProperty;
   private String customerEmail;
   private BigDecimal total;

   public EntityX() {}

   public EntityX(int type, String code, String customerEmail)
   {
      this.customerEmail = customerEmail;
      someProperty = "abc";
   }

   public String getSomeProperty() { return someProperty; }
   private void setSomeProperty(String someProperty) { this.someProperty = someProperty; }

   public String getCustomerEmail() { return customerEmail; }
   public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

   public BigDecimal getTotal() { return total; }
   public void setTotal(BigDecimal total) { this.total = total; }
}