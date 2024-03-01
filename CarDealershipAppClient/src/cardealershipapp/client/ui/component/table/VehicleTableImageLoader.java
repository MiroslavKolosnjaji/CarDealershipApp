package cardealershipapp.client.ui.component.table;

import cardealershipapp.common.domain.PurchaseOrder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.midi.Soundbank;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
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
            ex.printStackTrace();
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
