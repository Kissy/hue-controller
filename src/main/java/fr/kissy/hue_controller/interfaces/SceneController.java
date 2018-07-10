package fr.kissy.hue_controller.interfaces;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import fr.kissy.hue_controller.infrastructure.SceneListener;
import fr.kissy.hue_controller.model.ResourceIdentitifer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SceneController {

    private SceneListener sceneListener;

    @Autowired
    public SceneController(SceneListener sceneListener) {
        this.sceneListener = sceneListener;
    }

    @MessageMapping("/scene/delete")
    public void delete(ResourceIdentitifer identitifer) {
        PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
        selectedBridge.deleteScene(identitifer.getIdentifier(), sceneListener);
    }

}