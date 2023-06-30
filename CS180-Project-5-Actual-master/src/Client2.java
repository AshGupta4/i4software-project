
import Client.PacketHandler;
import GUI.GuiWindow;

import javax.swing.*;

/**
 * Client2 - Duplicate of Client. Run this to test concurrency.
 *
 * @author Gabriel I.
 * @version 11/6/2021
 */

public class Client2 {
    public static void main(String[] args) {
        //Create packet handler
        PacketHandler packetHandler = new PacketHandler();

        //GUI TESTING
        SwingUtilities.invokeLater(new GuiWindow(packetHandler));
    }
}
