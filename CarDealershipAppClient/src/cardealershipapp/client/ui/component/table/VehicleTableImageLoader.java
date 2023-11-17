package cardealershipapp.client.ui.component.table;

import cardealershipapp.common.domain.PurchaseOrder;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class VehicleTableImageLoader {
    
    private static final VehicleTableImageLoader instance = new VehicleTableImageLoader();
    private final List<ImageIcon> images;
    private final JLabel labelImage;
    
    private VehicleTableImageLoader() {
        images = new ArrayList<>();
        labelImage = new JLabel();
        loadImages();
    }

    public static VehicleTableImageLoader getInstance() {
        return instance;
    }
    
    
    
     private void loadImages() {

        String[] image = {"src/cardealershipapp/client/ui/resources/available.png", "src/cardealershipapp/client/ui/resources/sold.png"};

        for (String i : image) {
            images.add(new ImageIcon(i));
        }
    }
     
     public  JLabel getImages(Long id, List<PurchaseOrder> purchaseOrders) {
        if (purchaseOrders != null && !purchaseOrders.isEmpty()) {

            for (PurchaseOrder po : purchaseOrders) {
                if (po.getVehicle().getId().equals(id)) {
                    labelImage.setIcon(images.get(1));
                    return labelImage;
                }
            }

            labelImage.setIcon(images.get(0));
        }
        return labelImage;
    }
    
    
}
