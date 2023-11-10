package gm.inventarios.controlador;

import gm.inventarios.excepcion.RecursoNoEncontradoExcepcion;
import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//http://localhost:8080/inventario-app
@RequestMapping("inventario-app")
@CrossOrigin(value = "http://localhost:4200")
public class ProductoControlador {

    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoServicio productoServicio;

    //http://localhost:8080/inventario-app/productos
    @GetMapping("/productos")
    public List<Producto> getProducts(){
        List<Producto> productos = this.productoServicio.listarProductos();
        logger.info("Productos obtenidos: ");
        productos.forEach(producto -> logger.info(producto.toString()));
        return productos;
    }

    @PostMapping("/productos")
    public Producto createProducts(@RequestBody Producto producto){
        logger.info("Producto a agregar: " + producto.toString());
        return this.productoServicio.guardarProducto(producto);
    }

    @GetMapping("/productos/{idProducto}")
    public ResponseEntity<Producto> getProductById(@PathVariable Integer idProducto){
        logger.info("Producto obtenido: " + idProducto);
        Producto producto = this.productoServicio.buscarProductoPorId(idProducto);
        if(producto == null){
            logger.info("Producto no encontrado");
            throw new RecursoNoEncontradoExcepcion("Producto no encontrado" + idProducto);
        }
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/productos/{idProducto}")
    public ResponseEntity<Producto> updateProduct(@PathVariable Integer idProducto, @RequestBody Producto productoRecibido){
        Producto producto = this.productoServicio.buscarProductoPorId(idProducto);
        if(producto == null){
            logger.info("Producto no encontrado");
            throw new RecursoNoEncontradoExcepcion("Producto no encontrado: " + idProducto);
        }
        producto.setDescription(productoRecibido.getDescription());
        producto.setPrice(productoRecibido.getPrice());
        producto.setStock(productoRecibido.getStock());
        this.productoServicio.guardarProducto(producto);
        logger.info("Producto actualizado: " + producto);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/productos/{idProducto}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer idProducto){
        Producto producto = this.productoServicio.buscarProductoPorId(idProducto);
        if(producto == null){
            logger.info("Producto no encontrado");
            throw new RecursoNoEncontradoExcepcion("Producto no encontrado: " + idProducto);
        }
        this.productoServicio.eliminarProductoPorId(idProducto);
        logger.info("Producto eliminado: " + producto);
        return ResponseEntity.ok(null);
    }
}
