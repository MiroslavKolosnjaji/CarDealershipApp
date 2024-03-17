package cardealershipapp.client.ui.component.table;

import cardealershipapp.common.domain.PurchaseOrder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleTableImageLoader {

    private static final VehicleTableImageLoader instance = new VehicleTableImageLoader();

    private static final Logger log = LoggerFactory.getLogger(VehicleTableImageLoader.class);
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

        try {
            InputStream availableInputStream = getClass().getResourceAsStream("/cardealershipapp/client/ui/resources/images/available.png");
            InputStream soldInputStream = getClass().getResourceAsStream("/cardealershipapp/client/ui/resources/images/sold.png");

            assert availableInputStream != null;
            assert soldInputStream != null;

            BufferedImage availablePng = ImageIO.read(availableInputStream);
            BufferedImage soldPng = ImageIO.read(soldInputStream);

            BufferedImage[] image = {availablePng, soldPng};

            for (BufferedImage i : image) {
                images.add(new ImageIcon(i));
            }
        } catch (IOException ex) {
            log.error("Greska prilikom ucitavanja ikonica (available / sold) za vehicles tabelu: " + getClass().getSimpleName() + " : " + ex.getMessage());
        }

    }

    public JLabel getImages(Long id, List<PurchaseOrder> purchaseOrders) {
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
