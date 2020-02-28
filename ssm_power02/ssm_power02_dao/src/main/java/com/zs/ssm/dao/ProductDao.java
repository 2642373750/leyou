package com.zs.ssm.dao;


        import com.zs.ssm.pojo.Product;
        import org.apache.ibatis.annotations.Insert;
        import org.apache.ibatis.annotations.Select;
        import org.springframework.stereotype.Service;

        import java.util.List;

public interface ProductDao {

    //根据id查
    @Select("select * from product where id = #{id}")
    public Product findById(String id)  throws Exception;

    //查询所有
    @Select("select * from product")
    public List<Product> findAll() throws Exception;

    //保存
    @Insert("insert into product (productNum,productName,cityName,departureTime,productPrice,productDesc,productStatus) values(#{productNum},#{productName},#{cityName},#{departureTime},#{productPrice},#{productDesc},#{productStatus})")
    public void saveProduct(Product product) throws Exception;
}
