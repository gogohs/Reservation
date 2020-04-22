package market;

import javax.persistence.*;

import market.external.Product;
import market.external.ProductService;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String reservationStatus;
    private Integer productId;
    private String type;

    public String getType(){ return type;}
    public void setType(String type){this.type = type;}

    @PostPersist
    public void onPostPersist(){
        Reserved reserved = new Reserved();
        BeanUtils.copyProperties(this, reserved);
        reserved.publish();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
        System.out.println("Type : "+ type);
        market.external.Product product = new market.external.Product();
        product.setId(this.getProductId());
        if(type.equals("Reserved")){
            product.setProductStatus("02");
        }else{
            product.setProductStatus("01");
        }
        // mappings goes here
        Application.applicationContext.getBean(market.external.ProductService.class)
            .productStatusChange(product);
    }


    @PostUpdate
    public void onPostUpdate() {
        ReservationCanceled reservationCanceled = new ReservationCanceled();
        BeanUtils.copyProperties(this, reservationCanceled);
        reservationCanceled.publish();

        Product product = new Product();

        product.setId(this.getProductId());

        product.setProductStatus("01"); // 예약됨

        ProductService bookService = Application.applicationContext.getBean(ProductService.class);
        bookService.productStatusChange(product);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }




}
