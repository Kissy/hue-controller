package fr.kissy.hue_controller.interfaces;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.*;
import com.philips.lighting.model.rule.PHRule;
import com.philips.lighting.model.sensor.PHSensor;
import fr.kissy.hue_controller.model.ResourceIdentitifer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Collection;

@Controller
public class GroupController {

    @MessageMapping("/group/delete")
    public void delete(ResourceIdentitifer identitifer) {
        PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
        PHLightState newState = new PHLightState();
        newState.setOn(true);
        newState.setHue(0);
        newState.setSaturation(254);
        newState.setBrightness(254);
        selectedBridge.updateLightState(selectedBridge.getResourceCache().getLights().get("4"), newState);
    }

}